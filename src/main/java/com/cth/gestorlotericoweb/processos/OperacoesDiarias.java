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
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author CarlosEduardo
 */
public class OperacoesDiarias extends Processos{
    Integer numLinhas;
    public Map<Integer,Map<Integer,Integer>> mapOpers;
    Integer idTerminal;
    Integer idFuncionario;
    String dataOperacoes;
    String observacoes;

    public OperacoesDiarias(HttpServletRequest request) {
        super(request);
        this.id = 0;
    }
    
    public void setOperacoesDiarias(){
        try{
            this.numLinhas = Integer.valueOf(request.getParameter("num_linhas"));
            idTerminal = Integer.valueOf(request.getParameter("id_terminal"));
            idFuncionario = Integer.valueOf(request.getParameter("id_funcionario"));
            dataOperacoes = request.getParameter("data_operacoes");
            this.observacoes = request.getParameter("observacoes");
            mapOpers = new HashMap<>();
            for(int i=1;i<=numLinhas;i++){
                Map<Integer,Integer> mv = new HashMap<>();
                mv.put(Integer.valueOf(request.getParameter("id_"+i)),Integer.valueOf(request.getParameter("quantidade_"+i)));
                mapOpers.put(Integer.valueOf(request.getParameter("id_operacao_"+i)),mv);
            }
        }catch(Exception ex){
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void grava(){
        if("0".equals(request.getParameter("id"))){
            insere();
        }else{
            altera();
        }
    }
    
    private void insere(){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO public.operacoes_diarias(\n" +
                                                                "             id_terminal, id_funcionario, data_operacoes, \n" +
                                                                "             observacoes, id_entidade)\n" +
                                                                "    VALUES ( ?, ?, ?, \n" +
                                                                "             ?, ?);");
            ps.setInt(1, idTerminal);
            ps.setInt(2, idFuncionario);
            ps.setDate(3, Parser.toDbDate(dataOperacoes));
            ps = Seter.set(ps, 4, observacoes);
            ps.setInt(5, Parametros.idEntidade);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
                OperacoesDiariasLinhas odl = new OperacoesDiariasLinhas(request, this);
                odl.insere();
            }
            
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    private void altera(){
        String idL = request.getParameter("id");
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("UPDATE public.operacoes_diarias\n" +
                                                    "   SET id_terminal=?, id_funcionario=?, data_operacoes=?, \n" +
                                                    "       observacoes=? \n" +
                                                    " where id = ? and id_entidade = ? ", false);
            ps.setInt(1, idTerminal);
            ps.setInt(2, idFuncionario);
            ps.setDate(3, Parser.toDbDate(dataOperacoes));
            ps = Seter.set(ps, 4, observacoes);
            
            id = Integer.valueOf(idL);
            ps.setInt(5, id);
            ps.setInt(6, Parametros.idEntidade);
            
            ps.execute();
            OperacoesDiariasLinhas odl = new OperacoesDiariasLinhas(request, this);
            odl.altera();
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void deleta(){
        try{
            PreparedStatement ps = Parametros.getConexao(request).getPst("delete from operacoes_diarias where id = ? and id_entidade = ?", Boolean.FALSE);     
            ps.setInt(1, Integer.valueOf(request.getParameter("id")));
            ps.setInt(2, Parametros.idEntidade);
            ps.execute();
        }catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public VelocityContext getHtml(VelocityContext contextPrinc,VelocityEngine ve,String idS){        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/processos/operacoes_diarias.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString()); 
        if(!id.equals(0)){
            contextConteudo.put("linhas_det","");
        }else{
            contextConteudo.put("linhas_det","");
        }
        setOpcoesFiltro("operacoes_diarias");
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"operacoes_diarias").toString());
        return contextPrinc;
    }
}
