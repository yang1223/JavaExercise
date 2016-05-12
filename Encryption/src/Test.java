import java.io.File;
import java.io.IOException;

/**
 * Created by Yang, Chi-Chang on 2016/5/9.
 */
public class Test {

    public static void main(String[] args) throws IOException {

        LastModifiedRecord record = new LastModifiedRecord();
        File file = new File(".encrypted/test.txt");
        System.out.println(record.isShareModified(file));


    }



}
