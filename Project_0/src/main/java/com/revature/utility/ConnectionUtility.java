package com.revature.utility;

import org.postgresql.Driver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// TODO 2: Create a ConnectionUtility class that will contain the getConnection method where our code will belong
public class ConnectionUtility {

    public static Connection getConnection() throws SQLException, IOException {
        // TODO 3: Grab the connection string (url), username, password for the database
        // Preferably, this will be from environment variables
        FileInputStream fis = new FileInputStream("connection.prop");
        Properties p=new Properties ();
        p.load (fis);
        String url= (String) p.get ("URL");
        String username= (String) p.get ("USER");
        String password= (String) p.get ("PASSWORD");

        // TODO 4: Register the Postgres driver with the DriverManager
        DriverManager.registerDriver(new Driver());

        // TODO 5: Get the Connection object from the DriverManager
        Connection connection = DriverManager.getConnection(url, username, password);

        return connection;
    }

}