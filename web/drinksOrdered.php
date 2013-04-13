<?php
	require('mysql.config.inc');
	
	$mysqli = new mysqli($host, $username, $password, $db);					
	
	/* prepare statement */
	if ($stmt = $mysqli->prepare("SELECT o.orderID, d.drinkID, d.drinkName, d.ingredient1Amount, d.ingredient2Amount, d.ingredient3Amount, d.ingredient4Amount, d.stirred, d.ice FROM Orders o, Drinks d WHERE o.drinkID = d.drinkID;")) 
	{
		$stmt->execute();
		
		$stmt->bind_result($c1,$c2,$c3,$c4,$c5,$c6,$c7,$c8,$c9);
		
		// fetch values
		while ($stmt->fetch()) 
		{
			print("$c1,$c2,$c3,$c4,$c5,$c6,$c7,$c8,$c9;");
		}
		
		/* close statement */
		$stmt->close();
	}
	
	//Close the database connection
	$mysqli->close();
?>