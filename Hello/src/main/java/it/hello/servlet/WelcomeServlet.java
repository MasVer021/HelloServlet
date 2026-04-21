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
public class WelcomeServlet extends HttpServlet 
{
  
	protected static String nomeCookieUtente = "utente";
	protected static String nomeCookiePrimaVisita = "firstVisit";
	protected static String nomeCookieNumeroVisite = "numVisite";
    public WelcomeServlet() 
    {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
	
		//Impostazioni header e content type
		response.setContentType("text/html");
		response.setHeader("Content-Language","it-IT");
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Refresh","300");
		

		//Mapping cookie per ricerca agevole per nome
		Map<String, Cookie> cookie = new HashMap<>();
		if(request.getCookies() != null)
			for(var c : request.getCookies())
			{
				cookie.put(c.getName(),c);
			}
			
		//Estrazione cookie se già presenti
		Cookie cookieUtente =  cookie.get(nomeCookieUtente);
		Cookie firstVisit = cookie.get(nomeCookiePrimaVisita);
		Cookie cookieNumVisite = cookie.get(nomeCookieNumeroVisite);
		
		
		if(cookieNumVisite == null)
			cookieNumVisite = new Cookie(nomeCookieNumeroVisite,"0");
		
		//incremento e aggiornamento valore numero visite
		int valueNumeroVisite = Integer.parseInt(cookieNumVisite.getValue()) +1; // da fare controllo errori di conversione;
		
		cookieNumVisite = new Cookie(nomeCookieNumeroVisite, String.valueOf(valueNumeroVisite));
		
		response.addCookie(cookieNumVisite);
		
		//recupero nazione per bandiera e lingua per traduzione
		String Nazione = request.getLocale().getCountry();
		String Lingua = request.getLocale().getLanguage();
		
		//recupero dispositivo per dark mode
		String dispositivoUtente = request.getHeader("User-Agent");
		String style="";
		
		if(dispositivoUtente.contains("android"))
			style = "css/Dark.css";
		
		
		//pagina di base
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
		
		//se non trovato (prima visita) settaggio cookie
		if(!trovato)
		{
			cookieUtente = new Cookie(nomeCookieUtente,request.getParameter("fname"));
			cookieUtente.setMaxAge(60*60*24*7);
			firstVisit = new Cookie(nomeCookiePrimaVisita,LocalDateTime.now().toString());
			firstVisit.setMaxAge(60*60*24*7);
			response.addCookie(cookieUtente);
			response.addCookie(firstVisit);
			saluto = traduzioneCiao.get(Lingua);
		}
		
		
		//append del messaggio nella risposta
		response.getWriter().append(String.format(messaggio,style,getFlagHtmlEntities(Nazione),saluto,cookieUtente.getValue(),firstVisit.getValue(),request.getRemoteAddr(),valueNumeroVisite));	
	
	}

	
	//richiama il metodo do get
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}
	
	//ricava codice bandiera data il codice della nazione
	private String getFlagHtmlEntities(String CodiceNazione) 
	{
	    String s = "";
	    for (char c : CodiceNazione.toUpperCase().toCharArray()) 
	    {
	        int codePoint = 0x1F1E6 + (c - 'A');
	        s+="&#"+String.valueOf(codePoint)+";";
	    }
	    return s;
	}
	
	
	//traduzione dei messaggi tramite Map  futura implementazione traduzione attraverso API
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


