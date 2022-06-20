package ifce.ppd.finalproject.controller;

import ifce.ppd.finalproject.mom.MOMConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import javax.jms.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MediatorController implements Initializable {
    @FXML public TextArea chat;
    Destination destination;
    MessageConsumer messageConsumer;
    private final MOMConnection momConnection = new MOMConnection();

    public MediatorController() throws JMSException {
        destination = momConnection.getDestination();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
        messageConsumer = momConnection.getSession().createConsumer(destination);
            messageConsumer.setMessageListener(new QueueMessageListener());
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    private void addMessageToChat(String author, String message) {
        chat.appendText(author + ": " + message + "\n");
    }

    private class QueueMessageListener implements MessageListener {
        @Override
        public void onMessage(Message message) {
            try {
                if (message instanceof ObjectMessage) {
                    ObjectMessage objectMessage = (ObjectMessage) message;
                    ifce.ppd.finalproject.model.Message messagem = (ifce.ppd.finalproject.model.Message) objectMessage.getObject();
                        Platform.runLater(() -> {
                            addMessageToChat(messagem.author.name, messagem.content);
                        });

                }
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
