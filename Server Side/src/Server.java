import javax.management.remote.JMXServerErrorException;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;


class ClientHandler extends Thread{
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    List<String> question = new ArrayList<>();
    List<String> choiceA = new ArrayList<>();
    List<String> choiceB = new ArrayList<>();
    List<String> choiceC = new ArrayList<>();
    List<String> choiceD = new ArrayList<>();
    List<String> correct = new ArrayList<>();
    int i = 0;

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos){
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    public void readQuestion() throws IOException {
        List<String> allLines = Files.readAllLines(Paths.get("question.txt"));

        int count = 0;

        for (String allLine : allLines) {
            if (count == 0){
                question.add(allLine);
            }
            else if (count == 1){
                choiceA.add(allLine);
            }
            else if (count == 2){
                choiceB.add(allLine);
            }
            else if (count == 3){
                choiceC.add(allLine);
            }
            else if (count == 4){
                choiceD.add(allLine);
            }
            else if (count == 5){
                correct.add(allLine);
            }
            count ++;
            if(count == 6){
                count =0;
            }
        }
    }

    @Override
    public void run() {
        String received;
        try {
            readQuestion();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true){
            try{
                received = dis.readUTF();
                System.out.println(received);
                if (received.equals("ready")){
                    dos.writeUTF(question.get(i));
                    dos.flush();
                    dos.writeUTF(choiceA.get(i));
                    dos.flush();
                    dos.writeUTF(choiceB.get(i));
                    dos.flush();
                    dos.writeUTF(choiceC.get(i));
                    dos.flush();
                    dos.writeUTF(choiceD.get(i));
                    dos.flush();
                }
                else if (received.equals(correct.get(i))){
                    i++;
                    dos.writeUTF(question.get(i));
                    dos.flush();
                    dos.writeUTF(choiceA.get(i));
                    dos.flush();
                    dos.writeUTF(choiceB.get(i));
                    dos.flush();
                    dos.writeUTF(choiceC.get(i));
                    dos.flush();
                    dos.writeUTF(choiceD.get(i));
                    dos.flush();
                }
                else{
                    if (received != null){
                        dos.writeUTF("You lose!");
                        dos.flush();
                        break;
                    }
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }


    }
}
public class Server extends JFrame implements ActionListener {
    static ServerSocket serverSocket;
    static Socket con;
    static List<String> usernames = new ArrayList<>();

    JButton button;
    JButton connect;
    private static JLabel label;
    JLabel text;
    JTextField num;
    JPanel panel;
    static int players;




    public void ServerEnd(){
        initialize();
    }


    private static void startServer() throws IOException {
        serverSocket = new ServerSocket(8080);
        for (int i = 0; i < players; i++){
            Thread t = null;
            con = serverSocket.accept();

            InputStream inputStream = con.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            OutputStream outputStream = con.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);


            String message = dataInputStream.readUTF();
            if (usernames.isEmpty()){
                usernames.add(message);
                dataOutputStream.writeUTF("Hello "+message + ". You are player 1/" + players + ". There will be 30 questions in this game.");
                dataOutputStream.flush();
                t = new ClientHandler(con, dataInputStream, dataOutputStream);
                t.start();
            }
            else{
                if (usernames.contains(message)){
                    i -= 1;
                    dataOutputStream.writeUTF("Username existed!");
                    dataOutputStream.flush();
                }
                else{
                    if (usernames.size() == i){
                        usernames.add(message);
                        dataOutputStream.writeUTF("Hello "+ message + ". You are player " +(i+1)+"/"+players + ". There will be 30 questions in this game.");
                        t = new ClientHandler(con, dataInputStream, dataOutputStream);
                        t.start();
                    }
                }
            }



            label.setVisible(true);
            label.setText("Server started. There are " + players + " players joined.");

        }


    }


    public void stopServer() throws IOException {
        serverSocket.close();
    }

    private void initialize(){
        ImageIcon image = new ImageIcon("altp.jpg");

        button = new JButton();
        button.setBounds(250, 120, 175, 50);
        button.setText("Start game");
        button.addActionListener(this);
        button.setFocusable(false);
        button.setForeground(Color.red);
        button.setFont(new Font("Arial", Font.BOLD, 22));

        connect = new JButton();
        connect.setBounds(75, 120, 175, 50);
        connect.setText("Start server");
        connect.addActionListener(this);
        connect.setFocusable(false);
        connect.setForeground(Color.blue);
        connect.setFont(new Font("Arial", Font.BOLD, 22));

        label = new JLabel();
        label.setVisible(false);
        label.setBounds(32, 180, 200, 20);
        label.setFont(new Font("Arial", Font.ITALIC, 18));
        label.setLayout(null);


        panel = new JPanel();
        panel.setBounds(32, 60, 200, 50);

        text = new JLabel();
        text.setVisible(true);
        text.setText("Number of players");
        text.setLayout(null);
        panel.add(text);

        num = new JTextField(10);
        num.setBorder(new LineBorder(Color.black));
        num.setLayout(null);

        panel.add(num);




        this.setTitle("Who is the millionaire?");
        this.setVisible(true);
        this.setSize(500, 400);
        this.add(panel);
        this.add(button);
        this.add(connect);
        this.add(label);
        this.setIconImage(image.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == connect){
            try {
                label.setVisible(true);
                players = Integer.parseInt(num.getText());
                startServer();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try{
                    Server server = new Server();
                    server.initialize();

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }
}
