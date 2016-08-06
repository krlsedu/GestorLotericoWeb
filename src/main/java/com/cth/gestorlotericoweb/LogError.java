/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author CarlosEduardo
 */
public class LogError extends RuntimeException{
    public LogError(String message, Throwable cause,HttpServletRequest request) {
        super(message, cause);
    }    
}
