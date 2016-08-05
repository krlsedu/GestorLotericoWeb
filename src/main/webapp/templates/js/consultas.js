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
function setaIdEBusca(id,tabela){
    document.getElementById("id").value = id;
    buscaDadosN(tabela);s
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
                for (i=1;i<ncols;i++){
                    var nomeCol = htmlDoc.getElementById(i).value;
                    var valr = htmlDoc.getElementById(nomeCol).value;
                    document.getElementById(nomeCol).value = valr;
                }
            }            
        }
    );
}

 function buscaDados(id,coluna,tabela){
    var xmlreq = CriaRequest(); 
    var abrir = "consulta?id="+id+"&coluna="+coluna+"&tabela="+tabela;
    xmlreq.open("GET",abrir, true);
    
    xmlreq.onreadystatechange = function(){ 
        if (xmlreq.readyState == 4) { 
            if (xmlreq.status == 200) {
                var xmlString = xmlreq.responseText;
                var parser = new DOMParser();
                var htmlDoc = parser.parseFromString(xmlString, "text/html");
                var ncols = htmlDoc.getElementById("num_cols").value;
                for (i=1;i<ncols;i++){
                    var nomeCol = htmlDoc.getElementById(i).value;
                    var patern = document.getElementById(nomeCol).title;
                    var valr = htmlDoc.getElementById(nomeCol).value;
                    var temp = formataOnLoad(patern,valr);
                    document.getElementById(nomeCol).value = temp;
                    document.getElementById(nomeCol).alt = temp;
                }                
            }else{ 
                alert("Erro: " + xmlreq.statusText); 
            } 
        } 
    }; 
    xmlreq.send(null);     
}

