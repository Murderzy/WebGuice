package org.example.dao;

import org.example.entities.User;
import org.example.services.DataService;
import org.example.services.EmailService;
import org.example.services.hash.HashService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class UserDAO {
    private final Connection connection ;
    private final HashService hashService ;
    private final DataService dataService ;
    private final EmailService emailService ;
    @Inject
    public UserDAO( DataService dataService, HashService hashService, EmailService emailService ) {
        this.dataService = dataService ;
        this.hashService = hashService ;
        this.emailService = emailService ;

        this.connection = dataService.getConnection() ;
    }

    public boolean confirmEmail( User user ) {
        if( user.getId() == null ) return false ;
        String sql = "UPDATE Users SET email_code = NULL WHERE id = ?";
        try( PreparedStatement prep = dataService.getConnection().prepareStatement( sql ) ) {
            prep.setString( 1, user.getId() ) ;
            prep.executeUpdate() ;
        }
        catch( SQLException ex ) {
            System.out.println( "UserDAO::confirmEmail " + ex.getMessage() ) ;
            System.out.println( sql + " " + user.getId() ) ;
            return false;
        }
        return true;
    }

    public boolean updateUser( User user ) {
        if( user == null || user.getId() == null ) return false ;

        // Задание: сформировать запрос, учитывая только те данные, которые не null (в user)
        Map<String, String> data = new HashMap<>() ;
        if( user.getName() != null ) data.put( "name", user.getName() ) ;
        if( user.getLogin() != null ) data.put( "login", user.getLogin() ) ;
        if( user.getAvatar() != null ) data.put( "avatar", user.getAvatar() ) ;
        if( user.getEmail() != null ) {
            // Обновление почты + новый код подтверждения
            user.setEmailCode( UUID.randomUUID().toString().substring( 0, 6 ) ) ;
            data.put( "email", user.getEmail() ) ;
            data.put( "email_code", user.getEmailCode() ) ;
        }
        String sql = "UPDATE Users u SET " ;
        boolean needComma = false ;
        for( String fieldName : data.keySet() ) {
            sql += String.format( "%c u.`%s` = ?", ( needComma ? ',' : ' ' ), fieldName ) ;
            needComma = true ;
        }
        sql += " WHERE u.`id` = ? " ;
        if( ! needComma ) {  // не было ни одного параметра
            return false ;
        }
        try( PreparedStatement prep = dataService.getConnection().prepareStatement( sql ) ) {
            int n = 1;
            for( String fieldName : data.keySet() ) {
                prep.setString( n, data.get( fieldName ) ) ;
                ++n ;
            }
            prep.setString( n, user.getId() ) ;
            prep.executeUpdate() ;
        }
        catch( SQLException ex ) {
            System.out.println( "UserDAO::updateUser" + ex.getMessage() ) ;
            return false ;
        }
        // Запрос к БД выполнен успешно, если нужно, отправляем код на почту
        if( user.getEmailCode() != null ) {
            String text = String.format(
                    "<h2>Hello!</h2><p>Your code is <b>%s</b></p><p>Follow <a href='http://localhost:8080/WebBasics/checkmail/?userid=%s&confirm=%s'>link</a> to confirm email</p>",
                    user.getEmailCode(), user.getId(), user.getEmailCode() ) ;
            emailService.send( user.getEmail(), "Email confirmation", text ) ;
        }
        return true ;
    }

    public User getUserById( String userId ) {
        String sql = "SELECT * FROM Users u WHERE u.`id` = ? " ;
        try( PreparedStatement prep =
                     dataService.getConnection().prepareStatement( sql ) ) {
            prep.setString( 1, userId ) ;
            ResultSet res = prep.executeQuery() ;
            if( res.next() ) {
                return new User( res ) ;
            }
        }
        catch( Exception ex ) {
            System.out.println( "UserDAO::getUserById " + ex.getMessage()
                    + "\n" + sql + " -- " + userId ) ;
        }
        return null ;
    }

    public String add( User user ) {
        // генерируем id для новой записи
        String id = UUID.randomUUID().toString() ;
        // генерируем соль
        String salt = hashService.hash( UUID.randomUUID().toString() ) ;
        // генерируем хеш пароля
        String passHash = this.hashPassword( user.getPass(), salt ) ;
        // готовим запрос (подстановка введенных данных!!)
        String sql = "INSERT INTO Users(`id`,`login`,`pass`,`name`,`salt`,`avatar`) VALUES(?,?,?,?,?,?)" ;
        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString( 1, id ) ;
            prep.setString( 2, user.getLogin() ) ;
            prep.setString( 3, passHash ) ;
            prep.setString( 4, user.getName() ) ;
            prep.setString( 5, salt ) ;
            prep.setString( 6, user.getAvatar() ) ;
            prep.executeUpdate() ;
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            return null ;
        }
        return id ;
    }

    public boolean isLoginUsed( String login ) {
        String sql = "SELECT COUNT(u.`id`) FROM Users u WHERE u.`login`=?" ;
        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString( 1, login ) ;
            ResultSet res = prep.executeQuery() ;
            res.next() ;
            return res.getInt(1) > 0 ;
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            System.out.println( sql ) ;
            return true ;
        }
    }

    public String hashPassword( String password, String salt ) {
        return hashService.hash( salt + password + salt ) ;
    }

    public User getUserByCredentials( String login, String pass ) {
        String sql = "SELECT u.* FROM Users u WHERE u.`login`=?" ;
        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString( 1, login ) ;
            ResultSet res = prep.executeQuery() ;
            if( res.next() ) {
                User user = new User( res ) ;
                // pass - открытый пароль, user.pass - Hash(pass,user.salt)
                String expectedHash = this.hashPassword( pass, user.getSalt() ) ;
                if( expectedHash.equals( user.getPass() ) ) {
                    return user ;
                }
            }
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            System.out.println( sql ) ;
        }
        return null ;
    }
    public User getUserByCredentialsOld( String login, String pass ) {
        String sql = "SELECT u.* FROM Users u WHERE u.`login`=? AND u.`pass`=?" ;
        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString( 1, login ) ;
            prep.setString( 2, this.hashPassword( pass, "" ) ) ;
            ResultSet res = prep.executeQuery() ;
            if( res.next() ) return new User( res ) ;
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            System.out.println( sql ) ;
        }
        return null ;
    }
}