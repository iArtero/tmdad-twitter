function changeConfig(){
    $.getJSON("/configProcessor?processor=" + menu , {}, function(data) {

    });

    /*$.ajax({
        beforeSend: function(request) {
            request.setRequestHeader("X-XSRF-TOKEN", Cookies.get('XSRF-TOKEN'));

        },
        dataType: "json",
        url: "/configProcessor?processor=" + menu,
        success: function(data) {
        }
    });*/
}

function login(){
    window.location.href = "/login";

    /*$.ajax({
        beforeSend: function(request) {
            request.setRequestHeader("X-XSRF-TOKEN",
                Cookies.get('XSRF-TOKEN'));

        },
        dataType: "json",
        url: "/configProcessor?processor=" + menu,
        success: function(data) {
        }
    });*/
}


