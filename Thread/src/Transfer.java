/**
 * Created by Yang, Chi-Chang on 2016/5/1.
 */
public class Transfer extends Thread{

    static final Object lock = new Object();
    boolean sync;
    static int account1 = 1000;
    static int account2 = 0;
    int x;

    public Transfer(int x , boolean sync){
        this.x = x;
        this.sync = sync;
    }

    public void transfer() throws InterruptedException {
        Thread.sleep(10);
        account1 -= x;
        Thread.sleep(10);
        account2 += x;
    }

    public void run() {
        try {
            if (sync){
                synchronized (lock) {
                    transfer();
                }
            } else {
                transfer();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Not synchronized:");
        for (int i = 0; i < 4; i++) {
            account1 = 1000;
            account2 = 0;
            Transfer account1 = new Transfer(200 , false);
            Transfer account2 = new Transfer(300 , false);
            account1.start();
            account2.start();
            account1.join();
            account2.join();
            System.out.println("account1 = " + Transfer.account1 + " , account2 = " + Transfer.account2);
        }
        System.out.println("===================");
        System.out.println("Synchronized:");
        for (int i = 0; i < 4; i++) {
            account1 = 1000;
            account2 = 0;
            Transfer account1 = new Transfer(200 , true);
            Transfer account2 = new Transfer(300 , true);
            account1.start();
            account2.start();
            account1.join();
            account2.join();
            System.out.println("account1 = " + Transfer.account1 + " , account2 = " + Transfer.account2);
        }
    }

}
