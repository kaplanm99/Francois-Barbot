<?php

if(isset($_POST["drinkName"])&&isset($_POST["ingredient1Amount"])&&isset($_POST["ingredient2Amount"])&&isset($_POST["ingredient3Amount"])&&isset($_POST["ingredient4Amount"])&&isset($_POST["stirred"])&&isset($_POST["iced"])) 
{	
	require('mysql.config.inc');
	
	$mysqli = new mysqli($host, $username, $password, $db);					
	
	$input_drinkName = strip_tags($_POST["drinkName"]);
	$input_drinkName = trim($input_drinkName);
	$input_drinkName = filter_var($input_drinkName, FILTER_SANITIZE_STRING);
	
	$input_ingredient1Amount = strip_tags($_POST["ingredient1Amount"]);
	$input_ingredient1Amount = trim($input_ingredient1Amount);
	//$input_ingredient1Amount = filter_var($input_ingredient1Amount, FILTER_SANITIZE_INT);
	$input_ingredient1Amount = intval($input_ingredient1Amount);
	
	$input_ingredient2Amount = strip_tags($_POST["ingredient2Amount"]);
	$input_ingredient2Amount = trim($input_ingredient2Amount);
	//$input_ingredient2Amount = filter_var($input_ingredient2Amount, FILTER_SANITIZE_INT);
	$input_ingredient2Amount = intval($input_ingredient2Amount);
	
	$input_ingredient3Amount = strip_tags($_POST["ingredient3Amount"]);
	$input_ingredient3Amount = trim($input_ingredient3Amount);
	//$input_ingredient3Amount = filter_var($input_ingredient3Amount, FILTER_SANITIZE_INT);
	$input_ingredient3Amount = intval($input_ingredient3Amount);
	
	$input_ingredient4Amount = strip_tags($_POST["ingredient4Amount"]);
	$input_ingredient4Amount = trim($input_ingredient4Amount);
	//$input_ingredient4Amount = filter_var($input_ingredient4Amount, FILTER_SANITIZE_INT);
	$input_ingredient4Amount = intval($input_ingredient4Amount);
	
	$input_stirred = strip_tags($_POST["stirred"]);
	$input_stirred = trim($input_stirred);
	//$input_stirred = filter_var($input_stirred, FILTER_SANITIZE_INT);
	$input_stirred = intval($input_stirred);
	
	$input_iced = strip_tags($_POST["iced"]);
	$input_iced = trim($input_iced);
	//$input_iced = filter_var($input_iced, FILTER_SANITIZE_INT);
	$input_iced = intval($input_iced);
	
	/* prepare statement */
	if ($stmt = $mysqli->prepare("INSERT INTO Drinks (drinkName, ingredient1Amount, ingredient2Amount, ingredient3Amount, ingredient4Amount, stirred, ice) VALUES (?,?,?,?,?,?,?);")) 
	{
		$stmt->bind_param('siiiiii', $input_drinkName, $input_ingredient1Amount, $input_ingredient2Amount, $input_ingredient3Amount, $input_ingredient4Amount, $input_stirred, $input_iced);
		
		if($stmt->execute())
		{
			//print("$drinkID ordered\n");
		}
		else
		{
			//print("Error: selected drink could not be ordered");
		}
		
		/* close statement */
		$stmt->close();
	}
	else
	{
		//print("Error: selected drink could not be ordered");
	}
	
	
	//Close the database connection
	$mysqli->close();

}
else
{
	//print("Error: no drinks selected");
}
?>

<html>
<head> 
	<link rel="stylesheet" type="text/css" href="style.css" /> 	
	<title>Francois</title>	
	<script src="jquery-1.7.2.min.js"></script>
	<script>
	<!--
     $(document).ready(function(){
       $(".drinkChoice").click(function(event){
         $(this).css('border-color','#333');
		 $(this).clone().appendTo("#queuedDrinksList").click(function(event){
			$(this).remove();
		}).mouseover(function(event){$(this).css('border-color','#F21E0F');}).mouseout(function(event){$(this).css('border-color','#333');});
       });
	   
	   $(".drinkChoice").mouseover(function(event){
         $(this).css('border-color','#09c');
       });
	   
	   $(".drinkChoice").mouseout(function(event){
         $(this).css('border-color','#333');
       });
	   
	   $("#makeQueuedDrinks").click(function(event){
         $("#queuedDrinksForm").submit();
       }).mouseover(function(event){$(this).css('background-color','#03BD00');}).mouseout(function(event){$(this).css('background-color','#09c');});
	   
	   $("#makeACustomDrink").click(function(event){
         window.location = "makeACustomDrink.php";
       });
	   
     });
	 -->
    </script>
	
</head>

<body>


