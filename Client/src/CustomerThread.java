
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerThread implements Runnable {

    private int chefNum;
    private int dishAmount;
    private Socket custSock;
    private ObjectOutputStream outstream;
    private ObjectInputStream instream;
    private Object msg;

    public CustomerThread(int chefNum, int dishAmount) {

        this.chefNum = chefNum;
        this.dishAmount = dishAmount;

        // Start connection to the Server (handshake)
        try {
            // Create new socket, defining the address (localhost) and port it will be running on
            custSock = new Socket("localhost", 6666);

            // Create input-output streams (from-to the Server class) for the data from the socket
            outstream = new ObjectOutputStream(custSock.getOutputStream());
            instream = new ObjectInputStream(custSock.getInputStream());

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
                    custSock.close();
                }
            }

        } catch (IOException ex) {
            try {
                // In case an error occurs, release resources
                System.out.println("Server connection error");
                outstream.close();
                instream.close();
                custSock.close();
            } catch (IOException ex1) {
                Logger.getLogger(CustomerThread.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CustomerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        // Save the thread's name (Customer)
        String threadName = Thread.currentThread().getName();

        try {

            // Create input-output streams (from-to the ServerThread class) for the data from the socket
            ObjectOutputStream outstream = new ObjectOutputStream(custSock.getOutputStream());
            ObjectInputStream instream = new ObjectInputStream(custSock.getInputStream());

            // Repeat orders from the customer
            for (int i = 0; i < 10; i++) {

                // Print the threadName along with the wanted quantity from the selected Chef
                System.out.println("I am " + threadName + " and I want " + dishAmount + " dishes from Chef " + chefNum);

                // Check which Chef the customer has selected
                if (chefNum == 1) { // Case for 1st Chef
                    outstream.writeObject("Chef 1"); // Send Chef's name
                    outstream.writeObject(dishAmount); // Send quantity of dishes
                } else if (chefNum == 2) { // Case for 2nd Chef
                    outstream.writeObject("Chef 2");
                    outstream.writeObject(dishAmount);
                } else if (chefNum == 3) { // Case for 3rd Chef
                    outstream.writeObject("Chef 3");
                    outstream.writeObject(dishAmount);
                } else {
                    System.out.println("There are only 3 Chefs");
                }

                // Read response from server
                msg = instream.readObject();
                String answer = (String) msg;

                // If the response is OKAY, then the thread is put to sleep state
                // for a random period of time, between 1 and 5 seconds
                if (answer.equals("OKAY")) {
                    int time = (int) (Math.random() * 4000) + 1000;
                    Thread.sleep(time);
                    System.out.println(threadName + " done");
                }

            }

            // Send END message to the Server
            outstream.writeObject("END");
            System.out.println("CustomerThread connection end");
            // Release resources
            outstream.close();
            instream.close();
            custSock.close();

        } catch (IOException ex) {
            System.out.println("ServerThread connection error");
            try {
                // In case of an error, release resources
                outstream.close();
                instream.close();
                custSock.close();
            } catch (IOException ex1) {
                Logger.getLogger(CustomerThread.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (ClassNotFoundException | InterruptedException ex) {
            Logger.getLogger(CustomerThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
