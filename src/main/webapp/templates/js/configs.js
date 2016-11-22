
function configWidget(linha,coluna){
    $('#modal_edit_configs').modal('show'); 
    $("#titulo_configs").html("Mostrador ("+linha+","+coluna+")");
}

function acaoSelectTipo(){
    $('#modal_widgets').modal('show'); 
    $("#tamanho_widget").width(500);
    $("#tamanho_widget").css("padding-top",100);
}
