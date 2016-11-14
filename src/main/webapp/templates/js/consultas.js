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
                document.getElementById("corpo_aviso").innerHTML= "<div class=\"alert alert-danger\" role=\"alert\" style=\"text-align: center\"> Desculpe ocorreu um erro! :(<br> "+jXHR+textStatus+errorThrown+"</div>";
                $('#modal_avisos').modal('show');   
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
                document.getElementById("corpo_aviso").innerHTML= "<div class=\"alert alert-danger\" role=\"alert\" style=\"text-align: center\"> Desculpe ocorreu um erro! :(<br> "+jXHR+textStatus+errorThrown+"</div>";
                $('#modal_avisos').modal('show');   
            }             
        }
    );
}

function buscaConteudoTela(tela){    
    $.ajax(
        {
            type: "POST",
            url:  "conteudo_telas",
            data:{"it":tela},
            success: function (data) {   
                $("#conteudo_telas").html(data);
                //document.getElementById("conteudo_telas").innerHTML = data;
            }, 
            error: function (jXHR, textStatus, errorThrown) {
                document.getElementById("corpo_aviso").innerHTML= "<div class=\"alert alert-danger\" role=\"alert\" style=\"text-align: center\"> Desculpe ocorreu um erro! :(<br> "+jXHR+textStatus+errorThrown+"</div>";
                $('#modal_avisos').modal('show');   
            }             
        }
    );
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
                document.getElementById("corpo_aviso").innerHTML= "<div class=\"alert alert-danger\" role=\"alert\" style=\"text-align: center\"> Desculpe ocorreu um erro! :(<br> "+jXHR+textStatus+errorThrown+"</div>";
                $('#modal_avisos').modal('show');   
            }             
        }
    );
}

function setaIdEBusca(id){
    document.getElementById("id").value = id;
    buscaDadosN(document.getElementById("it").value);
}

function buscaDadosN(tabela){
    var id = document.getElementById("id").value;
    $.ajax(
        {
            type: "POST",
            url:  "consulta",
            data:{"id":id,"tabela":tabela,"tipo":"dados"},
            success: function (data) {     
                var parser = new DOMParser();
                var htmlDoc = parser.parseFromString(data, "text/html");
                var ncols = htmlDoc.getElementById("num_cols").value;
                for (i=1;i<=ncols;i++){
                    var nomeCol = htmlDoc.getElementById("nome_coluna_"+i).value;
                    var valr = htmlDoc.getElementById("busca_col_"+nomeCol).value;
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
                }
                fechaPopUp();
            }, 
            error: function (jXHR, textStatus, errorThrown) {
                document.getElementById("corpo_aviso").innerHTML= "<div class=\"alert alert-danger\" role=\"alert\" style=\"text-align: center\"> Desculpe ocorreu um erro! :(<br> "+jXHR+textStatus+errorThrown+"</div>";
                $('#modal_avisos').modal('show');   
            }             
        }
    );
}

function consultaCampoAuto(valorBuscar){
    var tabela = document.getElementById("it").value;
    $.ajax(
            {                
                type: "POST",
                url:  "consulta",
                data: $("#form_dados").serialize()+'&'+$.param({"valor_buscar":valorBuscar,"tabela":tabela,"tipo":"dado"}),
                success: function (data) {     
                    var parser = new DOMParser();
                    var htmlDoc = parser.parseFromString(data, "text/html");
                    var nomeCol = htmlDoc.getElementById("nome_coluna").value;
                    var valr = htmlDoc.getElementById("valor").value;
                    if(!(valr===null||valr==='null')){
                        $('input#'+nomeCol).val(valr ).trigger('mask.maskMoney');
                        try{
                            document.getElementById(nomeCol).onkeyup();
                        }catch (e){

                        }
                    }  
                }, 
                error: function (jXHR, textStatus, errorThrown) {
                    document.getElementById("corpo_aviso").innerHTML= "<div class=\"alert alert-danger\" role=\"alert\" style=\"text-align: center\"> Desculpe ocorreu um erro! :(<br> "+jXHR+textStatus+errorThrown+"</div>";
                    $('#modal_avisos').modal('show');   
                }
            }
    )    
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
                document.getElementById("corpo_aviso").innerHTML= "<div class=\"alert alert-danger\" role=\"alert\" style=\"text-align: center\"> Desculpe ocorreu um erro! :(<br> "+jXHR+textStatus+errorThrown+"</div>";
                $('#modal_avisos').modal('show');   
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
                document.getElementById("corpo_aviso").innerHTML= "<div class=\"alert alert-danger\" role=\"alert\" style=\"text-align: center\"> Desculpe ocorreu um erro! :(<br> "+jXHR+textStatus+errorThrown+"</div>";
                $('#modal_avisos').modal('show');   
            }            
        }
    );
}

function deletarDados(){
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
                document.getElementById("corpo_aviso").innerHTML= "<div class=\"alert alert-danger\" role=\"alert\" style=\"text-align: center\"> Desculpe ocorreu um erro! :(<br> "+jXHR+textStatus+errorThrown+"</div>";
                $('#modal_avisos').modal('show');       
            } 
        }
    );
}

function setaIdFk(id,campo,nome){
    document.getElementById(campo).value = id;
    document.getElementById('label_'+campo).innerHTML = nome;
    fechaPopUp();
}

function fechaPopUp(){    
    $('#myModal').modal('hide');
    document.getElementById('dados_busca').value = '';
    document.getElementById("tab_dados").innerHTML='';
}

function limpa(){    
    buscaConteudoTela(document.getElementById("it").value);
}

$(document).on("submit", '#form_dados', function(event) { 
    event.preventDefault();  
    $.ajax({
        type: "POST",
        url:  "grava",
        data: $("#form_dados").serialize(),
        success: function (data) {    
            setaIdEBusca(data.trim());
            document.getElementById("corpo_aviso").innerHTML= "<div class=\"alert alert-success\" role=\"alert\" style=\"text-align: center\"> O registro foi gravado com sucesso! </div>";
            $('#modal_avisos').modal('show');
            limpa();
        }, 
        error: function (jXHR, textStatus, errorThrown) {
            document.getElementById("corpo_aviso").innerHTML= "<div class=\"alert alert-danger\" role=\"alert\" style=\"text-align: center\"> Desculpe ocorreu um erro! :(<br> "+jXHR+textStatus+errorThrown+"</div>";
            $('#modal_avisos').modal('show');   
        }
    });     
    $("#botao-gravar").off("click");
});
