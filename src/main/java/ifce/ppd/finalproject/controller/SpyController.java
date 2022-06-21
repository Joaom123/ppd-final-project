package ifce.ppd.finalproject.controller;

import ifce.ppd.finalproject.rmi.RMIInterface;
import ifce.ppd.finalproject.model.Message;
import ifce.ppd.finalproject.spaces.server.Lookup;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class SpyController implements Initializable {
    @FXML
    public TextArea chat;

    @FXML
    public ListView<String> wordList = new ListView();

    @FXML
    public TextField newDangerousWordInput;

    @FXML
    public Text removeDangerousWordErrorText;

    @FXML
    public Text addDangerousWordErrorText;

    private JavaSpace javaSpaces = null;

    private final Set<Message> messageSet;

    private final Set<String> dangerousWords;

    private Registry registry;

    private RMIInterface rmiInterfaceStub = null;

    public SpyController() throws RemoteException, NotBoundException {
        messageSet = new HashSet<>();
        dangerousWords = new HashSet<>();
        System.out.println("Procurando serviço JavaSpace...");

        // Init Java Spaces connection
        Lookup finder = new Lookup(JavaSpace.class);
        javaSpaces = (JavaSpace) finder.getService();

        if (javaSpaces == null) {
            System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
            System.exit(-1);
        }

        // Init RMI Connection
        registry = LocateRegistry.getRegistry(null, 2002);
        rmiInterfaceStub = (RMIInterface) registry.lookup("RMIInterface");

        if (rmiInterfaceStub == null) {
            System.out.println("O serviço RMI não foi encontrado. Encerrando...");
            System.exit(-1);
        }

        System.out.println(registry);
        System.out.println(rmiInterfaceStub);
    }

    private boolean messageIsDangerous(Message message) {
        String[] words = message.content.split("\\W+");

        for (String word : words) {
            if (dangerousWords.contains(word))
                return true;
        }
        return false;
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

                        if (message == null || messageSet.contains(message)) return;
                        if (messageIsDangerous(message)) {
                            messageSet.add(message);
                            addMessageToChat(message.author.name, message.content);

                            // Send message ro RMI
                            registry = LocateRegistry.getRegistry(null, 2002);
                            rmiInterfaceStub = (RMIInterface) registry.lookup("RMIInterface");;
                            System.out.println(rmiInterfaceStub);
                            rmiInterfaceStub.sendMessage(message);
                        }
                    } catch (UnusableEntryException | TransactionException | InterruptedException | RemoteException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    } catch (NotBoundException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 800);
    }

    @FXML
    public void addDangerousWord(ActionEvent actionEvent) {
        String newWord = newDangerousWordInput.getText();

        if(newWord == null || newWord.trim().isEmpty()) {
            addDangerousWordErrorText.setText("Palavra Perigosa não pode ser vazia!");
            return;
        }

        if(dangerousWords.contains(newWord)) {
            addDangerousWordErrorText.setText("Palavra Perigosa já existe!");
            return;
        }

        addDangerousWordErrorText.setText("");

        // Add dangerous word to list
        dangerousWords.add(newWord);
        wordList.getItems().add(newWord);
    }

    @FXML
    public void removeDangerousWord(ActionEvent actionEvent) {
        String selectedWord = wordList.getSelectionModel().getSelectedItem();

        System.out.println(selectedWord);

        if(selectedWord == null) {
            removeDangerousWordErrorText.setText("Não há palavra selecionada!");
            return;
        }

        removeDangerousWordErrorText.setText("");
        dangerousWords.remove(selectedWord);
        wordList.getItems().remove(selectedWord);
    }

    private void addMessageToChat(String author, String message) {
        chat.appendText(author + ": " + message + "\n");
    }
}
