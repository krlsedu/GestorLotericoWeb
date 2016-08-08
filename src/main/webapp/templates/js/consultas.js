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

function ativaPopUp(tipo,colunaBuscar){
    if(tipo === 'id'){
        var tabela = document.getElementById("it").value;
        buscaOpcoesCBPopUp(tabela);
    }else{
        buscaOpcoesCBPopUp(tipo);       
    }  
    document.getElementById("tipo_busca").value = tipo;
    document.getElementById("col_buscar").value = colunaBuscar;
    document.getElementById('popup').style.display='block';  
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
                alert("Desculpe ocorreu um erro! :(");
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
                alert("Desculpe ocorreu um erro! :(");
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
                alert("Desculpe ocorreu um erro! :(");
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
                    document.getElementById(nomeCol).value = valr;
                }
                fechaPopUp();
            }, 
            error: function (jXHR, textStatus, errorThrown) {
                alert("Desculpe ocorreu um erro! :(");
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
                alert("Desculpe ocorreu um erro! :(");
            }            
        }
    );
}

$("document").ready(function () {
    $("#botao-gravar").click(function () {
        $('#form_dados').submit(function (e) {
            e.preventDefault();
            $.ajax({
                type: "POST",
                url:  "grava",
                data: $("#form_dados").serialize(),
                success: function (data) {    
                    setaIdEBusca(data.trim());
                    alert("Gravado com Sucesso!");
                }, 
                error: function (jXHR, textStatus, errorThrown) {
                    alert("Desculpe ocorreu um erro! :("+textStatus);
                }
            }); 
        });
    });     
});

function deletarDados(){
    var id = document.getElementById("id").value;
    $.ajax(
        {
            type: "POST",
            url:  "grava",
            data:{"id":id,"tipo":"rm","tabela":document.getElementById("it").value},
            success: function (data) {  
                alert("Excluído com Sucesso!");
                limpa();
            }, 
            error: function (jXHR, textStatus, errorThrown) {
                alert("Desculpe ocorreu um erro! :(");
            } 
        }
    );
}

function setaIdFk(id,campo){
    document.getElementById(campo).value = id;
    fechaPopUp();
}

function fechaPopUp(){    
    document.getElementById('popup').style.display='none';
    document.getElementById('dados_busca').value = '';
    document.getElementById("tab_dados").innerHTML='';
}

function limpa(){    
    document.getElementById('id').value='0';
}
