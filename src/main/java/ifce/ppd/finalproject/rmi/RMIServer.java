package ifce.ppd.finalproject.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends UnicastRemoteObject implements RMIInterface {
    private static final long serialVersionUID = 1L;

//    private Publisher publisher;

    public RMIServer() throws RemoteException {
        super();

        System.out.println("----Starting Server----");

//        this.publisher = new Publisher();
    }

    @Override
    public void sendMessage(String content) throws RemoteException {
        StringBuilder sb = new StringBuilder();

        sb.append("\nO usuario enviou uma mensagem que contem uma das palavras monitoradas");
        sb.append("\nA mensagem foi: " + content );

//        publisher.sendMessage(sb.toString());
    }
}