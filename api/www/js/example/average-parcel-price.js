
var searchMap;

function initMap() {
    searchMap = new google.maps.Map(document.getElementById("search-map"), {
        zoom: 11,
        center: new google.maps.LatLng(40, -83),
        mapTypeId: 'roadmap',
        mapTypeControl: false
    });
}

function showLoadingScreen() {
    $('#loading-screen').show();
}

function hideLoadingScreen() {
    $('#loading-screen').hide();
}

function formatPrice(price) {
    return price.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function displayZipCodeData(data) {
    const zipCode = data.zipCode;
    const formattedPrice = formatPrice(data.averageSalePrice);

    $('#zip-code-data').html(
        '<div>'
        + '<span style="font-weight: bold;">Zip Code:</span> ' + zipCode + "<br/>"
        + '<span style="font-weight: bold;">Average Sale Price:</span> $' + formattedPrice
        + '</div>'
    );
}

function getColorForAverageSalePrice(averageSalePrice) {
    if (averageSalePrice < 100000) {
        return "#00FF00";
    } else if (averageSalePrice < 150000) {
        return "#44FF00";
    } else if (averageSalePrice < 200000) {
        return "#88FF00";
    } else if (averageSalePrice < 250000) {
        return "#BBFF00";
    } else if (averageSalePrice < 300000) {
        return "#FFFF00";
    } else if (averageSalePrice < 350000) {
        return "#FFBB00";
    } else if (averageSalePrice < 400000) {
        return "#FF8800";
    } else if (averageSalePrice < 450000) {
        return "#FF4400";
    } else if (averageSalePrice < 500000) {
        return "#FF0000";
    } else if (averageSalePrice < 1000000) {
        return "#BB0000";
    } else if (averageSalePrice < 2000000) {
        return "#880000";
    } else if (averageSalePrice < 5000000) {
        return "#440000";
    } else {
        return "#110000";
    }
}

function displayZipCodes() {
    $.post('/api/v1/parcels?average_sale_price=1', {}, function(data) {
        const averageSalePrices = data.averageSalePrices;

        searchMap.data.setStyle(function(feature) {
            const zipCode = feature.getProperty("GEOID10");

            let opacity = 0.0;
            let color = "#000000";
            let strokeWeight = 0;

            const averageSalePrice = averageSalePrices[zipCode];
            if (averageSalePrice) {
                opacity = 0.25;
                strokeWeight = 1;
                color = getColorForAverageSalePrice(averageSalePrice);
            }

            return ({
                fillColor: color,
                fillOpacity: opacity,
                strokeColor: "#000000",
                strokeWeight: strokeWeight
            });
        });

        searchMap.data.addListener('mouseover', function(event) {
            const feature = event.feature;

            const zipCode = feature.getProperty("GEOID10");
            const averageSalePrice = averageSalePrices[zipCode];
            if (averageSalePrice) {
                displayZipCodeData({
                    zipCode: zipCode,
                    averageSalePrice: averageSalePrice
                });
            }
        });

        searchMap.data.loadGeoJson("/example/average-parcel-price/data/franklinZipRegions.json");

        hideLoadingScreen();
    });
}

$(function() {
    displayZipCodes();
});

