package controller.databasecontroller;

import model.Hotel;

import java.sql.SQLException;
import java.util.List;

public interface StandardQuery {
    List<Object> getObjectList(String name) throws SQLException;
    Hotel getHotelIfExists(String name) throws SQLException;
}
