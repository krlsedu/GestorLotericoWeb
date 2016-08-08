/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;
import com.cth.gestorlotericoweb.parametros.Parametros;
import java.io.StringWriter;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 *
 * @author CarlosEduardo
 */
public class Login{
    public String output;
    public void setLogin(HttpServletRequest request){
        
        Parametros.initDb(request);
        
        try{
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            ve.init();
            /*  next, get the Template  */
            Template t = ve.getTemplate( "templates/web/login.html" , "UTF-8");
            /*  create a context and add data */
            VelocityContext context = new VelocityContext();
            context.put("var", "World");
            //context.put("path",ve.getProperty(output));
            /* now render the template into a StringWriter */
            StringWriter writer = new StringWriter();
            t.merge( context, writer );
            /* show the World */
            output = writer.toString();
        
        }catch(ResourceNotFoundException e){
            new LogError(e.getMessage(), e,request);
        }
        //JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/web/index.html");
        //JtwigModel model = JtwigModel.newModel().with("var", "World");
                
    }
}
