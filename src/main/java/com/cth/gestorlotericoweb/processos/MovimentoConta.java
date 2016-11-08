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
public class MovimentoConta extends Processos{
    Integer idMovimentoConta;
    String idConta;
    String dataHoraMov;
    String formaDeposito;
    String tipoMovimento;
    String valorMovimentado;
    String observacoes;
    
    
    public MovimentoConta(HttpServletRequest request) {
        super(request);
        setMovimentosCaixas();
    }
    private void setMovimentosCaixas() {
        try{
            this.idMovimentoConta = Integer.valueOf(request.getParameter("id_movimento_conta"));
        }catch(Exception e){
            this.idMovimentoConta = null;
        }
        this.idConta = request.getParameter("id_conta");
        this.dataHoraMov = request.getParameter("data_hora_mov");
        this.formaDeposito = request.getParameter("forma_deposito");
        this.valorMovimentado = request.getParameter("valor_movimentado");
        this.tipoMovimento = request.getParameter("tipo_movimento");
        this.observacoes = request.getParameter("observacoes");
    }
    
    public MovimentoConta(Integer id,HttpServletRequest request) {
        super(request, id);
        getDados();
    }
    
    private void getDados(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("SELECT   id_conta, data_hora_mov,  valor_movimentado, \n" +
                                                        "       tipo_movimento_conta, forma_deposito, observacoes \n" +
                        "  FROM public.movimentos_contas where id = ? and id_entidade = ? ",false);
            ps.setInt(1, id);
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                idConta = rs.getString(1);
                dataHoraMov = rs.getString(2);
                valorMovimentado = Parser.toBigDecimalSt(rs.getString(3));
                tipoMovimento = rs.getString(4);
                formaDeposito = rs.getString(5);
                observacoes = rs.getString(6);
            }else{
                tipoMovimento = "";
                formaDeposito = "";
                dataHoraMov = "";
                valorMovimentado = "";
                observacoes = "";
                idConta = "";
                idMovimentoConta = null;
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void insere(){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO public.movimentos_contas(\n" +
"             id_conta, data_hora_mov, valor_movimentado, \n" +
"            tipo_movimento_cofre, forma_deposito, observacoes, \n" +
"            id_entidade)\n" +
"    VALUES ( ?, ?, ?, \n" +
"            ?, ?, ?, \n" +
"            ?);");
            ps.setInt(1, Integer.valueOf(idConta));
            ps.setTimestamp(2, Parser.toDbTimeStamp(dataHoraMov));
            ps.setBigDecimal(3, Parser.toBigDecimalFromHtml(valorMovimentado));
            ps.setInt(4, Integer.valueOf(tipoMovimento));
            ps.setString(5,formaDeposito);
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
            PreparedStatement ps = Parametros.getConexao(request).getPst("UPDATE public.movimentos_contas\n" +
                        "   SET  id_conta=?, data_hora_mov=?, valor_movimentado=?, \n" +
                        "       tipo_movimento_cofre=?, forma_deposito=?, observacoes=? \n" +
                        " where id = ? and id_entidade = ? ", false);
            ps.setInt(1, Integer.valueOf(idConta));
            ps.setTimestamp(2, Parser.toDbTimeStamp(dataHoraMov));
            ps.setBigDecimal(3, Parser.toBigDecimalFromHtml(valorMovimentado));
            ps.setInt(4, Integer.valueOf(tipoMovimento));
            ps.setString(5,formaDeposito);
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
        templateConteudo = ve.getTemplate( "templates/Modern/processos/movimentos_contas.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());       
        setOpcoesFiltro("movimentos_contas");
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"movimentos_contas").toString());
        return contextPrinc;
    }
}
