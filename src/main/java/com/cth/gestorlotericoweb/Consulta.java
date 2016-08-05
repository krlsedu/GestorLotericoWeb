/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;

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
        geraTabela();
    }
    
    private void geraTabela(){
        String sql = "select * from "+request.getParameter("tabela")+" where i_entidades = ? and "+request.getParameter("coluna")+" like '%?%'";
        try {
            PreparedStatement ps = Parametros.getConexao().getPst(sql, false);
            ps.setInt(1, Parametros.idEntidade);
            ps.setString(2, request.getParameter("id"));
            ResultSet rs = ps.getResultSet();
            List lCols = new ArrayList();
            List lLinhas = new ArrayList();
            List lVals;
            while (rs.next()) {  
                lVals = new ArrayList();
                for(int i = 1;i<=rs.getMetaData().getColumnCount();i++){                                    
                    if(rs.isFirst()){
                        lCols.add("<th>"+rs.getMetaData().getColumnName(i)+"</th>");
                    }
                    lVals.add("<th>"+rs.getString(i)+"</th>");
                }
                lLinhas.add("<tr>"+StringUtils.join(lVals, '\n')+"</tr>");
            }
            output = "<thead>"+StringUtils.join(lCols, '\n')+"</thead><tbody>"+StringUtils.join(lLinhas, '\n')+"</tbody>";
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        output = "";
    }
    
}
