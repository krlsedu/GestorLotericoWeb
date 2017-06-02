function abreTelaBtnAbr(tela,idTerminal) {
    buscaConteudoTela(tela).done(function () {
        $("#id_terminal").val(idTerminal);
        document.getElementById("id_terminal").onkeyup();
    }).fail(function () {
        console.log('r.reject() invocado');
    });;
}

function buscaTerminaisAbertos() {
    var r = $.Deferred();
    $.ajax(
        {
            type: "POST",
            url:  "consulta",
            data:{"tipo":"validacao","item":"terminais_abertos"},
            success: function (data) {
                if(data.trim().length>0) {
                    $('#caixas_abertos').show();
                    $('#caixas_abertos').html(data);
                    $('#modal_carregando').modal('hide');
                }else {
                    $('#caixas_abertos').hide();
                }
                r.resolve();
            },
            error: function (jXHR, textStatus, errorThrown) {
                avisoErros(jXHR,textStatus,errorThrown);
                r.reject();
            }
        }
    );
    return r
}

function existeTerminalAbertoConsTela(tela) {
    var r = $.Deferred();
    if(tela === 'movimentos_caixas' || tela === 'outros_movimentos' || tela === 'operacoes_diarias' || tela === 'fechamento_terminais'){
        $.ajax(
            {
                type: "POST",
                url:  "consulta",
                data:{"tipo":"validacao","item":"existe_term_aberto"},
                success: function (data) {
                    if(data.trim() === 'false') {
                        avisosErrosDiversos("N&atilde;o existem terminais abertos!");
                    }
                    r.resolve();
                },
                error: function (jXHR, textStatus, errorThrown) {
                    avisoErros(jXHR,textStatus,errorThrown);
                    r.reject();
                }
            }
        );
    }else {
        r.resolve();
    }
    return r;
}

function existeTerminalAberto(tela) {
    var r = $.Deferred();
    if(tela === 'movimentos_caixas' || tela === 'outros_movimentos' || tela === 'operacoes_diarias' || tela === 'fechamento_terminais'){
        $.ajax(
            {
                type: "POST",
                url:  "consulta",
                data:$("#form_dados").serialize()+ '&' + $.param({"tipo":"validacao","item":"existe_term_aberto"}),
                success: function (data) {
                    if(data.trim() === 'false') {
                        avisosErrosDiversos("O terminal "+$('#id_terminal').val()+" n&atilde;o est&aacute; aberto para o funcion&aacute;rio "+ $('#id_funcionario').val()+"!");
                        $('#botao-gravar').prop("disabled",true);
                    }else {
                        $('#botao-gravar').prop("disabled",false);
                    }
                    r.resolve();
                },
                error: function (jXHR, textStatus, errorThrown) {
                    avisoErros(jXHR,textStatus,errorThrown);
                    r.reject();
                }
            }
        );
    }else {
        if(tela === 'abertura_terminais'){
            $.ajax(
                {
                    type: "POST",
                    url:  "consulta",
                    data:$("#form_dados").serialize()+ '&' + $.param({"tipo":"validacao","item":"existe_term_aberto"}),
                    success: function (data) {
                        if(data.trim() === 'true') {
                            avisosErrosDiversos("O terminal "+$('#id_terminal').val()+" j&aacute; est&aacute; aberto para o funcion&aacute;rio "+ $('#id_funcionario').val()+"!");
                            $('#botao-gravar').prop("disabled",true);
                        }else {
                            $('#botao-gravar').prop("disabled",false);
                        }
                        r.resolve();
                    },
                    error: function (jXHR, textStatus, errorThrown) {
                        avisoErros(jXHR,textStatus,errorThrown);
                        r.reject();
                    }
                }
            );
        }else {
            r.resolve();
        }
    }
    return r;
}

function verificaSeFechado() {
    var r = $.Deferred();
    var tela = $('#it').val();
    if(tela === 'movimentos_caixas' || tela === 'outros_movimentos' || tela === 'operacoes_diarias' || tela === 'abertura_terminais' ){
        $.ajax(
            {
                type: "POST",
                url:  "consulta",
                data:$("#form_dados").serialize()+ '&' + $.param({"tipo":"validacao","item":"verifica_se_fechado"}),
                success: function (data) {
                    var bol = (data.trim() === 'true');
                    if(bol) {
                        avisosErrosDiversos("O registro est&aacute; fechado!\nPara alter&aacute;-lo deve ser estornado o Fechamento desse terminal, funcion&aacute;rio e dia");

                    }
                    $("form#form_dados").each(function(){
                        $(this).find(':input').prop("readonly", bol);
                        $(this).find(':button').prop("disabled", bol);
                    });
                    r.resolve();
                },
                error: function (jXHR, textStatus, errorThrown) {
                    avisoErros(jXHR,textStatus,errorThrown);
                    r.reject();
                }
            }
        );
    }else {
        r.resolve();
    }
    return r;
}

function ajustaValorMascaraDinamica() {
    var r = $.Deferred();
    var tela = $('#it').val();
    if(tela === 'movimentos_estoque') {
        var tabela;
        var campoFormatar;
        var item;
        switch (tela) {
            case "movimentos_estoque":
                item = 'id_itens_estoque';
                campoFormatar = 'quantidade_movimentada';
                tabela = 'itens_estoque';
                break;
        }
        var campo = $("#" + campoFormatar);
        $.ajax(
            {
                type: "POST",
                url: "consulta",
                data: {"tipo": "unidade", "id": $('#' + item).val(), "tabela": tabela},
                success: function (data) {
                    var tipo = data.trim();
                    var valor = campo.val();
                    if (tipo === "1") {
                        valor = valor.substring(0, valor.length - 2);
                        campo.val(valor);

                        campo.unmask();
                        campo.maskMoney('destroy');
                        campo.mask("99999999");
                    }else{
                        campo.unmask();
                        campo.maskMoney('destroy');
                        campo.maskMoney({showSymbol:false, symbol:"", decimal:",", thousands:".", allowZero:true,allowNegative:true});
                        campo.val(valor).trigger('mask.maskMoney');
                    }
                    r.resolve();
                },
                error: function (jXHR, textStatus, errorThrown) {
                    avisoErros(jXHR, textStatus, errorThrown);
                    r.reject();
                }
            }
        );
    }else {
        r.resolve();
    }
    return r;
}

function alteraMascara(campoFormatar,tabela,item) {
    timeout = setTimeout(function() {
        var r = $.Deferred();
        var campo = $("#"+campoFormatar);
        $.ajax(
            {
                type: "POST",
                url:  "consulta",
                data:{"tipo":"unidade","id":$('#'+item).val(),"tabela":tabela},
                success: function (data) {
                    var tipo = data.trim();
                    if(tipo === "2"){
                        campo.unmask();
                        campo.maskMoney('destroy');
                        campo.maskMoney({showSymbol:false, symbol:"", decimal:",", thousands:".", allowZero:true,allowNegative:true});
                    }else{
                        campo.unmask();
                        campo.maskMoney('destroy');
                        campo.mask("99999999");
                    }
                    r.resolve();
                },
                error: function (jXHR, textStatus, errorThrown) {
                    avisoErros(jXHR,textStatus,errorThrown);
                    r.reject();
                }
            }
        );
        return r;
    },500);
}