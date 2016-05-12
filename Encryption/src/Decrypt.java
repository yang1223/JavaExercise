import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Yang, Chi-Chang on 2016/5/10.
 */
public class Decrypt {

    static String encryption = Util.encryption;

    public static void main(String[] args) {
        try {
            LastModifiedRecord record = new LastModifiedRecord();
            File eFile = new File(encryption);
            decrypt(eFile, record);
            record.write();
            record.decryptDelete();
        } catch (IOException e) {
            System.out.println("===== ERROR =====");
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("===== ERROR =====");
        }
    }

    public static void decrypt(File rootFile, LastModifiedRecord record) throws IOException {

        for(File file:rootFile.listFiles()) {
            if ( Util.couldDecrypt(file) && file.exists()) {
                File originalFile = new File(Util.getOriginalFilePath(file.getPath()));
                if (file.isDirectory()) {
                    originalFile.mkdirs();
                    decrypt(file, record);
                } else if (record.isShareModified(file)) {
                    long size = file.length();
                    FileInputStream in = new FileInputStream(file.getPath());
                    FileOutputStream out = new FileOutputStream(originalFile.getPath());
                    boolean isEncrypted = record.isEncrypted(file);
                    int shift = isEncrypted? 1:0;
                    String action = isEncrypted? "decrypted\t":"copy\t\t";
                    long count = 0;
                    int read;
                    while ((read = in.read()) != -1) {
                        out.write(read - shift);
                        count++;
                        if (count % 400000 == 0) {
                            String percent = String.format("%3.0f",count*100.0/size) + "%";
                            System.out.println(action + file.getPath() + "\t\t......" + percent);
                        }
                    }
                    in.close();
                    out.close();
                    record.update(originalFile.getPath() , file.getPath());
                    System.out.println(action + file.getPath() + "\t\t......100%");
                }
            }
        }
    }
}
