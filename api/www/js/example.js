var searchMap;
var searchRadiusCircle;
var markers = [];

function initMap() {
    const searchRadius = parseInt(document.getElementById('radius').value);
    const latitude = document.getElementById('latitude').value;
    const longitude = document.getElementById('longitude').value;

    const columbus = new google.maps.LatLng(latitude, longitude);

    searchMap = new google.maps.Map(document.getElementById("search-map"), {
        zoom: 14,
        center: columbus,
        mapTypeId: 'roadmap',
        mapTypeControl: false
    });

    searchRadiusCircle = new google.maps.Circle({
        strokeColor: '#FF0000',
        strokeOpacity: 0.8,
        strokeWeight: 1,
        fillColor: '#FF0000',
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

function renderSearchCircle() {
    const searchRadius = parseInt(document.getElementById('radius').value);
    const latitude = document.getElementById('latitude').value;
    const longitude = document.getElementById('longitude').value;

    const newCenter = new google.maps.LatLng(latitude, longitude);

    searchRadiusCircle.setCenter(newCenter);
    searchRadiusCircle.setRadius(searchRadius);
}

function clearMarkers() {
    for (let i in markers) {
        const marker = markers[i];
        // remove from map
        marker.setMap(null);
    }
    markers = [];
}

function addMarker(marker) {
    marker.setMap(searchMap);
    markers.push(marker);
}

function findParkingMeters(formData, callbackFunction) {
    $.post('/api/v1/parking-meters?search=1', formData, function (data) {
        for (let i in data.parkingMeters) {
            const parkingMeter = data.parkingMeters[i];
            $('#parking-meters').append("<div class=\"result\">"
                                                + "Meter " + parkingMeter.meterId
                                                + "<br/>"
                                                + (parkingMeter.isHandicap ? "Handicapped<br/>" : "")
                                                + (parkingMeter.isChargingStation ? "Charging Station<br/>" : "")
                                                + parkingMeter.location
                                                + "<br/>"
                                                + "Max Time: " + parkingMeter.maxDwellDuration + " min"
                                                + "<br/>"
                                                + "Rate (Full): $" + (parkingMeter.rateTimes100 / 100).toFixed(2)
                                            + "</div>");
            // add marker
            const meterMarker = new google.maps.Marker({
                position: new google.maps.LatLng(parkingMeter.latitude, parkingMeter.longitude),
                label: "M"
            });
            addMarker(meterMarker);
        }
        if (typeof callbackFunction == "function") {
            callbackFunction();
        }
    });
}

function findParkingTickets(formData, callbackFunction) {
    $.post('/api/v1/parking-tickets?search=1', formData, function (data) {
        for (let i in data.parkingTickets) {
            const parkingTicket = data.parkingTickets[i];
            $('#parking-tickets').append("<div class=\"result\">"
                                                + "Ticket " + parkingTicket.ticketNumber
                                                + "<br/>"
                                                + parkingTicket.licensePlate.number
                                                + "<br/>"
                                                + parkingTicket.location
                                                + "<br/>"
                                                + "Fine Amount: $" + parkingTicket.fineAmount
                                                + "<br/>"
                                                + "Paid Amount: $" + parkingTicket.paidAmount
                                                + "<br/>"
                                                + "Due Amount: $" + parkingTicket.dueAmount
                                                + "<br/>"
                                                + "Disposition: " + parkingTicket.disposition
                                                + "<br/>"
                                            + "</div>");
            // add marker
            const meterMarker = new google.maps.Marker({
                position: new google.maps.LatLng(parkingTicket.latitude, parkingTicket.longitude),
                label: "T"
            });
            addMarker(meterMarker);
        }
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

        setSearchButtonHtml("<i class=\"fa fa-spin fa-refresh\"></i>");

        const includeMeters = $('#include-meters').is(':checked');
        const includeTickets = $('#include-tickets').is(':checked');

        // clear current results
        $('#parking-meters').html('');
        $('#parking-tickets').html('');
        clearMarkers();

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