/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.configuracoes;

import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author CarlosEduardo
 */
public class Estatisticas extends Configuracoes{

    public Estatisticas(HttpServletRequest request) {
        super(request);
    }   
    
    public VelocityContext getHtml(VelocityContext contextPrinc,VelocityEngine ve,String idS){        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/configuracoes/estatisticas.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        contextConteudo.put("linhas_det","");
        writerConteudo = new StringWriter();
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        return contextPrinc;
    }

    
}
