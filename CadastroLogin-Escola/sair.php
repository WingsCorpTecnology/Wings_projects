<?php
	session_start();
	
	$_SESSION['logindeslogado'] = "Deslogado com sucesso";
	//redirecionar o usuario para a página de login
	header("Location: index.php");
?>