function changeConfig(){
    var processor = menu;

    /*$.getJSON("/configProcessor?processor=" + menu , {},
    $.ajax({
        type: "POST",
        url: "/configProcessor",
        data: processor,
        success: success,
        dataType: dataType
    });*/

    //$.post( "/configProcessor", {processor:menu});
    //$.post( "test.php", { name: "John", time: "2pm" } );
    /*$.ajax({
        beforeSend: function(request) {
            request.setRequestHeader("X-XSRF-TOKEN", Cookies.get('XSRF-TOKEN'));

        },
        dataType: "json",
        url: "/configProcessor?processor=" + menu,
        success: function(data) {
        }
    });*/
    $.ajax({
        url: '/configProcessor',
        type: 'post',
        data: {
            processor: menu,

        },
        /*headers: {
            "X-XSRF-TOKEN": Cookies.get('XSRF-TOKEN')
        },*/
        dataType: 'json',
        success: function (data) {
            console.info(data);
        }
    });
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

