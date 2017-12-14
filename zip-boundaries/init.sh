#!/bin/bash

wget https://www2.census.gov/geo/tiger/TIGER2017/ZCTA5/tl_2017_us_zcta510.zip
unzip tl_2017_us_zcta510.zip
brew install gdal
ogr2ogr -f "GeoJSON" -lco COORDINATE_PRECISION=4 -simplify 0.00010 zipRegions.json tl_2017_us_zcta510.shp

