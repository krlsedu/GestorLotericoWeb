/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.dados;

/**
 *
 * @author CarlosEduardo
 */
public class ColunasTabelas {
    public static String getDescricao(String coluna){
        switch(coluna.trim()){
            case "id":
                return "Código";
            case "codigo_caixa":
                return "Código da Lotérica na Caixa";
            case "nome":
                return "Nome da Lotérica";
            default:
                return coluna.trim();
        }
    }
    public static String getColuna(String descricao){
        switch(descricao.trim()){
            case "Código":
                return "id";
            case "Código da Lotérica na Caixa":
                return "codigo_caixa";
            case "Nome da Lotérica":
                return "nome";
            default:
                return descricao.trim();
        }
    }
}
