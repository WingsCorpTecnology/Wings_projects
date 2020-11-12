<?php
    
    $conn = mysqli_connect('localhost', 'root', '', 'bdEscola');
        
    $conn->query("SET NAMES 'utf8'");
    $conn->query('SET character_set_connection=utf8');
    $conn->query('SET character_set_client=utf8');
    $conn->query('SET character_set_results=utf8');
        
    if(!$conn)
        die("Falha na conexão " . mysqli_connect_error());
    else
        //Sucesso

?>