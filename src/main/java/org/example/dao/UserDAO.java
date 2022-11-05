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

    @Inject
    public UserDAO( DataService dataService, HashService hashService) {
        this.dataService = dataService ;
        this.hashService = hashService ;

        this.connection = dataService.getConnection() ;
    }

    public boolean updateUser( User user ) {

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
                String expectedHash = pass;
                // pass - открытый пароль, user.pass - Hash(pass,user.salt)
                if(user.getSalt()!=null) {
                    expectedHash = this.hashPassword(pass, user.getSalt());
                }
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