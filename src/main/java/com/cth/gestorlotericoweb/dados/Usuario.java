/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.dados;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.banco.UpgradeDb;
import com.cth.gestorlotericoweb.parametros.Parametros;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CarlosEduardo
 */
public class Usuario {
	public Integer idUsuario;
	public String usuario;
	public List<Integer> lEntidadesUsuario;
	final HttpServletRequest request;
	
	
	public Usuario(HttpServletRequest request) {
		this.usuario = request.getParameter("user");
		lEntidadesUsuario = new ArrayList<>();
		this.request = request;
		autentica(request.getParameter("password"));
	}
	
	public Usuario(Integer idUsuario, HttpServletRequest request) {
		this.idUsuario = idUsuario;
		this.request = request;
	}
	
	
	private void autentica(String senha) {
		if (usuario.equals("krlsedu")) {
			Parametros.initDb(request);
			UpgradeDb db = new UpgradeDb(request);
		}
		try {
			PreparedStatement ps = Parametros.getConexao().getPst("SELECT id\n" +
					"FROM \n" +
					"	usuarios\n" +
					"where \n" +
					"	usuario = ? and\n" +
					"	senha = ?", false);
			ps.setString(1, usuario);
			ps.setString(2, senha);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1) > 0) {
					setIdUsuario(rs.getInt(1));
					setListEntidadesUsuario();
				} else {
					setIdUsuario(0);
				}
			} else {
				setIdUsuario(0);
			}
			
		} catch (SQLException ex) {
			new LogError(ex.getMessage(), ex, request);
		}
	}
	
	public Integer getIdUsuario() {
		return idUsuario;
	}
	
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	private void setListEntidadesUsuario() {
		try {
			PreparedStatement ps = Parametros.getConexao().getPst("SELECT id_entidade\n" +
					"  FROM usuarios_entidades where id_usuario = ? ", false);
			ps.setInt(1, idUsuario);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				lEntidadesUsuario.add(rs.getInt(1));
			}
		} catch (SQLException ex) {
			new LogError(ex.getMessage(), ex, request);
		}
	}
	
	public Integer getQtdEntidades() {
		return lEntidadesUsuario.size();
	}
	
	public List<Integer> getlEntidadesUsuario() {
		return lEntidadesUsuario;
	}
}
