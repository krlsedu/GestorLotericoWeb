/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;

import com.cth.gestorlotericoweb.estoque.Itens;
import com.cth.gestorlotericoweb.configuracoes.EstatisticasLinhas;
import com.cth.gestorlotericoweb.dados.*;
import com.cth.gestorlotericoweb.estoque.MovimentosEstoque;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.processos.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author CarlosEduardo
 */
public class Grava {
    public String output;
    HttpServletRequest request;
    public Integer id;

    public Grava(HttpServletRequest request) {
        this.request = request;
    }
    
    public void exec(){
        if(request.getParameter("tipo")!=null){
            deleta();
        }else{
            grava();
        }
        try {
            Parametros.getConexao().commit();
        } catch (SQLException e) {
        }
    }
    
    private void deleta(){
        try {                    
            ColunasTabelas colunasTabelas = new ColunasTabelas(request);
            String tabela = request.getParameter("tabela");
            switch(tabela){
                case "movimentos_cofres":
                    MovimentoCofre movimentoCofre = new MovimentoCofre(Integer.valueOf(request.getParameter("id")),request);
                    movimentoCofre.deleta();
                    break;
                case "movimentos_caixas":
                    MovimentoCaixa movimentoCaixa = new MovimentoCaixa(Integer.valueOf(request.getParameter("id")),request);
                    movimentoCaixa.deleta();
                    break;
                case "movimentos_contas":
                    MovimentoConta movimentoConta = new MovimentoConta(Integer.valueOf(request.getParameter("id")), request);
                    movimentoConta.deleta();
                    break;
                case "movimentos_estoque":
                    MovimentosEstoque movimentosEstoque = new MovimentosEstoque( request);
                    movimentosEstoque.deleta();
                    break;
                default:                    
                    tabela = colunasTabelas.getTabela(request.getParameter("tabela"));
                    if(tabela !=null){
                        PreparedStatement ps = Parametros.getConexao(request).getPst("delete from "+tabela+" where id = ? and id_entidade = ?", Boolean.FALSE);     
                        ps.setInt(1, Integer.valueOf(request.getParameter("id")));
                        ps.setInt(2, Parametros.idEntidade);
                        ps.execute();
                    }   
                    break;
            }         
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        } catch (Exception ex) {
            new LogError(ex.getMessage(), ex,request);
        }
        try {
            Parametros.getConexao().commit();
        } catch (SQLException e) {
        }
    }
    
