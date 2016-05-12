import java.io.*;

/**
 * Created by Yang, Chi-Chang on 2016/5/9.
 */
public class Encrypt {

    static String original = Util.original;

    public static void main(String[] args) {
        try {
            LastModifiedRecord record = new LastModifiedRecord();
            File file = new File(original);
            encrypt(file, record);
            record.write();
            record.encryptDelete();
        } catch (IOException e){
            System.out.println("===== ERROR =====");
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("===== ERROR =====");
        }
    }

    public static void encrypt(File rootFile , LastModifiedRecord record) throws IOException {

        for(File file:rootFile.listFiles()){
            if (file.exists()){
                File encryptionFile = new File(Util.getEncryptionFilePath(file.getPath()));
                if(file.isDirectory()) {
                    encryptionFile.mkdirs();
                    encrypt(file, record);
                } else if(record.isLocalModified(file)) {
                    long size = file.length();
                    FileInputStream in = new FileInputStream(file.getPath());
                    FileOutputStream out = new FileOutputStream(encryptionFile.getPath());
                    boolean isEncrypted = record.isEncrypted(file);
                    int shift = isEncrypted? 1:0;
                    String action = isEncrypted? "encrypted\t":"copy\t\t";
                    long count = 0;
                    int read;
                    while ( (read = in.read()) != -1 ) {
                        out.write(read + shift);
                        count++;
                        if (count % 400000 == 0) {
                            String percent = String.format("%3.0f",count*100.0/size) + "%";
                            System.out.println(action + file.getPath() + "\t\t......" + percent);
                        }
                    }
                    in.close();
                    out.close();
                    record.update(file.getPath(), encryptionFile.getPath());
                    System.out.println(action + file.getPath() + "\t\t......100%");
                }
            }
        }
    }
}
