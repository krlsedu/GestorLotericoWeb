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
    while (valSt.indexOf(".")>=0){
        valSt = valSt.replace(".","");
    }
    valSt = valSt.replace(",","");
    if(valSt === null || valSt === "undefined"){
        valSt = "0";
    }
    var valor1 = parseInt(valSt);
    $("#"+campo2).trigger('mask.maskMoney');
    valSt = $("#"+campo2).val();
    while (valSt.indexOf(".")>=0){
        valSt = valSt.replace(".","");
    }
    valSt = valSt.replace(",","");
    var valor2 = parseInt(valSt);

    var saldo  = valor1-valor2;
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
            $('#div_quantidade').show();
            $('#opt_gera').show();
            $('#opt_vend').show();
            $('#opt_troc').hide();
            $('#lb_cb_tipo').html("Tipo");
            break;
        case "2"://Bilhete
            $('#div_cb_tipo').show();
            $('#div_quantidade').show();
            $('#opt_gera').hide();
            $('#opt_vend').show();
            $('#opt_troc').hide();
            $('#lb_cb_tipo').html("Edição");
            break;
        case "3": // Dinheiro
            $("#div_valor").show();
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
            $('#div_quantidade').show();
            $('#opt_gera').hide();
            $('#opt_vend').show();
            $('#opt_troc').show();
            $('#lb_cb_tipo').html("Edição");
            break;
        case "5": //MOedas
            $("#div_valor").show();
            $('#div_cb_tipo').show();
            $('#div_quantidade').hide();
            $('#opt_gera').hide();
            $('#opt_vend').hide();
            $('#opt_troc').hide();
            $('#lb_cb_tipo').html("Valor");
            break;
        default://Outros
            $('#div_cb_tipo').show();
            $('#div_quantidade').show();
            $('#opt_gera').show();
            $('#opt_vend').show();
            $('#opt_troc').hide();
            $('#lb_cb_tipo').html("Tipo");
            break;
    }
}

function attCbs(its, toc) {
    switch (its.val()){
        case "1"://bolão
            $('#lb_qtd').html("Num. Cotas");
        case "6"://Outros
            $("#div_valor").hide();
            $("#div_data_sorteio").hide();
            $('#div_nome_concurso').hide();
            $('#div_cb_tipo').show();
            $('#div_quantidade').show();
            switch (toc.val()) {
                case "2": // Geração
                    $('#lb_val').html("Total");
                    $('#div_cb_tipo').show();
                    $('#div_data_sorteio').show();
                    $('#div_nome_concurso').show();
                    $("#div_valor").show();
                    break;
            }
            break;
        case "4": //tele sena
            $('#lb_qtd').html("Quantidade");
            $('#lb_val').html("Valor");
            $('#div_cb_tipo').show();
            $("#div_quantidade").show();
            $('#div_nome_concurso').hide();
            $("#div_valor").hide();
            switch (toc.val()) {
                case "5": // Geração
                    $('#div_nome_concurso').hide();
                    $('#div_cb_tipo').hide();
                    $('#edicao_item').html("");
                    $("#div_quantidade").show();
                    $("#div_valor").show();
                    break;
            }
            break;
        case "3":
            $('#lb_qtd').html("Quantidade");
            $('#lb_val').html("Valor");
            $('#div_cb_tipo').hide();
            $('#div_data_sorteio').hide();
            $('#div_nome_concurso').hide();
            break;
        case "5":
            $('#lb_qtd').html("Quantidade");
            $('#lb_val').html("Valor");
            $('#div_cb_tipo').show();
            $('#div_data_sorteio').hide();
            $('#div_nome_concurso').hide();
            break;
        default:
            $('#lb_qtd').html("Quantidade");
            $('#lb_val').html("Valor");
            $('#div_cb_tipo').show();
            $('#div_data_sorteio').hide();
            $('#div_nome_concurso').hide();
            $('#div_valor').hide();
            break;
    }
}


function buscaDadosFechamentoAdmDiario() {
    let r = $.Deferred();
    let tela = $('#it').val();
    $.ajax(
        {
            type: "POST",
            url: "consulta",
            data: $("#form_dados").serialize() + '&' + $.param({
                "tipo": "dado",
                "tabela":tela,
                "valor_buscar": tela
            }),
            success: function (data) {
                let jso = JSON.parse(data);
                // $('#total_creditos_terminais').setInput(jso);
                // $('#total_debitos_terminais').setInput(jso);
                // $('#total_depositado').setInput(jso);
                $('#form_dados').setFromJson(jso);
                r.resolve();
            },
            error: function (jXHR, textStatus, errorThrown) {
                r.reject();
                avisoErros(jXHR, textStatus, errorThrown);
            }
        }
    );
    return r;
}