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
public class MovimentoCofre extends Processos{
    Integer idMovimentoCaixa;
    Integer idMovimentoConta;
    String idCofre;
    String dataHoraMov;
    String valorMovimentado;
    String tipoOperacao;
    String observacoes;
    String numeroVolumes;
    
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
        this.dataHoraMov = request.getParameter("data_hora_mov");
        this.valorMovimentado = request.getParameter("valor_movimentado");
        this.tipoOperacao = request.getParameter("tipo_movimento_cofre");
        this.observacoes = request.getParameter("observacoes");
    }
    public MovimentoCofre(Integer id,HttpServletRequest request) {
        super(request, id);
        getDados();
    }
    
    
    public MovimentoCofre(HttpServletRequest request,MovimentoCaixa movimentoCaixa) {
        super(request);
        this.id = getIdMovimentoCofre(movimentoCaixa);
        this.idCofre = movimentoCaixa.idCofre;
        this.idMovimentoCaixa = movimentoCaixa.id;
        this.dataHoraMov = movimentoCaixa.dataHoraMov;
        this.valorMovimentado = movimentoCaixa.valorMovimentado;
        this.tipoOperacao = movimentoCaixa.tipoOperacao.trim().equals("1")?"2":"1";     
        this.numeroVolumes = "1";
    }
    
    public MovimentoCofre(HttpServletRequest request,MovimentoConta movimentoConta) {
        super(request);
        this.id = getIdMovimentoCofre(movimentoConta);
        this.idCofre = movimentoConta.idCofre;
        this.idMovimentoConta = movimentoConta.id;
        this.dataHoraMov = movimentoConta.dataHoraMov;
        this.valorMovimentado = movimentoConta.valorMovimentado;
        this.valorMovimentado = movimentoConta.numeroVolumes;
        this.tipoOperacao = movimentoConta.tipoMovimentoConta.trim().equals("1")?"2":"1";        
    }
    
    public void gravaAutoMovCaixa(){
        if(this.id == 0){
            insere();
        }else{
            altera();
        }
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
                dataHoraMov = rs.getString(3);
                valorMovimentado = Parser.toBigDecimalSt(rs.getString(4));
                tipoOperacao = rs.getString(5);
                observacoes = rs.getString(6);
                idMovimentoConta = rs.getInt(7);
            }else{
                tipoOperacao = "";
                dataHoraMov = "";
                valorMovimentado = "";
                observacoes = "";
                idCofre = "";
                idMovimentoCaixa = null;
                idMovimentoConta = null;
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void insere(){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO public.movimentos_cofres(\n" +
"             id_cofre, id_movimento_caixa, data_hora_mov, \n" +
"            valor_movimentado, tipo_movimento_cofre, observacoes, id_movimento_conta, \n" +
"            id_entidade)\n" +
"    VALUES ( ?, ?, ?, \n" +
"            ?, ?, ?,?,  \n" +
"            ?);");
            ps.setInt(1, Integer.valueOf(idCofre));
            ps = Seter.set(ps, 2, idMovimentoCaixa);
            ps.setTimestamp(3, Parser.toDbTimeStamp(dataHoraMov));
            ps.setBigDecimal(4, Parser.toBigDecimalFromHtml(valorMovimentado).multiply(Parser.toBigDecimalFromHtml(numeroVolumes)));
            ps.setInt(5, Integer.valueOf(tipoOperacao));
            ps = Seter.set(ps, 6, observacoes);
            ps = Seter.set(ps, 7, idMovimentoConta);
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
    
    public void altera(){
        String idL = request.getParameter("id");
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("UPDATE public.movimentos_cofres\n" +
                        "   SET id_cofre=?, id_movimento_caixa=?, data_hora_mov=?, \n" +
                        "       valor_movimentado=?, tipo_movimento_cofre=?, observacoes=?,id_movimento_conta=? \n" +
                        " where id = ? and id_entidade = ? ", false);
            ps.setInt(1, Integer.valueOf(idCofre));
            ps = Seter.set(ps, 2, idMovimentoCaixa);
            ps.setTimestamp(3, Parser.toDbTimeStamp(dataHoraMov));
            ps.setBigDecimal(4,  Parser.toBigDecimalFromHtml(valorMovimentado).multiply(Parser.toBigDecimalFromHtml(numeroVolumes)));
            ps.setInt(5, Integer.valueOf(tipoOperacao));
            ps = Seter.set(ps, 6, observacoes);
            ps = Seter.set(ps, 7, idMovimentoConta);
            
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
}
