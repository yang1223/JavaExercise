import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Yang, Chi-Chang on 2016/5/10.
 */
public class Decrypt {

    static String original = Util.original;
    static String encryption = Util.encryption;

    public static void main(String[] args) throws IOException {

        LastModifiedRecord record = new LastModifiedRecord();

        File file = new File(original);
        if(file.mkdirs()) System.out.println("Create original folder!");;

        File eFile = new File(encryption);
        decrypt(eFile, record);
        record.write();
    }

    public static void decrypt(File rootFile, LastModifiedRecord record) throws IOException {

        for(File file:rootFile.listFiles())
            if (file.exists()) {

                File originalFile = new File(Util.getOriginalFilePath(file.getPath()));

                if (file.isDirectory()) {
                    originalFile.mkdirs();
                    decrypt(file, record);
                } else if (record.isLocalModified(file)) { // TODO: record.isLocalModified(file)
                    FileInputStream in = new FileInputStream(file.getPath());
                    FileOutputStream out = new FileOutputStream(originalFile.getPath());
                    int read;
                    while ((read = in.read()) != -1) {
                        out.write(read + 1);
                    }
                    in.close();
                    out.close();
                    record.update(file.getPath(), originalFile.getPath());
                    System.out.println("encrypted " + file.getPath());
                }
            }
    }

}
