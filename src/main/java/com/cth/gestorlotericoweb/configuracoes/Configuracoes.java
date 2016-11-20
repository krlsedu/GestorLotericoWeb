/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.configuracoes;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author CarlosEduardo
 */
public class Configuracoes {
    public final HttpServletRequest request;
    public Configuracoes(HttpServletRequest request) {
        this.request = request;
    }
    
}
