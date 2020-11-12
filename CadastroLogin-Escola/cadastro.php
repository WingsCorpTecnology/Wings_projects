<?php
    header("Location: index.php");

    include_once("conexao.php");

    $name = $_POST['nameRegister'];
    $cpf = $_POST['cpfRegister'];
    $rg = $_POST['rgRegister'];
    $email = $_POST['emailRegister'];
    $password = $_POST['passwordRegister'];
    $telephone = $_POST['telephoneRegister'];
    $photo = $_FILES['imgRegister'];

    if(isset($photo)){
        $extensao = strtolower(substr($photo ['name'], -4));
        $novoNome = md5(time()) . $extensao;
        $dir = "assets/";
        
        move_uploaded_file($photo ['tmp_name'], $dir.$novoNome);
        
        $sqlImg = 
            "INSERT INTO tbFotoDiretor (fotoDiretor, dataFotoDiretor)
            VALUES ('$novoNome', NOW())";
        
        $executeImg = $conn->query($sqlImg);
        
    }

    $password = md5($password);

    $sql =
        "INSERT INTO tbDiretor (nome, cpf, rg, email, senha, telefone, foto)
        VALUES ('$name', '$cpf', '$rg', '$email', '$password', '$telephone', LAST_INSERT_ID())";

    $execute = $conn->query($sql);
    $conn->close();

?>