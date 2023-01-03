package controller.databasecontroller;

public interface DDLdb {
    public void createTables();
    public void insertIntoTable(String name, Object object);
}
