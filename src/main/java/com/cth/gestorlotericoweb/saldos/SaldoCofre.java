/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.saldos;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.processos.MovimentoCofre;
import com.cth.gestorlotericoweb.utils.Parser;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author CarlosEduardo
 */
public class SaldoCofre extends Saldos{

    public SaldoCofre(HttpServletRequest request) {
        super(request);
    }      
    
    public void grava(MovimentoCofre movimentoCofre){
        id = getIdSaldo(movimentoCofre);
        if(id==0){
            insere(movimentoCofre);
        }else{
            update(movimentoCofre);
        }
    }
    
    private void insere(MovimentoCofre movimentoCofre){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("INSERT INTO public.saldos_cofres(\n" +
                    "            id_cofre, data_saldo, saldo, id_movimento_cofre, id_entidade )\n" +
                    "    VALUES (?, ?, ?, ?, ?);",false);
            PreparedStatement psRs = Parametros.getConexao().getPst("select saldo from saldos_cofres where id_cofre = ? and id_entidade = ? order by data_hora_movimento desc limit 1",false);
            psRs.setInt(1, Integer.valueOf(movimentoCofre.getIdCofre()));
            psRs.setInt(2, Parametros.idEntidade);
            ResultSet rs = psRs.executeQuery();
            BigDecimal saldoAtual;
            if(rs.next()){
                saldoAtual = rs.getBigDecimal(1);
            }else{
                saldoAtual = BigDecimal.ZERO;
            }
            BigDecimal saldo;
            if(movimentoCofre.tipoOperacao.equals("1")){
                saldo = saldoAtual.subtract(Parser.toBigDecimalFromHtml(movimentoCofre.valorMovimentado).multiply(Parser.toBigDecimalFromHtml(movimentoCofre.numeroVolumes)));
            }else{
                BigDecimal valorSomar = Parser.toBigDecimalFromHtml(movimentoCofre.valorMovimentado).multiply(Parser.toBigDecimalFromHtml(movimentoCofre.numeroVolumes));
                saldo = saldoAtual.add(valorSomar);
            }
            ps.setInt(1,Integer.valueOf(movimentoCofre.getIdCofre()));
            ps.setDate(2, Parser.toDbDate(movimentoCofre.getDataHoraMov()));
            ps.setBigDecimal(3, saldo);
            ps.setInt(4, movimentoCofre.getId());
            ps.setInt(5, Parametros.idEntidade);
            ps.execute();
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);  
        }
        
    }
    
    private void update(MovimentoCofre movimentoCofre){
        try{
            PreparedStatement psRs = Parametros.getConexao().getPst("select saldo from saldos_cofres where id_cofre = ? and id < ?  and id_entidade = ? order by data_hora_movimento desc limit 1",false);
            psRs.setInt(1, Integer.valueOf(movimentoCofre.getIdCofre()));
            psRs.setInt(2, id);
            psRs.setInt(3, Parametros.idEntidade);
            ResultSet rs = psRs.executeQuery();
            BigDecimal saldoAtual;
            if(rs.next()){
                saldoAtual = rs.getBigDecimal(1);
            }else{
                saldoAtual = BigDecimal.ZERO;
            }
            BigDecimal saldo;
            if(movimentoCofre.tipoOperacao.equals("1")){
                saldo = saldoAtual.subtract(Parser.toBigDecimalFromHtml(movimentoCofre.valorMovimentado).multiply(Parser.toBigDecimalFromHtml(movimentoCofre.numeroVolumes)));
            }else{
                BigDecimal valorSomar = Parser.toBigDecimalFromHtml(movimentoCofre.valorMovimentado).multiply(Parser.toBigDecimalFromHtml(movimentoCofre.numeroVolumes));
                saldo = saldoAtual.add(valorSomar);
            }           
            PreparedStatement ps = Parametros.getConexao().getPst("UPDATE public.saldos_cofres\n" +
                                                                "   SET saldo=? \n" +
                                                                " WHERE \n" +
                                                                "   id = ? and"
                                                                + " id_entidade = ? ",false);
            ps.setBigDecimal(1, saldo);
            ps.setInt(2, id);
            ps.setInt(3, Parametros.idEntidade);
            ps.execute();
        }catch(SQLException ex) {
            new LogError(ex.getMessage(), ex,request);  
        }
    }
    
    private Integer getIdSaldo(MovimentoCofre movimentoCofre){
        try{
            PreparedStatement ps = Parametros.getConexao().getPst("select id from saldos_cofres where id_movimento_cofre = ? and id_entidade = ?",false);
            ps.setInt(1, movimentoCofre.getId());
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }catch(SQLException ex){
            new LogError(ex.getMessage(), ex,request);            
        }
        return 0;
    }
}
