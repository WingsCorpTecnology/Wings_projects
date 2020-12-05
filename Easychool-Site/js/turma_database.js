const turma_database = {};

(function () {
  let turma_id = false;

  async function new_turma(serie, total_vagas, vagas_ocupadas, periodo, data_hora) {
        

    const turma_data = {
      serie: serie,
      total_vagas: total_vagas,
      vagas_ocupadas: vagas_ocupadas,
      periodo: periodo,
      data_hora: data_hora
    };
    
    var idEscola = localStorage.getItem("escola_id");

    //if (!escola_id)
    turma_id = firebase.database().ref().child('Turmas').push().key;

    //escola_database.new('gustavo', 'gustavo@gmail.com', '03570-120', 'são paulo', 'são paulo', 'parque savoy city', 'monesia', '34', 'a', '767868957896');

    //../CadastroFunci/cadastroFuncionario.html


    // var escola64 = btoa(unescape(encodeURIComponent(escola)));

    //var escola64 = decodeURIComponent(escape(window.atob(escola)));

    let updates = {};
    updates['/Turmas/' + idEscola + '/' + turma_id + "/"] = turma_data;

    let turma_ref = firebase.database().ref();

    await turma_ref.update(updates)
      .then(function () {
        return {
          sucesso: true,
          message: 'Turma cadastrada'
        };
      })
      .catch(function (error) {
        return {
          sucesso: false,
          message: 'Falha ao cadastrar turma: ${error.message}'
        };
      })
  }

  function remove_turma() {}

  function update_turma() {}

  turma_database.new = new_turma;
  turma_database.remove = remove_turma;
  turma_database.update = update_turma;


})()
