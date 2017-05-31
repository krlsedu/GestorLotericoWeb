
function configWidget(linha,coluna){
    $('#modal_edit_configs').modal('show'); 
    $("#titulo_configs").html("Mostrador ("+linha+","+coluna+")");
    $("#linha").val(linha);
    $("#coluna").val(coluna);
}

function acaoSelectTipo(){
}

function acaoBtnUp(){
    
    $('#modal_upload').modal('show'); 
}

function selectEstilo(){
    $('#modal_widgets').modal('show'); 
    $("#tamanho_widget").width(500);
    $("#tamanho_widget").css("padding-top",100);
}

function addEstilo(componente){
    $("#estilo_widget").html($("#"+componente));
    $('#modal_widgets').modal('hide'); 
}
