package api;

import com.google.gson.Gson;
import controller.databasecontroller.*;
import controller.scrapping.BookingScrapper;
import controller.scrapping.HotelScrapper;
import model.Hotel;
import model.Request;
import model.Review;
import spark.Spark;
import web.HTMLMaker;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class Main extends Thread {
    private static Gson gson = new Gson();
    private static List<Hotel> hotels;
    private static DataBaseConnection dataBaseConnection;
    private static DDLdb dataBaseDDL;
    private static Query query;
    private static HotelScrapper bookingScrapper;
    private static HTMLMaker htmlMaker;

    public static boolean isJson(spark.Request req) {
        return (req.queryParams("accept") != null && req.queryParams("accept").equals("json"))
                || req.headers("Accept").equals("application/json");
    }

    public static void initialize() {
        dataBaseConnection = new DataBaseConnection("src/main/HotelsDataBase.db");
        dataBaseConnection.connect();
        dataBaseDDL = new DataBaseDDL(dataBaseConnection);
        dataBaseDDL.createTables();
        query = new DataBaseQuery(dataBaseConnection);

        bookingScrapper = new BookingScrapper();
        htmlMaker = new HTMLMaker();
    }

    public static void main(String[] args) throws Exception {
        initialize();

        port(8086);

        initialGetReq();
        getHotelsReq();
        getHotelByIdReq();
        getHotelReviewsReq();
        notAPageErrorHandler();
    }

    private static void notAPageErrorHandler() {
        get("//*", (req, res) -> {
            FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\carde\\Desktop\\ULPGC\\BookingScrapper\\src\\main\\java\\web\\error404notapage.html"));
            return fileInputStream.readAllBytes();
        });
    }

    private static void getHotelReviewsReq() {
        get("/v1/hotels/:name/reviews", (req, res) -> {
            for (Object hotel: query.getObjectList("Hotels")) {
                System.out.println(((Hotel)hotel).getId());
                if (((Hotel) hotel).getId().equalsIgnoreCase(req.params(":name"))) {
                    if (isJson(req)) return new Gson().toJson(((Hotel) hotel).getReviews());
                    return htmlMaker.feedHTML(new Gson().toJson(((Hotel) hotel).getReviews()), "src/main/java/web/index.html");
                }
            }

            String json = null;
            Hotel hotel = bookingScrapper.getHotel(req.params(":name"));
            dataBaseDDL.insertIntoTable("hotels", hotel);
            if (isJson(req)) return new Gson().toJson(hotel);
            return htmlMaker.feedHTML(new Gson().toJson(hotel.getReviews()), "src/main/java/web/index.html");
        });
    }

    private static void initialGetReq() {
        get("/v1", (req, res) -> {
            System.out.println(req.headers("Accept"));
            if (isJson(req)) return "{}";
            FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\carde\\Desktop\\ULPGC\\BookingScrapper\\src\\main\\java\\web\\index.html"));
            return fileInputStream.readAllBytes();
        });
    }

    private static void getHotelByIdReq() {
        get("/v1/hotels/:id", (req, res) -> {
            String json = null;

            for (Object hotel: query.getObjectList("Hotels")) {
                if (((Hotel) hotel).getId().equalsIgnoreCase(req.params(":id"))) {
                    if (isJson(req)) return new Gson().toJson(hotel);
                    return htmlMaker.feedHTML(new Gson().toJson(hotel), "src/main/java/web/index.html");
                }
            }

            Hotel hotel;
            try {
                hotel = bookingScrapper.getHotel(req.params(":id"));
            } catch (Exception e) {
                if (isJson(req)) return new Gson().toJson(new Hotel());
                FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\carde\\Desktop\\ULPGC\\BookingScrapper\\src\\main\\java\\web\\error404.html"));
                return fileInputStream.readAllBytes();
            }

            dataBaseDDL.insertIntoTable("hotels", hotel);
            if (isJson(req)) return new Gson().toJson(hotel);
            return htmlMaker.feedHTML(new Gson().toJson(hotel), "src/main/java/web/index.html");
        });
    }

    private static void getHotelsReq() {
        get("/v1/hotels", (req, res) -> {
            List<Hotel> hotels = query.getObjectList("Hotels").stream()
                    .map(o -> (Hotel) o)
                    .collect(Collectors.toList());

            if (isJson(req)) return new Gson().toJson(hotels);
            return htmlMaker.feedHTML(new Gson().toJson(hotels), "src/main/java/web/index.html");
        });
    }
}
