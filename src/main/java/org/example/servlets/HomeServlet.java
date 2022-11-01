package org.example.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
//import step.learning.services.EmailService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
// @WebServlet( "" )
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher( "WEB-INF/index.jsp" ).forward( req, resp ) ;
    }
}