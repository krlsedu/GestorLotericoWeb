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
import java.math.BigDecimal;
import java.sql.Date;
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
    Date dataAbertura;
    BigDecimal trocoDiaAnterior;
    BigDecimal trocoDia;
    String observacoes;
    String idCofre;
    
    public AberturaTerminal(HttpServletRequest request) {
        super(request);
        setAberturaTerminal();
    }

    private void setAberturaTerminal() {
        this.idTerminal = request.getParameter("id_terminal");
        this.idFuncionario = request.getParameter("id_funcionario");
        this.dataAbertura = Parser.toDbDate(request.getParameter("data_abertura"));
        this.trocoDiaAnterior = Parser.toBigDecimalFromHtml(request.getParameter("troco_dia_anterior"));
        this.trocoDia = Parser.toBigDecimalFromHtml(request.getParameter("troco_dia"));
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
                dataAbertura = rs.getDate(3);
                trocoDiaAnterior = rs.getBigDecimal(4);
                trocoDia = rs.getBigDecimal(5);
                idTerminal = rs.getString(6);
                idCofre = rs.getString(7);
            }else{
                idTerminal = "";
                idFuncionario = "";
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
                dataAbertura = rs.getDate(3);
                trocoDiaAnterior = rs.getBigDecimal(4);
                trocoDia = rs.getBigDecimal(5);
                idTerminal = rs.getString(6);
                idCofre = rs.getString(7);
            }else{
                idTerminal = "";
                idFuncionario = "";
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
            ps.setDate(3, dataAbertura);
            ps.setBigDecimal(4, trocoDiaAnterior);
            ps.setBigDecimal(5, trocoDia);
            ps = Seter.set(ps,6, observacoes);
            ps.setInt(7,Integer.valueOf(idCofre));
            ps.setInt(8, Parametros.idEntidade);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
                Parametros.setIdAberturaTerminal(id);
                Parametros.setIdTerminal(Parser.toIntegerNull(idTerminal));
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
            ps.setDate(3,dataAbertura);
            ps.setBigDecimal(4, trocoDiaAnterior);
            ps.setBigDecimal(5, trocoDia);
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
    
    public BigDecimal getTrocoDiaAnterior() {
        return trocoDiaAnterior;
    }
    
    public BigDecimal getTrocoDia() {
        return trocoDia;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void getTerminalAberto(Integer idFunc){
        //language=PostgresPLSQL
        String sql = "SELECT a.id,a.id_funcionario,a.id_terminal " +
                "FROM\n" +
                "  abertura_terminais a,\n" +
                "  terminais t,\n" +
                "  funcionarios func\n" +
                "WHERE\n" +
                "  func.id = a.id_funcionario AND\n" +
                "  t.id_entidade = a.id_entidade AND\n" +
                "  t.id = a.id_terminal AND\n" +
                "  a.id_funcionario = ? AND\n" +
                "  a.id_entidade = ? AND\n" +
                "  NOT exists( SELECT 1 FROM\n" +
                "                  fechamento_terminais f\n" +
                "              WHERE\n" +
                "                  f.id_terminal = a.id_terminal AND\n" +
                "                  f.id_funcionario = a.id_funcionario AND\n" +
                "                  f.data_encerramento = a.data_abertura AND\n" +
                "                  f.id_entidade = a.id_entidade)\n" +
                "ORDER BY\n" +
                "  t.nome";
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst(sql,false);
            ps.setInt(1,idFunc);
            ps.setInt(2,Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                this.id = rs.getInt(1);
                this.idFuncionario = rs.getString(2);
                this.idTerminal = rs.getString(3);
            }
        } catch (SQLException e) {
            new LogError(e.getMessage(),e,request);
        }
    }
    
    public String getIdTerminal() {
        return idTerminal;
    }
}
