/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.dados;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.parametros.Parametros;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author CarlosEduardo
 */
public class Conta extends Cadastros{
    Integer id;
    String nome;
    String agencia;
    String operacao;
    String conta;
    String dv;
    String telefone;
    String gerente;
    String observacoes;
    Integer idLoterica;
    final HttpServletRequest request;

    public Conta(String nome, String agencia, String operacao, String conta, String dv, String telefone, String gerente,  String observacoes, Integer idLoterica, HttpServletRequest request) {
        this.nome = nome;
        this.agencia = agencia;
        this.operacao = operacao;
        this.conta = conta;
        this.dv = dv;
        this.telefone = telefone;
        this.gerente = gerente;
        this.observacoes = observacoes;
        this.idLoterica = idLoterica;
        this.request = request;
    }

    

    public Conta(HttpServletRequest request) {
        this.request = request;
    }
    public Conta(Integer id,HttpServletRequest request) {
        this.id = id;
        this.request = request;
        getDados();
    }
    private void getDados(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("select conta_corrente, dv, nome_conta, operacao, agencia, telefone, gerente, \n" +
"       id_loterica, observacoes \n" +
"  FROM contas where id = ? and id_entidade = ? ",false);
            ps.setInt(1, id);
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                conta = rs.getString(1);
                dv = rs.getString(2);
                nome = rs.getString(3);
                operacao = rs.getString(4);
                agencia = rs.getString(5);
                telefone = rs.getString(6);
                gerente = rs.getString(7);
                idLoterica = rs.getInt(8);
                observacoes = rs.getString(9);
            }else{
                conta = "";
                dv = "";
                nome = "";
                operacao = "";
                agencia = "";
                telefone = "";
                gerente = "";
                idLoterica = 0;
                observacoes = "";
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void insere(){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO contas(\n" +
            "            conta_corrente, dv, nome_conta, operacao, agencia,"
                    + "  telefone, gerente, id_loterica, observacoes, id_entidade)\n" +
            "    VALUES (?, ?, ?, ?, ?,"
                    + "  ?, ?, ?, ?, ?);");
            ps.setString(1, conta);
            ps.setString(2, dv);
            ps.setString(3, nome);
            ps.setString(4, operacao);
            ps.setString(5, agencia);
            ps.setString(6, telefone);
            ps.setString(7, gerente);
            ps.setInt(8, idLoterica);
            if(observacoes == null){
                ps.setNull(9, java.sql.Types.LONGVARCHAR);
            }else{
                ps.setString(9, observacoes);
            }
            ps.setInt(10, Parametros.idEntidade);
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
            }
            
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void altera(String idL){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("UPDATE contas\n" +
"   SET  conta_corrente=?, dv=?, nome_conta=?, operacao=?, agencia=?, \n" +
"       telefone=?, gerente=?, id_loterica=?, observacoes=? "
                    + " where id = ? and id_entidade = ? ", false);
            ps.setString(1, conta);
            ps.setString(2, dv);
            ps.setString(3, nome);
            ps.setString(4, operacao);
            ps.setString(5, agencia);
            ps.setString(6, telefone);
            ps.setString(7, gerente);
            ps.setInt(8, idLoterica);
            if(observacoes == null){
                ps.setNull(9, java.sql.Types.LONGVARCHAR);
            }else{
                ps.setString(9, observacoes);
            }
            id = Integer.valueOf(idL);
            ps.setInt(10, id);
            ps.setInt(11, Parametros.idEntidade);
            ps.execute();
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getAgencia() {
        return agencia;
    }

    public String getOperacao() {
        return operacao;
    }

    public String getConta() {
        return conta;
    }

    public String getDv() {
        return dv;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getGerente() {
        return gerente;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public Integer getIdLoterica() {
        return idLoterica;
    }

    public HttpServletRequest getRequest() {
        return request;
    }
    
    private List<String> setOpcoesFiltro(){
        ColunasTabelas colunasTabelas = new ColunasTabelas(request);
        lOpts = colunasTabelas.getlOpts("contas");
        return lOpts;
    }
    
    public VelocityContext getHtml(VelocityContext contextPrinc,VelocityEngine ve,String idS){        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/contas.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());       
        setOpcoesFiltro();
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"contas").toString());
        return contextPrinc;
    }
}
