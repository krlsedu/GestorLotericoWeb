/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;

import com.cth.gestorlotericoweb.dados.Usuario;
import com.cth.gestorlotericoweb.parametros.Parametros;

import javax.servlet.http.HttpServletRequest;

/**
 * @author CarlosEduardo
 */
public class Auth {
	
	HttpServletRequest request;
	public String output;
	
	public Auth(HttpServletRequest request) {
		this.request = request;
	}
	
	public void auth() {
		try {
			Usuario usuario = new Usuario(request);
			if (usuario.getIdUsuario() > 0) {
				Parametros.setIdUsuario(usuario.getIdUsuario());
				if (usuario.getQtdEntidades() == 1) {
					output = "app";
					Parametros.setIdEntidade(usuario.getlEntidadesUsuario().get(0));
					Parametros.setDadosFuncXterm(request);
				} else {
					if (usuario.getQtdEntidades() != 0) {
						output = "entidades";
					} else {
						output = "entidades";
					}
				}
				Parametros.gravaLogin(request.getParameter("user"), request.getRemoteAddr(), request.getRemoteHost(), "S", request);
			} else {
				output = "False";
				Parametros.gravaLogin(request.getParameter("user"), request.getRemoteAddr(), request.getRemoteHost(), "N", request);
				request.getSession(false).invalidate();
			}
		} catch (Exception ex) {
			new LogError(ex.getMessage(), ex,request);
		}
	}
}
