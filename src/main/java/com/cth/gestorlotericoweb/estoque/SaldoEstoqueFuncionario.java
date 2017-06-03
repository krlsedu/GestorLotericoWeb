package com.cth.gestorlotericoweb.estoque;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.utils.Seter;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SaldoEstoqueFuncionario extends Estoque {
	public SaldoEstoqueFuncionario(HttpServletRequest request) {
		super(request);
	}
	public void grava(MovimentosEstoque movimentosEstoqueFuncionario){
		id = getIdSaldoFuncionario(movimentosEstoqueFuncionario);
		if(id==0){
			insere(movimentosEstoqueFuncionario);
		}else{
			update(movimentosEstoqueFuncionario);
		}
	}
	
	private void insere(MovimentosEstoque movimentosEstoqueFuncionario){
		try {
			PreparedStatement psRs = Parametros.getConexao().getPst("select saldo from saldos_estoque_funcionario where id_itens_estoque = ? and id_entidade = ? and id_funcionario = ? order by data_hora_movimento desc limit 1",false);
			psRs.setInt(1, movimentosEstoqueFuncionario.idItensEstoque);
			psRs.setInt(2, Parametros.idEntidade);
			psRs.setInt(3, movimentosEstoqueFuncionario.idFuncionario);
			ResultSet rs = psRs.executeQuery();
			BigDecimal saldoAtual;
			if(rs.next()){
				saldoAtual = rs.getBigDecimal(1);
			}else{
				saldoAtual = BigDecimal.ZERO;
			}
			BigDecimal saldo;
			BigDecimal qtdMov;
			if(movimentosEstoqueFuncionario.tipoOperacao.equals("1")|| movimentosEstoqueFuncionario.tipoOperacao.equals("3")){
				qtdMov = movimentosEstoqueFuncionario.quantidadeMovimentada.multiply(movimentosEstoqueFuncionario.numeroVolumesBd);
				saldo = saldoAtual.add(qtdMov);
			}else{
				qtdMov = movimentosEstoqueFuncionario.quantidadeMovimentada.multiply(movimentosEstoqueFuncionario.numeroVolumesBd);
				saldo = saldoAtual.subtract(qtdMov);
			}
			
			PreparedStatement ps = Parametros.getConexao().getPst("INSERT INTO public.saldos_estoque_funcionario(\n" +
					"            id_itens_estoque, quantidade_movimentada, saldo, data_hora_saldo, \n" +
					"             id_movimento, observacoes, id_loterica, id_entidade, id_funcionario)\n" +
					"    VALUES ( ?, ?, ?, ?, \n" +
					"            ?, ?, ?, ?, ?);",false);
			//teste
			ps.setInt(1, movimentosEstoqueFuncionario.idItensEstoque);
			ps.setBigDecimal(2, qtdMov);
			ps.setBigDecimal(3, saldo);
			ps.setTimestamp(4, movimentosEstoqueFuncionario.dataHoraReferencia);
			ps.setInt(5, movimentosEstoqueFuncionario.id);
			ps = Seter.set(ps,6, movimentosEstoqueFuncionario.observacoes);
			ps = Seter.set(ps,7, movimentosEstoqueFuncionario.idLoterica);
			ps.setInt(8, Parametros.idEntidade);
			ps.setInt(9,movimentosEstoqueFuncionario.idFuncionario);
			ps.execute();
		} catch (SQLException ex) {
			new LogError(ex.getMessage(), ex,request);
		}
		
	}
	
	private void update(MovimentosEstoque movimentosEstoqueFuncionario){
		try{
			PreparedStatement psRs = Parametros.getConexao().getPst("select quantidade_movimentada from saldos_estoque_funcionario where id_itens_estoque = ? and id = ?  and id_entidade = ? and id_funcionario = ?",false);
			psRs.setInt(1, movimentosEstoqueFuncionario.idItensEstoque);
			psRs.setInt(2, id);
			psRs.setInt(3, Parametros.idEntidade);
			psRs.setInt(4,movimentosEstoqueFuncionario.idFuncionario);
			ResultSet rs = psRs.executeQuery();
			BigDecimal saldoAtual = BigDecimal.ZERO;
			BigDecimal valorMovAtu = BigDecimal.ZERO;
			if(rs.next()){
				valorMovAtu  = rs.getBigDecimal(1);
			}
			BigDecimal novoValor = movimentosEstoqueFuncionario.quantidadeMovimentada.multiply(movimentosEstoqueFuncionario.numeroVolumesBd);
			if(valorMovAtu.compareTo(novoValor)!=0) {
				PreparedStatement psAlt = Parametros.getConexao().getPst("UPDATE saldos_estoque_funcionario set quantidade_movimentada = ? where id_itens_estoque = ? and id = ?  and id_entidade = ? and id_funcionario = ?",false);
				psAlt.setBigDecimal(1,novoValor);
				psAlt.setInt(2, movimentosEstoqueFuncionario.idItensEstoque);
				psAlt.setInt(3, id);
				psAlt.setInt(4, Parametros.idEntidade);
				psAlt.setInt(5,movimentosEstoqueFuncionario.idFuncionario);
				psAlt.execute();
				BigDecimal difMov = novoValor.subtract(valorMovAtu);
				
				psRs = Parametros.getConexao().getPst("select saldo from saldos_estoque where id_itens_estoque = ? and id_entidade = ? order by data_hora_movimento desc limit 1",false);
				psRs.setInt(1, movimentosEstoqueFuncionario.idItensEstoque);
				psRs.setInt(2, Parametros.idEntidade);
				rs = psRs.executeQuery();
				if (rs.next()) {
					saldoAtual = rs.getBigDecimal(1);
				}
				BigDecimal saldo;
				if (movimentosEstoqueFuncionario.tipoOperacao.equals("1") || movimentosEstoqueFuncionario.tipoOperacao.equals("3")) {
					saldo = saldoAtual.add(difMov);
				} else {
					saldo = saldoAtual.subtract(difMov);
				}
				PreparedStatement ps = Parametros.getConexao().getPst("INSERT INTO public.saldos_estoque_funcionario(\n" +
						"            id_itens_estoque, quantidade_movimentada, saldo, data_hora_saldo, \n" +
						"             id_movimento, observacoes, id_loterica, id_entidade, id_funcionario)\n" +
						"    VALUES ( ?, ?, ?, ?, \n" +
						"            ?, ?, ?, ?, ?);", false);
				ps.setInt(1, movimentosEstoqueFuncionario.idItensEstoque);
				ps.setBigDecimal(2, difMov);
				ps.setBigDecimal(3, saldo);
				ps.setTimestamp(4, movimentosEstoqueFuncionario.dataHoraReferencia);
				ps.setInt(5, movimentosEstoqueFuncionario.id);
				ps = Seter.set(ps,6, movimentosEstoqueFuncionario.observacoes);
				ps = Seter.set(ps,7, movimentosEstoqueFuncionario.idLoterica);
				ps.setInt(8, Parametros.idEntidade);
				ps.setInt(9,movimentosEstoqueFuncionario.idFuncionario);
				
				ps.execute();
			}
			
		}catch(SQLException ex) {
			new LogError(ex.getMessage(), ex,request);
		}
	}
	
	
	public void ajustaSaldoAntesDeletarMovimento(MovimentosEstoque movimentosEstoqueFuncionario){
		//language=PostgresPLSQL
		this.id = getIdSaldoFuncionario(movimentosEstoqueFuncionario);
		String sql = "UPDATE " +
				"           saldos_estoque_funcionario " +
				"       set " +
				"           saldo = saldo - ?" +
				"       where " +
				"           id > ? AND " +
				"           id_itens_estoque = ? AND " +
				"           id_loterica = ? AND" +
				"           id_entidade = ? AND " +
				"           id_funcionario = ?";
		try {
			PreparedStatement ps = Parametros.getConexao().getPst(sql);
			ps.setBigDecimal(1,movimentosEstoqueFuncionario.qtdTotalMovimentada);
			ps.setInt(2,id);
			ps.setInt(3,movimentosEstoqueFuncionario.idItensEstoque);
			ps = Seter.set(ps,4,movimentosEstoqueFuncionario.idLoterica);
			ps.setInt(5,Parametros.idEntidade);
			ps.setInt(6,movimentosEstoqueFuncionario.idFuncionario);
			ps.execute();
			
			sql = "DELETE FROM " +
					"   saldos_estoque_funcionario " +
					"WHERE " +
					"   id_movimento = ? AND " +
					"   id_loterica = ? AND " +
					"   id_entidade = ? AND " +
					"   id_funcionario = ?";
			ps = Parametros.getConexao().getPst(sql);
			ps.setInt(1,movimentosEstoqueFuncionario.id);
			ps = Seter.set(ps,2,movimentosEstoqueFuncionario.idLoterica);
			ps.setInt(3,Parametros.idEntidade);
			ps.setInt(4,movimentosEstoqueFuncionario.idFuncionario);
			ps.execute();
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
	}
	
	private Integer getIdSaldoFuncionario(MovimentosEstoque movimentosEstoqueFuncionario){
		try{
			PreparedStatement ps = Parametros.getConexao().getPst("select id from saldos_estoque_funcionario where id_movimento = ? and id_entidade = ? and id_funcionario = ? ORDER by id",false);
			ps.setInt(1, movimentosEstoqueFuncionario.id);
			ps.setInt(2, Parametros.idEntidade);
			ps.setInt(3,movimentosEstoqueFuncionario.idFuncionario);
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
