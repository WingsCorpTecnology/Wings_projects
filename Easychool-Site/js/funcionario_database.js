const funcionario_database = {};

(function () {
        let funcionario_id = false;

        function new_funcionario(nome, email, cpf, /*senha*/ ) {
                const funcionario_data = {
                        email: email,
                        nome: nome,
                        cpf: cpf,
                        //senha: senha
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
                updates['/Funcionario/' + emailBase64] = funcionario_data;

                let funcionario_ref = firebase.database().ref();

                funcionario_ref.update(updates)
                        .then(function () {
                                return {
                                        sucesso: true,
                                        message: 'Funcionario cadastrado'
                                };
                        })
                        .catch(function (error) {
                                return {
                                        sucesso: false,
                                        message: 'Falha ao cadastrar o funcionario: ${error.message}'
                                };
                        })
                //window.location.href = "../CadastroFunci/cadastroFuncionario.html";

                /*firebase.auth().createUserWithEmailAndPassword(email, senha)
                        .then(function(){
                                return {
                                        sucess: true,
                                        message: 'Funcionario cadastrado'
                                };
                        })
                        .catch(function(error) {
                                console.error(error);
                      });*/
        }

        function remove_funcionario() {}

        function update_funcionario() {}

        async function cadastrar_funcionario_auth(email, senha) {

                return await firebase.auth().createUserWithEmailAndPassword(email, senha)
                        .then(() => {
                                return{
                                        cadastro: true
                                }
                        }).catch(data => {
                                // Handle Errors here.
                                return {
                                        cadastro: false,
                                        message: data.message
                                }
                                // ...
                        });
        }

        async function login_funcionario(email, senha) {
                return await firebase.auth().signInWithEmailAndPassword(email, senha)
                        .then(() => {
                                return {
                                        login: true
                                }
                        }).catch(data => {
                                return {
                                        login: false,
                                        message: data.message
                                }
                        });
        }

        funcionario_database.new = new_funcionario;
        funcionario_database.remove = remove_funcionario;
        funcionario_database.update = update_funcionario;
        funcionario_database.insert_auth = cadastrar_funcionario_auth;
        funcionario_database.login = login_funcionario;


})()
