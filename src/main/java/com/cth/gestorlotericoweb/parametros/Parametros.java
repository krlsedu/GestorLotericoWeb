/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.parametros;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.banco.Conexao;
import com.cth.gestorlotericoweb.processos.AberturaTerminal;
import com.cth.gestorlotericoweb.utils.Parser;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author CarlosEduardo
 */
public class Parametros {
	public static Conexao conexao;
	public static Integer idUsuario;
	private static Integer idFuncionario;
	public static Integer idEntidade;
	private static Integer idLoterica;
	private static Integer idTerminal;
	private static Integer idAberturaTerminal;
	public static String entidadeNome;
	public static String version;
	public static String codificacao;
	public static String patern;
	
	public static Integer getIdTerminal() {
		return idTerminal;
	}
	
	public static void setIdTerminal(Integer idTerminal) {
		Parametros.idTerminal = idTerminal;
	}
	
	public static Integer getIdAberturaTerminal() {
		return idAberturaTerminal;
	}
	
	public static void setIdAberturaTerminal(Integer idAberturaTerminal) {
		Parametros.idAberturaTerminal = idAberturaTerminal;
	}
	
	public static void initDb(HttpServletRequest request) {
		conexao = new Conexao(request);
		setVersion();
	}
	
	public static Conexao getConexao(HttpServletRequest request) {
		gravaLogSessao(request);
		return conexao;
	}
	
	public static Conexao getConexao() {
		return conexao;
	}
	
	public static void gravaLogSessao(HttpServletRequest request) {
		Date date = new Date();
		Long tempoSessao = (date.getTime() - request.getSession(false).getLastAccessedTime()) / 1000;
		String pgAnt;
		if (request.getSession(false).getAttribute("url") == null) {
			pgAnt = "";
		} else {
			pgAnt = request.getSession(false).getAttribute("url").toString();
		}
		String pgAtu;
		if (request.getParameterMap().keySet().size() > 0) {
			pgAtu = request.getRequestURL().toString() + "?";
			List l = new ArrayList();
			for (Object ob : request.getParameterMap().keySet()) {
				l.add(ob.toString() + "=" + StringUtils.join(request.getParameterValues(ob.toString()), ','));
			}
			
			pgAtu += StringUtils.join(l, "&");
			//pgAtu = String.valueOf(request.getRequestURL().append('?').append(request.getQueryString()));
			
		} else {
			pgAtu = request.getRequestURL().toString();
		}
		gravaSessao(request, pgAnt, pgAtu, tempoSessao);
		request.getSession(false).setAttribute("url", pgAtu);
	}
	
	public static void setIdUsuario(Integer idUsuario) {
		Parametros.idUsuario = idUsuario;
	}
	
	public static void setIdEntidade(Integer idEntidade) {
		Parametros.idEntidade = idEntidade;
		try {
			PreparedStatement ps = conexao.getPst("select nome,codificacao,patern from entidades where id = ?", false);
			ps.setInt(1, idEntidade);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				setEntidadeNome(rs.getString(1));
				setCodificacao(rs.getString(2));
				setPatern(rs.getString(3));
			}
			ps = conexao.getPst("SELECT id FROM lotericas WHERE id_entidade = ?",false);
			ps.setInt(1,idEntidade);
			rs = ps.executeQuery();
			if (rs.next()){
				setIdLoterica(rs.getInt(1));
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	private static void setIdFuncionario(Integer idFuncionario, HttpServletRequest request) {
		Parametros.idFuncionario = idFuncionario;
		AberturaTerminal aberturaTerminal = new AberturaTerminal(request);
		aberturaTerminal.getTerminalAberto(idFuncionario);
		setIdTerminal(Parser.toIntegerNull(aberturaTerminal.getIdTerminal()));
		setIdAberturaTerminal(aberturaTerminal.getId());
	}
	
	public static void setDadosFuncXterm(HttpServletRequest request){
		try {
			PreparedStatement ps = getConexao(request).getPst("SELECT id FROM funcionarios WHERE id_usuario = ? AND id_entidade = ?",false);
			ps.setInt(1, idUsuario);
			ps.setInt(2, idEntidade);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				setIdFuncionario(rs.getInt(1),request);
			}
		} catch (SQLException e) {
			new LogError(e.getMessage(), e, request);
		}
	}
	
	public static Integer getIdFuncionario() {
		return idFuncionario;
	}
	
	public static Integer getIdLoterica() {
		return idLoterica;
	}
	
	public static void setIdLoterica(Integer idLoterica) {
		Parametros.idLoterica = idLoterica;
	}
	
	public static String getEntidadeNome() {
		return entidadeNome;
	}
	
	public static void setEntidadeNome(String entidadeNome) {
		Parametros.entidadeNome = entidadeNome;
	}
	
	public static void gravaSessao(HttpServletRequest request, String pgAnterior, String pgAtual, Long tempoSessao) {
		try {
			PreparedStatement ps = conexao.getPst("INSERT INTO sessoes(\n" +
					"            id_usuario, id_entidade, id_sessao, ip, nome_maquina, pg_anterior, \n" +
					"            pg_atual, tempo_sessao)\n" +
					"    VALUES ( ?, ?, ?, ?, ?, ?, \n" +
					"            ?, ?);");
			ps.setInt(1, idUsuario);
			ps.setInt(2, idEntidade);
			ps.setString(3, request.getSession(false).getId());
			ps.setString(4, request.getRemoteAddr());
			ps.setString(5, request.getRemoteHost());
			ps.setString(6, pgAnterior);
			ps.setString(7, pgAtual);
			ps.setLong(8, tempoSessao);
			ps.execute();
		} catch (SQLException ex) {
			new LogError(ex.getMessage(), ex, request);
		}
	}
	
	public static void gravaLogin(String usuario, String ip, String host, String sucesso, HttpServletRequest request) {
		try {
			PreparedStatement ps = conexao.getPst("INSERT INTO logins(\n" +
					"            usuario, ip, nome_maquina, sucesso)\n" +
					"    VALUES (?, ?, ?, ?);", false);
			ps.setString(1, usuario);
			ps.setString(2, ip);
			ps.setString(3, host);
			ps.setString(4, sucesso);
			ps.execute();
		} catch (SQLException ex) {
			new LogError(ex.getMessage(), ex, request);
		}
	}
	
	public static String getVersion() {
		return version;
	}
	
	public static void setVersion() {
		String sql = "select * from db_version ORDER BY id DESC LIMIT 1";
		try {
			ResultSet resultSet = conexao.getRs(sql);
			if (resultSet.next()) {
				Parametros.version=resultSet.getString(3)+"-"+resultSet.getString(4)+"("+resultSet.getString(5)+")";
			}
		} catch (SQLException e) {
			Parametros.version = "";
		}
	}
	
	public static String getCodificacao() {
		return codificacao;
	}
	
	public static void setCodificacao(String codificacao) {
		Parametros.codificacao = codificacao;
	}
	
	public static String getPatern() {
		return patern;
	}
	
	public static void setPatern(String patern) {
		Parametros.patern = patern;
	}
}
