/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;

import com.cth.gestorlotericoweb.dados.ColunasTabelas;
import com.cth.gestorlotericoweb.parametros.Parametros;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author CarlosEduardo
 */
public class Consulta {
    public Integer id;
    public String output;
    public Map<String,String> mapDados;
    HttpServletRequest request;

    public Consulta(Integer id) {
        this.id = id;
    }

    public Consulta(HttpServletRequest request) {
        this.request = request;
        switch(request.getParameter("tipo").trim()){
            case "busca":
                geraTabelaPopUp(request.getParameter("coluna_buscar"));
                break;
            case "dados":
                geraTabelaDados();
                break;                
            case "id":
                getId();
                break;
            case "opt":
                getOpt();
                break;
            default:
                output ="definir o tipo de busca";
                break;
        }
    }
    
    private void getOpt(){        
        ColunasTabelas colunasTabelas = new ColunasTabelas(request);
        String tabela = colunasTabelas.getTabela(request.getParameter("tabela"));
        if(tabela !=null){
            output = colunasTabelas.getOpts(tabela);
        }
    }
    
    private void getId(){
        ColunasTabelas colunasTabelas = new ColunasTabelas(request);
        String tabela = colunasTabelas.getTabela(request.getParameter("tabela"));
        if(tabela !=null){
            String btn = request.getParameter("btn");            
            String id = request.getParameter("id");
            String col;
            String order;
            switch(btn){
                case "i":
                    col = "";
                    order = "asc";
                    break;
                case "a":
                    col = " and id < "+id;
                    order = "desc";
                    break;                
                case "p":
                    col = " and id > "+id;
                    order = "asc";
                    break;                  
                case "f":
                    col = "";
                    order = "desc";
                    break;
                default:
                    col = "";
                    order = "";
                    break;
            }
            String sql = "select id from "+tabela+" where id_entidade = ? "+col+" order by id "+order+" Limit 1";
            try {
                PreparedStatement ps = Parametros.getConexao(request).getPst(sql, false);
                ps.setInt(1, Parametros.idEntidade);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    output = rs.getString(1);
                }else{
                    output = id;
                }
            } catch (SQLException ex) {
                output = ex.getMessage();
                new LogError(ex.getMessage(), ex,request);
            }
        }
    }
    
    private void geraTabelaDados(){        
        ColunasTabelas colunasTabelas = new ColunasTabelas(request);
        String tabela = colunasTabelas.getTabela(request.getParameter("tabela"));
        if(tabela !=null){
            String sql = "select "+colunasTabelas.getTabColsDados(tabela)+" from "+tabela+" where id_entidade = ? and id = ? ";
            try {
                PreparedStatement ps = Parametros.getConexao(request).getPst(sql, false);
                ps.setInt(1, Parametros.idEntidade);
                ps.setInt(2, Integer.valueOf(request.getParameter("id")));
                ResultSet rs = ps.executeQuery();
                List<String> lInputs = new ArrayList<>();
                if (rs.next()) {
                    lInputs.add("<input type=\"text\" id=\"num_cols\" value=\""+rs.getMetaData().getColumnCount()+"\" readonly>");
                    for(int i = 1; i<=rs.getMetaData().getColumnCount();i++){    
                        lInputs.add("<input type=\"text\" id=\"nome_coluna_"+i+"\" value=\""+rs.getMetaData().getColumnName(i)+"\" readonly>");
                        lInputs.add("<input type=\"text\" id=\"busca_col_"+rs.getMetaData().getColumnName(i)+"\" value=\""+rs.getString(i)+"\" readonly>");
                    }
                }
                output = StringUtils.join(lInputs, "\n");
            } catch (SQLException ex) {
                new LogError(ex.getMessage(), ex,request);
            }
        }
    }
    
    private void geraTabelaPopUp(String colunaBuscar){
        ColunasTabelas colunasTabelas = new ColunasTabelas(request);
        String coluna = colunasTabelas.getColuna(request.getParameter("coluna"));
        String tabela = colunasTabelas.getTabela(request.getParameter("tabela"));
        if(coluna!=null && tabela !=null){
            String sql = "select "+colunasTabelas.getTabColsBusca(tabela)+" from "+tabela+" where id_entidade = ? and lower("+coluna+") like lower(?) order by "+coluna;
            try {
                PreparedStatement ps = Parametros.getConexao(request).getPst(sql, false);
                ps.setInt(1, Parametros.idEntidade);
                ps.setString(2, "%"+request.getParameter("id")+"%");
                ResultSet rs = ps.executeQuery();
                List lCols = new ArrayList();
                List lLinhas = new ArrayList();
                List lVals;
                if(rs !=null){
                    while (rs.next()) {  
                        lVals = new ArrayList();
                        for(int i = 1;i<=rs.getMetaData().getColumnCount();i++){                                    
                            if(rs.isFirst()){
                                lCols.add("<th>"+colunasTabelas.getDescricao(rs.getMetaData().getColumnName(i))+"</th>");
                            }
                            if(colunaBuscar == null){
                                lVals.add("<th onclick=\"setaIdEBusca('"+rs.getString("id")+"')\">"+rs.getString(i)+"</th>");
                            }else{
                                if(colunaBuscar.equals("id")){
                                    lVals.add("<th onclick=\"setaIdEBusca('"+rs.getString("id")+"')\">"+rs.getString(i)+"</th>");
                                }else{
                                    lVals.add("<th onclick=\"setaIdFk('"+rs.getString(1)+"','"+colunaBuscar+"','"+rs.getString(2)+"')\">"+rs.getString(i)+"</th>");
                                }
                            }
                        }
                        lLinhas.add("<tr >"+StringUtils.join(lVals, ' ')+"</tr>");
                    }
                    output="<thead>"+StringUtils.join(lCols, ' ')+"</thead><tbody>"+StringUtils.join(lLinhas, ' ')+"</tbody>";  
                }else{
                    output = ps.toString();
                }
            } catch (SQLException ex) {
                new LogError(ex.getMessage(), ex,request);
            }
        }else{
            output = "coluna==null || tabela ==null"+request.getParameter("coluna")+request.getParameter("tabela")+coluna+tabela;
        }
    }
    
    
}
