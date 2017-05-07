function CriaRequest() { 
     try{ 
         request = new XMLHttpRequest(); 
     }catch (IEAtual){ 
         try{ 
             request = new ActiveXObject("Msxml2.XMLHTTP"); 
         }catch(IEAntigo){ 
             try{ 
                 request = new ActiveXObject("Microsoft.XMLHTTP"); 
             }catch(falha){ 
                 request = false; 
             } 
         } 
     } 
     if (!request) 
         alert("Seu Navegador não suporta Ajax!"); 
     else 
         return request; 
 }  

function ativaPopUp(tipo,colunaBuscar,nomeBusca){
    if(tipo === 'id'){
        var tabela = document.getElementById("it").value;
        buscaOpcoesCBPopUp(tabela);
    }else{
        buscaOpcoesCBPopUp(tipo);       
    }  
    document.getElementById("tipo_busca").value = tipo;
    document.getElementById("col_buscar").value = colunaBuscar;
    document.getElementById("titulo_busca").innerHTML = nomeBusca;
    $('#myModal').modal('show');
}

function buscaOpcoesCBPopUp(tabela){
    $.ajax(
        {
            type: "POST",
            url:  "consulta",
            data:{"tipo":"opt","tabela":tabela},
            success: function (data) {     
                document.getElementById("selector1").innerHTML = data;
            }, 
            error: function (jXHR, textStatus, errorThrown) {
                avisoErros(jXHR,textStatus,errorThrown);
            }             
        }
    );
}
function consultaDadosBusca(){
    var tipo = document.getElementById("tipo_busca").value;
    var colunaBuscar = document.getElementById("col_buscar").value;
    if(tipo === 'id'){
        var tabela = document.getElementById("it").value;
        consultaDadosBuscaId(tabela);
    }else{
        consultaDadosFk(tipo,colunaBuscar);       
    } 
}
        
function consultaDadosBuscaId(tabela){    
    var id = document.getElementById("dados_busca").value;
    var sel = document.getElementById("selector1");
    var coluna = sel.options[sel.selectedIndex].value;
    $.ajax(
        {
            type: "POST",
            url:  "consulta",
            data:{"id":id,"coluna":coluna,"tabela":tabela,"tipo":"busca"},
            success: function (data) {   
                document.getElementById("tab_dados").innerHTML = data;
            }, 
            error: function (jXHR, textStatus, errorThrown) {
                avisoErros(jXHR,textStatus,errorThrown);
            }             
        }
    );
}

function buscaConteudoTela(tela){     
    $('#modal_carregando').modal('show');
    $.ajax(
        {
            type: "POST",
            url:  "conteudo_telas",
            data:{"it":tela},
            success: function (data) {   
                $("#conteudo_telas").html(data);
                $('#modal_carregando').modal('hide');
                try{
                    $('#id_terminal').focus();

                }catch (e){
                }
            }, 
            error: function (jXHR, textStatus, errorThrown) {
                avisoErros(jXHR,textStatus,errorThrown);
            }
        }
    );
}

function addItem(tela){    
    var num_linhas = parseInt(document.getElementById("num_linhas").value);
    num_linhas += 1;
    document.getElementById("num_linhas").value = num_linhas;
    $.ajax(
        {
            type: "POST",
            url:  "conteudo_telas",
            data:{"it":tela,"num_linhas":num_linhas},
            success: function (data) {   
                $("#btns-add-rm-linhas").before(data);
            }, 
            error: function (jXHR, textStatus, errorThrown) {
                avisoErros(jXHR,textStatus,errorThrown);
            }             
        }
    );
}

function rmItem(){
    rmItem(null,null);
}

function rmItem(linha,elementos){
    var num_linhas = parseInt(document.getElementById("num_linhas").value);
    if(!(linha===null||linha==='null')&&num_linhas!==linha){
        var element = document.getElementById("item_"+linha);
        var elements = elementos.split(",");
        var i = elements.length;
        while(i--){
            document.getElementById(elements[i].replace(linha,num_linhas).trim()).setAttribute('name', elements[i].trim());
            document.getElementById(elements[i].replace(linha,num_linhas).trim()).setAttribute('id', elements[i].trim()); 
        } 
        document.getElementById("item_"+num_linhas).setAttribute('id', "item_"+linha); 
        document.getElementById("id_"+num_linhas).setAttribute('name', "id_"+linha); 
        document.getElementById("id_"+num_linhas).setAttribute('id', "id_"+linha); 
        document.getElementById('rm_btn_'+num_linhas).setAttribute('onclick',"rmItem('"+linha+"','label_id_operacao_"+linha+" , id_operacao_"+linha+" , quantidade_"+linha+"')");
        document.getElementById('rm_btn_'+num_linhas).setAttribute('id', 'rm_btn_'+linha);  
        num_linhas -= 1;
        document.getElementById("num_linhas").value = num_linhas;
        element.parentNode.removeChild(element);
        element.remove(); 
    }else{
        var num_linhas = parseInt(document.getElementById("num_linhas").value);
        var element = document.getElementById("item_"+num_linhas);
        num_linhas -= 1;
        document.getElementById("num_linhas").value = num_linhas;
        element.parentNode.removeChild(element);
        element.remove();        
    }
}

