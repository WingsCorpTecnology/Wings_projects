<?php
            session_start();

            if (!isset($_SESSION['Diretor'])) {
	 header("Location: index.php");
                $_SESSION['loginErro'] = "Usuário ou senha Inválido";
                
                //exit;
            }

?>