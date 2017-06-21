/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.processos;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.saldos.SaldoCofre;
import com.cth.gestorlotericoweb.utils.Parser;
import com.cth.gestorlotericoweb.utils.MyPreparedStatement;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author CarlosEduardo
 */
public class MovimentoCofre extends Processos{
    Integer idMovimentoCaixa;
    Integer idMovimentoConta;
    Integer idAberturaTerminal;
    String idCofre;
    Timestamp dataHoraMov;
    public BigDecimal valorMovimentado;
    public String tipoOperacao;
    String observacoes;
    public Integer numeroVolumes;
    
    public MovimentoCofre(HttpServletRequest request) {
        super(request);
        setMovimentosCaixas();
    }
    
    private void setMovimentosCaixas() {
        try{
            this.idMovimentoCaixa = Integer.valueOf(request.getParameter("id_movimento_caixa"));
        }catch(Exception e){
            this.idMovimentoCaixa = null;
        }
        this.idCofre = request.getParameter("id_cofre");
        this.dataHoraMov = Parser.toDbTimeStamp(request.getParameter("data_hora_mov"));
        this.valorMovimentado = Parser.toBigDecimalFromHtml(request.getParameter("valor_movimentado"));
        this.tipoOperacao = request.getParameter("tipo_movimento_cofre");
        this.observacoes = request.getParameter("observacoes");
        numeroVolumes = 1;
    }
    
    public MovimentoCofre(Integer id,HttpServletRequest request) {
        super(request, id);
        getDados();
    }    
    
    public MovimentoCofre(HttpServletRequest request,MovimentoCaixa movimentoCaixa) {
        super(request);
        this.id = getIdMovimentoCofre(movimentoCaixa);
        this.idCofre = movimentoCaixa.idCofre.toString();
        this.idMovimentoCaixa = movimentoCaixa.id;
        this.dataHoraMov = movimentoCaixa.dataHoraMov;
        this.valorMovimentado = movimentoCaixa.valorMovimentado;
        this.tipoOperacao = movimentoCaixa.tipoOperacao==1?"2":"1";
        this.numeroVolumes = 1;
    }
    
    public MovimentoCofre(HttpServletRequest request,AberturaTerminal aberturaTerminal) {
        super(request);
        this.id = getIdMovimentoCofre(aberturaTerminal);
        this.idCofre = aberturaTerminal.idCofre;
        this.idAberturaTerminal = aberturaTerminal.id;
        this.dataHoraMov = new Timestamp(aberturaTerminal.dataAbertura.getTime());
        this.valorMovimentado = aberturaTerminal.trocoDia;
        this.tipoOperacao = "1";
        this.numeroVolumes = 1;
    }
    
    public MovimentoCofre(HttpServletRequest request,MovimentoConta movimentoConta) {
        super(request);
        this.id = getIdMovimentoCofre(movimentoConta);
        this.idCofre = movimentoConta.idCofre;
        this.idMovimentoConta = movimentoConta.id;
        this.dataHoraMov = movimentoConta.dataHoraMov;
        this.valorMovimentado = movimentoConta.valorMovimentado;
        this.numeroVolumes = movimentoConta.numeroVolumes;
        this.tipoOperacao = movimentoConta.tipoMovimentoConta.trim().equals("1")?"2":"1";        
    }
    
    private Integer getIdMovimentoCofre(MovimentoCaixa movimentoCaixa){
        try{
            PreparedStatement ps = Parametros.getConexao().getPst("select id from movimentos_cofres where id_movimento_caixa = ? and id_entidade = ?",false);
            ps.setInt(1, movimentoCaixa.id);
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }catch(SQLException ex){
            new LogError(ex.getMessage(), ex,request);            
        }
        return 0;
    }
    
    private Integer getIdMovimentoCofre(AberturaTerminal aberturaTerminal){
        try{
            PreparedStatement ps = Parametros.getConexao().getPst("select id from movimentos_cofres where id_abertura_terminal = ? and id_entidade = ?",false);
            ps.setInt(1, aberturaTerminal.id);
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }catch(SQLException ex){
            new LogError(ex.getMessage(), ex,request);
        }
        return 0;
    }
    
    private Integer getIdMovimentoCofre(MovimentoConta movimentoConta){
        try{
            PreparedStatement ps = Parametros.getConexao().getPst("select id from movimentos_cofres where id_movimento_conta = ? and id_entidade = ?",false);
            ps.setInt(1, movimentoConta.id);
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }catch(SQLException ex){
            new LogError(ex.getMessage(), ex,request);            
        }
        return 0;
    }
    
