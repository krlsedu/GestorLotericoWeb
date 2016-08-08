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
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author CarlosEduardo
 */
public class Terminal extends Cadastros{
    String codigoCaixa;
    Integer idLoterica;
    String nome;
    String marca;
    String modelo;
    String observacoes;
    Integer id;
    final HttpServletRequest request;

    public Terminal(String codigoCaixa, Integer idLoterica, String nome, String marca, String modelo, String observacoes, HttpServletRequest request) {
        this.codigoCaixa = codigoCaixa;
        this.idLoterica = idLoterica;
        this.nome = nome;
        this.marca = marca;
        this.modelo = modelo;
        this.observacoes = observacoes;
        this.request = request;
    }    

    public Terminal(HttpServletRequest request) {
        this.request = request;
    }
    
    public Terminal(Integer id,HttpServletRequest request) {
        this.id = id;
        this.request = request;
        getDados();
    }
    private void getDados(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("SELECT codigo_caixa, nome, marca, modelo, observacoes, id_loterica \n" +
                    "  FROM public.terminais where id = ? and id_entidade = ? ",false);
            ps.setInt(1, id);
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                codigoCaixa = rs.getString(1);
                nome = rs.getString(2);
                marca = rs.getString(3);
                modelo = rs.getString(4);
                observacoes = rs.getString(5);
                idLoterica = rs.getInt(6);
            }else{
                codigoCaixa = "";
                nome = "";
                marca = "";
                modelo = "";
                observacoes = "";
                idLoterica = 0;
                id = 0;
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void insere(){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO public.terminais(\n" +
            "             codigo_caixa, nome, marca, modelo, observacoes, id_loterica, id_entidade)\n" +
            "    VALUES ( ?, ?, ?, ?, ?, ?, ?);");
            ps.setString(1, codigoCaixa);
            ps.setString(2, nome);
            ps.setString(3, marca);
            ps.setString(4, modelo);
            ps.setString(5, observacoes);
            ps.setInt(6, idLoterica);
            ps.setInt(7, Parametros.idEntidade);
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
            PreparedStatement ps = Parametros.getConexao(request).getPst("UPDATE public.terminais\n" +
            "   SET codigo_caixa=?, nome=?, marca=?, modelo=?, observacoes=?, \n" +
            "       id_loterica=? where id = ? and id_entidade = ? ", false);
            ps.setString(1, codigoCaixa);
            ps.setString(2, nome);
            ps.setString(3, marca);
            ps.setString(4, modelo);
            ps.setString(5, observacoes);
            ps.setInt(6, idLoterica);
            
            id = Integer.valueOf(idL);
            ps.setInt(7, id);
            ps.setInt(8, Parametros.idEntidade);
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

    public Integer getIdLoterica() {
        return idLoterica;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getObservacoes() {
        return observacoes;
    }
       
    private List<String> setOpcoesFiltro(){
        ColunasTabelas colunasTabelas = new ColunasTabelas(request);
        lOpts = colunasTabelas.getlOpts("terminais");
        return lOpts;
    }
    
    public VelocityContext getHtmlTerminal(VelocityContext contextPrinc,VelocityEngine ve,String idS){
        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/terminais.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        if(idS !=null){
            Terminal terminal = new Terminal(Integer.valueOf(idS),request);
            contextConteudo.put("idbd", terminal.getId()); 
            contextConteudo.put("codcaixa", terminal.getCodigoCaixa());
            contextConteudo.put("id_loterica", terminal.getNome());   
            contextConteudo.put("nome", terminal.getNome());   
            contextConteudo.put("marca", terminal.getNome());   
            contextConteudo.put("modelo", terminal.getNome());   
            contextConteudo.put("observacoes", terminal.getNome());              
            contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());                   
        }else{
            contextConteudo.put("idbd", "0");    
            contextConteudo.put("codcaixa", "");
            contextConteudo.put("id_loterica", "");   
            contextConteudo.put("nome", "");   
            contextConteudo.put("marca", "");   
            contextConteudo.put("modelo", "");   
            contextConteudo.put("observacoes", "");              
            contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());   
            contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());
        }
        setOpcoesFiltro();
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"terminais").toString());
        return contextPrinc;
    }
}
