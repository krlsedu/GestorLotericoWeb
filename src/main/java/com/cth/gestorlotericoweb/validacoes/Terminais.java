package com.cth.gestorlotericoweb.validacoes;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.utils.Parser;
import org.jsoup.nodes.Element;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Terminais extends Validacoes {
	public Terminais(HttpServletRequest request) {
		super(request);
	}
	
	private String geraElementos(){
		Element dados = new Element("h4");
		dados.append("Terminais Abertos: ");
		
		//language=PostgresPLSQL
		String sql = "SELECT t.id,'btn_term_abt_'||a.id,t.nome ||' - '||to_char(a.data_abertura , 'DD/MM/YYYY'), func.id||' - '||func.nome FROM\n" +
				"  abertura_terminais a,\n" +
				"  terminais t,\n" +
				"  funcionarios func\n" +
				"WHERE\n" +
				"  func.id = a.id_funcionario AND\n" +
				"  t.id_entidade = a.id_entidade AND\n" +
				"  t.id = a.id_terminal AND\n" +
				"  NOT exists( SELECT * FROM\n" +
				"                  fechamento_terminais f\n" +
				"              WHERE\n" +
				"                  f.id_terminal = a.id_terminal AND\n" +
				"                  f.id_funcionario = a.id_funcionario AND\n" +
				"                  f.data_encerramento = a.data_abertura AND\n" +
				"                  f.id_entidade = a.id_entidade)\n" +
				"ORDER BY\n" +
				"  t.nome";
		try {
			PreparedStatement ps = Parametros.getConexao().getPst(sql,false);
			ResultSet rs = ps.executeQuery();
			List<Element> elementList = new ArrayList<>();
			while (rs.next()) {
				Element divGroup = getElement("div","btn-group");
				
				Element btn = getElement("button","btn btn-info dropdown-toggle",rs.getString(2));
				btn.attr("data-toggle","dropdown");
				btn.attr("title",rs.getString(4));
				btn.append(rs.getString(3));
				Element spn = getElement("span","caret");
				btn.appendChild(spn);
				
				divGroup.appendChild(btn);
				
				Element drpMenu = getElement("ul","dropdown-menu");
//				ela === 'movimentos_caixas' || tela === '' || tela === '' || tela === ''
				
				
				Element li = new Element("li");
				Element a = new Element("a");
				a.attr("href","javascript:void(0)");
				a.attr("onclick","abreTelaBtnAbr('movimentos_caixas',"+rs.getInt(1)+")");
				a.append("Movimentação dos Caixas");
				li.appendChild(a);
				drpMenu.appendChild(li);
				
				li = new Element("li");
				a = new Element("a");
				a.attr("href","javascript:void(0)");
				a.attr("onclick","abreTelaBtnAbr('outros_movimentos',"+rs.getInt(1)+")");
				a.append("Outras Movimentações de Caixa");
				li.appendChild(a);
				
				drpMenu.appendChild(li);
				
				li = new Element("li");
				a = new Element("a");
				a.attr("href","javascript:void(0)");
				a.attr("onclick","abreTelaBtnAbr('operacoes_diarias',"+rs.getInt(1)+")");
				a.append("Operações diárias");
				li.appendChild(a);
				
				drpMenu.appendChild(li);
				
				li = new Element("li");
				a = new Element("a");
				a.attr("href","javascript:void(0)");
				a.attr("onclick","abreTelaBtnAbr('fechamento_terminais',"+rs.getInt(1)+")");
				a.append("Fechamento do Terminal");
				li.appendChild(a);
				
				drpMenu.appendChild(li);
				
				divGroup.appendChild(drpMenu);
				
				elementList.add(divGroup);
			}
			if (elementList.isEmpty()) {
				return "";
			}else {
				for (Element e:elementList) {
					dados.appendChild(e);
				}
			}
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
		
		return dados.outerHtml();
	}
	
	public String getTerminaisAbertos(){
		return geraElementos();
	}
	
	public Boolean existeTerminalAberto(){
		String sql;
		String idTermial = request.getParameter("id_terminal");
		String idFuncionario = request.getParameter("id_funcionario");
		if (idTermial != null) {
			if (idTermial.trim().isEmpty()) {
				idTermial = null;
			}
		}
		if (idFuncionario != null) {
			if (idFuncionario.trim().isEmpty()) {
				idFuncionario = null;
			}
		}
		try {
			PreparedStatement ps;
			if(idFuncionario!=null && idTermial !=null ) {
				sql = "SELECT 1 FROM\n" +
						"  abertura_terminais a,\n" +
						"  terminais t,\n" +
						"  funcionarios func\n" +
						"WHERE\n" +
						"  func.id = a.id_funcionario and\n" +
						"  t.id_entidade = a.id_entidade AND\n" +
						"  t.id = a.id_terminal and\n" +
						"  NOT exists( SELECT * from\n" +
						"                  fechamento_terminais f\n" +
						"              WHERE\n" +
						"                  f.id_terminal = a.id_terminal AND\n" +
						"                  f.id_funcionario = a.id_funcionario AND\n" +
						"                  f.data_encerramento = a.data_abertura AND\n" +
						"                  f.id_entidade = a.id_entidade) AND\n" +
						"    a.id_terminal = ? AND\n" +
						"    a.id_funcionario = ? \n" +
						"ORDER BY\n" +
						"  t.nome";
				ps = Parametros.getConexao().getPst(sql,false);
				ps.setInt(1,Integer.valueOf(idTermial));
				ps.setInt(2,Integer.valueOf(idFuncionario));
			}else{
				sql = "SELECT 1 FROM\n" +
						"  abertura_terminais a,\n" +
						"  terminais t,\n" +
						"  funcionarios func\n" +
						"WHERE\n" +
						"  func.id = a.id_funcionario and\n" +
						"  t.id_entidade = a.id_entidade AND\n" +
						"  t.id = a.id_terminal and\n" +
						"  NOT exists( SELECT * from\n" +
						"                  fechamento_terminais f\n" +
						"              WHERE\n" +
						"                  f.id_terminal = a.id_terminal AND\n" +
						"                  f.id_funcionario = a.id_funcionario AND\n" +
						"                  f.data_encerramento = a.data_abertura AND\n" +
						"                  f.id_entidade = a.id_entidade)";
				ps = Parametros.getConexao().getPst(sql,false);
			}
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			new LogError(e.getMessage(),e,request);
		}
		return false;
	}
	
	public Boolean verificaSeFechado(){
		String idTermial = request.getParameter("id_terminal");
		String idFuncionario = request.getParameter("id_funcionario");
		String it = request.getParameter("it");
		String parDt;
		switch (it){
			case "operacoes_diarias":
				parDt = "data_operacoes";
				break;
			case "abertura_terminais":
				parDt = "data_abertura";
				break;
			default:
				parDt = "data_hora_mov";
				break;
		}
		String dataHoraMov = request.getParameter(parDt);
		String sql = "SELECT 1 FROM\n" +
				"  abertura_terminais a,\n" +
				"  terminais t,\n" +
				"  funcionarios func\n" +
				"WHERE\n" +
				"  func.id = a.id_funcionario and\n" +
				"  t.id_entidade = a.id_entidade AND\n" +
				"  t.id = a.id_terminal and\n" +
				"  exists( SELECT * from\n" +
				"                  fechamento_terminais f\n" +
				"              WHERE\n" +
				"                  f.id_terminal = a.id_terminal AND\n" +
				"                  f.id_funcionario = a.id_funcionario AND\n" +
				"                  f.data_encerramento = a.data_abertura AND\n" +
				"                  f.id_entidade = a.id_entidade) AND\n" +
				"    a.id_terminal = ? AND\n" +
				"    a.id_funcionario = ? AND " +
				"    date(a.data_abertura) = date(?)";
		try {
			PreparedStatement ps = Parametros.getConexao().getPst(sql, false);
			ps.setInt(1, Integer.valueOf(idTermial));
			ps.setInt(2, Integer.valueOf(idFuncionario));
			ps.setDate(3, Parser.toDbDate(dataHoraMov));
			ResultSet rs = ps.executeQuery();
			return rs.next();
		}catch (SQLException e){
			new LogError(e.getMessage(),e,request);
		}
		return false;
	}
	
	
}
