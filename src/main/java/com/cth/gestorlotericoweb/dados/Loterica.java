/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.dados;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.banco.Conexao;
import com.cth.gestorlotericoweb.parametros.Parametros;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author CarlosEduardo
 */
public class Loterica extends Cadastros{
    String codigoCaixa;
    String nome;
    Integer id;
    final HttpServletRequest request;

    public Loterica(String codigoCaixa, String nome,HttpServletRequest request) {
        this.codigoCaixa = codigoCaixa;
        this.nome = nome;
        this.request = request;
    }

    public Loterica(HttpServletRequest request) {
        this.request = request;
    }
    
    public Loterica(Integer id,HttpServletRequest request) {
        this.id = id;
        this.request = request;
        getDados();
    }
    private void getDados(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("SELECT codigo_caixa, nome\n" +
                    "  FROM public.lotericas where id = ? and id_entidade = ? ",false);
            ps.setInt(1, id);
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                codigoCaixa = rs.getString(1);
                nome = rs.getString(2);
            }else{
                codigoCaixa = "";
                nome = "";
                id = 0;
            }
        } catch (SQLException ex) {
            throw new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void insere(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("INSERT INTO public.lotericas(\n" +
                    "            codigo_caixa, nome,id_entidade)\n" +
                    "    VALUES (?, ?, ?);");
            ps.setString(1, (codigoCaixa));
            ps.setString(2, nome);
            ps.setInt(3, Parametros.idEntidade);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void altera(String idL){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("UPDATE public.lotericas\n" +
                    "   SET  codigo_caixa=?, nome=?\n" +
                    " WHERE id=? and id_entidade = ? ", false);
            ps.setString(1, codigoCaixa);
            ps.setString(2, nome);
            id = Integer.valueOf(idL);
            ps.setInt(3, id);
            ps.setInt(4, Parametros.idEntidade);
            ps.execute();
        } catch (SQLException ex) {
            throw new LogError(ex.getMessage(), ex,request);
        }
    }

    public Integer getId() {
        return id;
    }

    public String getCodigoCaixa() {
        return codigoCaixa;
    }

    public String getNome() {
        return nome;
    }
    
    private List<String> setOpcoesFiltro(){
        lOpts = new ArrayList<>();
        lOpts.add("<option>Código na Caixa</option>");
        lOpts.add("<option>Nome</option>");
        return lOpts;
    }
    
    public VelocityContext getHtmlLoterica(VelocityContext contextPrinc,VelocityEngine ve,String idS){
        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/lotericas.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        if(idS !=null){
            Loterica loterica = new Loterica(Integer.valueOf(idS),request);
            contextConteudo.put("idbd", loterica.getId()); 
            contextConteudo.put("codcaixa", loterica.getCodigoCaixa());
            contextConteudo.put("nome", loterica.getNome());              
            contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());                   
        }else{
            contextConteudo.put("idbd", "0");    
            contextConteudo.put("codcaixa", "");
            contextConteudo.put("nome", ""); 
            contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());
        }
        setOpcoesFiltro();
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"lotericas").toString());
        return contextPrinc;
    }
}
