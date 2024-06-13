import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

public class ChatUserImpl {
    private String title = "Logiciel de discussion en ligne";
    private String pseudo = null;
    private int lastIndex = 0;

    private JFrame window = new JFrame(this.title);
    private JTextArea txtOutput = new JTextArea();
    private JTextField txtMessage = new JTextField();
    private JButton btnSend = new JButton("Envoyer");

    Client client = new Client();
    boolean firstTime = true;
    ArrayList<Message> messages = new ArrayList<Message>();

    public ChatUserImpl() {
        this.createIHM();
        this.requestPseudo();
    }

    public void createIHM() {
        Timer timer = new Timer();
        // Assemblage des composants
        JPanel panel = (JPanel) this.window.getContentPane();
        JScrollPane sclPane = new JScrollPane(txtOutput);
        messages = client.getMessages();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Rafraichissement des messages ....");
                messages = client.getMessages();
                if (lastIndex < messages.size()) {
                    messages = client.getMessages();
                    for (int i = lastIndex; i < messages.size(); i++) {
                        Message message = messages.get(i);
                        String objectMessage = message.getPseudo() + " : " + message.getTextMessage();
                        txtOutput.append(objectMessage + "\n");
                        txtMessage.setText("");
                        txtMessage.requestFocus();
                    }
                    lastIndex = messages.size();
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 2000);

        panel.add(sclPane, BorderLayout.CENTER);
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(this.txtMessage, BorderLayout.CENTER);
        southPanel.add(this.btnSend, BorderLayout.EAST);
        panel.add(southPanel, BorderLayout.SOUTH);

        // Gestion des évènements
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                window_windowClosing(e);
            }
        });
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    btnSend_actionPerformed(e);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        txtMessage.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent event) {
                if (event.getKeyChar() == '\n')
                    try {
                        btnSend_actionPerformed(null);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
            }
        });

        // Initialisation des attributs
        this.txtOutput.setBackground(new Color(220, 220, 220));
        this.txtOutput.setEditable(false);
        this.window.setSize(500, 400);
        this.window.setVisible(true);
        this.txtMessage.requestFocus();
    }

    public void requestPseudo() {
        this.pseudo = JOptionPane.showInputDialog(
                this.window, "Entrez votre pseudo : ",
                this.title, JOptionPane.OK_OPTION);
        if (this.pseudo == null)
            System.exit(0);
    }

    public void window_windowClosing(WindowEvent e) {
        System.exit(-1);
    }

    public void btnSend_actionPerformed(ActionEvent e) throws Exception {
        String text = this.txtMessage.getText();
        client.addMessage(pseudo, text);
        String objectMessage = this.pseudo + " : " + text;
        // this.txtOutput.append(objectMessage + "\n");
        // this.txtMessage.setText("");
        // this.txtMessage.requestFocus();
    }

    public static void main(String[] args) {
        new ChatUserImpl();
    }
}
