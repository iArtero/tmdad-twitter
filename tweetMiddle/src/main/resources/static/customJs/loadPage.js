/*
 *   Funciones JavaScript para arrancar la aplicacion (registrar eventos, plantillas y conectar el websocket)
 */

var subscription = null;
var templatePlainTweets = null;
var templateEncryptedTweets = null;
var stompClient = null;
var menu = 1;
var currentPage = 0;
var totalPages = 1;
var authenticated = false;

$
    .ajaxSetup({
        beforeSend : function(xhr, settings) {
            if (settings.type == 'POST' || settings.type == 'PUT'
                || settings.type == 'DELETE') {
                if (!(/^http:.*/.test(settings.url) || /^https:.*/
                        .test(settings.url))) {
                    // Only send the token to relative URLs i.e. locally.
                    //xhr.setRequestHeader("X-XSRF-TOKEN",
                    //    Cookies.get('XSRF-TOKEN'));
                }
            }
        }
    });



var rechargeAuthenticated= function(){
    $.get("/user", function(data) {


        if(data && data.authenticated){
            authenticated = true;
        }else{
            authenticated = false;
        }

        if(authenticated){
            document.getElementById('buttonLogin').style.visibility = 'hidden';
            document.getElementById('buttonLogout').style.visibility = 'visible';
            document.getElementById('buttonChangeConfig').style.visibility = 'visible';

        }else{
            document.getElementById('buttonLogin').style.visibility = 'visible';
            document.getElementById('buttonLogout').style.visibility = 'hidden';
            document.getElementById('buttonChangeConfig').style.visibility = 'hidden';

        }
    });
};

$(document).ready(rechargeAuthenticated());
//window.onbeforeunload = rechargePage();

function startSearch(event) {
    event.preventDefault();
    var target = $(this).attr('action');
    var q = $("#q").val();

    if (menu === 1) {
        // Streaming de Tweets (operacion ENCRIPTAR)
        startSubscription(target, q, 1);
    }
    else if (menu === 2) {
        // Streaming de Tweets (operacion CAMBIAR VOCALES)
        startSubscription(target, q, 2);
    }
    else if (menu === 3) {
        // Streaming de Tweets (operacion CAMBIAR MAYUS)
        startSubscription(target, q, 3);
    }
    else if (menu === 4) {
        // Listar Tweets de base de datos
        currentPage = 0;
        $('#divPagination').show();
        listFromDatabase(q);
    }
}

function registerEvents() {
    $("#search").submit(startSearch);
    $("#streamingEncryptTweets").click(streamingEncryptClicked);
    $("#streamingChangeTweets").click(streamingChangeClicked);
    $("#streamingChangeTweetsCase").click(streamingChangeCaseClicked);
    $("#databaseTweets").click(databaseClicked);
    $("#dashboard").click(dashboardClicked);
    $("#previousPage").click(previousPageClicked);
    $("#nextPage").click(nextPageClicked);

}

function registerTemplates() {
    templatePlainTweets = $("#templatePlainTweets").html();
    Mustache.parse(templatePlainTweets); // optional, speeds up future uses

    templateEncryptedTweets = $("#templateEncryptedTweets").html();
    Mustache.parse(templateEncryptedTweets); // optional, speeds up future uses
}

function connect() {
    var socket = new SockJS('/twitter');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
    });
}

$(document).ready(function() {
    connect();
    registerTemplates();
    registerEvents();
});

