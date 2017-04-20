/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;
import com.cth.gestorlotericoweb.parametros.Parametros;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;

/**
 *
 * @author CarlosEduardo
 */
public class Login{
    public String output;
    public void setLogin(HttpServletRequest request,Boolean interno){
        
        Parametros.initDb(request);
        
        try{
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            ve.init();
            Template t;
            VelocityContext context = new VelocityContext();
            if(interno) {
                t = ve.getTemplate("templates/web/login_interno.html", "UTF-8");
                context.put("it_r", "app");
                context.put("it_sub", request.getParameter("it"));
            }else {
                t = ve.getTemplate("templates/web/login.html", "UTF-8");
                context.put("var", "Bem Vindo ao Gestor Lotérico Web");
            }
            StringWriter writer = new StringWriter();
            t.merge( context, writer );
            output = writer.toString();
        
        }catch(ResourceNotFoundException e){
            new LogError(e.getMessage(), e,request);
        }
        //JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/web/index.html");
        //JtwigModel model = JtwigModel.newModel().with("var", "World");
                
    }
}
