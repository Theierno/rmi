import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import RMI.ChatUser;

public class Client {

    public void subscribe(ChatUser user, String pseudo) throws Exception {

        this.addMessage(pseudo, "est dans la réunion");
    }

    public void unsubscribe(String pseudo) throws Exception {
        this.addMessage(pseudo, "a quitté la reunion");

    }

    public void addMessage(String pseudo, String text) throws Exception {
        try {
            // Localisation du registre RMI sur le serveur
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);

            // Récupération de l'objet distant du registre en utilisant le nom
            RemoteInterface stub = (RemoteInterface) registry.lookup("RemoteInterface");
            stub.addMessage(pseudo, text);

        } catch (Exception e) {
            System.err.println("Erreur côté client : " + e.toString());
            e.printStackTrace();
        }
    }

    public ArrayList<Message> getMessages() {
        ArrayList<Message> messages = new ArrayList<Message>();
        try {
            // Localisation du registre RMI sur le serveur
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);

            // Récupération de l'objet distant du registre en utilisant le nom
            RemoteInterface stub = (RemoteInterface) registry.lookup("RemoteInterface");
            messages = stub.getMessages();
            return messages;

        } catch (Exception e) {
            System.err.println("Erreur côté client : " + e.toString());
            e.printStackTrace();
            return messages;
        }
    }

}
