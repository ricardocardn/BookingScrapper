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
    private String id;
    private String name;
    private String type;
    private int stars;
    private float rating;
    private int totalReviews;
    private List<Review> reviews;
    private String address;
    private Map<String, List<String>> services;
    private Map<String, Float> grades;

    public Hotel(String id, String name, String location, String type, int stars, float rating, int reviews, String address) {
        this.id = id;
        this.name = name;
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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public String getAddress() {
        return address;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public Map<String, List<String>> getServices() {
        return services;
    }

    public Map<String, Float> getGrades() {
        return grades;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setAddress(String address) {
        this.address = address;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setServices(Map<String, List<String>> services) {
        this.services = services;
    }

    public void setGrades(Map<String, Float> grades) {
        this.grades = grades;
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

    public Object get(String key) {
        if (key.equalsIgnoreCase("stars")) return Integer.toString(this.stars);
        else if (key.equalsIgnoreCase("type")) return this.type;
        else return null;
    }
}
