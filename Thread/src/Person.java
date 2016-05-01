/**
 * Created by Yang, Chi-Chang on 2016/5/1.
 */
public class Person {

    private String name;
    private String gender;

    public void setPerson(String name , String gender){
        this.name = name;
        this.gender = gender;
        if(!check()) {
            System.out.println("Not equal!");
        } else {
            System.out.println(".");
        }
    }

    private boolean check(){
        if (name.equals("Alice") && gender.equals("female")) return true;
        else if (name.equals("Bob") && gender.equals("male")) return true;
        else return false;
    }


    public static void main(String[] args) {
        final Person p = new Person();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) p.setPerson("Alice" , "female");
            }
        });


        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) p.setPerson("Bob" , "male");
            }
        });

    }


}
