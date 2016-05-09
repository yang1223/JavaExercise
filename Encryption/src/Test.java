import java.io.File;

/**
 * Created by Yang, Chi-Chang on 2016/5/9.
 */
public class Test {

    public static void main(String[] args) {
        File file = new File("original/a.txt");


        System.out.println(file.lastModified());

    }

}
