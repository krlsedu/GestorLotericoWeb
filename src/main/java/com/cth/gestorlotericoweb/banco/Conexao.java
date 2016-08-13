/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.banco;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import static java.sql.DriverManager.getConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author CarlosEduardo
 */
public class Conexao {
    public static String user;
    public static String senha;
    public static String ip;
    public static String porta;
    public static String banco;
    public Connection Con;
    public Statement st;
    public static String sql;
    public ResultSet results;
    final HttpServletRequest request;
    
    public Conexao(HttpServletRequest request) {      
        try{
            this.request = request;
            Class.forName("org.postgresql.Driver");
            String usuarioPg = lerPropriedade("user");
            String senhaPg = lerPropriedade("senha");
            String ipPg = lerPropriedade("ip");
            String portaPg = lerPropriedade("porta");
            String bancoPg = lerPropriedade("banco");
            String url ="jdbc:postgresql://"+ipPg+":"+portaPg+"/"+bancoPg;
            Con = getConnection(url,usuarioPg, senhaPg);
            st = Con.createStatement();
        }catch(ClassNotFoundException|SQLException ex){
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
    
    public Statement getSt() throws SQLException{
        Statement st1 = Con.createStatement();
	    return st1;
    }
    
    public ResultSet getRs(String sql)throws SQLException{
        Statement st1 = Con.createStatement();
	return st1.executeQuery(sql);
    }
    
    public PreparedStatement getPst(String sql) throws SQLException{
        PreparedStatement st1;
        st1 = Con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
	return st1;
    }
    
    public PreparedStatement getPst(String sql,Boolean b) throws SQLException{
        PreparedStatement st1;
        st1 = Con.prepareStatement(sql);
	return st1;
    }
    
    public void close() throws SQLException{
	Con.close();
    }
    
    public DatabaseMetaData getMetaData() throws SQLException{
        return Con.getMetaData();
    }
    public void commit() throws SQLException{
	Con.commit();
    }
    

    private String lerPropriedade(String chave) {
        InputStream input = request.getServletContext().getResourceAsStream("/WEB-INF/db.properties");
        Properties propSalvar = new Properties();
        try {
            propSalvar.load(input);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        try {
            String ret = propSalvar.getProperty(chave);
            input.close();
            if (ret == null)
                ret = "";
            return ret;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
}
