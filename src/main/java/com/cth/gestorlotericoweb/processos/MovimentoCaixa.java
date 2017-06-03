/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.processos;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.estoque.MovimentosEstoque;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.utils.Parser;
import com.cth.gestorlotericoweb.utils.Seter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import jdk.nashorn.internal.objects.annotations.Setter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author CarlosEduardo
 */
public class MovimentoCaixa extends Processos{
    String idTerminal;
    public String idFuncionario;
    String idCofre;
    String dataHoraMov;
    String valorMovimentado;
    String tipoOperacao;
    String tipoOperacaoAnt;
    String observacoes;
    
    BigDecimal valorMoeda;
    
    public MovimentoCaixa(HttpServletRequest request) {
        super(request);
        setMovimentosCaixas();
    }
    
    public MovimentoCaixa(HttpServletRequest request, ResultSet rs) throws SQLException {
        super(request);
        tipoOperacao = rs.getString(1);
        idTerminal = rs.getString(2);
        idFuncionario = rs.getString(3);
        dataHoraMov = rs.getString(4);
        valorMovimentado = Parser.toBigDecimalSt(rs.getString(5));
        observacoes = rs.getString(6);
        idCofre = rs.getString(7);
        valorMoeda = rs.getBigDecimal(8);
    }
    
    private void setMovimentosCaixas() {
        this.idTerminal = request.getParameter("id_terminal");
        this.idFuncionario = request.getParameter("id_funcionario");
        this.idCofre = request.getParameter("id_cofre");
        this.dataHoraMov = request.getParameter("data_hora_mov");
        this.valorMovimentado = request.getParameter("valor_movimentado");
        this.tipoOperacao = request.getParameter("tipo_operacao_caixa");
        this.observacoes = request.getParameter("observacoes");
        this.valorMoeda = Parser.toBigDecimalFromHtmlNull(request.getParameter("valor_moeda"));
    }
    
    public MovimentoCaixa(Integer id,HttpServletRequest request) {
        super(request, id);
        getDadosPorId();
    }
    
    public void getDadosPorId(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("SELECT  tipo_operacao_caixa,id_terminal, id_funcionario, data_hora_mov, \n" +
                    "       valor_movimentado, observacoes, id_cofre, valor_moeda \n" +
                    "  FROM public.movimentos_caixas where id = ? and id_entidade = ? ",false);
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
                idCofre = rs.getString(7);
                valorMoeda = rs.getBigDecimal(8);
            }else{
                tipoOperacao = "";
                idTerminal = "";
                idFuncionario = "";
                dataHoraMov = "";
                valorMovimentado = "";
                observacoes = "";
                idCofre = "";
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void deleta(){
        try{
            PreparedStatement psIdMovCofre = Parametros.getConexao().getPst("SELECT id FROM movimentos_cofres WHERE id_movimento_caixa = ? and id_entidade = ? ",false);
            psIdMovCofre.setInt(1, id);
            psIdMovCofre.setInt(2, Parametros.idEntidade);
            ResultSet rs = psIdMovCofre.executeQuery();
            while (rs.next()) {     
                MovimentoCofre movimentoCofre = new  MovimentoCofre(rs.getInt(1), request);
                movimentoCofre.deleta();          
            }
            PreparedStatement ps = Parametros.getConexao(request).getPst("delete from movimentos_caixas where id = ? and id_entidade = ?", Boolean.FALSE);     
            ps.setInt(1, Integer.valueOf(request.getParameter("id")));
            ps.setInt(2, Parametros.idEntidade);
            ps.execute();
        }catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public List<MovimentoCaixa> getDadosPorTerminalFuncionarioData(){
        try {
            List<MovimentoCaixa> movimentoCaixaList = new ArrayList<>();
            PreparedStatement ps = Parametros.getConexao().getPst("SELECT  tipo_operacao_caixa,id_terminal, id_funcionario, data_hora_mov, \n" +
                    "       valor_movimentado, observacoes, id_cofre, valor_moeda \n" +
                    "  FROM public.movimentos_caixas where id_terminal = ? and id_funcionario = ? and date( data_hora_mov) = ?  and id_entidade = ? ",false);
            ps.setInt(1, Integer.valueOf(request.getParameter("id_terminal")));
            ps.setInt(2, Integer.valueOf(request.getParameter("id_funcionario")));
            ps.setDate(3, Parser.toDbDate(request.getParameter("data_movs")));
            ps.setInt(4, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                MovimentoCaixa mc = new MovimentoCaixa(request,rs);
                movimentoCaixaList.add(mc);
            }
            return movimentoCaixaList;
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
        return new ArrayList<>();
    }
    
    public void insere(){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO public.movimentos_caixas(\n" +
"            tipo_operacao_caixa, id_terminal, id_funcionario, data_hora_mov, \n" +
"            valor_movimentado, observacoes, id_entidade,id_cofre, valor_moeda)\n" +
"    VALUES (?, ?, ?, ?, \n" +
"            ?, ?, ?, ?, ?);");
            ps.setInt(1, Integer.valueOf(tipoOperacao));
            ps = Seter.set(ps,2,Parser.toIntegerNull(idTerminal));
            ps.setInt(3, Integer.valueOf(idFuncionario));
            ps.setTimestamp(4, Parser.toDbTimeStamp(dataHoraMov));
            ps.setBigDecimal(5, Parser.toBigDecimalFromHtml(valorMovimentado));
            ps = Seter.set(ps, 6, observacoes);
            ps.setInt(7, Parametros.idEntidade);
            ps = Seter.set(ps,8,Parser.toIntegerNull(idCofre));
            ps = Seter.set(ps,9,valorMoeda);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
    
                gravaOutrosMovimentos();
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
            "    valor_movimentado=?, observacoes=?, id_cofre=? \n" 
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
            ps = Seter.set(ps,7,Integer.valueOf(idCofre));
            
            id = Integer.valueOf(idL);
            MovimentoCaixa movimentoCaixa = new MovimentoCaixa(request);
            movimentoCaixa.id = id;
            movimentoCaixa.getDadosPorId();
            tipoOperacaoAnt = movimentoCaixa.tipoOperacao;
            ps.setInt(8, id);
            ps.setInt(9, Parametros.idEntidade);
            
            ps.execute();
            gravaOutrosMovimentos();
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    private void gravaOutrosMovimentos(){
        if(this.idCofre!=null){
            try{
                MovimentoCofre movimentoCofre = new MovimentoCofre(request, this);
                movimentoCofre.gravaAutoMov();
                if (valorMoeda != null) {
                    MovimentosEstoque movimentosEstoque = new MovimentosEstoque(this);
                    movimentosEstoque.gravaAutoMov();
                }
            }catch(Exception e){
            
            }
        }
    }
    
    public VelocityContext getHtml(VelocityContext contextPrinc,VelocityEngine ve,String idS){        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/processos/movimentos_caixas.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());       
        setOpcoesFiltro("movimentos_caixas");
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"movimentos_caixas").toString());
        return contextPrinc;
    }
    
    public String getTipoOperacao() {
        return tipoOperacao;
    }
    
    public String getValorMovimentado() {
        return Parser.toBigDecimalSt(valorMovimentado);
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public BigDecimal getValorMoeda() {
        return valorMoeda;
    }
    
    public String getTipoOperacaoAnt() {
        return tipoOperacaoAnt;
    }
}
