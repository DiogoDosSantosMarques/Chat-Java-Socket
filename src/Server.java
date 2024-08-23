import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    // é uma classe que permite que você crie um servidor que aceita conexões de clientes em uma rede. 
    private ServerSocket serverSocket;


    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }


    public void startServer(){

        try {

            // enquanto server tiver rodando espero um cliente pra conectar
            while(!serverSocket.isClosed()){

                Socket socket = serverSocket.accept(); // Esse método faz com que o servidor "escute" por uma conexão de um cliente.

                System.out.println(" Um novo cliente se conectou");


                // ClientHandler clientHandler = new ClientHandler(socket); 
                // cria um novo ClientHandler para gerenciar a comunicação com um cliente específico usando o socket fornecido.
                ClientHandler clientHandler = new ClientHandler(socket);


                Thread thread = new Thread(clientHandler);

                //Quando você chama thread.start(), o Java inicia um novo thread e chama o método run() 
                // da classe ClientHandler, que é onde o código para lidar com o cliente é executado.
                thread.start();

            }
            
        } catch (Exception e) {

            
           
        }

    }


    public void closeServerSocket(){

        try {

            if(serverSocket != null){
                serverSocket.close();
            }
            
        } catch (Exception e) {
           e.printStackTrace();
        }

    }


    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(1234);

        Server server = new Server(serverSocket);
        server.startServer();
       
    }
    
}
