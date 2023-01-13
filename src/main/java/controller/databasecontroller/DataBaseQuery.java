package controller.databasecontroller;

import com.google.gson.Gson;
import model.Hotel;
import model.HotelAPI;
import model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataBaseQuery implements StandardQuery {
    private final DataBaseConnector dataBaseConnection;

    public DataBaseQuery(DataBaseConnector dataBaseConnection) {
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

    @Override
    public Hotel getHotelIfExists(String name) throws SQLException {
        for (Object hotel: getObjectList("Hotels")) {
            if (((Hotel) hotel).getId().equalsIgnoreCase(name)) {
                return (Hotel) hotel;
            }
        }

        return null;
    }

    private Review getReview(ResultSet rs) throws SQLException {
        Review review = new Review();

        review.setHotelId(rs.getString(1));
        review.setScore(rs.getInt(3));
        review.setPositive(rs.getString(4));
        review.setNegative(rs.getString(5));
        return review;
    }

    private Hotel getHotel(ResultSet rs) throws SQLException {
        Hotel hotel = new HotelAPI();

        hotel.setId(rs.getString(1));
        hotel.setName(rs.getString(2));
        hotel.setType(rs.getString(3));
        hotel.setStars(rs.getInt(4));
        hotel.setRating(rs.getInt(5));
        hotel.setReviews(new Gson().fromJson(rs.getString(7), List.class));
        hotel.setAddress(rs.getString(8));
        hotel.setServices(new Gson().fromJson(rs.getString(9), Map.class));
        hotel.setGrades(new Gson().fromJson(rs.getString(10), Map.class));
        return hotel;
    }
}
