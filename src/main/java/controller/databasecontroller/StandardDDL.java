package controller.databasecontroller;

public interface StandardDDL {
    void createTables();
    void insertIntoTable(String name, Object object);
}
