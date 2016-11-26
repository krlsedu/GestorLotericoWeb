/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author CarlosEduardo
 */
public class ConversorImagens {

    public ConversorImagens() {
    }
    
    public void convert() throws IOException{
        File img = new File("imagen.png");
        BufferedImage master = ImageIO.read(img);
        BufferedImage blackWhite;
        blackWhite = new BufferedImage(master.getWidth(), master.getHeight(), BufferedImage.TYPE_BYTE_BINARY);        
        ImageIO.write(blackWhite,"png", new File("saved.png"));
    }
}
