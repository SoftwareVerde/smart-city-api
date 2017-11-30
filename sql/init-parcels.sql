
CREATE TABLE parcels (
    id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    parcel_id VARCHAR(255),
    exempt_property_appraisal_land_value DECIMAL(14, 4) NULL,
    exempt_property_appraisal_building_value DECIMAL(14, 4) NULL,
    exempt_property_appraisal_total_value DECIMAL(14, 4) NULL,
    taxable_property_appraisal_land_value DECIMAL(14, 4) NULL,
    taxable_property_appraisal_building_value DECIMAL(14, 4) NULL,
    taxable_property_appraisal_total_value DECIMAL(14, 4) NULL,
    auditors_map_number VARCHAR(255) NULL,
    auditors_routing_sequence VARCHAR(255) NULL,
    appraisal_land_use_code VARCHAR(255) NULL,
    current_agricultural_use_value VARCHAR(255) NULL,
    school_district_code VARCHAR(255) NULL,
    contains_homestead_exemption_values BOOLEAN NULL,
    tax_bill_mailing_address_line1 VARCHAR(255) NULL,
    tax_bill_mailing_address_line2 VARCHAR(255) NULL,
    tax_bill_mailing_address_line3 VARCHAR(255) NULL,
    tax_bill_mailing_address_line4 VARCHAR(255) NULL,
    transfer_date DATE NULL,
    transfer_year INT UNSIGNED NULL,
    owner_name1 VARCHAR(255) NULL,
    owner_name2 VARCHAR(255) NULL,
    owner_name3 VARCHAR(255) NULL,
    owner_address1 VARCHAR(255) NULL,
    owner_address2 VARCHAR(255) NULL,
    auditors_neighborhood_code VARCHAR(255) NULL,
    flood_info VARCHAR(255) NULL,
    property_class VARCHAR(255) NULL,
    number_of_property_cards INT UNSIGNED NULL,
    acreage DECIMAL(14, 4) NULL,
    last_sale_price DECIMAL(14, 4) NULL,
    total_taxable_value DECIMAL(14, 4) NULL,
    city VARCHAR(255) NULL,
    state VARCHAR(255) NULL,
    zipcode VARCHAR(255) NULL,
    description_line1 VARCHAR(255) NULL,
    description_line2 VARCHAR(255) NULL,
    description_line3 VARCHAR(255) NULL,
    tax_designation VARCHAR(255) NULL,
    primary_building_area_sqft DECIMAL(14, 4) NULL,
    dwelling_type VARCHAR(255) NULL,
    total_room_count INT UNSIGNED NULL,
    full_bath_count INT UNSIGNED NULL,
    half_bath_count INT UNSIGNED NULL,
    bedroom_count INT UNSIGNED NULL,
    central_air_style VARCHAR(255) NULL,
    building_condition VARCHAR(255) NULL,
    has_fireplaces BOOLEAN NULL,
    building_grade VARCHAR(255) NULL,
    height VARCHAR(255) NULL,
    story_count DECIMAL(14, 4) NULL,
    year_built VARCHAR(255) NULL,
    property_type VARCHAR(255) NULL,
    wall_code VARCHAR(255) NULL,
    latitude DECIMAL(14, 4) NULL,
    longitude DECIMAL(14, 4) NULL,
    INDEX (parcel_id),
    INDEX (city),
    INDEX (acreage),
    INDEX (last_sale_price),
    INDEX (zipcode),
    INDEX (latitude),
    INDEX (longitude)
) ENGINE=InnoDB;

-- Unused columns from CSV
-- "STHNUM"
-- "STCONT"
-- "STHSFX"
-- "STDIRE"
-- "STNAME"
-- "STSFX"
-- "STADDR"
-- "VALID"
-- "CINBRHD"
-- "TIFMLND"
-- "TIFMBLD"

