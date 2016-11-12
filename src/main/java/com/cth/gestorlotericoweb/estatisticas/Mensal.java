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
public class Mensal extends Estatisticas{
    Diarias diarias;
    public Mensal(HttpServletRequest request,Diarias diarias) {
        super(request);
        this.diarias = diarias;
        calculaValorMovimentado();
    }
      
    
    private void calculaValorMovimentado(){
        this.valorApresentar = getSaldoMovimentosMes();
    }
    
    public String getSaldoMovimentosMes(){
        BigDecimal totalMovimentosDiaL = BigDecimal.ZERO;
        String sql = "select \n" +
                    "	sum(saldo_terminal) \n" +
                    "from \n" +
                    "	fechamento_terminais  \n" +
                    "where\n" +
                    "	extract(month from data_encerramento) = extract(month from now()) and\n" +
                    "	extract(year from data_encerramento) = extract(year from now()) and\n" +
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
            totalMovimentosDiaL = totalMovimentosDiaL.add(diarias.valorMovimentado);
        } catch (SQLException ex) {
            new LogError(ex.getMessage()+sql, ex, request);
        }        
        return Parser.formataComMascara(totalMovimentosDiaL);
    }
}
