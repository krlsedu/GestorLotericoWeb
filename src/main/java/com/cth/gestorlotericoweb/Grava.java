/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;

import com.cth.gestorlotericoweb.dados.ColunasTabelas;
import com.cth.gestorlotericoweb.dados.Conta;
import com.cth.gestorlotericoweb.dados.Funcionario;
import com.cth.gestorlotericoweb.dados.Loterica;
import com.cth.gestorlotericoweb.dados.Terminal;
import com.cth.gestorlotericoweb.parametros.Parametros;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

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
        if(request.getParameter("tipo")!=null){
            deleta();
        }else{
            grava();
        }
    }
    
    private void deleta(){
        try {                    
            ColunasTabelas colunasTabelas = new ColunasTabelas(request);
            String tabela = colunasTabelas.getTabela(request.getParameter("tabela"));
            if(tabela !=null){
                PreparedStatement ps = Parametros.getConexao(request).getPst("delete from "+tabela+" where id = ?", Boolean.FALSE);     
                ps.setInt(1, Integer.valueOf(request.getParameter("id")));
                ps.execute();
            }            
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    private void grava(){
        String input = request.getParameter("it");
        switch(input){
            case "contas":
                Conta conta = new Conta(request.getParameter("nome_conta"), request.getParameter("agencia"), request.getParameter("operacao"), request.getParameter("conta_corrente"), request.getParameter("dv"), request.getParameter("telefone"), request.getParameter("gerente"), request.getParameter("observacoes"), Integer.valueOf(request.getParameter("id_loterica")), request);
                if("0".equals(request.getParameter("id"))){
                    conta.insere();
                }else{
                    conta.altera(request.getParameter("id"));
                }
                id = conta.getId();
                break;
            case "funcionarios":       
                Funcionario funcionario = new Funcionario(request.getParameter("codigo_caixa"), request.getParameter("nome"), request.getParameter("cpf"), Integer.valueOf(request.getParameter("tipo_func")), request.getParameter("observacoes"), request);
                if("0".equals(request.getParameter("id"))){
                    funcionario.insere();
                }else{
                    funcionario.altera(request.getParameter("id"));
                }
                id = funcionario.getId();
                break;
            case "lotericas":     
                Loterica loterica = new Loterica(request.getParameter("codigo_caixa"), request.getParameter("nome"),request);
                if("0".equals(request.getParameter("id"))){
                    loterica.insere();
                }else{
                    loterica.altera(request.getParameter("id"));
                }
                id = loterica.getId();
                break;
            case "terminais":       
                Terminal terminal = new Terminal(request.getParameter("codigo_caixa"), Integer.valueOf(request.getParameter("id_loterica")), request.getParameter("nome"), request.getParameter("marca"), request.getParameter("modelo"), request.getParameter("observacoes"), request);
                if("0".equals(request.getParameter("id"))){
                    terminal.insere();
                }else{
                    terminal.altera(request.getParameter("id"));
                }
                id = terminal.getId();
                break;
            default:
                break;
        }
    }
}
