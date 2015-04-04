/*price range*/
var app = angular.module('store',['ngRoute']);

app.factory('thefactory',function($http,$q){
  var factory={};
  factory.product=[{'productId': 15 ,'name':'Lint','price': 50,'description':'Most awesome thing ever.','img':'images/choclate.png'},
                   {'productId': 16 ,'name':'Lint','price': 50,'description':'Most awesome thing ever.','img':'images/choclate.png'},
                   {'productId': 17 ,'name':'Lint','price': 50,'description':'Most awesome thing ever.','img':'images/choclate.png'},
                   {'productId': 18 ,'name':'Lint','price': 50,'description':'Most awesome thing ever.','img':'images/choclate.png'}];
  factory.cart_items = 0;
  factory.cart_amount = 0;
  factory.cart=[];
  factory.loggedIn = false;
  factory.user="Ronak Shah"
  factory.wishlist=[{'productId':19,'name':'Ronak' ,'description':'TheBest' , 'image' :'images/home/image.jpg' ,'price' : 100}];
  
  factory.setLoggedIn=function(value)
  {
    factory.loggedIn=value;
  }

  factory.changeQuantity=function(prodId,quantity)
  {
    console.log("Quantity Change");
    for(i=0;i<factory.cart.length;i++)
    {
      if(factory.cart[i].productId==prodId)
      {
        if((factory.cart[i].count+quantity)>=0)
        {
          factory.cart[i].count+=quantity;
          factory.cart[i].totCost=factory.cart[i].price*factory.cart[i].count;
          factory.cart_amount=factory.cart_amount+(factory.cart[i].price*quantity);
        }
      }
    }
  };

  factory.setCartAmount=function()
  {
    return factory.cart_amount;
  }

  factory.addProdToWishlist=function(prodId,prodName,prodDescription,prodImage,prodPrice)
  {
    var find=0;
    for(i=0;i<factory.wishlist.length;i++)
    {
      if(factory.wishlist[i].productId==prodId)
      {
        find=1;
      }
    }
    if(find==1)
    {
      console.log("Product Already in Wishlist");
    }
    else
    {
      factory.wishlist.push({'productId':prodId,'name':prodName ,'description':prodDescription , 'image' : prodImage ,'price' : prodPrice});
      console.log("Product Added to Wishlist");
    }
  }

  factory.removeProdFromWishlist=function(prodId)
  {
    var find=0;
    for(i=0;i<factory.wishlist.length;i++)
    {
      if(factory.wishlist[i].productId==prodId)
      {
        factory.wishlist.splice(i,1);
      }
    }
  }

  factory.findProduct=function(prodId)
  {
    for(i=0;i<factory.product.length;i++)
    {
      if(factory.product[i].productId==prodId)
      {
        return factory.product[i];
        console.log("Found Product");
      }
    }
  }

  factory.addProdToCart=function(prodId,price,name,image)
  {
    factory.cart_amount=factory.cart_amount+price;
    factory.cart_items++;
    var find=0;
    for(i=0;i<factory.cart.length;i++)
    {
      if(factory.cart[i].productId==prodId)
      {
        find=1;
      }
    }
    if(find==1)
    {
      for(i=0;i<factory.cart.length;i++)
      {
        if(factory.cart[i].productId==prodId)
        {
          factory.cart[i].count++;
          factory.cart[i].totCost=factory.cart[i].price * factory.cart[i].count;
        }
      }
      console.log("Match Found");
    }
    else
    {
      factory.cart.push({'productId':prodId , 'price':price , 'name':name ,'count' : 1 ,'image':image, 'totCost': price});
      console.log("Match Not Found");
    }
  };
  return factory;
});

var chocolate= [{name : 'Lint ', link :'#/Lint'},{name : 'Dairy Milk', link:'#/Dairy'}];
var cake=[{name:'Black Forest'}];
var categories = [{name : "Dark Choclate"},{name : "White Chocolate"},{name : "Clueberry Cheese Cake"},{name : "Cake"}];
var brands = [{name : "Lint"},{name : "Dairy Milk"},{name : "5-Star"},{name : "Ferro Rocher"}];

app.controller('HeaderController',function(thefactory){
  this.cart_items=thefactory.cart_items;
});

app.controller('NavbarController',function($scope,thefactory){
  $scope.loggedIn=thefactory.loggedIn;
  console.log($scope.loggedIn);
  $scope.username = thefactory.user;
  console.log("Navbar Controller");
	$scope.chocolate=chocolate;
	$scope.cake=cake;

  $scope.logout=function(){
    $scope.loggedIn=false;
    thefactory.setLoggedIn(false);
  }
});

app.controller('CategoriesController',function(thefactory){
	this.categories = categories;
});

app.controller('BrandsController',function(thefactory){
	this.brands = brands;
});

