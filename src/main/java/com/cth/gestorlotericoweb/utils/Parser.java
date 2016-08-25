/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.utils;

import java.sql.Date;
import java.sql.Timestamp;
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
    
    public static String toHtmlDouble(Double valor){
        try{
            String st = String.format("%,.2f", valor);
            return st;//.replace(".", "x").replace(",", ".").replace("x", ",");
        }catch(Exception e){
            return "0,00";
        }
    }
    
    public static Double toDoubleFromHtml(String valor){
        String st = valor;
        st = st.replace(".", "");
        st = st.replace(",", ".");
        try{
            return Double.valueOf(st);
        }catch(Exception e){
            return 0.0;
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
