package org.example.services;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class GmailService implements EmailService {
    @Override
    public boolean send( String to, String subject, String text ) {
        Properties gmailProperties = new Properties() ;  // java.util
        gmailProperties.put( "mail.smtp.auth", "true" ) ;
        gmailProperties.put( "mail.smtp.starttls.enable", "true" ) ;
        gmailProperties.put( "mail.smtp.port", "587" ) ;
        gmailProperties.put( "mail.smtp.ssl.protocols", "TLSv1.2" ) ;
        gmailProperties.put( "mail.smtp.ssl.trust", "smtp.gmail.com" ) ;

        Session mailSession = Session.getInstance( gmailProperties ) ;  // javax.mail.Session
        // mailSession.setDebug( true ) ;   // вывод в консоль данных обмена

        try {
            Transport mailTransport = mailSession.getTransport( "smtp" ) ;
            mailTransport.connect(               // Подключаемся к почтовому серверу
                    "smtp.gmail.com",            // адрес (хост) сервера
                    "proviryalovich@gmail.com",  // ящик
                    "umfqogmkoabhzpiu" ) ;       // пароль приложения
            // Создаем сообщение (javax.mail.internet.MimeMessage;)
            MimeMessage message = new MimeMessage( mailSession ) ;
            // от кого
            message.setFrom( new InternetAddress( "proviryalovich@gmail.com" ) ) ;
            // тема
            message.setSubject( subject ) ;
            // содержание (тело)
            message.setContent( text, "text/html; charset=utf-8" ) ;
            // отправляем
            mailTransport.sendMessage( message, InternetAddress.parse( to ) ) ;
            mailTransport.close() ;
        }
        catch( MessagingException ex ) {
            System.out.println( ex.getMessage() ) ;
            return false ;
        }
        return true ;
    }
}
