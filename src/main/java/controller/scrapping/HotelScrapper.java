package controller.scrapping;

import model.Hotel;

import java.util.List;

public interface HotelScrapper {
    Hotel getHotel(String url, int pages) throws Exception;
}
