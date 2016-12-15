/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.ocr;

import java.awt.Polygon;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author CarlosEduardo
 */
public class PosicoesObjetos {
    Map<Integer,List<ObjetosOCR>> mapaLinhasObj;
    public Integer numLinhas = 0;
    Map<Integer,Polygon> areasLinhas;
    Map<Integer,String> linhasTxt;
    public Integer xte,yte,xbe,ybe,xtd,ytd,xbd,ybd;
    public Integer nyte,nytd,nybe,nybd;
    List<String> lobj = new ArrayList<>();

    public PosicoesObjetos() {
        mapaLinhasObj = new HashMap<>();
        areasLinhas = new HashMap<>();
        linhasTxt= new HashMap<>();
    }
    
    public void posiciona(ObjetosOCR objetosOCR){
        if(mapaLinhasObj.isEmpty()){
            numLinhas ++;
            List<ObjetosOCR> lo = new ArrayList<>();
            lo.add(objetosOCR);
            mapaLinhasObj.put(numLinhas, lo);   
            linhasTxt.put(numLinhas, objetosOCR.descricao);
            
            this.xte = objetosOCR.xte;
            this.yte = objetosOCR.yte;
            this.xtd = objetosOCR.xtd;
            this.ytd = objetosOCR.ytd;
            this.xbe = objetosOCR.xbe;
            this.ybe = objetosOCR.ybe;
            this.xbd = objetosOCR.xbd;
            this.ybd = objetosOCR.ybd;
            
            if((xtd-xte)!=0){
                /*double inc = (ytd-yte)/(xtd-xte);
                yte += (int) (xtd*inc);
                ybe += (int) (xbd*inc);
                
                ytd += (int) ((10000-xtd)*inc);
                ybd += (int) ((10000-xbd)*inc);*/                
                
                nyte =((ytd-yte)/(xtd-xte))*(0-xte)+yte; 
                nybe =((ybd-ybe)/(xbd-xbe))*(0-xbe)+ybe; 

                nytd =((ytd-yte)/(xtd-xte))*(xtd-1000)+ytd; 
                nybd =((ybd-ybe)/(xbd-xbe))*(xbd-1000)+ybd; 
                
                
            }
            int[] xs = new int[] {0,0,10000,10000};
            int[] ys = new int[] {nyte,nybe-3,nytd,nybd-3};
            Polygon linha = new Polygon(xs, ys, 4);
            areasLinhas.put(numLinhas, linha);
        }else{
            boolean nln = true;
            for(Integer l:areasLinhas.keySet()){
                Area a = new Area(areasLinhas.get(l));
                a.intersect(new Area(objetosOCR.espaco));
                if(!a.isEmpty()){
                    List lob = mapaLinhasObj.get(l);
                    lob.add(objetosOCR);
                    mapaLinhasObj.put(l, lob);
                    lobj.add("V"+numLinhas+objetosOCR.descricao);
                    lobj.add("+++"+areasLinhas.get(l).getBounds2D().toString()+" --- "+objetosOCR.espaco.getBounds2D().toString());
                    nln = false;
                    linhasTxt.put(numLinhas, linhasTxt.get(numLinhas)+"--+--"+objetosOCR.descricao);
                }
            }    
            if(nln){
                numLinhas++;
                linhasTxt.put(numLinhas, objetosOCR.descricao);
                lobj.add("\nF"+numLinhas+objetosOCR.descricao);
                List<ObjetosOCR> lo = new ArrayList<>();
                lo.add(objetosOCR);
                mapaLinhasObj.put(numLinhas, lo);   

                this.xte = objetosOCR.xte;
                this.yte = objetosOCR.yte;
                this.xtd = objetosOCR.xtd;
                this.ytd = objetosOCR.ytd;
                this.xbe = objetosOCR.xbe;
                this.ybe = objetosOCR.ybe;
                this.xbd = objetosOCR.xbd;
                this.ybd = objetosOCR.ybd;

                if((xtd-xte)!=0){
                    /*double inc = (ytd-yte)/(xtd-xte);
                    yte += (int) (xtd*inc);
                    ybe += (int) (xbd*inc);

                    ytd += (int) ((10000-xtd)*inc);
                    ybd += (int) ((10000-xbd)*inc);*/
                
                    nyte =((ytd-yte)/(xtd-xte))*(0-xte)+yte; 
                    nybe =((ybd-ybe)/(xbd-xbe))*(0-xbe)+ybe; 

                    nytd =((ytd-yte)/(xtd-xte))*(xtd-1000)+ytd; 
                    nybd =((ybd-ybe)/(xbd-xbe))*(xbd-1000)+ybd; 

                }
                int[] xs = new int[] {0,0,10000,10000};
                int[] ys = new int[] {nyte,nybe-3,nytd,nybd-3};
                Polygon linha = new Polygon(xs, ys, 4);
                areasLinhas.put(numLinhas, linha);
                lobj.add("++++"+areasLinhas.get(numLinhas).getBounds2D().toString()+" --- "+objetosOCR.espaco.getBounds2D().toString());
            }
        }
        
    }

    @Override
    public String toString() {
        return linhasTxt.values().toString();
    }

    
    
}
