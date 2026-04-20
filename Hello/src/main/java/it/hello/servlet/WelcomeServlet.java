package it.hello.servlet;


import java.io.IOException;
import java.net.http.HttpClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.time.*;
import java.util.*;

@WebServlet("/WelcomeServlet")
public class WelcomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
    public WelcomeServlet() {
        super();
       
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
	
		response.setContentType("text/html");
		response.setHeader("Content-Language","it-IT");
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Refresh","300");
		

		Map<String, Cookie> cookie = new HashMap<>();
		
		for(var c : request.getCookies())
		{
			cookie.put(c.getName(),c);
		}
			
		
		Cookie cookieUtente =  cookie.get("utente");
		Cookie firstVisit = cookie.get("firstVisit");
		Cookie cookieNumVisite = cookie.get("numVisite");
		
		
		if(cookieNumVisite == null)
			cookieNumVisite = new Cookie("numVisite","0");
		
		int valueNumeroVisite = Integer.parseInt(cookieNumVisite.getValue()) +1; // da fare controllo errori di conversione;
		
		cookieNumVisite = new Cookie("numVisite", String.valueOf(valueNumeroVisite));
		
		response.addCookie(cookieNumVisite);
		
		
		String Nazione = request.getLocale().getCountry();
		String Lingua = request.getLocale().getLanguage();
		
		String dispositivoUtente = request.getHeader("User-Agent");
		String style="";
		
		if(dispositivoUtente.contains("android"))
			style = "css/Dark.css";
		
		
		
		String messaggio = 	"""
								<html>
									<head>
										<link rel="styleSheet" href="%s">
									</head>
									<body>
									<h1>%s %s %s</h1>
										<br>Primo accesso %s
										<br>Il tuo ip è %s
										<br>Numero visite: %d
									</body>
								</html>
							""";
		
		boolean trovato = cookieUtente!=null;
		
		String saluto = traduzioneBentornato.get(Lingua);
		
		if(!trovato)
		{
			cookieUtente = new Cookie("utente",request.getParameter("fname"));
			cookieUtente.setMaxAge(60*60*24*7);
			firstVisit = new Cookie("firstVisit",LocalDateTime.now().toString());
			firstVisit.setMaxAge(60*60*24*7);
			response.addCookie(cookieUtente);
			response.addCookie(firstVisit);
			saluto = traduzioneCiao.get(Lingua);
		}
		
	
		response.getWriter().append(String.format(messaggio,style,getFlagHtmlEntities(Nazione),saluto,cookieUtente.getValue(),firstVisit.getValue(),request.getRemoteAddr(),valueNumeroVisite));	
	
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}
	
	private String getFlagHtmlEntities(String countryCode) 
	{
	    String s = "";
	    for (char c : countryCode.toUpperCase().toCharArray()) 
	    {
	        int codePoint = 0x1F1E6 + (c - 'A');
	        s+="&#"+String.valueOf(codePoint)+";";
	    }
	    return s;
	}
	
	private static final Map<String, String> traduzioneCiao = Map.of(
		    "it", "Ciao",
		    "en", "Hello",
		    "fr", "Salut",
		    "es", "Hola",
		    "de", "Hallo",
		    "pt", "Olá"
		);


		private static final Map<String, String> traduzioneBentornato = Map.of(
		    "it", "Bentornato",
		    "en", "Welcome back",
		    "fr", "Bon retour",
		    "es", "Bienvenido de nuovo",
		    "de", "Willkommen zurück",
		    "pt", "Bem-vindo de volta"
		);
	
	
}


