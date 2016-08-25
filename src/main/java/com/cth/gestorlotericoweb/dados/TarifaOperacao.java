/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.dados;

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
public class TarifaOperacao extends Cadastros{
    String idOperacao;
    String dataBase;
    String valorTarifa;
    String observacoes;
    
    public TarifaOperacao(HttpServletRequest request) {
        super(request);
        setTarifaOperacao();
    }
    private void setTarifaOperacao() {
        this.idOperacao = request.getParameter("id_operacao");
        this.dataBase = request.getParameter("data_base");
        this.valorTarifa = request.getParameter("valor_tarifa");
        this.observacoes = request.getParameter("observacoes");
    }
    
    public TarifaOperacao(Integer id,HttpServletRequest request) {
        super(id, request);
        getDados();
    }
    private void getDados(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("SELECT id_operacao, data_base, valor_tarifa, observacoes \n" +
                                                                "  FROM public.tarifas_operacoes where id = ? and id_entidade = ? ",false);
            ps.setInt(1, id);
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                idOperacao = rs.getString(1);
                dataBase = rs.getString(2);
                valorTarifa = rs.getString(3);
                observacoes = rs.getString(4);
            }else{
                idOperacao = "";
                dataBase = "";
                valorTarifa = "";
                observacoes = "";
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void insere(){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO public.tarifas_operacoes(\n" +
                    "             id_operacao, data_base, valor_tarifa, observacoes, \n" +
                    "             id_entidade)\n" +
                    "    VALUES ( ?, ?, ?, ?, \n" +
                    "             ?);");
            ps.setInt(1, Integer.valueOf(idOperacao));
            ps.setDate(2, Parser.toDbDate(dataBase));
            ps.setDouble(3, Parser.toDoubleFromHtml(valorTarifa));
            ps = Seter.set(ps, 4, observacoes);
            ps.setInt(5, Parametros.idEntidade);
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
            }
            
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void altera(String idL){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("UPDATE public.tarifas_operacoes\n" +
"   SET id_operacao=?, data_base=?,  valor_tarifa=?, \n" +
"       observacoes=? "
                    + " where id = ? and id_entidade = ? ", false);
            ps.setInt(1, Integer.valueOf(idOperacao));
            ps.setDate(2, Parser.toDbDate(dataBase));
            ps.setDouble(3, Parser.toDoubleFromHtml(valorTarifa));
            ps = Seter.set(ps, 4, observacoes);
            
            id = Integer.valueOf(idL);
            ps.setInt(5, id);
            ps.setInt(6, Parametros.idEntidade);
            ps.execute();
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public VelocityContext getHtml(VelocityContext contextPrinc,VelocityEngine ve,String idS){        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/cadastros/tarifas_operacoes.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());       
        setOpcoesFiltro("tarifas_operacoes");
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"tarifas_operacoes").toString());
        return contextPrinc;
    }
    
}
