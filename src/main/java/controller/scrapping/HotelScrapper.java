package controller.scrapping;

import model.Hotel;

import java.util.List;

public interface HotelScrapper {
    public Hotel getHotel(String url) throws Exception;
    public List<Hotel> getHotels(String name) throws Exception;
}
