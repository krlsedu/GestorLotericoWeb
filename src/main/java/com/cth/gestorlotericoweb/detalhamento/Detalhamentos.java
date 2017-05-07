package com.cth.gestorlotericoweb.detalhamento;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by CarlosEduardo on 07/05/2017.
 */
public class Detalhamentos {
	final HttpServletRequest request;
	
	public Detalhamentos(HttpServletRequest request) {
		this.request = request;
	}
	
	public String getCel(String valor){
		return "<td>"+valor+"</td>";
	}
	
	public List<String> getLinha(List<String> linhas,String valor){
		String linha;
		if(linhas.size()%2==0){
			linha = "<tr class=\"active\">\n"+valor +
					"\n</tr>";
		}else {
			linha = "<tr >\n"+valor +
					"\n</tr>";
		}
		linhas.add(linha);
		return linhas;
	}
}
