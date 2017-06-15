function downloadServerTime(callback) {
    $.post(
        "/api/server/time/?select=1",
        {},
        function(data) {
            if (typeof callback == "function") {
                callback(data);
            }
        }
    ).fail(function(data) {
        if (typeof callback == "function") {
            callback(JSON.parse(data.responseText || "{}"));
        }
    });
}

function downloadDatabaseTime(callback) {
    $.post(
        "/api/database/time/?select=1",
        {},
        function(data) {
            if (typeof callback == "function") {
                callback(data);
            }
        }
    ).fail(function(data) {
        if (typeof callback == "function") {
            callback(JSON.parse(data.responseText || "{}"));
        }
    });
}

function updateServerTimeUi(data) {
    var timeDiv = $("#server-time");
    var formattedTimeDiv = $("#server-formatted-time");

    if (! data.wasSuccess) {
        timeDiv.toggleClass("error", true);
        timeDiv.text("");

        formattedTimeDiv.toggleClass("error", true);
        formattedTimeDiv.text(data.errorMessage);
    }
    else {
        timeDiv.toggleClass("error", false);
        timeDiv.html(data.serverTime);

        formattedTimeDiv.toggleClass("error", false);
        formattedTimeDiv.text(data.formattedServerTime);
    }
}

function updateDatabaseTimeUi(data) {
    var timeDiv = $("#database-time");
    var formattedTimeDiv = $("#database-formatted-time");

    if (! data.wasSuccess) {
        timeDiv.toggleClass("error", true);
        timeDiv.text("");

        formattedTimeDiv.toggleClass("error", true);
        formattedTimeDiv.text(data.errorMessage);
    }
    else {
        timeDiv.toggleClass("error", false);
        timeDiv.html(data.databaseTime);

        formattedTimeDiv.toggleClass("error", false);
        formattedTimeDiv.text(data.formattedDatabaseTime);
    }
}

$(window).on("load", function() {
    setInterval(
        function() {
            downloadServerTime(function(data) {
                updateServerTimeUi(data);
            });

            downloadDatabaseTime(function(data) {
                updateDatabaseTimeUi(data);
            });
        },
        1000
    );
});

