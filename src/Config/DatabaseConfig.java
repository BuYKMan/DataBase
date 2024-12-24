package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL = "jdbc:postgresql://localhost:5432/game_studio_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

    public static Connection getConnection() throws SQLException {
        try{
        return DriverManager.getConnection(URL, USER, PASSWORD);}
        catch (Exception e)
        {e.printStackTrace();
            System.out.println("baglanti basarisiz");}
        return null;
    }
}

// Base entity classes



// DAO (Data Access Object) classes


// Main application class with basic GUI
