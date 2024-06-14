import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

// Interface distante
interface RemoteInterface extends Remote {

    void addMessage(String pseudo, String text) throws RemoteException;

    ArrayList<Message> getMessages() throws RemoteException;
}

// Implémentation de l'interface distante
class RemoteImpl implements RemoteInterface {
    ArrayList<Message> messages = new ArrayList<>();

    public void addMessage(String pseud, String text) {
        Message message = new Message(pseud, text);
        messages.add(message);

        System.out.println(pseud);
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

}

public class Server1 {
    public static void main(String[] args) {
        try {
            // Création d'une instance du serveur
            RemoteImpl remoteObj = new RemoteImpl();

            // Exportation de l'objet pour rendre les méthodes accessibles à distance
            RemoteInterface stub = (RemoteInterface) UnicastRemoteObject.exportObject(remoteObj, 0);

            // Création d'un registre RMI
            Registry registry = LocateRegistry.createRegistry(1099);

            // Enregistrement de l'objet distant dans le registre sous le nom
            // "RemoteInterface"
            registry.rebind("RemoteInterface", stub);

            System.out.println("Serveur RMI prêt !");
        } catch (Exception e) {
            System.err.println("Erreur côté serveur : " + e.toString());
            e.printStackTrace();
        }
    }
}
