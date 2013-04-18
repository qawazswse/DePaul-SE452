package edu.depaul.se.servlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

/**
 * Application Lifecycle Listener implementation class ServletInialization
 *
 */
@WebListener
public class ServletInialization implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public ServletInialization() {
    }


    static {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }
	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("--> SI Creating USER table");

        try {
	    	Context initContext = new InitialContext();
	    	Context envContext  = (Context)initContext.lookup("java:/comp/env");
	    	DataSource ds = (DataSource)envContext.lookup("jdbc/TestDB");

        	System.out.println("Before regular table creation");
        	createUserTable(DriverManager.getConnection("jdbc:hsqldb:mem:.", "", ""));
        	System.out.println("After regular table creation");
        	
	    	System.out.println("Before jdbcDriver");
        	createUserTable(ds.getConnection());
        	System.out.println("After datasource table creation");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        System.out.println("--> SI Created USER table");
    }
    

	private void createUserTable(Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        stmt.executeUpdate("create table USERS(id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 100) PRIMARY KEY, name varchar(50))");
        stmt.executeUpdate("INSERT INTO users (name) values('Kid')");
        stmt.executeUpdate("INSERT INTO users (name) values('Rob')");
        stmt.executeUpdate("INSERT INTO users (name) values('Dave')");
        stmt.close();
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    }
	
}
