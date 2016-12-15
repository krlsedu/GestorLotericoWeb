/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.ocr;

import com.cth.gestorlotericoweb.LogError;
import com.google.gson.JsonSyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author CarlosEduardo
 */
public class DadosOcr {
    final HttpServletRequest request;
    final String txtJson;
    JSONObject jsonObject;
    JSONArray jsonArray;
    Set<String> keySet = new HashSet<>();
    Stack<String> pilhaKeyNames = new Stack<>();
    Map<String,Integer> arraysCont;
    public PosicoesObjetos objetos;

    public DadosOcr(String txtJson,HttpServletRequest request) {
        arraysCont = new HashMap<>();
        this.txtJson = txtJson;
        this.request = request;
        jsonObject = new JSONObject(txtJson);
        jsonArray = new JSONArray(jsonObject.get("responses").toString());
        //setKeyNames(jsonArray,"");
        posiciona(jsonObject.getJSONArray("responses"));
    }    
    
    private void posiciona(JSONArray arr){
        objetos = new PosicoesObjetos();
        for (int i=0; i<arr.length(); i++) {
            JSONObject jso = arr.getJSONObject(i);
            JSONArray jsa = jso.getJSONArray("textAnnotations");
            //pilhaKeyNames.add(jsa.length()+"");
            for (int j=1; j<jsa.length(); j++) {
                JSONObject jsot = jsa.getJSONObject(j);
                if(!jsot.has("locale")){
                    ObjetosOCR objetosOCR = new ObjetosOCR(jsot.getString("description"), jsot.getJSONObject("boundingPoly").getJSONArray("vertices"));
                    try{
                        objetos.posiciona(objetosOCR);
                        pilhaKeyNames.add(jsot.getString("description"));
                    }catch(Exception e){
                        pilhaKeyNames.add(e.getLocalizedMessage());
                    }
                }
            }
        }
    }
    
    private void setJsonObj(JSONObject js){
        jsonObject = js;
    }
    
    private void setJsonArray(JSONArray js){
        jsonArray = js;
    }
    
    public void addJsonObjToArray(JSONObject jObj){
        jsonArray.put(jObj);
    }
    
    private void toJson(String txt){
        try{
            Object obj;
            JSONParser parser = new JSONParser();
            obj = parser.parse(txt);
            if(obj instanceof JSONArray){
                jsonArray = (JSONArray) obj;
            }else{
                jsonObject = new JSONObject(obj);
                addJsonObjToArray(jsonObject);           
            }
        }catch(ParseException|JsonSyntaxException e){
            new LogError(e.getMessage(), e,request); 
        }
    }
    
    
    public void setItemArray(String label,String values[]){
        JSONArray list = new JSONArray();
        list.put(Arrays.asList(values));
        jsonObject.put(label, list);
    }
    
    public JSONObject getJsonObj(){
        return jsonObject;
    }
    
    public JSONObject getJsonObjInArray(Integer index){
        return new JSONObject(jsonArray.get(index));
    }
    
    public JSONArray getJsonArray() {
        return jsonArray;
    }
    
    public String getJsonStr(){
        return jsonArray.toString();
    }
    
    public JSONArray getJSONArray(){
        return jsonArray;
    }
    
    public Set<String> getKeySet(JSONObject jsObj){
        return jsObj.keySet();
    }
    
    public Set<String> getKeySet(){
        return jsonObject.keySet();
    }
    
    public Object getKeyValue(String key){
        return jsonObject.get(key);
        //if( typeof oValue == 'object' || typeof oValue == 'array' )
    }
    
    public static boolean checkKey(JSONObject object, String searchedKey) {
        boolean exists = object.has(searchedKey);
        if(!exists) {      
             Set<String> keys = object.keySet();
             for(String key : keys){
                if ( object.get(key) instanceof JSONObject ) {
                    exists = checkKey(new JSONObject(object.get(key)), searchedKey);
                }
             }
        }
        return exists;
    }
    
    public boolean checkContainsChild(JSONObject object, String Key){
        boolean exists =false;
        Set<String> keys = object.keySet();
        for(String key : keys){
            try{
                JSONObject obj1 = new JSONObject(object.get(key).toString());
                if(key.trim().equalsIgnoreCase(Key.trim())){
                    return true;
                }else{
                    if (checkContainsChild(new JSONObject(object.get(key)), Key)) {
                        return true;
                    }
                }
            }catch(Exception e){
                try{
                    JSONArray ja1 = new JSONArray(object.get(key).toString());
                    return true;
                }catch(Exception ex){
                    return true;
                }
            }
        }
        return exists;
    }
    
    public boolean checkIsArray(JSONObject object, String Key){
        Object objVal = object.get(Key);
        return objVal instanceof JSONArray;
    }
    
