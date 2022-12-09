package controller.activemq;

import com.google.gson.Gson;
import controller.databasecontroller.DataBaseConnection;
import controller.databasecontroller.DataBaseQuery;
import model.Hotel;
import model.Request;
import model.Review;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.activemq.ActiveMQConnection.DEFAULT_BROKER_URL;

public class RequestReceiver extends Thread {

    // URL of the JMS server
    private static String url = DEFAULT_BROKER_URL;
    // default broker URL is : tcp://localhost:61616"

    // Name of the queue we will receive messages from
    private static String subject = "bookingQueue";

    public static void main(String[] args) {
        try {
            // Getting JMS connection from the server
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            Connection connection = connectionFactory.createConnection();
            connection.setClientID("id_admin_10");
            connection.start();

            // Creating session for seding messages
            Session session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);

            // Getting the queue 'JCG_QUEUE'
            //Destination destination = session.createQueue(subject);
            Queue destination = session.createQueue(subject);

            // MessageConsumer is used for receiving (consuming) messages
            //MessageConsumer consumer = session.createConsumer(destination);
            MessageConsumer consumer = session.createConsumer(destination);

            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        Request request = new Gson().fromJson(((TextMessage)message).getText(), Request.class);
                        DataBaseQuery dbQueries = new DataBaseQuery(new DataBaseConnection(
                                "C:\\Users\\carde\\Desktop\\ULPGC\\BookingScrapper\\src\\main\\java\\booking.db"
                        ));

                        if (request.getRequestInfo().equalsIgnoreCase("hotels")) {
                            System.out.println("ok");
                            List<Hotel> hotels = getHotels(request, dbQueries);
                            DataSender hotelSender = new DataSender(new Gson().toJson(hotels));
                        } else if (request.getRequestInfo().equalsIgnoreCase("hotelId")) {
                            Hotel hotel = getHotelById(request, dbQueries);
                            DataSender hotelSender = new DataSender(new Gson().toJson(hotel));
                        } else if (request.getRequestInfo().equalsIgnoreCase("reviews")) {
                            List<Review> reviews = getReviews(request, dbQueries);
                            DataSender hotelSender = new DataSender(new Gson().toJson(reviews));
                        }

                    } catch (Exception e) {
                        throw new RuntimeException();}
                }
            });

            //connection.close();
        } catch (Exception e) {}
    }

    private static List<Review> getReviews(Request request, DataBaseQuery dbQueries) throws SQLException {
        List<Review> reviews = dbQueries.getObjectList("Reviews").stream()
                .map(review -> (Review) review)
                .filter(review -> review.getHotelId() == request.getId())
                .filter(review -> request.getQueryParams().keySet().stream()
                        .filter(key -> review.get(key).equals(request.getQueryParams().get(key)))
                        .count() == request.getQueryParams().size())
                .collect(Collectors.toList());
        return reviews;
    }

    private static Hotel getHotelById(Request request, DataBaseQuery dbQueries) throws SQLException {
        List<Hotel> hotels = dbQueries.getObjectList("Hotels").stream()
                .map(hotel -> (Hotel) hotel)
                .filter(hotel -> hotel.getId() == request.getId())
                .collect(Collectors.toList());
        return hotels.get(0);
    }

    private static List<Hotel> getHotels(Request request, DataBaseQuery dbQueries) throws SQLException {
        List<Hotel> hotels = dbQueries.getObjectList("Hotels").stream()
                .map(hotel -> (Hotel) hotel)
                .filter(hotel -> request.getQueryParams().keySet().stream()
                        .filter(key -> hotel.get(key).equals(request.getQueryParams().get(key)))
                        .count() == request.getQueryParams().size())
                .collect(Collectors.toList());
        return hotels;
    }
}