<div id="app">
	<h1><img src="pic1.png" /><span id="title">Automatic Bartender</span><img src="pic2.png" /></h1> <!-- Student project. Images borrowed from http://www.mangotribe.com/wp-content/uploads/2012/03/1256720_glasses_2.jpg -->
	
	<?php
	
	if(isset($_POST["drinkID"])) 
	{
		$drinksOrdered = false;
		
		require('mysql.config.inc');
		
		$mysqli = new mysqli($host, $username, $password, $db);					
		
		foreach ($_POST["drinkID"] as $drinkID)
		{	
			$drinkID = strip_tags($drinkID);
			$drinkID = trim($drinkID);
			$drinkID = filter_var($drinkID, FILTER_SANITIZE_STRING);
			
			// prepare statement
			if ($stmt = $mysqli->prepare("INSERT INTO Orders (drinkID) VALUES (?);")) 
			{
				$stmt->bind_param('i', $drinkID);
				
				if($stmt->execute())
				{
					//print("$drinkID ordered\n");
					$drinksOrdered = true;
				}
				else
				{
					//print("Error: selected drink could not be ordered");
				}
				
				// close statement
				$stmt->close();
			}
			else
			{
				//print("Error: selected drink could not be ordered");
			}
		}
		
		//Close the database connection
		$mysqli->close();
		
		if($drinksOrdered == true){
			print("<h2 id=\"drinksOrdered\">Drinks Ordered</h2>");
		}

	}
	else
	{
		//print("Error: no drinks selected");
	}
	
	?>
	
	<form id="queuedDrinksForm" name="input" action="index.php" method="post">
	<div id="queuedDrinks" class="drinkChoices">
		<h2 id="queuedDrinksTitle">Queued Drinks</h2>
	
	
		<div id="queuedDrinksList"></div>
		
		<!--
		<div class="drinkChoice">
			<h3>Shirley Temple</h3>
			<ul>
				<li>2oz Lemon</li>
				<li>5oz Seltzer</li>
				<li>2oz Cherry</li>
				<li>&nbsp;</li>
				<li>Stirred</li>
				<li>Ice</li>
			</ul>
			<h4>Remove</h4>
		</div>
		<div class="drinkChoice">
			<h3>Grape Soda</h3>
			<ul>
				<li>4oz Grape</li>
				<li>5oz Seltzer</li>
				<li>&nbsp;<li>
				<li>&nbsp;</li>
				<li>Stirred</li>
				<li>Ice</li>
			</ul>
			<h4>Remove</h4>
		</div>
		<div class="drinkChoice">
			<h3>Lemon Soda</h3>
			<ul>
				<li>4oz Lemon</li>
				<li>5oz Seltzer</li>
				<li>&nbsp;</li>
				<li>&nbsp;</li>
				<li>Stirred</li>
				<li>Ice</li>
			</ul>
			<h4>Remove</h4>
		</div>
		<div class="drinkChoice">
			<h3>Cherry Soda</h3>
			<ul>
				<li>4oz Cherry</li>
				<li>5oz Seltzer</li>
				<li>&nbsp;</li>
				<li>&nbsp;</li>
				<li>Stirred</li>
				<li>Ice</li>
			</ul>
			<h4>Remove</h4>
		</div>
		-->
		<p class="bottomPad" id="queuedDrinksBottomPad">&nbsp;</p>
	</div>
	
	<h2 class="bigButton" id="makeQueuedDrinks">Make Queued Drinks</h2>
	<!--<input type="submit" value="Order" />-->
	</form>
	
	<div class="spacer"></div>
	
	<div class="drinkChoices">
		<h2>Add A Drink</h2>
		
		<?php
			require('mysql.config.inc');
			
			$mysqli = new mysqli($host, $username, $password, $db);					
			
			/* prepare statement */
			if ($stmt = $mysqli->prepare("SELECT d.drinkID, d.drinkName, d.ingredient1Amount, d.ingredient2Amount, d.ingredient3Amount, d.ingredient4Amount, d.stirred, d.ice FROM Drinks d;")) 
			{
				$stmt->execute();
				
				$stmt->bind_result($drinkID,$drinkName,$ingredient1Amount,$ingredient2Amount,$ingredient3Amount,$ingredient4Amount,$stirred,$ice);
				
				// fetch values
				while ($stmt->fetch()) 
				{
					/*
					print("<div class=\"drinkChoice sigDrink\">
							<h3>$drinkName</h3>
							<ul>
								<li>$ingredient1Amount oz Lemon</li>
								<li>$ingredient2Amount oz Seltzer</li>
								<li>$ingredient3Amount oz Cherry</li>
								<li>$ingredient4Amount oz Grape</li>");
					*/
					
					print("<div class=\"drinkChoice\">
							<h3>$drinkName</h3>
							<ul>");
					
					if($ingredient1Amount != 0)
					{
						print("<li>$ingredient1Amount oz Vodka</li>");
					}/*
					else
					{
						print("<li>&nbsp;</li>");
					}*/
					
					if($ingredient2Amount != 0)
					{
						print("<li>$ingredient2Amount oz Gin</li>");
					}/*
					else
					{
						print("<li>&nbsp;</li>");
					}*/
					
					if($ingredient3Amount != 0)
					{
						print("<li>$ingredient3Amount oz Cranberry Juice</li>");
					}/*
					else
					{
						print("<li>&nbsp;</li>");
					}*/
					
					if($ingredient4Amount != 0)
					{
						print("<li>$ingredient4Amount oz Tonic</li>");
					}/*
					else
					{
						print("<li>&nbsp;</li>");
					}*/
					
					if($stirred == 1)
					{
						print("<li>Stirred</li>");
					}/*
					else
					{
						//print("<li>Not stirred</li>");
						print("<li>&nbsp;</li>");
					}*/
					
					if($ice == 1)
					{
						print("<li>Iced</li>");
					}/*
					else
					{
						//print("<li>No ice</li>");
						print("<li>&nbsp;</li>");
					}*/
					
					////////////////////////////////////
					// Fixes Height Issues
					
					if($ingredient1Amount == 0)
					{
						print("<li>&nbsp;</li>");
					}
					if($ingredient2Amount == 0)
					{
						print("<li>&nbsp;</li>");
					}
					if($ingredient3Amount == 0)
					{
						print("<li>&nbsp;</li>");
					}
					if($ingredient4Amount == 0)
					{
						print("<li>&nbsp;</li>");
					}
					if($stirred == 0)
					{
						print("<li>&nbsp;</li>");
					}
					if($ice == 0)
					{
						print("<li>&nbsp;</li>");
					}
					
					//////////////////////////////////
					
					
					
					
					
					print("</ul>
							<input type=\"hidden\" name=\"drinkID[]\" value=\"$drinkID\" />
						</div>\n");
				}
				
				/* close statement */
				$stmt->close();
			}
			
			//Close the database connection
			$mysqli->close();
		?>

		<!--
		<div class="drinkChoice sigDrink">
			<h3>Shirley Temple</h3>
			<ul>
				<li>2oz Lemon</li>
				<li>5oz Seltzer</li>
				<li>2oz Cherry</li>
				<li>&nbsp;</li>
				<li>Stirred</li>
				<li>Ice</li>
			</ul>
			<input type="hidden" name="drinkID[]" value="1" />
		</div>
		
		
		<div class="drinkChoice sigDrink">
			<h3>Grape Soda</h3>
			<ul>
				<li>4oz Grape</li>
				<li>5oz Seltzer</li>
				<li>&nbsp;<li>
				<li>&nbsp;</li>
				<li>Stirred</li>
				<li>Ice</li>
			</ul>
			<input type="hidden" name="drinkID[]" value="2" />
		</div>
		<div class="drinkChoice sigDrink">
			<h3>Lemon Soda</h3>
			<ul>
				<li>4oz Lemon</li>
				<li>5oz Seltzer</li>
				<li>&nbsp;</li>
				<li>&nbsp;</li>
				<li>Stirred</li>
				<li>Ice</li>
			</ul>
			<input type="hidden" name="drinkID[]" value="3" />
		</div>
		<div class="drinkChoice sigDrink">
			<h3>Cherry Soda</h3>
			<ul>
				<li>4oz Cherry</li>
				<li>5oz Seltzer</li>
				<li>&nbsp;</li>
				<li>&nbsp;</li>
				<li>Stirred</li>
				<li>Ice</li>
			</ul>
			<input type="hidden" name="drinkID[]" value="4" />
		</div>
		<div class="drinkChoice sigDrink">
			<h3>Le Soda</h3>
			<ul>
				<li>4oz Lemon</li>
				<li>5oz Seltzer</li>
				<li>&nbsp;</li>
				<li>&nbsp;</li>
				<li>Stirred</li>
				<li>Ice</li>
			</ul>
			<input type="hidden" name="drinkID[]" value="5" />
		</div>
		<div class="drinkChoice sigDrink">
			<h3>Ch Soda</h3>
			<ul>
				<li>4oz Cherry</li>
				<li>5oz Seltzer</li>
				<li>&nbsp;</li>
				<li>&nbsp;</li>
				<li>Stirred</li>
				<li>Ice</li>
			</ul>
			<input type="hidden" name="drinkID[]" value="5" />
		</div>
		-->
		<p class="bottomPad">&nbsp;</p>
		
	</div>
	
	-<h2 class="bigButton" id="makeACustomDrink">Create A Custom Drink</h2>
	
	<!--
	<form name="input" action="orderDrinks.php" method="post">
	Drink name: <input type="text" name="drinkName" /><br />
	<input type="submit" value="Order" />
	</form>
	-->
</div>

</body>
</html>