/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;

import com.cth.gestorlotericoweb.estoque.Itens;
import com.cth.gestorlotericoweb.configuracoes.EstatisticasLinhas;
import com.cth.gestorlotericoweb.dados.*;
import com.cth.gestorlotericoweb.estatisticas.Estatisticas;
import com.cth.gestorlotericoweb.estoque.MovimentosEstoque;
import com.cth.gestorlotericoweb.operador.Operacoes;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.processos.*;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;

/**
 *
 * @author CarlosEduardo
 */
public class ConteudoTelas {
    public HttpServletRequest request;
    public ConteudoTelas(HttpServletRequest request){
        this.request = request;
    }
    public static VelocityContext getConteudoTela(VelocityContext contextPrinc,VelocityEngine ve,HttpServletRequest request){
//        try {
            String id = request.getParameter("id");
            String input = request.getParameter("it");
            if (input == null) {
                Estatisticas estatisticas = new Estatisticas(request);
                //contextPrinc = estatisticas.getHtml(contextPrinc, ve, id);
                contextPrinc = estatisticas.getHtml(contextPrinc, ve);
            } else {
                switch (input) {
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
                        OutrosMovimentos outrosMovimentos = new OutrosMovimentos(request);
                        contextPrinc = outrosMovimentos.getHtml(contextPrinc, ve, id);
                        break;
            
                    case "operacoes_diarias":
                        OperacoesDiarias operacoesDiarias = new OperacoesDiarias(request);
                        contextPrinc = operacoesDiarias.getHtml(contextPrinc, ve, id);
                        break;
            
                    case "operacoes_diarias_linhas":
                        OperacoesDiariasLinhas operacoesDiariasLinhas = new OperacoesDiariasLinhas(request);
                        contextPrinc = operacoesDiariasLinhas.getHtml(contextPrinc, ve, id);
                        break;
            
                    case "fechamento_terminais":
                        FechamentoTerminal fechamentoTerminal = new FechamentoTerminal(request);
                        contextPrinc = fechamentoTerminal.getHtml(contextPrinc, ve, id);
                        break;
                    // Fim Processos
                    // Início Configurações
                    case "config_estatisticas":
                        com.cth.gestorlotericoweb.configuracoes.Estatisticas configEstatisticas = new com.cth.gestorlotericoweb.configuracoes.Estatisticas(request);
                        contextPrinc = configEstatisticas.getHtml(contextPrinc, ve, id);
                        break;
            
                    case "estatisticas_linhas":
                        EstatisticasLinhas estatisticasLinhas = new EstatisticasLinhas(request);
                        contextPrinc = estatisticasLinhas.getHtml(contextPrinc, ve, id);
                        break;
                    // Fim Configurações
    
                    //Início Gravação Estoque
                    case "itens_estoque":
                        Itens itens = new Itens(request);
                        contextPrinc = itens.getHtml(contextPrinc, ve, id);
                        break;
                        //fim Gravação Estoque
                    case "movimentos_estoque":
                        MovimentosEstoque movimentosEstoque = new MovimentosEstoque(request);
                        contextPrinc = movimentosEstoque.getHtml(contextPrinc, ve, id);
                        break;
                    //fim Gravação Estoque
                    
                    //inicio processos operador
                    case "operacoes_funcionario":
                        Operacoes operacoes = new Operacoes(request);
                        contextPrinc = operacoes.getHtml(contextPrinc,ve,id);
                        break;
                    // fim processo operador
                    default:
                        Estatisticas estatisticas = new Estatisticas(request);
                        contextPrinc = estatisticas.getHtml(contextPrinc, ve, id);
                        break;
                }
            }
//        }catch (Exception ex){
//            new LogError(ex.getMessage(),ex,request);
//        }
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
        } catch (Exception ex) {
            new LogError(ex.getMessage(), ex,request);
        }
        return "";
    }
    
}
