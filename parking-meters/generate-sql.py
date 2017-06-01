from dateutil.parser import parse
import csv
import re

def dms2dd(degrees, minutes, seconds, direction):
    dd = float(degrees) + float(minutes)/60 + float(seconds)/(60*60);
    if direction == 'S' or direction == 'W':
        dd *= -1
    return dd;

def dd2dms(deg):
    d = int(deg)
    md = abs(deg - d) * 60
    m = int(md)
    sd = (md - m) * 60
    return [d, m, sd]

def parse_dms(dms):
    parts = re.split('[^\d\w.]+', dms)
    lat = dms2dd(parts[0].strip(), parts[1].strip(), parts[2].strip(), parts[3].strip())
    return (lat)

def create_query(row):
    object_id = row[0].strip()
    meter_id = row[1].strip()
    coin_collect = row[2].strip()
    key_code = row[3].strip()
    label_angle = row[4].strip()
    location = row[5].strip()
    side_of_street = row[6].strip()
    blockface = row[7].strip()
    meter_status = row[8].strip()
    meter_type = row[9].strip()
    tow_away_hours = row[10].strip()
    meter_time = row[11].strip()
    handicap = row[12].strip()
    hours_operation = row[13].strip()
    in_service = row[14].strip()
    valet_hours = row[15].strip()
    rate = row[16].strip()
    food_service_hours = row[17].strip()
    tax_zone_hours = row[18].strip()
    charging_station = row[19].strip()
    charging_station_status = row[20].strip()
    lng = row[21].strip()
    lat = row[22].strip()

    rate = re.sub("[^0-9\.]", "", rate)
    if len(rate) < 1:
        rate = "0.00"

    charging_station = "1" if charging_station == "Yes" else "0"
    handicap = "1" if handicap == "1" else "0"

    if len(lat) > 0 and len(lng) > 0:
        lat = parse_dms(lat)
        lng = parse_dms(lng)
    else:
        lat = "NULL"
        lng = "NULL"

    if len(meter_time) < 1:
        meter_time = 24 * 60

    query = "INSERT INTO parking_meters (meter_id, location, max_dwell_duration, is_handicap, rate, is_charging_station, latitude, longitude) VALUES ('{}', '{}', '{}', '{}', '{}', '{}', {}, {});"
    return query.format(meter_id, location, meter_time, handicap, rate, charging_station, lat, lng)
    

with open('meters.csv', 'rb') as csvfile:
    csv_reader = csv.reader(csvfile, delimiter=',', quotechar="\"")
    for row in csv_reader:
        print(create_query(row))

