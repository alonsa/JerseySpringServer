
   var url = "http://localhost:8090/recommend/epgs/";
    // var url = "http://ec2-52-33-215-84.us-west-2.compute.amazonaws.com:8080/ServerSide/recommend/epgs/";
    var user;
    var youTubeUrl;
    var nextPlay;
    var dataSet = false;

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
    var  parsedUrl = url + qs["userName"] + "?play=" + qs["play"];

    var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            var myArr = JSON.parse(xmlhttp.responseText);
            parseGetRecommandationResponse(myArr);
        }
    };

    doGetRequest(xmlhttp, parsedUrl);

    function doGetRequest(xmlhttp, url) {
        xmlhttp.open("GET", url, true);
        xmlhttp.overrideMimeType('text/xml'); // unsupported by IE

        xmlhttp.send();
    }

    function parseGetRecommandationResponse(arr) {
        var out = "";
        var i;
        for(i = 0; i < arr.length; i++) {
            out += '<a href="' + arr[i].uri + '">' + arr[i].uri + '</a><br>';
        }

        var firstElement = arr[0];
        document.getElementById("first-movie-title").innerHTML = firstElement.title;
        document.getElementById("first-movie-plot").innerHTML = firstElement.plot;

        var sec = firstElement.length / 1000;
        var minutes = formatNum(Math.floor(sec / 60));
        var seconds = formatNum(Math.floor(sec % 60));
        document.getElementById("timer").innerHTML = "Ends in: " + minutes + ":" + seconds;
        youTubeUrl = firstElement.uri;

        var secondElement = arr[1];
        document.getElementById('second-movie-title').innerHTML = secondElement.title;
        document.getElementById("second-movie-plot").innerHTML = secondElement.plot;

        nextPlay = secondElement.id;
        localStorage.setItem("play", secondElement.id);


        localStorage.setItem("youTubeUrl", youTubeUrl);
        localStorage.setItem("nextPlay", nextPlay);
        localStorage.setItem("user", user);

        document.getElementById('video-frame').src = youTubeUrl;

        dataSet = true;

        function setTimer (sec) {

            var minutes = formatNum(Math.floor(sec / 60));
            var seconds = formatNum(Math.floor(sec % 60));

            document.getElementById("timer").innerHTML = "Ends in: " + minutes + ":" + seconds;
        }

        function inter(sec) {

            setTimeout( function () {
                setTimer (sec);

                if (sec > 0) {
                    inter(sec - 1);
                }else{
                    window.location.href = getRefreshPage();
                }

            }, 1000);
        }

        inter(sec);

    }

    function getRefreshPage() {
        return location.protocol + '//' + location.host + location.pathname + "?userName=" + user + "&play=" + nextPlay;
    }

    function formatNum(input){
        if(input < 10){
            return "0" + input;
        }else {
            return input;
        }
    }

    function showPopup() {
        if (dataSet){
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

    function skipButtonLogic() {
        window.location.href = getRefreshPage();
    }

    function trashButtonLogic() {

        document.getElementById("second-movie-body-text").setAttribute("style", "transition: visibility 0s, opacity 1.0s linear; opacity:0;");

        var  parsedUrl = url + qs["userName"] + "?play=" + nextPlay + "&like=false" + "&num=1";

        var xmlhttp = new XMLHttpRequest();

        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                var myArr = JSON.parse(xmlhttp.responseText);
                parseUnLikeResponse(myArr);
            }
        };

        doGetRequest(xmlhttp, parsedUrl);

        function parseUnLikeResponse(arr) {
            var out = "";
            var i;
            for(i = 0; i < arr.length; i++) {
                out += '<a href="' + arr[i].uri + '">' + arr[i].uri + '</a><br>';
            }

            var newElement = arr[0];
            document.getElementById('second-movie-title').innerHTML = newElement.title;
            document.getElementById('second-movie-plot').innerHTML = newElement.plot;

            nextPlay = newElement.id;
            localStorage.setItem("play", newElement.id);

            localStorage.setItem("nextPlay", nextPlay);
            localStorage.setItem("user", user);

            document.getElementById("second-movie-body-text").setAttribute("style", "transition: visibility 0s, opacity 0.3s linear; opacity:1;");
        }
    }

    window.onload = function () {

        localStorage.setItem("userName", qs["userName"]);
        user = localStorage.getItem("userName");
        localStorage.setItem("valUser", user);

    }
