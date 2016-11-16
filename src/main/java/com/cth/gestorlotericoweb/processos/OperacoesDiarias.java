/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.processos;

import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author CarlosEduardo
 */
public class OperacoesDiarias extends Processos{
    Integer numLinhas;
    Integer[] idOperacao;
    Integer[] qtdOper;
    Integer idTerminal;
    Integer idFuncionario;
    String dataOperacoes;

    public OperacoesDiarias(HttpServletRequest request) {
        super(request);
        setOperacoesDiarias();
    }
    
    private void setOperacoesDiarias(){
        this.numLinhas = Integer.valueOf(request.getParameter("id_conta"));
        idOperacao = new Integer[numLinhas];
        qtdOper = new Integer[numLinhas];
        for(int i=1;i<=numLinhas;i++){
            idOperacao[i-1]=  Integer.valueOf(request.getParameter("id_operacao_"+i));
            qtdOper[i-1]= Integer.valueOf(request.getParameter("quantidade_"+1));
        }
        idTerminal = Integer.valueOf(request.getParameter("id_terminal"));
        idFuncionario = Integer.valueOf(request.getParameter("id_funcionario"));
        dataOperacoes = request.getParameter("data_operacoes");
    }
    
    public VelocityContext getHtml(VelocityContext contextPrinc,VelocityEngine ve,String idS){        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/processos/operacoes_diarias.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());       
        setOpcoesFiltro("operacoes_diarias");
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"operacoes_diarias").toString());
        return contextPrinc;
    }
}
