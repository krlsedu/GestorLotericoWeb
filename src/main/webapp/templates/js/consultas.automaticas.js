/**
 * Created by CarlosEduardo on 13/05/2017.
 */
function eventoOnKeyUp(element, camposCons, parsCampos, buscarNome) {
    var campos = camposCons.split(",");
    var pars = parsCampos.split(",");
    if(( typeof(buscarNome) === "undefined" || buscarNome === null ) ){
        buscarNome = true;
    }
    var dones = campos.length-1;
    if(buscarNome) {
        buscaNome($(element).parent().attr('id'), $(element).attr('id'));
    }
    if(campos.length>1) {
        switch (dones) {
            case 2:
                consultaCampoAutoLoopCheck(campos[0], (pars[0].toLowerCase() === 'true'), element).done(function () {
                    consultaCampoAutoLoopCheck(campos[1], (pars[1].toLowerCase() === 'true'), element);
                }).done(function () {
                    for (i = dones; i < campos.length; i++) {
                        consultaCampoAutoLoopCheck(campos[i], (pars[i].toLowerCase() === 'true'), element);
                    }
                });
                break;
            default:
                consultaCampoAutoLoopCheck(campos[0], (pars[0].toLowerCase() === 'true'), element).done(function () {
                    for (i = 1; i < campos.length; i++) {
                        consultaCampoAutoLoopCheck(campos[i], (pars[i].toLowerCase() === 'true'), element);
                    }
                });
        }
    }else {
        consultaCampoAutoLoopCheck(campos[0], (pars[0].toLowerCase() === 'true'), element);
    }
}

function consultaCampoAutoLoopCheck(valorBuscar,loop,element){
    var r = $.Deferred();
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
                        r.resolve();
                    },
                    error: function (jXHR, textStatus, errorThrown) {
                        r.resolve();
                        avisoErros(jXHR, textStatus, errorThrown);
                    }
                }
            )
        }else{
            r.resolve();
        }
    }else {
        r.resolve();
    }
    return r;
}
