/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author CarlosEduardo
 */
public class LogError{
    public LogError(String message, Throwable cause,HttpServletRequest request) {
        new  Erros(request, cause.getMessage(), StringUtils.join(cause.getStackTrace(),';'));
        throw new RuntimeException(message, cause);
    }    
}
