package view;

import com.google.gson.*;
import controller.databasecontroller.*;
import controller.scrapping.HotelScrapper;
import model.Hotel;
import controller.web.HTMLMaker;

import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class ApiController {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static List<Hotel> hotels;
    private static StandardDDL dataBaseDDL;
    private static StandardQuery query;
    private static HotelScrapper bookingScrapper;
    private static HTMLMaker htmlMaker;

    public ApiController(HotelScrapper hotelScrapper) {
        DataBaseConnector dataBaseConnection = new DataBaseConnector("HotelsDataBase.db");
        dataBaseConnection.connect();
        dataBaseDDL = new DataBaseDDL(dataBaseConnection);
        query = new DataBaseQuery(dataBaseConnection);
        bookingScrapper = hotelScrapper;
        htmlMaker = new HTMLMaker();
    }

    public void run() {
        port(8080);

        initialGetReq();
        getJsonReq("comments");
        getJsonReq("ratings");
        getJsonReq("services");
        getJsonReq("grades");

        getHtmlReq("comments");
        getHtmlReq("ratings");
        getHtmlReq("services");
        getHtmlReq("grades");

        getHotelByIdJsonReq();
        getHotelByIdReq();

        getHotelsJsonReq();
        getHotelsReq();

        notAPageErrorJsonHandler();
        notAPageErrorHandler();
    }

    private void notAPageErrorHandler() {
        get("//*", (req, res) -> {
            FileInputStream fileInputStream = new FileInputStream("web/error404notapage.html");
            return fileInputStream.readAllBytes();
        });
    }

    private void notAPageErrorJsonHandler() {
        get("/v1/json/*", (req, res) -> {
            FileInputStream fileInputStream = new FileInputStream("web/error404notapage.html");
            return fileInputStream.readAllBytes();
        });
    }

    private void getJsonReq(String name) {
        get("/v1/json/hotels/:name/" + name, (req, res) -> {
            Hotel hotelQuery = query.getHotelIfExists(req.params(":name"));
            if (hotelQuery != null) return gson.toJson((hotelQuery).get(name));

            String json = null;
            Hotel hotel = bookingScrapper.getHotel(req.params(":name"),
                    (req.queryParams("pages") != null) ? Integer.parseInt(req.queryParams("pages")) : 1);
            dataBaseDDL.insertIntoTable("hotels", hotel);
            return gson.toJson(hotel.get(name));
        });
    }

    private void getHtmlReq(String name) {
        get("/v1/hotels/:name/" + name, (req, res) -> {
            Hotel hotelQuery = query.getHotelIfExists(req.params(":name"));
            if (hotelQuery != null) return htmlMaker.feedHTML(
                    gson.toJson((hotelQuery).get(name)),
                    "web/index.html");

            String json = null;
            Hotel hotel = bookingScrapper.getHotel(req.params(":name"),
                    (req.queryParams("pages") != null) ? Integer.parseInt(req.queryParams("pages")) : 1);
            dataBaseDDL.insertIntoTable("hotels", hotel);
            return htmlMaker.feedHTML(gson.toJson(hotel.get(name)), "web/index.html");
        });
    }

    private void initialGetReq() {
        get("/v1", (req, res) -> {
            FileInputStream fileInputStream = new FileInputStream("web/home.html");
            return fileInputStream.readAllBytes();
        });
    }

    private void getHotelByIdReq() {
        get("/v1/hotels/:id", (req, res) -> {
            Hotel hotelQuery = query.getHotelIfExists(req.params(":id"));
            if (hotelQuery != null) return htmlMaker.feedHTML(
                    gson.toJson(fromHotelCreateJsonObject(hotelQuery)),
                    "web/index.html");

            Hotel hotel;
            try {
                hotel = bookingScrapper.getHotel(req.params(":id"),
                        (req.queryParams("pages") != null) ? Integer.parseInt(req.queryParams("pages")) : 1);
            } catch (Exception e) {
                FileInputStream fileInputStream = new FileInputStream("web/error404.html");
                return fileInputStream.readAllBytes();
            }

            dataBaseDDL.insertIntoTable("hotels", hotel);
            return htmlMaker.feedHTML(gson.toJson(fromHotelCreateJsonObject(hotel)), "web/index.html");
        });
    }

    private void getHotelByIdJsonReq() {
        get("/v1/json/hotels/:id", (req, res) -> {
            Hotel hotelQuery = query.getHotelIfExists(req.params(":id"));
            if (hotelQuery != null) return gson.toJson(fromHotelCreateJsonObject(hotelQuery));

            Hotel hotel;
            try {
                hotel = bookingScrapper.getHotel(req.params(":id"),
                        (req.queryParams("pages") != null) ? Integer.parseInt(req.queryParams("pages")) : 1);
            } catch (Exception e) {
                return "{\"error\":404}";
            }

            dataBaseDDL.insertIntoTable("hotels", hotel);
            return gson.toJson(fromHotelCreateJsonObject(hotel));
        });
    }

    private void getHotelsReq() {
        get("/v1/hotels", (req, res) -> {
            List<JsonObject> hotels = query.getObjectList("Hotels").stream()
                    .map(o -> (Hotel) o)
                    .map(h -> {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("id", h.getId());
                        jsonObject.addProperty("name", h.getName());
                        jsonObject.addProperty("score", h.getRating());
                        jsonObject.addProperty("location", h.getAddress());

                        return jsonObject;
                    }).collect(Collectors.toList());

            return htmlMaker.feedHTML(gson.toJson(hotels), "web/index.html");
        });
    }

    private void getHotelsJsonReq() {
        get("/v1/json/hotels", (req, res) -> {
            List<JsonObject> hotels = query.getObjectList("Hotels").stream()
                    .map(o -> (Hotel) o)
                    .map(h -> {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("id", h.getId());
                        jsonObject.addProperty("name", h.getName());
                        jsonObject.addProperty("score", h.getRating());
                        jsonObject.addProperty("location", h.getAddress());

                        return jsonObject;
                    }).collect(Collectors.toList());

            return gson.toJson(hotels);
        });
    }

    private JsonObject fromHotelCreateJsonObject(Hotel hotel) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", hotel.getId());
        jsonObject.addProperty("name", hotel.getName());
        jsonObject.addProperty("score", hotel.getRating());
        jsonObject.addProperty("location", hotel.getAddress());

        return jsonObject;
    }
}
