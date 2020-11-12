<html>
    <head>
        <meta charset="utf-8">
        <title>Cadastre-se</title>
    </head>
    <body>
        Cadastre-se
        <br>
        <br>
        <form name="formRegister" method="post" action="cadastro.php" enctype="multipart/form-data">
            <label>Nome: </label> <input type="text" name="nameRegister"  required>
            <br>
            <br>
            <label>Cpf: </label> <input type="text" name="cpfRegister" required>
            <br>
            <br>
            <label>Rg: </label> <input type="text" name="rgRegister" required>
            <br>
            <br>
            <label>Email: </label> <input type="email" name="emailRegister" required>
            <br>
            <br>
            <label>Senha: </label> <input type="password" name="passwordRegister" required>
            <br>
            <br>
            <label>Confirme sua senha: </label> <input type="password" name="passRegister"  required>
            <br>
            <br>
            <label>Telefone: </label> <input type="text" name="telephoneRegister"  required>
            <br>
            <br>
            <label>Foto de perfil: </label> <input type="file" name="imgRegister" required>
            <br>
            <br>
            <a href="index.php">Já possui cadastro</a>
            <br>
            <br>
            <input type="submit" name="btnRegister" id="btnRegister" value="Cadastrar">
        </form>
    </body>
</html>