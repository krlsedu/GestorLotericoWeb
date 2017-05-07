package com.cth.gestorlotericoweb.detalhamento;

import com.cth.gestorlotericoweb.processos.AberturaTerminal;
import com.cth.gestorlotericoweb.processos.MovimentoCaixa;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CarlosEduardo on 07/05/2017.
 */
public class MovimentosTerminal extends Detalhamentos {
	
	String idTerminal;
	String idFuncionario;
	String data;
	public MovimentosTerminal(HttpServletRequest request) {
		super(request);
	}
	
	public String getTabelaMovimentos(){
		List<String> lLinhas = new ArrayList<>();
		String linha;
		AberturaTerminal aberturaTerminal = new AberturaTerminal(request);
		aberturaTerminal.getDadosPorTerminalFuncionarioEData();
		linha = getCel("Entrada")+getCel("Troco dia Anterior")+getCel(aberturaTerminal.getTrocoDiaAnterior())+getCel(aberturaTerminal.getObservacoes());
		lLinhas = getLinha(lLinhas,linha);
		linha = getCel("Entrada")+getCel("Troco do dia")+getCel(aberturaTerminal.getTrocoDia())+getCel(aberturaTerminal.getObservacoes());
		lLinhas = getLinha(lLinhas,linha);
		MovimentoCaixa movimentoCaixa = new MovimentoCaixa(request);
		for (MovimentoCaixa mc:movimentoCaixa.getDadosPorTerminalFuncionarioData()){
			linha =  getCel("Movimento de caixa")+getCel(mc.getTipoOperacao().equals("1")?"Sangria":"Depósito")+getCel(mc.getValorMovimentado())+getCel(mc.getObservacoes());
			lLinhas = getLinha(lLinhas,linha);
		}
		return "";
	}
	
}