function consultaDadosFk(tabela,colunaBuscar){
    var id = document.getElementById("dados_busca").value;
    var sel = document.getElementById("selector1");
    var coluna = sel.options[sel.selectedIndex].value;
    $.ajax(
        {
            type: "POST",
            url:  "consulta",
            data:{"id":id,"coluna":coluna,"tabela":tabela,"tipo":"busca","coluna_buscar":colunaBuscar},
            success: function (data) {     
                document.getElementById("tab_dados").innerHTML = data;
            }, 
            error: function (jXHR, textStatus, errorThrown) {
                avisoErros(jXHR,textStatus,errorThrown);
            }             
        }
    );
}

function setaIdEBusca(id){
    document.getElementById("id").value = id;
    buscaDadosN(document.getElementById("it").value);
}

function buscaDadosN(tabela){
    $('#modal_carregando').modal('show');
    var id = document.getElementById("id").value;
    $.ajax(
        {
            type: "POST",
            url:  "consulta",
            data:{"id":id,"tabela":tabela,"tipo":"dados"},
            success: function (data) {     
                var parser = new DOMParser();
                var htmlDoc = parser.parseFromString(data, "text/html");
                try{
                    var nLinhas = htmlDoc.getElementById("num_linhas").value;
                    var nomeLinhas = htmlDoc.getElementById("nome_linhas").value;
                    if(!(nLinhas===null||nLinhas==='null')){
                        var numLinhas = parseInt(document.getElementById("num_linhas").value);
                        if(numLinhas<parseInt(nLinhas)){
                            for(i=numLinhas;i<parseInt(nLinhas);i++){
                                addItem(nomeLinhas);            
                            }
                        }else{
                            for(i=parseInt(nLinhas);i<numLinhas;i++){
                                rmItem(null);
                            }
                        }
                    }                
                    setTimeout(function() {insereDados(htmlDoc);}, 100);
                }catch (e){
                    insereDados(htmlDoc);
                }
            }, 
            error: function (jXHR, textStatus, errorThrown) {
                avisoErros(jXHR,textStatus,errorThrown);
            }             
        }
    );
    fechaPopUp(); 
}

function insereDados(htmlDoc){
    var ncols = htmlDoc.getElementById("num_cols").value;
    
    for (i=1;i<=ncols;i++){
        try{
            var nomeCol = htmlDoc.getElementById("nome_coluna_"+i).value;
            var valr = htmlDoc.getElementById("busca_col_"+nomeCol).value;
            try{
                var tipo = document.getElementById(nomeCol).type;
                if(!(valr===null||valr==='null')){
                    if(tipo==="select-one"||tipo==="textarea"){
                            document.getElementById(nomeCol).value = valr;
                    }else{
                            $('input#'+nomeCol).val(valr).trigger('mask.maskMoney');
                    }                        
                    try{
                        document.getElementById(nomeCol).onchange();
                    }catch (e){
                    }
                }
            }catch (ex){
                setTimeout(function() {insereDados(htmlDoc);}, 1000);                
            }
        }catch (e){

        }
    }
    $('#modal_carregando').modal('hide');   
}

function  consultaCampoAuto(valorBuscar,element) {
    consultaCampoAutoLoopCheck(valorBuscar,true,element);
}

function consultaCampoAutoLoopCheck(valorBuscar,loop,element){
    if(element.value !== "") {
        var tabela = document.getElementById("it").value;
        if (loop || document.getElementById(valorBuscar).value === "") {
            $.ajax(
                {
                    type: "POST",
                    url: "consulta",
                    data: $("#form_dados").serialize() + '&' + $.param({
                        "valor_buscar": valorBuscar,
                        "tabela": tabela,
                        "tipo": "dado"
                    }),
                    success: function (data) {
                        var parser = new DOMParser();
                        var htmlDoc = parser.parseFromString(data, "text/html");
                        var nomeCol = htmlDoc.getElementById("nome_coluna").value;
                        var valr = htmlDoc.getElementById("valor").value;
                        if (!(valr === null || valr === 'null')) {
                            $('input#' + nomeCol).val(valr).trigger('mask.maskMoney');
                            try {
                                if (tabela === 'movimentos_caixas' || tabela === 'abertura_terminais' || !loop) {
                                    document.getElementById(nomeCol).onchange();
                                }
                                document.getElementById(nomeCol).onkeyup();
                            } catch (e) {

                            }
                        }
                    },
                    error: function (jXHR, textStatus, errorThrown) {
                        avisoErros(jXHR, textStatus, errorThrown);
                    }
                }
            )
        }
    }
}