    private void setKeyNames(JSONArray arr,String pref){
        for (int i=0; i<arr.length(); i++) {
            try{
                setKeyNames(new JSONObject(arr.get(i).toString()),pref+i+".");                
            }catch(Exception e){
                try{
                    setKeyNames(new JSONArray(arr.get(i).toString()), pref+i+"."); 
                    if(!pref.trim().equals("")){
                        arraysCont.put(pref, i);
                    }               
                }catch(Exception ex){
                    pilhaKeyNames.add(pref+i);
                }
            }
        }
    }
    
    public void setKeyNames(JSONObject obj,String pref){
        Set<String> keys = obj.keySet();
        keys.stream().forEach((key) -> {
            if(checkContainsChild(obj,key)){
                try{
                    setKeyNames(new JSONObject(obj.get(key).toString()),pref+key+".");
                }catch(Exception e){
                    try{
                        setKeyNames(new JSONArray(obj.get(key).toString()), pref+key+".");
                    }catch(Exception d){    
                        pilhaKeyNames.add(pref+key);                    
                    }
                }
            }else{
                pilhaKeyNames.add(pref+key);
            }
        });
        //pilhaKeyNames.add("o.O"+obj.toString());
    }
    
    public void setKeyNames(String pref){
        Set<String> keys = jsonObject.keySet();
        for(String key:keys){
            if(checkContainsChild(jsonObject,key)){
                setKeyNames(new JSONObject(jsonObject.get(key)),pref+key+".");
            }else{
                pilhaKeyNames.add(pref+key);
            }
        }
    }
    
    public Stack<String> getPilhaKeyNames(){
        return pilhaKeyNames;
    }
    
    public void printPilha(){
        String vars[]=new String[pilhaKeyNames.size()];
        pilhaKeyNames.toArray(vars);
        for(String st:vars){
            System.out.println(st); 
        }
    }
    
    public void printPilhaComKeyValue(){
        String vars[]=new String[pilhaKeyNames.size()];
        pilhaKeyNames.toArray(vars);
        for(String st:vars){
            System.out.println(st); 
            System.out.println("--"+getSubKey(st));
        }
    }
    
    public Object getSubKey(String Key){
        if(Key.contains(".")){
            String keyMap[] = Key.split("\\.");
            Object ob = null;
            JSONObject jobj = jsonObject;
            for(String km:keyMap){
                if(checkContainsChild(jobj,km)){
                    ob = jobj.get(km);
                    jobj= new JSONObject(ob);  
                }else{
                    ob = jobj.get(km);
                }
            }
            return ob;
        }else{
            return getKeyValue(Key);
        }
    }
    
    public Boolean getExistSubKey(String Key){
        if(Key.contains(".")){
            String keyMap[] = Key.split("\\.");
            Object ob = null;
            JSONObject jobj = jsonObject;
            String kmf="";
            for(String km:keyMap){
                kmf = km;
                if(checkContainsChild(jobj,km)){                    
                    ob = jobj.get(km);
                    jobj= new JSONObject(ob);  
                }else{
                    return checkKey(jobj, km);
                }
            }
             return checkKey(jobj, kmf);
        }else{
            return checkKey(jsonObject, Key);
        }
    }
    
    public Stack<String> getPilhaItensDefault(String Key){
        Stack<String> pilhaItens = new Stack<>();
        JSONObject jsobj = new JSONObject(getSubKey(Key));
        for(Object st:jsobj.keySet()){
            if(!checkContainsChild(jsobj,st.toString())){
                if(!checkIsArray(jsobj, st.toString())){
                    pilhaItens.add(st.toString());
                }
            }
        }
        if(pilhaItens.empty()){
            pilhaItens.add("");
        }
        return pilhaItens;
    }
    
    public Stack<String> getPilhaItensFk(String Key){
        JSONObject jsobj = new JSONObject(getSubKey(Key));
        Stack<String> pilhaItens = new Stack<>();
        for(Object st:jsobj.keySet()){
            if(checkContainsChild(jsobj,st.toString())){
                //if(!checkIsArray(jsobj, st.toString())){
                    //if(!st.toString().equalsIgnoreCase("criterio"))
                        pilhaItens.add(st.toString());
                //}
            }
        }
        if(pilhaItens.empty()){
            pilhaItens.add("");
        }
        return pilhaItens;
    }
    
    public static Boolean ehIgual(JSONObject js1,JSONObject js2){
        Set keys = js1.keySet();
        Boolean iguais = true;
        for(Object key:keys){
            Object obj1 = js1.get(String.valueOf(key));
            Object obj2 = js2.get(String.valueOf(key));
            if(!Objects.equals(obj2, obj1)){
                iguais = false;
            }
        }
        return iguais;
    }
    
    public static Boolean ehIgual(JSONObject js1,JSONObject js2,Set keys){
        Boolean iguais = true;
        for(Object key:keys){
            Object obj1 = js1.get(String.valueOf(key));
            Object obj2 = js2.get(String.valueOf(key));
            if(!Objects.equals(obj2, obj1)){
                iguais = false;
            }
        }
        return iguais;
    }
    
}
