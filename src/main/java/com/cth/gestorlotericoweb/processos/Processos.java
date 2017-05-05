/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.processos;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.dados.ColunasTabelas;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.utils.Parser;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CarlosEduardo
 */
public class Processos {
    List<String> lOpts = new ArrayList<>();
    final HttpServletRequest request;
    Integer id;

    public Processos(HttpServletRequest request) {
        this.request = request;
    }  

    public Processos(HttpServletRequest request, Integer id) {
        this.request = request;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
    
    
    
    public StringWriter getSWPopup(VelocityEngine ve,String tipo){
        Template templatePopup;
        VelocityContext contextPopup;
        StringWriter writerPopup;
        templatePopup = ve.getTemplate( "templates/Modern/modal.html" , "UTF-8");
        contextPopup = new VelocityContext();
        writerPopup = new StringWriter();
        contextPopup.put("tabela", tipo);
        contextPopup.put("opt", StringUtils.join(lOpts, '\n'));
        templatePopup.merge(contextPopup, writerPopup);
        return writerPopup;
    }
    
    public StringWriter getSWBotoesPercorrer(VelocityEngine ve){
        Template templateBtnPerc;
        VelocityContext contextBtnPerc;
        StringWriter writerBtnPerc;
        templateBtnPerc = ve.getTemplate( "templates/Modern/botoes_percorrer.html" , "UTF-8");
        contextBtnPerc = new VelocityContext();
        writerBtnPerc = new StringWriter();
        templateBtnPerc.merge(contextBtnPerc, writerBtnPerc);
        return writerBtnPerc;
    }
    
    public List<String> setOpcoesFiltro(String tipo){
        ColunasTabelas colunasTabelas = new ColunasTabelas(request);
        lOpts = colunasTabelas.getlOpts(tipo);
        return lOpts;
    }
    
    public String getIdTerminal(){
        String sql = "SELECT id_terminal from abertura_terminais where id_funcionario = ? and id_entidade = ? ORDER BY data_abertura DESC LIMIT 1";
        try{
            PreparedStatement ps = Parametros.getConexao().getPst(sql,false);
            ps.setInt(1,Integer.valueOf(request.getParameter("id_funcionario")));
            ps.setInt(2,Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        }catch (SQLException e){
            new LogError(e.getMessage(), e,request);
        }
        return "0";
    }
    
    public String getIdFuncionario(){
        String sql = "SELECT id_funcionario from abertura_terminais where id_terminal = ? and id_entidade = ? ORDER BY data_abertura DESC LIMIT 1";
        try{
            PreparedStatement ps = Parametros.getConexao().getPst(sql,false);
            ps.setInt(1,Integer.valueOf(request.getParameter("id_terminal")));
            ps.setInt(2,Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        }catch (SQLException e){
            new LogError(e.getMessage(), e,request);
        }
        return "0";
    }
    
    public String getDataFechar(){
        String sql = "SELECT at.data_abertura FROM\n" +
                "	abertura_terminais at\n" +
                "where\n" +
                "	not EXISTS (SELECT 1 FROM fechamento_terminais ft where ft.id_terminal = at.id_terminal and ft.id_funcionario = at.id_funcionario and at.data_abertura = ft.data_encerramento and at.id_entidade = ft.id_entidade) and \n" +
                "	at.id_terminal = ? and\n" +
                "	at.id_funcionario = ? and " +
                "           at.id_entidade = ?\n" +
                "order by\n" +
                "	at.data_abertura desc\n" +
                "limit 1";
        try{
            PreparedStatement ps = Parametros.getConexao().getPst(sql, false);
            ps.setInt(1, Parser.toInteger(request.getParameter("id_terminal")));
            ps.setInt(2, Parser.toInteger(request.getParameter("id_funcionario")));
            ps.setInt(3, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString(1);
            }
        }catch(SQLException e){
            new LogError(e.getMessage()+" - "+sql, e, request);
        }
        return null;
    }
    
    public String getDataHoraMov(){
        if(request.getParameter("data_hora_mov")==null) {
            return getDataFechar()+"T00:00";
        }else{
            if(request.getParameter("data_hora_mov").trim().equals("")){
                return getDataFechar()+"T00:00";
            }else {
                return request.getParameter("data_hora_mov");
            }
        }
    }
    
    public String getIdCofre(){
        String sql = "SELECT count(*) from cofres WHERE id_entidade = ?";
        try {
            PreparedStatement ps = Parametros.getConexao().getPst(sql,false);
            ps.setInt(1,Parametros.idEntidade);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()){
                if (resultSet.getInt(1)==1){
                    sql = "SELECT id from cofres WHERE id_entidade = ?";
                    ps = Parametros.getConexao().getPst(sql,false);
                    ps.setInt(1,Parametros.idEntidade);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()){
                        return rs.getString(1);
                    }
                }else{
                    return getIdCofreUsado();
                }
            }
        } catch (SQLException e) {
            new LogError(e.getMessage(),e,request);
        }
        return getIdCofreUsado();
    }
    
    private String getIdCofreUsado(){
        String sql = "SELECT id_cofre FROM movimentos_cofres WHERE id_entidade = ? ORDER BY data_hora_mov DESC LIMIT 1";
        try {
            PreparedStatement ps = Parametros.getConexao().getPst(sql,false);
            ps.setInt(1,Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            new LogError(e.getMessage(),e,request);
        }
        return "";
    }

}
