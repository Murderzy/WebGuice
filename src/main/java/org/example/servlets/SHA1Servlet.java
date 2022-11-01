package org.example.servlets;

//import com.google.inject.Inject;
//import org.example.services.hash.SHA1HashService;

import com.google.inject.Inject;
import org.example.services.hash.SHA1HashService;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Singleton

public class SHA1Servlet extends HttpServlet {
    @Inject
    private SHA1HashService sha1HashService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher( "WEB-INF/sha1hash.jsp" ).forward( req, res ) ;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String userText = req.getParameter("text");
        String userTextHash=sha1HashService.hash(userText);
        System.out.println(userText);
        System.out.println(userTextHash);
        req.getSession().setAttribute("userTextHash", userTextHash);
        res.sendRedirect( req.getRequestURI() ) ;
    }
}
