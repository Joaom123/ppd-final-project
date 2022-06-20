package ifce.ppd.finalproject.rmi;

import ifce.ppd.finalproject.model.Message;
import ifce.ppd.finalproject.mom.MOMConnection;

import javax.jms.JMSException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends UnicastRemoteObject implements RMIInterface {
    private static final long serialVersionUID = 1L;

    private final MOMConnection momConnection;

    public RMIServer() throws RemoteException, JMSException {
        super();

        momConnection = new MOMConnection();
        System.out.println("Connection to MOM ready");
    }

    public static void main(String[] args) throws RemoteException, JMSException {
        System.setProperty("java.security.policy","/home/joaomarcus/Projetos/final-project/src/main/java/ifce/ppd/finalproject/spaces/all.policy");
        System.setSecurityManager(new SecurityManager());

        RMIServer obj = new RMIServer();

        // Bind the remote object's stub in the registry
        Registry registry = LocateRegistry.createRegistry(2002);
        registry.rebind("RMIInterface", obj);

        System.out.println("Server ready");
    }

    @Override
    public void sendMessage(Message message) throws RemoteException {
        System.out.println(message.content);

        // send message to MOM
        try {
            momConnection.sendMessage(message);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}