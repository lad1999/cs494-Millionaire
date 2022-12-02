import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

public class Main extends JFrame implements ActionListener {

    Socket socket;
    JTextField port;
    JTextField username;
    JPanel panel;
    JButton btn_Connect;
    List<String> usernames = new ArrayList<>();

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

    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;

    public Main() throws IOException {
    }


    public void init(){
        panel = new JPanel();
        panel.setBounds(10, 0, 200, 100);
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
        btn_Connect.setBounds(220, 12, 150, 38);
        btn_Connect.setFocusable(false);
        btn_Connect.setVisible(true);
        btn_Connect.setLayout(null);
        btn_Connect.addActionListener(this);

        message = new JLabel();
        message.setVisible(true);
        message.setBounds(400, 12, 500, 20);
        message.setLayout(null);

        panel.add(port);
        panel.add(labelU);
        panel.add(username);

        quest = new JLabel();
        quest.setVisible(true);
        quest.setBounds(135, 200, 600, 100);
        quest.setLayout(null);
        quest.setFont(new Font("Arial",Font.BOLD, 20));
        quest.setBorder(new LineBorder(Color.black));

        a = new JButton();
        a.setVisible(true);
        a.setBounds(135, 320, 270, 60);
        a.setLayout(null);
        a.setBackground(Color.white);
        a.setFocusable(false);
        a.addActionListener(this);

        b = new JButton();
        b.setVisible(true);
        b.setBounds(465, 320, 270, 60);
        b.setLayout(null);
        b.setBackground(Color.white);
        b.setFocusable(false);
        b.addActionListener(this);

        c = new JButton();
        c.setVisible(true);
        c.setBounds(135, 390, 270, 60);
        c.setLayout(null);
        c.setBackground(Color.white);
        c.setFocusable(false);
        c.addActionListener(this);

        d = new JButton();
        d.setVisible(true);
        d.setBounds(465, 390, 270, 60);
        d.setLayout(null);
        d.setBackground(Color.white);
        d.setFocusable(false);
        d.addActionListener(this);

        ready = new JButton();
        ready.setVisible(true);
        ready.setLayout(null);
        ready.setText("Ready");
        ready.setBounds(220, 62, 150, 38);
        ready.setFocusable(false);
        ready.setFont(new Font("Arial",Font.BOLD, 20));
        ready.addActionListener(this);

        this.setTitle("Client");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900, 600);
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
                            quest.setText("<html>" + message + "</html>");
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
            a.setBackground(Color.orange);
            OutputStream outputStream = null;
            try {
                outputStream = socket.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                dataOutputStream.writeUTF(a.getText());
                dataOutputStream.flush();

                while (true){
                    InputStream inputStream = socket.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                    String message = dataInputStream.readUTF();
                    if (message.equals("You lose!")){
                        alert("YOU LOSE!", "WARNING");
                        a.setFocusable(false);
                        b.setFocusable(false);
                        c.setFocusable(false);
                        d.setFocusable(false);
                    }
                    else {
                        a.setBackground(Color.white);
                        count ++;
                        if (count == 1){
                            quest.setText("<html>" + message + "</html>");
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
                }


            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else if (e.getSource() == b){
            b.setBackground(Color.orange);
            try {
                OutputStream outputStream = socket.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                dataOutputStream.writeUTF(b.getText());
                dataOutputStream.flush();

                while (true){
                    InputStream inputStream = socket.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                    String message = dataInputStream.readUTF();
                    if (message.equals("You lose!")){
                        alert("YOU LOSE!", "WARNING");
                        a.setFocusable(false);
                        b.setFocusable(false);
                        c.setFocusable(false);
                        d.setFocusable(false);
                    }
                    else {
                        b.setBackground(Color.white);
                        count ++;
                        if (count == 1){
                            quest.setText("<html>" + message + "</html>");
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
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else if (e.getSource() == c){
            c.setBackground(Color.orange);
            try {
                OutputStream outputStream = socket.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                dataOutputStream.writeUTF(c.getText());
                dataOutputStream.flush();

                while (true){
                    InputStream inputStream = socket.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                    String message = dataInputStream.readUTF();
                    if (message.equals("You lose!")){
                        alert("YOU LOSE!", "WARNING");
                        a.setFocusable(false);
                        b.setFocusable(false);
                        c.setFocusable(false);
                        d.setFocusable(false);
                    }
                    else {
                        c.setBackground(Color.white);
                        count ++;
                        if (count == 1){
                            quest.setText("<html>" + message + "</html>");
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
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else if (e.getSource() == d){
            d.setBackground(Color.orange);
            OutputStream outputStream = null;
            try {
                outputStream = socket.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                dataOutputStream.writeUTF(d.getText());
                dataOutputStream.flush();

                while (true){
                    InputStream inputStream = socket.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                    String message = dataInputStream.readUTF();
                    if (message.equals("You lose!")){
                        alert("YOU LOSE!", "WARNING");
                        a.setFocusable(false);
                        b.setFocusable(false);
                        c.setFocusable(false);
                        d.setFocusable(false);
                    }
                    else {
                        d.setBackground(Color.white);
                        count ++;
                        if (count == 1){
                            quest.setText("<html>" + message + "</html>");
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
                }
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
