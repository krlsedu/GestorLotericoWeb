/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.dados;

import com.cth.gestorlotericoweb.banco.Conexao;
import com.cth.gestorlotericoweb.parametros.Parametros;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CarlosEduardo
 */
public class Loterica {
    String codigoCaixa;
    String nome;
    Integer id;

    public Loterica(String codigoCaixa, String nome) {
        this.codigoCaixa = codigoCaixa;
        this.nome = nome;
    }

    public Loterica(Integer id) {
        this.id = id;
        getDados();
    }
    private void getDados(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("SELECT codigo_caixa, nome\n" +
                    "  FROM public.lotericas where id = ? and id_entidade = ? ",false);
            ps.setInt(1, id);
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                codigoCaixa = rs.getString(1);
                nome = rs.getString(2);
            }else{
                codigoCaixa = "";
                nome = "";
                id = 0;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
    
    public void insere(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("INSERT INTO public.lotericas(\n" +
                    "            codigo_caixa, nome,id_entidade)\n" +
                    "    VALUES (?, ?, ?);");
            ps.setString(1, (codigoCaixa));
            ps.setString(2, nome);
            ps.setInt(3, Parametros.idEntidade);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
    
    public void altera(String idL){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("UPDATE public.lotericas\n" +
                    "   SET  codigo_caixa=?, nome=?\n" +
                    " WHERE id=? and id_loterica = ? ", false);
            ps.setString(1, codigoCaixa);
            ps.setString(2, nome);
            id = Integer.valueOf(idL);
            ps.setInt(3, id);
            ps.setInt(4, Parametros.idEntidade);
            ps.execute();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public Integer getId() {
        return id;
    }

    public String getCodigoCaixa() {
        return codigoCaixa;
    }

    public String getNome() {
        return nome;
    }
    
}
