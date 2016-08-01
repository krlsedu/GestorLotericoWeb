/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;

import java.io.StringWriter;
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

    public Home(String input) {
        this.input = input;
    }

    public void setHome() {
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            ve.init();
            /*  next, get the Template  */
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
                            templateConteudo = ve.getTemplate( "templates/Modern/lotericas.html" , "UTF-8");
                            contextConteudo = new VelocityContext();
                            writerConteudo = new StringWriter();
                            templateConteudo.merge( contextConteudo, writerConteudo );
                            contextPrinc.put("conteudo", writerConteudo.toString());
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
            output = writer.toString();        
    }    
}
