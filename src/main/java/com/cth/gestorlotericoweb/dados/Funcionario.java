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
public class Funcionario extends Cadastros{
    String codigoCaixa;
    String nome;
    String cpf;
    Integer tipo;
    String observacoes;
    Integer id;
    final HttpServletRequest request;

    public Funcionario(String codigoCaixa, String nome, String cpf, Integer tipo, String observacoes,HttpServletRequest request) {
        this.codigoCaixa = codigoCaixa;
        this.nome = nome;
        this.cpf = cpf;
        this.tipo = tipo;
        this.observacoes = observacoes;
        this.request = request;
    } 

    public Funcionario(HttpServletRequest request) {
        this.request = request;
    }
    
    public Funcionario(Integer id,HttpServletRequest request) {
        this.id = id;
        this.request = request;
        getDados();
    }
    private void getDados(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("SELECT codigo_caixa, nome, cpf, tipo,observacoes \n" +
                    "  FROM funcionarios where id = ? and id_entidade = ? ",false);
            ps.setInt(1, id);
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                codigoCaixa = rs.getString(1);
                nome = rs.getString(2);
                cpf = rs.getString(3);
                tipo = rs.getInt(4);
                observacoes = rs.getString(5);
            }else{
                codigoCaixa = "";
                nome = "";
                cpf = "";
                tipo = 0;
                observacoes ="";
                id = 0;
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void insere(){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO funcionarios(\n" +
            "            codigo_caixa, nome, cpf, tipo, id_entidade, observacoes)\n" +
            "    VALUES ( ?, ?, ?, ?, ?, ?);");
            ps.setString(1, codigoCaixa);
            ps.setString(2, nome);
            ps.setString(3, cpf);
            ps.setInt(4, tipo);
            ps.setInt(5, Parametros.idEntidade);
            if(observacoes == null){
                ps.setNull(6, java.sql.Types.LONGVARCHAR);
            }else{
                ps.setString(6, observacoes);
            }
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
            PreparedStatement ps = Parametros.getConexao(request).getPst("UPDATE funcionarios \n" +
                    "set codigo_caixa = ?, nome = ?, cpf = ?, tipo_func = ?, observacoes = ? "
                    + " where id = ? and id_entidade = ? ", false);
            ps.setString(1, codigoCaixa);
            ps.setString(2, nome);
            ps.setString(3, cpf);
            ps.setInt(4, tipo);
            if(observacoes == null){
                ps.setNull(5, java.sql.Types.LONGVARCHAR);
            }else{
                ps.setString(5, observacoes);
            }
            id = Integer.valueOf(idL);
            ps.setInt(6, id);
            ps.setInt(7, Parametros.idEntidade);
            ps.execute();
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }

    public Integer getId() {
        return id;
    }

    public String getCodigoCaixa() {
        return codigoCaixa;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public Integer getTipo() {
        return tipo;
    }

    public String getObservacoes() {
        return observacoes;
    }
    
    
       
    private List<String> setOpcoesFiltro(){
        ColunasTabelas colunasTabelas = new ColunasTabelas(request);
        lOpts = colunasTabelas.getlOpts("funcionarios");
        return lOpts;
    }
    
    public VelocityContext getHtml(VelocityContext contextPrinc,VelocityEngine ve,String idS){
        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/funcionarios.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        if(idS !=null){
            Funcionario funcionario = new Funcionario(Integer.valueOf(idS),request);
            contextConteudo.put("idbd", funcionario.getId()); 
            contextConteudo.put("codcaixa", funcionario.getCodigoCaixa());
            contextConteudo.put("nome", funcionario.getNome());   
            contextConteudo.put("cpf", funcionario.getCpf());   
            contextConteudo.put("tipo", funcionario.getTipo());     
            contextConteudo.put("observacoes", funcionario.getObservacoes());     
            
            contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());                   
        }else{
            contextConteudo.put("idbd", "0");    
            contextConteudo.put("codcaixa", "");
            contextConteudo.put("nome", "");   
            contextConteudo.put("cpf", "");   
            contextConteudo.put("tipo", "");     
            contextConteudo.put("observacoes", "");               
            contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());
        }
        setOpcoesFiltro();
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"funcionarios").toString());
        return contextPrinc;
    }
}
