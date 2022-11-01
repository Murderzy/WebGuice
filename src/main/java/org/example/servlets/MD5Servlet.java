package org.example.servlets;

import com.google.inject.Inject;
import org.example.services.hash.HashService;
import org.example.services.hash.MD5HashService;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Singleton

public class MD5Servlet extends HttpServlet {
    @Inject
    private MD5HashService md5HashService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher( "WEB-INF/md5hash.jsp" ).forward( req, res ) ;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String userText = req.getParameter("text");
        String userTextHash=md5HashService.hash(userText);
        System.out.println(userText);
        System.out.println(userTextHash);
        req.getSession().setAttribute("userTextHashmd", userTextHash);
        res.sendRedirect( req.getRequestURI() ) ;
    }
}
