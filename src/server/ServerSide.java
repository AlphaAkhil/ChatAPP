package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ServerSide{
    private static final int PORT = 88888; 
    private static final Set<ClientHandler> clientHandlers = ConcurrentHashMap.newKeySet();
    
    public static void main(String[] args) throws IOException{
        ServerSocket serverSocket = new ServerSocket(PORT);

        while(true){
            Socket clientSocket = serverSocket.accept();
            ClientHandler handler = new ClientHandler(clientSocket);
            clientHandlers.add(handler);
            new Thread(handler).start();
        }
    }

    static void Broadcast(String message, ClientHandler sender){
        for(ClientHandler client : clientHandlers){
            if(client != sender){
                client.SendMessage(message);
            }
        }
    }

    static void removeclient(ClientHandler client){
        clientHandlers.remove(client);
    }

}

class ClientHandler implements Runnable{

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName;
    

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    public void SendMessage(String msg){
        out.println(msg);
    }

    public void run(){
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);

            out.println("Enter your Name:");
            clientName = in.readLine();

            System.out.println(clientName + " connected!");

            String message;
            while((message = in.readLine()) != null){
                System.out.println(clientName +":" + message);
                ServerSide.Broadcast((clientName+":"+message),this);
            }
        }
        catch (IOException e){
            System.out.println(clientName +"disconnected.");
        }finally{
            try{
                socket.close();
            }catch(IOException ignored){}
            ServerSide.removeclient(this);
            ServerSide.Broadcast(clientName+" left the chat.",this);

        }
    }
}