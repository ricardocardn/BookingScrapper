package controller.databasecontroller;

import java.sql.SQLException;
import java.util.List;

public interface Query {
    public List<Object> getObjectList(String name) throws SQLException;
}
