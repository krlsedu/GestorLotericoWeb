/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.parametros;

import com.cth.gestorlotericoweb.banco.Conexao;
import com.cth.gestorlotericoweb.dados.ColunasTabelas;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author CarlosEduardo
 */
public class Parametros {
    public static Conexao conexao;
    public static Integer idUsuario;
    public static Integer idEntidade;
    public static void initDb(){
        try {
            conexao = new Conexao();
        } catch (ClassNotFoundException | SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
    }

    public static Conexao getConexao() {
        return conexao;
    }

    public static void setIdUsuario(Integer idUsuario) {
        Parametros.idUsuario = idUsuario;
    }

    public static void setIdEntidade(Integer idEntidade) {
        Parametros.idEntidade = idEntidade;
    }
    
    
    
    public static void gravaSessao(HttpServletRequest request,String pgAnterior,String pgAtual,Long tempoSessao){
        try {
            PreparedStatement ps = conexao.getPst("INSERT INTO public.sessoes(\n" +
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
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
    
    public static void gravaLogin(String usuario,String ip,String host, String sucesso){
        try {
            PreparedStatement ps = conexao.getPst("INSERT INTO public.logins(\n" +
                    "            usuario, ip, nome_maquina, sucesso)\n" +
                    "    VALUES (?, ?, ?, ?);", false);
            ps.setString(1, usuario);
            ps.setString(2, ip);
            ps.setString(3, host);
            ps.setString(4, sucesso);
            ps.execute();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}
