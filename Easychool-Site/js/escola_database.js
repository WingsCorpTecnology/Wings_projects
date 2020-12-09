const escola_database = {};

(function () {
        let escola_id = false;
        
        async function new_telefone(telefone, id_escola){
                const tel_data = {
                        "Telefone": telefone
                }
                
                var id_tel = firebase.database().ref().child('TelefoneEscola').push().key;
                
                let updates = {};
                updates['/TelefoneEscola/' + id_escola + '/' + id_tel + '/'] = tel_data;
                
                let telefone_ref = firebase.database().ref();
                
                await telefone_ref.update(updates)
                        .then(function(){
                                return{
                                        sucess: true
                                }
                        })
                        .catch(function(error){
                                return{
                                        sucess: false
                                }
                        })
        }
        
        async function info_escola(infantil, fund_1, fund_2, medio, especial, eja, instituicao, id_escola){
                const nivel_data = {
                        "Educação Infantil": infantil,
                        "Ensino Fundamental I": fund_1,
                        "Ensino Fundamental II": fund_2,
                        "Ensino Médio": medio,
                        "Educação Especial": especial,
                        "EJA": eja,
                        "Tipo de Instituição": instituicao
                }
                
                let updates = {};
                updates['/Escola/' + id_escola + '/Filtros/'] = nivel_data;
                
                let info_escola_ref = firebase.database().ref();
                
                await info_escola_ref.update(updates)
                        .then(function(){
                                return{
                                        sucess: true,
                                        message: 'ok'
                                }
                        })
                        .catch(function(error){
                                return{
                                        sucess: false,
                                        message: '${error.message}'
                                }
                        })
        }

        async function new_escola(nome, email, cep, estado, cidade, bairro, rua, numero, complemento, cnpj) {
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

                await escola_ref.update(updates)
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
        escola_database.nivel = info_escola;
        escola_database.telefone = new_telefone;
})()
