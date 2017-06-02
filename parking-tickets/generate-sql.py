from dateutil.parser import parse
import sys
import re
import csv
import MySQLdb

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

connection = MySQLdb.connect(user='root', passwd='absolution', host='127.0.0.1', db='smart_city_api')

def create_query(row):
    global connection

    date = parse(row[0].strip()).strftime('%Y-%m-%d');
    ticket_number = row[1].strip()
    plate_number = row[2].strip()
    plate_state = row[3].strip()
    location = row[4].strip()
    violation_code = row[5].strip()
    total_fine = row[6].strip()
    fine_paid = row[7].strip()
    amount_due = row[8].strip()
    disposition = row[9].strip()

    match = re.search('^([0-9]+ )(.*)', location, re.IGNORECASE)
    if (match != None):
        first_word = match.group(1)
        location = match.group(2).strip()

    cursor = connection.cursor()
    cursor.execute("SELECT id, location FROM parking_meters")
    row = cursor.fetchone()
    best_match = None
    best_match_location = None
    best_match_score = sys.maxint
    while (row != None):
        row_id = row[0]
        row_location = row[1]
        score = levenshtein(row_location, location)
        if (score < best_match_score):
            best_match_score = score
            best_match = row_id
            best_match_location = row_location
        row = cursor.fetchone()

    print("{} matched to {} (score: {})".format(location, best_match_location, best_match_score))

    if (best_match is None):
        print("No match for: "+ location)
        cursor.close()
        return None

    parking_meter_id = best_match
    cursor.close()

    cursor = connection.cursor()
    cursor.execute("SELECT id FROM license_plates WHERE number = %s AND state = %s", (plate_number, plate_state))
    row = cursor.fetchone()
    if (row == None):
        cursor.execute("INSERT INTO license_plates (number, state) VALUES (%s, %s)", (plate_number, plate_state))
        license_plate_id = cursor.lastrowid
    else:
        license_plate_id = row[0]

    cursor.close()

    query = "INSERT INTO parking_tickets (date, ticket_number, license_plate_id, parking_meter_id, violation_code, fine_amount, paid_amount, due_amount, disposition) VALUES ('{}', '{}', '{}', '{}', '{}', '{}', '{}', '{}', '{}');"
    return query.format(date, ticket_number, license_plate_id, parking_meter_id, violation_code, total_fine, fine_paid, amount_due, disposition)
    

with open('tickets.csv', 'rb') as csvfile:
    csv_reader = csv.reader(csvfile, delimiter=';')
    for row in csv_reader:
        create_query(row)
        # print(create_query(row))

connection.close()
