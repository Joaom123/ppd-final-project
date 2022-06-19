package ifce.ppd.finalproject.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The client can execute a function on the server-side.
 * Each function must send the client's info, used by the server to identify the player.
 */
public interface RMIInterface extends Remote {
    /**
     * Send message to rmi server.
     *
     * @param messageContent The message's content.
     * @throws RemoteException Exception
     */
    void sendMessage(String messageContent) throws RemoteException;
}