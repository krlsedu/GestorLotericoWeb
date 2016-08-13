/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.banco;

/**
 *
 * @author CarlosEduardo
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Douglas Rauber<douglas.rauber@gmail.com>
 */
public class Propriedades {

    FileInputStream arquivoIn;
    Properties propSalvar = new Properties();
    String ARQUIVO = "db.properties";

    public Propriedades() {
        try {
            File f = new File(ARQUIVO);
            if (!f.exists()) {
                f.createNewFile();
            }
        } catch (IOException ex) {
            Logger.getLogger(Propriedades.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String lerPropriedade(String chave) {
        try {
            arquivoIn = new FileInputStream(ARQUIVO);
            propSalvar.load(arquivoIn);
            String ret = propSalvar.getProperty(chave);
            arquivoIn.close();
            if (ret == null)
                ret = "";
            return ret;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não encontrado o db.properties\n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return "";
    }

}