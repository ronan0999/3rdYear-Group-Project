


// var mainText = document.getElementById("mainText");
// var submitBtn =  document.getElementById("submitBtn");
// var heading = document.getElementById("fireHeading");
//
// // RETRIEVE DATA
// var firebaseHeadingRef = firebase.database().ref().child("Heading");
// firebaseHeadingRef.on('value', function(datasnapshot){
//   fireHeading.innerText = datasnapshot.val();
// })
// // PUSH DATA
// function submitClick() {
//   var firebaseRef = firebase.database().ref();
//
//   var messageText = mainText.value;
//   // firebaseRef.child("Text").set(messageText); //FOR ADDING, if child name exists --> replace value
//   firebaseRef.push().set(messageText);  // WILL CREATE A UNIQUE ID INSTEAD OF REPLACE IF EXITS
//   console.log("DONE");
// }


// $(document).ready(function() {  // will run when the page is ready
//   var rootRef = firebase.database().ref().child("Users");
//
//   rootRef.on("child_added", snap => {
//     var name = snap.child("Name").val();
//     var email = snap.child("Email").val();
//
//     // $("#tableBody").append("<tr><td>" + name + "</td><td>" + email + "</td><td><button>Remove</button></td></tr>");
//     $("#tableBody").append("<tr><td>" + name + "</td><td>" + email + "</td><td><button>Remove</button></td></tr>");
//   })
// });

firebase.auth().setPersistence(firebase.auth.Auth.Persistence.LOCAL)

$("#loginButton").click(function(){
  // alert("user");
  console.log("LOGIN")
  var email = $("#email").val();
  var password = $("#password").val();
  // console.log(email);
  // console.log(password);
  if (email != "" && password != "") {
    var result = firebase.auth().signInWithEmailAndPassword(email, password);

    result.catch(function(error){
      var errorCode = error.code;
      var errorMessage = error.message;

      console.log(errorCode + ": " + errorMessage);
      alert("Message: " + errorMessage);
    })
  }
  else {
    alert("Please fill out all fields");
  }
})

$("#logoutButton").click(function(){
  firebase.auth().signOut();
  // firebase.auth().signOut().then(function() {
  // // Sign-out successful.
  //
  // })
  // console.log(user);
  // alert("log");
})

$("#registerLink").click(function(){
  window.location.href = "register.html";
})

$("#registerButton").click(function(){
  var type = $("input[name='profession']:checked").val()
  var fname = $("#firstName").val();
  var lname = $("#lastName").val();
  var id = $("#id").val();
  var phone = $("#phone").val();
  var email = $("#email").val();
  var password = $("#password").val();
  console.log(email);
  // if (email != "" && password != "") {
  var result = firebase.auth().createUserWithEmailAndPassword(email, password);

  result.catch(function(error){
    var errorCode = error.code;
    var errorMessage = error.message;

    console.log(errorCode + ": " + errorMessage);
    alert("Message: " + errorMessage);
  })
  var firebaseRef = firebase.database().ref().child("professionals");
  toPush = {
    'type':type,
    'name':fname + " " + lname,
    'id':id,
    'phone':phone,
    'email':email,
  };
  if (type == 'Insurance'){
    var insuranceName = $('#insuranceName').val();
    // console.log(insuranceName);
    toPush['insuranceName'] = insuranceName;
  }
  console.log(toPush['insuranceName']);
  firebaseRef.push().set(toPush);
  console.log('done');
  // }
  // else {
  //   alert("Please fill out all fields");
  // }
// })
})

$(".profession").change(function(){
  // if ($("#gpRadio").checked()) {
  // console.log($("input[name='profession']:checked").val());
  if ($("input[name='profession']:checked").val() == 'GP') {
    console.log("IN");
    $("#insuranceDetail").hide();
  }
  else {
    $("#insuranceDetail").show();
  }
})

$("#checkLink").click(function(){
  // alert(window.location);
  var user = firebase.auth().currentUser.email;
  console.log(user)
  window.location.href = "checkPatients.html";
  // // var rootRef = firebase.database().ref().child("Professionals")//.orderByChild("email").equalTo(user);
  var rootRef = firebase.database().ref().child("professionals");
  var current = null;
  rootRef.on("value", function(snapshot){

    console.log(typeof snapshot.val());
    // for (var i in snapshot.val()){
    //   // console.log(snapshot.i);
    //   if(snapshot.val().hasOwnProperty(i)){
    //    console.log(${i} : ${snapshot.val()[i]});
    //   }
    //   // console.log(value);
    //   if (snapshot.val().email == user){
    //     current = snapshot.val();
    //     getPatient(current);
    //     // console.log(current);
    //
    //   }
    // }

    Object.values(snapshot.val()).forEach(value=>{
      // console.log(value);
      if (value.email == user) {
        current = value;
        getPatient(current);
      }
    });
  })
  alert("wait");
  // console.log(current);
  // firebase.database().ref("/test-6e817/Professionals/LwEkbZ_xicodOfUwnfS").once("value").then(function(snapshot) {
  // var username = snapshot.val();
  // console.log(snapshot.val());
  // ...
})

function getAllPatients() {
  var user = firebase.auth().currentUser.email;
  console.log(user);
  // var rootRef = firebase.database().ref().child("Professionals")//.orderByChild("email").equalTo(user);
  var rootRef = firebase.database().ref().child("professionals");
  var current = null;
  rootRef.on("value", function(snapshot){
    console.log(snapshot.val());
    Object.values(snapshot.val()).forEach(value=>{
      if (value.email == user) {
        current = value;
        getPatient(current);
      }
    })
  })
}
//   rootRef.on("value", function(snapshot){
//     Object.values(snapshot.val().forEach(value=>{
//       // console.log(value);
//       if (value.email == user) {
//         current = value;
//         getPatient(current);
//       }
//   }))
//   }
// }


function getPatient(user) {
  var rootRef = firebase.database().ref().child("patients");
  // var patients = [];
  console.log("In");
  rootRef.on("value", function(snapshot){
    console.log("Hi");
    Object.values(snapshot.val()).forEach(value=>{
      // console.log(value);
      if (value.gpId == user.id) {
        // patients.push(value);
        $("#tableBody").append("<tr><td><a href='#' class='patientLink' id=" + value.pID + ">" + value.name + "</a></td></tr>");
        $("#" + value.pID).click(function(){
          var queryString = "?para1=" + this.id;
          // console.log(this.id);
          window.location.href = "patientDetails.html" + queryString;
        })
      }
    })
    // console.log(snapshot.val().id);
    // if (snapshot.val().gpId == user.id){
    //   patients.push(snapshot.val());
    // }
    // console.log(patients);
  })

}
//
// document.getElementByClassName("patientLink").addEventListener('click', function(){
//   console.log(this.id);
// });

// $(".patientLink").click(function() {
//   console.log("er")
//   window.location.href = "patientDetails.html";
// })
  // var x = rootRef.orderByChild("email").equalTo(user);
  // console.log(x.getValue());
    // rootRef.on("child_added", snap => {
    //   var name = snap.child("Name").val();
    //   var email = snap.child("Email").val();
    //
    //   // $("#tableBody").append("<tr><td>" + name + "</td><td>" + email + "</td><td><button>Remove</button></td></tr>");
    //   $("#tableBody").append("<tr><td>" + name + "</td><td>" + email + "</td><td><button>Remove</button></td></tr>");
    // })

// })
// 7b92aff4
