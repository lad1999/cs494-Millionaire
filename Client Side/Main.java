import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

class JTextFieldLimit extends PlainDocument {
    private int limit;

    JTextFieldLimit(int limit){
        super();
        this.limit = limit;
    }

    JTextFieldLimit(int limit, boolean upper){
        super();
        this.limit = limit;
    }


    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null)
            return;

        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }


}

public class Main extends JFrame implements ActionListener{

    Socket socket;
    JTextField port;
    JTextField username;
    JPanel panel;
    JButton btn_Connect;
    List<String> usernames = new ArrayList<>();
    String question;
    String ansA;
    String ansB;
    String ansC;
    String ansD;

    JLabel quest;
    JButton a;
    JButton b;
    JButton c;
    JButton d;
    JButton ready;

    int count = 0;

    JLabel labelP;
    JLabel labelU;
    JLabel message;
    final Image background = ImageIO.read(this.getClass().getResource("/image/background.jpg"));

    public Main() throws IOException {
    }


    public void init(){
        panel = new JPanel();
        panel.setBounds(135, 100, 200, 100);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        username = new JTextField(15);
        username.setDocument(new JTextFieldLimit(10));
        username.setBorder(new LineBorder(Color.black));
        username.setLayout(null);


        labelP = new JLabel();
        labelP.setText("Enter port:");
        labelP.setVisible(true);
        panel.add(labelP);

        labelU = new JLabel();
        labelU.setText("Enter username:");
        labelP.setVisible(true);

        port = new JTextField(15);
        port.setDocument(new JTextFieldLimit(4));
        port.setBorder(new LineBorder(Color.black));
        port.setLayout(null);

        btn_Connect = new JButton();
        btn_Connect.setText("Connect");
        btn_Connect.setFont(new Font("Arial",Font.BOLD, 20));
        btn_Connect.setBounds(340, 100, 150, 50);
        btn_Connect.setFocusable(false);
        btn_Connect.setVisible(true);
        btn_Connect.setLayout(null);
        btn_Connect.addActionListener(this);

        message = new JLabel();
        message.setVisible(true);
        message.setBounds(500, 100, 500, 100);
        message.setBackground(Color.white);
        message.setOpaque(true);
        message.setFont(new Font("Arial",Font.PLAIN, 20));
        message.setForeground(Color.red);
        message.setLayout(null);

        panel.add(port);
        panel.add(labelU);
        panel.add(username);

        quest = new JLabel("", JLabel.CENTER);
        quest.setVisible(true);
        quest.setBounds(420, 490, 800, 100);
        quest.setLayout(null);
        quest.setFont(new Font("Trajan",Font.BOLD, 20));
        quest.setBackground(new Color(0, 0, 205));
        quest.setOpaque(true);
        quest.setForeground(Color.white);

        a = new JButton();
        a.setVisible(true);
        a.setBounds(420, 610, 300, 60);
        a.setLayout(null);
        a.setFont(new Font("Trajan",Font.BOLD, 20));
        a.setBackground(Color.white);
        a.setFocusable(false);
        a.addActionListener(this);

        b = new JButton();
        b.setVisible(true);
        b.setBounds(920, 610, 300, 60);
        b.setLayout(null);
        b.setFont(new Font("Trajan",Font.BOLD, 20));
        b.setBackground(Color.white);
        b.setFocusable(false);
        b.addActionListener(this);


        c = new JButton();
        c.setVisible(true);
        c.setBounds(420, 690, 300, 60);
        c.setLayout(null);
        c.setFont(new Font("Trajan",Font.BOLD, 20));
        c.setBackground(Color.white);
        c.setFocusable(false);
        c.addActionListener(this);

        d = new JButton();
        d.setVisible(true);
        d.setBounds(920, 690, 300, 60);
        d.setLayout(null);
        d.setFont(new Font("Trajan",Font.BOLD, 20));
        d.setBackground(Color.white);
        d.setFocusable(false);
        d.addActionListener(this);


        ready = new JButton();
        ready.setVisible(true);
        ready.setLayout(null);
        ready.setText("Ready");
        ready.setBounds(340, 150, 150, 50);
        ready.setFocusable(false);
        ready.setFont(new Font("Arial",Font.BOLD, 20));
        ready.addActionListener(this);

        this.setTitle("Client");
        this.setContentPane(new JPanel(new BorderLayout()){
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(background, 0, 0, null);
            }
        });
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1600, 900);
        this.setVisible(true);
        this.add(panel);
        this.add(btn_Connect);
        this.add(ready);
        this.add(message);
        this.add(quest);
        this.add(a);
        this.add(b);
        this.add(c);
        this.add(d);
        this.setLayout(null);

    }

    public static void alert(String message, String title){
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }

    public void begin(){
        int getPort = Integer.parseInt(port.getText()) ;
        String getUSN = username.getText();
        usernames.add(getUSN);
        try {
            socket = new Socket("localhost", getPort);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            dataOutputStream.writeUTF(getUSN);
            dataOutputStream.flush();

            String response = dataInputStream.readUTF();
            message.setText(response);


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_Connect){
            begin();
        }
        if (e.getSource() == ready){
            if (socket.isConnected()){
                try {
                    OutputStream outputStream = socket.getOutputStream();
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                    dataOutputStream.writeUTF("ready");
                    dataOutputStream.flush();

                    while (true){
                        count ++;
                        InputStream inputStream = socket.getInputStream();
                        DataInputStream dataInputStream = new DataInputStream(inputStream);
                        String message = dataInputStream.readUTF();
                        if (count == 1){
                            quest.setText("<html><div style='text-align: center;'>" + message + "<div></html>");
                        }
                        if (count == 2){
                            a.setText(message);
                        }
                        if (count == 3){
                            b.setText(message);
                        }
                        if(count ==4){
                            c.setText(message);
                        }
                        if(count == 5){
                            d.setText(message);
                            count = 0;
                            break;
                        }
                    }


                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        if (e.getSource() == a){
            a.setBackground(new Color(255, 140, 0));
            OutputStream outputStream = null;
            try {
                outputStream = socket.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                dataOutputStream.writeUTF(a.getText());
                dataOutputStream.flush();

                Timer t = new Timer(3000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        while (true) {
                            InputStream inputStream = null;
                            try {
                                inputStream = socket.getInputStream();
                                DataInputStream dataInputStream = new DataInputStream(inputStream);
                                String message = dataInputStream.readUTF();
                                if (message.equals("You lose!")) {
                                    a.setBackground(Color.red);
                                    alert("YOU LOSE!", "WARNING");
                                    a.setFocusable(false);
                                    b.setFocusable(false);
                                    c.setFocusable(false);
                                    d.setFocusable(false);
                                    socket.close();
                                    dispose();
                                    System.exit(0);
                                } else {
                                    a.setBackground(Color.green);
                                    count++;
                                    Timer t1 = new Timer(2500, new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            a.setBackground(Color.white);
                                            quest.setText("<html>"+question+"</html>");
                                            a.setText(ansA);
                                            b.setText(ansB);
                                            c.setText(ansC);
                                            d.setText(ansD);
                                            ((Timer)e.getSource()).stop();
                                        }
                                    });
                                    t1.setRepeats(false);
                                    t1.start();
                                    if (count == 1) {
                                        question = message;
                                    }
                                    if (count == 2) {
                                        ansA = message;
                                    }
                                    if (count == 3) {
                                        ansB = message;
                                    }
                                    if (count == 4) {
                                        ansC = message;
                                    }
                                    if (count == 5) {
                                        ansD = message;
                                        count = 0;
                                        break;
                                    }
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
                t.setRepeats(false);
                t.start();


            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else if (e.getSource() == b){
            try {
                b.setBackground(Color.orange);
                OutputStream outputStream = socket.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                dataOutputStream.writeUTF(b.getText());
                dataOutputStream.flush();
                Timer t = new Timer(3000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        while (true) {
                            InputStream inputStream = null;
                            try {
                                inputStream = socket.getInputStream();
                                DataInputStream dataInputStream = new DataInputStream(inputStream);
                                String message = dataInputStream.readUTF();
                                if (message.equals("You lose!")) {
                                    b.setBackground(Color.red);
                                    alert("YOU LOSE!", "WARNING");
                                    a.setFocusable(false);
                                    b.setFocusable(false);
                                    c.setFocusable(false);
                                    d.setFocusable(false);
                                    socket.close();
                                    dispose();
                                    System.exit(0);
                                } else {
                                    b.setBackground(Color.green);
                                    count++;
                                    Timer t1 = new Timer(2500, new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            b.setBackground(Color.white);
                                            quest.setText("<html>"+question+"</html>");
                                            a.setText(ansA);
                                            b.setText(ansB);
                                            c.setText(ansC);
                                            d.setText(ansD);
                                            ((Timer)e.getSource()).stop();
                                        }
                                    });
                                    t1.setRepeats(false);
                                    t1.start();
                                    if (count == 1) {
                                        question = message;
                                    }
                                    if (count == 2) {
                                        ansA = message;
                                    }
                                    if (count == 3) {
                                        ansB = message;
                                    }
                                    if (count == 4) {
                                        ansC = message;
                                    }
                                    if (count == 5) {
                                        ansD = message;
                                        count = 0;
                                        break;
                                    }
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
                t.setRepeats(false);
                t.start();
            }catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else if (e.getSource() == c){
            c.setBackground(new Color(255, 140, 0));
            try {
                OutputStream outputStream = socket.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                dataOutputStream.writeUTF(c.getText());
                dataOutputStream.flush();

                Timer t = new Timer(3000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        while (true) {
                            InputStream inputStream = null;
                            try {
                                inputStream = socket.getInputStream();
                                DataInputStream dataInputStream = new DataInputStream(inputStream);
                                String message = dataInputStream.readUTF();
                                if (message.equals("You lose!")) {
                                    c.setBackground(Color.red);
                                    alert("YOU LOSE!", "WARNING");
                                    a.setFocusable(false);
                                    b.setFocusable(false);
                                    c.setFocusable(false);
                                    d.setFocusable(false);
                                    socket.close();
                                    dispose();
                                    System.exit(0);
                                } else {
                                    c.setBackground(Color.green);
                                    count++;
                                    Timer t1 = new Timer(2500, new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            c.setBackground(Color.white);
                                            quest.setText("<html>"+question+"</html>");
                                            a.setText(ansA);
                                            b.setText(ansB);
                                            c.setText(ansC);
                                            d.setText(ansD);
                                            ((Timer)e.getSource()).stop();
                                        }
                                    });
                                    t1.setRepeats(false);
                                    t1.start();
                                    if (count == 1) {
                                        question = message;
                                    }
                                    if (count == 2) {
                                        ansA = message;
                                    }
                                    if (count == 3) {
                                        ansB = message;
                                    }
                                    if (count == 4) {
                                        ansC = message;
                                    }
                                    if (count == 5) {
                                        ansD = message;
                                        count = 0;
                                        break;
                                    }
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
                t.setRepeats(false);
                t.start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else if (e.getSource() == d){
            d.setBackground(new Color(255, 140, 0));
            OutputStream outputStream = null;
            try {
                outputStream = socket.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                dataOutputStream.writeUTF(d.getText());
                dataOutputStream.flush();

                Timer t = new Timer(3000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        while (true) {
                            InputStream inputStream = null;
                            try {
                                inputStream = socket.getInputStream();
                                DataInputStream dataInputStream = new DataInputStream(inputStream);
                                String message = dataInputStream.readUTF();
                                if (message.equals("You lose!")) {
                                    d.setBackground(Color.red);
                                    alert("YOU LOSE!", "WARNING");
                                    a.setFocusable(false);
                                    b.setFocusable(false);
                                    c.setFocusable(false);
                                    d.setFocusable(false);
                                    socket.close();
                                    dispose();
                                    System.exit(0);
                                } else {
                                    d.setBackground(Color.green);
                                    count++;
                                    Timer t1 = new Timer(2500, new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            d.setBackground(Color.white);
                                            quest.setText("<html>"+question+"</html>");
                                            a.setText(ansA);
                                            b.setText(ansB);
                                            c.setText(ansC);
                                            d.setText(ansD);
                                            ((Timer)e.getSource()).stop();
                                        }
                                    });
                                    t1.setRepeats(false);
                                    t1.start();
                                    if (count == 1) {
                                        question = message;
                                    }
                                    if (count == 2) {
                                        ansA = message;
                                    }
                                    if (count == 3) {
                                        ansB = message;
                                    }
                                    if (count == 4) {
                                        ansC = message;
                                    }
                                    if (count == 5) {
                                        ansD = message;
                                        count = 0;
                                        break;
                                    }
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
                t.setRepeats(false);
                t.start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Main init = null;
                try {
                    init = new Main();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                init.init();

            }

        });
    }


}