    private void grava(){
        String input = request.getParameter("it");
        switch (input) {
            //inicio gravação cadastros;
            case "cofres":
                Cofre cofre = new Cofre(request.getParameter("nome_cofre"), request.getParameter("tipo_cofre"), request.getParameter("id_loterica"), request.getParameter("observacoes"), request);
                if ("0".equals(request.getParameter("id"))) {
                    cofre.insere();
                } else {
                    cofre.altera(request.getParameter("id"));
                }
                id = cofre.getId();
                break;
            case "contas":
                Conta conta = new Conta(request.getParameter("nome_conta"), request.getParameter("agencia"), request.getParameter("operacao"), request.getParameter("conta_corrente"), request.getParameter("dv"), request.getParameter("telefone"), request.getParameter("gerente"), request.getParameter("observacoes"), Integer.valueOf(request.getParameter("id_loterica")), request);
                if ("0".equals(request.getParameter("id"))) {
                    conta.insere();
                } else {
                    conta.altera(request.getParameter("id"));
                }
                id = conta.getId();
                break;
            case "funcionarios":
                Funcionario funcionario = new Funcionario(request.getParameter("codigo_caixa"), request.getParameter("nome"), request.getParameter("cpf"), Integer.valueOf(request.getParameter("tipo_func")), request.getParameter("observacoes"), request);
                if ("0".equals(request.getParameter("id"))) {
                    funcionario.insere();
                } else {
                    funcionario.altera(request.getParameter("id"));
                }
                id = funcionario.getId();
                break;
            case "lotericas":
                Loterica loterica = new Loterica(request.getParameter("codigo_caixa"), request.getParameter("nome"), request);
                if ("0".equals(request.getParameter("id"))) {
                    loterica.insere();
                } else {
                    loterica.altera(request.getParameter("id"));
                }
                id = loterica.getId();
                break;
            case "operacoes":
                Operacao operacao = new Operacao(request);
                if ("0".equals(request.getParameter("id"))) {
                    operacao.insere();
                } else {
                    operacao.altera(request.getParameter("id"));
                }
                id = operacao.getId();
                break;
            case "tarifas_operacoes":
                TarifaOperacao tarifaOperacao = new TarifaOperacao(request);
                if ("0".equals(request.getParameter("id"))) {
                    tarifaOperacao.insere();
                } else {
                    tarifaOperacao.altera(request.getParameter("id"));
                }
                id = tarifaOperacao.getId();
                break;
            case "terminais":
                Terminal terminal = new Terminal(request);
                if ("0".equals(request.getParameter("id"))) {
                    terminal.insere();
                } else {
                    terminal.altera(request.getParameter("id"));
                }
                id = terminal.getId();
                break;
            //fim gravação cadastros;
            //inicio gravação processos
            case "abertura_terminais":
                AberturaTerminal aberturaTerminal = new AberturaTerminal(request);
                if ("0".equals(request.getParameter("id"))) {
                    aberturaTerminal.insere();
                } else {
                    aberturaTerminal.altera();
                }
                id = aberturaTerminal.getId();
                break;
            case "movimentos_caixas":
                MovimentoCaixa movimentoCaixa = new MovimentoCaixa(request);
                if ("0".equals(request.getParameter("id"))) {
                    movimentoCaixa.insere();
                } else {
                    movimentoCaixa.altera();
                }
                id = movimentoCaixa.getId();
                break;
            case "movimentos_cofres":
                MovimentoCofre movimentoCofre = new MovimentoCofre(request);
                movimentoCofre.grava();
                id = movimentoCofre.getId();
                break;
            case "movimentos_contas":
                MovimentoConta movimentoConta = new MovimentoConta(request);
                if ("0".equals(request.getParameter("id"))) {
                    movimentoConta.insere();
                } else {
                    movimentoConta.altera();
                }
                id = movimentoConta.getId();
                break;
            case "outros_movimentos":
                OutrosMovimentos outrosMovimentos = new OutrosMovimentos(request);
                if ("0".equals(request.getParameter("id"))) {
                    outrosMovimentos.insere();
                } else {
                    outrosMovimentos.altera();
                }
                id = outrosMovimentos.getId();
                break;
            case "fechamento_terminais":
                FechamentoTerminal fechamentoTerminal = new FechamentoTerminal(request);
                if ("0".equals(request.getParameter("id"))) {
                    fechamentoTerminal.insere();
                } else {
                    fechamentoTerminal.altera();
                }
                id = fechamentoTerminal.getId();
                break;
            case "operacoes_diarias":
                OperacoesDiarias operacoesDiarias = new OperacoesDiarias(request);
                operacoesDiarias.setOperacoesDiarias();
                operacoesDiarias.grava();
                id = operacoesDiarias.getId();
                break;
            //fim gravação processos
            //inicio gravação configs
            case "itens_estatisticas":
                EstatisticasLinhas estatisticasLinhas = new EstatisticasLinhas(request);
                estatisticasLinhas.setEstatisticasLinhas();
                estatisticasLinhas.grava();
                break;
            //fim gravação configs
            //Início Gravação Estoque
            case "itens_estoque":
                Itens itens = new Itens(request);
                itens.setItens();
                itens.grava();
                break;
            case "movimentos_estoque":
                MovimentosEstoque movimentosEstoque = new MovimentosEstoque(request);
                movimentosEstoque.setMovimentacao();
                movimentosEstoque.grava();
                break;
            //fim Gravação Estoque
            default:
                break;
        }
        try {
            Parametros.getConexao().commit();
        } catch (SQLException e) {
        }
    }
}
