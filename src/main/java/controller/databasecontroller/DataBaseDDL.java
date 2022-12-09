package controller.databasecontroller;

import model.Hotel;
import model.Review;

import java.sql.PreparedStatement;
import java.sql.Statement;

public class DataBaseDDL {
    private DataBaseConnection dataBaseConnection;

    public DataBaseDDL(DataBaseConnection dataBaseConnection) {
        this.dataBaseConnection = dataBaseConnection;
    }

    public void createHotelsTable() {
        String dbPath = "jdbc:sqlite:" + dataBaseConnection.getDbPath();
        String sql = "CREATE TABLE IF NOT EXISTS Hotels (\n"
                + "id NUMBER PRIMARY KEY,\n"
                + "name text NOT NULL,\n"
                + "type text,\n"
                + "stars NUMBER,\n"
                + "rating NUMBER,\n"
                + "reviews NUMBER integer,\n"
                + "address TEXT\n"
                + ");";

        if (dataBaseConnection.getConn() != null) {
            try {
                Statement stmt = dataBaseConnection.getConn().createStatement();
                stmt.execute("DROP TABLE IF EXISTS Hotels");
                stmt.execute(sql);
                System.out.println("HOTELS TABLE CREATED");

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void createReviewsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Reviews (\n"
                + "hotelId NUMBER NOT NULL,\n"
                + "title text NOT NULL,\n"
                + "score integer,\n"
                + "positive TEXT,\n"
                + "negative TEXT,\n"
                + "travellerType TEXT integer,\n"
                + "room TEXT integer,\n"
                + "nightStay integer integer,\n"
                + "date TEXT integer,\n"
                + "country TEXT integer,\n"
                + "countryCode TEXT,\n"
                + "FOREIGN KEY(hotelId) REFERENCES Hotels(id)\n"
                + ");";

        if (dataBaseConnection.getConn() != null) {
            try {
                Statement stmt = dataBaseConnection.getConn().createStatement();
                stmt.execute("DROP TABLE IF EXISTS Reviews");
                stmt.execute(sql);
                System.out.println("Reviews TABLE CREATED");

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void insertIntoHotels(Hotel hotel) {
        String sql = "INSERT INTO Hotels(id,name,type,stars,rating,reviews,address) VALUES(?,?,?,?,?,?,?)";
        try {
            if (dataBaseConnection.getConn() == null) dataBaseConnection.connect();
            PreparedStatement pstmt = dataBaseConnection.getConn().prepareStatement(sql);
            pstmt.setInt(1, hotel.getId());
            pstmt.setString(2, hotel.getName());
            pstmt.setString(3, hotel.getType());
            pstmt.setInt(4, hotel.getStars());
            pstmt.setFloat(5, hotel.getRating());
            pstmt.setInt(6, hotel.getTotalReviews());
            pstmt.setString(7, hotel.getAddress().toString());
            pstmt.executeUpdate();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void insertIntoReviews(Review review, int id) {

        String sql = "INSERT INTO Reviews(hotelId,title,score,positive,negative,travellerType,room,nightStay,date,country,countryCode) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try {
            if (dataBaseConnection.getConn() == null) dataBaseConnection.connect();
            PreparedStatement pstmt = dataBaseConnection.getConn().prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, review.getTitle());
            pstmt.setInt(3, review.getScore());
            pstmt.setString(4, review.getPositive());
            pstmt.setString(5, review.getNegative());
            pstmt.setString(6, review.getTravellerType());
            pstmt.setString(7, review.getRoom());
            pstmt.setInt(8, review.getNightsStay());
            pstmt.setString(9, review.getDate());
            pstmt.setString(10, review.getCountry());
            pstmt.setString(11, review.getCountryCode());
            pstmt.executeUpdate();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
