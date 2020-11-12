
<?php
    session_start();
?>

<html>

<head>
    <meta charset="utf-8">
    <title>Login</title>
</head>

<body>
    Login
    <br>
    <br>
    <form method="post" action="valida.php">
        <label>Email: </label> <input type="email" name="emailLogin" required>
        <br>
        <br>
        <label>Senha: </label> <input type="password" name="passwordLogin" required>
        <br>
        <br>
        <a href="telaCadastro.php">Cadastre-se</a>
        <br>
        <br>
        <input type="submit" name="btnLogin" id="btnLogin" value="Login">
    </form>
    <p>
        <?php
            if(isset($_SESSION['loginErro'])){
                echo $_SESSION['loginErro'];
                unset($_SESSION['loginErro']);
            }
        ?>
    </p>
</body>

</html>