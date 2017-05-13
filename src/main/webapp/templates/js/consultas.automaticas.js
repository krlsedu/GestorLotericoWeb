/**
 * Created by CarlosEduardo on 13/05/2017.
 */
function campoIdTerminal(element) {
    consultaCampoAutoLoopCheck('id_funcionario',false,element).done(function(){
        consultaCampoAuto('data_hora_mov',element);
        consultaCampoAutoLoopCheck('id_cofre',false,element);
        buscaNome('terminais','id_terminal');
    });
}
function campoIdFuncionario(element) {
    consultaCampoAutoLoopCheck('id_terminal',false,element).done(function(){
        consultaCampoAuto('data_hora_mov',element);
        consultaCampoAutoLoopCheck('id_cofre',false,element);
        buscaNome('funcionarios','id_funcionario');
    });
}
