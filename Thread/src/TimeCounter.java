/**
 * Created by Yang, Chi-Chang on 2016/4/30.
 */
public class TimeCounter extends Thread {

    String counterName;

    TimeCounter (String name) {
        counterName = name;
    }

    public void run(){
        for (int i = 5; i >= 0 ; i--) {
            System.out.println(i + " , from " + counterName + ".");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        TimeCounter t1 = new TimeCounter("Counter1");
        TimeCounter t2 = new TimeCounter("Counter2");
        t1.start();
        t2.start();

    }

}
