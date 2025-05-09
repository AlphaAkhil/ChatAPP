

import java.io.*;
import java.net.*;

public class ClientSide{

    private static final String SERVER_ID ="localhost";
    private static final int SERVER_PORT = 8888;

    public static void main(String[] args) throws IOException{
        Socket socket = new Socket(SERVER_ID,SERVER_PORT);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        // Thread to read messages from server
        new Thread(()->{
            try {
            String response;
            while((response = input.readLine())!= null){
                System.out.println(response);
                }
            }catch(IOException e){
                System.out.println("Disconnected from server.");
            }

        }).start();
        
        String userInput;
        while((userInput = console.readLine())!=null){
            output.println(userInput);
        }
    }
}
