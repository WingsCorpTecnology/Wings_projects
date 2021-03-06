const funcionario_database = {};

(function () {
  let funcionario_id = false;

  async function new_funcionario(nome, email, cpf) {
    var teste = localStorage.getItem("id_item");
    var teste64 = btoa(teste);

    var escola_certa;

    escola_certa = teste64;

    const funcionario_data = {
      email: email,
      nome: nome,
      cpf: cpf,
      escola: escola_certa
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

    await funcionario_ref.update(updates)
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
  }

  function remove_funcionario() {}

  async function update_funcionario(nome, email, cpf) {

    var escola_id = localStorage.getItem("escola_id");

    var escola_certa;

    escola_certa = escola_id;

    const funcionario_data = {
      email: email,
      nome: nome,
      cpf: cpf,
      escola: escola_certa
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

    await funcionario_ref.update(updates)
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
  }

  async function cadastrar_funcionario_auth(email, senha) {

    return await firebase.auth().createUserWithEmailAndPassword(email, senha)
      .then(() => {
        return {
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
