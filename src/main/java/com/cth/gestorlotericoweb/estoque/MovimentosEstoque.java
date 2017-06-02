package com.cth.gestorlotericoweb.estoque;

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
import java.sql.Timestamp;

public class MovimentosEstoque extends Estoque {
	
	Integer idItensEstoque;
	Integer idLoterica;
	Integer numeroVolumes;
	
	String tipoOperacao;
	String observacoes;
	
	BigDecimal quantidadeMovimentada;
	BigDecimal numeroVolumesBd;
	BigDecimal qtdTotalMovimentada;
	
	
	Timestamp dataHoraReferencia;
	
	public MovimentosEstoque(HttpServletRequest request) {
		super(request);
	}
	
	public void setMovimentacao(){
		idItensEstoque = Parser.toInteger(request.getParameter("id_itens_estoque"));
		idLoterica = Parser.toIntegerNull(request.getParameter("id_loterica"));
		numeroVolumes = 1;
		
		tipoOperacao = request.getParameter("tipo_movimento");
		observacoes = request.getParameter("observacoes");
		
		quantidadeMovimentada = Parser.toBigDecimalFromHtml(request.getParameter("quantidade_movimentada"));
		numeroVolumesBd = Parser.toBigDecimalFromSt("1");
		qtdTotalMovimentada = quantidadeMovimentada.multiply(numeroVolumesBd);
		
		if (!(tipoOperacao.equals("1")||tipoOperacao.equals("3"))) {
			qtdTotalMovimentada = qtdTotalMovimentada.multiply(BigDecimal.valueOf(-1L));
		}
		
		dataHoraReferencia = Parser.toDbTimeStamp(request.getParameter("data_hora_mov"));
	}
	
	public void getDadosBd(){
		String sql = "SELECT  tipo_movimento, id_itens_estoque, quantidade_movimentada, \n" +
				"       id_loterica, numero_volumes, observacoes, \n" +
				"       data_hora_mov\n" +
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
				tipoOperacao = rs.getString(1);
				idItensEstoque = rs.getInt(2);
				quantidadeMovimentada = rs.getBigDecimal(3);
				idLoterica = rs.getInt(4);
				numeroVolumesBd = rs.getBigDecimal(5);
				numeroVolumes = rs.getInt(5);
				observacoes = rs.getString(6);
				dataHoraReferencia = rs.getTimestamp(7);
				qtdTotalMovimentada = quantidadeMovimentada.multiply(numeroVolumesBd);
				
				if (!(tipoOperacao.equals("1")||tipoOperacao.equals("3"))) {
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
		Saldo saldo = new Saldo(request);
		saldo.grava(this);
	}
	
	private void insere(){
		try {
			PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO public.movimentos_estoque(\n" +
					"            tipo_movimento, id_itens_estoque, quantidade_movimentada, \n" +
					"             numero_volumes, data_hora_mov, observacoes,\n" +
					"             id_loterica, id_entidade)\n" +
					"    VALUES ( ?, ?, ?, \n" +
					"            ?, ?, ?, \n" +
					"              ?, ? )");
			ps.setInt(1, Integer.valueOf(tipoOperacao));
			ps.setInt(2,idItensEstoque);
			ps.setBigDecimal(3,quantidadeMovimentada);
			ps.setInt(4,numeroVolumes);
			ps.setTimestamp(5,dataHoraReferencia);
			
			ps = Seter.set(ps,6,observacoes);
			ps = Seter.set(ps,7,idLoterica);
			ps.setInt(8, Parametros.idEntidade);
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
					"        id_loterica=? \n" +
					" where id = ? and id_entidade = ? ", false);
			ps.setInt(1, Integer.valueOf(tipoOperacao));
			ps.setInt(2,idItensEstoque);
			ps.setBigDecimal(3,quantidadeMovimentada);
			ps.setInt(4,numeroVolumes);
			ps.setTimestamp(5,dataHoraReferencia);
			
			ps = Seter.set(ps,6,observacoes);
			ps = Seter.set(ps,7,idLoterica);
			
			id = Integer.valueOf(idL);
			ps.setInt(8, id);
			ps.setInt(9, Parametros.idEntidade);
			
			ps.execute();
		} catch (SQLException ex) {
			new LogError(ex.getMessage(), ex,request);
		}
	}
	
	public void deleta(){
		this.id = Parser.toInteger(request.getParameter("id"));
		MovimentosEstoque movimentosEstoque = new MovimentosEstoque(request);
		movimentosEstoque.id = this.id;
		movimentosEstoque.getDadosBd();
		try{
			Saldo saldos = new Saldo(request);
			saldos.ajustaSaldoAntesDeletarMovimento(movimentosEstoque);
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
}