app.controller('ListController',function($scope,thefactory){
  $scope.loggedIn= thefactory.loggedIn;
  console.log($scope.loggedIn);
  $scope.username = thefactory.user;
  console.log("List Controller");
  $scope.chocolate=chocolate;
  $scope.cake=cake;

  $scope.logout=function(){
    $scope.loggedIn=false;
    thefactory.loggedIn();
  }
  $scope.products=thefactory.product;
  $scope.loggedIn=thefactory.loggedIn;
  console.log("List Controller");
  $scope.addcart=function(a,b,c,d){
    console.log("Add to cart called!");
    thefactory.addProdToCart(a,b,c,d);
    console.log(thefactory.cart_items);
  };

  $scope.addwishlist=function(a,b,c,d,e){
    console.log("Add to Wishliist called!");
    thefactory.addProdToWishlist(a,b,c,d,e);
  };
})

app.controller('CartController',function($scope,thefactory){
  $scope.list=thefactory.cart;

  $scope.cart_amount=thefactory.setCartAmount();
  $scope.increaseQuantity=function(a){
    console.log("Quantity Change");
    thefactory.changeQuantity(a,1);
    $scope.cart_amount=thefactory.setCartAmount();
  };

  $scope.decreaseQuantity=function(a){
    console.log("Quantity Change");
    thefactory.changeQuantity(a,-1);
    $scope.cart_amount=thefactory.setCartAmount();
  };
})


app.controller('WishlistController',function($scope,thefactory){
  this.list=thefactory.wishlist;
  $scope.removeWishlist=function(a){
    thefactory.removeProdFromWishlist(a);
  };
})

app.controller('ProductController',function($scope,thefactory,$routeParams){
  this.product_id=$routeParams.productId;
  this.loggedIn=thefactory.loggedIn;
  console.log(this.product_id);
  var product_num=Number(this.product_id);
  console.log(product_num);
  this.product=thefactory.findProduct(product_num);
  console.log(this.product.product_id);
  $scope.addcart=function(a,b,c,d){
    console.log("Add to cart called!");
    thefactory.addProdToCart(a,b,c,d);
    console.log(thefactory.cart_items);
  };
})

app.controller('LoginController',function($scope,thefactory){
  var rootURL = "/choc/rest/api";
	
  $('#loginBtn').click(function() {
    console.log("Login Button Clicked");
    $scope.loginUser($('#emailId').val(), $('#password').val());
    return false;
  });

  $scope.loginUser=function(uname,pass) {
    console.log("Login Fuction Called");
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
    
    function renderList(data) {
    	console.log("Got data");
    	console.log(data);
    	if(data['status'] == 'success')
    	{
    		thefactory.user=data['name'];
    		thefactory.setLoggedIn(true);
    	}
    	console.log(thefactory.user);
    	console.log(thefactory.loggedIn);
    	location.href="#/";
    }
  }
  
  $('#signupBtn').click(function() {
		console.log("Signup Button Clicked");
		signupUser($('#email').val(), $('#password2').val(), $('#fname').val(), $('#lname').val());
		console.log('pass: ' + $('#password2').val());
		return false;
	})
	
  function signupUser(email,pass1,fname,lname) {
	if(true) {
		var item = {};
		item['email_id']=email;
		item['password']=pass1;
		console.log(item['password']);
		item['firstname']=fname;
		item['lastname']=lname;
		item['contact_no']=null;
		var jsonObj = JSON.stringify(item);
		$.ajax({
			type  :'POST',
			url : rootURL + '/signup',
			contentType : 'application/json',
			data : jsonObj
		});
	}
	else {
		console.log('password doesnt match:' + pass1 + ' ' + pass2)
	}
	
}
})

app.config(function($routeProvider){
  $routeProvider
  .when('/',
    {
      controller:'ListController',
      templateUrl:'partials/startpage.html'
    })
  .when('/listing',
    {
      controller:'ListController',
      templateUrl:'partials/listing.html'
    })
  .when('/product/:productId',
    {
      templateUrl:'partials/product.html'
    })
  .when('/cart',
    {
      controller:'CartController',
      templateUrl:'partials/cart.html'
    })
  .when('/wishlist',
    {
      controller:'WishlistController',
      templateUrl:'partials/wishlist.html'
    })
  .when('/login',
    {
      templateUrl:'partials/login.html'
    })
});

 $('#sl2').slider();

	var RGBChange = function() {
	  $('#RGB').css('background', 'rgb('+r.getValue()+','+g.getValue()+','+b.getValue()+')')
	};	
		
/*scroll to top*/


  $(document).ready(function(){
    $(function () {
    	$.scrollUp({
    	       scrollName: 'scrollUp', // Element ID
    	       scrollDistance: 300, // Distance from top/bottom before showing element (px)
    	       scrollFrom: 'top', // 'top' or 'bottom'
    	       scrollSpeed: 300, // Speed back to top (ms)
    	       easingType: 'linear', // Scroll to top easing (see http://easings.net/)
    	       animation: 'fade', // Fade, slide, none
    	       animationSpeed: 200, // Animation in speed (ms)
    	       scrollTrigger: false, // Set a custom triggering element. Can be an HTML string or jQuery object
    					//scrollTarget: false, // Set a custom target element for scrolling to the top
    	       scrollText: 'Cart', // Text for element, can contain HTML
    	       scrollTitle: false, // Set a custom <a> title if required.
    	       scrollImg: false, // Set true to use image
    	       activeOverlay: false, // Set CSS color to display scrollUp active point, e.g '#00FFFF'
    	       zIndex: 2147483647 // Z-Index for the overlay
    		});
    	});
  });

