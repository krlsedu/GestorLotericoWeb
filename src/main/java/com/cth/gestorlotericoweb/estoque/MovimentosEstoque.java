package com.cth.gestorlotericoweb.estoque;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.operador.Operacoes;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.processos.MovimentoCaixa;
import com.cth.gestorlotericoweb.processos.OutrosMovimentos;
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
import java.sql.Timestamp;

public class MovimentosEstoque extends Estoque {
	
	Integer idItensEstoque;
	Integer idLoterica;
	Integer idFuncionario;
	
	private Integer numeroVolumes;
	private Integer idMovimentoCaixa;
	Integer idOutrosMovimentos;
	
	Integer tipoOperacao;
	Integer tipoOperacaoAnt;
	
	String observacoes;
	
	BigDecimal quantidadeMovimentada;
	BigDecimal numeroVolumesBd;
	BigDecimal qtdTotalMovimentada;
	
	Timestamp dataHoraReferencia;
	
	OutrosMovimentos  outrosMovimentos;
	
	public MovimentosEstoque(HttpServletRequest request) {
		super(request);
	}
	
	public MovimentosEstoque(MovimentoCaixa movimentoCaixa){
		super(movimentoCaixa.getRequest());
		this.id = getIdMovimentoEstoque(movimentoCaixa);
		
		this.idMovimentoCaixa = movimentoCaixa.getId();
		this.idLoterica = Parametros.getIdLoterica();
		Itens itens = new Itens(this.request);
		itens.idLoterica = this.idLoterica;
		this.idItensEstoque = itens.getIdItensEstoque(movimentoCaixa.getTipoMoeda(),false);
		this.idFuncionario = movimentoCaixa.idFuncionario;
		this.numeroVolumes = 1;
		
		if (movimentoCaixa.getTipoOperacao()==2) {
			this.tipoOperacao = 2;
		}else {
			this.tipoOperacao = 1;
		}
		this.observacoes = "Movimento gerado automaticamente pela rotina de movimento de caixa";
		
		this.quantidadeMovimentada = movimentoCaixa.getValorMovimentado();
		this.numeroVolumesBd = Parser.toBigDecimalFromSt("1");
		this.tipoOperacaoAnt = movimentoCaixa.getTipoOperacaoAnt();
		
		this.qtdTotalMovimentada = this.quantidadeMovimentada.multiply(this.numeroVolumesBd);
		if (!(this.tipoOperacao==1||this.tipoOperacao==3)) {
			this.qtdTotalMovimentada = this.qtdTotalMovimentada.multiply(BigDecimal.valueOf(-1L));
		}
		this.dataHoraReferencia = Parser.toDbTimeStamp(movimentoCaixa.getDataHoraMovProc());
	}
	
	public MovimentosEstoque(HttpServletRequest request, OutrosMovimentos outrosMovimentos){
		super(request);
		this.outrosMovimentos = outrosMovimentos;
		
		this.idOutrosMovimentos = outrosMovimentos.getId();
		this.idLoterica = Parametros.getIdLoterica();
		Itens itens = new Itens(this.request);
		itens.idLoterica = this.idLoterica;
		
		if ((!outrosMovimentos.getEntrada()&&this.outrosMovimentos.getOperacoes().getTipoOperacaoCaixa()<=3)||this.outrosMovimentos.getOperacoes().getTipoOperacaoCaixa()==2) {
			this.tipoOperacao = 1;
		}else {
			this.tipoOperacao = 2;
		}
		this.idItensEstoque = itens.getIdItensEstoque(this.outrosMovimentos.getIdEdicaoItem(),true);
		this.id = getIdMovimentoEstoque(outrosMovimentos);
		
		
		if (this.idItensEstoque == null && this.outrosMovimentos.getOperacoes().getTipoOperacaoCaixa()==2) {
			itens = new Itens(this);
			itens.insere();
			this.idItensEstoque = itens.id;
		}
		this.outrosMovimentos.setIdEdicaoItem(this.idItensEstoque);
		this.outrosMovimentos.getOperacoes().alteraIdItem();
		
		this.idFuncionario = this.outrosMovimentos.getIdFuncionario();
		this.numeroVolumes = this.outrosMovimentos.getQuantidade();
		
		this.observacoes = "Movimento gerado automaticamente pela rotina de operações do funcionário";
		
		if (itens.unidade == 2) {
			this.quantidadeMovimentada = outrosMovimentos.getValorMovimentado();
			this.numeroVolumesBd = new BigDecimal(numeroVolumes);
		}else {
			Integer qtd = outrosMovimentos.getOperacoes().getQuantidade();
			if (qtd == null) {
				qtd = 1;
			}
			this.quantidadeMovimentada = new BigDecimal(qtd);
			this.numeroVolumes = 1;
			this.numeroVolumesBd = new BigDecimal(numeroVolumes);
		}
		this.tipoOperacaoAnt = outrosMovimentos.getTipoOperacaoAnt();
		
		this.qtdTotalMovimentada = this.quantidadeMovimentada.multiply(this.numeroVolumesBd);
		if (!(this.tipoOperacao==1||this.tipoOperacao==3)) {
			this.qtdTotalMovimentada = this.qtdTotalMovimentada.multiply(BigDecimal.valueOf(-1L));
		}
		this.dataHoraReferencia = outrosMovimentos.getDataHoraMov();
	}
	
