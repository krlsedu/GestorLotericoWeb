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
    String idLoterica;
    String idTerminal;
    String idFuncionario;
    String dataAbertura;
    String trocoDiaAnterior;
    String trocoDia;
    String observacoes;
    public AberturaTerminal(HttpServletRequest request) {
        super(request);
    }

    public AberturaTerminal(String idLoteria, String idTerminal, String idFuncionario, String dataAbertura, String trocoDiaAnterior, String trocoDia, String observacoes, HttpServletRequest request) {
        super(request);
        this.idLoterica = idLoteria;
        this.idTerminal = idTerminal;
        this.idFuncionario = idFuncionario;
        this.dataAbertura = dataAbertura;
        this.trocoDiaAnterior = trocoDiaAnterior;
        this.trocoDia = trocoDia;
        this.observacoes = observacoes;
    }

    
    
    public AberturaTerminal(Integer id,HttpServletRequest request) {
        super(request, id);
        getDados();
    }
    private void getDados(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("select id_loterica, id_terminal, id_funcionario, data_abertura, \n" +
"        troco_dia_anterior, troco_dia, observacoes \n" +
"        \n" +
"  FROM abertura_terminais where id = ? and id_entidade = ? ",false);
            ps.setInt(1, id);
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                idLoterica = rs.getString(1);
                idTerminal = rs.getString(2);
                idFuncionario = rs.getString(3);
                dataAbertura = rs.getString(4);
                trocoDiaAnterior = rs.getString(5);
                trocoDia = rs.getString(6);
                idTerminal = rs.getString(7);
            }else{
                idLoterica = "";
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
"            id_loterica, id_terminal, id_funcionario, data_abertura, \n" +
"            troco_dia_anterior, troco_dia, observacoes, id_entidade)\n" +
"    VALUES (?, ?, ?, ?, \n" +
"            ?, ?, ?, ?);");
            ps.setInt(1, Integer.valueOf(idLoterica));
            ps.setInt(2, Integer.valueOf(idTerminal));
            ps.setInt(3, Integer.valueOf(idFuncionario));
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
            ps.setDate(4, dateAbertura);
            ps.setDouble(5, Double.parseDouble(trocoDiaAnterior.replace(".", "x").replace(",", ".").replace("x", ",")));
            ps.setDouble(6, Double.parseDouble(trocoDia.replace(".", "x").replace(",", ".").replace("x", ",")));
            if(observacoes == null){
                ps.setNull(7, java.sql.Types.LONGVARCHAR);
            }else{
                ps.setString(7, observacoes);
            }
            ps.setInt(8, Parametros.idEntidade);
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
            PreparedStatement ps = Parametros.getConexao(request).getPst("UPDATE abertura_terminais\n" +
        "   SET id_loterica=?, id_terminal=?, id_funcionario=?, data_abertura=?, \n" +
        "       troco_dia_anterior=?, troco_dia=?, observacoes=?\n" 
                    + " where id = ? and id_entidade = ? ", false);
            ps.setInt(1, Integer.valueOf(idLoterica));
            ps.setInt(2, Integer.valueOf(idTerminal));
            ps.setInt(3, Integer.valueOf(idFuncionario));
            
            Date dateAbertura;
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsed = format.parse(dataAbertura);
                dateAbertura = new java.sql.Date(parsed.getTime());
            } catch (ParseException ex) {
                throw new RuntimeException(dataAbertura, ex);
            }
            ps.setDate(4, dateAbertura);
            
            ps.setDouble(5, Double.parseDouble(trocoDiaAnterior.replace(".", "x").replace(",", ".").replace("x", ",")));
            ps.setDouble(6, Double.parseDouble(trocoDia.replace(".", "x").replace(",", ".").replace("x", ",")));
            if(observacoes == null){
                ps.setNull(7, java.sql.Types.LONGVARCHAR);
            }else{
                ps.setString(7, observacoes);
            }
            
            id = Integer.valueOf(idL);
            ps.setInt(8, id);
            ps.setInt(9, Parametros.idEntidade);
            
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
