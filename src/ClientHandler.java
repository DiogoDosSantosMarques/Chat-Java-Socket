import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList <ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;

   
    private BufferedReader bufferedReader;

    
    private BufferedWriter bufferedWriter;


    private String clientUsername;


    // construtor
    public ClientHandler(Socket socket){

        try {

            // armazena o socket passado no parametro
            this.socket = socket;

            // envia mensagem do servidor para o cliente
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // recebe mensagem do cliente
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);

            broadcastMessage( clientUsername + " entrou no chat");
        } catch (Exception e) {

            closeTudo(socket, bufferedReader, bufferedWriter);
            
        }

    }


    
    //  método run() será executado quando o thread associado for iniciado.
    @Override
    public void run(){

        String messageFromClient;

        while (socket.isConnected()) {

            try {

                messageFromClient = bufferedReader.readLine();

                broadcastMessage(messageFromClient);
                
            } catch (Exception e) {

                closeTudo(socket, bufferedReader, bufferedWriter);
                break;
              
            }
            
        }

    }



    public void broadcastMessage(String messageToSend){

        for(ClientHandler clientHandler : clientHandlers){

            try {

                if(!clientHandler.clientUsername.equals(clientUsername)){

                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();

                }
                
            } catch (Exception e) {

                closeTudo(socket, bufferedReader, bufferedWriter);
               
            }

        }

    }


    public void removeClientHandler(){
        clientHandlers.remove(this);

        broadcastMessage(clientUsername + "Saiu do chat");
    }



    public void closeTudo(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){


        removeClientHandler();

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
    
}
