package org.example.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.dao.UserDAO;
import org.example.entities.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class CheckMailServlet extends HttpServlet {
    @Inject
    private UserDAO userDAO ;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String confirm = req.getParameter( "confirm" ) ;
        String userId  = req.getParameter( "userid" ) ;
        if( confirm != null ) {     // есть сообщение на подтверджение
            try {
                User user = userId == null
                        ? (User) req.getAttribute( "AuthUser" )
                        : userDAO.getUserById( userId ) ;

                if ( user == null )  // ошибка авторизации
                    throw new Exception( "Invalid user credentials" ) ;

                if ( user.getEmailCode() == null ) // почта подтверждена
                    throw new Exception( "Email already confirmed" ) ;

                if ( ! confirm.equals( user.getEmailCode() ) ) // код не подтвержден
                    throw new Exception( "Invalid code" ) ;

                if ( ! userDAO.confirmEmail( user ) )
                    throw new Exception( "DB error" ) ;

                req.setAttribute( "confirm", "OK" ) ;  // код подтвержден
            }
            catch( Exception ex ) {
                req.setAttribute( "confirmError", ex.getMessage() ) ;
            }
        }
        req.setAttribute( "pageBody", "checkmail.jsp" ) ;
        req.getRequestDispatcher( "/WEB-INF/_layout.jsp" ).forward( req, resp ) ;
    }
}