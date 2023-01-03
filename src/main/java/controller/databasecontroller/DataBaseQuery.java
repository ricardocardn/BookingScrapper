package controller.databasecontroller;

import com.google.gson.Gson;
import controller.databasecontroller.DataBaseConnection;
import model.Hotel;
import model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataBaseQuery implements Query {
    private DataBaseConnection dataBaseConnection;

    public DataBaseQuery(DataBaseConnection dataBaseConnection) {
        this.dataBaseConnection = dataBaseConnection;
    }

    @Override
    public List<Object> getObjectList(String name) throws SQLException {
        String sql = String.format("SELECT * FROM %s", name);

        Statement statement = dataBaseConnection.getConn().createStatement();
        ResultSet rs = statement.executeQuery(sql);

        List<Object> objects = new ArrayList<>();

        while (rs.next()) {
            if (name.equalsIgnoreCase("hotels")) {
                objects.add(getHotel(rs));

            } else {
                objects.add(getReview(rs));
            }
        }

        return objects;
    }

    private Review getReview(ResultSet rs) throws SQLException {
        Review review = new Review();

        review.setHotelId(rs.getString(1));
        review.setTitle(rs.getString(2));
        review.setScore(rs.getInt(3));
        review.setPositive(rs.getString(4));
        review.setNegative(rs.getString(5));
        review.setTravellerType(rs.getString(6));
        review.setRoom(rs.getString(7));
        review.setNightsStay(rs.getInt(8));
        review.setDate(rs.getString(9));
        review.setCountry(rs.getString(10));
        review.setCountryCode(rs.getString(11));
        return review;
    }

    private Hotel getHotel(ResultSet rs) throws SQLException {
        Hotel hotel = new Hotel();

        hotel.setId(rs.getString(1));
        hotel.setName(rs.getString(2));
        hotel.setType(rs.getString(3));
        hotel.setStars(rs.getInt(4));
        hotel.setRating(rs.getInt(5));
        hotel.setReviews(new Gson().fromJson(rs.getString(7), List.class));
        hotel.setServices(new Gson().fromJson(rs.getString(9), Map.class));
        hotel.setGrades(new Gson().fromJson(rs.getString(10), Map.class));
        return hotel;
    }
}