function buscaNomeComponente(tabela,campo) {
    var id = document.getElementById(campo).value;
    if(id.length===0){
        id = 0;
    }
    $.ajax(
        {
            type: "POST",
            url:  "consulta",
            data:{"id":id,"tabela":tabela,"tipo":"nome"},
            success: function (data) {
                document.getElementById('label_'+campo).innerHTML = data;
                setaOnclickIdComponente(id,data);
            },
            error: function (jXHR, textStatus, errorThrown) {
                avisoErros(jXHR,textStatus,errorThrown);
            }
        }
    );
}

function setaOnclickIdComponente(id,nome) {
    $.ajax(
        {
            type: "POST",
            url:  "consulta",
            data:{"id":id,"tipo":"item_componente"},
            success: function (data) {
                var dadoSplit = data.split(';');
                document.getElementById('label_item_componente').innerHTML = nome;
                document.getElementById('item_buscar_componente').setAttribute('onclick',"ativaPopUp('"+dadoSplit[0].trim()+"','id_item_componente' , 'Busca Id "+nome.trim()+"')");
            },
            error: function (jXHR, textStatus, errorThrown) {
                avisoErros(jXHR,textStatus,errorThrown);
            }
        }
    );

}

function buscaNome(tabela,campo){
    var id = document.getElementById(campo).value;
    if(id.length===0){
        id = 0;
    }
    $.ajax(
        {
            type: "POST",
            url:  "consulta",
            data:{"id":id,"tabela":tabela,"tipo":"nome"},
            success: function (data) {     
                document.getElementById('label_'+campo).innerHTML = data;
            }, 
            error: function (jXHR, textStatus, errorThrown) {
                avisoErros(jXHR,textStatus,errorThrown);
            }             
        }
    );
}

function btnsPercorrer(btn){
    var id = document.getElementById("id").value;
    var tabela = document.getElementById("it").value;
    $.ajax(
        {
            type: "POST",
            url:  "consulta",
            data:{"id":id,"tabela":tabela,"tipo":"id","btn":btn},
            success: function (data) {   
                setaIdEBusca(data.trim());
            }, 
            error: function (jXHR, textStatus, errorThrown) {
                avisoErros(jXHR,textStatus,errorThrown);
            }            
        }
    );
}

function deletarDados(){
    $('#modal_carregando').modal('show');
    var id = document.getElementById("id").value;
    $.ajax(
        {
            type: "POST",
            url:  "grava",
            data:{"id":id,"tipo":"rm","tabela":document.getElementById("it").value},
            success: function (data) {  
                document.getElementById("corpo_aviso").innerHTML= "<div class=\"alert alert-success\" role=\"alert\" style=\"text-align: center\"> O registro foi removido com sucesso! </div>";
                $('#modal_avisos').modal('show');
                limpa();
            }, 
            error: function (jXHR, textStatus, errorThrown) {
                avisoErros(jXHR,textStatus,errorThrown);
            } 
        }
    );
}


function sair(tela){
    $('#modal_carregando').modal('show');
    $.ajax(
        {
            type: "POST",
            url:  "logout",
            success: function (data) {
                $(location).attr('href',"index.jsp");
            },
            error: function (jXHR, textStatus, errorThrown) {
                avisoErros(jXHR,textStatus,errorThrown);
            }
        }
    );
}

function setaIdFk(id,campo,nome){
    document.getElementById(campo).value = id;
    try {
        document.getElementById(campo).onkeyup();
    } catch (e) {

    }
    document.getElementById('label_'+campo).innerHTML = nome;
    if(campo==='id_componente'){
        setaOnclickIdComponente(id,nome);
    }
    fechaPopUp();
}

function fechaPopUp(){    
    $('#myModal').modal('hide');
}

$(document).on('hide.bs.modal','#myModal', function () {
    document.getElementById('dados_busca').value = '';
    document.getElementById("tab_dados").innerHTML='';
});

function limpa(){    
    buscaConteudoTela(document.getElementById("it").value);
}

function enviaImagens(){
    $('#modal_upload').modal('show');    
}

function avisoErros(jXHR, textStatus, errorThrown) {
    $('#modal_carregando').modal('hide');
    if(jXHR.status===401){
        document.getElementById("corpo_aviso").innerHTML= "<div class=\"alert alert-success\" role=\"alert\" style=\"text-align: center\">"+jXHR.responseText+"</div>";
    }else{
        document.getElementById("corpo_aviso").innerHTML= "<div class=\"alert alert-danger\" role=\"alert\" style=\"text-align: center\">"+jXHR.responseText+"</div>";
    }
    $('#modal_avisos').modal('show');
}

