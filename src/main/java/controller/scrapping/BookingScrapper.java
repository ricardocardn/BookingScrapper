package controller.scrapping;

import model.Hotel;
import model.HotelAPI;
import model.Review;
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

public class BookingScrapper implements HotelScrapper {

    public BookingScrapper() {}

    public Hotel getHotel(String name, int pages) throws Exception {
        Hotel hotel = new HotelAPI();

        Connection hotelConn = Jsoup.connect("https://www.booking.com/hotel/es/" + name + ".es.html").userAgent("Mozilla/5.0").timeout(100000);
        Document hotelDoc = hotelConn.get();

        getGeneralInfo(name, hotel, hotelDoc);
        getGrades(hotel, hotelDoc);
        getServices(hotel, hotelDoc);
        getReviews(hotel, hotelDoc, hotelConn, name, pages);
        hotel.setTotalReviews(hotel.getReviews().size());

        return hotel;
    }

    private void getGeneralInfo(String name, Hotel hotel, Document hotelDoc) {
        hotel.setId(name);
        hotel.setName(hotelDoc.select("h2.d2fee87262.pp-header__title").text());
        hotel.setStars(hotelDoc.select("span.b6dc9a9e69.adc357e4f1.fe621d6382").size());
        hotel.setRating(Float.parseFloat(hotelDoc.select("div.b5cd09854e.d10a6220b4").get(0).text().replace(",", ".")));
        hotel.setType(hotelDoc.select("span.e2f34d59b1").get(0).text());
        hotel.setAddress(hotelDoc.select("span.hp_address_subtitle.js-hp_address_subtitle.jq_tooltip").text());
    }

    private void getReviews(Hotel hotel, Document hotelDoc, Connection hotelConn, String name, int pages) throws IOException {
        String urlReviews = "https://www.booking.com/reviews/es/hotel/" + name + ".es.html?page=";

        List<Review> reviews = new ArrayList<>();
        System.out.println("ok");
        for (int i = 1; i < pages + 1; i++) {
            Connection hotelReviewsConn = Jsoup.connect(urlReviews + i + "&r_lang=es&rows=75&").userAgent("Mozilla/5.0").timeout(100000);
            System.out.println(urlReviews + i + "&r_lang=es&rows=75&");
            Document hotelReviewsDoc = hotelReviewsConn.get();
            Elements docReviews = hotelReviewsDoc.select("div.review_item_review_container.lang_ltr");
            if (docReviews.size() == 0) break;
            for (Element docReview : docReviews) {
                Review review = getReview(name, docReview);
                reviews.add(review);
            }
        }

        hotel.setReviews(reviews);
    }

    private Review getReview(String name, Element docReview) {
        Review review = new Review();
        //review.setCountry(docReview.select("span.reviewer_country_flag.sflag.slang-es..").text());
        review.setScore(Integer.parseInt(docReview.select("span.review-score-badge").text().replace(",", "")));
        review.setHotelId(name);
        review.setPositive(docReview.select("p.review_pos").text());
        review.setNegative(docReview.select("p.review_neg").text());
        return review;
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
