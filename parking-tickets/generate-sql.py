from dateutil.parser import parse
import csv

import mysql.connector
connection = mysql.connector.connect(user='root', password='absolution', host='127.0.0.1', database='smart_city_api')

def create_query(row):
    global connection

    date_string = parse(row[0].strip()).strftime('%Y-%m-%d');
    ticket_number = row[1].strip()
    plate_number = row[2].strip()
    plate_state = row[3].strip()
    location = row[4].strip()
    violation_code = row[5].strip()
    total_fine = row[6].strip()
    fine_paid = row[7].strip()
    amount_due = row[8].strip()
    disposition = row[9].strip()

    cursor = connection.cursor()
    cursor.execute("SELECT id FROM parking_meters WHERE location = %s", (location,))

    for (id) in cursor:
        print(id)

    cursor.close()

    query = "INSERT INTO parking_tickets (date, ticket_number, license_plate_id, meter_id, violation_code, fine_amount, paid_amount, due_amount, disposition)"
    # return ', '.join(row)
    return query
    

with open('tickets.csv', 'rb') as csvfile:
    csv_reader = csv.reader(csvfile, delimiter=';')
    for row in csv_reader:
        print(create_query(row))

connection.close()
