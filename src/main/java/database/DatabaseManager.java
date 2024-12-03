package database;

import models.Category;
import models.Article;
import models.User;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/rocommendation_system_databse";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection c;


    public static Statement createConnection() throws Exception {

        if (c == null) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(URL, USER, PASSWORD);
        }

        Statement s = c.createStatement();
        return s;

    }

    public static boolean iud(String query) {
        try {

            int affectedRows=createConnection().executeUpdate(query);
            return affectedRows>0;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

    }

    public static ResultSet search(String query) throws Exception {

        ResultSet r = createConnection().executeQuery(query);

        return r;

    }
    //------------------


}
