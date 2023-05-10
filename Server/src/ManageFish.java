
public class ManageFish {

    private final int ARRAY_SIZE = 5; // Variable for array size (in case it needs to be changed later)
    private Integer fish[] = new Integer[ARRAY_SIZE];
    private int counterOfElem = 0;

    public ManageFish() {
        // Fill the array with the null value
        for (int i = 0; i < ARRAY_SIZE; i++) {
            fish[i] = null;
        }
    }

    /**
     * Synchronized method for inserting dishes into the array (Chef)
     * 
     * @return the message DONE when all actions are complete
     * @throws InterruptedException if connection error occurs
     */
    public synchronized String putDish() throws InterruptedException {
        // Iteration control structure for going through the array elements
        for (int i = 0; i < ARRAY_SIZE; i++) {
            // If the value of the array element is empty, we put a random menu value (1-10) in the array
            if (fish[i] == null) {
                System.out.println("Chef 1 is making fish");
                fish[i] = (int) (Math.random() * 9) + 1;
                counterOfElem++; // Increment the amount of elements
                // If the final element of the array isn't empty, then wait
                if (fish[ARRAY_SIZE - 1] != null) {
                    wait();
                }
                break; // Exit the iteration control structure
            }
        }
        notifyAll(); // Notify all threads
        
        return "DONE";
    }

    /**
     * Synchronized method for removing dishes from the array (Customer)
     * 
     * @param dishesToGet the number of plates the customer wants
     * @return the message OKAY when all actions are complete
     * @throws InterruptedException if connection error occurs
     */
    public synchronized String getDish(int dishesToGet) throws InterruptedException {
        // If the dishes wanted are more than those available
        if (dishesToGet > counterOfElem) {
            int x = (dishesToGet - counterOfElem) - 1;
            // Remove all the available dishes for now, then wait until there are more ready
            for (int i = x; i < counterOfElem; i++) {
                fish[i] = null;
                counterOfElem--;
            }
            wait();
        }

        // Print the amount of dishes taken
        System.out.println("Customer took " + dishesToGet + " dishes");
        notifyAll(); // Notify all threads
        
        return "OKAY";
    }
}
