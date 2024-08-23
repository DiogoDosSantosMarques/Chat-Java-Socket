import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client (Socket socket, String username){

        try {

            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;    
        } catch (Exception e) {

            closeTudo(socket, bufferedReader, bufferedWriter);
           
        }

    }

    public void sendMessage(){

        try {

            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);

            while (socket.isConnected()){

                String messageToSend = scanner.nextLine();
                bufferedWriter.write(username + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();

            }
            
        } catch (Exception e) {

            closeTudo(socket, bufferedReader, bufferedWriter);
            
        }
    }


    public void listenForMessage(){

        new Thread(new Runnable() {

            @Override
            public void run(){

                String msgFromGroupChat;

                while (socket.isConnected()) {

                    try {

                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                        
                    } catch (Exception e) {

                        closeTudo(socket, bufferedReader, bufferedWriter);
                       
                    }
                    
                }

            }
            
        }).start();

    }


    public void closeTudo(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){

        try {

            if(bufferedReader != null){
                bufferedReader.close();
            }

            if(bufferedWriter != null){
                bufferedWriter.close();
            }

            if(socket != null){
                socket.close();
            }
            
        } catch (Exception e) {

            e.printStackTrace();
            
        }


    }


    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Coloque o seu nome pra entrar no grupo do chat");

        String username = scanner.nextLine();

        Socket socket = new Socket("localhost", 1234);

        Client client = new Client(socket, username);
        
        client.listenForMessage();
        client.sendMessage();
       
    }
 
}
