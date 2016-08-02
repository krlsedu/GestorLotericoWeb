/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;

import com.cth.gestorlotericoweb.banco.Conexao;
import com.cth.gestorlotericoweb.parametros.Parametros;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author CarlosEduardo
 */
public class Auth {
    
    HttpServletRequest request;
    public String output;

    public Auth(HttpServletRequest request) {
        this.request = request;
    }
    public void auth(){
        try {
            Conexao c = Parametros.getConexao();
            PreparedStatement ps = c.getPst("SELECT count(*)\n" +
                    "FROM \n" +
                    "	public.users\n" +
                    "where \n" +
                    "	usuario = ? and\n" +
                    "	senha = ?",false);       
            ps.setString(1, request.getParameter("user"));
            ps.setString(2, request.getParameter("password"));
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                if(rs.getInt(1)>0){
                    output = "True";
                }else{
                    output = "False";                    
                }
            }else{
                output = "False";                      
            }
        } catch (SQLException ex) {
            output= ex.getMessage();
            //throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
    }
}
