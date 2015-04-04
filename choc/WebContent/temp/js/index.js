// The root URL for the RESTful services
var rootURL = "/choc/rest/api";


// Register listeners
$('#loginBtn').click(function() {
	console.log("Login Button Clicked");
	loginUser($('#username').val(), $('#password').val());
	return false;
});

$('#signupBtn').click(function() {
	console.log("Signup Button Clicked");
	signupUser($('#email_id').val(), $('#pass1').val(), $('#pass2').val(), $('#fname').val(), $('#lname').val(), $('#contact').val());
	return false;
})

function renderList(data) {
	
	console.log("Got data");
	console.log(data);
}

function multiply(a, b) {
	console.log('multiply:A' + a + ' B:' + b);
	$.ajax({
		type: 'GET',
		url: rootURL + '/multiply/' + a + '/' + b,
		dataType: "json",
		success: renderList 
	});
}

function loginUser(uname,pass) {
	var item={};	
	item["username"]=uname;
	item["password"]=pass;
	var credentials=JSON.stringify(item);
	console.log(item['username'] + ' ' + item['password']);
	$.ajax({
		type: 'POST',
		url: rootURL + '/login',
		contentType : 'application/json',
		data : credentials,
		success : renderList
	});
}

function signupUser(email,pass1,pass2,fname,lname,contact) {
	if(pass1 == pass2) {
		var item = {};
		item['email_id']=email;
		item['password']=pass1;
		item['firstname']=fname;
		item['lastname']=lname;
		item['contact_no']=contact;
		var jsonObj = JSON.stringify(item);
		$.ajax({
			type  :'POST',
			url : rootURL + '/signup',
			contentType : 'application/json',
			data : jsonObj,
			success  : renderList
		});
	}
	else {
		console.log('password doesnt match:' + pass1 + ' ' + pass2)
	}
	
}

