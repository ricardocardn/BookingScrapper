package controller;

import controller.api.ApiController;
import controller.scrapping.BookingScrapper;
import controller.scrapping.HotelScrapper;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        HotelScrapper hotelScrapper = new BookingScrapper();
        ApiController apiController = new ApiController(hotelScrapper);

        apiController.start();
        apiController.join();
    }
}
