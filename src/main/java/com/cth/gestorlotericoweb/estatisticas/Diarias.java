/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.estatisticas;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.parametros.Parametros;
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
public class Diarias extends Estatisticas{

    public Diarias(HttpServletRequest request) {
        super(request);
        calculaValorMovimentado();
    }
    
    private void calculaValorMovimentado(){
        this.valorMovimentado = getSaldoMovimentosDia();
        this.valorApresentar = Parser.formataComMascara(this.valorMovimentado);
    }
    
    public BigDecimal getSaldoMovimentosDia(){
        BigDecimal totalMovimentosDiaL = BigDecimal.ZERO;
        String sql = "select \n" +
                    "	sum(case when tipo_operacao_caixa in (1) then valor_movimentado else valor_movimentado * (-1) end) \n" +
                    "from \n" +
                    "	movimentos_caixas  \n" +
                    "where\n" +
                    "	date(data_hora_mov) = date(now()) and\n" +
                    "	id_entidade = ? \n" +
                    "	";
        try {
            PreparedStatement ps = Parametros.getConexao().getPst(sql,false);
            ps.setInt(1, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalMovimentosDiaL = rs.getBigDecimal(1);
            }else{
                totalMovimentosDiaL = BigDecimal.ZERO;
            }
            if(totalMovimentosDiaL==null) totalMovimentosDiaL = BigDecimal.ZERO;
            totalMovimentosDiaL = totalMovimentosDiaL.subtract(getSaldoAbertura());
            totalMovimentosDiaL = totalMovimentosDiaL.add(getSaldoOutrosMovimentos());
        } catch (SQLException ex) {
            new LogError(ex.getMessage()+sql, ex, request);
        }        
        return totalMovimentosDiaL;
    }
    
    public BigDecimal getSaldoAbertura(){
        String sql = "select \n" +
                "	troco_dia_anterior+troco_dia \n" +
                "from \n" +
                "	abertura_terminais  \n" +
                "where\n" +
                "	data_abertura = date(now()) and\n" +
                    "	id_entidade = ? \n";
        try {
            PreparedStatement ps = Parametros.getConexao().getPst(sql,false);
            ps.setInt(1, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }else{
                return BigDecimal.ZERO;
            }           
        } catch (SQLException ex) {
            new LogError(sql, ex, request);
        }
        return BigDecimal.ZERO;
    }
    
    public BigDecimal getSaldoOutrosMovimentos(){
        String sql = "SELECT \n" +
                "	sum(case when tipo_operacao_caixa in (1,3,5,7) then valor_movimentado else valor_movimentado * (-1) end)\n" +
                "FROM \n" +
                "	outros_movimentos \n" +
                                "where\n" +
                "	date(data_hora_mov) =  date(now()) and\n" +
                    "	id_entidade = ? \n";
        try {
            PreparedStatement ps = Parametros.getConexao().getPst(sql,false);
            ps.setInt(1, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String bgd = rs.getString(1);
                if(bgd==null){
                    return BigDecimal.ZERO;
                }else{
                    return rs.getBigDecimal(1);
                }
            }else{
                return BigDecimal.ZERO;
            }  
        } catch (SQLException ex) {
            new LogError(sql, ex, request);
        }
        return BigDecimal.ZERO;
    }
    
}
