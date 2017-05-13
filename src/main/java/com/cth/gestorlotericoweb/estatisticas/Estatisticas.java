/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.estatisticas;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.dados.Cofre;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.saldos.SaldoCofre;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CarlosEduardo
 */
public class Estatisticas {
    final HttpServletRequest request;
    String valorApresentar;
    BigDecimal valorMovimentado;
    Boolean bom;

    public Estatisticas(HttpServletRequest request) {
        this.request = request;
    }
    
    public VelocityContext getHtml(VelocityContext contextPrinc,VelocityEngine ve,String idS){        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/estatisticas.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter(); 
        
        Diarias movimentoDiario = new Diarias(request);
        contextConteudo.put("acm_dia",movimentoDiario.valorApresentar); 
        Semanal movimentoSemanal = new Semanal(request,movimentoDiario);
        contextConteudo.put("acm_semana",movimentoSemanal.valorApresentar); 
        Mensal movimentoMensal = new Mensal(request,movimentoDiario);
        contextConteudo.put("acm_mes",movimentoMensal.valorApresentar); 
        
        SaldoCofre saldoCofre1 = new SaldoCofre(1, request);
        SaldoCofre saldoCofre2 = new SaldoCofre(2, request);        
        contextConteudo.put("saldo_cofre_1", saldoCofre1.getSaldoSt());
        contextConteudo.put("saldo_cofre_2", saldoCofre2.getSaldoSt());
        
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"movimentos_contas").toString());
        return contextPrinc;
    }
    
    public VelocityContext getHtml(VelocityContext contextPrinc,VelocityEngine ve){
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/estatisticas_dinamico.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        
        
        
        
        
//        Diarias movimentoDiario = new Diarias(request);
//        contextConteudo.put("acm_dia",movimentoDiario.valorApresentar);
//        Semanal movimentoSemanal = new Semanal(request,movimentoDiario);
//        contextConteudo.put("acm_semana",movimentoSemanal.valorApresentar);
//        Mensal movimentoMensal = new Mensal(request,movimentoDiario);
//        contextConteudo.put("acm_mes",movimentoMensal.valorApresentar);
//
//        SaldoCofre saldoCofre1 = new SaldoCofre(1, request);
//        SaldoCofre saldoCofre2 = new SaldoCofre(2, request);
//        contextConteudo.put("saldo_cofre_1", saldoCofre1.getSaldoSt());
//        contextConteudo.put("saldo_cofre_2", saldoCofre2.getSaldoSt());
        contextConteudo.put("stats",getHtmlWidgets());
        
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"movimentos_contas").toString());
        return contextPrinc;
    }
    
    public StringWriter getSWPopup(VelocityEngine ve,String tipo){
        Template templatePopup;
        VelocityContext contextPopup;
        StringWriter writerPopup;
        templatePopup = ve.getTemplate( "templates/Modern/modal.html" , "UTF-8");
        contextPopup = new VelocityContext();
        writerPopup = new StringWriter();
        contextPopup.put("tabela", tipo);
        List<String> lOpts = new ArrayList<>();
        contextPopup.put("opt", StringUtils.join(lOpts, '\n'));
        templatePopup.merge(contextPopup, writerPopup);
        return writerPopup;
    }
    
    private String getHtmlWidgets(){
        String abreLinha = "<div class=\"col_3\">";
        String fechaLinha = "</div>";
        StringBuilder sbWidgets= new StringBuilder();
        sbWidgets.append(abreLinha);
        Integer linhaAtu = 1;
        String sql = "SELECT * from itens_estatisticas where id_entidade = ? ORDER BY linha,coluna";
        try {
            PreparedStatement ps = Parametros.getConexao().getPst(sql,false);
            ps.setInt(1,Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Integer linha = rs.getInt("linha");
                if(linha!=linhaAtu){
                    sbWidgets.append(fechaLinha).append(abreLinha);
                }
                linhaAtu = linha;
                sbWidgets.append(getDadosWidget(rs));
            }
            sbWidgets.append(fechaLinha);
        } catch (SQLException e) {
            new LogError(e.getMessage(), e,request);
        }
        return sbWidgets.toString();
    }
    
    private String getDadosWidget(ResultSet rs)throws SQLException{
        Integer tipoValWidget = rs.getInt("tipo_widget");
        Integer idComponente = rs.getInt("id_componente");
        Integer idItemComponente = rs.getInt("id_item_componente");
        Integer linha = rs.getInt("linha");
        Integer coluna = rs.getInt("coluna");
        String htmlWidget  = rs.getString("estilo_widget");
        htmlWidget = htmlWidget.replace("col-md-5","col-md-3");
        Document document = Jsoup.parse(htmlWidget);
        Element nomeTpeWidget = document.getElementById("nome_tipo_widget");
        switch (nomeTpeWidget.val()){
            case "widget_valor_rs":
                Element div= document.getElementById(nomeTpeWidget.val());
                div.attr("id",nomeTpeWidget.val()+"_"+linha+"_"+coluna);
                Element valor = div.getElementById("valor_rs");
                valor.attr("id","valor_rs"+"_"+linha+"_"+coluna);
                valor.html(getValWidget(tipoValWidget,idComponente,idItemComponente));
                Element nome = div.getElementById("nome_valor_rs");
                nome.attr("id","nome_valor_rs"+"_"+linha+"_"+coluna);
                nome.html(getNomeItemComponente(idComponente,idItemComponente));
                nomeTpeWidget.remove();
                break;
        }
        return document.outerHtml();
    }
    
    private String getNomeItemComponente( Integer componente, Integer itemComponente){
        switch (componente){
            case 1://Cofre
                Cofre cofre = new Cofre(itemComponente,request);
                return cofre.getNomeCofre();
            case 2://Saldo de movimentações da conta da lotérica
            
            default:
                return "Não existe";
        }
    }
    
    private String getValWidget(Integer tipo, Integer componente, Integer itemComponente){
        switch (tipo){
            case 1: //saldo
                switch (componente){
                    case 1://Cofre
                        SaldoCofre saldoCofre = new SaldoCofre(itemComponente,request);
                        return saldoCofre.getSaldoSt();
                    default:
                        return "Não existe";
                }
            case 2: //Quantidades
                break;
            default:
                return "Não disponivel";
        }
        return "";
    }
    
}
