/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;

import com.cth.gestorlotericoweb.dados.ColunasTabelas;
import com.cth.gestorlotericoweb.dados.Loterica;
import com.cth.gestorlotericoweb.parametros.Parametros;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;

/**
 *
 * @author CarlosEduardo
 */
public class Grava {
    public String output;
    HttpServletRequest request;
    public Integer id;

    public Grava(HttpServletRequest request) {
        this.request = request;
        if(request.getParameter("tipo")!=null){
            deleta();
        }else{
            grava();
        }
    }
    
    private void deleta(){
        try {                    
            ColunasTabelas colunasTabelas = new ColunasTabelas(request);
            String tabela = colunasTabelas.getTabela(request.getParameter("tabela"));
            if(tabela !=null){
                PreparedStatement ps = Parametros.getConexao(request).getPst("delete from "+tabela+" where id = ?", Boolean.FALSE);     
                ps.setInt(1, Integer.valueOf(request.getParameter("id")));
                ps.execute();
            }            
        } catch (SQLException ex) {
            throw new LogError(ex.getMessage(), ex,request);
        }
    }
    
    private void grava(){
        String input = request.getParameter("it");
        switch(input){
            case "lotericas":     
                Loterica loterica = new Loterica(request.getParameter("codigo_caixa"), request.getParameter("nome"),request);
                if("0".equals(request.getParameter("id"))){
                    loterica.insere();
                }else{
                    loterica.altera(request.getParameter("id"));
                }
                id = loterica.getId();
                break;
            case "terminais":                            
                break;
            default:
                break;
        }
    }
}
