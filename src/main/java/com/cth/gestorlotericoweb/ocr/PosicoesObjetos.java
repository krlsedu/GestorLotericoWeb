/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.ocr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author CarlosEduardo
 */
public class PosicoesObjetos {
    Integer LARGURA = 100000;
    Map<Integer,Map<Integer,ObjetosOCR>> mapaLinhasObj;
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
            Map<Integer,ObjetosOCR> m = new HashMap<>();
            m.put(objetosOCR.xte, objetosOCR);
            mapaLinhasObj.put(numLinhas, m);   
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
                nyte = getY(xte,xtd,yte,ytd,0);
                nybe = getY(xbe,xbd,ybe,ybd,0);
                nytd = getY(xte,xtd,yte,ytd,1000);
                nybd = getY(xbe,xbd,ybe,ybd,1000);
            }
            PosicoesObjetos posicoesObjetos = new PosicoesObjetos(nyte, nybe, nytd, nybd, LARGURA);
            areasLinhas.put(numLinhas, posicoesObjetos);
        }else{
            boolean nln = true;
            for(Integer l:areasLinhas.keySet()){
                if(naLinha(areasLinhas.get(l), objetosOCR)){
                    Map<Integer,ObjetosOCR> m = mapaLinhasObj.get(l);
                    m.put(objetosOCR.xte, objetosOCR);
                    mapaLinhasObj.put(l, m);
                    nln = false;
                    linhasTxt.put(numLinhas, linhasTxt.get(numLinhas)+"--+--"+objetosOCR.descricao);
                }
            }    
            if(nln){
                numLinhas++;
                linhasTxt.put(numLinhas, objetosOCR.descricao);
                lobj.add("\nF"+numLinhas+objetosOCR.descricao);
                Map<Integer,ObjetosOCR> m = new HashMap<>();
                m.put(objetosOCR.xte, objetosOCR);
                mapaLinhasObj.put(numLinhas, m);    

                this.xte = objetosOCR.xte;
                this.yte = objetosOCR.yte;
                this.xtd = objetosOCR.xtd;
                this.ytd = objetosOCR.ytd;
                this.xbe = objetosOCR.xbe;
                this.ybe = objetosOCR.ybe;
                this.xbd = objetosOCR.xbd;
                this.ybd = objetosOCR.ybd;

                if((xtd-xte)!=0){
                    nyte = getY(xte,xtd,yte,ytd,0);
                    nybe = getY(xbe,xbd,ybe,ybd,0);
                    nytd = getY(xte,xtd,yte,ytd,LARGURA);
                    nybd = getY(xbe,xbd,ybe,ybd,LARGURA);
                }
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
        int yt,yb,meio;
        boolean dentro = false;
        
        yt = getY(po.yte, po.ytd, objetosOCR.xte);
        yb = getY(po.ybe, po.ybd, objetosOCR.xte);
        meio = (yt+yb)/2;
        if(meio >= objetosOCR.yte && meio <= objetosOCR.ybe){
            dentro = true;
        }        
        
        yt = getY(po.yte, po.ytd, objetosOCR.xtd);
        yb = getY(po.ybe, po.ybd, objetosOCR.xtd);
        meio = (yt+yb)/2;
        if(meio >= objetosOCR.ytd && meio <= objetosOCR.ybd){
            dentro = true;
        }   
        return dentro;
    }

    @Override
    public String toString() {
        Set<Integer> s = mapaLinhasObj.keySet();
        List<Integer> l = new ArrayList(s);
        Collections.sort(l);
        StringBuilder sb = new StringBuilder();
        for(Integer i:l){
            sb.append("\n").append(i);
            Map<Integer,ObjetosOCR> m = mapaLinhasObj.get(i);
            Set s2 = m.keySet();
            List<Integer> l2 = new ArrayList<>(s2);
            Collections.sort(l2);
            for(Integer j:l2){
                sb.append("-+-").append(m.get(j));
            }
        }
        return sb.toString();
    }

    
    
}
