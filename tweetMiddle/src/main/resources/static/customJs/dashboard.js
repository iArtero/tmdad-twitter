/*
 *   Funciones JavaScript relacionadas con la parte DASHBOARD de la aplicación
 */

function dashboardClicked() {
    $("#lblTitle").text("Monitorización de la sesión")
    $("#dashboardBlock").show();
    $("#q").val("");
    $("#q").prop('disabled', true);
    $("#q").attr('placeholder', '');
    $("#streamingEncryptTweets").removeClass("active");
    $("#streamingChangeTweets").removeClass("active");
    $("#streamingChangeTweetsCase").removeClass("active");
    $("#databaseTweets").removeClass("active");
    $("#dashboard").addClass("active");
    $('#divPagination').hide();
    document.getElementById('buttonLogin').style.visibility = 'hidden';
    document.getElementById('buttonLogout').style.visibility = 'hidden';
    document.getElementById('buttonChangeConfig').style.visibility = 'hidden';
    menu = 5;
    unsubscribeIfNeeded();
    startDashboard();

}

function startDashboard() {
    // Dos peticiones AJAX para traer informacion del estado del sistema
    $.getJSON("/health", {}).always(updateHealthInfo);

    $.getJSON("/dashboardInfo?node=chooser", {}, updateMetricsInfo);
    $.getJSON("/dashboardInfo?node=saver", {}, updateMetricsInfo);
    $.getJSON("/dashboardInfo?node=access", {}, updateMetricsInfo);
    $.getJSON("/dashboardInfo?node=processor1", {}, updateMetricsInfo);
    $.getJSON("/dashboardInfo?node=processor2", {}, updateMetricsInfo);
    $.getJSON("/dashboardInfo?node=processor3", {}, updateMetricsInfo);


    // Mientras siga el dashboard seleccionado, se actualiza el estado cada 5 segundos
    if (menu === 5)
        setTimeout(startDashboard, 30000);
}

function updateHealthInfo(healthInfo) {
    var rabbitStatus;
    var mongoStatus;

    if (healthInfo.rabbit === undefined) {
        healthInfo = healthInfo.responseJSON;
    }

    if (healthInfo.rabbit !== undefined) {
        rabbitStatus = healthInfo.rabbit.status;
    }
    if (healthInfo.mongo !== undefined) {
        mongoStatus = healthInfo.mongo.status;
    }

    if (rabbitStatus === "UP") {
        $('#rabbitStatusOk').show();
        $('#rabbitStatusNoOk').hide();
    } else {
        $('#rabbitStatusOk').hide();
        $('#rabbitStatusNoOk').show();
    }

    if (mongoStatus === "UP") {
        $('#mongoStatusOk').show();
        $('#mongoStatusNoOk').hide();
    } else {
        $('#mongoStatusOk').hide();
        $('#mongoStatusNoOk').show();
    }
}

function updateMetricsInfo(metricsInfo) {
    if(metricsInfo.node == "chooser"){
        var totalStreams = metricsInfo["counter.streams.total"];
        var currentStreams = metricsInfo["counter.streams.current"];

        if (!totalStreams) totalStreams = 0;
        if (!currentStreams) currentStreams = 0;

        $('#totalStreamings').text(totalStreams);
        $('#currentStreamings').text(currentStreams);

        if(metricsInfo["health"] == "UP"){
            $('#chooserStatusOk').show();
            $('#chooserStatusNoOk').hide();
        }else{
            $('#chooserStatusOk').hide();
            $('#chooserStatusNoOk').show();
        }
    }else if(metricsInfo.node == "saver"){
        if(metricsInfo["health"] == "UP"){
            $('#saverStatusOk').show();
            $('#saverStatusNoOk').hide();
        }else{
            $('#saverStatusOk').hide();
            $('#saverStatusNoOk').show();
        }
    }else if(metricsInfo.node == "access"){
        if(metricsInfo["health"] == "UP"){
            $('#accessStatusOk').show();
            $('#accessStatusNoOk').hide();

        }else{
            $('#accessStatusOk').hide();
            $('#accessStatusNoOk').show();
        }
    }else if(metricsInfo.node == "processor1"){
        var encryptedTweets = metricsInfo["counter.encryptedtweets.total"];

        if (!encryptedTweets) encryptedTweets = 0;
        $('#encryptedTweets').text(encryptedTweets);


        if(metricsInfo["health"] == "UP"){
            $('#processor1StatusOk').show();
            $('#processor1StatusNoOk').hide();

        }else{
            $('#processor1StatusOk').hide();
            $('#processor1StatusNoOk').show();
        }
    }else if(metricsInfo.node == "processor2"){
        var changedtweets = metricsInfo["counter.changedtweets.total"];

        if (!changedtweets) changedtweets = 0;
        $('#changedTweets').text(changedtweets);

        if(metricsInfo["health"] == "UP"){
            $('#processor2StatusOk').show();
            $('#processor2StatusNoOk').hide();

        }else{
            $('#processor2StatusOk').hide();
            $('#processor2StatusNoOk').show();
        }
    }else if(metricsInfo.node == "processor3"){
        var changedtweetscase = metricsInfo["counter.changedtweetscase.total"];

        if (!changedtweetscase) changedtweetscase = 0;
        $('#changedTweetsCase').text(changedtweetscase);

        if(metricsInfo["health"] == "UP"){
            $('#processor3StatusOk').show();
            $('#processor3StatusNoOk').hide();
        }else{
            $('#processor3StatusOk').hide();
            $('#processor3StatusNoOk').show();
        }
    }



}