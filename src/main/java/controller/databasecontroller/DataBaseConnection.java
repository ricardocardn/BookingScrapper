package controller.databasecontroller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

public class DataBaseConnection {
    private String dbPath;
    private Connection conn;

    /*
    * Class Constructor
    * @param dbPath: String with the local url of the database
    */
    public DataBaseConnection(String dbPath) {
        this.dbPath = dbPath;
        connect();
    }

    /*
    * Method that establishes a connection with the database
    * given as class params
    */
    public boolean connect() {
        String dbPath = "jdbc:sqlite:" + this.dbPath;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbPath);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());

        } finally {
            try {
                if (conn != null) {
                    this.conn = conn;
                    return true;
                }

            } catch (Exception ex) {
                System.out.printf(ex.getMessage());
            }
        }

        return false;
    }

    public Connection getConn() {
        return conn;
    }

    public String getDbPath() {
        return dbPath;
    }

    /*
     * Method that closed the opened connection with the database
     * given as class params
     */
    public boolean disconnect() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("DataBase connection closed");
                return true;

            } catch (Exception ex) {
                System.out.printf(ex.getMessage());
            }
        }

        return false;
    }

    /*
    * Method that creates a database in local src
    * @param fileName: name of the database
    */
    public boolean createDB(String fileName) {
        if (conn == null) this.connect();
        String dbPath = "jdbc:sqlite:/Users/ricardocardenes/Desktop/java-projects/Spotify_first/src/" + fileName;
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name ir " + meta.getDriverName());
                System.out.println(String.format("The data base %s has been created", fileName));

                this.conn = conn;
                this.dbPath = "/Users/ricardocardenes/Desktop/java-projects/Spotify_first/src/" + fileName;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }

        return true;
    }
}
