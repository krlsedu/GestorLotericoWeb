/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.banco;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static java.sql.DriverManager.getConnection;

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
    public static String userConv;
    public static String senhaConv;
    public static String ipConv;
    public static String portaConv;
    public static String bancoConv;
    public Connection Con;
    public Statement st;
    public static String sql;
    public ResultSet results;
    
    public Conexao() throws ClassNotFoundException, SQLException{
        Class.forName("org.postgresql.Driver");
        String usuarioPg = "postgres";
        String senhaPg = "Thaisa-2707";
        String ipPg = "127.0.0.1";
        String portaPg = "5432";
        String bancoPg = "glw";
        String url ="jdbc:postgresql://"+ipPg+":"+portaPg+"/"+bancoPg;
        Con = getConnection(url,usuarioPg, senhaPg);
        st = Con.createStatement();
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
    
}
