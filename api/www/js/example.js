
const TICKET_COLOR_MAX = 30.0;

var searchMap;
var searchRadiusCircle;
var markers = [];
var squares = [];

function initMap() {
    const searchRadius = getRadius();
    const latitude = getLatitude();
    const longitude = getLongitude();

    const columbus = new google.maps.LatLng(latitude, longitude);

    searchMap = new google.maps.Map(document.getElementById("search-map"), {
        zoom: 14,
        center: columbus,
        mapTypeId: 'roadmap',
        mapTypeControl: false
    });

    searchRadiusCircle = new google.maps.Circle({
        strokeColor: '#333333',
        strokeOpacity: 0.4,
        strokeWeight: 1,
        fillColor: '#333333',
        fillOpacity: 0.1,
        map: searchMap,
        center: columbus,
        editable: true,
        draggable: true,
        radius: searchRadius
    });

    searchMap.addListener('click', function (event) {
        const clickLocation = event.latLng;
        // recenter search circle
        searchRadiusCircle.setCenter(clickLocation);
        searchRadiusCircle.setMap(searchMap)
        // update inputs
        const latitude = clickLocation.lat();
        const longitude = clickLocation.lng();
        document.getElementById('latitude').value = latitude;
        document.getElementById('longitude').value = longitude;
    });

    searchRadiusCircle.addListener('radius_changed', function (event) {
        const newRadius = searchRadiusCircle.getRadius();
        document.getElementById('radius').value = newRadius;
    });

    searchRadiusCircle.addListener('center_changed', function (event) {
        const newCenter = searchRadiusCircle.getCenter();
        const newLatitude = newCenter.lat();
        const newLongitude = newCenter.lng();
        document.getElementById('latitude').value = newLatitude;
        document.getElementById('longitude').value = newLongitude;
    });
}

function getRadius() {
    const searchRadius = parseFloat(document.getElementById('radius').value);
    return searchRadius;
}

function getLatitude() {
    const latitude = parseFloat(document.getElementById('latitude').value);
    return latitude;
}

function getLongitude() {
    const longitude = parseFloat(document.getElementById('longitude').value);
    return longitude;
}

function renderSearchCircle() {
    const searchRadius = getRadius();
    const latitude = getLatitude();
    const longitude = getLongitude();

    const newCenter = new google.maps.LatLng(latitude, longitude);

    searchRadiusCircle.setCenter(newCenter);
    searchRadiusCircle.setRadius(searchRadius);

    searchRadiusCircle.setMap(searchMap);
}

function clearMap() {
    for (let i in markers) {
        const marker = markers[i];
        // remove from map
        marker.setMap(null);
    }
    for (let i in squares) {
        const square = squares[i];
        // remove from map
        square.setMap(null);
    }
    markers = [];
    squares = [];
}

function addMarker(marker) {
    marker.setMap(searchMap);
    markers.push(marker);
}

function addSquare(square) {
    square.setMap(searchMap);
    squares.push(square);
}

function getParkingMeterIcon(parkingMeter) {
    let url = '/img/black-dot.svg';
    if (parkingMeter.isHandicap) {
        url = '/img/blue-dot.svg';
    } else if (parkingMeter.isChargingStation) {
        url = '/img/yellow-dot.svg';
    }

    return {
        url: url,
        scaledSize: new google.maps.Size(5, 5)
    };
}

function findParkingMeters(formData, callbackFunction) {
    $.post('/api/v1/parking-meters?search=1', formData, function (data) {
        for (let i in data.parkingMeters) {
            const parkingMeter = data.parkingMeters[i];
            // add marker
            const icon = getParkingMeterIcon(parkingMeter);

            const infoWindow = new google.maps.InfoWindow({
                content: "<div class=\"info-text\">"
                        + "Meter " + parkingMeter.meterId + "<br/>"
                        + (parkingMeter.isHandicap ? "(Handicap)<br/>" : "")
                        + (parkingMeter.isChargingStation ? "(Charging Station)<br/>" : "")
                        + "Max Time: " + parkingMeter.maxDwellDuration + " min<br/>"
                        + "Cost (Full): $" + (parkingMeter.rateTimes100 / 100.0).toFixed(2)
                        + "</div>"
            });

            const meterMarker = new google.maps.Marker({
                position: new google.maps.LatLng(parkingMeter.latitude, parkingMeter.longitude),
                icon: icon,
                clickable: true
            });

            meterMarker.addListener('click', function() {
                infoWindow.open(searchMap, meterMarker);
            });

            addMarker(meterMarker);
        }
        if (typeof callbackFunction == "function") {
            callbackFunction();
        }
    });
}

function determineSquareColor(ticketSquare) {
    const ticketCount = ticketSquare.tickets.length;
    return getColorForPercentage(Math.min(ticketCount, TICKET_COLOR_MAX) / TICKET_COLOR_MAX);
}