	public void setMovimentacao(){
		this.id = Parser.toInteger(request.getParameter("id"));
		idItensEstoque = Parser.toInteger(request.getParameter("id_itens_estoque"));
		idLoterica = Parser.toIntegerNull(request.getParameter("id_loterica"));
		idFuncionario = Parser.toIntegerNull(request.getParameter("id_funcionario"));
		numeroVolumes = 1;
		
		tipoOperacao = Parser.toIntegerNull(request.getParameter("tipo_movimento"));
		observacoes = request.getParameter("observacoes");
		
		quantidadeMovimentada = Parser.toBigDecimalFromHtml(request.getParameter("quantidade_movimentada"));
		numeroVolumesBd = Parser.toBigDecimalFromSt("1");
		qtdTotalMovimentada = quantidadeMovimentada.multiply(numeroVolumesBd);
		
		if (!(tipoOperacao == 1||tipoOperacao == 3 )) {
			qtdTotalMovimentada = qtdTotalMovimentada.multiply(BigDecimal.valueOf(-1L));
		}
		
		dataHoraReferencia = Parser.toDbTimeStamp(request.getParameter("data_hora_mov"));
	}
	
	private void getDadosBd(){
		String sql = "SELECT  tipo_movimento, id_itens_estoque, quantidade_movimentada, \n" +
				"       id_loterica, numero_volumes, observacoes, \n" +
				"       data_hora_mov, id_funcionario, id_movimento_caixa, id_outros_movimentos\n" +
				"  FROM movimentos_estoque" +
				"  where " +
				"   id = ? AND " +
				"   id_entidade = ? ";
		try {
			PreparedStatement ps = Parametros.getConexao().getPst(sql,false);
			ps.setInt(1,this.id);
			ps.setInt(2,Parametros.idEntidade);
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				tipoOperacao = rs.getInt(1);
				idItensEstoque = rs.getInt(2);
				quantidadeMovimentada = rs.getBigDecimal(3);
				idLoterica = rs.getInt(4);
				numeroVolumesBd = rs.getBigDecimal(5);
				numeroVolumes = rs.getInt(5);
				observacoes = rs.getString(6);
				dataHoraReferencia = rs.getTimestamp(7);
				idFuncionario =rs.getInt(8);
				idMovimentoCaixa =rs.getInt(9);
				idOutrosMovimentos = rs.getInt(10);
				qtdTotalMovimentada = quantidadeMovimentada.multiply(numeroVolumesBd);
				
				if (!(tipoOperacao==1 ||tipoOperacao == 3 )) {
					qtdTotalMovimentada = qtdTotalMovimentada.multiply(BigDecimal.valueOf(-1L));
				}
			}
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
	}
	
	public void grava(){
		if("0".equals(request.getParameter("id"))){
			insere();
		}else{
			altera();
		}
		gravaSaldoEstoque();
		gravaSaldoEstoqueFuncionario();
	}
	
	public void gravaAutoMov(){
		if(this.id==0){
			insere();
		}else{
			altera();
		}
		gravaSaldoEstoque();
		gravaSaldoEstoqueFuncionario();
	}
	
