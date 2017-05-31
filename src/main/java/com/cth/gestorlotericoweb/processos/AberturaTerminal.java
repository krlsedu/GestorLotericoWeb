/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.processos;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.utils.Parser;
import com.cth.gestorlotericoweb.utils.Seter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    String idCofre;
    
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
        this.idCofre = request.getParameter("id_cofre");
    }
    
    public AberturaTerminal(Integer id,HttpServletRequest request) {
        super(request, id);
        getDadosPorId();
    }
    
    public void getDadosPorTerminalFuncionarioEData(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("select  id_terminal, id_funcionario, data_abertura, \n" +
                    "        troco_dia_anterior, troco_dia, observacoes, id_cofre \n" +
                    "        \n" +
                    "  FROM abertura_terminais where id_terminal = ? and id_funcionario = ? and data_abertura = ? and id_entidade = ? ",false);
            ps.setInt(1, Integer.valueOf(request.getParameter("id_terminal")));
            ps.setInt(2, Integer.valueOf(request.getParameter("id_funcionario")));
            ps.setDate(3, Parser.toDbDate(request.getParameter("data_movs")));
            ps.setInt(4, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                idTerminal = rs.getString(1);
                idFuncionario = rs.getString(2);
                dataAbertura = rs.getString(3);
                trocoDiaAnterior = Parser.toBigDecimalSt(rs.getString(4));
                trocoDia = Parser.toBigDecimalSt(rs.getString(5));
                idTerminal = rs.getString(6);
                idCofre = rs.getString(7);
            }else{
                idTerminal = "";
                idFuncionario = "";
                dataAbertura = "";
                trocoDiaAnterior = "";
                trocoDia = "";
                idTerminal = "";
                idCofre = "";
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    private void getDadosPorId(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("select  id_terminal, id_funcionario, data_abertura, \n" +
            "        troco_dia_anterior, troco_dia, observacoes, id_cofre \n" +
            "        \n" +
            "  FROM abertura_terminais where id = ? and id_entidade = ? ",false);
            ps.setInt(1, id);
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                idTerminal = rs.getString(1);
                idFuncionario = rs.getString(2);
                dataAbertura = rs.getString(3);
                trocoDiaAnterior = Parser.toBigDecimalSt(rs.getString(4));
                trocoDia = Parser.toBigDecimalSt(rs.getString(5));
                idTerminal = rs.getString(6);
                idCofre = rs.getString(7);
            }else{
                idTerminal = "";
                idFuncionario = "";
                dataAbertura = "";
                trocoDiaAnterior = "";
                trocoDia = "";
                idTerminal = "";
                idCofre = "";
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void insere(){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO abertura_terminais(\n" +
            "             id_terminal, id_funcionario, data_abertura, \n" +
            "            troco_dia_anterior, troco_dia, observacoes, id_cofre ,id_entidade)\n" +
            "    VALUES ( ?, ?, ?, \n" +
            "            ?, ?, ?, ?, ?);");
            ps.setInt(1, Integer.valueOf(idTerminal));
            ps.setInt(2, Integer.valueOf(idFuncionario));       
            ps.setDate(3, Parser.toDbDate(dataAbertura));            
            ps.setBigDecimal(4, Parser.toBigDecimalFromHtml(trocoDiaAnterior));
            ps.setBigDecimal(5, Parser.toBigDecimalFromHtml(trocoDia));
            ps = Seter.set(ps,6, observacoes);
            ps.setInt(7,Integer.valueOf(idCofre));
            ps.setInt(8, Parametros.idEntidade);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
                if(this.id!=null){
                    try{
                        MovimentoCofre movimentoCofre = new MovimentoCofre(request, this);
                        movimentoCofre.gravaAutoMov();
                    }catch(Exception e){
                        new LogError(e.getMessage(),e,request);
                    }
                }
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
        "       troco_dia_anterior=?, troco_dia=?, observacoes=?, id_cofre = ?\n"
                    + " where id = ? and id_entidade = ? ", false);
            ps.setInt(1, Integer.valueOf(idTerminal));
            ps.setInt(2, Integer.valueOf(idFuncionario));            
            ps.setDate(3, Parser.toDbDate(dataAbertura));            
            ps.setBigDecimal(4, Parser.toBigDecimalFromHtml(trocoDiaAnterior));
            ps.setBigDecimal(5, Parser.toBigDecimalFromHtml(trocoDia));
            ps.setInt(7,Integer.valueOf(idCofre));
            ps = Seter.set(ps,6, observacoes);
            
            
            id = Integer.valueOf(idL);
            ps.setInt(8, id);
            ps.setInt(9, Parametros.idEntidade);
            
            ps.execute();
            if(this.id!=null){
                try{
                    MovimentoCofre movimentoCofre = new MovimentoCofre(request, this);
                    movimentoCofre.gravaAutoMov();
                }catch(Exception e){
                    new LogError(e.getMessage(),e,request);
                }
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public VelocityContext getHtml(VelocityContext contextPrinc,VelocityEngine ve,String idS){        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/processos/abertura_terminais.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());       
        setOpcoesFiltro("abertura_terminais");
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"abertura_terminais").toString());
        return contextPrinc;
    }
    
    public String getTrocoDiaAnteriorAbertura(){
        FechamentoTerminal fechamentoTerminal = new FechamentoTerminal(request);
        return fechamentoTerminal.getRestoCaixaDiaAnterior();
    }
    
    public String getTrocoDiaAnterior() {
        return Parser.toBigDecimalSt(trocoDiaAnterior);
    }
    
    public String getTrocoDia() {
        return Parser.toBigDecimalSt(trocoDia);
    }
    
    public String getObservacoes() {
        return observacoes;
    }
}
