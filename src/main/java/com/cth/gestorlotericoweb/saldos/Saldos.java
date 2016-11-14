/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.saldos;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author CarlosEduardo
 */
public class Saldos {
    public Integer id;
    final HttpServletRequest request;

    public Saldos(HttpServletRequest request) {
        this.request = request;
    }
      
}
