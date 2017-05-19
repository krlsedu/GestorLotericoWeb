package com.cth.gestorlotericoweb.utils;

import java.sql.Date;
import java.util.Calendar;

/**
 * Created by CarlosEduardo on 18/05/2017.
 */
public class DataHandler {
	
	public static Date getInicioMes(Date data){
		java.util.Date dt = new java.util.Date(data.getTime());
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.set(Calendar.DATE, c.getActualMinimum(Calendar.DATE));
		return new Date(c.getTime().getTime());
	}
	
	public static Date getInicioMes(){
		java.util.Date dt = new java.util.Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.set(Calendar.DATE, c.getActualMinimum(Calendar.DATE));
		return new Date(c.getTime().getTime());
	}
	
	public static Date getFimMes(Date date){
		java.util.Date dt = new java.util.Date(date.getTime());
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
		return new Date(c.getTime().getTime());
	}
	
	public static Date getFimMes(){
		java.util.Date dt = new java.util.Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
		return new Date(c.getTime().getTime());
	}
}
