/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;

import com.cth.gestorlotericoweb.dados.Loterica;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;

/**
 *
 * @author CarlosEduardo
 */
public class Grava {
    public String output;
    HttpServletRequest request;
    public Integer id;

    public Grava(HttpServletRequest request) {
        this.request = request;
        grava();
    }
    private void grava(){
        String input = request.getParameter("it");
        switch(input){
            case "lotericas":     
                Loterica loterica = new Loterica(request.getParameter("codigo_caixa"), request.getParameter("nome"));
                if("0".equals(request.getParameter("id"))){
                    loterica.insere();
                }else{
                    loterica.altera(request.getParameter("id"));
                }
                id = loterica.getId();
                break;
            case "terminais":                            
                break;
            default:
                break;
        }
    }
}
