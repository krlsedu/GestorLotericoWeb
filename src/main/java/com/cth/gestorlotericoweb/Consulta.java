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
import java.util.logging.Level;
import java.util.logging.Logger;
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
        switch(request.getParameter("coluna")){
            case "busca":
                geraTabelaPopUp();
                break;
            case "dados":
                geraTabelaDados();
                break;
            default:
                output ="definir o tipo de busca";
                break;
        }
    }
    
    private void geraTabelaDados(){        
        ColunasTabelas colunasTabelas = new ColunasTabelas();
        String tabela = colunasTabelas.getTabela(request.getParameter("tabela"));
        if(tabela !=null){
            String sql = "select "+colunasTabelas.getTabColsDados(tabela)+" from "+tabela+" where id_entidade = ? and id = ?";
            try {
                PreparedStatement ps = Parametros.getConexao().getPst(sql, false);
                ps.setInt(1, Parametros.idEntidade);
                ps.setInt(2, 0);
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
    }
    
    private void geraTabelaPopUp(){
        ColunasTabelas colunasTabelas = new ColunasTabelas();
        String coluna = colunasTabelas.getColuna(request.getParameter("coluna"));
        String tabela = colunasTabelas.getTabela(request.getParameter("tabela"));
        if(coluna!=null && tabela !=null){
            String sql = "select "+colunasTabelas.getTabColsBusca(tabela)+" from "+tabela+" where id_entidade = ? and "+coluna+" like ?";
            try {
                PreparedStatement ps = Parametros.getConexao().getPst(sql, false);
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
                            lVals.add("<th>"+rs.getString(i)+"</th>");
                        }
                        lLinhas.add("<tr>"+StringUtils.join(lVals, ' ')+"</tr>");
                    }
                    output = "<thead>"+StringUtils.join(lCols, ' ')+"</thead><tbody>"+StringUtils.join(lLinhas, ' ')+"</tbody>";   
                }else{
                    output = ps.toString();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }else{
            output = "coluna==null || tabela ==null"+request.getParameter("coluna")+request.getParameter("tabela")+coluna+tabela;
        }
    }
    
    
}