function drawSquares(ticketSquares) {
    for (let i in ticketSquares) {
        const ticketSquare = ticketSquares[i];

        const ticketCount = ticketSquare.tickets.length;
        if (ticketCount > 0) {
            const color = determineSquareColor(ticketSquare);

            const totalFined = ticketSquare.tickets.reduce(function (acc, value) {
                return acc + parseFloat(value.fineAmount);
            }, 0.0);

            const infoText = ticketCount + " tickets, totaling $" + totalFined.toFixed(2);
            const infoWindow = new google.maps.InfoWindow({
                content: "<div class=\"info-text\">"
                        + infoText
                        + "</div>"
            });

            const mapSquare = new google.maps.Rectangle({
                strokeColor: color,
                strokeOpacity: 0.35,
                strokeWeight: 0,
                fillColor: color,
                fillOpacity: 0.35,
                clickable: true,
                bounds: {
                    north: ticketSquare.north,
                    south: ticketSquare.south,
                    west: ticketSquare.west,
                    east: ticketSquare.east
                }
            });

            infoWindow.setPosition({
                lat: ticketSquare.north,
                lng: (ticketSquare.east + ticketSquare.west) / 2
            });

            mapSquare.addListener('click', function() {
                infoWindow.open(searchMap);
            });

            addSquare(mapSquare);
        }
    }
}

function getTicketMapSegments() {
    return parseInt(document.getElementById('ticket-map-segments').value);
}

function findParkingTickets(formData, callbackFunction) {
    $.post('/api/v1/parking-tickets?search=1', formData, function (data) {
        const radius = getRadius();
        const centerLatitude = getLatitude();
        const centerLongitude = getLongitude();

        const ticketMapSegments = getTicketMapSegments();

        const startLatitude = addMetersToLatitude(centerLatitude, centerLongitude, radius);
        const endLatitude = addMetersToLatitude(centerLatitude, centerLongitude, -radius);
        const latitudeDifferential = (endLatitude - startLatitude) / ticketMapSegments;

        const startLongitude = addMetersToLongitude(centerLatitude, centerLongitude, -radius);
        const endLongitude = addMetersToLongitude(centerLatitude, centerLongitude, radius);
        const longitudeDifferential = (endLongitude - startLongitude) / ticketMapSegments;

        const ticketSquares = [];
        for (let i in data.parkingTickets) {
            const parkingTicket = data.parkingTickets[i];

            const latitudeIndex = Math.floor((parkingTicket.latitude - startLatitude) / latitudeDifferential);
            const longitudeIndex = Math.floor((parkingTicket.longitude - startLongitude) / longitudeDifferential);

            const index = latitudeIndex + ticketMapSegments * longitudeIndex;

            const latitude = startLatitude + latitudeIndex * latitudeDifferential;
            const longitude = startLongitude + longitudeIndex * longitudeDifferential;

            if (!ticketSquares[index]) {
                ticketSquares[index] = {
                    north: latitude,
                    south: latitude + latitudeDifferential,
                    west: longitude,
                    east: longitude + longitudeDifferential,
                    tickets: []
                };
            }
            ticketSquares[index].tickets.push(parkingTicket);
        }

        drawSquares(ticketSquares);

        if (typeof callbackFunction == "function") {
            callbackFunction();
        }
    });
}

function setSearchButtonHtml(html) {
    $('#search-button').html(html);
}

$(function () {
    $('#radius').change(function() {
        renderSearchCircle();
    });
    $('#latitude').change(function() {
        renderSearchCircle();
    });
    $('#longitude').change(function() {
        renderSearchCircle();
    });

    $('#search-form').submit(function (event) {
        // prevent standard form submission
        event.preventDefault();

        // temporarily stop displaying the circle
        searchRadiusCircle.setMap(null);

        setSearchButtonHtml("<i class=\"fa fa-spin fa-refresh\"></i>");

        const includeMeters = $('#include-meters').is(':checked');
        const includeTickets = $('#include-tickets').is(':checked');

        // clear current results
        $('#parking-meters').html('');
        $('#parking-tickets').html('');
        clearMap();

        // perform search
        const formData = $('#search-form').serialize();
        const dataLoaded = {};
        dataLoaded.meters = !includeMeters;
        dataLoaded.tickets = !includeTickets;
        // meters
        if (includeMeters) {
            findParkingMeters(formData, function() {
                dataLoaded.meters = true;
                if (dataLoaded.tickets) {
                    setSearchButtonHtml("Search");
                }
            });
        }
        // tickets
        if (includeTickets) {
            findParkingTickets(formData, function() {
               dataLoaded.tickets = true;
               if (dataLoaded.meters) {
                   setSearchButtonHtml("Search");
               }
           });
        }

        $('#results-container').css('visibility', 'visible');
    });
});