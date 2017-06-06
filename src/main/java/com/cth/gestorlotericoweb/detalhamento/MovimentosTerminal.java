package com.cth.gestorlotericoweb.detalhamento;

import com.cth.gestorlotericoweb.processos.AberturaTerminal;
import com.cth.gestorlotericoweb.processos.FechamentoTerminal;
import com.cth.gestorlotericoweb.processos.MovimentoCaixa;
import com.cth.gestorlotericoweb.processos.OutrosMovimentos;
import com.cth.gestorlotericoweb.utils.Parser;

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
		linha = getCel("Abertura - Entrada")+getCel("Troco dia Anterior")+getCel(Parser.formataComMascara(Parser.toBigDecimalFromSt(aberturaTerminal.getTrocoDiaAnterior()),""))+getCel(aberturaTerminal.getObservacoes());
		lLinhas = getLinha(lLinhas,linha);
		linha = getCel("Abertura - Entrada")+getCel("Troco do dia")+getCel(Parser.formataComMascara(Parser.toBigDecimalFromSt(aberturaTerminal.getTrocoDia()),""))+getCel(aberturaTerminal.getObservacoes());
		lLinhas = getLinha(lLinhas,linha);
		MovimentoCaixa movimentoCaixa = new MovimentoCaixa(request);
		for (MovimentoCaixa mc:movimentoCaixa.getDadosPorTerminalFuncionarioData()){
			linha =  getCel("Movimento de caixa")+getCel(mc.getTipoOperacao().equals("1")?"Sangria":"Depósito")+getCel(Parser.formataComMascara(Parser.toBigDecimalFromSt(mc.getValorMovimentado()),""))+getCel(mc.getObservacoes());
			lLinhas = getLinha(lLinhas,linha);
		}
		OutrosMovimentos outrosMovimentos = new OutrosMovimentos(request);
		for (OutrosMovimentos om:outrosMovimentos.getDadosPorTerminalFuncionarioData()){
			String tipoOperacao;
			String entradaSaida;
//			 <option value="1" selected="selected">Bolão - Saída</option>
//            <option value="2" >Bolão - Entrada</option>
//            <option value="8" >Bolão - Geração</option>
//            <option value="9" >Bolão - Venda</option>
//            <option value="3" >Bilhetes - Vendas</option>
//            <option value="4" >Tele Sena - Troca</option>
//            <option value="5" >Tele Sena - Vendas</option>
//            <option value="6" >Outros - Saída</option>
//            <option value="7" >Outros - Entrada</option>
			switch (om.getTipoOperacao()){
				case "1":
					tipoOperacao = "Bolão";
					entradaSaida = "Saída";
					break;
				case "2":
					tipoOperacao = "Bolão";
					entradaSaida = "Entrada";
					break;
				case "8":
					tipoOperacao = "Bolão";
					entradaSaida = "Geração";
					break;
				case "9":
					tipoOperacao = "Bolão";
					entradaSaida = "Venda";
					break;
				case "3":
					tipoOperacao = "Bilhetes";
					entradaSaida = "Venda";
					break;
				case "4":
					tipoOperacao = "Tele Sena";
					entradaSaida = "Troca";
					break;
				case "5":
					tipoOperacao = "Tele Sena";
					entradaSaida = "Venda";
					break;
				case "6":
					tipoOperacao = "Outros";
					entradaSaida = "Saída";
					break;
				case "7":
					tipoOperacao = "Outros";
					entradaSaida = "Entrada";
					break;
				default:
					tipoOperacao ="";
					entradaSaida ="";
					break;
			}
			linha =  getCel(entradaSaida)+getCel(tipoOperacao)+getCel(Parser.formataComMascara(Parser.toBigDecimalFromSt(om.getValorMovimentado()),""))+getCel(om.getObservacoes());
			lLinhas = getLinha(lLinhas,linha);
		}
		FechamentoTerminal fechamentoTerminal = new FechamentoTerminal(request);
		for (FechamentoTerminal ft:fechamentoTerminal.getDadosPorTerminalFuncionarioData()){
			linha =  getCel("Fechamento ")+getCel("Resto em caixa")+getCel(ft.getRestoCaixa())+getCel(ft.getObservacoes());
			lLinhas = getLinha(lLinhas,linha);
			linha =  getCel("Fechamento ")+getCel("Total Movimentação registrada")+getCel(Parser.formataComMascara(ft.getTotalMovimentosDiaBigDecimal(),""))+getCel(ft.getObservacoes());
			lLinhas = getLinha(lLinhas,linha);
			linha =  getCel("Fechamento ")+getCel("Saldo Movimentos terminal")+getCel(Parser.formataComMascara(ft.getSaldoTerminalBigDecimal(),""))+getCel(ft.getObservacoes());
			lLinhas = getLinha(lLinhas,linha);
			linha =  getCel("Fechamento ")+getCel("Diferença de Caixa")+getCel(Parser.formataComMascara(ft.getDiferencaCaixaBigDec(),""))+getCel(ft.getObservacoes());
			lLinhas = getLinha(lLinhas,linha);
		}
		return getTabela(lLinhas);
	}
	
}
