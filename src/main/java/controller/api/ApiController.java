package controller.api;

import com.google.gson.Gson;
import controller.databasecontroller.*;
import controller.scrapping.BookingScrapper;
import controller.scrapping.HotelScrapper;
import model.Hotel;
import web.HTMLMaker;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class ApiController extends Thread {
    private static final Gson gson = new Gson();
    private static List<Hotel> hotels;
    private static DataBaseConnection dataBaseConnection;
    private static DDLdb dataBaseDDL;
    private static Query query;
    private static HotelScrapper bookingScrapper;
    private static HTMLMaker htmlMaker;

    public ApiController(HotelScrapper hotelScrapper) {
        dataBaseConnection = new DataBaseConnection("HotelsDataBase.db");
        dataBaseConnection.connect();
        dataBaseDDL = new DataBaseDDL(dataBaseConnection);
        dataBaseDDL.createTables();
        query = new DataBaseQuery(dataBaseConnection);
        bookingScrapper = hotelScrapper;
        htmlMaker = new HTMLMaker();
    }

    public void run() {
        port(8088);

        initialGetReq();
        getHotelsReq();
        getHotelsJsonReq();
        getHotelByIdReq();
        getHotelByIdJsonReq();
        getHotelReviewsReq();
        getHotelReviewsJsonReq();
        notAPageErrorJsonHandler();
        notAPageErrorHandler();
    }

    private static void notAPageErrorHandler() {
        get("//*", (req, res) -> {
            FileInputStream fileInputStream = new FileInputStream("web/error404notapage.html");
            return fileInputStream.readAllBytes();
        });
    }

    private static void notAPageErrorJsonHandler() {
        get("/v1/json/*", (req, res) -> {
            FileInputStream fileInputStream = new FileInputStream("web/error404notapage.html");
            return fileInputStream.readAllBytes();
        });
    }

    private static void getHotelReviewsReq() {
        get("/v1/hotels/:name/reviews", (req, res) -> {
            for (Object hotel: query.getObjectList("Hotels")) {
                System.out.println(((Hotel)hotel).getId());
                if (((Hotel) hotel).getId().equalsIgnoreCase(req.params(":name"))) {
                    return htmlMaker.feedHTML(new Gson().toJson(((Hotel) hotel).getReviews()), "web/index.html");
                }
            }

            String json = null;
            Hotel hotel = bookingScrapper.getHotel(req.params(":name"));
            dataBaseDDL.insertIntoTable("hotels", hotel);
            return htmlMaker.feedHTML(new Gson().toJson(hotel.getReviews()), "web/index.html");
        });
    }

    private static void getHotelReviewsJsonReq() {
        get("/v1/json/hotels/:name/reviews", (req, res) -> {
            for (Object hotel: query.getObjectList("Hotels")) {
                System.out.println(((Hotel)hotel).getId());
                if (((Hotel) hotel).getId().equalsIgnoreCase(req.params(":name"))) {
                    return new Gson().toJson(((Hotel) hotel).getReviews());
                }
            }

            String json = null;
            Hotel hotel = bookingScrapper.getHotel(req.params(":name"));
            dataBaseDDL.insertIntoTable("hotels", hotel);
            return new Gson().toJson(hotel);
        });
    }

    private static void initialGetReq() {
        get("/v1", (req, res) -> {
            System.out.println(req.headers("Accept"));
            FileInputStream fileInputStream = new FileInputStream("web/home.html");
            return fileInputStream.readAllBytes();
        });
    }

    private static void getHotelByIdReq() {
        get("/v1/hotels/:id", (req, res) -> {
            String json = null;

            for (Object hotel: query.getObjectList("Hotels")) {
                if (((Hotel) hotel).getId().equalsIgnoreCase(req.params(":id"))) {
                    return htmlMaker.feedHTML(new Gson().toJson(hotel), "web/index.html");
                }
            }

            Hotel hotel;
            try {
                hotel = bookingScrapper.getHotel(req.params(":id"));
            } catch (Exception e) {
                FileInputStream fileInputStream = new FileInputStream("web/error404.html");
                return fileInputStream.readAllBytes();
            }

            dataBaseDDL.insertIntoTable("hotels", hotel);
            return htmlMaker.feedHTML(new Gson().toJson(hotel), "web/index.html");
        });
    }

    private static void getHotelByIdJsonReq() {
        get("/v1/json/hotels/:id", (req, res) -> {
            String json = null;

            for (Object hotel: query.getObjectList("Hotels")) {
                if (((Hotel) hotel).getId().equalsIgnoreCase(req.params(":id"))) {
                    return new Gson().toJson(hotel);
                }
            }

            Hotel hotel;
            try {
                hotel = bookingScrapper.getHotel(req.params(":id"));
            } catch (Exception e) {
                return "{\"error\":404}";
            }

            dataBaseDDL.insertIntoTable("hotels", hotel);
            return new Gson().toJson(hotel);
        });
    }

    private static void getHotelsReq() {
        get("/v1/hotels", (req, res) -> {
            List<Hotel> hotels = query.getObjectList("Hotels").stream()
                    .map(o -> (Hotel) o)
                    .collect(Collectors.toList());

            return htmlMaker.feedHTML(new Gson().toJson(hotels), "web/index.html");
        });
    }

    private static void getHotelsJsonReq() {
        get("/v1/json/hotels", (req, res) -> {
            List<Hotel> hotels = query.getObjectList("Hotels").stream()
                    .map(o -> (Hotel) o)
                    .collect(Collectors.toList());

            return new Gson().toJson(hotels);
        });
    }
}
