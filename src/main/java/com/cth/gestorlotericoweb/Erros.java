/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.cth.gestorlotericoweb.parametros.Parametros.*;

/**
 *
 * @author CarlosEduardo
 */
public class Erros {

    public Erros(HttpServletRequest request,String msg,String trace) {
        try {
            String pgAnt;
            if(request.getSession(false).getAttribute("url")==null){
                pgAnt = "";
            }else{
                pgAnt = request.getSession(false).getAttribute("url").toString();
            }
            String pgAtu = "";
            if(request.getParameterMap().keySet().size()>0){
                pgAtu = request.getRequestURL().toString()+"?";
                List l = new ArrayList();
                for(Object ob:request.getParameterMap().keySet()){
                    l.add(ob.toString()+"="+StringUtils.join(request.getParameterValues(ob.toString()), ','));
                }
                
                pgAtu += StringUtils.join(l,"&");
                //pgAtu = String.valueOf(request.getRequestURL().append('?').append(request.getQueryString()));
                
            }
            PreparedStatement ps = conexao.getPst("INSERT INTO erros(\n" +
"            id_usuario, id_entidade, id_sessao, ip, nome_maquina, pg_anterior, \n" +
        "            pg_atual, erro, stacktrace)\n" +
        "    VALUES ( ?, ?, ?, ?, ?, ?, \n" +
        "            ?, ?, ? );");
            ps.setInt(1, idUsuario);
            ps.setInt(2, idEntidade);
            ps.setString(3, request.getSession(false).getId());
            ps.setString(4, request.getRemoteAddr());
            ps.setString(5, request.getRemoteHost());
            ps.setString(6, pgAnt);
            ps.setString(7, pgAtu);
            ps.setString(8, msg);
            ps.setString(9, trace);
            ps.execute();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        Sessao.invalidaSessao(request);
    }
    
}
