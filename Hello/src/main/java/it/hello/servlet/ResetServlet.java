package it.hello.servlet;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/ResetServlet")
public class ResetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public ResetServlet() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		Cookie [] cookie = request.getCookies();
		
		Cookie cookieUtente = new Cookie("utente","");
		cookieUtente.setMaxAge(0);
		
		Cookie firstVisit = new Cookie("firstVisit","");
		firstVisit.setMaxAge(0);
		
		Cookie cookieNumVisite = new Cookie("numVisite","");
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
