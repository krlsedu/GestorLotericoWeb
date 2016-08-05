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
  function consultaDados(id,coluna,tabela){
    var xmlreq = CriaRequest(); 
    var abrir = "consulta?id="+id+"&coluna="+coluna+"&tabela="+tabela;
    xmlreq.open("GET",abrir, true); 
    
    xmlreq.onreadystatechange = function(){ 
        if (xmlreq.readyState == 4) { 
            if (xmlreq.status == 200) {
                var xmlString = xmlreq.responseText;
                var parser = new DOMParser();
                var htmlDoc = parser.parseFromString(xmlString, "text/html");
                document.getElementById("tab_dados").innerHTML = htmlDoc;
            }else{ 
                alert("Erro: " + xmlreq.statusText); 
            } 
        } 
    }; 
    xmlreq.send(null);     
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

