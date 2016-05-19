import java.io.*;

/**
 * Created by Yang, Chi-Chang on 2016/5/9.
 */
public class encrypt {

    static String original = Util.original;

    public static void main(String[] args) {
        try {
            LastModifiedRecord record = new LastModifiedRecord();
            File file = new File(original);
            encryptFiles(file, record);
            record.encryptDelete();
            record.write();
        } catch (IOException e){
            System.out.println("===== ERROR =====");
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("===== ERROR =====");
        }
    }

    public static void encryptFiles(File rootFile, LastModifiedRecord record) throws IOException {

        for(File file:rootFile.listFiles()){
            if (file.exists()){
                File encryptionFile = new File(Util.getEncryptionFilePath(file.getPath()));
                if(file.isDirectory()) {
                    encryptionFile.mkdirs();
                    encryptFiles(file, record);
                } else if(record.isLocalModified(file)) {
                    long size = file.length();
                    FileInputStream in = new FileInputStream(file.getPath());
                    FileOutputStream out = new FileOutputStream(encryptionFile.getPath());
                    boolean isEncrypted = record.isEncrypted(file);
                    int shift = isEncrypted? 1:0;
                    String action = isEncrypted? "encrypted":"copy";
                    action = String.format("%-12s",action);
                    long count = 0;
                    int read;
                    String filename = String.format("%-50s",file.getPath());
                    while ( (read = in.read()) != -1 ) {
                        out.write(read + shift);
                        count++;
                        if (count % 20000 == 0) {
                            String percent = String.format("%3.0f",count*100.0/size) + "%";
                            System.out.print("\r" + action + filename + "\t......" + percent);
                        }
                    }
                    in.close();
                    out.close();
                    record.update(file.getPath(), encryptionFile.getPath());
                    System.out.print("\r" + action + filename + "\t......100%");
                    System.out.println("");
                }
            }
        }
    }
}
