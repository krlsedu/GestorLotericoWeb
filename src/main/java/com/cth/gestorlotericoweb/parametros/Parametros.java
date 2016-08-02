/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.parametros;

import com.cth.gestorlotericoweb.banco.Conexao;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;

/**
 *
 * @author CarlosEduardo
 */
public class Parametros {
    public static Conexao conexao;
    public static void initDb(){
        try {
            conexao = new Conexao();
        } catch (ClassNotFoundException | SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
    }

    public static Conexao getConexao() {
        return conexao;
    }
    
}
