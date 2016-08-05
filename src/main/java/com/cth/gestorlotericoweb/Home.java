/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;

import com.cth.gestorlotericoweb.dados.Loterica;
import com.cth.gestorlotericoweb.parametros.Parametros;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 *
 * @author CarlosEduardo
 */
public class Home {

    public String output;
    public String input;
    public String id;
    HttpServletRequest request;

    public Home(HttpServletRequest request) {
        this.input = request.getParameter("it");
        this.id = request.getParameter("id");
        this.request = request;
    }

    public void setHome() {
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            ve.init();
            Template templatePrinc = ve.getTemplate( "templates/Modern/corpo.html" , "UTF-8");
            VelocityContext contextPrinc = new VelocityContext();
            if(input == null){
                Template tConteudo = ve.getTemplate( "templates/Modern/estatisticas.html" , "UTF-8");
                VelocityContext contextConteudo = new VelocityContext();
                StringWriter writerConteudo = new StringWriter();
                tConteudo.merge( contextConteudo, writerConteudo );
                contextPrinc.put("conteudo", writerConteudo.toString());
            }else{
                Template templateConteudo;
                VelocityContext contextConteudo;
                StringWriter writerConteudo;
                switch(input){
                    case "lotericas":          
                            Loterica loterica = new Loterica();
                            contextPrinc = loterica.getHtmlLoterica(contextPrinc, ve, id);
                        break;
                    case "terminais":                            
                            templateConteudo = ve.getTemplate( "templates/Modern/terminais.html" , "UTF-8");
                            contextConteudo = new VelocityContext();
                            writerConteudo = new StringWriter();
                            templateConteudo.merge( contextConteudo, writerConteudo );
                            contextPrinc.put("conteudo", writerConteudo.toString());
                        break;
                    default:
                            templateConteudo = ve.getTemplate( "templates/Modern/estatisticas.html" , "UTF-8");
                            contextConteudo = new VelocityContext();
                            writerConteudo = new StringWriter();
                            templateConteudo.merge( contextConteudo, writerConteudo );
                            contextPrinc.put("conteudo", writerConteudo.toString());
                        break;
                }
            }
            StringWriter writer = new StringWriter();
            templatePrinc.merge( contextPrinc, writer );
            Date date = new Date();
            Long tempoSessao = (date.getTime() - request.getSession(false).getLastAccessedTime())/1000;
            String pgAnt;
            if(request.getSession(false).getAttribute("url")==null){
                pgAnt = "index.jsp";
            }else{
                pgAnt = request.getSession(false).getAttribute("url").toString();
            }
            String pgAtu ;
            if(request.getParameterMap().keySet().size()>0){
                pgAtu = request.getRequestURL().toString()+"?";
                List l = new ArrayList();
                for(Object ob:request.getParameterMap().keySet()){
                    l.add(ob.toString()+"="+StringUtils.join(request.getParameterValues(ob.toString()), ','));
                }
                pgAtu += StringUtils.join(l,"&");
                
            }else{
                pgAtu = request.getRequestURL().toString();
            }
            Parametros.gravaSessao(request, pgAnt, pgAtu, tempoSessao);
            request.getSession(false).setAttribute("url", pgAtu);
            output = writer.toString();
    }    
}
