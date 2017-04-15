package com.cth.gestorlotericoweb;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by CarlosEduardo on 09/04/2017.
 */
public class Sessao {
	public static void invalidaSessao(HttpServletRequest request){
		request.getSession(false).invalidate();
	}
}
