package controller;

import view.ApiController;
import controller.scrapping.BookingScrapper;
import controller.scrapping.HotelScrapper;

public class Main {
    public static void main(String[] args) {
        HotelScrapper hotelScrapper = new BookingScrapper();
        ApiController apiController = new ApiController(hotelScrapper);
        apiController.run();
    }
}
