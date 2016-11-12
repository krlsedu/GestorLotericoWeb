/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.estatisticas;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author CarlosEduardo
 */
public class Estatisticas {
    final HttpServletRequest request;
    String valorApresentar;
    BigDecimal valorMovimentado;
    Boolean bom;

    public Estatisticas(HttpServletRequest request) {
        this.request = request;
    }
    
    public VelocityContext getHtml(VelocityContext contextPrinc,VelocityEngine ve,String idS){        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/estatisticas.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter(); 
        Diarias movimentoDiario = new Diarias(request);
        contextConteudo.put("acm_dia",movimentoDiario.valorApresentar); 
        Semanal movimentoSemanal = new Semanal(request,movimentoDiario);
        contextConteudo.put("acm_semana",movimentoSemanal.valorApresentar); 
        Mensal movimentoMensal = new Mensal(request,movimentoDiario);
        contextConteudo.put("acm_mes",movimentoMensal.valorApresentar); 
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"movimentos_contas").toString());
        return contextPrinc;
    }    
    public StringWriter getSWPopup(VelocityEngine ve,String tipo){
        Template templatePopup;
        VelocityContext contextPopup;
        StringWriter writerPopup;
        templatePopup = ve.getTemplate( "templates/Modern/modal.html" , "UTF-8");
        contextPopup = new VelocityContext();
        writerPopup = new StringWriter();
        contextPopup.put("tabela", tipo);
        List<String> lOpts = new ArrayList<>();
        contextPopup.put("opt", StringUtils.join(lOpts, '\n'));
        templatePopup.merge(contextPopup, writerPopup);
        return writerPopup;
    }
    
}
