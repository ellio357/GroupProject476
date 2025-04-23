document.addEventListener("DOMContentLoaded", function () {
   const createBtn = document.getElementById("createEventBtn");
   const joinBtn = document.getElementById("joinEventBtn");
 
   createBtn.addEventListener("click", function () {
     window.location.href = "/create_event";
   });
 
   joinBtn.addEventListener("click", function () {
     window.location.href = "/join_event";
   });
 });
 