	private void gravaSaldoEstoque(){
		if(outrosMovimentos == null) {
			SaldoEstoque saldoEstoque = new SaldoEstoque(request);
			saldoEstoque.grava(this);
		}else {
			if (outrosMovimentos.getOperacoes().getTipoOperacaoCaixa() <=3) {
				SaldoEstoque saldoEstoque = new SaldoEstoque(request);
				saldoEstoque.grava(this);
			}
		}
	}
	
	private void gravaSaldoEstoqueFuncionario(){
		boolean entra;
		switch (request.getParameter("it")){
			case "movimentos_caixas":
				entra = true;
				break;
			case "operacoes_funcionario":
				if(outrosMovimentos == null){
					entra = true;
				}else {
					if (outrosMovimentos.getOperacoes().getTipoOperacaoCaixa()==2 || outrosMovimentos.getOperacoes().getTipoOperacaoCaixa()>3) {
						entra = false;
					}else {
						entra = true;
					}
				}
				break;
			default:
				entra = false;
				break;
		}
		if (this.idFuncionario != null) {
			if (entra){
				if(this.tipoOperacaoAnt==null){
					this.tipoOperacaoAnt = this.tipoOperacao;
				}
				if(this.tipoOperacao == 1 ){
					if (this.tipoOperacaoAnt.equals(tipoOperacao)) {
						this.tipoOperacao = 2;
					}else {
						this.tipoOperacao = 1;
					}
				}else {
					if (this.tipoOperacaoAnt.equals(tipoOperacao)) {
						this.tipoOperacao = 1;
					}else {
						this.tipoOperacao = 2;
					}
				}
			}
			if(this.outrosMovimentos != null){
				if (this.outrosMovimentos.getOperacoes().getTipoOperacaoCaixa()==2) {
					Operacoes opr = this.outrosMovimentos.getOperacoes();
					opr.setTipoOperacaoCaixa(1);
					this.outrosMovimentos.setOperacoes(opr);
					this.outrosMovimentos.setIdEdicaoItem(this.outrosMovimentos.getIdEdicaoItem()+100);
					MovimentosEstoque movimentosEstoque = new MovimentosEstoque(request,this.outrosMovimentos);
					movimentosEstoque.gravaAutoMov();
				}else {
					SaldoEstoqueFuncionario saldoEstoqueFuncionario = new SaldoEstoqueFuncionario(request);
					saldoEstoqueFuncionario.grava(this);
				}
			}else {
				SaldoEstoqueFuncionario saldoEstoqueFuncionario = new SaldoEstoqueFuncionario(request);
				saldoEstoqueFuncionario.grava(this);
			}
		}
	}
	
