/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.utils;

import com.cth.gestorlotericoweb.parametros.Parametros;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;

/**
 *
 * @author CarlosEduardo
 */
public class Parser {
    public static Date toDbDate(String dataString){
        Date dateAbertura;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsed = format.parse(dataString);
            dateAbertura = new java.sql.Date(parsed.getTime());
            return dateAbertura;
        } catch (Exception ex) {
        }
        return null;
    }
    
    public static Timestamp toDbTimeStamp(String dataString){
        try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddhh:mm");
                java.util.Date parsedDate = dateFormat.parse(dataString.replace("T", ""));
                Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                return timestamp;
            } catch (Exception ex) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date parsedDate = dateFormat.parse(dataString.replace("T", ""));
                    Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                    return timestamp;
                }catch (Exception e){
                
                }
            }
        return null;
    }
    
    public static String toHtmlBigDecimal(BigDecimal valor){
        try{
            DecimalFormat df = new DecimalFormat("#.##");
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setDecimalSeparator('.');
            df.setDecimalFormatSymbols(dfs); 
            valor = new BigDecimal(df.format((valor)));
//            valor = valor.multiply(new BigDecimal("100"));
            String st = valor.toString();
//            if(st.contains(".")){
//                return st.substring(0,st.lastIndexOf("."));
//            }else{
//                return st;
//            }
            return st;
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    public static String formataComMascara(BigDecimal valor){
        return formataComMascara(valor,"");
    }
    public static String formataComMascara(BigDecimal valor,String pref){
        try{
            DecimalFormat df = new DecimalFormat(Parametros.getPatern());
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            if(Parametros.getCodificacao().equals("PT-BR")) {
                dfs.setDecimalSeparator(',');
                dfs.setMonetaryDecimalSeparator('.');
            }else {
                dfs.setDecimalSeparator('.');
                dfs.setMonetaryDecimalSeparator(',');
            }
            df.setDecimalFormatSymbols(dfs);
            return pref+df.format((valor));
        }catch(Exception e){
            return e.getMessage();
        }
    }
    public static BigDecimal toBigDecimalFromSt(String valor){
        String st = valor;
        try{
            return new BigDecimal(st);
        }catch(Exception e){
            return BigDecimal.ZERO;
        }
    }
    
    public static BigDecimal toBigDecimalFromHtmlNNull(String valor){
        BigDecimal val = toBigDecimalFromHtml(valor);
        if (val == null) {
            return BigDecimal.ZERO;
        }
        return val;
    }
    
    public static BigDecimal toBigDecimalFromHtml(String valor){
        try {
            String st = valor;
            st = st.replace(".", "");
            st = st.replace(",", ".");
            try {
                return new BigDecimal(st);
            } catch (Exception e) {
                return BigDecimal.ZERO;
            }
        }catch (Exception e){
            return null;
        }
    }
    
    public static BigDecimal toBigDecimalFromHtmlNull(String valor){
        try{
            String st = valor;
            st = st.replace(".", "");
            st = st.replace(",", ".");
            return new BigDecimal(st);
        }catch(Exception e){
            return null;
        }
    }
    
    public static String toBigDecimalSt(String valor){
        String st = valor;
        st = st.replace(".", "x");
        st = st.replace(",", ".");
        st = st.replace("x", ",");
        return st;
    }
    
    public static Integer toInteger(String valor){
        try{
            return Integer.valueOf(valor);
        }catch(Exception e){
            return 0;
        }
    }
    
    public static Integer toIntegerNull(String valor){
        try{
            if(valor.trim().equals("0")){
                return null;
            }
            return Integer.valueOf(valor);
        }catch(Exception e){
            return null;
        }
    }
}
