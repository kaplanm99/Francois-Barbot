<?php
	require('mysql.config.inc');
	
	$mysqli = new mysqli($host, $username, $password, $db);					
	
	$mysqli->query("DELETE FROM Orders WHERE 1=1;");
	
	//Close the database connection
	$mysqli->close();
?>