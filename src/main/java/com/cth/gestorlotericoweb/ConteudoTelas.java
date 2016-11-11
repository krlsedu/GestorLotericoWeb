/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;

import com.cth.gestorlotericoweb.dados.Cofre;
import com.cth.gestorlotericoweb.dados.Conta;
import com.cth.gestorlotericoweb.dados.Funcionario;
import com.cth.gestorlotericoweb.dados.Loterica;
import com.cth.gestorlotericoweb.dados.Operacao;
import com.cth.gestorlotericoweb.dados.TarifaOperacao;
import com.cth.gestorlotericoweb.dados.Terminal;
import com.cth.gestorlotericoweb.estatisticas.Estatisticas;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.processos.AberturaTerminal;
import com.cth.gestorlotericoweb.processos.FechamentoTerminal;
import com.cth.gestorlotericoweb.processos.MovimentoCaixa;
import com.cth.gestorlotericoweb.processos.MovimentoCofre;
import com.cth.gestorlotericoweb.processos.MovimentoConta;
import com.cth.gestorlotericoweb.processos.OutroMovimento;
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
public class ConteudoTelas {
    public static VelocityContext getConteudoTela(VelocityContext contextPrinc,VelocityEngine ve,HttpServletRequest request){
        String id = request.getParameter("id");
        String input = request.getParameter("it");
        if(input == null){                
            Estatisticas estatisticas = new Estatisticas(request);
            contextPrinc = estatisticas.getHtml(contextPrinc, ve, id);
        }else{
            switch(input){
                // inicio Cadastros
                    case "cofres":      
                            Cofre cofre = new Cofre(request);
                            contextPrinc = cofre.getHtml(contextPrinc, ve, id);
                        break;

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

                    case "operacoes":          
                            Operacao operacao = new Operacao(request);
                            contextPrinc = operacao.getHtml(contextPrinc, ve, id);
                        break;

                    case "tarifas_operacoes":          
                            TarifaOperacao tarifaOperacao = new TarifaOperacao(request);
                            contextPrinc = tarifaOperacao.getHtml(contextPrinc, ve, id);
                        break;

                    case "terminais":      
                            Terminal terminal = new Terminal(request);
                            contextPrinc = terminal.getHtmlTerminal(contextPrinc, ve, id);
                        break;
                //fim cadastros
                // Inicio Processos
                    case "abertura_terminais":      
                        AberturaTerminal aberturaTerminal = new AberturaTerminal(request);
                        contextPrinc = aberturaTerminal.getHtml(contextPrinc, ve, id);
                    break;

                    case "movimentos_caixas":      
                        MovimentoCaixa movimentoCaixa = new MovimentoCaixa(request);
                        contextPrinc = movimentoCaixa.getHtml(contextPrinc, ve, id);
                    break;                        

                    case "movimentos_cofres":      
                        MovimentoCofre movimentoCofre = new MovimentoCofre(request);
                        contextPrinc = movimentoCofre.getHtml(contextPrinc, ve, id);
                    break;                      

                    case "movimentos_contas":      
                        MovimentoConta movimentoConta = new MovimentoConta(request);
                        contextPrinc = movimentoConta.getHtml(contextPrinc, ve, id);
                    break;

                    case "outros_movimentos":      
                        OutroMovimento outroMovimento = new OutroMovimento(request);
                        contextPrinc = outroMovimento.getHtml(contextPrinc, ve, id);
                    break;

                    case "fechamento_terminais":      
                        FechamentoTerminal fechamentoTerminal = new FechamentoTerminal(request);
                        contextPrinc = fechamentoTerminal.getHtml(contextPrinc, ve, id);
                    break;
                // Fim Processos   
                default:
                        Estatisticas estatisticas = new Estatisticas(request);
                        contextPrinc = estatisticas.getHtml(contextPrinc, ve, id);
                    break;
            }
        }
        return contextPrinc;
    }
    
    public static String getHtmlTela(HttpServletRequest request){
        try{
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            ve.init();
            Template templatePrinc = ve.getTemplate( "templates/Modern/conteudo_telas.html" , "UTF-8");
            VelocityContext contextPrinc = new VelocityContext();
            contextPrinc = ConteudoTelas.getConteudoTela(contextPrinc, ve, request);
            StringWriter writer = new StringWriter();
            templatePrinc.merge( contextPrinc, writer );  
            Parametros.gravaLogSessao(request);
            return  writer.toString();
        }catch(ResourceNotFoundException|MethodInvocationException|ParseErrorException ex){
            new LogError(ex.getMessage(), ex, request);
        }
        return "";
    }
    
}
