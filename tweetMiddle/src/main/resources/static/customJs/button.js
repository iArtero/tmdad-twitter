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
    $.get("/user", function(data) {
        rechargeAuthenticated();
    });
}


function logout() {
    $.post("/logout", function() {
        $.get("/user", function(data) {
            rechargeAuthenticated();
        });
    })
    return true;
}