    private void getDados(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("SELECT  id_cofre, id_movimento_caixa, data_hora_mov,  \n" +
                        "       valor_movimentado, tipo_movimento_cofre, observacoes,id_movimento_conta  \n" +
                        "  FROM public.movimentos_cofres where id = ? and id_entidade = ? ",false);
            ps.setInt(1, id);
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                idCofre = rs.getString(1);
                idMovimentoCaixa = rs.getInt(2);
                dataHoraMov = rs.getTimestamp(3);
                valorMovimentado = rs.getBigDecimal(4);
                tipoOperacao = rs.getString(5);
                observacoes = rs.getString(6);
                idMovimentoConta = rs.getInt(7);
                numeroVolumes = 1;
            }else{
                tipoOperacao = "";
                observacoes = "";
                idCofre = "";
                idMovimentoCaixa = null;
                idMovimentoConta = null;
                numeroVolumes =1;
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void grava(){
        if("0".equals(request.getParameter("id"))){
            insere();
        }else{
            altera();
        }
        SaldoCofre saldoCofre = new SaldoCofre(request);
        saldoCofre.grava(this);
    }
    
    public void gravaAutoMov(){
        if(this.id == 0){
            insere();
        }else{
            altera();
        }
        SaldoCofre saldoCofre = new SaldoCofre(request);
        saldoCofre.grava(this);
    }
    
    public void deleta(){
        try{
            if(tipoOperacao.equals("1")){
                PreparedStatement psUpSaldos = Parametros.getConexao().getPst("update\n" +
                                                                "	saldos_cofres \n" +
                                                                "set\n" +
                                                                "	saldo = saldo + ?\n" +
                                                                "where\n" +
                                                                "	id > (SELECT id FROM saldos_cofres sc1 where id_movimento_cofre = ? limit 1) and"
                                                                + "     id_cofre = ? and"
                                                                + "     data_saldo >= (SELECT data_saldo FROM saldos_cofres sc1 where id_movimento_cofre = ? limit 1) and"
                                                                + "     id_entidade = ? ", false);
                psUpSaldos.setBigDecimal(1,valorMovimentado.multiply(new BigDecimal(numeroVolumes)));
                psUpSaldos.setInt(2, id);
                psUpSaldos.setInt(3, Integer.valueOf(idCofre));
                psUpSaldos.setInt(4, id);
                psUpSaldos.setInt(5, Parametros.idEntidade);
                psUpSaldos.execute();
            }else{
                PreparedStatement psUpSaldos = Parametros.getConexao().getPst("update\n" +
                                                                "	saldos_cofres \n" +
                                                                "set\n" +
                                                                "	saldo = saldo - ?\n" +
                                                                "where\n" +
                                                                "	id > (SELECT id FROM saldos_cofres sc1 where id_movimento_cofre = ? limit 1) and"
                                                                + "     id_cofre = ? and"
                                                                + "     data_saldo >= (SELECT data_saldo FROM saldos_cofres sc1 where id_movimento_cofre = ? limit 1) and"
                                                                + "     id_entidade = ? ", false);
                psUpSaldos.setBigDecimal(1,valorMovimentado.multiply(new BigDecimal(numeroVolumes)));
                psUpSaldos.setInt(2, id);
                psUpSaldos.setInt(3, Integer.valueOf(idCofre));
                psUpSaldos.setInt(4, id);
                psUpSaldos.setInt(5, Parametros.idEntidade);
                psUpSaldos.execute();
            }
            PreparedStatement ps = Parametros.getConexao(request).getPst("delete from movimentos_cofres where id = ? and id_entidade = ?", Boolean.FALSE);     
            ps.setInt(1, id);
            ps.setInt(2, Parametros.idEntidade);
            ps.execute();
        }catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    private void insere(){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO public.movimentos_cofres(\n" +
"             id_cofre, id_movimento_caixa, data_hora_mov, \n" +
"            valor_movimentado, tipo_movimento_cofre, observacoes, id_movimento_conta, id_abertura_terminal, \n" +
"            id_entidade)\n" +
"    VALUES ( ?, ?, ?, \n" +
"            ?, ?, ?, ?, ?, \n" +
"            ?);");
            ps.setInt(1, Integer.valueOf(idCofre));
            ps = MyPreparedStatement.set(ps, 2, idMovimentoCaixa);
            ps.setTimestamp(3, dataHoraMov);
            ps.setBigDecimal(4, valorMovimentado.multiply(new BigDecimal(numeroVolumes)));
            ps.setInt(5, Integer.valueOf(tipoOperacao));
            ps = MyPreparedStatement.set(ps, 6, observacoes);
            ps = MyPreparedStatement.set(ps, 7, idMovimentoConta);
            ps = MyPreparedStatement.set(ps, 8, idAberturaTerminal);
            ps.setInt(9, Parametros.idEntidade);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
            }
            
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    private void altera(){
        String idL;
        if(this.id==null) {
            idL = request.getParameter("id");
        }else{
            idL = this.id.toString();
        }
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("UPDATE public.movimentos_cofres\n" +
                        "   SET id_cofre=?, id_movimento_caixa=?, data_hora_mov=?, \n" +
                        "       valor_movimentado=?, tipo_movimento_cofre=?, observacoes=?,id_movimento_conta=? \n" +
                        " where id = ? and id_entidade = ? ", false);
            ps.setInt(1, Integer.valueOf(idCofre));
            ps = MyPreparedStatement.set(ps, 2, idMovimentoCaixa);
            ps.setTimestamp(3, dataHoraMov);
            ps.setBigDecimal(4, valorMovimentado.multiply(new BigDecimal(numeroVolumes)));
            ps.setInt(5, Integer.valueOf(tipoOperacao));
            ps = MyPreparedStatement.set(ps, 6, observacoes);
            ps = MyPreparedStatement.set(ps, 7, idMovimentoConta);
            
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
        templateConteudo = ve.getTemplate( "templates/Modern/processos/movimentos_cofres.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());       
        setOpcoesFiltro("movimentos_cofres");
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"movimentos_cofres").toString());
        return contextPrinc;
    }

    public String getIdTerminalMet() {
        return idCofre;
    }

    public Timestamp getDataHoraMovMC() {
        return dataHoraMov;
    }
    
}
