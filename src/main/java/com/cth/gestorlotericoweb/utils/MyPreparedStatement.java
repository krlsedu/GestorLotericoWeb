/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.utils;

import com.cth.gestorlotericoweb.parametros.Parametros;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.*;

/**
 *
 * @author CarlosEduardo
 */
public class MyPreparedStatement {
    PreparedStatement pst;
    Integer idx;
    
    public MyPreparedStatement(String sql, HttpServletRequest request) throws SQLException {
        this.pst = Parametros.getConexao(request).getPst(sql);
        idx = 0;
    }
    public MyPreparedStatement(String sql, HttpServletRequest request, Boolean b) throws SQLException {
        this.pst = Parametros.getConexao(request).getPst(sql,b);
        idx = 0;
    }
    
    public PreparedStatement getPst() {
        return pst;
    }
    
    public boolean execute() throws SQLException {
        return getPst().execute();
    }
    
    public ResultSet executeQuery() throws SQLException {
        return getPst().executeQuery();
    }
    
    public void set(Timestamp valor) throws SQLException {
        idx++;
        pst = set(pst,idx,valor);
    }
    
    public void set(Date valor) throws SQLException {
        idx++;
        pst = set(pst,idx,valor);
    }
    
    public void set(Integer valor) throws SQLException {
        idx++;
        pst = set(pst,idx,valor);
    }
    
    public void set(String valor) throws SQLException {
        idx++;
        pst = set(pst,idx,valor);
    }
    
    public void set(BigDecimal valor) throws SQLException {
        idx++;
        pst = set(pst,idx,valor);
    }
    
    public static PreparedStatement set(PreparedStatement ps, Integer index, Timestamp valor) throws SQLException{
        if(valor==null){
            ps.setNull(index, Types.TIMESTAMP);
        }else{
            ps.setTimestamp(index, valor);
        }
        return ps;
    }
    
    public static PreparedStatement set(PreparedStatement ps, Integer index, Date valor) throws SQLException{
        if(valor==null){
            ps.setNull(index, Types.DATE);
        }else{
            ps.setDate(index, valor);
        }
        return ps;
    }
    
    public static PreparedStatement set(PreparedStatement ps, Integer index, Integer valor) throws SQLException{
        if(valor==null){
            ps.setNull(index, Types.BIGINT);
        }else{
            ps.setInt(index, valor);
        }
        return ps;
    }
    
    public static PreparedStatement set(PreparedStatement ps,Integer index,String valor) throws SQLException{
        if(valor==null){
            ps.setNull(index, Types.LONGVARCHAR);
        }else{
            ps.setString(index, valor);
        }
        return ps;
    }
    
    public static PreparedStatement set(PreparedStatement ps,Integer index,BigDecimal valor) throws SQLException{
        if(valor==null){
            ps.setNull(index, Types.NUMERIC);
        }else{
            ps.setBigDecimal(index, valor);
        }
        return ps;
    }
}
