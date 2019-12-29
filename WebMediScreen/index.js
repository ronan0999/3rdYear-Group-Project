
firebase.auth().setPersistence(firebase.auth.Auth.Persistence.LOCAL)

$("#loginButton").click(function(){ // Setting onclick listener for loggin in button
  // alert("user");
  console.log("LOGIN")
  var email = $("#email").val();  // Getting the value in the email field
  var password = $("#password").val(); // Getting the value in the pasword field

  if (email != "" && password != "") {  // Checking that all the fields were filled
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

$("#logoutButton").click(function(){  // Setting onclick listener to the logout button
  firebase.auth().signOut();  // Using Fireabse Authentication to sign out the user
})

$("#registerLink").click(function(){  // Setting onclick listener to the link to register
  window.location.href = "register.html"; // Displaying the registration form
})

$("#registerButton").click(function(){
  var type = $("input[name='profession']:checked").val()
  var fname = $("#firstName").val();
  var lname = $("#lastName").val();
  var id = $("#id").val();
  var phone = $("#phone").val();
  var email = $("#email").val();
  var password = $("#password").val();

  var result = firebase.auth().createUserWithEmailAndPassword(email, password); // Trying to create a new user and storing the result
  var error = false;
  result.catch(function(error){
    var errorCode = error.code;
    var errorMessage = error.message;

    console.log(errorCode + ": " + errorMessage);
    alert("Message: " + errorMessage);
  })
  if (!error){
    var firebaseRef = firebase.database().ref().child("professionals"); // Getting a reference to the professionals collection in the database
    // Creating a dictionary with the information from the form
    // This dictionary will be pushed to the database
    toPush = {
      'type':type,
      'name':fname + " " + lname,
      'id':id,
      'phone':phone,
      'email':email,
    };

    if (type == 'Insurance'){ // Checking the type of the professional to create
      var insuranceName = $('#insuranceName').val();
      // console.log(insuranceName);
      toPush['insuranceName'] = insuranceName;
    }
    // console.log(toPush['insuranceName']);

    firebaseRef.push().set(toPush); // Pushing the data to the database

  }

})

$(".profession").change(function(){ // Setting onchange listener for the radio buttons
  if ($("input[name='profession']:checked").val() == 'GP') {  // if it is a GP
    $("#insuranceDetail").hide(); // we don't get the insurance name
  }
  else {
    $("#insuranceDetail").show(); // get the insurance name
  }
})

$("#checkLink").click(function(){ // Setting onclick listener to the checkPatients link
  var user = firebase.auth().currentUser.email; // Getting the current user's email
  window.location.href = "checkPatients.html";  // Displaying the checkPatients page

  var rootRef = firebase.database().ref().child("professionals"); // Getting a reference to the professionals collection

  var current = null;
  rootRef.on("value", function(snapshot){

    Object.values(snapshot.val()).forEach(value=>{  // Looping through the collection
      if (value.email == user) {  // Checking if the rmail corresponds to the current user's email
        current = value;
        getPatient(current);
      }
    });
  })
})

function getAllPatients() {
  var user = firebase.auth().currentUser.email; // getting the current user's email

  var rootRef = firebase.database().ref().child("professionals");
  var current = null;
  rootRef.on("value", function(snapshot){ // looping through collection of professionals
    Object.values(snapshot.val()).forEach(value=>{
      if (value.email == user) {  // if the email corresponds to the current user's one
        current = value;  // get the user's details from the database
        getPatient(current);
      }
    })
  })
}


function getPatient(user) {
  var rootRef = firebase.database().ref().child("patients");

  rootRef.on("value", function(snapshot){

    Object.values(snapshot.val()).forEach(value=>{ // looping through all the patients
      if (value.gpId == user.id) {  // checking if the current user's id is the same to the one for the patient
        $("#tableBody").append("<tr><td><a href='#' class='patientLink' id=" + value.pID + ">" + value.name + "</a></td></tr>");  // add the patient to the table
        $("#" + value.pID).click(function(){
          var queryString = "?para1=" + this.id;  // passing the id to the url

          window.location.href = "patientDetails.html" + queryString;
        })
      }
    })
  })

}
