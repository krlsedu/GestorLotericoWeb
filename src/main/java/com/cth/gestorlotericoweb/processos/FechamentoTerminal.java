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
    String dataEncerramento;
    String restoCaixa;
    String totalMovimentosDia;
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
        this.dataEncerramento = request.getParameter("data_encerramento");
        this.restoCaixa = request.getParameter("resto_caixa");
        this.totalMovimentosDia = request.getParameter("total_movimentos_dia");
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
    
    public FechamentoTerminal(Integer id,HttpServletRequest request) {
        super(request, id);
        getDados();
    }
    
    private void getDados(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("SELECT  id_terminal, id_funcionario, data_encerramento, \n" +
"       resto_caixa, total_movimentos_dia, total_creditos_terminal, total_debitos_terminal, \n" +
"       saldo_terminal, diferenca_caixa, observacoes " +
"  FROM public.fechamento_terminais where id = ? and id_entidade = ? ",false);
            ps.setInt(1, id);
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                idTerminal = rs.getString(1);
                idFuncionario = rs.getString(2);
                dataEncerramento = rs.getString(3);
                restoCaixa =Parser.toDoubleSt(rs.getString(4));
                totalMovimentosDia = Parser.toDoubleSt(rs.getString(5));
                totalCreditosTerminal =  Parser.toDoubleSt(rs.getString(6));
                totalDebitosTerminal = Parser.toDoubleSt(rs.getString(7));
                saldoTerminal = Parser.toDoubleSt(rs.getString(8));
                diferencaCaixa = Parser.toDoubleSt(rs.getString(9));
                observacoes = rs.getString(10);
            }else{
                idTerminal = "";
                idFuncionario = "";
                dataEncerramento = "";
                restoCaixa = "";
                totalMovimentosDia = "";
                totalCreditosTerminal =  "";
                totalDebitosTerminal = "";
                saldoTerminal = "";
                diferencaCaixa = "";
                observacoes = "";
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }     
    
    public void insere(){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO public.fechamento_terminais(\n" +
"            id_terminal, id_funcionario, data_encerramento, \n" +
"            resto_caixa, total_movimentos_dia, total_creditos_terminal, total_debitos_terminal, \n" +
"            saldo_terminal, diferenca_caixa, observacoes, \n" +
"            id_entidade)\n" +
"    VALUES (?, ?, ?, \n" +
"            ?, ?, ?, ?, \n" +
"            ?, ?, ?, \n" +
"            ?)");
            ps.setInt(1, Integer.valueOf(idTerminal));
            ps.setInt(2, Integer.valueOf(idFuncionario));            
            ps.setDate(3, Parser.toDbDate(dataEncerramento));
            ps.setDouble(4, Parser.toDoubleFromHtml(restoCaixa));
            ps.setDouble(5, Parser.toDoubleFromHtml(totalMovimentosDia));
            ps.setDouble(6, Parser.toDoubleFromHtml(totalCreditosTerminal));
            ps.setDouble(7, Parser.toDoubleFromHtml(totalDebitosTerminal));
            ps.setDouble(8, Parser.toDoubleFromHtml(saldoTerminal));
            ps.setDouble(9, Parser.toDoubleFromHtml(diferencaCaixa));
            ps = Seter.set(ps, 10, observacoes);
            ps.setInt(11, Parametros.idEntidade);
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
            PreparedStatement ps = Parametros.getConexao(request).getPst("UPDATE public.fechamento_terminais\n" +
"   SET  id_terminal=?, id_funcionario=?, data_encerramento=?,  \n" +
"       resto_caixa=?, total_movimentos_dia=?, total_creditos_terminal=?, \n" +
"       total_debitos_terminal=?, saldo_terminal=?, diferenca_caixa=?, \n" +
"       observacoes=? \n" 
                    + " where id = ? and id_entidade = ? ", false);
            ps.setInt(1, Integer.valueOf(idTerminal));
            ps.setInt(2, Integer.valueOf(idFuncionario));            
            ps.setDate(3, Parser.toDbDate(dataEncerramento));
            ps.setDouble(4, Parser.toDoubleFromHtml(restoCaixa));
            ps.setDouble(5, Parser.toDoubleFromHtml(totalMovimentosDia));
            ps.setDouble(6, Parser.toDoubleFromHtml(totalCreditosTerminal));
            ps.setDouble(7, Parser.toDoubleFromHtml(totalDebitosTerminal));
            ps.setDouble(8, Parser.toDoubleFromHtml(saldoTerminal));
            ps.setDouble(9, Parser.toDoubleFromHtml(diferencaCaixa));
            ps = Seter.set(ps, 10, observacoes);
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
        Double totalMovimentosDiaL = 0.0;
        String sql = "select \n" +
                    "	sum(case when tipo_operacao_caixa in (1) then valor_movimentado else valor_movimentado * (-1) end) \n" +
                    "from \n" +
                    "	movimentos_caixas  \n" +
                    "where\n" +
                    "	date(data_hora_mov) = ? and\n" +
                    "	id_terminal = ? and\n" +
                    "	id_funcionario = ? \n" +
                    "	";
        try {
            PreparedStatement ps = Parametros.getConexao().getPst(sql,false);
            dataFechamentoDt = Parser.toDbDate(dataEncerramento);
            if(dataFechamentoDt!=null&&idTerminal!=null&&idFuncionario!=null){
                ps.setDate(1, dataFechamentoDt);
                ps = Seter.set(ps, 2, Integer.valueOf(idTerminal));
                ps = Seter.set(ps, 3, Integer.valueOf(idFuncionario));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    totalMovimentosDiaL = rs.getDouble(1);
                }else{
                    totalMovimentosDiaL = 0.0;
                }
                totalMovimentosDiaL += getSaldoAbertura();
                totalMovimentosDiaL += Parser.toDoubleFromHtml(restoCaixa);
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage()+sql, ex, request);
        }        
        return Parser.toHtmlDouble(totalMovimentosDiaL);
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
            PreparedStatement ps = Parametros.getConexao().getPst(sql,false);
            if(dataFechamentoDt!=null&&idTerminal!=null&&idFuncionario!=null){
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
    
    public String getSaldoTerminal(){
      Double valor = Parser.toDoubleFromHtml(totalCreditosTerminal)-Parser.toDoubleFromHtml(totalDebitosTerminal);
      return Parser.toHtmlDouble(valor);
    };
    
    public String getDiferencaCaixa(){
      Double valor = Parser.toDoubleFromHtml(totalMovimentosDia)-Parser.toDoubleFromHtml(saldoTerminal);
      return Parser.toHtmlDouble(valor);
    };
    
    public String getDataFechar(){
        String sql = "SELECT at.data_abertura FROM\n" +
                        "	abertura_terminais at\n" +
                        "where\n" +
                        "	not EXISTS (SELECT 1 FROM fechamento_terminais ft where ft.id_terminal = at.id_terminal and ft.id_funcionario = at.id_funcionario and at.data_abertura = ft.data_encerramento and at.id_entidade = ft.id_entidade) and \n" +
                        "	at.id_terminal = ? and\n" +
                        "	at.id_funcionario = ?\n" +
                        "order by\n" +
                        "	at.data_abertura\n" +
                        "limit 1";
        try{
            PreparedStatement ps = Parametros.getConexao().getPst(sql, false);
            ps.setInt(1, Parser.toInteger(idTerminal));
            ps.setInt(2, Parser.toInteger(idFuncionario));
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString(1);
            }
        }catch(SQLException e){
            new LogError(e.getMessage()+" - "+sql, e, request);
        }
        return null;
    }
    
}
