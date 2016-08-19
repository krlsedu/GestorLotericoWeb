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
import java.io.StringWriter;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author CarlosEduardo
 */
public class FechamentoTerminal extends Processos{
    String idTerminal;
    String idFuncionario;
    String dataFechamento;
    String restoCaixa;
    Double totalMovimentosDia;
    String totalCreditosTerminal;
    String totalDebitosTerminal;
    String saldoTerminal;
    String diferencaCaixa;
    String observacoes;
    Date dataFechamentoDt;
    public FechamentoTerminal(HttpServletRequest request) {
        super(request);
        seFechamentoTErminal();
    }
    
    private void seFechamentoTErminal() {
        this.idTerminal = request.getParameter("id_terminal");
        this.idFuncionario = request.getParameter("id_funcionario");
        this.dataFechamento = request.getParameter("data_fechamento");
        this.restoCaixa = request.getParameter("resto_caixa");
        this.totalCreditosTerminal = request.getParameter("total_creditos_terminal");
        this.totalDebitosTerminal = request.getParameter("total_debitos_terminal");
        this.saldoTerminal = request.getParameter("saldo_terminal");
        this.diferencaCaixa = request.getParameter("diferenca_caixa");
        this.observacoes = request.getParameter("observacoes");
    }   

    public FechamentoTerminal(HttpServletRequest request, Integer id) {
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
                idTerminal = rs.getString(1);
                idFuncionario = rs.getString(1);
                dataFechamento = rs.getString(1);
                restoCaixa = rs.getString(1);
                //totalMovimentosDia;
                totalCreditosTerminal =  rs.getString(1);
                totalDebitosTerminal = rs.getString(1);
                //saldoTerminal;
                //diferencaCaixa;
            }else{
                idTerminal = rs.getString(1);
                idFuncionario = rs.getString(1);
                dataFechamento = rs.getString(1);
                restoCaixa = rs.getString(1);
                //totalMovimentosDia;
                totalCreditosTerminal =  rs.getString(1);
                totalDebitosTerminal = rs.getString(1);
                //saldoTerminal;
                //diferencaCaixa;
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    } 
    
    public VelocityContext getHtml(VelocityContext contextPrinc,VelocityEngine ve,String idS){        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/fechamento_terminais.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());       
        setOpcoesFiltro("fechamento_terminais");
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"fechamento_terminais").toString());
        return contextPrinc;
    }
    
    public String getTotalMovimentosDia(){
        String sql = "select \n" +
                    "	sum(case when tipo_operacao_caixa in (1) then valor_movimentado else valor_movimentado * (-1) end) \n" +
                    "from \n" +
                    "	movimentos_caixas  \n" +
                    "where\n" +
                    "	data_hora_mov::date = ? and\n" +
                    "	id_terminal = ? and\n" +
                    "	id_funcionario = ? \n" +
                    "	";
        try {
            PreparedStatement ps = Parametros.getConexao().getPst(sql);
            if(dataFechamentoDt!=null){
                ps.setDate(1, dataFechamentoDt);
                ps = Seter.set(ps, 2, Integer.valueOf(idTerminal));
                ps = Seter.set(ps, 3, Integer.valueOf(idFuncionario));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    totalMovimentosDia = rs.getDouble(1);
                }else{
                    totalMovimentosDia = 0.0;
                }
                totalMovimentosDia += getSaldoAbertura();
                totalMovimentosDia += Parser.toDouble(restoCaixa);
            }
        } catch (SQLException ex) {
            new LogError(sql, ex, request);
        }        
        return Parser.toHtmlDouble(totalMovimentosDia);
    }
    
    public Double getSaldoAbertura(){
        String sql = "select \n" +
                "	troco_dia_anterior+troco_dia \n" +
                "from \n" +
                "	abertura_terminais  \n" +
                "where\n" +
                "	data_abertura = ? and\n" +
                "	id_terminal = ? and\n" +
                "	id_funcionario = ?";
        try {
            PreparedStatement ps = Parametros.getConexao().getPst(sql);
            if(dataFechamentoDt!=null){
                ps.setDate(1, dataFechamentoDt);
                ps = Seter.set(ps, 2, Integer.valueOf(idTerminal));
                ps = Seter.set(ps, 3, Integer.valueOf(idFuncionario));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getDouble(1);
                }else{
                    return 0.0;
                }
                
            }
        } catch (SQLException ex) {
            new LogError(sql, ex, request);
        }
        return 0.0;
    }
}
