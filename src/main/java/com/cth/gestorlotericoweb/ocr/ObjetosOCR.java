/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.ocr;

import java.awt.Polygon;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author CarlosEduardo
 */
public class ObjetosOCR {
    final String descricao;
    final JSONArray posicao;
    public Integer xte,yte,xbe,ybe,xtd,ytd,xbd,ybd;
    public Polygon espaco;

    public ObjetosOCR(String descricao, JSONArray posicao) {
        this.descricao = descricao;
        this.posicao = posicao;
        setaPosicoes();
        int[] xs = new int[] {xte,xbe,xtd,xbd};
        int[] ys = new int[] {yte,ybe,ytd,ybd};
        espaco = new Polygon(xs, ys, 4);
    }
    
    private void setaPosicoes(){
        Integer x,y;
        boolean primeiro = true;
        for(int i=0;i<posicao.length();i++){
            JSONObject jso = new JSONObject(posicao.get(i).toString());
            x = jso.getInt("x");
            y = jso.getInt("y");
            if(primeiro){
                primeiro = false;
                xte=xbe=xtd=xbd = x;
                yte=ybe=ytd=ybd = y;
            }else{
                if(xte<x){
                    if(ytd>=y){
                        ytd = y;
                        xtd = x;
                    }else{
                        ybd = y;
                        xbd = x;
                    }
                }else{
                    if(yte>=y){
                        yte = y;
                        xte = x;
                    }else{
                        ybe = y;
                        xbe = x;
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return  descricao ;
    }
    
    
}
