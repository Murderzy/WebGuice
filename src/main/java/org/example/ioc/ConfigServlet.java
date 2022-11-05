package org.example.ioc;

import com.google.inject.servlet.ServletModule;
import org.example.filters.AuthFilter;
import org.example.filters.CharsetFilter;
import org.example.filters.DataFilter;
import org.example.servlets.HomeServlet;
import org.example.servlets.MD5Servlet;
import org.example.servlets.RegUserServlet;
import org.example.servlets.SHA1Servlet;

public class ConfigServlet extends ServletModule {
    @Override
    protected void configureServlets() {
        // Программная замена web.xml - конфигурация фильтров ...
        filter( "/*" ).through( CharsetFilter.class ) ;
        filter( "/*" ).through( DataFilter.class ) ;
        filter( "/*" ).through( AuthFilter.class ) ;
        //filter( "/*" ).through( DemoFilter.class ) ;

        // ...  и сервлетов
        //serve( "/filters" ).with( FiltersServlet.class ) ;
        //serve( "/servlet" ).with( ViewServlet.class ) ;
        serve( "/register/" ).with( RegUserServlet.class ) ;
        //serve( "/image/*" ).with( DownloadServlet.class ) ;
        //serve( "/profile" ).with( ProfileServlet.class ) ;
        //serve( "/checkmail/" ).with( CheckMailServlet.class ) ;
        serve( "/" ).with( HomeServlet.class ) ;
        serve("/md5hash").with(MD5Servlet.class);
        serve("/sha1hash").with(SHA1Servlet.class);
    }
}