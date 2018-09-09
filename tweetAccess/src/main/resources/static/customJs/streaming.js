/*
 *   Funciones JavaScript relacionadas con la parte STREAMING de la aplicación
 */

function streamingEncryptClicked() {
    $("#lblTitle").text("Tweets en streaming")
    $("#dashboardBlock").hide();
    $("#q").val("");
    $("#q").prop('disabled', false);
    $("#q").attr('placeholder', 'Press Enter to start a new search');
    $("#streamingEncryptTweets").addClass("active");
    $("#streamingChangeTweets").removeClass("active");
    $("#databaseTweets").removeClass("active");
    $("#dashboard").removeClass("active");
    $('#divPagination').hide();
    menu = 1;
    unsubscribeIfNeeded();
}

function streamingChangeClicked() {
    $("#lblTitle").text("Tweets en streaming")
    $("#dashboardBlock").hide();
    $("#q").val("");
    $("#q").prop('disabled', false);
    $("#q").attr('placeholder', 'Press Enter to start a new search');
    $("#streamingEncryptTweets").removeClass("active");
    $("#streamingChangeTweets").addClass("active");
    $("#changeTweets").removeClass("active");
    $("#databaseTweets").removeClass("active");
    $("#dashboard").removeClass("active");
    $('#divPagination').hide();
    menu = 2;
    unsubscribeIfNeeded();
}


function startSubscription(target, query, mode) {
    unsubscribeIfNeeded();
/*
    var parameter = {query: query, mode: mode};
    stompClient.send("/app/" + target, {}, JSON.stringify(parameter));
    subscription = stompClient.subscribe('/queue/search/' + query, showEncrpytedTweet);*/
    var parameter = Object.keys({query: query, mode: mode}).map(k => encodeURIComponent(k) + '=' + encodeURIComponent({query: query, mode: mode}[k])).join('&');
    var socket = new SockJS("/app/" + target + "?"+parameter);
    var stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        stompClient.subscribe('/queue/search'+query, showEncrpytedTweet);
        }, function(err) {

    // connection error
});
}

function showEncrpytedTweet(generatedTweetDto) {
    var data = JSON.parse(generatedTweetDto.body)
    var rendered = Mustache.render(templateEncryptedTweets, {tweets: data});
    $('#resultsBlock').append(rendered);
}

function unsubscribeIfNeeded() {
    if (subscription != null) {
        subscription.unsubscribe();
        subscription = null;
    }
    $('#resultsBlock').empty();
}
