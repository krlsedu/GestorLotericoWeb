/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.utils;

import java.sql.Date;
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
    
    public static String toHtmlDouble(Double valor){
        try{
            return valor.toString().replace(".", "x").replace(",", ".").replace("x", ",");
        }catch(Exception e){
            return "0,00";
        }
    }
    public static Double toDouble(String valor){
        try{
            return Double.valueOf(valor.replace(".", "x").replace(",", ".").replace("x", ","));
        }catch(Exception e){
            return 0.0;
        }
    }
}
