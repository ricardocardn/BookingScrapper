package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hotel {
    public enum Service {
        BATH, ROOM, PETS, LivingROOM, TvZONE, FOOD,
        PARKING, FrontDESK, CLEANING, BUSINESS, SECURITY,
        GENERAL, ACCESSIBILITY, SPA
    }
    private int id;
    private String name;
    private String location;
    private String type;
    private int stars;
    private float rating;
    private int totalReviews;
    private List<ShortReview> reviews;
    private Address address;
    private Map<String, List<String>> services;
    private Map<String, Float> grades;

    public Hotel(int id, String name, String location, String type, int stars, float rating, int reviews, Address address) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.type = type;
        this.stars = stars;
        this.rating = rating;
        this.totalReviews = reviews;
        this.address = address;
        this.grades = new HashMap<>();
        this.services = new HashMap<>();
    }

    public Hotel() {
        this.grades = new HashMap<>();
        this.services = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public int getStars() {
        return stars;
    }

    public float getRating() {
        return rating;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public Address getAddress() {
        return address;
    }

    public List<ShortReview> getReviews() {
        return reviews;
    }

    public Map<String, List<String>> getServices() {
        return services;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setReviews(List<ShortReview> reviews) {
        this.reviews = reviews;
    }

    public void insertService(String service, List<String> services) {
        this.services.put(service, services);
    }

    public void insertGrade(String desc, Float grade) {
        grades.put(desc, grade);
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", type='" + type + '\'' +
                ", stars=" + stars +
                ", rating=" + rating +
                ", totalReviews=" + totalReviews +
                ", reviews=" + reviews +
                ", address=" + address +
                ", services=" + services +
                ", grades=" + grades +
                '}';
    }

    public String get(String key) {
        if (key.equalsIgnoreCase("stars")) return Integer.toString(this.stars);
        else if (key.equalsIgnoreCase("type")) return this.type;
        else return null;
    }
}
