/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.configuracoes;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.parametros.Parametros;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author CarlosEduardo
 */
public class EstatisticasLinhas extends Configuracoes{
    String linha;
    String coluna;
    String idComponente;
    String idItemComponente;
    String estiloWidget;
    String nomeWidget;
    String idLoterica;
    String tipoWidget;
    String html;
    Integer id;

    public EstatisticasLinhas(HttpServletRequest request) {
        super(request);
        this.linha = request.getParameter("num_linhas");
        id=0;
    }
    
    public VelocityContext getHtml(VelocityContext contextPrinc,VelocityEngine ve,String idS){        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/configuracoes/estatisticas_linhas.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        contextConteudo.put("num",linha);       
        contextConteudo.put("id_reg",id);  
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        return contextPrinc;
    }
    
    public void setEstatisticasLinhas(){
        this.linha = request.getParameter("linha");
        this.coluna = request.getParameter("coluna");
        this.idComponente = request.getParameter("id_componente");
        this.idItemComponente = request.getParameter("id_item_componente");
        this.estiloWidget = request.getParameter("estilo_widget");
        this.nomeWidget = request.getParameter("nome_widget");
        this.idLoterica = request.getParameter("id_loterica");
        this.tipoWidget = request.getParameter("tipo_widget");
        this.html = request.getParameter("html");
    }
    
    public void grava(){
        if("0".equals(request.getParameter("id"))){
            insere();
        }else{
            altera();
        }
    }
    
    public void insere(){
        try {
            String sql = "INSERT INTO public.itens_estatisticas(\n" +
                    "            linha, coluna, id_componente, id_item_componente, estilo_widget, \n" +
                    "            nome_widget, id_loterica, id_entidade, tipo_widget, html)\n" +
                    "    VALUES ( ?, ?, ?, ?, ?, \n" +
                    "            ?, ?, ?, ?, ?);";
            PreparedStatement ps = Parametros.getConexao(request).getPst(sql);
            ps.setInt(1, Integer.valueOf(linha));
            ps.setInt(2, Integer.valueOf(coluna));
            ps.setInt(3, Integer.valueOf(idComponente));
            ps.setInt(4, Integer.valueOf(idItemComponente));
            ps.setString(5, estiloWidget);
            ps.setString(6, nomeWidget);
            ps.setInt(7, Integer.valueOf(idLoterica));
            ps.setInt(8, Parametros.idEntidade);
            ps.setInt(9, Integer.valueOf(tipoWidget));
            ps.setString(10, html);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void altera(){
    
    }
}
