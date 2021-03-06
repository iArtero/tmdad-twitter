/*
 *   Funciones JavaScript relacionadas con la parte BASE DE DATOS de la aplicación
 */

function databaseClicked() {
    $("#lblTitle").text("Tweets en la base de datos")
    $("#dashboardBlock").hide();
    $("#q").val("");
    $("#q").prop('disabled', false);
    $("#q").attr('placeholder', 'Press Enter to start a new search');
    $("#streamingEncryptTweets").removeClass("active");
    $("#streamingChangeTweets").removeClass("active");
    $("#streamingChangeTweetsCase").removeClass("active");

    $("#databaseTweets").addClass("active");
    $("#dashboard").removeClass("active");
    document.getElementById('buttonLogin').style.visibility = 'hidden';
    document.getElementById('buttonLogout').style.visibility = 'hidden';
    document.getElementById('buttonChangeConfig').style.visibility = 'hidden';
    menu = 4;
    currentPage = 0;
    unsubscribeIfNeeded();
}

function listFromDatabase(text) {
    // Peticion AJAX para buscar Tweets
    $.getJSON("/findByTextContaining?text=" + text + "&page=" + currentPage + "&size=8", {}, function(data) {
        totalPages = data.page.totalPages;
        $('#currentPage').text("Página " + (currentPage+1) + " de " + totalPages);
        var rendered = Mustache.render(templatePlainTweets, {tweets: data._embedded.searchedTweets});
        $('#resultsBlock').html(rendered);
    });
}

function previousPageClicked() {
    if (currentPage > 0) {
        currentPage = currentPage - 1;
        $('#resultsBlock').empty();
        listFromDatabase($("#q").val())
    }
}

function nextPageClicked() {
    if (totalPages > currentPage + 1) {
        currentPage = currentPage + 1;
        $('#resultsBlock').empty();
        listFromDatabase($("#q").val())
    }
}
