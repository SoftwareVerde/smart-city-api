from dateutil.parser import parse
import sys
import re
import csv
import MySQLdb
import requests

def find_closest_meter_id(lat, lng):
    global connection

    cursor = connection.cursor()
    cursor.execute("SELECT id, latitude, longitude, (POWER(latitude - %s, 2) + POWER(longitude - %s, 2)) AS distance FROM parking_meters WHERE latitude IS NOT NULL AND longitude IS NOT NULL HAVING distance < 0.001 ORDER BY distance ASC", (lat, lng))
    row = cursor.fetchone()
    if (row is not None):
        row_id = row[0]
        latitude = row[1]
        longitude = row[2]
        distance = row[3]
        cursor.close()

        # print("{}: ({}, {}) - ({}, {}) -> {}".format(row_id, lat, lng, latitude, longitude, distance))
        return row_id
    else:
        cursor.close()
        return None


def levenshtein(s1, s2):
    if len(s1) < len(s2):
        return levenshtein(s2, s1)

    # len(s1) >= len(s2)
    if len(s2) == 0:
        return len(s1)

    previous_row = range(len(s2) + 1)
    for i, c1 in enumerate(s1):
        current_row = [i + 1]
        for j, c2 in enumerate(s2):
            insertions = previous_row[j + 1] + 1 # j+1 instead of j since previous_row and current_row are one character longer
            deletions = current_row[j] + 1       # than s2
            substitutions = previous_row[j] + (c1 != c2)
            current_row.append(min(insertions, deletions, substitutions))
        previous_row = current_row
    
    return previous_row[-1]

def is_number(s):
    try:
        int(s)
        return True
    except ValueError:
        return False

def geocode_address(location):
    api_token="AIzaSyACxY0gd_t0J3lrT3Da77_ExWaGT2nChZQ"
    r = requests.get("https://maps.googleapis.com/maps/api/geocode/json?address="+ location +"&key="+ api_token)
    json = r.json()
    if (len(json['results']) == 0):
        return (None, None)
    return (json['results'][0]['geometry']['location']['lat'], json['results'][0]['geometry']['location']['lng'])

connection = MySQLdb.connect(user='root', passwd='absolution', host='127.0.0.1', db='smart_city_api')

def create_query(row):
    global connection

    date = parse(row[0].strip()).strftime('%Y-%m-%d');
    ticket_number = row[1].strip()
    plate_number = row[2].strip()
    plate_state = row[3].strip()
    original_location = row[4].strip()
    violation_code = row[5].strip()
    total_fine = row[6].strip()
    fine_paid = row[7].strip()
    amount_due = row[8].strip()
    disposition = row[9].strip()

    if (len(total_fine) < 1):
        total_fine = "0.00"
    if (len(fine_paid) < 1):
        fine_paid = "0.00"
    if (len(amount_due) < 1):
        amount_due = "0.00"

    cursor = connection.cursor()
    cursor.execute("SELECT id FROM parking_tickets WHERE ticket_number = %s", (ticket_number, ))
    row = cursor.fetchone()
    if (row is not None): # Ticket already processed...
        cursor.close()
        return

    latlng = geocode_address(original_location)
    latitude = latlng[0]
    longitude = latlng[1]

    cursor = connection.cursor()
    cursor.execute("SELECT id FROM license_plates WHERE number = %s AND state = %s", (plate_number, plate_state))
    row = cursor.fetchone()
    if (row == None):
        cursor.execute("INSERT INTO license_plates (number, state) VALUES (%s, %s)", (plate_number, plate_state))
        license_plate_id = cursor.lastrowid
        connection.commit()
    else:
        license_plate_id = row[0]
    cursor.close()

    query = "INSERT INTO parking_tickets (date, ticket_number, license_plate_id, violation_code, fine_amount, paid_amount, due_amount, disposition, latitude, longitude) VALUES ('{}', '{}', '{}', '{}', '{}', '{}', '{}', '{}', '{}', '{}');"
    print(query.format(date, ticket_number, license_plate_id, violation_code, total_fine, fine_paid, amount_due, disposition, latitude, longitude))

    cursor = connection.cursor()
    cursor.execute("INSERT INTO parking_tickets (date, ticket_number, license_plate_id, violation_code, fine_amount, paid_amount, due_amount, disposition, latitude, longitude) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", (date, ticket_number, license_plate_id, violation_code, total_fine, fine_paid, amount_due, disposition, latitude, longitude))
    connection.commit()
    cursor.close()

with open('tickets.csv', 'rb') as csvfile:
    max_count = 50000
    i = 0
    skip_to = 0

    csv_reader = csv.reader(csvfile, delimiter=';')
    for row in csv_reader:
        if (i < skip_to):
            i += 1
            continue
        if (i - skip_to >= max_count):
            print("Max Count Reached: "+ i)
            break
        create_query(row)
        i += 1

connection.close()