	private void insere(){
		try {
			PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO public.movimentos_estoque(\n" +
					"            tipo_movimento, id_itens_estoque, quantidade_movimentada, \n" +
					"             numero_volumes, data_hora_mov, observacoes,\n" +
					"             id_funcionario,id_loterica, id_movimento_caixa, id_entidade, id_outros_movimentos)\n" +
					"    VALUES ( ?, ?, ?, " +
					"            ?, ?, ?, " +
					"             ?, ?, ?, ?, ? )");
			ps.setInt(1, tipoOperacao);
			ps.setInt(2,idItensEstoque);
			ps.setBigDecimal(3,quantidadeMovimentada);
			ps.setInt(4,numeroVolumes);
			ps.setTimestamp(5,dataHoraReferencia);
			
			ps = Seter.set(ps,6,observacoes);
			ps = Seter.set(ps,7,idFuncionario);
			ps = Seter.set(ps,8,idLoterica);
			ps = Seter.set(ps,9,idMovimentoCaixa);
			ps.setInt(10, Parametros.idEntidade);
			ps = Seter.set(ps,11,idOutrosMovimentos);
			ps.execute();
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()){
				id = rs.getInt(1);
			}
			
		} catch (SQLException ex) {
			new LogError(ex.getMessage(), ex,request);
		}
	}
	
	private void altera(){
		String idL;
		if(this.id==null) {
			idL = request.getParameter("id");
		}else{
			idL = this.id.toString();
		}
		try {
			PreparedStatement ps = Parametros.getConexao(request).getPst("UPDATE public.movimentos_estoque\n" +
					"   SET  tipo_movimento=?, id_itens_estoque=?, quantidade_movimentada=?, \n" +
					"       numero_volumes=?,data_hora_mov=?, observacoes=?, \n" +
					"        id_funcionario = ?, id_loterica=?, id_outros_movimentos = ? \n" +
					" where id = ? and id_entidade = ? ", false);
			ps.setInt(1, tipoOperacao);
			ps.setInt(2,idItensEstoque);
			ps.setBigDecimal(3,quantidadeMovimentada);
			ps.setInt(4,numeroVolumes);
			ps.setTimestamp(5,dataHoraReferencia);
			
			ps = Seter.set(ps,6,observacoes);
			ps = Seter.set(ps,7,idFuncionario);
			ps = Seter.set(ps,8,idLoterica);
			ps = Seter.set(ps,9,idOutrosMovimentos);
			
			id = Integer.valueOf(idL);
			ps.setInt(10, id);
			ps.setInt(11, Parametros.idEntidade);
			
			ps.execute();
		} catch (SQLException ex) {
			new LogError(ex.getMessage(), ex,request);
		}
	}
	
	public void deleta(){
		MovimentosEstoque movimentosEstoque = new MovimentosEstoque(request);
		movimentosEstoque.id = this.id;
		movimentosEstoque.getDadosBd();
		try{
			SaldoEstoque saldos = new SaldoEstoque(request);
			saldos.ajustaSaldoAntesDeletarMovimento(movimentosEstoque);
			if (movimentosEstoque.idFuncionario!=null) {
				SaldoEstoqueFuncionario saldoEstoqueFuncionario = new SaldoEstoqueFuncionario(request);
				saldoEstoqueFuncionario.ajustaSaldoAntesDeletarMovimento(movimentosEstoque);
			}
			PreparedStatement ps = Parametros.getConexao(request).getPst("delete from movimentos_estoque where id = ? and id_entidade = ?", Boolean.FALSE);
			ps.setInt(1, id);
			ps.setInt(2, Parametros.idEntidade);
			ps.execute();
		}catch (SQLException ex) {
			new LogError(ex.getMessage(), ex,request);
		}
	}
	
	public VelocityContext getHtml(VelocityContext contextPrinc, VelocityEngine ve, String idS){
		Template templateConteudo;
		VelocityContext contextConteudo;
		StringWriter writerConteudo;
		templateConteudo = ve.getTemplate("templates/Modern/estoque/movimentos_estoque.html", "UTF-8");
		contextConteudo = new VelocityContext();
		writerConteudo = new StringWriter();
		contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());
		setOpcoesFiltro("movimentos_estoque");
		templateConteudo.merge( contextConteudo, writerConteudo );
		contextPrinc.put("conteudo", writerConteudo.toString());
		contextPrinc.put("popup", getSWPopup(ve,"movimentos_estoque").toString());
		return contextPrinc;
	}
	
	private Integer getIdMovimentoEstoque(MovimentoCaixa movimentoCaixa){
		try{
			PreparedStatement ps = Parametros.getConexao().getPst("select id from movimentos_estoque where id_movimento_caixa = ? and id_entidade = ?",false);
			ps.setInt(1, movimentoCaixa.getId());
			ps.setInt(2, Parametros.idEntidade);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		}catch(SQLException ex){
			new LogError(ex.getMessage(), ex,request);
		}
		return 0;
	}
	
	private Integer getIdMovimentoEstoque(OutrosMovimentos outrosMovimentos){
		try{
			PreparedStatement ps = Parametros.getConexao().getPst("select id from movimentos_estoque where id_outros_movimentos = ? and tipo_movimento = ? and id_entidade = ?",false);
			ps.setInt(1, outrosMovimentos.getId());
			ps.setInt(2, this.tipoOperacao);
			ps.setInt(3, Parametros.idEntidade);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		}catch(SQLException ex){
			new LogError(ex.getMessage(), ex,request);
		}
		return 0;
	}
	
	public OutrosMovimentos getOutrosMovimentos() {
		return outrosMovimentos;
	}
}
