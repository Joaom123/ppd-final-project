package ifce.ppd.finalproject.controller;

import ifce.ppd.finalproject.rmi.RMIInterface;
import ifce.ppd.finalproject.model.Message;
import ifce.ppd.finalproject.spaces.server.Lookup;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
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
    public ListView wordList;

    private JavaSpace javaSpaces = null;

    private final Set<Message> messageSet;

    private Registry registry;

    private RMIInterface rmiInterfaceStub = null;

    private void addMessageToChat(String author, String message) {
        chat.appendText(author + ": " + message + "\n");
    }

    public SpyController() throws RemoteException, NotBoundException {
        messageSet = new HashSet<>();
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
        return true;
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
}
