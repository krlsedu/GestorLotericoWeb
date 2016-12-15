/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.ocr;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author CarlosEduardo
 */
public class PosicoesObjetos {
    Integer LARGURA = 1000;
    Map<Integer,List<ObjetosOCR>> mapaLinhasObj;
    public Integer numLinhas = 0;
    Map<Integer,PosicoesObjetos> areasLinhas;
    Map<Integer,String> linhasTxt;
    public Integer xte,yte,xbe,ybe,xtd,ytd,xbd,ybd;
    public Integer nyte,nytd,nybe,nybd;
    List<String> lobj = new ArrayList<>();

    public PosicoesObjetos() {
        mapaLinhasObj = new HashMap<>();
        areasLinhas = new HashMap<>();
        linhasTxt= new HashMap<>();
    }

    public PosicoesObjetos( Integer yte, Integer ybe, Integer ytd,  Integer ybd, Integer largura) {
        this.xte = 0;
        this.yte = yte;
        this.xbe = 0;
        this.ybe = ybe;
        this.xtd = largura;
        this.ytd = ytd;
        this.xbd = largura;
        this.ybd = ybd;
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
                nyte = getY(xte,xtd,yte,ytd,0);
                nybe = getY(xbe,xbd,ybe,ybd,0);
                nytd = getY(xte,xtd,yte,ytd,1000);
                nybd = getY(xbe,xbd,ybe,ybd,1000);
                
                
            }
            int[] xs = new int[] {0,0,LARGURA,LARGURA};
            int[] ys = new int[] {nyte,nybe-3,nytd,nybd-3};
            PosicoesObjetos posicoesObjetos = new PosicoesObjetos(nyte, nybe, nytd, nybd, LARGURA);
            areasLinhas.put(numLinhas, posicoesObjetos);
        }else{
            boolean nln = true;
            for(Integer l:areasLinhas.keySet()){
                if(naLinha(areasLinhas.get(l), objetosOCR)){
                    List lob = mapaLinhasObj.get(l);
                    lob.add(objetosOCR);
                    mapaLinhasObj.put(l, lob);
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
                    nyte = getY(xte,xtd,yte,ytd,0);
                    nybe = getY(xbe,xbd,ybe,ybd,0);
                    nytd = getY(xte,xtd,yte,ytd,LARGURA);
                    nybd = getY(xbe,xbd,ybe,ybd,LARGURA);
                }
                int[] xs = new int[] {0,0,LARGURA,LARGURA};
                int[] ys = new int[] {nyte,nybe,nytd,nybd};
                Polygon linha = new Polygon(xs, ys, 4);
                PosicoesObjetos posicoesObjetos = new PosicoesObjetos(nyte, nybe, nytd, nybd, LARGURA);
                areasLinhas.put(numLinhas, posicoesObjetos);
            }
        }
        
    }
    
    private int getY(int x0,int x1,int y0,int y1,int nx){
        return ((y1-y0)/(x1-x0))*(nx-x0)+y0;
    }
    
    private int getY(int y0,int y1,int nx){
        int x0 = 0;
        int x1 = LARGURA;
        return ((y1-y0)/(x1-x0))*(nx-x0)+y0;
    }
    
    private boolean naLinha(PosicoesObjetos po,ObjetosOCR objetosOCR){
        // aqui deve ser feita a comparação.
        return false;
    }

    @Override
    public String toString() {
        return linhasTxt.values().toString();
    }

    
    
}
