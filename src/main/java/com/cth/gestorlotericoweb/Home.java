/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;

import com.cth.gestorlotericoweb.parametros.Parametros;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;

/**
 * @author CarlosEduardo
 */
public class Home {
	
	public String output;
	public String input;
	public String id;
	HttpServletRequest request;
	
	public Home(HttpServletRequest request) {
		this.request = request;
	}
	
	public void setHome() {
		try {
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
			Template templatePrinc = ve.getTemplate("templates/Modern/corpo.html", "UTF-8");
			VelocityContext contextPrinc = new VelocityContext();
			contextPrinc = ConteudoTelas.getConteudoTela(contextPrinc, ve, request);
			contextPrinc.put("version", "?v="+Parametros.getVersion());
			contextPrinc.put("entidadeNome", Parametros.getEntidadeNome());
			StringWriter writer = new StringWriter();
			templatePrinc.merge(contextPrinc, writer);
			Parametros.gravaLogSessao(request);
			output = writer.toString();
		} catch (ResourceNotFoundException | MethodInvocationException | ParseErrorException ex) {
			new LogError(ex.getMessage(), ex, request);
		}
	}
}
