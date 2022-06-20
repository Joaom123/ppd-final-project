package ifce.ppd.finalproject.mom;

import ifce.ppd.finalproject.model.Message;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MOMConnection {
    private final Connection connection;
    private final Session session;
    private final Destination destination;
    private final MessageProducer producer;

    public MOMConnection() throws JMSException {
        // Init connection
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
        connection = connectionFactory.createConnection();
        connection.start();

        // Create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic)
        destination = session.createTopic("spy");

        // Create a MessageProducer from the Session to the Topic
        producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Encerrando conexão!");
            try {
                session.close();
                connection.close();
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public void sendMessage(Message message) throws JMSException {
        ObjectMessage objectMessage = session.createObjectMessage(message);
        producer.send(objectMessage);
    }

    public Session getSession() {
        return session;
    }

    public Destination getDestination() {
        return destination;
    }
}
