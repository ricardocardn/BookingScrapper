package api;

import com.google.gson.Gson;
import controller.databasecontroller.DataBaseConnection;
import controller.databasecontroller.DataBaseDDL;
import controller.databasecontroller.DataBaseQuery;
import controller.databasecontroller.Query;
import controller.scrapping.BookingScrapper;
import controller.scrapping.HotelScrapper;
import model.Hotel;
import model.Review;
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
    public static List<Hotel> hotels;

    public static void main(String[] args) throws Exception {
        DataBaseConnection dataBaseConnection = new DataBaseConnection("src/main/HotelsDataBase.db");
        dataBaseConnection.connect();
        DataBaseDDL dataBaseDDL = new DataBaseDDL(dataBaseConnection);
        dataBaseDDL.createHotelsTable();
        dataBaseDDL.createReviewsTable();
        Query query = new DataBaseQuery(dataBaseConnection);

        HotelScrapper bookingScrapper = new BookingScrapper();
        //Initializer.getInstance(dataBaseConnection).initialize(bookingScrapper, 0);

        HTMLMaker htmlMaker = new HTMLMaker();

        port(8088);
        get("/v1/hotels", (req, res) -> {
            List<Hotel> hotels = query.getObjectList("Hotels").stream()
                            .map(o -> (Hotel) o)
                            .collect(Collectors.toList());

            return htmlMaker.feedHTML(new Gson().toJson(hotels), "src/main/java/web/json.html");
        });

        get("/v1/hotels/:name", (req, res) -> {
            String json = null;

            Hotel hotel = bookingScrapper.getHotel(req.params(":name"));
            if (hotel.getName() != null) {
                dataBaseDDL.insertIntoHotels(hotel);
                return htmlMaker.feedHTML(new Gson().toJson(hotel), "src/main/java/web/json.html");
            }

            FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\carde\\Desktop\\ULPGC\\BookingScrapper\\src\\main\\java\\web\\error404.html"));
            return fileInputStream.readAllBytes();
        });

        get("/v1/hotels/:name/reviews", (req, res) -> {
            String json = null;
            return htmlMaker.feedHTML(new Gson().toJson(bookingScrapper.getHotel(req.params(":name")).getReviews()), "src/main/java/web/review.html");
        });

    }
}
