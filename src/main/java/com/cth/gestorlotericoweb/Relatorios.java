package com.cth.gestorlotericoweb;

import com.cth.gestorlotericoweb.detalhamento.MovimentosTerminal;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by CarlosEduardo on 07/05/2017.
 */
public class Relatorios {
	public String output;
	HttpServletRequest request;
	
	public Relatorios(HttpServletRequest request) {
		this.request = request;
		setConsulta();
	}
	
	private void setConsulta() {
		switch (request.getParameter("tipo").trim()) {
			case "detalhamento":
				getDetalhamento();
				break;
		}
	}
	
	private void getDetalhamento(){
		String modelo = request.getParameter("modelo").trim();
		switch (modelo){
			case "movimentos_terminal":
				MovimentosTerminal movimentosTerminal = new MovimentosTerminal(request);
				output = movimentosTerminal.getTabelaMovimentos();
				break;
		}
	}
}
