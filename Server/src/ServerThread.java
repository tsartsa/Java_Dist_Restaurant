
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerThread implements Runnable {

    private Socket servThreadSock;
    private ObjectOutputStream outstream;
    private ObjectInputStream instream;
    private static ManageFish fish = new ManageFish();
    private static ManageSeafood seaf = new ManageSeafood();
    private static ManageMeat meat = new ManageMeat();
    private Object msg1;
    private Object msg2;
    private String answer;

    ServerThread(Socket servThreadSock) {
        this.servThreadSock = servThreadSock;
    }

    @Override
    public void run() {
        try {
            // Create input-output streams (from-to the ChefThread/CustomerThread classes) for the data from the socket
            outstream = new ObjectOutputStream(servThreadSock.getOutputStream());
            instream = new ObjectInputStream(servThreadSock.getInputStream());

            while (true) {

                // Read the 1st message from the client side
                msg1 = instream.readObject();

                // If msg1 is of type String
                if (msg1 instanceof String) {

                    String smsg = (String) msg1; // Cast msg1 to String smsg

                    // Check for which Chef it is or which Chef the Customer wants
                    if (smsg.equals("Chef 1")) { // Case for Chef 1
                        // Read the 2nd message
                        msg2 = instream.readObject();
                        int imsg = (int) msg2; // Cast msg2 to int imsg
                        if (imsg == 0) { // If the 2nd message is 0, then it is a Chef
                            answer = fish.putDish();
                        } else { // Else, it is a Customer and we insert the quantity they want in getDish
                            answer = fish.getDish(imsg);
                        }
                    } else if (smsg.equals("Chef 2")) { // Case for Chef 2
                        msg2 = instream.readObject();
                        int imsg = (int) msg2;
                        if (imsg == 0) {
                            answer = seaf.putDish();
                        } else {
                            answer = seaf.getDish(imsg);
                        }
                    } else if (smsg.equals("Chef 3")) { // Case for Chef 3
                        msg2 = instream.readObject();
                        int imsg = (int) msg2;
                        if (imsg == 0) {
                            answer = meat.putDish();
                        } else {
                            answer = meat.getDish(imsg);
                        }
                    } else if (smsg.equals("END")) { // In case the message is END
                        System.out.println("Client connection closed");
                        // Release resources
                        outstream.close();
                        instream.close();
                        servThreadSock.close();
                        break; // Exit the while loop
                    }

                }

                // Send response to the client side
                outstream.writeObject(answer);

            }
        } catch (IOException ex) {
            try {
                System.out.println("Client connection error");
                // In case an error occurs, release resources
                outstream.close();
                instream.close();
                servThreadSock.close();
            } catch (IOException ex1) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (ClassNotFoundException | InterruptedException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
