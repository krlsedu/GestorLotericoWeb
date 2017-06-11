package com.cth.gestorlotericoweb.estoque;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.operador.Operacoes;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.utils.Parser;
import com.cth.gestorlotericoweb.utils.Seter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Date;
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
	
	Date dataSorteio;
	public Itens(HttpServletRequest request) {
		super(request);
	}
	
	public Itens(MovimentosEstoque movimentosEstoque) {
		super(movimentosEstoque.request);
		this.idLoterica = Parametros.getIdLoterica();
		Operacoes operacoes =movimentosEstoque.getOutrosMovimentos().getOperacoes();
		switch (operacoes.getTipoItem()){
//           1 >> <option value="3" >Bolão</option>
			case 1:
				switch (operacoes.getTipoOperacaoCaixa()){
					//2 Geração >> 3
					case 2:
						this.tipoItem = 3;
						this.unidade = 1;
						switch (operacoes.getEdicaoItem()){
//							"        <option value=\"1\" >MEGA-SENA</option>\n" +
//									"        <option value=\"3\" >QUINA</option>\n" +
//									"        <option value=\"5\" >LOTOFÁCIL</option>\n" +
//									"        <option value=\"6\" >LOTOMANIA</option>\n" +
//									"        <option value=\"7\" >TIMEMANIA</option>\n" +
//									"        <option value=\"8\" >DUPLA SENA</option>\n" +
//									"        <option value=\"9\" >LOTECA</option>\n" +
//									"        <option value=\"10\" >LOTOGOL</option>\n" +
//									"        <option value=\"11\" >Outros</option>";
							case 1:
								this.nomeItem = "MEGA-SENA";
								break;
							case 3:
								this.nomeItem = "QUINA";
								break;
							case 5:
								this.nomeItem = "LOTOFÁCIL";
								break;
							case 6:
								this.nomeItem = "LOTOMAINIA";
								break;
							case 7:
								this.nomeItem = "TIMEMANIA";
								break;
							case 8:
								this.nomeItem = "DUPLA SENA";
								break;
							case 9:
								this.nomeItem = "LOTECA";
								break;
							case 10:
								this.nomeItem = "LOTOGOL";
								break;
							case 11:
								this.nomeItem = "Outros";
								break;
							default:
								this.nomeItem = "Não iformado";
						}
						break;
					default:
						break;
				}
				break;
			default:
				break;
		}
		this.dataSorteio = operacoes.getDataSorteio();
		this.valorPadrao = operacoes.getValorMovimentado();
		this.observacoes = "Cadatrado automaticamente pela rotina de Operações do funcionário";
	}
	
	private void buscaBanco(){
		//language=PostgresPLSQL
		String sql = "SELECT tipo_item, unidade, nome_item, valor_padrao, observacoes, " +
				"       id_loterica, data_sorteio " +
				"   FROM public.itens_estoque" +
				"   WHERE " +
				"       id = ? AND " +
				"       id_entidade = ?";
		try {
			Seter ps = new Seter(sql,request,false);
			ps.set(this.id);
			ps.set(Parametros.idEntidade);
			ResultSet rs = ps.getPst().executeQuery();
			if (rs.next()) {
				this.tipoItem = rs.getInt(1);
				this.unidade  = rs.getInt(2);
				this.nomeItem = rs.getString(3);
				this.valorPadrao = rs.getBigDecimal(4);
				this.observacoes = rs.getString(5);
				this.idLoterica  = rs.getInt(6);
				this.dataSorteio = rs.getDate(7);
			}
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
	}
	
	public void setItens(){
		nomeItem = request.getParameter("nome_item");
		observacoes = request.getParameter("observacoes");
		
		tipoItem = Integer.valueOf(request.getParameter("tipo_item"));
		unidade = Integer.valueOf(request.getParameter("unidade"));
		idLoterica = Parser.toIntegerNull(request.getParameter("id_loterica"));
		
		valorPadrao = Parser.toBigDecimalFromHtmlNull(request.getParameter("valor_padrao"));
		
		dataSorteio = Parser.toDbDate(request.getParameter("data_sorteio"));
	}
	
	public void grava(){
		if("0".equals(request.getParameter("id"))){
			insere();
		}else{
			altera();
		}
	}
	
	public void insere(){
		//language=PostgresPLSQL
		String sql = "INSERT INTO public.itens_estoque(\n" +
				"            tipo_item, unidade, nome_item, valor_padrao, observacoes, \n" +
				"            id_loterica, id_entidade, data_sorteio)\n" +
				"    VALUES ( ?, ?, ?, ?, ?, \n" +
				"            ?, ?, ?)";
		try {
			PreparedStatement ps = Parametros.getConexao().getPst(sql);
			ps.setInt(1,tipoItem);
			ps.setInt(2,unidade);
			ps.setString(3,nomeItem);
			ps = Seter.set(ps,4,valorPadrao);
			ps = Seter.set(ps,5,observacoes);
			ps = Seter.set(ps,6,idLoterica);
			ps.setInt(7,Parametros.idEntidade);
			ps = Seter.set(ps,8,dataSorteio);
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
				"       id_loterica=?, data_sorteio = ? \n" +
				" WHERE id = ? and id_entidade = ?";
		try {
			PreparedStatement ps = Parametros.getConexao().getPst(sql);
			ps.setInt(1,tipoItem);
			ps.setInt(2,unidade);
			ps.setString(3,nomeItem);
			ps = Seter.set(ps,4,valorPadrao);
			ps = Seter.set(ps,5,observacoes);
			ps = Seter.set(ps,6,idLoterica);
			ps = Seter.set(ps,7,dataSorteio);
			ps.setInt(8,id);
			ps.setInt(9,Parametros.idEntidade);
			ps.execute();
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
	}
	
	public VelocityContext getHtml(VelocityContext contextPrinc, VelocityEngine ve, String idS){
		Template templateConteudo;
		VelocityContext contextConteudo;
		StringWriter writerConteudo;
		templateConteudo = ve.getTemplate("templates/Modern/estoque/Item.html", "UTF-8");
		contextConteudo = new VelocityContext();
		writerConteudo = new StringWriter();
		contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());
		setOpcoesFiltro("itens_estoque");
		templateConteudo.merge( contextConteudo, writerConteudo );
		contextPrinc.put("conteudo", writerConteudo.toString());
		contextPrinc.put("popup", getSWPopup(ve,"itens_estoque").toString());
		return contextPrinc;
	}
	
	public Integer getIdItensEstoque(Integer idItemEst,Boolean bolao){
		if (idItemEst==null){
			return null;
		}
		if (idItemEst>100 && bolao) {
			idItemEst -=100;
			this.id = idItemEst;
			buscaBanco();
			return idItemEst;
		}
		if(bolao) {
			return null;
		}else {
			this.id = idItemEst;
			buscaBanco();
			return idItemEst;
		}
	}
	
	public BigDecimal getValorPadrao() {
		return valorPadrao;
	}
}
