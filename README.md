# Smart City API - Franklin County, Ohio Data

This project provides methods for viewing and accessing parking data.
Currently, there are APIs for parking meters and parking tickets across the
Columbus metropolitan area.  There is also parcel data from the Franklin
County Auditor's website.

The data used was provided by the city of Columbus, OH for 2016 (parking
tickets and meters) and 2017 (parcels).

If you are only interested in seeing what this project offers, you can view
Software Verde-hosted version at:

    https://smart-city.softwareverde.com

## Getting Started (Local Installation)
    1. Create the database schema: smart_city_api.
    2. Source sql/init.sql
    3. Source sql/smart-city-api.sql
    4. Source sql/init-parcels.sql
    5. Unzip parcels/parcels.zip and source parcels/parcels.sql
    6. Source sql/parcels_indexes.sql
    7. Create a mysql user and grant SELECT privileges on smart_city_api.*
    8. cd api
    9. Configure your database information in conf/server.conf
    10. In api/www/example/index.html replace "YOUR_API_KEY" in the googleapis
        script src to an API key you own.
    11. ./scripts/make.sh
    12. ./scripts/run-jar.sh
    13. Navigate to http://localhost:8080/