function avisoErrosCTimeOut(jXHR, textStatus, errorThrown) {
    $('#modal_carregando').modal('hide');
    if(jXHR.status===401){
        document.getElementById("corpo_aviso").innerHTML= "<div class=\"alert alert-success\" role=\"alert\" style=\"text-align: center\">"+jXHR.responseText+"</div>";
    }else{
        document.getElementById("corpo_aviso").innerHTML= "<div class=\"alert alert-danger\" role=\"alert\" style=\"text-align: center\">"+jXHR.responseText+"</div>";
    }
    setTimeout(function() {$('#modal_avisos').modal('show');}, 100);
}

function acoesConsulta(it_sub,it_par) {
    $.ajax(
        {
            type: "POST",
            url: "consulta",
            data: it_par,
            success: function (data) {
                switch (it_sub){
                    case "id":
                        setaIdEBusca(data);
                        break;
                    case "busca":
                        consultaDadosBusca();
                        break;
                    case "opt":
                        var tipo = document.getElementById("tipo_busca").value;
                        if(tipo === 'id'){
                            var tabela = document.getElementById("it").value;
                            buscaOpcoesCBPopUp(tabela);
                        }else{
                            buscaOpcoesCBPopUp(tipo);
                        }
                        break;
                    default:
                        alert(it_sub + " não implementado");
                        break;
                }
            },
            error: function (jXHR, textStatus, errorThrown) {
                avisoErros(jXHR,textStatus,errorThrown);
            }
        }
    )
}

function avisaOk() {
    document.getElementById("corpo_aviso").innerHTML= "<div class=\"alert alert-success\" role=\"alert\" style=\"text-align: center\"> O registro foi gravado com sucesso! </div>";
    setTimeout(function() {$('#modal_avisos').modal('show');}, 300);
    limpa();
}

function gravaRelogin(it_par) {
    $.ajax({
        type: "POST",
        url:  "grava",
        data: it_par,
        success: function (data) {
            avisaOk();
        },
        error: function (jXHR, textStatus, errorThrown) {
            avisoErrosCTimeOut(jXHR,textStatus,errorThrown);
        }
    });
}

$(document).on("submit", '#form_dados', function(event) {
    $('#modal_carregando').modal('show');
    event.preventDefault();
    $.ajax({
        type: "POST",
        url:  "grava",
        data: $("#form_dados").serialize(),
        success: function (data) {
            avisaOk();
        },
        error: function (jXHR, textStatus, errorThrown) {
            avisoErrosCTimeOut(jXHR,textStatus,errorThrown);
        }
    });
    $("#botao-gravar").off("click");
});

$(document).on("click", '#grava_config_estatisticas', function(event) {
    $('#modal_carregando').modal('show');
    event.preventDefault();
    $.ajax({
        type: "POST",
        url:  "grava",
        data: $("#form_estatisticas").serialize()+'&'+$.param({"estilo_widget":document.getElementById("estilo_widget").innerHTML}),
        success: function (data) {
            //setaIdEBusca(data.trim());
            $('#modal_edit_configs').modal('hide');
            $('#modal_carregando').modal('hide');
            avisaOk();
        },
        error: function (jXHR, textStatus, errorThrown) {
            $('#modal_edit_configs').modal('hide');
            avisoErrosCTimeOut(jXHR,textStatus,errorThrown);
        }
    });
    $("#grava_config_estatisticas").off("click");
});

$(document).on("click", '#login_interno_btn', function(event) {
    var user=$('#userId').val();
    var pwd=$('#pswId').val();
    var it=$('#it_sub').val();
    var it_redirect=$('#it_redirect').val();
    var it_par=$('#it_par').val();
    $.ajax(
        {
            type: "POST",
            url:  "auth",
            data:$("#form_login").serialize(),
            success: function (data) {
                if(data.trim()!='False'){
                    $('#modal_avisos').modal('hide');
                    switch (it_redirect){
                        case "app":
                            buscaConteudoTela(it);
                            break;
                        case "consulta":
                            acoesConsulta(it,it_par);
                            break;
                        case "grava":
                            gravaRelogin(it_par);
                            break;
                    }
                }else{
                    alert('Fail....');
                }
            },
            error: function (jXHR, textStatus, errorThrown) {
                avisoErros(jXHR,textStatus,errorThrown);
            }
        });
    $("#login_interno_btn").off("click");
});

$(document).on('hidden.bs.modal', '#modal_avisos', function() {
    $("#id_terminal").focus();
});
$(document).on('hidden.bs.modal', '#modal_carregando', function() {
    $("#id_terminal").focus();
});