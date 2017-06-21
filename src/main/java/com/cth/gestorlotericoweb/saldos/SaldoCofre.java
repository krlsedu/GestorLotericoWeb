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

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author CarlosEduardo
 */
public class SaldoCofre extends Saldos{
    Integer idCofre;
    public SaldoCofre(HttpServletRequest request) {
        super(request);
    }      

    public SaldoCofre(Integer idCofre, HttpServletRequest request) {
        super(request);
        this.idCofre = idCofre;
    }
    
    public String getSaldoSt(){
        return Parser.formataComMascara(getSaldo(),"R$ ");
    }
    
    private BigDecimal getSaldo(){
        return getSaldo(new Date(new java.util.Date().getTime()));
    }
    
    public BigDecimal getSaldo(Date date){
        try{
            PreparedStatement ps = Parametros.getConexao().getPst("SELECT saldo FROM saldos_cofres where id_cofre = ? and id_entidade = ? and date(data_saldo) <= ? order by data_hora_movimento desc, data_saldo desc limit 1", false);
            ps.setInt(1, idCofre);
            ps.setInt(2, Parametros.idEntidade);
            ps.setDate(3,date);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {                
                return rs.getBigDecimal(1);
            }
        }catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);  
        }
        return BigDecimal.ZERO;
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
                    "            id_cofre, data_saldo, saldo, id_movimento_cofre, id_entidade, valor_movimentado )\n" +
                    "    VALUES (?, ?, ?, ?, ?, ?);",false);
            PreparedStatement psRs = Parametros.getConexao().getPst("select saldo from saldos_cofres where id_cofre = ? and id_entidade = ? order by data_hora_movimento desc limit 1",false);
            psRs.setInt(1, Integer.valueOf(movimentoCofre.getIdTerminalMet()));
            psRs.setInt(2, Parametros.idEntidade);
            ResultSet rs = psRs.executeQuery();
            BigDecimal saldoAtual;
            if(rs.next()){
                saldoAtual = rs.getBigDecimal(1);
            }else{
                saldoAtual = BigDecimal.ZERO;
            }
            BigDecimal saldo;
            BigDecimal valorMov;
            if(movimentoCofre.tipoOperacao.equals("1")){
                valorMov = movimentoCofre.valorMovimentado.multiply(new BigDecimal(movimentoCofre.numeroVolumes));
                saldo = saldoAtual.subtract(valorMov);
            }else{
                valorMov = movimentoCofre.valorMovimentado.multiply(new BigDecimal(movimentoCofre.numeroVolumes));
                saldo = saldoAtual.add(valorMov);
            }
            ps.setInt(1,Integer.valueOf(movimentoCofre.getIdTerminalMet()));
            ps.setDate(2, new Date(movimentoCofre.getDataHoraMovMC().getTime()));
            ps.setBigDecimal(3, saldo);
            ps.setInt(4, movimentoCofre.getId());
            ps.setInt(5, Parametros.idEntidade);
            ps.setBigDecimal(6, valorMov);
            ps.execute();
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);  
        }
        
    }
    
    private void update(MovimentoCofre movimentoCofre){
        try{
            PreparedStatement psRs = Parametros.getConexao().getPst("select valor_movimentado from saldos_cofres where id_cofre = ? and id = ?  and id_entidade = ? ",false);
            psRs.setInt(1, Integer.valueOf(movimentoCofre.getIdTerminalMet()));
            psRs.setInt(2, id);
            psRs.setInt(3, Parametros.idEntidade);
            ResultSet rs = psRs.executeQuery();
            BigDecimal saldoAtual = BigDecimal.ZERO;
            BigDecimal valorMovAtu = BigDecimal.ZERO;
            if(rs.next()){
                valorMovAtu  = rs.getBigDecimal(1);
            }
            BigDecimal novoValor = movimentoCofre.valorMovimentado.multiply(new BigDecimal(movimentoCofre.numeroVolumes));
            if(valorMovAtu.compareTo(novoValor)!=0) {
                PreparedStatement psAlt = Parametros.getConexao().getPst("UPDATE saldos_cofres set valor_movimentado = ? where id_cofre = ? and id = ?  and id_entidade = ? ",false);
                psAlt.setBigDecimal(1,novoValor);
                psAlt.setInt(2, Integer.valueOf(movimentoCofre.getIdTerminalMet()));
                psAlt.setInt(3, id);
                psAlt.setInt(4, Parametros.idEntidade);
                psAlt.execute();
                BigDecimal difMov = novoValor.subtract(valorMovAtu);
                PreparedStatement ps = Parametros.getConexao().getPst("INSERT INTO public.saldos_cofres(\n" +
                        "            id_cofre, data_saldo, saldo, id_movimento_cofre, id_entidade, valor_movimentado )\n" +
                        "    VALUES (?, ?, ?, ?, ?, ?);", false);
                psRs = Parametros.getConexao().getPst("select saldo from saldos_cofres where id_cofre = ? and id_entidade = ? order by data_hora_movimento desc limit 1", false);
                psRs.setInt(1, Integer.valueOf(movimentoCofre.getIdTerminalMet()));
                psRs.setInt(2, Parametros.idEntidade);
                rs = psRs.executeQuery();
                if (rs.next()) {
                    saldoAtual = rs.getBigDecimal(1);
                }
                BigDecimal saldo;
                if (movimentoCofre.tipoOperacao.equals("1")) {
                    saldo = saldoAtual.subtract(difMov);
                } else {
                    saldo = saldoAtual.add(difMov);
                }
                ps.setInt(1, Integer.valueOf(movimentoCofre.getIdTerminalMet()));
                ps.setDate(2, new Date(movimentoCofre.getDataHoraMovMC().getTime()));
                ps.setBigDecimal(3, saldo);
                ps.setInt(4, movimentoCofre.getId());
                ps.setInt(5, Parametros.idEntidade);
                ps.setBigDecimal(6, difMov);
                ps.execute();
            }
            /*BigDecimal saldo;
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
            ps.execute();*/
            
        }catch(SQLException ex) {
            new LogError(ex.getMessage(), ex,request);  
        }
    }
    
    private Integer getIdSaldo(MovimentoCofre movimentoCofre){
        try{
            PreparedStatement ps = Parametros.getConexao().getPst("select id from saldos_cofres where id_movimento_cofre = ? and id_entidade = ? ORDER by id",false);
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
