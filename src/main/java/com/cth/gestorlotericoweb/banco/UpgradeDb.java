/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.banco;

import com.cth.gestorlotericoweb.parametros.Parametros;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

/**
 *
 * @author carloseduardo
 */
public class UpgradeDb {
    Conexao con;
    String buildApp;
    String output;
    final HttpServletRequest request;

    public UpgradeDb(HttpServletRequest request) {
        Parametros.initDb(request);
        con =  Parametros.getConexao();
        this.request = request;
        InputStream input = request.getServletContext().getResourceAsStream("/WEB-INF/buildNumber.properties");
        Properties propSalvar = new Properties();
        try {
            propSalvar.load(input);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        try {
            String ret = propSalvar.getProperty("buildNumber");
            input.close();
            if (ret == null)
                ret = "";
            buildApp = ret;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        upgrade();
    }
    
    private void  upgrade() {        
        Liquibase liquibase = null;
        java.sql.Connection c = null;
        String build;
        try {
            ResultSet rs = con.getRs("select build from db_version order by id desc limit 1");
            if(rs.next()){
                build = rs.getString(1);
            }else{
                build = "0";
            }
        } catch (SQLException ex) {
            build = "0";
        }
        if(buildApp!=null){
            Integer bldApp = Integer.valueOf(buildApp.split("-")[0]);
            Integer bld = Integer.valueOf(build);
            if(bldApp>bld){     
                try {
                    c  = con.getCon();
                    DatabaseConnection dbc = new JdbcConnection(c);
                    Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(dbc);
                    String folders = request.getServletContext().getRealPath("WEB-INF/db_versions/");
                    File fileDir = new File(folders);
                    if(fileDir.isDirectory()){
                            List listFile = Arrays.asList(fileDir.list());
                            
                            Collections.sort(listFile);
                            for(Object s:listFile){                       
                                try{
                                    liquibase = new Liquibase(folders+"/"+s, new FileSystemResourceAccessor(), database);                                
                                    liquibase.update(new Contexts(), new LabelExpression());
                                }catch (LiquibaseException ex) {
                                    output +=folders+"/"+s+" - "+ ex.getMessage();
                                } 
                            }
                            

                    }
                } catch (DatabaseException ex) {
                    output += ex.getMessage();
                }                    
            }
        }
    }

    public String getOutput() {
        return output;
    }
    
}