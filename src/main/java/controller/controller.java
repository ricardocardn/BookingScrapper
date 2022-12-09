package controller;

import controller.activemq.RequestReceiver;
import controller.databasecontroller.DataBaseConnection;
import controller.databasecontroller.DataBaseDDL;
import controller.scrapping.HotelsScrapper;
import model.Hotel;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class controller {
    public static void main(String[] args) throws Exception {
        HotelsScrapper hotelsScrapper = new HotelsScrapper();
        List<Hotel> hotels = hotelsScrapper.getHotels("https://www.booking.com/searchresults.es.html?aid=1610682&label=las-palmas-de-gran-canaria-YWpiL2gLgIxjGTd1A6L2ggS410974253970%3Apl%3Ata%3Ap1%3Ap2%3Aac%3Aap%3Aneg%3Afi%3Atiaud-146342137510%3Akwd-360951961344%3Alp1005463%3Ali%3Adec%3Adm%3Appccp%3DUmFuZG9tSVYkc2RlIyh9YfpWGnRw6lOGdE15X_QAcTg&sid=c53359469d524f4d4e4e8762244afb1a&sb=1&sb_lp=1&src=city&src_elem=sb&error_url=https%3A%2F%2Fwww.booking.com%2Fcity%2Fes%2Flas-palmas-de-gran-canaria.es.html%3Faid%3D1610682%26label%3Dlas-palmas-de-gran-canaria-YWpiL2gLgIxjGTd1A6L2ggS410974253970%253Apl%253Ata%253Ap1%253Ap2%253Aac%253Aap%253Aneg%253Afi%253Atiaud-146342137510%253Akwd-360951961344%253Alp1005463%253Ali%253Adec%253Adm%253Appccp%253DUmFuZG9tSVYkc2RlIyh9YfpWGnRw6lOGdE15X_QAcTg%26sid%3Dc53359469d524f4d4e4e8762244afb1a%26inac%3D0%26%26&ss=Las+Palmas+de+Gran+Canaria&is_ski_area=0&ssne=Las+Palmas+de+Gran+Canaria&ssne_untouched=Las+Palmas+de+Gran+Canaria&city=-388528&checkin_year=2022&checkin_month=12&checkin_monthday=8&checkout_year=2022&checkout_month=12&checkout_monthday=9&group_adults=2&group_children=0&no_rooms=1&b_h4u_keep_filters=&from_sf=1");
    }
}
