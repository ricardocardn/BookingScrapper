package model;

import java.util.Arrays;

public class Review {
    private String hotelId;
    private int score;
    private String positive;
    private String negative;

    public Review(String hotelId, int score, String positive, String negative) {
        this.hotelId = hotelId;
        this.score = score;
        this.positive = positive;
        this.negative = negative;
    }

    public Review() {}

    public String getHotelId() {
        return hotelId;
    }

    public int getScore() {
        return score;
    }

    public String getPositive() {
        return positive;
    }

    public String getNegative() {
        return negative;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }

    public void setNegative(String negative) {
        this.negative = negative;
    }

    @Override
    public String toString() {
        return "Review{" +
                "hotelId='" + hotelId + '\'' +
                ", score=" + score +
                ", positive='" + positive + '\'' +
                ", negative='" + negative + '\'' +
                '}';
    }
}
