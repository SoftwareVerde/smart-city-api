from dateutil.parser import parse
from osgeo import ogr, osr
import csv

SOURCE_DATUM = osr.SpatialReference()
SOURCE_DATUM.ImportFromEPSG(102723) # NAD 1983 StatePlane Ohio South FIPS 3402 Feet

TARGET_DATUM = osr.SpatialReference()
TARGET_DATUM.ImportFromEPSG(4326) # WGS 84

# Transformation
POINT_CONVERSION = osr.CoordinateTransformation(SOURCE_DATUM, TARGET_DATUM)

# Returns a tuple (lat, long)
def convert_coordinates(x, y):
    point = ogr.Geometry(ogr.wkbPoint)
    point.AddPoint(x, y)
    point.Transform(POINT_CONVERSION)
    return (point.GetY(), point.GetX())

def sql_sanitize(value):
    if (isinstance(value, basestring)):
        if (len(value) == 0):
            return "NULL"
        escaped = value.replace("\\", "\\\\");
        escaped = escaped.replace("'", "\\'");
        # add quotes
        return "'%s'" % escaped
    return value

def convert_date(value):
    # yyyy-mm-dd hh:mm:ss format, because strftime doesn't support years before 1900
    date = parse(value).isoformat(" ").split(".")[0]
    return date

def convert_int(value):
    try:
        return int(value)
    except:
        return ""

def convert_float(value):
    try:
        return float(value)
    except:
        return ""

def convert_boolean(value):
    intValue = convert_int(value)
    if (isinstance(intValue, int)):
        if (intValue > 0):
            return 1
        else:
            return 0
    return intValue

def create_query(row):
    parcel_id = row[0].strip()
    exempt_land_value = convert_float(row[1].strip())
    exempt_building_value = convert_float(row[2].strip())
    exempt_total_value = convert_float(row[3].strip())
    taxable_land_value = convert_float(row[4].strip())
    taxable_building_value = convert_float(row[5].strip())
    taxable_total_value = convert_float(row[6].strip())
    map_number = row[7].strip()
    routing_sequence = row[8].strip()
    land_use_code = row[9].strip()
    agricultural_use_value = row[10].strip()
    school_district_code = row[11].strip()
    contains_homestead_exemption_values = convert_boolean(row[12].strip())
    tax_bill_mailing_address_line1 = row[13].strip()
    tax_bill_mailing_address_line2 = row[14].strip()
    tax_bill_mailing_address_line3 = row[15].strip()
    tax_bill_mailing_address_line4 = row[16].strip()
    transfer_date = convert_date(row[17].strip())
    transfer_year = convert_int(row[18].strip())
    owner_name1 = row[19].strip()
    owner_name2 = row[20].strip()
    owner_name3 = row[21].strip()
    owner_address1 = row[22].strip()
    owner_address2 = row[23].strip()
    neighborhood_code = row[24].strip()
    flood_info = row[25].strip()
    property_class = row[26].strip()
    number_of_property_cards = convert_int(row[27].strip())
    acreage = convert_float(row[28].strip())
    last_sale_price = convert_float(row[29].strip())
    total_taxable_value = convert_float(row[30].strip())
    city = row[38].strip()
    state = row[39].strip()
    zipcode = row[40].strip()
    description_line1 = row[41].strip()
    description_line2 = row[42].strip()
    description_line3 = row[43].strip()
    tax_designation = row[44].strip()
    primary_building_area_sqft = convert_float(row[46].strip())
    dwelling_type = row[47].strip()
    total_room_count = convert_int(row[48].strip())
    full_bath_count = convert_int(row[49].strip())
    half_bath_count = convert_int(row[50].strip())
    bedroom_count = convert_int(row[51].strip())
    central_air_style = row[52].strip()
    building_condition = row[55].strip()
    has_fireplaces = convert_boolean(row[55].strip())
    building_grade = row[56].strip()
    height = row[57].strip()
    story_count = convert_float(row[58].strip())
    year_built = row[59].strip()
    property_code = row[60].strip()
    wall_code = row[61].strip()
    point_x = row[64].strip()
    point_y = row[65].strip()

    lat = None;
    lng = None;
    if len(point_x) > 0 and len(point_y) > 0:
        gps = convert_coordinates(float(point_x), float(point_y))
        lat = gps[0]
        lng = gps[1]
    else:
        lat = "NULL"
        lng = "NULL"

    # initial NULL values is for ID column (mysql will use auto increment value)
    # this avoids the need to list all the column (since we will be inserting into all of them)
    query = "INSERT INTO parcels VALUES (NULL, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {});"
    return query.format(
        sql_sanitize(parcel_id),
        sql_sanitize(exempt_land_value),
        sql_sanitize(exempt_building_value),
        sql_sanitize(exempt_total_value),
        sql_sanitize(taxable_land_value),
        sql_sanitize(taxable_building_value),
        sql_sanitize(taxable_total_value),
        sql_sanitize(map_number),
        sql_sanitize(routing_sequence),
        sql_sanitize(land_use_code),
        sql_sanitize(agricultural_use_value),
        sql_sanitize(school_district_code),
        sql_sanitize(contains_homestead_exemption_values),
        sql_sanitize(tax_bill_mailing_address_line1),
        sql_sanitize(tax_bill_mailing_address_line2),
        sql_sanitize(tax_bill_mailing_address_line3),
        sql_sanitize(tax_bill_mailing_address_line4),
        sql_sanitize(transfer_date),
        sql_sanitize(transfer_year),
        sql_sanitize(owner_name1),
        sql_sanitize(owner_name2),
        sql_sanitize(owner_name3),
        sql_sanitize(owner_address1),
        sql_sanitize(owner_address2),
        sql_sanitize(neighborhood_code),
        sql_sanitize(flood_info),
        sql_sanitize(property_class),
        sql_sanitize(number_of_property_cards),
        sql_sanitize(acreage),
        sql_sanitize(last_sale_price),
        sql_sanitize(total_taxable_value),
        sql_sanitize(city),
        sql_sanitize(state),
        sql_sanitize(zipcode),
        sql_sanitize(description_line1),
        sql_sanitize(description_line2),
        sql_sanitize(description_line3),
        sql_sanitize(tax_designation),
        sql_sanitize(primary_building_area_sqft),
        sql_sanitize(dwelling_type),
        sql_sanitize(total_room_count),
        sql_sanitize(full_bath_count),
        sql_sanitize(half_bath_count),
        sql_sanitize(bedroom_count),
        sql_sanitize(central_air_style),
        sql_sanitize(building_condition),
        sql_sanitize(has_fireplaces),
        sql_sanitize(building_grade),
        sql_sanitize(height),
        sql_sanitize(story_count),
        sql_sanitize(year_built),
        sql_sanitize(property_code),
        sql_sanitize(wall_code),
        lat,
        lng
    );

with open('parcels_2017-07.csv', 'rb') as csvfile:
    csv_reader = csv.reader(csvfile, delimiter=',', quotechar="\"")
    for row in csv_reader:
        sql = create_query(row);
        print(sql)

