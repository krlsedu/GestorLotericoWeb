/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.processos;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.parametros.Parametros;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author CarlosEduardo
 */
public class OperacoesDiariasLinhas extends Processos{
    Map<Integer,Map<Integer,Integer>> mapOpers;
    Integer idOperDiaria;
    String linha;
    
    public OperacoesDiariasLinhas(HttpServletRequest request) {
        super(request);
        this.linha = request.getParameter("num_linhas");
        id = 0;
    } 
    
    public OperacoesDiariasLinhas(HttpServletRequest request,OperacoesDiarias operacoesDiarias) {
        super(request);
        setOperacoesDiariasLinhas(operacoesDiarias);
    } 
    
    private void setOperacoesDiariasLinhas(OperacoesDiarias operacoesDiarias){
        try{
            mapOpers = operacoesDiarias.mapOpers;
            idOperDiaria = operacoesDiarias.id;
        }catch(Exception ex){
            
        }
    }
    
    public void insere(){
        mapOpers.keySet().stream().forEach((i) -> {
            mapOpers.get(i).keySet().stream().forEach((idTmp) -> {
                insere( i, mapOpers.get(i).get(idTmp));
            });
        });
    }
    
    private void insere(Integer idOper,Integer qtd){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO public.operacoes_diarias_det(\n" +
                                                                    "             id_operacoes_diarias, id_operacao, quantidade)\n" +
                                                                    "    VALUES ( ?, ?, ?);");
            ps.setInt(1, idOperDiaria);                
            ps.setInt(2, idOper);
            ps.setInt(3, qtd);
            ps.execute();
            
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void altera(){
        String idL = request.getParameter("id");
        try {
            PreparedStatement psKeys = Parametros.getConexao().getPst("select id from public.operacoes_diarias_det where id_operacoes_diarias = ?",false);
            psKeys.setInt(1, idOperDiaria);
            ResultSet rs = psKeys.executeQuery();
            List<Integer> listKeys = new ArrayList<>();
            while (rs.next()) {                
                listKeys.add(rs.getInt(1));
            }
            List<Integer> listKeysGravar = new ArrayList<>();
            mapOpers.keySet().stream().forEach((opers) -> {
                listKeysGravar.addAll(mapOpers.get(opers).keySet());
            });
            
            mapOpers.keySet().stream().forEach((oper) -> {
                mapOpers.get(oper).keySet().stream().forEach((idGr) -> {
                    if(listKeys.contains(idGr)&&!idGr.equals(0)){
                        update(idGr,mapOpers.get(oper).get(idGr));
                    }else{
                        insere(oper, mapOpers.get(oper).get(idGr));
                    }
                });
            });
            
            //Remove registros não existentes após a alteração 
            listKeys.removeAll(listKeysGravar);
            listKeys.stream().forEach((idRm) -> {
                deleta(idRm);
            });
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    private void update(Integer idGr,Integer qtd){
        try{           
            
            PreparedStatement ps = Parametros.getConexao(request).getPst("UPDATE public.operacoes_diarias_det \n" +
                                                        "set quantidade=? \n" +
                                                    " where id = ? ", false);
            ps.setInt(1, qtd);
            ps.setInt(2, idGr);
            
            ps.execute();
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    private void deleta(Integer idRm){
        try{
            PreparedStatement ps = Parametros.getConexao(request).getPst("delete from operacoes_diarias_det where id = ? ", Boolean.FALSE);     
            ps.setInt(1, idRm);
            ps.execute();
        }catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public VelocityContext getHtml(VelocityContext contextPrinc,VelocityEngine ve,String idS){        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/processos/operacoes_diarias_linha.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        contextConteudo.put("num",linha);       
        contextConteudo.put("id_reg",id);  
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        return contextPrinc;
    }
}
