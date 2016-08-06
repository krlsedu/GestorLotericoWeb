/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.dados;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author CarlosEduardo
 */
public class Cadastros {
    List<String> lOpts = new ArrayList<>();
    public Cadastros() {
    }
    
    
    public StringWriter getSWPopup(VelocityEngine ve,String tipo){
        Template templatePopup;
        VelocityContext contextPopup;
        StringWriter writerPopup;
        templatePopup = ve.getTemplate( "templates/Modern/popup.html" , "UTF-8");
        contextPopup = new VelocityContext();
        writerPopup = new StringWriter();
        contextPopup.put("tabela", tipo);
        contextPopup.put("opt", StringUtils.join(lOpts, '\n'));
        templatePopup.merge(contextPopup, writerPopup);
        return writerPopup;
    }
    
    public StringWriter getSWBotoesPercorrer(VelocityEngine ve){
        Template templateBtnPerc;
        VelocityContext contextBtnPerc;
        StringWriter writerBtnPerc;
        templateBtnPerc = ve.getTemplate( "templates/Modern/botoes_percorrer.html" , "UTF-8");
        contextBtnPerc = new VelocityContext();
        writerBtnPerc = new StringWriter();
        templateBtnPerc.merge(contextBtnPerc, writerBtnPerc);
        return writerBtnPerc;
    }
}
