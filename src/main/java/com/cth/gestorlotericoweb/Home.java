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

    public Home() {
    }

    public String output;
    public void setHome() {
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            ve.init();
            /*  next, get the Template  */
            Template t = ve.getTemplate( "templates/Modern/corpo.html" , "UTF-8");
            VelocityContext context = new VelocityContext();
            
            context.put("conteudo", "estatisticas.html");
            
            StringWriter writer = new StringWriter();
            t.merge( context, writer );
            output = writer.toString();        
    }    
}
