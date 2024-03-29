/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.processos;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.utils.Parser;
import com.cth.gestorlotericoweb.utils.MyPreparedStatement;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Date;
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
public class FechamentoTerminal extends Processos{
    String idTerminal;
    String idFuncionario;
    String dataEncerramento;
    String dataAbertura;
    String restoCaixa;
    String totalMovimentosDia;
    String totalCreditosTerminal;
    String totalDebitosTerminal;
    String saldoTerminal;
    String diferencaCaixa;
    String observacoes;
    Date dataFechamentoDt;
    Date dataAberturaDt;
    public FechamentoTerminal(HttpServletRequest request) {
        super(request);
        seFechamentoTErminal();
    }
    
    private void seFechamentoTErminal() {
        this.idTerminal = request.getParameter("id_terminal");
        this.idFuncionario = request.getParameter("id_funcionario");
        this.dataEncerramento = request.getParameter("data_encerramento");
        this.dataAbertura = request.getParameter("data_abertura");
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
        getDadosPorId();
    }
    
    public FechamentoTerminal(Integer id,HttpServletRequest request) {
        super(request, id);
        getDadosPorId();
    }
    
    private void getDadosPorId(){
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
                restoCaixa =Parser.toBigDecimalSt(rs.getString(4));
                totalMovimentosDia = Parser.toBigDecimalSt(rs.getString(5));
                totalCreditosTerminal =  Parser.toBigDecimalSt(rs.getString(6));
                totalDebitosTerminal = Parser.toBigDecimalSt(rs.getString(7));
                saldoTerminal = Parser.toBigDecimalSt(rs.getString(8));
                diferencaCaixa = Parser.toBigDecimalSt(rs.getString(9));
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
    
    public List<FechamentoTerminal> getDadosPorTerminalFuncionarioData(){
        List<FechamentoTerminal> fechamentoTerminalList = new ArrayList<>();
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("SELECT  id_terminal, id_funcionario, data_encerramento, \n" +
                    "       resto_caixa, total_movimentos_dia, total_creditos_terminal, total_debitos_terminal, \n" +
                    "       saldo_terminal, diferenca_caixa, observacoes " +
                    "  FROM public.fechamento_terminais where id_terminal = ? and id_funcionario = ? and data_encerramento = ? and id_entidade = ? ",false);
            ps.setInt(1, Integer.valueOf(request.getParameter("id_terminal")));
            ps.setInt(2, Integer.valueOf(request.getParameter("id_funcionario")));
            ps.setDate(3, Parser.toDbDate(request.getParameter("data_movs")));
            ps.setInt(4, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                idTerminal = rs.getString(1);
                idFuncionario = rs.getString(2);
                dataEncerramento = rs.getString(3);
                restoCaixa =Parser.toBigDecimalSt(rs.getString(4));
                totalMovimentosDia = Parser.toBigDecimalSt(rs.getString(5));
                totalCreditosTerminal =  Parser.toBigDecimalSt(rs.getString(6));
                totalDebitosTerminal = Parser.toBigDecimalSt(rs.getString(7));
                saldoTerminal = Parser.toBigDecimalSt(rs.getString(8));
                diferencaCaixa = Parser.toBigDecimalSt(rs.getString(9));
                observacoes = rs.getString(10);
                fechamentoTerminalList.add(this);
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
        return fechamentoTerminalList;
    }
    
    
    public void insere(){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO public.fechamento_terminais(\n" +
"            id_terminal, id_funcionario, data_encerramento, \n" +
"            resto_caixa, total_movimentos_dia, total_creditos_terminal, total_debitos_terminal, \n" +
"            saldo_terminal, diferenca_caixa, observacoes, \n" +
"            id_entidade,id_loterica)\n" +
"    VALUES (?, ?, ?, \n" +
"            ?, ?, ?, ?, \n" +
"            ?, ?, ?, \n" +
"            ?, ?)");
            ps.setInt(1, Integer.valueOf(idTerminal));
            ps.setInt(2, Integer.valueOf(idFuncionario));            
            ps.setDate(3, Parser.toDbDate(dataEncerramento));
            ps.setBigDecimal(4, Parser.toBigDecimalFromHtml(restoCaixa));
            ps.setBigDecimal(5, Parser.toBigDecimalFromHtml(totalMovimentosDia));
            ps.setBigDecimal(6, Parser.toBigDecimalFromHtml(totalCreditosTerminal));
            ps.setBigDecimal(7, Parser.toBigDecimalFromHtml(totalDebitosTerminal));
            ps.setBigDecimal(8, Parser.toBigDecimalFromHtml(saldoTerminal));
            ps.setBigDecimal(9, Parser.toBigDecimalFromHtml(diferencaCaixa));
            ps = MyPreparedStatement.set(ps, 10, observacoes);
            ps.setInt(11, Parametros.idEntidade);
            ps.setInt(12, Parametros.getIdLoterica());
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
                    + " where id = ? and id_entidade = ? AND id_loterica = ?", false);
            ps.setInt(1, Integer.valueOf(idTerminal));
            ps.setInt(2, Integer.valueOf(idFuncionario));            
            ps.setDate(3, Parser.toDbDate(dataEncerramento));
            ps.setBigDecimal(4, Parser.toBigDecimalFromHtml(restoCaixa));
            ps.setBigDecimal(5, Parser.toBigDecimalFromHtml(totalMovimentosDia));
            ps.setBigDecimal(6, Parser.toBigDecimalFromHtml(totalCreditosTerminal));
            ps.setBigDecimal(7, Parser.toBigDecimalFromHtml(totalDebitosTerminal));
            ps.setBigDecimal(8, Parser.toBigDecimalFromHtml(saldoTerminal));
            ps.setBigDecimal(9, Parser.toBigDecimalFromHtml(diferencaCaixa));
            ps = MyPreparedStatement.set(ps, 10, observacoes);
            id = Integer.valueOf(idL);
            ps.setInt(11, id);
            ps.setInt(12, Parametros.idEntidade);
            ps.setInt(13, Parametros.getIdLoterica());
            
            ps.execute();
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public VelocityContext getHtml(VelocityContext contextPrinc,VelocityEngine ve,String idS){        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/processos/fechamento_terminais.html" , "UTF-8");
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
        return Parser.toHtmlBigDecimal(getTotalMovimentosDiaBigDecimal());
    }
    
    public BigDecimal getTotalMovimentosDiaBigDecimal(){
        BigDecimal totalMovimentosDiaL = BigDecimal.ZERO;
        String sql = "select \n" +
                    "	sum(case when tipo_operacao_caixa in (1) then valor_movimentado else valor_movimentado * (-1) end) \n" +
                    "from \n" +
                    "	movimentos_caixas  \n" +
                    "where\n" +
                    "	date(data_hora_mov) = ? and\n" +
                    "	id_terminal = ? and\n" +
                    "	id_funcionario = ? and\n" +
                    "	id_entidade = ? \n" +
                    "	";
        try {
            PreparedStatement ps = Parametros.getConexao().getPst(sql,false);
            dataFechamentoDt = Parser.toDbDate(dataEncerramento);
            if(dataFechamentoDt!=null&&idTerminal!=null&&idFuncionario!=null){
                ps.setDate(1, dataFechamentoDt);
                ps = MyPreparedStatement.set(ps, 2, Integer.valueOf(idTerminal));
                ps = MyPreparedStatement.set(ps, 3, Integer.valueOf(idFuncionario));
                ps.setInt(4, Parametros.idEntidade);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    totalMovimentosDiaL = rs.getBigDecimal(1);
                }else{
                    totalMovimentosDiaL = BigDecimal.ZERO;
                }
                if (totalMovimentosDiaL==null) {
                    totalMovimentosDiaL = BigDecimal.ZERO;
                }
                //(saldoMovimentacoes + saldoOutrosMovimentos + valorGeracaoBoloes)-saldoVendas-trocasDeTeleSena
                totalMovimentosDiaL = totalMovimentosDiaL.add(getSaldoOutrosMovimentos());
                totalMovimentosDiaL = totalMovimentosDiaL.add(getSaldoGeracoesBoloes());
                
                totalMovimentosDiaL = totalMovimentosDiaL.subtract(getSaldoVendas());
                totalMovimentosDiaL = totalMovimentosDiaL.subtract(getSaldoTrocaTeleSena());
    
                totalMovimentosDiaL = totalMovimentosDiaL.subtract(getSaldoAbertura());
                totalMovimentosDiaL = totalMovimentosDiaL.add(Parser.toBigDecimalFromHtmlNNull(restoCaixa));
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage()+sql, ex, request);
        }        
        return totalMovimentosDiaL;
    }
    
    public BigDecimal getSaldoAbertura(){
        BigDecimal valor = BigDecimal.ZERO;
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
                ps = MyPreparedStatement.set(ps, 2, Integer.valueOf(idTerminal));
                ps = MyPreparedStatement.set(ps, 3, Integer.valueOf(idFuncionario));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    valor = rs.getBigDecimal(1);
                }
            }
        } catch (SQLException ex) {
            new LogError(sql, ex, request);
        }
        return valor;
    }
    
    public BigDecimal getSaldoVendas(){
        //language=PostgresPLSQL
        String sql = "SELECT " +
                "       sum(valor_movimentado)" +
                "   FROM " +
                "       outros_movimentos" +
                "   where" +
                "   tipo_operacao_caixa IN (13,9,5,3) AND" +
                "   date(data_hora_mov) = ? and " +
                "	id_terminal = ? and " +
                "	id_funcionario = ? AND " +
                "   id_entidade = ? ";
        return getSaldoOperacoes(sql);
    }
    
    public BigDecimal getSaldoEntradas(){
        //language=PostgresPLSQL
        String sql = "SELECT " +
                "       sum(valor_movimentado)" +
                "   FROM " +
                "       outros_movimentos" +
                "   where" +
                "   tipo_operacao_caixa IN (2,8,12,4,14,7) AND" +
                "   date(data_hora_mov) = ? and " +
                "	id_terminal = ? and " +
                "	id_funcionario = ? AND " +
                "   id_entidade = ? ";
        return getSaldoOperacoes(sql);
    }
    
    public BigDecimal getSaldoGeracoesBoloes(){
        //language=PostgresPLSQL
        String sql = "SELECT " +
                "       sum(valor_movimentado)" +
                "   FROM " +
                "       outros_movimentos" +
                "   where" +
                "   tipo_operacao_caixa IN (8) AND" +
                "   date(data_hora_mov) = ? and " +
                "	id_terminal = ? and " +
                "	id_funcionario = ? AND " +
                "   id_entidade = ? ";
        return getSaldoOperacoes(sql);
    }
    
    public BigDecimal getSaldoTrocaTeleSena(){
        //language=PostgresPLSQL
        String sql = "SELECT " +
                "       sum(valor_movimentado)" +
                "   FROM " +
                "       outros_movimentos" +
                "   where" +
                "   tipo_operacao_caixa IN (4) AND" +
                "   date(data_hora_mov) = ? and " +
                "	id_terminal = ? and " +
                "	id_funcionario = ? AND " +
                "   id_entidade = ? ";
        return getSaldoOperacoes(sql);
    }
    
    public BigDecimal getSaldoOperacoes(String sql){
        BigDecimal valor = BigDecimal.ZERO;
        try {
            PreparedStatement ps = Parametros.getConexao().getPst(sql,false);
            if(dataFechamentoDt!=null&&idTerminal!=null&&idFuncionario!=null){
                ps.setDate(1, dataFechamentoDt);
                ps = MyPreparedStatement.set(ps, 2, Integer.valueOf(idTerminal));
                ps = MyPreparedStatement.set(ps, 3, Integer.valueOf(idFuncionario));
                ps = MyPreparedStatement.set(ps,4,Parametros.idEntidade);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    valor =  rs.getBigDecimal(1);
                }
            }
        } catch (SQLException ex) {
            new LogError(sql, ex, request);
        }
        if (valor==null) {
            valor = BigDecimal.ZERO;
        }
        return valor;
    }
    
    public BigDecimal getSaldoOutrosMovimentos(){
        //((FinalDia - InicioDia)+saldoMovimentacoes + saldoOutrosMovimentos + valorGeracaoBoloes)-trocasDeTeleSena
        BigDecimal valor = BigDecimal.ZERO;
        String sql = "SELECT \n" +
                //"	sum(case when tipo_operacao_caixa in (13,9,8,5,3) then valor_movimentado * (-1) else valor_movimentado  end)\n" +
                "	sum(case when tipo_operacao_caixa in (1,9,5,15,11,3,13,6) then valor_movimentado * (-1) else valor_movimentado end)\n" +
                "FROM \n" +
                "	outros_movimentos \n" +
                                "where" +
                //"   tipo_operacao_caixa NOT IN (1,2,6,7,11,12,14,15) AND" +
                "   date(data_hora_mov) = ? and " +
                "	id_terminal = ? and " +
                "	id_funcionario = ?";
        try {
            PreparedStatement ps = Parametros.getConexao().getPst(sql,false);
            if(dataFechamentoDt!=null&&idTerminal!=null&&idFuncionario!=null){
                ps.setDate(1, dataFechamentoDt);
                ps = MyPreparedStatement.set(ps, 2, Integer.valueOf(idTerminal));
                ps = MyPreparedStatement.set(ps, 3, Integer.valueOf(idFuncionario));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    valor =  rs.getBigDecimal(1);
                }
            }
        } catch (SQLException ex) {
            new LogError(sql, ex, request);
        }
        if (valor==null) {
            valor = BigDecimal.ZERO;
        }
        return valor;
    }
    
    public String getSaldoTerminal(){
        return Parser.toHtmlBigDecimal(getSaldoTerminalBigDecimal());
    }
    
    public BigDecimal getSaldoTerminalBigDecimal(){
        return Parser.toBigDecimalFromHtmlNNull(totalCreditosTerminal).subtract(Parser.toBigDecimalFromHtmlNNull(totalDebitosTerminal));
    };
    
    public String getDiferencaCaixa(){
        return Parser.toHtmlBigDecimal(getDiferencaCaixaBigDec());
    }
    
    public BigDecimal getDiferencaCaixaBigDec(){
        //((FinalDia - InicioDia)+saldoMovimentacoes + saldoOutrosMovimentos + valorGeracaoBoloes)-trocasDeTeleSena
      return Parser.toBigDecimalFromHtmlNNull(totalMovimentosDia).subtract(Parser.toBigDecimalFromHtmlNNull(saldoTerminal));
    };
    
    public String getRestoCaixaDiaAnterior(){
        return Parser.toHtmlBigDecimal(getRestoCaixaDiaAnt());
    }
    
    public BigDecimal getRestoCaixaDiaAnt(){
        BigDecimal valor = BigDecimal.ZERO;
        //language=PostgreSQL
        String sql = "Select resto_caixa from fechamento_terminais " +
                " where " +
                "   data_encerramento < ? AND " +
                "   id_terminal = ? AND " +
                "   id_entidade = ? " +
                " ORDER BY " +
                "   data_encerramento DESC " +
                " LIMIT 1";
        try {
            PreparedStatement preparedStatement = Parametros.getConexao().getPst(sql,false);
            dataAberturaDt = Parser.toDbDate(dataAbertura);
            preparedStatement.setDate(1,dataAberturaDt);
            preparedStatement.setInt(2, Integer.valueOf(idTerminal));
            preparedStatement.setInt(3,Parametros.idEntidade);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                valor = resultSet.getBigDecimal(1);
            }
        }catch(SQLException e){
            new LogError(e.getMessage()+" - "+sql, e, request);
        }
        return valor;
    }
    
    public String getRestoCaixa() {
        return Parser.toBigDecimalSt(restoCaixa);
    }
    
    public String getObservacoes() {
        return observacoes;
    }
}
