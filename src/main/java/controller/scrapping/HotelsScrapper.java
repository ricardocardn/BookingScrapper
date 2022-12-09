package controller.scrapping;

import controller.databasecontroller.DataBaseConnection;
import controller.databasecontroller.DataBaseDDL;
import model.Address;
import model.Hotel;
import model.Hotel.Service;
import model.Review;
import model.ShortReview;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HotelsScrapper {
    private DataBaseDDL dataBaseDDL;
    private static int id = 0;

    public HotelsScrapper() {
        dataBaseDDL = new DataBaseDDL(new DataBaseConnection(
                "C:\\Users\\carde\\Desktop\\ULPGC\\TrivagoScrapper\\src\\main\\java\\booking.db"
        ));

        dataBaseDDL.createHotelsTable();
        dataBaseDDL.createReviewsTable();
    }

    public List<Hotel> getHotels(String url) throws Exception {
        Connection conn = Jsoup.connect(url).userAgent("Chrome/5.0").timeout(100000);
        Document doc = conn.get();
        Elements elements = doc.select("div.a826ba81c4.fe821aea6c.fa2f36ad22.afd256fc79.d08f526e0d.ed11e24d01.ef9845d4b3.da89aeb942");

        List<Hotel> hotels = new ArrayList<>();

        for (Element element : elements) {
            Hotel hotel = getHotel(element);
            hotel.setId(id++);
            hotel.setAddress(new Address());
            dataBaseDDL.insertIntoHotels(hotel);
            hotels.add(hotel);

            System.out.println(hotel.toString());
        }
        return hotels;
    }

    private Hotel getHotel(Element element) throws IOException {
        Hotel hotel = new Hotel();
        String name = element.select("div.fcab3ed991.a23c043802").text();
        int stars = element.select("span.b6dc9a9e69.adc357e4f1.fe621d6382").size();
        float rating = Float.parseFloat(element.select("div.b5cd09854e.d10a6220b4").text().replace(",", "."));

        hotel.setName(name);
        hotel.setStars(stars);
        hotel.setRating(rating);

        getHotelDeeperInfo(element, hotel);

        return hotel;
    }

    private void getHotelDeeperInfo(Element element, Hotel hotel) throws IOException {
        // Accessing hotel main page
        String aLink = element.select("a.e13098a59f").attr("href");
        System.out.println(aLink);
        Connection hotelConn = Jsoup.connect(aLink).userAgent("Mozilla/5.0").timeout(100000);
        Document hotelDoc = hotelConn.get();

        /*File file = new File("C:\\Users\\carde\\Desktop\\ULPGC\\TrivagoScrapper\\src\\main\\java\\index.html");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(hotelDoc.html().getBytes(StandardCharsets.UTF_8));

        fileOutputStream.close();*/

        getGrades(hotel, hotelDoc);
        getServices(hotel, hotelDoc);
        getReviews(hotel, hotelDoc);

        hotel.setTotalReviews(hotel.getReviews().size());

    }

    private void getReviews(Hotel hotel, Document hotelDoc) {
        Elements docReviews = hotelDoc.select("div.a826ba81c4.fe821aea6c.fa2f36ad22.afd256fc79.d08f526e0d.ed11e24d01.ef9845d4b3.bb0d9ee3d6");
        List<ShortReview> reviews = new ArrayList<>();
        for (Element docReview : docReviews) {
            ShortReview shortReview = new ShortReview();
            shortReview.setCountry(docReview.select("span.a5499473ab.f01b03907b").text());
            shortReview.setPositive(docReview.select("div.db29ecfbe2.c688f151a2").text());
            reviews.add(shortReview);
        }

        hotel.setReviews(reviews);
    }

    private void getServices(Hotel hotel, Document hotelDoc) {
        Elements services = hotelDoc.select("div.hotel-facilities-group");
        for (Element service : services) {
            Elements subServices = service.select("div.bui-list__description");
            List<String> sv = new ArrayList<>();
            for (Element sub : subServices) {
                sv.add(sub.text());
            }

            hotel.insertService(service.select(
                    "div.bui-title__text.hotel-facilities-group__title-text").text(),
                    sv
            );
        }
    }

    private void getGrades(Hotel hotel, Document hotelDoc) {
        Elements grades = hotelDoc.select("div.a1b3f50dcd.b2fe1a41c3.a1f3ecff04.e187349485.d19ba76520");
        for (Element grade : grades) {
            hotel.insertGrade(
                    grade.select("span.d6d4671780").text(),
                    Float.parseFloat(grade.select("div.ee746850b6.b8eef6afe1").text().replace(",","."))
            );
        }
    }
}
