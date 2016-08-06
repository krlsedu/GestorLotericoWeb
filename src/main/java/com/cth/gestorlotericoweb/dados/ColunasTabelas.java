/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.dados;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.parametros.Parametros;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author CarlosEduardo
 */
public class ColunasTabelas {
    public Map<String,String> mDescri;
    public Map<String,String> mCol;
    public Map<String,String> mTabelas;
    public Map<String,String> mTabColsSelBusca;
    public Map<String,String> mTabColsSelDados;
    final HttpServletRequest request;
    
    public ColunasTabelas(HttpServletRequest request){
        mDescri = new HashMap<>();
        mCol = new HashMap<>();
        mTabelas = new HashMap<>();
        mTabColsSelBusca = new HashMap<>();
        mTabColsSelDados = new HashMap<>();
        this.request = request;
        setColTab();
        carregaTabelas();
        carregaTabColsBusca();
        carregaTabColsDados();
    }
    
    private void setColTab(){
        try {
            ResultSet rs = Parametros.getConexao().getRs("select * from cols_descri");
            while (rs.next()) {                
                putMs(rs.getString(2), rs.getString(3));
            }
        } catch (SQLException ex) {
            throw new LogError(ex.getMessage(), ex,request);
        }
    }
    
    private void putMs(String coluna,String descri){
        mDescri.put(coluna, descri);
        mCol.put(descri, coluna);
    }
    
    public String getDescricao(String coluna){
        return mDescri.get(coluna);
    }
    
    public String getColuna(String descricao){
        return mCol.get(descricao);
    }
    
    private void carregaTabelas(){
        mTabelas.put("lotericas", "lotericas");
    }
    
    private void carregaTabColsBusca(){
        mTabColsSelBusca.put("lotericas", "id,codigo_caixa,nome");
    }
    private void carregaTabColsDados(){
        mTabColsSelDados.put("lotericas", "codigo_caixa,nome");
    }
    public String getTabela(String tabela){
        return mTabelas.get(tabela);
    }
    
    public String getTabColsBusca(String tabela){
        return mTabColsSelBusca.get(tabela);
    }
    public String getTabColsDados(String tabela){
        return mTabColsSelDados.get(tabela);
    }
    
}
