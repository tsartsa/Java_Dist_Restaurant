
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static ObjectOutputStream outstream;
    private static ObjectInputStream instream;
    private static ServerSocket servSock;

    public static void main(String[] args) {
        try {
            // Create new ServerSocket, defining the port it will be running on and the amount of incoming connections it can accept (queue length)
            servSock = new ServerSocket(6666, 50);
            System.out.println("Server started");

            while (true) {
                // The program does not get executed until there is a request from the client side
                Socket sock = servSock.accept();

                // Create input-output streams (from-to the client side) for the data from the socket
                outstream = new ObjectOutputStream(sock.getOutputStream());
                instream = new ObjectInputStream(sock.getInputStream());

                // Read message from the client side
                // If it's of type String and it's BEGIN, send response LISTENING
                Object msg = instream.readObject();
                if (msg instanceof String) {
                    String smsg = (String) msg;
                    if (smsg.equals("BEGIN")) {
                        outstream.writeObject("LISTENING");
                        System.out.println("Client Begun");
                    } else {
                        // In case of wrong message, release resources
                        System.out.println("Client connection error");
                        instream.close();
                        outstream.close();
                        servSock.close();
                    }
                }

                // Create a thread for each request and start it
                ServerThread servThread = new ServerThread(sock);
                Thread st1 = new Thread(servThread);
                st1.start();
            }

        } catch (IOException ex) {
            try {
                System.out.println("Client connection error");
                // In case an error occurs, release resources
                outstream.close();
                instream.close();
                servSock.close();
            } catch (IOException ex1) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
