/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;

import com.cth.gestorlotericoweb.dados.Conta;
import com.cth.gestorlotericoweb.dados.Estatisticas;
import com.cth.gestorlotericoweb.dados.Funcionario;
import com.cth.gestorlotericoweb.dados.Loterica;
import com.cth.gestorlotericoweb.dados.Terminal;
import com.cth.gestorlotericoweb.parametros.Parametros;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
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
        try{
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            ve.init();
            Template templatePrinc = ve.getTemplate( "templates/Modern/corpo.html" , "UTF-8");
            VelocityContext contextPrinc = new VelocityContext();
            if(input == null){                
                Estatisticas estatisticas = new Estatisticas();
                contextPrinc = estatisticas.getHtmlTerminal(contextPrinc, ve, id);
            }else{
                switch(input){
                    case "contas":      
                            Conta conta = new Conta(request);
                            contextPrinc = conta.getHtml(contextPrinc, ve, id);
                        break;
                    
                    case "funcionarios":      
                            Funcionario funcionario = new Funcionario(request);
                            contextPrinc = funcionario.getHtml(contextPrinc, ve, id);
                        break;
                        
                    case "lotericas":          
                            Loterica loterica = new Loterica(request);
                            contextPrinc = loterica.getHtml(contextPrinc, ve, id);
                        break;
                        
                    case "terminais":      
                            Terminal terminal = new Terminal(request);
                            contextPrinc = terminal.getHtmlTerminal(contextPrinc, ve, id);
                        break;
                        
                    default:
                            Estatisticas estatisticas = new Estatisticas();
                            contextPrinc = estatisticas.getHtmlTerminal(contextPrinc, ve, id);
                        break;
                }
            }
            StringWriter writer = new StringWriter();
            templatePrinc.merge( contextPrinc, writer );  
            Parametros.gravaLogSessao(request);
            output = writer.toString();
        }catch(ResourceNotFoundException|MethodInvocationException|ParseErrorException ex){
            new LogError(ex.getMessage(), ex, request);
        }
    }    
}
