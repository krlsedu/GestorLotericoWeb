package com.cth.gestorlotericoweb.operador;

import com.cth.gestorlotericoweb.dados.ColunasTabelas;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Operador {
	List<String> lOpts = new ArrayList<>();
	final HttpServletRequest request;
	Integer id;
	
	public Operador(HttpServletRequest request) {
		this.request = request;
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
	
	public StringWriter getSWPopup(VelocityEngine ve, String tipo){
		Template templatePopup;
		VelocityContext contextPopup;
		StringWriter writerPopup;
		templatePopup = ve.getTemplate( "templates/Modern/modal.html" , "UTF-8");
		contextPopup = new VelocityContext();
		writerPopup = new StringWriter();
		contextPopup.put("tabela", tipo);
		contextPopup.put("opt", StringUtils.join(lOpts, '\n'));
		templatePopup.merge(contextPopup, writerPopup);
		return writerPopup;
	}
	
	public List<String> setOpcoesFiltro(String tipo){
		ColunasTabelas colunasTabelas = new ColunasTabelas(request);
		lOpts = colunasTabelas.getlOpts(tipo);
		return lOpts;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
}
