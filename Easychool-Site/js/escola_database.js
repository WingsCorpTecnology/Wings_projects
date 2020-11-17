const escola_database = {};

(function () {
        let escola_id = false;

        function new_escola(nome, email, cep, estado, cidade, bairro, rua, numero, complemento, cnpj) {
                const escola_data = {
                        cnpj: cnpj,
                        complemento: complemento,
                        numero: numero,
                        rua: rua,
                        bairro: bairro,
                        cidade: cidade,
                        uf: estado,
                        cep: cep,
                        email: email,
                        nome: nome,
                };

                //if (!escola_id)
                        //escola_id = firebase.database().ref().child('escolas').push().key;
                
                //escola_database.new('gustavo', 'gustavo@gmail.com', '03570-120', 'são paulo', 'são paulo', 'parque savoy city', 'monesia', '34', 'a', '767868957896');
                
                //../CadastroFunci/cadastroFuncionario.html


                // var escola64 = btoa(unescape(encodeURIComponent(escola)));

                //var escola64 = decodeURIComponent(escape(window.atob(escola)));
                                
                var emailKey = email;
                emailKey.toString();
                                
                var emailBase64 = btoa(emailKey);
                
                console.log(emailBase64);
                
                let updates = {};
                updates['/Escola/' +emailBase64] = escola_data;

                let escola_ref = firebase.database().ref();

                escola_ref.update(updates)
                        .then(function () {
                                return {
                                        sucess: true,
                                        message: 'Escola cadastrada'
                                };
                        })
                        .catch(function (error) {
                                return {
                                        sucess: false,
                                        message: 'Falha ao cadastrar escola: ${error.message}'
                                };
                        })
                //window.location.href = "../CadastroFunci/cadastroFuncionario.html";

        }

        function remove_escola() {}

        function update_escola() {}

        escola_database.new = new_escola;
        escola_database.remove = remove_escola;
        escola_database.update = update_escola;
})()
