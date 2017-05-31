package com.cth.gestorlotericoweb.Estoque;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.utils.Parser;
import com.cth.gestorlotericoweb.utils.Seter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Itens extends Estoque{
	String nomeItem;
	String observacoes;
	
	Integer tipoItem;
	Integer unidade;
	Integer idLoterica;
	
	BigDecimal valorPadrao;
	
	
	public Itens(HttpServletRequest request) {
		super(request);
	}
	
	public void setItens(){
		nomeItem = request.getParameter("nome_item");
		observacoes = request.getParameter("observacoes");
		
		tipoItem = Integer.valueOf(request.getParameter("tipo_item"));
		unidade = Integer.valueOf(request.getParameter("unidade"));
		idLoterica = Parser.toIntegerNull(request.getParameter("id_loterica"));
		
		valorPadrao = Parser.toBigDecimalFromHtmlNull(request.getParameter("valor_padrao"));
	}
	
	public void grava(){
		if("0".equals(request.getParameter("id"))){
			insere();
		}else{
			altera();
		}
	}
	
	private void insere(){
		//language=PostgresPLSQL
		String sql = "INSERT INTO public.itens_estoque(\n" +
				"            tipo_item, unidade, nome_item, valor_padrao, observacoes, \n" +
				"            id_loterica, id_entidade)\n" +
				"    VALUES ( ?, ?, ?, ?, ?, \n" +
				"            ?, ?)";
		try {
			PreparedStatement ps = Parametros.getConexao().getPst(sql);
			ps.setInt(1,tipoItem);
			ps.setInt(2,unidade);
			ps.setString(3,nomeItem);
			ps = Seter.set(ps,4,valorPadrao);
			ps = Seter.set(ps,5,observacoes);
			ps = Seter.set(ps,6,idLoterica);
			ps.setInt(7,Parametros.idEntidade);
			ps.execute();
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()){
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
	}
	
	private void altera(){
		id = Parser.toInteger(request.getParameter("id"));
		//language=PostgresPLSQL
		String sql = "UPDATE public.itens_estoque\n" +
				"   SET  tipo_item=?, unidade=?, nome_item=?, valor_padrao=?, observacoes=?, \n" +
				"       id_loterica=?\n" +
				" WHERE id = ? and id_entidade = ?";
		try {
			PreparedStatement ps = Parametros.getConexao().getPst(sql);
			ps.setInt(1,tipoItem);
			ps.setInt(2,unidade);
			ps.setString(3,nomeItem);
			ps = Seter.set(ps,4,valorPadrao);
			ps = Seter.set(ps,5,observacoes);
			ps = Seter.set(ps,6,idLoterica);
			ps.setInt(7,id);
			ps.setInt(8,Parametros.idEntidade);
			ps.execute();
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
	}
	
	public VelocityContext getHtml(VelocityContext contextPrinc, VelocityEngine ve, String idS){
		Template templateConteudo;
		VelocityContext contextConteudo;
		StringWriter writerConteudo;
		templateConteudo = ve.getTemplate( "templates/Modern/Estoque/Item.html" , "UTF-8");
		contextConteudo = new VelocityContext();
		writerConteudo = new StringWriter();
		contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());
		setOpcoesFiltro("itens_estoque");
		templateConteudo.merge( contextConteudo, writerConteudo );
		contextPrinc.put("conteudo", writerConteudo.toString());
		contextPrinc.put("popup", getSWPopup(ve,"itens_estoque").toString());
		return contextPrinc;
	}
}
