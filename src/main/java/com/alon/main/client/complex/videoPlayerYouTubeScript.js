

   var url = "http://localhost:8090/recommend/epgs/";
   // var url = "http://ec2-52-33-215-84.us-west-2.compute.amazonaws.com:8080/ServerSide/recommend/epgs/";
   var user;
   var youTubeUrl;
   var youTubeId;
   var nextPlay;
   var player;
   var qs;
   var initialized = false;
   var playerSet = false;
   var playerDefined = false;
   var youTubeIdSet = false;
   var timeOutResponse;
   initialize();

   function onYouTubeIframeAPIReady() {
       player = new YT.Player('video-container', {
           width: 600,
           height: 400,
           playerVars: {
               color: 'white',
               'autoplay': 1
           },
           events: {
               onReady: initializeTimer
           }
       });
   }

   function setFirstButtom(firstElement) {
       document.getElementById("first-movie-title").innerHTML = firstElement.title;
       document.getElementById("first-movie-plot").innerHTML = firstElement.plot;

       youTubeUrl = firstElement.uri;

       youTubeId = firstElement.youTubeId;

       youTubeIdSet = true;
       setPlayer();

       initialized = true;
   }

   function doGetRequest(xmlhttp, url) {
       xmlhttp.open("GET", url, true);
       xmlhttp.overrideMimeType('text/xml'); // unsupported by IE

       xmlhttp.send();
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

       qs = (function(a) {
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
       localStorage.setItem("userName", qs["userName"]);
       user = localStorage.getItem("userName");

       var xmlhttp = new XMLHttpRequest();

       xmlhttp.onreadystatechange = function() {
           if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
               var myArr = JSON.parse(xmlhttp.responseText);
               parseGetRecommandationResponse(myArr);
           }
       };

       doGetRequest(xmlhttp, parsedUrl);
   }

   function initializeTimer(){

       playerDefined = true;

       setPlayer();

       player.onmousemove = showPopup();

       updateTimerDisplay();

       time_update_interval = setInterval(function () {
           updateTimerDisplay();
       }, 1000);

       initialized = true;
   }

   function setPlayer() {
       if (!playerSet && youTubeIdSet && playerDefined){
           playerSet = true;
           player.loadVideoById(youTubeId);
       }
   }

   // This function is called by initialize()
   function updateTimerDisplay(){

       // Update current time text display.
       var duration = player.getDuration();
       var playerCurrentTime = player.getCurrentTime();

       var timeDuration = formatTime(duration - playerCurrentTime);

       if (!timeDuration.includes("NaN")){
           document.getElementById("timer").innerHTML = "Ends in: " + timeDuration;
       }

       if (duration > 0 && duration === playerCurrentTime){
           window.location.href = getRefreshPage();
       }
   }

   function formatTime(time){
       time = Math.round(time);

       var minutes = Math.floor(time / 60),
           seconds = time - minutes * 60;

       seconds = seconds < 10 ? '0' + seconds : seconds;

       return minutes + ":" + seconds;
   }

   // function showPopup() {
   //     console.log("showPopup");
   //
   //     if (initialized){
   //         var btns = document.getElementsByClassName("btns-location");
   //
   //         var i;
   //         for (i = 0; i < btns.length; i++) {
   //             btns[i].setAttribute("style", "transition: visibility 0s, opacity 1.0s linear; opacity:1;");
   //         }
   //
   //         setTimeout(function(){ hidePopup();}, 5000);
   //     }
   // }

   function showPopupPermenent() {
       showPopupWithTimer(true)
   }

   function showPopup() {
       showPopupWithTimer(false)
   }

   function showPopupWithTimer(isPermenent) {


       if (initialized){
           var btns = document.getElementsByClassName("btns-location");

           var i;
           for (i = 0; i < btns.length; i++) {
               btns[i].setAttribute("style", "transition: visibility 0s, opacity 1.0s linear; opacity:1;");
           }

           if (!isPermenent){
               timeOutResponse = setTimeout(function(){ hidePopup();}, 5000);
           }else{
               clearTimeout(timeOutResponse);
           }
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

   function getRefreshPage() {
       return location.protocol + '//' + location.host + location.pathname + "?userName=" + user + "&play=" + nextPlay;
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