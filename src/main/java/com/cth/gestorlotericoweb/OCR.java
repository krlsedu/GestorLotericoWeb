/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;

import com.cth.gestorlotericoweb.ocr.DadosOcr;
import com.google.api.services.vision.v1.Vision;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author CarlosEduardo
 */
public class OCR {
    final HttpServletRequest request;
    public String output;
    Vision  vision;

    public OCR(HttpServletRequest request) {
        this.request = request;
        try {
            output = postingtogoogle();
            DadosOcr dadosOcr = new DadosOcr(output, request);
            output = dadosOcr.objetos.toString();
        } catch (Exception ex) {
            output = "Aqui "+ex.getMessage();
        }
    }
    
   
    private String postingtogoogle() throws Exception {
        //    Base64.encode;
        String re;
        String url = "https://vision.googleapis.com/v1/images:annotate?key=AIzaSyBKY3JawFqtxuKI3z-o41xQzxa6_SsGUxc";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        String imgstr =request.getParameter("img_str");//"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";;

        //imgstr = encodeToString(img, "png");

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        String urlParameters = "{\n"
                + "  \"requests\":[\n"
                + "    {\n"
                + "      \"image\":{\n"
                + "        \"content\":\"" + imgstr.split(",")[1] + "\"\n"
                + "      },\n"
                + "      \"features\":[\n"
                + "        {\n"
                + "          \"type\":\"TEXT_DETECTION\",\n"
                + "          \"maxResults\":1\n"
                + "        }\n"
                + "      ]\n"
                + "    }\n"
                + "  ]\n"
                + "}";

        // Send post request
        con.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(urlParameters);
            wr.flush();
        }

        StringBuilder response;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }   
        }

        return response.toString();

    }
}
