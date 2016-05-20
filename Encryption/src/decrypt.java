import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Yang, Chi-Chang on 2016/5/10.
 */
public class decrypt {

    static String encryption = Util.encryption;

    public static void main(String[] args) {
        try {
            LastModifiedRecord record = new LastModifiedRecord();
            File eFile = new File(encryption);
            decryptFiles(eFile, record);
            record.decryptDelete();
            record.write();
        } catch (IOException e) {
            System.out.println("===== ERROR =====");
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("===== ERROR =====");
        }
    }

    public static void decryptFiles(File rootFile, LastModifiedRecord record) throws IOException {

        for(File file:rootFile.listFiles()) {
            if ( Util.couldDecrypt(file) && file.exists()) {
                File originalFile = new File(Util.getOriginalFilePath(file.getPath()));
                if (file.isDirectory()) {
                    originalFile.mkdirs();
                    decryptFiles(file, record);
                } else if (record.isShareModified(file)) {
                    long size = file.length();
                    FileInputStream in = new FileInputStream(file.getPath());
                    FileOutputStream out = new FileOutputStream(originalFile.getPath());
                    boolean isEncrypted = record.isEncrypted(file);
                    int shift = isEncrypted? 1:0;
                    String action = isEncrypted? "decrypted":"copy";
                    action = String.format("%-12s",action);
                    long count = 0;
                    int read;
                    String filename = String.format("%-50s",file.getPath());
                    while ((read = in.read()) != -1) {
                        out.write(read - shift);
                        count++;
                        if (count % 20000 == 0) {
                            String percent = String.format("%3.0f",count*100.0/size) + "%";
                            System.out.print("\r" + action + filename + "\t......" + percent);
                        }
                    }
                    in.close();
                    out.close();
                    record.update(originalFile.getPath() , file.getPath());
                    System.out.print("\r" + action + filename + "\t......100%");
                    System.out.println("");
                }
            }
        }
    }
}
