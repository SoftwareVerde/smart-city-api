# Smart City API - Parking Data

This project provides methods for viewing and accessing parking data.
Currently, there are APIs for parking meters and parking tickets across the
Columbus metropolitan area.

The data used was provided by the city of Columbus, OH for 2016.

If you are only interested in seeing what this project offers, you can view
Software Verde-hosted version at:

    https://smart-city.softwareverde.com

## Getting Started (Local Installation)
    1. Create the database schema: smart_city_api.
    2. Source sql/init.sql
    3. Source sql/smart-city-api.sql
    4. cd api
    5. In api/www/example/index.html replace "YOUR_API_KEY" in the googleapis
        script src to an API key you own.
    6. ./scripts/make.sh
    7. ./scripts/run-jar.sh
    8. Navigate to localhost:8080

