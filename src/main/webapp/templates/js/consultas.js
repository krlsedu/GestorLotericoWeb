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


function consultaDadosBusca(tabela){
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
                    alert(errorThrown);
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
            }
        }
    );
}

function fechaPopUp(){    
    document.getElementById('popup').style.display='none';
    document.getElementById('dados_busca').value = '';
    document.getElementById("tab_dados").innerHTML='';
}

function limpa(){    
    document.getElementById('id').value='0';
}
