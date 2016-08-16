/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.dados;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.parametros.Parametros;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author CarlosEduardo
 */
public class Operacao extends Cadastros{
    Integer id;
    String nomeOperCaixa;
    String nomeOper;
    String tipoOper;
    String observacoes;
    final HttpServletRequest request;

    public Operacao(String nomeOperCaixa, String nomeOper, String tipoOper, String observacoes, HttpServletRequest request) {
        this.nomeOperCaixa = nomeOperCaixa;
        this.nomeOper = nomeOper;
        this.tipoOper = tipoOper;
        this.observacoes = observacoes;
        this.request = request;
    }    

    public Operacao(HttpServletRequest request) {
        this.request = request;
    }
    public Operacao(Integer id,HttpServletRequest request) {
        this.id = id;
        this.request = request;
        getDados();
    }
    private void getDados(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("select nome_oper_caixa, nome_oper, tipo_oper, observacoes \n" +
"  FROM contas where id = ? and id_entidade = ? ",false);
            ps.setInt(1, id);
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                nomeOperCaixa = rs.getString(1);
                nomeOper = rs.getString(2);
                tipoOper = rs.getString(3);
                observacoes = rs.getString(4);
            }else{
                nomeOper = "";
                nomeOperCaixa = "";
                tipoOper = "";
                observacoes = "";
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void insere(){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO operacoes(\n" +
"            nome_oper_caixa, nome_oper, tipo_oper, observacoes, id_entidade)\n" +
"    VALUES ( ?, ?, ?, ?, ?);");
            ps.setString(1, nomeOperCaixa);
            ps.setString(2, nomeOper);
            ps.setInt(3, Integer.valueOf(tipoOper));
            if(observacoes == null){
                ps.setNull(4, java.sql.Types.LONGVARCHAR);
            }else{
                ps.setString(4, observacoes);
            }
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
            PreparedStatement ps = Parametros.getConexao(request).getPst("UPDATE operacoes\n" +
"   SET nome_oper_caixa=?, nome_oper=?, tipo_oper=?, observacoes=? "
                    + " where id = ? and id_entidade = ? ", false);
            ps.setString(1, nomeOperCaixa);
            ps.setString(2, nomeOper);
            ps.setInt(3, Integer.valueOf(tipoOper));
            if(observacoes == null){
                ps.setNull(4, java.sql.Types.LONGVARCHAR);
            }else{
                ps.setString(4, observacoes);
            }
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
        lOpts = colunasTabelas.getlOpts("operacoes");
        return lOpts;
    }
    
    public VelocityContext getHtml(VelocityContext contextPrinc,VelocityEngine ve,String idS){        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/operacoes.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());       
        setOpcoesFiltro();
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"operacoes").toString());
        return contextPrinc;
    }

    public Integer getId() {
        return id;
    }

    public String getNomeOperCaixa() {
        return nomeOperCaixa;
    }

    public String getNomeOper() {
        return nomeOper;
    }

    public String getTipoOper() {
        return tipoOper;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public HttpServletRequest getRequest() {
        return request;
    }
    
    
    
}
