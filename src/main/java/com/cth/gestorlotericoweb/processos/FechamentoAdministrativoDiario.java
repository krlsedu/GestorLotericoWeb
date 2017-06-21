package com.cth.gestorlotericoweb.processos;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.saldos.SaldoCofre;
import com.cth.gestorlotericoweb.utils.MyPreparedStatement;
import com.cth.gestorlotericoweb.utils.Parser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
	private BigDecimal totalCreditosTerminais;
	private BigDecimal totalDebitosTerminais;
	private BigDecimal totalDepositado;
	private BigDecimal diferenca;
	private BigDecimal saldoCofre;
	
	private Date dataFechamento;
	
	private Integer idFuncionario;
	private Integer idLoterica;
	
	private String observacoes;
	
	public FechamentoAdministrativoDiario(HttpServletRequest request) {
		super(request);
		setFechamentoAdministrativoDiario();
	}
	
	private void setFechamentoAdministrativoDiario(){
		this.id = Parser.toInteger(request.getParameter("id"));
		this.idFuncionario = Parser.toIntegerNull(request.getParameter("id_funcionario"));
		this.idLoterica = Parser.toIntegerNull(request.getParameter("id_loterica"));
		
		this.totalCreditosTerminais = Parser.toBigDecimalFromHtml(request.getParameter("total_creditos_terminais"));
		this.totalDebitosTerminais = Parser.toBigDecimalFromHtml(request.getParameter("total_debitos_terminais"));
		this.totalDepositado = Parser.toBigDecimalFromHtml(request.getParameter("total_depositado"));
		this.diferenca = Parser.toBigDecimalFromHtml(request.getParameter("diferenca"));
		this.saldoCofre = Parser.toBigDecimalFromHtml(request.getParameter("saldo_cofre"));
		
		this.dataFechamento = Parser.toDbDate(request.getParameter("data_fechamento"));
		
		this.observacoes = request.getParameter("observacoes");
		
	}
	
	public void grava(){
		if(this.id == 0){
			insere();
		}else{
			altera();
		}
	}
	
	public void insere(){
		String sql = "INSERT INTO public.fechamento_administrativo_diario(\n" +
				"             id_funcionario, id_loterica, " +
				"             total_creditos_terminais, total_debitos_terminais, \n" +
				"             total_depositado, data_fechamento, \n" +
				"             diferenca, saldo_cofre, " +
				"             observacoes, id_entidade)\n" +
				"    VALUES ( ?, ?, ?, \n" +
				"            ?, ?, ?, ?, \n" +
				"            ?, ?, ?)";
		try {
			MyPreparedStatement ps = new MyPreparedStatement(sql,request);
			ps.set(idFuncionario);
			ps.set(idLoterica);
			ps.set(totalCreditosTerminais);
			ps.set(totalDebitosTerminais);
			ps.set(totalDepositado);
			ps.set(dataFechamento);
			ps.set(diferenca);
			ps.set(saldoCofre);
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
			MyPreparedStatement ps = new MyPreparedStatement(sql,request);
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
			MyPreparedStatement ps = new MyPreparedStatement(sql,request,false);
			
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
	
	public void calculaMovimentosTerminais(){
		String sql = "SELECT sum(total_creditos_terminal),sum(total_debitos_terminal) FROM fechamento_terminais\n" +
				"WHERE\n" +
				"    DATE(data_encerramento) = ? AND \n" +
				"    id_loterica = ? AND \n" +
				"    id_entidade = ?";
		try {
			MyPreparedStatement ps = new MyPreparedStatement(sql,request,false);
			ps.set(dataFechamento);
			ps.set(idLoterica);
			ps.set(Parametros.idEntidade);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				setTotalCreditosTerminais(rs.getBigDecimal(1));
				setTotalDebitosTerminais(rs.getBigDecimal(2));
			}else {
				setTotalCreditosTerminais(BigDecimal.ZERO);
				setTotalDebitosTerminais(BigDecimal.ZERO);
			}
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
	}
	
	public void calculaSaldoCofres(){
		String sql = "SELECT id FROM cofres WHERE id_loterica = ? and id_entidade = ?";
		try {
			MyPreparedStatement ps = new MyPreparedStatement(sql,request,false);
			ps.set(idLoterica);
			ps.set(Parametros.idEntidade);
			ResultSet rs = ps.executeQuery();
			BigDecimal saldo = BigDecimal.ZERO;
			while (rs.next()){
				SaldoCofre saldoCofre = new SaldoCofre(rs.getInt(1),request);
				saldo.add(saldoCofre.getSaldo(this.dataFechamento));
			}
			setSaldoCofre(saldo);
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
	}
	
	public void calculaTotalDepositado(){
		//language=PostgresPLSQL
		String sql = "SELECT \n" +
				"  sum(CASE when tipo_movimento_conta = '2' then valor_movimentado else valor_movimentado *(-1) END) \n" +
				"FROM \n" +
				"  movimentos_contas \n" +
				"where \n" +
				"  id_loterica = ? AND \n" +
				"  id_entidade = ?";
		try {
			MyPreparedStatement ps = new MyPreparedStatement(sql,request,false);
			ps.set(idLoterica);
			ps.set(Parametros.idEntidade);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				setTotalDepositado(rs.getBigDecimal(1));
			}else {
				setTotalDepositado(BigDecimal.ZERO);
			}
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
		
	}
	
	public void calculaDiferenca(){
		BigDecimal saldoTerm = getTotalCreditosTerminais().subtract(getTotalDebitosTerminais());
		setDiferenca(getTotalDepositado().subtract(saldoTerm));
	}
	
	public BigDecimal getTotalCreditosTerminais() {
		return totalCreditosTerminais;
	}
	
	public void setTotalCreditosTerminais(BigDecimal totalCreditosTerminais) {
		if (totalCreditosTerminais==null) {
			totalCreditosTerminais = BigDecimal.ZERO;
		}
		this.totalCreditosTerminais = totalCreditosTerminais;
	}
	
	public BigDecimal getTotalDebitosTerminais() {
		return totalDebitosTerminais;
	}
	
	public void setTotalDebitosTerminais(BigDecimal totalDebitosTerminais) {
		if (totalDebitosTerminais==null) {
			totalDebitosTerminais = BigDecimal.ZERO;
		}
		this.totalDebitosTerminais = totalDebitosTerminais;
	}
	
	public BigDecimal getTotalDepositado() {
		return totalDepositado;
	}
	
	public void setTotalDepositado(BigDecimal totalDepositado) {
		if (totalDepositado == null) {
			totalDepositado = BigDecimal.ZERO;
		}
		this.totalDepositado = totalDepositado;
	}
	
	public BigDecimal getDiferenca() {
		return diferenca;
	}
	
	public void setDiferenca(BigDecimal diferenca) {
		if (diferenca == null) {
			diferenca = BigDecimal.ZERO;
		}
		this.diferenca = diferenca;
	}
	
	public BigDecimal getSaldoCofre() {
		return saldoCofre;
	}
	
	public void setSaldoCofre(BigDecimal saldoCofre) {
		if (saldoCofre == null) {
			saldoCofre = BigDecimal.ZERO;
		}
		this.saldoCofre = saldoCofre;
	}
	
	public Date getDataFechamento() {
		return dataFechamento;
	}
	
	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}
	
	public String getJsonDados(){
		Gson gson = new GsonBuilder().serializeNulls().create();
		calculaMovimentosTerminais();
		calculaTotalDepositado();
		calculaDiferenca();
		calculaSaldoCofres();
		return gson.toJson(this);
	}
}
