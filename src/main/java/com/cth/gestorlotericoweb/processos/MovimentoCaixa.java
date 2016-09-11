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
public class MovimentoCaixa extends Processos{
    String idTerminal;
    String idFuncionario;
    String idCofre;
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
        this.idCofre = request.getParameter("id_cofre");
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
            "       valor_movimentado, observacoes, id_cofre \n" +
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
    
    public void insere(){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO public.movimentos_caixas(\n" +
"            tipo_operacao_caixa, id_terminal, id_funcionario, data_hora_mov, \n" +
"            valor_movimentado, observacoes, id_entidade,id_cofre)\n" +
"    VALUES (?, ?, ?, ?, \n" +
"            ?, ?, ?, ?);");
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
            ps = Seter.set(ps,8,Integer.valueOf(idCofre));
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
                if(this.idCofre!=null){
                    try{
                        MovimentoCofre movimentoCofre = new MovimentoCofre(request, this);         
                        movimentoCofre.gravaAutoMovCaixa();
                    }catch(Exception e){

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
            ps.setInt(8, id);
            ps.setInt(9, Parametros.idEntidade);
            
            ps.execute();
            if(this.idCofre!=null){
                try{
                    MovimentoCofre movimentoCofre = new MovimentoCofre(request, this);         
                    movimentoCofre.gravaAutoMovCaixa();
                }catch(Exception e){

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
}
