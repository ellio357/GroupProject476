let count = 0;
function checkCredentials() {
    // package data in a JSON object
    var data_d = {'email': 'owner@email.com', 'password': 'password'};
    console.log('data_d', data_d);

    // SEND DATA TO SERVER VIA jQuery.ajax({})
    jQuery.ajax({
        url: "/processlogin",
        data: data_d,
        type: "POST",
        success: function(returned_data) {
            returned_data = JSON.parse(returned_data);
            window.location.href = "/home";
        }
    });
}

$(document).ready(function() {
    $('#loginForm').on('submit', function(event) {
        event.preventDefault();
        var data_d = {
            'email': $('#email').val(),
            'password': $('#password').val()
        };
        $.ajax({
            url: "/processlogin",
            data: data_d,
            type: "POST",
            success: function(returned_data) {
                returned_data = JSON.parse(returned_data);
                if (returned_data.success) {
                    window.location.href = "/home";
                } else {
                    count++;
                    $('#error-message').text('Authentication Failure: ' + count);
                }
            }
        });
    });
});