package it.hello.servlet;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/ResetServlet")
public class ResetServlet extends HttpServlet 
{
	
       
    
    public ResetServlet() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
			
		Cookie cookieUtente = new Cookie(WelcomeServlet.nomeCookieUtente,"");
		cookieUtente.setMaxAge(0);
		
		Cookie firstVisit = new Cookie(WelcomeServlet.nomeCookiePrimaVisita,"");
		firstVisit.setMaxAge(0);
		
		Cookie cookieNumVisite = new Cookie(WelcomeServlet.nomeCookieNumeroVisite,"");
		cookieNumVisite.setMaxAge(0);
		
		response.addCookie(cookieUtente);
		response.addCookie(firstVisit);
		response.addCookie(cookieNumVisite);
		
		
		response.sendRedirect("index.html");
		
		
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
