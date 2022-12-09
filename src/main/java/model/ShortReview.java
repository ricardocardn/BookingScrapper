package model;

public class ShortReview {
    private String country;
    private String positive;

    public ShortReview(String country, String positive) {
        this.country = country;
        this.positive = positive;
    }

    public ShortReview() {}

    public String getCountry() {
        return country;
    }

    public String getPositive() {
        return positive;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }

    @Override
    public String toString() {
        return "ShortReview{" +
                "country='" + country + '\'' +
                ", positive='" + positive + '\'' +
                '}';
    }
}
