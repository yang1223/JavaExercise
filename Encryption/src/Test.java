import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Yang, Chi-Chang on 2016/5/9.
 */
public class Test {

    public static void main(String[] args) throws IOException {
        File file = new File("original/P3_JayLee.pdf");

        System.out.println(file.length());
        long l = file.length();
        FileInputStream in = new FileInputStream(file.getPath());
        int count = 0;
        int read;
        while ((read = in.read()) != -1 ) {
            count++;
            if (count % 400000 == 0) {
                System.out.println(String.format("%.0f",count*100.0/l) + "%");
            }
        }
        System.out.println(count);

    }



}
