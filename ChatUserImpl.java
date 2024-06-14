import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

public class ChatUserImpl {
    private String title = "Logiciel de discussion en ligne";
    private String pseudo = null;
    private int lastIndex = 0;

    private JLabel avatarLabel = new JLabel();
    private ImageIcon selectedAvatar;
    private JLabel usernameLabel = new JLabel();

    private static final String ASSETS_PATH = "C:/assets/";

    private JFrame window = new JFrame(this.title);
    private JTextArea txtOutput = new JTextArea();
    private JTextField txtMessage = new JTextField();
    private JButton btnSend = new JButton("Envoyer");

    Client client = new Client();
    boolean firstTime = true;
    ArrayList<Message> messages = new ArrayList<Message>();

    public ChatUserImpl() {
        this.requestPseudo();
        this.createIHM();
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

        JPanel avatarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        avatarPanel.setBackground(new Color(200, 220, 240));
        avatarPanel.add(avatarLabel);
        avatarPanel.add(usernameLabel);
        panel.add(avatarPanel, BorderLayout.NORTH);

        // panel.add(avatarPanel, BorderLayout.NORTH);

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

        selectedAvatar = chooseAvatar();
        avatarLabel = new JLabel(
                new ImageIcon(selectedAvatar.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
        usernameLabel = new JLabel(this.pseudo);

        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        if (this.pseudo == null)
            System.exit(0);
    }

    private ImageIcon chooseAvatar() {
        String[] avatars = { "male.png", "male1.png", "female.png", "female1.png" };
        JDialog avatarDialog = new JDialog(this.window, "Choisissez votre avatar", true);
        avatarDialog.setLayout(new FlowLayout());

        JButton[] avatarButtons = new JButton[avatars.length];
        ImageIcon[] avatarIcons = new ImageIcon[avatars.length];

        for (int i = 0; i < avatars.length; i++) {
            avatarIcons[i] = loadAvatarIcon(avatars[i], avatars[i].replace(".png", ""));
            avatarButtons[i] = new JButton(
                    new ImageIcon(avatarIcons[i].getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
            final int index = i;
            avatarButtons[i].addActionListener(e -> {
                avatarDialog.dispose();
                selectedAvatar = avatarIcons[index];
            });
            avatarDialog.add(avatarButtons[i]);
        }

        avatarDialog.pack();
        avatarDialog.setVisible(true);

        return selectedAvatar;
    }

    private ImageIcon loadAvatarIcon(String filename, String description) {
        File file = new File(ASSETS_PATH + filename);
        if (!file.exists()) {
            throw new RuntimeException("Ressource non trouvée : " + file.getAbsolutePath());
        }
        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
        icon.setDescription(description);
        return icon;
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
