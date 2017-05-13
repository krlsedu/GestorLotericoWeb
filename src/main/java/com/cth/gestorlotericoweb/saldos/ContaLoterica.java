package com.cth.gestorlotericoweb.saldos;

import com.cth.gestorlotericoweb.utils.Parser;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * Created by CarlosEduardo on 13/05/2017.
 */
public class ContaLoterica extends Saldos {
	private Integer idLoterica;
	public ContaLoterica(HttpServletRequest request, Integer idLoterica) {
		super(request);
		this.idLoterica = idLoterica;
	}
	
	public String getSaldoMovsSt(){
		return Parser.formataComMascara(getSaldoMovs(),"");
	}
	
	private BigDecimal getSaldoMovs(){
		return BigDecimal.ZERO;
	}
	
	private BigDecimal getTotalTerminais(){
		String sql = "SELECT sum(saldo_terminal) from fechamento_terminais WHERE id_";
		return BigDecimal.ZERO;
	}
	
	private BigDecimal getTotalDepositado(){
		return BigDecimal.ZERO;
	}
}
