/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.processos;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.utils.Seter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
public class MovimentoCaixa extends Processos{
    String idTerminal;
    String idFuncionario;
    String dataHoraMov;
    String valorMovimentado;
    String tipoOperacao;
    String observacoes;
    
    public MovimentoCaixa(HttpServletRequest request) {
        super(request);
        setMovimentosCaixas();
    }
    private void setMovimentosCaixas() {
        this.idTerminal = request.getParameter("id_terminal");
        this.idFuncionario = request.getParameter("id_funcionario");
        this.dataHoraMov = request.getParameter("data_hora_mov");
        this.valorMovimentado = request.getParameter("valor_movimentado");
        this.tipoOperacao = request.getParameter("tipo_operacao_caixa");
        this.observacoes = request.getParameter("observacoes");
    }
    public MovimentoCaixa(Integer id,HttpServletRequest request) {
        super(request, id);
        getDados();
    }
    private void getDados(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("SELECT  tipo_operacao_caixa,id_terminal, id_funcionario, data_hora_mov, \n" +
            "       valor_movimentado, observacoes\n" +
            "  FROM public.movimentos_caixas where id = ? and id_entidade = ? ",false);
            ps.setInt(1, id);
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                tipoOperacao = rs.getString(1);
                idTerminal = rs.getString(2);
                idFuncionario = rs.getString(3);
                dataHoraMov = rs.getString(4);
                valorMovimentado = rs.getString(5);
                observacoes = rs.getString(6);
            }else{
                tipoOperacao = "";
                idTerminal = "";
                idFuncionario = "";
                dataHoraMov = "";
                valorMovimentado = "";
                observacoes = "";
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void insere(){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO public.movimentos_caixas(\n" +
"            tipo_operacao_caixa, id_terminal, id_funcionario, data_hora_mov, \n" +
"            valor_movimentado, observacoes, id_entidade)\n" +
"    VALUES (?, ?, ?, ?, \n" +
"            ?, ?, ?);");
            ps.setInt(1, Integer.valueOf(tipoOperacao));
            if(idTerminal == null||idTerminal.trim().equals("")){
                ps.setNull(2, java.sql.Types.BIGINT);
            }else{
                ps.setInt(2, Integer.valueOf(idTerminal));
            }
            ps.setInt(3, Integer.valueOf(idFuncionario));
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddhh:mm");
                java.util.Date parsedDate = dateFormat.parse(dataHoraMov.replace("T", ""));
                Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                ps.setTimestamp(4, timestamp);
            } catch (ParseException ex) {
                new LogError(ex.getMessage(), ex,request);
                java.util.Date parsed = new java.util.Date();
                Timestamp timestamp = new java.sql.Timestamp(parsed.getTime());
                ps.setTimestamp(4, timestamp);
            }
            ps.setDouble(5, Double.parseDouble(valorMovimentado.replace(".", "x").replace(",", ".").replace("x", ",")));
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
            PreparedStatement ps = Parametros.getConexao(request).getPst("UPDATE public.movimentos_caixas\n" +
            "   SET tipo_operacao_caixa=?, id_terminal=?, id_funcionario=?, data_hora_mov=?,\n" +
            "    valor_movimentado=?, observacoes=?\n" 
                    + " where id = ? and id_entidade = ? ", false);
            ps.setInt(1, Integer.valueOf(tipoOperacao));
            ps = Seter.set(ps, 2, Integer.valueOf(idTerminal));
            ps.setInt(3, Integer.valueOf(idFuncionario));
            
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddhh:mm");
                java.util.Date parsedDate = dateFormat.parse(dataHoraMov.replace("T", ""));
                Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                ps.setTimestamp(4, timestamp);
            } catch (ParseException ex) {
                new LogError(ex.getMessage(), ex,request);
                java.util.Date parsed = new java.util.Date();
                Timestamp timestamp = new java.sql.Timestamp(parsed.getTime());
                ps.setTimestamp(4, timestamp);
            }
            
            ps.setDouble(5, Double.parseDouble(valorMovimentado.replace(".", "x").replace(",", ".").replace("x", ",")));
            ps = Seter.set(ps, 6, observacoes);
            
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
        templateConteudo = ve.getTemplate( "templates/Modern/movimentos_caixas.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());       
        setOpcoesFiltro("movimentos_caixas");
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"movimentos_caixas").toString());
        return contextPrinc;
    }
}
