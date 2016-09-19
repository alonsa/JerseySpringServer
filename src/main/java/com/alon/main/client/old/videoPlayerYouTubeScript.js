


var player;
var youTubeId;

   var url = "http://localhost:8090/recommend/epgs/";
// var url = "http://ec2-52-33-215-84.us-west-2.compute.amazonaws.com:8080/ServerSide/recommend/epgs/";
var user;
var youTubeUrl;
var nextPlay;
var initialized = false;

initialize();

function onYouTubeIframeAPIReady() {

    player = new YT.Player('video-placeholder', {
        width: 100,
        height: 100,
        videoId: youTubeId,
        // playerVars: { 'autoplay': 1, 'controls': 0 },

        playerVars: {
            color: 'white',
            'autoplay': 1
            // playlist: 'taJ60kskkns,FG0fTKAqZ5g'
        },
        events: {
            onReady: onPlayerReady
        }
    });
}

function onPlayerReady() {

    player.loadVideoById(youTubeId);
}


function getRefreshPage() {
    return location.protocol + '//' + location.host + location.pathname + "?userName=" + user + "&play=" + nextPlay  + "&type=" + "YouTube";
}

function setFirstButtom(firstElement) {
    document.getElementById("first-movie-title").innerHTML = firstElement.title;
    document.getElementById("first-movie-plot").innerHTML = firstElement.plot;

    youTubeUrl = firstElement.uri;

    youTubeId = firstElement.youTubeId;

    time_update_interval = setInterval(function () {
        updateTimerDisplay();
    }, 1000);

    initialized = true;

    // inter(sec);
}

function setSecondButtom(secondElement) {
    document.getElementById('second-movie-title').innerHTML = secondElement.title;
    document.getElementById("second-movie-plot").innerHTML = secondElement.plot;

    nextPlay = secondElement.id;
}

function parseGetRecommandationResponse(arr) {
    setFirstButtom(arr[0]);
    setSecondButtom(arr[1]);

    initialized = true;

}

function initialize(){

    var qs = (function(a) {
        if (a == "") return {};
        var b = {};
        for (var i = 0; i < a.length; ++i)
        {
            var p=a[i].split('=', 2);
            if (p.length == 1)
                b[p[0]] = "";
            else
                b[p[0]] = decodeURIComponent(p[1].replace(/\+/g, " "));
        }
        return b;
    })(window.location.search.substr(1).split('&'));
    var  parsedUrl = url + qs["userName"] + "?play=" + qs["play"] + "&type=" + "YouTube";

    var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            var myArr = JSON.parse(xmlhttp.responseText);
            parseGetRecommandationResponse(myArr);
        }
    };

    doGetRequest(xmlhttp, parsedUrl);
}

function formatTime(time){
    time = Math.round(time);

    var minutes = Math.floor(time / 60),
        seconds = time - minutes * 60;

    seconds = seconds < 10 ? '0' + seconds : seconds;

    return minutes + ":" + seconds;
}

function updateTimerDisplay() {
    var duration = player.getDuration();

    if (Number.isInteger(duration)){
        var timeDuration = formatTime(duration - player.getCurrentTime());

        if (timeDuration > 0){
            alert("Set element => " + "TimeDuration: " + timeDuration + ",Duration: "  + duration);
            document.getElementById("timer").innerHTML = "Ends in: " + timeDuration;
        }else if (duration > 0){

            alert("GetRefreshPage => " + "TimeDuration: " + timeDuration + ",Duration: "  + duration);
            window.location.href = getRefreshPage();
        }

    }

}

function setTimerButton(timeDuration) {
    document.getElementById("timer").innerHTML = "Ends in: " + timeDuration;
}

function doGetRequest(xmlhttp, url) {
    xmlhttp.open("GET", url, true);
    xmlhttp.overrideMimeType('text/xml'); // unsupported by IE

    xmlhttp.send();
}

function showPopup() {
    if (initialized){
        var btns = document.getElementsByClassName("btns-location");

        var i;
        for (i = 0; i < btns.length; i++) {
            btns[i].setAttribute("style", "transition: visibility 0s, opacity 1.0s linear; opacity:1;");
        }

        setTimeout(function(){ hidePopup();}, 5000);
    }
}

function hidePopup() {
    var btns = document.getElementsByClassName("btns-location");

    var i;
    for (i = 0; i < btns.length; i++) {
        btns[i].setAttribute("style", "transition: visibility 0s, opacity 1.0s linear; opacity:0;");
    }
}






