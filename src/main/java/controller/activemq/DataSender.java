package controller.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

import static org.apache.activemq.ActiveMQConnection.DEFAULT_BROKER_URL;

public class DataSender {

    //URL of the JMS server. DEFAULT_BROKER_URL will just mean that JMS server is on localhost
    private String url = DEFAULT_BROKER_URL;

    // default broker URL is : tcp://localhost:61616"
    private String subject = "hotelsQueue"; // Queue Name.You can create any/many queue names as per your requirement.

    private ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
    private Connection connection;
    private Queue destination;
    private Session session;

    public DataSender(String messageText) {
        try {
            connection = connectionFactory.createConnection();
            connection.start();

            //Creating a non transactional session to send/receive JMS message.
            session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);

            //Destination represents here our queue 'JCG_QUEUE' on the JMS server.
            //The queue will be created automatically on the server.
            destination = session.createQueue(subject);

            MessageProducer producer = session.createProducer(destination);
            TextMessage message = session
                    .createTextMessage(messageText);

            producer.send(message);

            connection.close();
        } catch (JMSException e) {}
    }
}