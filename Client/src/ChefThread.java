
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChefThread implements Runnable {

    private Socket chefSock;
    private ObjectOutputStream outstream;
    private ObjectInputStream instream;
    private Object msg;

    public ChefThread() {

        // Start connection to the Server (handshake)
        try {

            // Create new socket, defining the address (localhost) and port it will be running on
            chefSock = new Socket("localhost", 6666);

            // Create input-output streams (from-to the Server class) for the data from the socket
            outstream = new ObjectOutputStream(chefSock.getOutputStream());
            instream = new ObjectInputStream(chefSock.getInputStream());

            // Send BEGIN message to the Server to initiate handshake
            outstream.writeObject("BEGIN");

            // Read response message from the Server
            Object msg = instream.readObject();
            if (msg instanceof String) {
                String smsg = (String) msg;
                if (smsg.equals("LISTENING")) {
                    System.out.println("Server is Listening");
                } else {
                    // In case of a different (false) response, release resources
                    System.out.println("Server connection error");
                    outstream.close();
                    instream.close();
                    chefSock.close();
                }
            }

        } catch (IOException ex) {
            try {
                // In case an error occurs, release resources
                System.out.println("Server connection error");
                outstream.close();
                instream.close();
                chefSock.close();
            } catch (IOException ex1) {
                Logger.getLogger(ChefThread.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChefThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        try {

            // Create input-output streams (from-to the ServerThread class) for the data from the socket
            ObjectOutputStream outstream = new ObjectOutputStream(chefSock.getOutputStream());
            ObjectInputStream instream = new ObjectInputStream(chefSock.getInputStream());

            // The Chef prepares 20 portions (their shift's limit)
            for (int i = 0; i < 20; i++) {

                // Save the thread's name (Chef)
                String threadName = Thread.currentThread().getName();

                // Send the thread's name to the Server
                outstream.writeObject(threadName);
                outstream.writeObject(0); // The Chefs want 0 dishes, since they only make dishes

                // Read response from server
                msg = instream.readObject();
                String answer = (String) msg;

                // If the response is DONE, then the thread is put to sleep state
                // for a random period of time, between 1 and 5 seconds
                if (answer.equals("DONE")) {
                    int time = (int) (Math.random() * 4000) + 1000;
                    Thread.sleep(time);
                    System.out.println(threadName + " made dish");
                }

            }

            // Send END message to the Server
            outstream.writeObject("END");
            System.out.println("ChefThread connection end");
            // Release resources
            outstream.close();
            instream.close();
            chefSock.close();

        } catch (IOException ex) {
            System.out.println("ServerThread connection error");
            try {
                // In case of an error, release resources
                outstream.close();
                instream.close();
                chefSock.close();
            } catch (IOException ex1) {
                Logger.getLogger(ChefThread.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (ClassNotFoundException | InterruptedException ex) {
            Logger.getLogger(ChefThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
