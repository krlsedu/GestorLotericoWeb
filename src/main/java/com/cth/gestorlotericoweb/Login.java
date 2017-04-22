/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;
import com.cth.gestorlotericoweb.parametros.Parametros;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CarlosEduardo
 */
public class Login{
    public String output;
    HttpServletRequest request;
    public void setLogin(){
        try{
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            ve.init();
            Template t;
            VelocityContext context = new VelocityContext();
            t = ve.getTemplate("templates/web/login.html", "UTF-8");
            context.put("var", "Bem Vindo ao Gestor Lotérico Web");
            Writer writer = new StringWriter();
            t.merge( context, writer );
            output = writer.toString();
        
        }catch(ResourceNotFoundException e){
            new LogError(e.getMessage(), e,request);
        }
    }
    
    private void setLogin(ConteudoTelas c){
        try{
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            ve.init();
            Template t;
            VelocityContext context = new VelocityContext();
            t = ve.getTemplate("templates/web/login_interno.html", "UTF-8");
            context.put("it_r", "app");
            context.put("it_sub", request.getParameter("it"));
            context.put("it_par", "");
            Writer writer = new StringWriter();
            t.merge( context, writer );
            output = writer.toString();
            
        }catch(ResourceNotFoundException e){
            new LogError(e.getMessage(), e,request);
        }
    }
    
    private void setLogin(Consulta consulta){
        try{
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            ve.init();
            Template t;
            VelocityContext context = new VelocityContext();
            t = ve.getTemplate("templates/web/login_interno.html", "UTF-8");
            context.put("it_r", "consulta");
            context.put("it_sub", request.getParameter("tipo").trim());
            String params="";
            if (request.getParameterMap().keySet().size() > 0) {
                List l = new ArrayList();
                for (Object ob : request.getParameterMap().keySet()) {
                    l.add(ob.toString() + "=" + StringUtils.join(request.getParameterValues(ob.toString()), ','));
                }
    
                params += StringUtils.join(l, "&");
            }
            context.put("it_par",params);
            Writer writer = new StringWriter();
            t.merge( context, writer );
            output = writer.toString();
            
        }catch(ResourceNotFoundException e){
            new LogError(e.getMessage(), e,request);
        }
    }
    
    private void setLogin(Grava grava){
        try{
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            ve.init();
            Template t;
            VelocityContext context = new VelocityContext();
            t = ve.getTemplate("templates/web/login_interno.html", "UTF-8");
            context.put("it_r", "grava");
            if (request.getParameter("tipo")!=null) {
                context.put("it_sub", request.getParameter("tipo").trim());
            }else{
                context.put("it_sub",null);
            }
            String params="";
            if (request.getParameterMap().keySet().size() > 0) {
                List l = new ArrayList();
                for (Object ob : request.getParameterMap().keySet()) {
                    l.add(ob.toString() + "=" + StringUtils.join(request.getParameterValues(ob.toString()), ','));
                }
                params += StringUtils.join(l, "&");
            }
            context.put("it_par",params);
            Writer writer = new StringWriter();
            t.merge( context, writer );
            output = writer.toString();
            
        }catch(ResourceNotFoundException e){
            new LogError(e.getMessage(), e,request);
        }
    }
    
    public Login(HttpServletRequest request) {
        this.request = request;
        Parametros.initDb(request);
    }
    
    public Login(ConteudoTelas conteudoTelas){
        this.request = conteudoTelas.request;
        Parametros.initDb(request);
        setLogin(conteudoTelas);
    }
    
    public Login(Consulta consulta){
        this.request = consulta.request;
        setLogin(consulta);
    }
    
    public Login(Grava grava){
        this.request = grava.request;
        setLogin(grava);
    }
}
