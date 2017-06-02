package com.cth.gestorlotericoweb.estoque;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.utils.Seter;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Saldo extends Estoque {
	public Saldo(HttpServletRequest request) {
		super(request);
	}
	
	public void grava(MovimentosEstoque movimentosEstoque){
		id = getIdSaldo(movimentosEstoque);
		if(id==0){
			insere(movimentosEstoque);
		}else{
			update(movimentosEstoque);
		}
	}
	
	private void insere(MovimentosEstoque movimentosEstoque){
		try {
			PreparedStatement ps = Parametros.getConexao().getPst("INSERT INTO public.saldos_estoque(\n" +
					"            id_itens_estoque, quantidade_movimentada, saldo, data_hora_saldo, \n" +
					"             id_movimento, observacoes, id_loterica, id_entidade)\n" +
					"    VALUES ( ?, ?, ?, ?, \n" +
					"            ?, ?, ?, ?);",false);
			PreparedStatement psRs = Parametros.getConexao().getPst("select saldo from saldos_estoque where id_itens_estoque = ? and id_entidade = ? order by data_hora_movimento desc limit 1",false);
			psRs.setInt(1, movimentosEstoque.idItensEstoque);
			psRs.setInt(2, Parametros.idEntidade);
			ResultSet rs = psRs.executeQuery();
			BigDecimal saldoAtual;
			if(rs.next()){
				saldoAtual = rs.getBigDecimal(1);
			}else{
				saldoAtual = BigDecimal.ZERO;
			}
			BigDecimal saldo;
			BigDecimal qtdMov;
			if(movimentosEstoque.tipoOperacao.equals("1")|| movimentosEstoque.tipoOperacao.equals("3")){
				qtdMov = movimentosEstoque.quantidadeMovimentada.multiply(movimentosEstoque.numeroVolumesBd);
				saldo = saldoAtual.add(qtdMov);
			}else{
				qtdMov = movimentosEstoque.quantidadeMovimentada.multiply(movimentosEstoque.numeroVolumesBd);
				saldo = saldoAtual.subtract(qtdMov);
			}
			
			ps.setInt(1, movimentosEstoque.idItensEstoque);
			ps.setBigDecimal(2, qtdMov);
			ps.setBigDecimal(3, saldo);
			ps.setTimestamp(4, movimentosEstoque.dataHoraReferencia);
			ps.setInt(5, movimentosEstoque.id);
			ps = Seter.set(ps,6, movimentosEstoque.observacoes);
			ps = Seter.set(ps,7, movimentosEstoque.idLoterica);
			ps.setInt(8, Parametros.idEntidade);
			ps.execute();
		} catch (SQLException ex) {
			new LogError(ex.getMessage(), ex,request);
		}
		
	}
	
	private void update(MovimentosEstoque movimentosEstoque){
		try{
			PreparedStatement psRs = Parametros.getConexao().getPst("select quantidade_movimentada from saldos_estoque where id_itens_estoque = ? and id = ?  and id_entidade = ? ",false);
			psRs.setInt(1, movimentosEstoque.idItensEstoque);
			psRs.setInt(2, id);
			psRs.setInt(3, Parametros.idEntidade);
			ResultSet rs = psRs.executeQuery();
			BigDecimal saldoAtual = BigDecimal.ZERO;
			BigDecimal valorMovAtu = BigDecimal.ZERO;
			if(rs.next()){
				valorMovAtu  = rs.getBigDecimal(1);
			}
			BigDecimal novoValor = movimentosEstoque.quantidadeMovimentada.multiply(movimentosEstoque.numeroVolumesBd);
			if(valorMovAtu.compareTo(novoValor)!=0) {
				PreparedStatement psAlt = Parametros.getConexao().getPst("UPDATE saldos_estoque set quantidade_movimentada = ? where id_itens_estoque = ? and id = ?  and id_entidade = ? ",false);
				psAlt.setBigDecimal(1,novoValor);
				psAlt.setInt(2, movimentosEstoque.idItensEstoque);
				psAlt.setInt(3, id);
				psAlt.setInt(4, Parametros.idEntidade);
				psAlt.execute();
				BigDecimal difMov = novoValor.subtract(valorMovAtu);
				PreparedStatement ps = Parametros.getConexao().getPst("INSERT INTO public.saldos_estoque(\n" +
						"            id_itens_estoque, quantidade_movimentada, saldo, data_hora_saldo, \n" +
						"             id_movimento, observacoes, id_loterica, id_entidade)\n" +
						"    VALUES ( ?, ?, ?, ?, \n" +
						"            ?, ?, ?, ?);", false);
				psRs = Parametros.getConexao().getPst("select saldo from saldos_estoque where id_itens_estoque = ? and id_entidade = ? order by data_hora_movimento desc limit 1",false);
				psRs.setInt(1, movimentosEstoque.idItensEstoque);
				psRs.setInt(2, Parametros.idEntidade);
				rs = psRs.executeQuery();
				if (rs.next()) {
					saldoAtual = rs.getBigDecimal(1);
				}
				BigDecimal saldo;
				if (movimentosEstoque.tipoOperacao.equals("1") || movimentosEstoque.tipoOperacao.equals("3")) {
					saldo = saldoAtual.add(difMov);
				} else {
					saldo = saldoAtual.subtract(difMov);
				}
				ps.setInt(1, movimentosEstoque.idItensEstoque);
				ps.setBigDecimal(2, difMov);
				ps.setBigDecimal(3, saldo);
				ps.setTimestamp(4, movimentosEstoque.dataHoraReferencia);
				ps.setInt(5, movimentosEstoque.id);
				ps = Seter.set(ps,6, movimentosEstoque.observacoes);
				ps = Seter.set(ps,7, movimentosEstoque.idLoterica);
				ps.setInt(8, Parametros.idEntidade);
				
				ps.execute();
			}
			
		}catch(SQLException ex) {
			new LogError(ex.getMessage(), ex,request);
		}
	}
	
	
	public void ajustaSaldoAntesDeletarMovimento(MovimentosEstoque movimentosEstoque){
		//language=PostgresPLSQL
		this.id =getIdSaldo(movimentosEstoque);
		String sql = "UPDATE " +
				"           saldos_estoque " +
				"       set " +
				"           saldo = saldo - ?" +
				"       where " +
				"           id > ? AND " +
				"           id_itens_estoque = ? AND " +
				"           id_loterica = ? AND" +
				"           id_entidade = ?";
		try {
			PreparedStatement ps = Parametros.getConexao().getPst(sql);
			ps.setBigDecimal(1,movimentosEstoque.qtdTotalMovimentada);
			ps.setInt(2,id);
			ps.setInt(3,movimentosEstoque.idItensEstoque);
			ps = Seter.set(ps,4,movimentosEstoque.idLoterica);
			ps.setInt(5,Parametros.idEntidade);
			ps.execute();
			
			sql = "DELETE FROM " +
					"   saldos_estoque " +
					"WHERE " +
					"   id_movimento = ? AND " +
					"   id_loterica = ? AND " +
					"   id_entidade = ?";
			ps = Parametros.getConexao().getPst(sql);
			ps.setInt(1,movimentosEstoque.id);
			ps = Seter.set(ps,2,movimentosEstoque.idLoterica);
			ps.setInt(3,Parametros.idEntidade);
			ps.execute();
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
	}
	
	private Integer getIdSaldo(MovimentosEstoque movimentosEstoque){
		try{
			PreparedStatement ps = Parametros.getConexao().getPst("select id from saldos_estoque where id_movimento = ? and id_entidade = ? ORDER by id",false);
			ps.setInt(1, movimentosEstoque.id);
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
}
