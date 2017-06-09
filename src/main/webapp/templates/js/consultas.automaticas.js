/**
 * Created by CarlosEduardo on 13/05/2017.
 */
var timeout;

function eventoOnKeyPress() {
    if(timeout){ clearTimeout(timeout);}
}

function eventoOnKeyUp(element, camposCons, parsCampos, buscarNome) {
    timeout = setTimeout(function() {
        var campos = camposCons.split(",");
        var pars = parsCampos.split(",");
        var tabela = document.getElementById("it").value;
        if (( typeof(buscarNome) === "undefined" || buscarNome === null )) {
            buscarNome = true;
        }
        var dones = campos.length - 1;
        if (buscarNome) {
            buscaNome($(element).parent().attr('id'), $(element).attr('id'));
        }
        if (campos.length > 1) {
            switch (dones) {
                case 2:
                    consultaCampoAutoLoopCheck(campos[0], (pars[0].toLowerCase() === 'true'), element).done(function () {
                        consultaCampoAutoLoopCheck(campos[1], (pars[1].toLowerCase() === 'true'), element).done(function () {
                            for (i = dones; i < campos.length; i++) {
                                consultaCampoAutoLoopCheck(campos[i], (pars[i].toLowerCase() === 'true'), element);
                            }
                            return $.Deferred().resolve();
                        });
                    }).done(function () {
                        existeTerminalAberto(tabela);
                    });
                    break;
                default:
                    consultaCampoAutoLoopCheck(campos[0], (pars[0].toLowerCase() === 'true'), element).done(function () {
                        for (i = 1; i < campos.length; i++) {
                            consultaCampoAutoLoopCheck(campos[i], (pars[i].toLowerCase() === 'true'), element);
                        }
                        return $.Deferred().resolve();
                    }).done(function () {
                        existeTerminalAberto(tabela);
                    });
            }
        } else {
            consultaCampoAutoLoopCheck(campos[0], (pars[0].toLowerCase() === 'true'), element);
        }
    },500);
}

function consultaCampoAutoLoopCheck(valorBuscar,loop,element){
    var r = $.Deferred();
    var tabela = document.getElementById("it").value;
    var vlb = $('#'+valorBuscar);

    if(element.value !== "") {
        if (loop || vlb.val() === "" || vlb.val() === "0") {
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
                        console.log('consultaCampoAutoLoopCheck',valorBuscar);
                        console.log('consultaCampoAutoLoopCheck val',valr);
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

function calculaSaldo(campo1, campo2, destino) {
    $("#"+campo1).trigger('mask.maskMoney');
    var valSt = $("#"+campo1).val();
    valSt = valSt.replace(".","").replace(",","");
    if(valSt === null || valSt === "undefined"){
        valSt = "0";
    }
    var valor1 = parseInt(valSt);
    $("#"+campo2).trigger('mask.maskMoney');
    valSt = $("#"+campo2).val();
    valSt = valSt.replace(".","").replace(",","");
    var valor2 = parseInt(valSt);

    var saldo  = valor1-valor2;
    console.log("valor1",valor1);
    console.log("valor2",valor2);
    console.log("saldo",saldo);
    $("#"+destino).val(saldo).trigger('mask.maskMoney');
}

function consultaOpcoesCb(cb) {
    var r = $.Deferred();
    var tela = $('#it').val();
    var val = cb.val();
    var idCb = cb.attr('id');

    if(idCb==='tipo_item' && tela==='operacoes_funcionario') {
        var its = $('#edicao_item');
        ajustaCamposOpFu(cb);
        $.ajax(
            {
                type: "POST",
                url: "consulta",
                data: {
                    "tipo": "opcoes",
                    "tela": tela,
                    "item": val
                },
                success: function (data) {
                    its.html(data);
                    r.resolve();
                },
                error: function (jXHR, textStatus, errorThrown) {
                    r.reject();
                    avisoErros(jXHR, textStatus, errorThrown);
                }
            }
        );
    }else {
        r.resolve();
    }
    return r;
}

function ajustaCamposOpFu(its) {
    switch (its.val()){
        case "1"://Bolão
            $('#div_cb_tipo').show();
            $('#div_data_sorteio').show();
            $('#div_nome_concurso').show();
            $('#div_quantidade').show();
            $('#opt_gera').show();
            $('#opt_vend').show();
            $('#opt_troc').hide();
            $('#lb_cb_tipo').html("Tipo");
            break;
        case "2"://Bilhete
            $('#div_cb_tipo').show();
            $('#div_data_sorteio').show();
            $('#div_nome_concurso').show();
            $('#div_quantidade').show();
            $('#opt_gera').hide();
            $('#opt_vend').show();
            $('#opt_troc').hide();
            $('#lb_cb_tipo').html("Edição");
            break;
        case "3": // Dinheiro
            $('#div_cb_tipo').hide();
            $('#div_data_sorteio').hide();
            $('#div_nome_concurso').hide();
            $('#div_quantidade').hide();
            $('#opt_gera').hide();
            $('#opt_vend').hide();
            $('#opt_troc').hide();
            break;
        case "4"://Tele Sena
            $('#div_cb_tipo').show();
            $('#div_data_sorteio').show();
            $('#div_nome_concurso').show();
            $('#div_quantidade').show();
            $('#opt_gera').hide();
            $('#opt_vend').show();
            $('#opt_troc').show();
            $('#lb_cb_tipo').html("Edição");
            break;
        case "5": //MOedas
            $('#div_cb_tipo').show();
            $('#div_data_sorteio').hide();
            $('#div_nome_concurso').hide();
            $('#div_quantidade').hide();
            $('#opt_gera').hide();
            $('#opt_vend').hide();
            $('#opt_troc').hide();
            $('#lb_cb_tipo').html("Valor");
            break;
        default://Outros
            $('#div_cb_tipo').show();
            $('#div_data_sorteio').show();
            $('#div_nome_concurso').show();
            $('#div_quantidade').show();
            $('#opt_gera').show();
            $('#opt_vend').show();
            $('#opt_troc').hide();
            $('#lb_cb_tipo').html("Tipo");
            break;
    }
}
