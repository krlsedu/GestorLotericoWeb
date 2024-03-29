package com.cth.gestorlotericoweb.operador;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.processos.MovimentoCaixa;
import com.cth.gestorlotericoweb.processos.OutrosMovimentos;
import com.cth.gestorlotericoweb.utils.Parser;
import com.cth.gestorlotericoweb.utils.MyPreparedStatement;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jsoup.nodes.Element;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Operacoes extends Operador {
	Integer tipoItem;
	Integer tipoOperacaoCaixa;
	Integer edicaoItem;
	Integer idFuncionario;
	Integer idTerminal;
	Integer idAberturaTerminal;
	Integer quantidade;
	
	String nomeConcurso;
	String observacoes;
	
	Date dataSorteio;
	Timestamp dataHoraMov;
	
	BigDecimal valorMovimentado;
	
	
	
	public Operacoes(HttpServletRequest request) {
		super(request);
		setOperacoe();
	}
	
	private void setOperacoe(){
		this.id = Parser.toInteger(request.getParameter("id"));
		this.tipoItem = Parser.toIntegerNull(request.getParameter("tipo_item"));
		this.tipoOperacaoCaixa = Parser.toInteger(request.getParameter("tipo_operacao_caixa"));
		this.edicaoItem = Parser.toIntegerNull(request.getParameter("edicao_item"));
		this.idFuncionario = Parser.toIntegerNull(request.getParameter("id_funcionario"));
		this.idTerminal = Parser.toIntegerNull(request.getParameter("id_terminal"));
		this.idAberturaTerminal = Parser.toIntegerNull(request.getParameter("id_abertura_terminal"));
		this.quantidade = Parser.toIntegerNull(request.getParameter("quantidade"));
		
		this.nomeConcurso = request.getParameter("nome_concurso");
		this.observacoes = request.getParameter("observacoes");
		
		this.dataSorteio = Parser.toDbDate(request.getParameter("data_sorteio"));
		if(this.tipoItem != null && this.tipoOperacaoCaixa != null) {
			if (this.tipoItem == 1 && this.tipoOperacaoCaixa == 2) {
				if (this.quantidade != null) {
					this.valorMovimentado = Parser.toBigDecimalFromHtml(request.getParameter("valor_movimentado"));
					this.valorMovimentado = this.valorMovimentado.divide(new BigDecimal(this.quantidade));
				} else {
					this.quantidade = 1;
				}
			} else {
				this.valorMovimentado = Parser.toBigDecimalFromHtml(request.getParameter("valor_movimentado"));
			}
		}else{
			this.valorMovimentado = Parser.toBigDecimalFromHtml(request.getParameter("valor_movimentado"));
		}
	}
	
	public void getDadosPorId(){
		//language=PostgresPLSQL
		String sql = "SELECT tipo_item, tipo_operacao_caixa, edicao_item, nome_concurso, \n" +
				"       data_sorteio, valor_movimentado, observacoes, id_fucionario, \n" +
				"       id_terminal, id_abertura_terminal, quantidade, data_hora_mov\n" +
				"    FROM public.operacoes_funcionario" +
				"    where" +
				"       id = ? AND " +
				"       id_entidade = ?";
		try {
			MyPreparedStatement ps = new MyPreparedStatement(sql,request,false);
			ps.set(this.id);
			ps.set(Parametros.idEntidade);
			ResultSet rs = ps.getPst().executeQuery();
			if (rs.next()) {
				tipoItem = rs.getInt(1);
				tipoOperacaoCaixa = rs.getInt(2);
				edicaoItem = rs.getInt(3);
				nomeConcurso = rs.getString(4);
				dataSorteio = rs.getDate(5);
				valorMovimentado = rs.getBigDecimal(6);
				observacoes = rs.getString(7);
				idFuncionario = rs.getInt(8);
				idTerminal = rs.getInt(9);
				idAberturaTerminal = rs.getInt(10);
				quantidade = rs.getInt(11);
				dataHoraMov = rs.getTimestamp(12);
			}
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
	}

	public VelocityContext getHtml(VelocityContext contextPrinc, VelocityEngine ve, String idS){
		Template templateConteudo;
		VelocityContext contextConteudo;
		StringWriter writerConteudo;
		templateConteudo = ve.getTemplate( "templates/Modern/operador/operacoes.html" , "UTF-8");
		contextConteudo = new VelocityContext();
		writerConteudo = new StringWriter();
		contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());
		contextConteudo.put("opts_padrao", getOptsEdicaoItem());
		contextConteudo.put("id_fun", Parametros.getIdFuncionario());
		contextConteudo.put("id_ter", Parametros.getIdTerminal());
		contextConteudo.put("id_abr", Parametros.getIdAberturaTerminal());
		
		setOpcoesFiltro("operacoes_funcionario");
		templateConteudo.merge( contextConteudo, writerConteudo );
		contextPrinc.put("conteudo", writerConteudo.toString());
		contextPrinc.put("popup", getSWPopup(ve,"operacoes_funcionario").toString());
		return contextPrinc;
	}
	
	public void deleta(){
		getDadosPorId();
		MovimentoCaixa movimentoCaixa = new MovimentoCaixa(request,this);
		if (movimentoCaixa.getId() != null) {
			movimentoCaixa.getDados();
			movimentoCaixa.deleta();
		}
		OutrosMovimentos outrosMovimentos = new OutrosMovimentos(request,this);
		if (outrosMovimentos.getId() != null) {
			outrosMovimentos.getDados();
			outrosMovimentos.deleta();
		}
		//language=PostgresPLSQL
		String sql = "DELETE FROM operacoes_funcionario where id = ? and id_entidade = ?";
		try {
			MyPreparedStatement ps = new MyPreparedStatement(sql,request);
			ps.set(this.id);
			ps.set(Parametros.idEntidade);
			ps.getPst().execute();
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
	}
	
	public void grava(){
		if (this.id>0) {
			altera();
		}else{
			insere();
		}
	}
	
	private void insere(){
		//language=PostgresPLSQL
		String sql = "INSERT INTO public.operacoes_funcionario(\n" +
				"             tipo_item, tipo_operacao_caixa, edicao_item, nome_concurso, \n" +
				"            data_sorteio, valor_movimentado, observacoes, id_fucionario, \n" +
				"            id_terminal, id_abertura_terminal, id_entidade, quantidade)\n" +
				"    VALUES ( ?, ?, ?, ?, \n" +
				"            ?, ?, ?, ?, \n" +
				"            ?, ?, ?, ?);";
		try {
			MyPreparedStatement st = new MyPreparedStatement(sql,request);
			st.set(tipoItem);
			st.set(tipoOperacaoCaixa);
			st.set(edicaoItem);
			st.set(nomeConcurso);
			st.set(dataSorteio);
			if(this.tipoItem != null && this.tipoOperacaoCaixa != null) {
				if (this.tipoItem == 1 && this.tipoOperacaoCaixa == 2) {
					st.set(valorMovimentado.multiply(new BigDecimal(quantidade)));
				}else {
					st.set(valorMovimentado);
				}
			}else {
				st.set(valorMovimentado);
			}
			st.set(observacoes);
			st.set(idFuncionario);
			st.set(idTerminal);
			st.set(idAberturaTerminal);
			st.set(Parametros.idEntidade);
			st.set(quantidade);
			st.getPst().execute();
			ResultSet rs = st.getPst().getGeneratedKeys();
			if (rs.next()) {
				this.id = rs.getInt(1);
				acoesAutomaticas();
			}
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
	}
	
	public void alteraIdItem(){
		//language=PostgresPLSQL
		String sql = "UPDATE operacoes_funcionario set edicao_item = ? where id = ? AND id_entidade = ?";
		try {
			MyPreparedStatement ps = new MyPreparedStatement(sql,request);
			ps.set(tipoItem == 1 ?(edicaoItem+100):edicaoItem);
			ps.set(id);
			ps.set(Parametros.idEntidade);
			ps.getPst().execute();
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
	}
	
	private void altera(){
		String sql = "UPDATE public.operacoes_funcionario\n" +
				"   SET tipo_item=?, tipo_operacao_caixa=?, edicao_item=?, nome_concurso=?, \n" +
				"       data_sorteio=?, valor_movimentado=?, observacoes=?, id_fucionario=?, \n" +
				"       id_terminal=?, id_abertura_terminal=?, quantidade=?" +
				"   WHERE " +
				"       id = ? and id_entidade = ?";
		try {
			MyPreparedStatement ps = new MyPreparedStatement(sql,request);
			ps.set(tipoItem);
			ps.set(tipoOperacaoCaixa);
			ps.set(edicaoItem);
			ps.set(nomeConcurso);
			ps.set(dataSorteio);
			if(this.tipoItem != null && this.tipoOperacaoCaixa != null) {
				if (this.tipoItem == 1 && this.tipoOperacaoCaixa == 2) {
					ps.set(valorMovimentado.multiply(new BigDecimal(quantidade)));
				}else {
					ps.set(valorMovimentado);
				}
			}else {
				ps.set(valorMovimentado);
			}
			ps.set(observacoes);
			ps.set(idFuncionario);
			ps.set(idTerminal);
			ps.set(idAberturaTerminal);
			ps.set(quantidade);
			
			ps.set(this.id);
			ps.set(Parametros.idEntidade);
			ps.getPst().execute();
			acoesAutomaticas();
			
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
	}
	
	private void acoesAutomaticas(){
		switch (tipoItem) {
			case 3:
			case 5:
				MovimentoCaixa movimentoCaixa = new MovimentoCaixa(request, this);
				movimentoCaixa.gravaAutoMov();
				break;
			default:
				OutrosMovimentos outrosMovimentos = new OutrosMovimentos(request,this);
				outrosMovimentos.gravaAutoMov();
				break;
		}
	}
	
	public String getOptsEdicaoItem(){
		StringBuilder sb = new StringBuilder("<option value=\"0\" >Selecione</option>\n");
		if (tipoItem==null) {
			tipoItem = 1;
		}
		switch (tipoItem){
			case 1:
				sb.append(getOptsSorteio(3)).append(getOptionsBolao());
				break;
			case 2:
				sb.append(getOptsSorteio(5));
				break;
			case 4:
				sb.append(getOptsSorteio(4));
				break;
			case 5:
				sb.append(getOpts(2));
				break;
			case 6:
				sb.append(getOpts(6)).append(getOpts(7));
				break;
		}
		return sb.toString();
	}
	
	private String getOptionsBolao(){
		String options =
				"        <option value=\"1\" >MEGA-SENA</option>\n" +
				"        <option value=\"3\" >QUINA</option>\n" +
				"        <option value=\"5\" >LOTOFÁCIL</option>\n" +
				"        <option value=\"6\" >LOTOMANIA</option>\n" +
				"        <option value=\"7\" >TIMEMANIA</option>\n" +
				"        <option value=\"8\" >DUPLA SENA</option>\n" +
				"        <option value=\"9\" >LOTECA</option>\n" +
				"        <option value=\"10\" >LOTOGOL</option>\n" +
				"        <option value=\"11\" >Outros</option>";
		
		return options;
	}
	
	private String getOpts(Integer tipo){
		String sql = "SELECT id,nome_item FROM itens_estoque where  tipo_item = ? and id_entidade = ? ";
		try {
			MyPreparedStatement ps = new MyPreparedStatement(sql,request,false);
			ps.set(tipo);
			ps.set(Parametros.idEntidade);
			ResultSet rs = ps.getPst().executeQuery();
			List<Element> elements = new ArrayList<>();
			while (rs.next()) {
				elements.add(getOption(rs.getInt(1),rs.getString(2)));
			}
			return StringUtils.join(elements,"\n");
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
		return "";
	}
	private String getOptsSorteio(Integer tipo){
		//language=PostgresPLSQL
		String sql = "SELECT case when tipo_item = 3 then id+100 else id END , nome_item ||' - '|| to_char(data_sorteio, 'DD/MM/YYYY') FROM itens_estoque where data_sorteio >= ? and tipo_item = ? and id_entidade = ? ";
		try {
			MyPreparedStatement ps = new MyPreparedStatement(sql,request,false);
			ps.set(new Date(new java.util.Date().getTime()));
			ps.set(tipo);
			ps.set(Parametros.idEntidade);
			ResultSet rs = ps.getPst().executeQuery();
			List<Element> elements = new ArrayList<>();
			while (rs.next()) {
				elements.add(getOption(rs.getInt(1),rs.getString(2)));
			}
			return StringUtils.join(elements,"\n");
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
		return "";
	}
	
	public Integer getIdOutrosMovimentos(){
		//language=PostgresPLSQL
		String sql = "select id from outros_movimentos where id_operacao_funcionario = ? AND  id_entidade = ?";
		try {
			MyPreparedStatement ps = new MyPreparedStatement(sql,request,false);
			ps.set(this.id);
			ps.set(Parametros.idEntidade);
			ResultSet rs = ps.getPst().executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
		return null;
	}
	
	private Element getOption(Integer value, String texto){
		Element element = new Element("option");
		element.attr("value",value.toString());
		element.html(texto);
		return element;
	}
	
	public void setTipoItem(Integer tipoItem) {
		this.tipoItem = tipoItem;
	}
	
	public Integer getTipoOperacaoCaixa() {
		return tipoOperacaoCaixa;
	}
	
	public void setTipoOperacaoCaixa(Integer tipoOperacaoCaixa) {
		this.tipoOperacaoCaixa = tipoOperacaoCaixa;
	}
	
	public Integer getTipoItem() {
		return tipoItem;
	}
	
	public Integer getEdicaoItem() {
		return edicaoItem;
	}
	
	public Integer getIdFuncionario() {
		return idFuncionario;
	}
	
	public Integer getIdTerminal() {
		return idTerminal;
	}
	
	public Integer getIdAberturaTerminal() {
		return idAberturaTerminal;
	}
	
	public Integer getQuantidade() {
		return quantidade;
	}
	
	public String getNomeConcurso() {
		return nomeConcurso;
	}
	
	public String getObservacoes() {
		return observacoes;
	}
	
	public Date getDataSorteio() {
		return dataSorteio;
	}
	
	public BigDecimal getValorMovimentado() {
		return valorMovimentado;
	}
	
	public void setEdicaoItem(Integer edicaoItem) {
		this.edicaoItem = edicaoItem;
	}
}
