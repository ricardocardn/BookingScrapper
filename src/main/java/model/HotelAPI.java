package model;

public class HotelAPI extends Hotel {
    public HotelAPI() {
        super();
    }

    public Object get(String name) {
        if (name.equalsIgnoreCase("comments")) return getReviews();
        else if (name.equalsIgnoreCase("ratings")) return getGrades();
        else if (name.equalsIgnoreCase("services")) return getServices();
        else if (name.equalsIgnoreCase("location")) return getAddress();
        return null;
    }
}
