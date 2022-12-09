package model;

public class Address {
    private String full;
    private String postalCode;
    private String street;
    private String country;
    private String region;

    public Address(String full, String postalCode, String street, String country, String region) {
        this.full = full;
        this.postalCode = postalCode;
        this.street = street;
        this.country = country;
        this.region = region;
    }

    public Address() {}

    public String getFull() {
        return full;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStreet() {
        return street;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "Address{" +
                "full='" + full + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", street='" + street + '\'' +
                ", country='" + country + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
