/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.processos;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.parametros.Parametros;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author CarlosEduardo
 */
public class AberturaTerminal extends Processos{
    String idTerminal;
    String idFuncionario;
    String dataAbertura;
    String trocoDiaAnterior;
    String trocoDia;
    String observacoes;
    
    public AberturaTerminal(HttpServletRequest request) {
        super(request);
        setAberturaTerminal();
    }

    private void setAberturaTerminal() {
        this.idTerminal = request.getParameter("id_terminal");
        this.idFuncionario = request.getParameter("id_funcionario");
        this.dataAbertura = request.getParameter("data_abertura");
        this.trocoDiaAnterior = request.getParameter("troco_dia_anterior");
        this.trocoDia = request.getParameter("troco_dia");
        this.observacoes = request.getParameter("observacoes");
    }

    
    
    public AberturaTerminal(Integer id,HttpServletRequest request) {
        super(request, id);
        getDados();
    }
    
    private void getDados(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("select  id_terminal, id_funcionario, data_abertura, \n" +
            "        troco_dia_anterior, troco_dia, observacoes \n" +
            "        \n" +
            "  FROM abertura_terminais where id = ? and id_entidade = ? ",false);
            ps.setInt(1, id);
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                idTerminal = rs.getString(1);
                idFuncionario = rs.getString(2);
                dataAbertura = rs.getString(3);
                trocoDiaAnterior = rs.getString(4);
                trocoDia = rs.getString(5);
                idTerminal = rs.getString(6);
            }else{
                idTerminal = "";
                idFuncionario = "";
                dataAbertura = "";
                trocoDiaAnterior = "";
                trocoDia = "";
                idTerminal = "";
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void insere(){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO abertura_terminais(\n" +
            "             id_terminal, id_funcionario, data_abertura, \n" +
            "            troco_dia_anterior, troco_dia, observacoes, id_entidade)\n" +
            "    VALUES ( ?, ?, ?, \n" +
            "            ?, ?, ?, ?);");
            ps.setInt(1, Integer.valueOf(idTerminal));
            ps.setInt(2, Integer.valueOf(idFuncionario));
            Date dateAbertura;
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsed = format.parse(dataAbertura);
                dateAbertura = new java.sql.Date(parsed.getTime());
            } catch (ParseException ex) {
                new LogError(ex.getMessage(), ex,request);
                java.util.Date parsed = new java.util.Date();
                dateAbertura = new java.sql.Date(parsed.getTime());
            }
            ps.setDate(3, dateAbertura);
            ps.setDouble(4, Double.parseDouble(trocoDiaAnterior.replace(".", "x").replace(",", ".").replace("x", ",")));
            ps.setDouble(5, Double.parseDouble(trocoDia.replace(".", "x").replace(",", ".").replace("x", ",")));
            if(observacoes == null){
                ps.setNull(6, java.sql.Types.LONGVARCHAR);
            }else{
                ps.setString(6, observacoes);
            }
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
    
    public void altera(){
        String idL = request.getParameter("id");
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("UPDATE abertura_terminais\n" +
        "   SET id_terminal=?, id_funcionario=?, data_abertura=?, \n" +
        "       troco_dia_anterior=?, troco_dia=?, observacoes=?\n" 
                    + " where id = ? and id_entidade = ? ", false);
            ps.setInt(1, Integer.valueOf(idTerminal));
            ps.setInt(2, Integer.valueOf(idFuncionario));
            
            Date dateAbertura;
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsed = format.parse(dataAbertura);
                dateAbertura = new java.sql.Date(parsed.getTime());
            } catch (ParseException ex) {
                throw new RuntimeException(dataAbertura, ex);
            }
            ps.setDate(3, dateAbertura);
            
            ps.setDouble(4, Double.parseDouble(trocoDiaAnterior.replace(".", "x").replace(",", ".").replace("x", ",")));
            ps.setDouble(5, Double.parseDouble(trocoDia.replace(".", "x").replace(",", ".").replace("x", ",")));
            if(observacoes == null){
                ps.setNull(6, java.sql.Types.LONGVARCHAR);
            }else{
                ps.setString(6, observacoes);
            }
            
            id = Integer.valueOf(idL);
            ps.setInt(7, id);
            ps.setInt(8, Parametros.idEntidade);
            
            ps.execute();
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public VelocityContext getHtml(VelocityContext contextPrinc,VelocityEngine ve,String idS){        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/abertura_terminais.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());       
        setOpcoesFiltro("abertura_terminais");
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"abertura_terminais").toString());
        return contextPrinc;
    }
}
