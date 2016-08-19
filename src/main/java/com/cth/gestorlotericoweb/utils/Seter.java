/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author CarlosEduardo
 */
public class Seter {
    public static PreparedStatement set(PreparedStatement ps,Integer index,Integer valor) throws SQLException{
        if(valor==null){
            ps.setNull(index, java.sql.Types.INTEGER);
        }else{
            ps.setInt(index, valor);
        }
        return ps;
    }
    
    public static PreparedStatement set(PreparedStatement ps,Integer index,String valor) throws SQLException{
        if(valor==null){
            ps.setNull(index, java.sql.Types.LONGNVARCHAR);
        }else{
            ps.setString(index, valor);
        }
        return ps;
    }
}
