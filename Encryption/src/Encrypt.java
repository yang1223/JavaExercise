import java.io.*;

/**
 * Created by Yang, Chi-Chang on 2016/5/9.
 */
public class Encrypt {

    static String original = Util.original;
    static String encryption = Util.encryption;

    public static void main(String[] args) throws IOException {

        LastModifiedRecord record = new LastModifiedRecord();

        File eFile = new File(encryption);
        if (!eFile.exists()){
            eFile.mkdirs();
            new File(encryption + "/.local").mkdirs();
            new File(encryption + "/.share").mkdirs();
        }

        File file = new File(original);
        encrypt(file, record);
        record.write();
    }

    public static void encrypt(File rootFile , LastModifiedRecord record) throws IOException {

        for(File file:rootFile.listFiles()){
            if (file.exists()){

                File encryptionFile = new File(Util.getEncryptionFilePath(file.getPath()));

                if(file.isDirectory()) {
                    encryptionFile.mkdirs();
                    encrypt(file, record);
                } else if(record.isLocalModified(file)) {
                    FileInputStream in = new FileInputStream(file.getPath());
                    FileOutputStream out = new FileOutputStream(encryptionFile.getPath());
                    int read;
                    while ( (read = in.read()) != -1 ) {
                        out.write(read + 1);
                    }
                    in.close();
                    out.close();
                    record.update(file.getPath(), encryptionFile.getPath());
                    System.out.println("encrypted " + file.getPath());
                }
            }
        }
    }


}
