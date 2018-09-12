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
    $("#streamingChangeTweetsCase").removeClass("active");
    $("#databaseTweets").removeClass("active");
    $("#dashboard").removeClass("active");
    $('#divPagination').hide();
    menu = 1;
    unsubscribeIfNeeded();

    if(authenticated){
        document.getElementById('buttonLogin').style.visibility = 'hidden';
        document.getElementById('buttonLogout').style.visibility = 'visible';
        document.getElementById('buttonChangeConfig').style.visibility = 'visible';

    }else{
        document.getElementById('buttonLogin').style.visibility = 'visible';
        document.getElementById('buttonLogout').style.visibility = 'hidden';
        document.getElementById('buttonChangeConfig').style.visibility = 'hidden';

    }


}

function streamingChangeClicked() {
    $("#lblTitle").text("Tweets en streaming")
    $("#dashboardBlock").hide();
    $("#q").val("");
    $("#q").prop('disabled', false);
    $("#q").attr('placeholder', 'Press Enter to start a new search');
    $("#streamingEncryptTweets").removeClass("active");
    $("#streamingChangeTweets").addClass("active");
    $("#streamingChangeTweetsCase").removeClass("active");
    $("#changeTweets").removeClass("active");
    $("#databaseTweets").removeClass("active");
    $("#dashboard").removeClass("active");
    $('#divPagination').hide();
    menu = 2;
    unsubscribeIfNeeded();
    if(authenticated){
        document.getElementById('buttonLogin').style.visibility = 'hidden';
        document.getElementById('buttonLogout').style.visibility = 'visible';
        document.getElementById('buttonChangeConfig').style.visibility = 'visible';

    }else{
        document.getElementById('buttonLogin').style.visibility = 'visible';
        document.getElementById('buttonLogout').style.visibility = 'hidden';
        document.getElementById('buttonChangeConfig').style.visibility = 'hidden';

    }
}
function streamingChangeCaseClicked() {
    $("#lblTitle").text("Tweets en streaming")
    $("#dashboardBlock").hide();
    $("#q").val("");
    $("#q").prop('disabled', false);
    $("#q").attr('placeholder', 'Press Enter to start a new search');
    $("#streamingEncryptTweets").removeClass("active");
    $("#streamingChangeTweets").removeClass("active");
    $("#streamingChangeTweetsCase").addClass("active");
    $("#changeTweets").removeClass("active");
    $("#databaseTweets").removeClass("active");
    $("#dashboard").removeClass("active");
    $('#divPagination').hide();
    menu = 3;
    unsubscribeIfNeeded();
    if(authenticated){
        document.getElementById('buttonLogin').style.visibility = 'hidden';
        document.getElementById('buttonLogout').style.visibility = 'visible';
        document.getElementById('buttonChangeConfig').style.visibility = 'visible';

    }else{
        document.getElementById('buttonLogin').style.visibility = 'visible';
        document.getElementById('buttonLogout').style.visibility = 'hidden';
        document.getElementById('buttonChangeConfig').style.visibility = 'hidden';

    }
}

function startSubscription(target, query, mode) {
    unsubscribeIfNeeded();
    var parameter = {query: query, mode: mode};
    stompClient.send("/app/" + target, {}, JSON.stringify(parameter));
    subscription = stompClient.subscribe('/queue/search/' +mode + '/' + query, showEncrpytedTweet);
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
