/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.utils;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 *
 * @author CarlosEduardo
 */
public class Seter {
    public static PreparedStatement set(PreparedStatement ps,Integer index,Integer valor) throws SQLException{
        if(valor==null){
            ps.setNull(index, java.sql.Types.BIGINT);
        }else{
            ps.setInt(index, valor);
        }
        return ps;
    }
    
    public static PreparedStatement set(PreparedStatement ps,Integer index,String valor) throws SQLException{
        if(valor==null){
            ps.setNull(index, java.sql.Types.LONGVARCHAR);
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
