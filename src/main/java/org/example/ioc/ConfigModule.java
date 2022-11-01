package org.example.ioc;

import com.google.inject.AbstractModule;
import org.example.services.hash.HashService;
import org.example.services.hash.MD5HashService;
import org.example.services.hash.SHA1HashService;
//import org.example.services.hash.SHA1HashService;
//import step.learning.services.*;
//import step.learning.services.hash.*;

public class ConfigModule extends AbstractModule {
    @Override
    protected void configure() {
        // Конфигурация служб-поставщиков
        //bind( DataService.class ).to( MysqlDataService.class ) ;
        bind( HashService.class ).to( SHA1HashService.class ) ;
        //bind( HashService.class ).to( MD5HashService.class ) ;
        //bind( EmailService.class ).to( GmailService.class ) ;
    }
}
