package com.cth.gestorlotericoweb.validacoes;

import org.jsoup.nodes.Element;

import javax.servlet.http.HttpServletRequest;

public class Validacoes {
	final HttpServletRequest request;
	
	public Validacoes(HttpServletRequest request) {
		this.request = request;
	}
	
	public Element getElement(String tag, String classe){
		Element element = new Element(tag);
		element.attr("class",classe);
		return element;
	}
	
	public Element getElement(String tag, String classe, String id){
		Element element = new Element(tag);
		element.attr("class",classe);
		element.attr("id",id);
		return element;
	}
	
	public Element setEvento(Element element, String nomeEvento, String evento){
		element.attr(nomeEvento,evento);
		return element;
	}
}
