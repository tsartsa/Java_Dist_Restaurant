
public class Client {

    public static void main(String[] args) {

        // Create 3 Chef threads, name and start them
        ChefThread chef1 = new ChefThread();
        Thread t1 = new Thread(chef1);
        t1.setName("Chef 1");
        t1.start();

        ChefThread chef2 = new ChefThread();
        Thread t2 = new Thread(chef2);
        t2.setName("Chef 2");
        t2.start();

        ChefThread chef3 = new ChefThread();
        Thread t3 = new Thread(chef3);
        t3.setName("Chef 3");
        t3.start();

        // Customer 1 wants 2 portions from Chef 1 (Fish)
        CustomerThread cust1 = new CustomerThread(1, 2);
        Thread c1 = new Thread(cust1);
        c1.setName("Customer 1");
        c1.start();

        // Customer 2 wants 3 portions from Chef 2 (Seafood)
        CustomerThread cust2 = new CustomerThread(2, 3);
        Thread c2 = new Thread(cust2);
        c2.setName("Customer 2");
        c2.start();

        // Customer 3 wants 5 portions from Chef 3 (Meat)
        CustomerThread cust3 = new CustomerThread(3, 5);
        Thread c3 = new Thread(cust3);
        c3.setName("Customer 3");
        c3.start();

    }

}
