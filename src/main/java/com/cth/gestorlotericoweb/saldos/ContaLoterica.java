package com.cth.gestorlotericoweb.saldos;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.dados.Loterica;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.utils.DataHandler;
import com.cth.gestorlotericoweb.utils.Parser;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by CarlosEduardo on 13/05/2017.
 */
public class ContaLoterica extends Saldos {
	
	private Integer idLoterica;
	
	@Setter
	private Date dataIni;
	
	private Date dataFim;
	public ContaLoterica(Integer idLoterica,HttpServletRequest request) {
		super(request);
		this.idLoterica = idLoterica;
	}
	
	public String getSaldoMovsSt(){
		return Parser.formataComMascara(getSaldoMovs(),"R$ ");
	}
	
	private BigDecimal getSaldoMovs(){
		BigDecimal totalTerminias = getTotalTerminais();
		if (totalTerminias == null) {
			totalTerminias = BigDecimal.ZERO;
		}
		BigDecimal totalDepositado = getTotalDepositado();
		if (totalDepositado == null) {
			totalDepositado = BigDecimal.ZERO;
		}
		return totalDepositado.subtract(totalTerminias);
	}
	
	private BigDecimal getTotalTerminais(){
		//language=PostgresPLSQL
		String sql = "SELECT sum(saldo_terminal) from fechamento_terminais WHERE id_loterica = ? and id_entidade = ? and data_encerramento BETWEEN ? and ?";
		return getValor(sql);
	}
	
	private BigDecimal getTotalDepositado(){
		//language=PostgresPLSQL
		String sql = "SELECT sum(case when tipo_movimento_conta = '2' then valor_movimentado ELSE valor_movimentado *(-1) end *numero_volumes) from movimentos_contas WHERE id_loterica = ? and id_entidade = ? and date(data_hora_mov) BETWEEN ? and ?";
		//TODO criar filtro quando for feito o parametro para a conta princial da lotérica
		return getValor(sql);
	}
	
	private BigDecimal getValor(String sql){
		try {
			PreparedStatement ps = Parametros.getConexao().getPst(sql,false);
			ps.setInt(1,idLoterica);
			ps.setInt(2,Parametros.idEntidade);
			if (dataIni != null) {
				ps.setDate(3,dataIni);
			}else {
				ps.setDate(3, DataHandler.getInicioMes());
			}
			
			if (dataFim != null) {
				ps.setDate(4,dataFim);
			}else {
				ps.setDate(4,DataHandler.getFimMes());
			}
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				return rs.getBigDecimal(1);
			}else {
				return BigDecimal.ZERO;
			}
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
		return BigDecimal.ZERO;
	}
	
	public String getNomeLoterica(){
		Loterica loterica = new Loterica(idLoterica,request);
		return loterica.getNome();
	}
}
