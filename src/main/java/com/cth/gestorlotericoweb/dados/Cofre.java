/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.dados;

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
import java.util.List;

/**
 *
 * @author CarlosEduardo
 */
public class Cofre extends Cadastros{
    Integer id;
    String nomeCofre;
    String tipoCofre;
    String idLoterica;
    String observacoes;    
    final HttpServletRequest request;

    public Cofre(String nomeCofre, String tipoCofre, String idLoterica, String observacoes, HttpServletRequest request) {
        this.nomeCofre = nomeCofre;
        this.tipoCofre = tipoCofre;
        this.idLoterica = idLoterica;
        this.observacoes = observacoes;
        this.request = request;
    }

    public Cofre(HttpServletRequest request) {
        this.request = request;
    }
    
    public Cofre(Integer id,HttpServletRequest request) {
        this.id = id;
        this.request = request;
        getDados();
    }
    private void getDados(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("select nome_cofre, tipo_cofre, observacoes, id_loterica \n" +
"  FROM cofres where id = ? and id_entidade = ? ",false);
            ps.setInt(1, id);
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                nomeCofre = rs.getString(1);
                tipoCofre = rs.getString(2);
                observacoes = rs.getString(3);
                idLoterica = rs.getString(4);
            }else{
                nomeCofre = "";
                tipoCofre = "";
                observacoes = "";
                idLoterica = "";
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void insere(){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO cofres(\n" +
"            nome_cofre, tipo_cofre, observacoes, id_loterica, id_entidade)\n" +
"    VALUES ( ?, ?, ?, ?, ? );");
            ps.setString(1, nomeCofre);
            ps.setInt(2, Integer.valueOf(tipoCofre));
            if(observacoes == null){
                ps.setNull(3, java.sql.Types.LONGVARCHAR);
            }else{
                ps.setString(3, observacoes);
            }
            ps.setInt(4, Integer.valueOf(idLoterica));
            ps.setInt(5, Parametros.idEntidade);
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
            }
            
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void altera(String idL){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("UPDATE cofres\n" +
"   SET nome_cofre = ?, tipo_cofre = ?, observacoes = ?, id_loterica = ? \n" 
                    + " where id = ? and id_entidade = ? ", false);
            ps.setString(1, nomeCofre);
            ps.setInt(2, Integer.valueOf(tipoCofre));
            if(observacoes == null){
                ps.setNull(3, java.sql.Types.LONGVARCHAR);
            }else{
                ps.setString(3, observacoes);
            }
            ps.setInt(4, Integer.valueOf(idLoterica));
            id = Integer.valueOf(idL);
            ps.setInt(5, id);
            ps.setInt(6, Parametros.idEntidade);
            ps.execute();
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    private List<String> setOpcoesFiltro(){
        ColunasTabelas colunasTabelas = new ColunasTabelas(request);
        lOpts = colunasTabelas.getlOpts("cofres");
        return lOpts;
    }
    
    public VelocityContext getHtml(VelocityContext contextPrinc,VelocityEngine ve,String idS){        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/cofres.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());       
        setOpcoesFiltro();
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"cofres").toString());
        return contextPrinc;
    }

    public Integer getId() {
        return id;
    }
    
    public String getNomeCofre() {
        return nomeCofre;
    }
}
