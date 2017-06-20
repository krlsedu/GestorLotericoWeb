package com.cth.gestorlotericoweb.processos;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.utils.Seter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FechamentoAdministrativoDiario extends Processos {
	BigDecimal totalCreditosTerminais;
	BigDecimal totalDebitosTerminais;
	BigDecimal totalDepositado;
	BigDecimal diferenca;
	BigDecimal saldoCofre;
	
	Date dataFechamento;
	
	Integer idFuncionario;
	Integer idLoterica;
	
	String observacoes;
	
	public FechamentoAdministrativoDiario(HttpServletRequest request) {
		super(request);
	}
	
	public void insere(){
		String sql = "INSERT INTO public.fechamento_administrativo_diario(\n" +
				"             id_funcionario, id_loterica, total_creditos_terminais, total_debitos_terminais, \n" +
				"            total_depositado, data_fechamento, \n" +
				"            observacoes, saldo_cofre, diferenca, id_entidade)\n" +
				"    VALUES ( ?, ?, ?, \n" +
				"            ?, ?, ?, ?, \n" +
				"            ?, ?, ?)";
		try {
			Seter ps = new Seter(sql,request);
			ps.set(idFuncionario);
			ps.set(idLoterica);
			ps.set(totalCreditosTerminais);
			ps.set(totalDebitosTerminais);
			ps.set(totalDepositado);
			ps.set(dataFechamento);
			ps.set(saldoCofre);
			ps.set(diferenca);
			ps.set(observacoes);
			ps.set(Parametros.idEntidade);
			ps.execute();
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
	}
	
	public void altera(){
		//language=PostgresPLSQL
		String sql = "UPDATE public.fechamento_administrativo_diario\n" +
				"   SET  id_funcionario=?, id_loterica=?, total_creditos_terminais=?, total_debitos_terminais=?, \n" +
				"       total_depositado=?, data_fechamento=?, \n" +
				"        saldo_cofre=?, diferenca=?, observacoes=? \n" +
				" WHERE " +
				"       id = ? AND " +
				"       id_entidade = ? ";
		try {
			Seter ps = new Seter(sql,request);
			ps.set(idFuncionario);
			ps.set(idLoterica);
			ps.set(totalCreditosTerminais);
			ps.set(totalDebitosTerminais);
			ps.set(totalDepositado);
			ps.set(dataFechamento);
			ps.set(saldoCofre);
			ps.set(diferenca);
			ps.set(observacoes);
			
			ps.set(id);
			ps.set(Parametros.idEntidade);
			
			ps.execute();
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
	}
	
	public void getDados(){
		//language=PostgresPLSQL
		String sql = "SELECT id_funcionario, id_loterica, total_creditos_terminais, total_debitos_terminais, \n" +
				"       total_depositado, \n" +
				"       saldo_cofre, diferenca, data_fechamento, observacoes\n" +
				"   FROM public.fechamento_administrativo_diario" +
				"   WHERE " +
				"       id = ? AND " +
				"       id_entidade = ?";
		try {
			Seter ps = new Seter(sql,request,false);
			
			ps.set(id);
			ps.set(Parametros.idEntidade);
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				this.idFuncionario = rs.getInt(1);
				this.idLoterica = rs.getInt(2);
				this.totalCreditosTerminais = rs.getBigDecimal(3);
				this.totalDebitosTerminais = rs.getBigDecimal(4);
				this.totalDepositado = rs.getBigDecimal(5);
				this.saldoCofre = rs.getBigDecimal(6);
				this.diferenca = rs.getBigDecimal(7);
				this.dataFechamento = rs.getDate(8);
				this.observacoes = rs.getString(9);
			}else {
				this.id = null;
			}
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
	}
	
	public VelocityContext getHtml(VelocityContext contextPrinc, VelocityEngine ve, String idS){
		Template templateConteudo;
		VelocityContext contextConteudo;
		StringWriter writerConteudo;
		templateConteudo = ve.getTemplate( "templates/Modern/processos/fechamento_administrativo_diario.html" , "UTF-8");
		contextConteudo = new VelocityContext();
		writerConteudo = new StringWriter();
		contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());
		contextConteudo.put("id_fun", Parametros.getIdFuncionario());
		contextConteudo.put("id_lot", Parametros.getIdLoterica());
		
		setOpcoesFiltro("fechamento_administrativo_diario");
		templateConteudo.merge( contextConteudo, writerConteudo );
		contextPrinc.put("conteudo", writerConteudo.toString());
		contextPrinc.put("popup", getSWPopup(ve,"fechamento_administrativo_diario").toString());
		return contextPrinc;
	}
	
}
