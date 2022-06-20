package ifce.ppd.finalproject.controller;

import ifce.ppd.finalproject.model.Message;
import ifce.ppd.finalproject.model.User;
import ifce.ppd.finalproject.spaces.server.Lookup;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

public class ChatController implements Initializable {
    @FXML
    public TextArea chat;

    @FXML
    public TextField messageInput;

    private JavaSpace javaSpaces = null;

    private final User user;

    private final Set<Message> messageSet;

    public ChatController(User user) {
        this.user = user;
        messageSet = new HashSet<>();
        System.out.println("Procurando serviço JavaSpace...");
        Lookup finder = new Lookup(JavaSpace.class);
        javaSpaces = (JavaSpace) finder.getService();

        if (javaSpaces == null) {
            System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
            System.exit(-1);
        }
    }

    @FXML
    public void sendMessage(ActionEvent actionEvent) throws TransactionException, RemoteException {
        String inputText = messageInput.getText();

        // If user typed nothing, exit function
        if (inputText.equals("")) return;

        messageInput.setText("");

        addMessageToChat(user.name, inputText);

        // Send inputText to javaSpaces
        Message message = new Message(UUID.randomUUID(), user, inputText);

        javaSpaces.write(message, null, 1000);
    }

    private void addMessageToChat(String author, String message) {
        chat.appendText(author + ": " + message + "\n");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Timer timer = new Timer();

        timer.schedule( new TimerTask()
        {
            public void run() {
                Platform.runLater(() -> {
                    try {
                        Message message = (Message) javaSpaces.read(new Message(), null, 200);
                        System.out.println(message);
                        if (message == null || messageSet.contains(message)) return;
                        messageSet.add(message);
                        if (!message.author.equals(user))
                            addMessageToChat(message.author.name, message.content);
                    } catch (UnusableEntryException | TransactionException | InterruptedException | RemoteException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 800);
    }
}
