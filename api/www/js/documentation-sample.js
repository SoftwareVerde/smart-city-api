$(function() {
    $('#sample-request-button').click(function() {
        const url = $('#sample-request-url').val();
        const requestData = $('#sample-request-data').val();
        $.post(url, requestData, function(data) {
            const responseString = JSON.stringify(data);
            $('#sample-response').val(responseString);
        });
    });
});