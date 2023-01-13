package controller.databasecontroller;

import com.google.gson.Gson;
import model.Hotel;
import model.Review;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseDDL implements StandardDDL {
    private static DataBaseDDL dataBaseDDL;
    private final DataBaseConnector dataBaseConnection;
    private final int id = 0;

    public DataBaseDDL(DataBaseConnector dataBaseConnection) {
        this.dataBaseConnection = dataBaseConnection;
        dataBaseDDL = this;
    }

    public static DataBaseDDL getInstance() throws SQLException {
        if (dataBaseDDL != null) {
            return dataBaseDDL;
        } else {
            throw new SQLException();
        }
    }

    @Override
    public void createTables() {
        createHotelsTable();
        createReviewsTable();
    }

    private void createHotelsTable() {
        String dbPath = "jdbc:sqlite:" + dataBaseConnection.getDbPath();
        String sql = "CREATE TABLE IF NOT EXISTS Hotels (\n"
                + "id TEXT NOT NULL,\n"
                + "name text NOT NULL,\n"
                + "type text,\n"
                + "stars NUMBER,\n"
                + "rating NUMBER,\n"
                + "reviews NUMBER integer,\n"
                + "reviewsData TEXT integer,\n"
                + "address TEXT,\n"
                + "services TEXT NOT NULL,\n"
                + "grades TEXT NOT NULL\n"
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

    private void createReviewsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Reviews (\n"
                + "hotelId TEXT NOT NULL,\n"
                + "score integer,\n"
                + "positive TEXT,\n"
                + "negative TEXT,\n"
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

    @Override
    public void insertIntoTable(String name, Object object) {
        if (name.equalsIgnoreCase("hotels")) insertIntoHotels((Hotel) object);
        else if (name.equalsIgnoreCase("reviews")) insertIntoReviews((Review) object);
    }

    private void insertIntoHotels(Hotel hotel) {
        String sql = "INSERT INTO Hotels(id,name,type,stars,rating,reviews,reviewsData,address,services,grades) VALUES(?,?,?,?,?,?,?,?,?,?)";
        try {
            if (dataBaseConnection.getConn() == null) dataBaseConnection.connect();
            PreparedStatement pstmt = dataBaseConnection.getConn().prepareStatement(sql);
            pstmt.setString(1, hotel.getId());
            pstmt.setString(2, hotel.getName());
            pstmt.setString(3, hotel.getType());
            pstmt.setInt(4, hotel.getStars());
            pstmt.setFloat(5, hotel.getRating());
            pstmt.setInt(6, hotel.getTotalReviews());
            pstmt.setString(7, new Gson().toJson(hotel.getReviews()));
            pstmt.setString(8, hotel.getAddress());
            pstmt.setString(9, new Gson().toJson(hotel.getServices()));
            pstmt.setString(10, new Gson().toJson(hotel.getGrades()));
            pstmt.executeUpdate();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void insertIntoReviews(Review review) {

        String sql = "INSERT INTO Reviews(hotelId,score,positive,negative) VALUES(?,?,?,?)";
        try {
            if (dataBaseConnection.getConn() == null) dataBaseConnection.connect();
            PreparedStatement pstmt = dataBaseConnection.getConn().prepareStatement(sql);
            pstmt.setString(1, review.getHotelId());
            pstmt.setInt(2, review.getScore());
            pstmt.setString(3, review.getPositive());
            pstmt.setString(4, review.getNegative());
            pstmt.executeUpdate();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
