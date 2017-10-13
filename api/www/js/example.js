$(function () {
    // prevent standard form submission
    $('#search-form').submit(function (event) {
        event.preventDefault();

        // clear current results
        $('#results-container').html('');

        // perform search
        const formData = $('#search-form').serialize();
        $.post('/api/v1/parking-meters?search=1', formData, function (data) {
            for (let i in data.parkingMeters) {
                const parkingMeter = data.parkingMeters[i];
                $('#results-container').append("<div class=\"result\">"
                                                    + parkingMeter.meterId
                                                    + "<br/>"
                                                    + (parkingMeter.isHandicap ? "Handicapped<br/>" : "")
                                                    + (parkingMeter.isChargingStation ? "Charging Station<br/>" : "")
                                                    + parkingMeter.location
                                                    + "<br/>"
                                                    + "Max Time: " + parkingMeter.maxDwellDuration + " min"
                                                    + "<br/>"
                                                    + "Rate (Full): $" + (parkingMeter.rateTimes100 / 100).toFixed(2)
                                                + "</div>");
            }
        });
    });
});