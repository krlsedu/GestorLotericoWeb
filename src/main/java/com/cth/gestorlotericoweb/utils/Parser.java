/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.utils;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
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
        } catch (ParseException ex) {
        }
        return null;
    }
    
    public static Timestamp toDbTimeStamp(String dataString){
        try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddhh:mm");
                java.util.Date parsedDate = dateFormat.parse(dataString.replace("T", ""));
                Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                return timestamp;
            } catch (ParseException ex) {
            }
        return null;
    }
    
    public static String toHtmlDouble(BigDecimal valor){
        try{
            DecimalFormat df = new DecimalFormat("#.##");
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setDecimalSeparator('.');
            df.setDecimalFormatSymbols(dfs); 
            valor = new BigDecimal(df.format((valor)));
            valor = valor.multiply(new BigDecimal("100"));
            String st = valor.toString();
            return st.substring(0,st.lastIndexOf("."));
            //return st.replace(",", "").replace(".", ",").replace("x", ",");
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    public static BigDecimal toBigDecimalFromHtml(String valor){
        String st = valor;
        st = st.replace(".", "");
        st = st.replace(",", ".");
        try{
            return new BigDecimal(st);
        }catch(Exception e){
            return BigDecimal.ZERO;
        }
    }
    
    public static String toDoubleSt(String valor){
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
}
