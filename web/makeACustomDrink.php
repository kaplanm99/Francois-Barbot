<html>
<head> 
	<link rel="stylesheet" type="text/css" href="style.css" /> 	
	<title>Francois</title>	
	<script src="jquery-1.7.2.min.js"></script>
	<script>
	<!--
	var ingr1Amt = 0;
	var ingr2Amt = 0;
	var ingr3Amt = 0;
	var ingr4Amt = 0;
	
	$(document).ready(function(){
       $("#minusButton1").click(function(event){
         if(ingr1Amt > 0)
		 {
			ingr1Amt--;
			$("#ingredient1").html(ingr1Amt);
			$("#ingredient1Amount").attr("value",ingr1Amt);
		 }
		});
		
		$("#plusButton1").click(function(event){
         if((ingr1Amt+ingr2Amt+ingr3Amt+ingr4Amt) < 8)
		 {
			ingr1Amt++;
			$("#ingredient1").html(ingr1Amt);
			$("#ingredient1Amount").attr("value",ingr1Amt);
		 }
		});
		
		$("#minusButton2").click(function(event){
         if(ingr2Amt > 0)
		 {
			ingr2Amt--;
			$("#ingredient2").html(ingr2Amt);
			$("#ingredient2Amount").attr("value",ingr2Amt);
		 }
		});
		
		$("#plusButton2").click(function(event){
         if((ingr1Amt+ingr2Amt+ingr3Amt+ingr4Amt) < 8)
		 {
			ingr2Amt++;
			$("#ingredient2").html(ingr2Amt);
			$("#ingredient2Amount").attr("value",ingr2Amt);
		 }
		});
		
		$("#minusButton3").click(function(event){
         if(ingr3Amt > 0)
		 {
			ingr3Amt--;
			$("#ingredient3").html(ingr3Amt);
			$("#ingredient3Amount").attr("value",ingr3Amt);
		 }
		});
		
		$("#plusButton3").click(function(event){
         if((ingr1Amt+ingr2Amt+ingr3Amt+ingr4Amt) < 8)
		 {
			ingr3Amt++;
			$("#ingredient3").html(ingr3Amt);
			$("#ingredient3Amount").attr("value",ingr3Amt);
		 }
		});
		
		$("#minusButton4").click(function(event){
         if(ingr4Amt > 0)
		 {
			ingr4Amt--;
			$("#ingredient4").html(ingr4Amt);
			$("#ingredient4Amount").attr("value",ingr4Amt);
		 }
		});
		
		$("#plusButton4").click(function(event){
         if((ingr1Amt+ingr2Amt+ingr3Amt+ingr4Amt) < 8)
		 {
			ingr4Amt++;
			$("#ingredient4").html(ingr4Amt);
			$("#ingredient4Amount").attr("value",ingr4Amt);
		 }
		});
	   
	   $("#submitButton").click(function(event){
         $("#addACustomDrinkForm").submit();
       });
	   
	   
	   $("#cancelButton").click(function(event){
         window.location = "index.php";
       });
	   
	   
     });
	 -->
    </script>
	
</head>

<body>


<div id="app">
	<h1><img src="pic1.png" /><span id="title">Automatic Bartender</span><img src="pic2.png" /></h1> <!-- Student project. Images borrowed from http://www.mangotribe.com/wp-content/uploads/2012/03/1256720_glasses_2.jpg -->
	<div class="addACustomDrink">
		<h2>Add A Custom Drink</h2>
		<form id="addACustomDrinkForm" name="input" action="index.php" method="post">
			<div id="makePair">
			<p class="oz">Name</p><div class="buttonCol"><input type="text" name="drinkName" value="Custom Drink" /></div>
			</div>
			<div id="makePair">
			<p class="oz">Ounces of Vodka</p><div class="buttonCol"> <h4 class="minusButton" id ="minusButton1">-</h4><h4 id ="ingredient1" class="ingredientAmount">0</h4><input type="hidden" id="ingredient1Amount" name="ingredient1Amount" value="0" /><h4 class="plusButton" id ="plusButton1">+</h4></div>
			</div>
			<div id="makePair">
			<p class="oz">Ounces of Gin</p><div class="buttonCol"> <h4 class="minusButton" id ="minusButton2">-</h4><h4 id ="ingredient2" class="ingredientAmount">0</h4><input type="hidden" id="ingredient2Amount"  name="ingredient2Amount" value="0" /><h4 class="plusButton" id ="plusButton2">+</h4></div>
			</div>
			<div id="makePair">
			<p class="oz">Ounces of Cranberry Juice</p><div class="buttonCol"> <h4 class="minusButton" id ="minusButton3">-</h4><h4 id ="ingredient3" class="ingredientAmount">0</h4><input type="hidden" id="ingredient3Amount"  name="ingredient3Amount" value="0" /><h4 class="plusButton" id ="plusButton3">+</h4></div>
			</div>
			<div id="makePair">
			<p class="oz">Ounces of Tonic</p><div class="buttonCol"> <h4 class="minusButton" id ="minusButton4">-</h4><h4 id ="ingredient4" class="ingredientAmount">0</h4><input type="hidden" id="ingredient4Amount"  name="ingredient4Amount" value="0" /><h4 class="plusButton" id ="plusButton4">+</h4></div>
			</div>
			
			<div id="makePair">
			<p class="oz">Stirred?</p><div class="buttonCol"> <h4 class="radioAmount"><input type="radio" name="stirred" value="1" checked="checked" /> Yes <input type="radio" name="stirred" value="0" /> No</h4></div>
			</div>
			
			<div id="makePair">
			<p class="oz">Iced?</p><div class="buttonCol"> <h4 class="radioAmount"><input type="radio" name="iced" value="1" checked="checked" /> Yes <input type="radio" name="iced" value="0" /> No</h4></div>
			</div>
			<!--
			<p class="oz">
			Ounces of Kiwi Strawberry <input type="text" name="drinkName" /><br />
			Ounces of Cherry <input type="text" name="drinkName" /><br />
			Ounces of Blue Raspberry <input type="text" name="drinkName" /><br />
			Stirred? <input type="radio" name="stirred" value="1" /> Yes <input type="radio" name="stirred" value="0" /> No <br />
			Ice? <input type="radio" name="ice" value="1" /> Yes <input type="radio" name="ice" value="0" /> No <br />
			</p>
			-->
			<h3 id="cancelButton">Cancel</h3><h3 id="submitButton">Submit</h3>
			
		</form>
		<h5 class="bottomPad2">&nbsp;</h5>
	</div>
</div>

</body>
</html>