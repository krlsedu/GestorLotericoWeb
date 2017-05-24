function abreTelaBtnAbr(tela,idTerminal) {
    buscaConteudoTela(tela).done(function () {
        $("#id_terminal").val(idTerminal);
        document.getElementById("id_terminal").onkeyup();
    });
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
                r.resolve();
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
                    console.log('buscaTerminaisAbertos',data);
                    if(data.trim() === 'false') {
                        avisosErrosDiversos("N&atilde;o existem terminais abertos!");
                    }
                    r.resolve();
                },
                error: function (jXHR, textStatus, errorThrown) {
                    avisoErros(jXHR,textStatus,errorThrown);
                    r.resolve();
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
                    console.log('buscaTerminaisAbertos',data);
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
                    r.resolve();
                }
            }
        );
    }else {
        r.resolve();
    }
    return r;
}