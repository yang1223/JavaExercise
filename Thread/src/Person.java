/**
 * Created by Yang, Chi-Chang on 2016/5/1.
 */
public class Person {

    private String name;
    private String gender;
    private boolean sync = false;

    public void setSync(boolean sync){
        this.sync = sync;
    }

    public void setPerson(String name , String gender) throws InterruptedException {
        if(sync) syncSetPerson(name , gender);
        else  asyncSetPerson(name , gender);
    }


    public synchronized void syncSetPerson (String name , String gender) throws InterruptedException {
        this.name = name;
        Thread.sleep(10);
        this.gender = gender;
        Thread.sleep(10);
        if(!check()) {
            System.out.println("Error!");
        } else {
            System.out.println("Fine...");
        }
    }

    public void asyncSetPerson (String name , String gender) throws InterruptedException {
        this.name = name;
        Thread.sleep(10);
        this.gender = gender;
        Thread.sleep(10);
        if(!check()) {
            System.out.println("Error!");
        } else {
            System.out.println("Fine...");
        }
    }

    private boolean check() {
        return name.equals("Alice") && gender.equals("female")
                || name.equals("Bob") && gender.equals("male");
    }


    public static void main(String[] args) throws InterruptedException {

        final Person p = new Person();

        p.setSync(false);
        System.out.println("==================");
        System.out.println("Asynchronized. There may be some error with name and gender!");
        Thread t1 = getNewThread("Alice" , "female" , p);
        Thread t2 = getNewThread("Bob" , "male" , p);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        p.setSync(true);
        System.out.println("==================");
        System.out.println("Synchronized. There must be 'Fine...' every time. ");
        t1 = getNewThread("Alice" , "female" , p);
        t2 = getNewThread("Bob" , "male" , p);
        t1.start();
        t2.start();

    }

    public static Thread getNewThread(final String name , final String gender , final Person p){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0 ; i < 5 ; i++ ) {
                    try {
                        p.setPerson(name , gender);
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}
