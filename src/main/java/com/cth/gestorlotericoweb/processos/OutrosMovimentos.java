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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CarlosEduardo
 */
public class OutrosMovimentos extends Processos{
    String idTerminal;
    String idFuncionario;
    String dataHoraMov;
    String valorMovimentado;
    String tipoOperacao;
    String observacoes;
    
    public OutrosMovimentos(HttpServletRequest request) {
        super(request);
        setOutrosMovimentos();
    }
    public OutrosMovimentos(HttpServletRequest request,ResultSet rs) throws SQLException {
        super(request);
        tipoOperacao = rs.getString(1);
        idTerminal = rs.getString(2);
        idFuncionario = rs.getString(3);
        dataHoraMov = rs.getString(4);
        valorMovimentado = Parser.toBigDecimalSt(rs.getString(5));
        observacoes = rs.getString(6);
    }
    private void setOutrosMovimentos() {
        this.idTerminal = request.getParameter("id_terminal");
        this.idFuncionario = request.getParameter("id_funcionario");
        this.dataHoraMov = request.getParameter("data_hora_mov");
        this.valorMovimentado = request.getParameter("valor_movimentado");
        this.tipoOperacao = request.getParameter("tipo_operacao_caixa");
        this.observacoes = request.getParameter("observacoes");
    }
    
    public OutrosMovimentos(Integer id, HttpServletRequest request) {
        super(request, id);
        getDados();
    }
    
    public List<OutrosMovimentos> getDadosPorTerminalFuncionarioData(){
        List<OutrosMovimentos> outrosMovimentos = new ArrayList<>();
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("SELECT  tipo_operacao_caixa,id_terminal, id_funcionario, data_hora_mov, \n" +
                    "       valor_movimentado, observacoes\n" +
                    "  FROM public.outros_movimentos where id_terminal = ? and id_funcionario = ? and date( data_hora_mov) = ?  and  id_entidade = ? ",false);
            ps.setInt(1, Integer.valueOf(request.getParameter("id_terminal")));
            ps.setInt(2, Integer.valueOf(request.getParameter("id_funcionario")));
            ps.setDate(3, Parser.toDbDate(request.getParameter("data_movs")));
            ps.setInt(4, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                OutrosMovimentos om = new OutrosMovimentos(request,rs);
                outrosMovimentos.add(om);
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
        return outrosMovimentos;
    }
    
    private void getDados(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("SELECT  tipo_operacao_caixa,id_terminal, id_funcionario, data_hora_mov, \n" +
            "       valor_movimentado, observacoes\n" +
            "  FROM public.outros_movimentos where id = ? and id_entidade = ? ",false);
            ps.setInt(1, id);
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                tipoOperacao = rs.getString(1);
                idTerminal = rs.getString(2);
                idFuncionario = rs.getString(3);
                dataHoraMov = rs.getString(4);
                valorMovimentado = Parser.toBigDecimalSt(rs.getString(5));
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
            PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO public.outros_movimentos(\n" +
            "            tipo_operacao_caixa, id_terminal, id_funcionario, data_hora_mov, \n" +
            "            valor_movimentado, observacoes, id_entidade)\n" +
            "    VALUES ( ?, ?, ?, ?, \n" +
            "            ?, ?, ? );");
            ps.setInt(1, Integer.valueOf(tipoOperacao));
            if(idTerminal == null||idTerminal.trim().equals("")){
                ps.setNull(2, java.sql.Types.BIGINT);
            }else{
                ps.setInt(2, Integer.valueOf(idTerminal));
            }
            ps.setInt(3, Integer.valueOf(idFuncionario));
            ps.setTimestamp(4, Parser.toDbTimeStamp(dataHoraMov));
            ps.setBigDecimal(5, Parser.toBigDecimalFromHtml(valorMovimentado));
            ps = Seter.set(ps, 6, observacoes);
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
            PreparedStatement ps = Parametros.getConexao(request).getPst("UPDATE public.outros_movimentos\n" +
            "   SET tipo_operacao_caixa=?, id_terminal=?, id_funcionario=?, data_hora_mov=?,\n" +
            "    valor_movimentado=?, observacoes=?\n" 
                    + " where id = ? and id_entidade = ? ", false);
            ps.setInt(1, Integer.valueOf(tipoOperacao));
            if(idTerminal == null||idTerminal.trim().equals("")){
                ps.setNull(2, java.sql.Types.BIGINT);
            }else{
                ps.setInt(2, Integer.valueOf(idTerminal));
            }
            ps.setInt(3, Integer.valueOf(idFuncionario));
            ps.setTimestamp(4, Parser.toDbTimeStamp(dataHoraMov));
            ps.setBigDecimal(5, Parser.toBigDecimalFromHtml(valorMovimentado));
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
        templateConteudo = ve.getTemplate( "templates/Modern/processos/outros_movimentos.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());       
        setOpcoesFiltro("outros_movimentos");
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"outros_movimentos").toString());
        return contextPrinc;
    }
    
    public String getValorMovimentado() {
        return Parser.toBigDecimalSt(valorMovimentado);
    }
    
    public String getTipoOperacao() {
        return tipoOperacao;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
}
