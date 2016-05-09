import java.io.*;

/**
 * Created by Yang, Chi-Chang on 2016/5/9.
 */
public class Encrypt {

    static String original = "original";
    static String encryption = ".encrypted";

    public static void main(String[] args) throws IOException {



//        File file = new File("original");
        File file1 = new File("original/a.txt");

        File eFile = new File(encryption);
        if (!eFile.exists()) eFile.mkdirs();

        String encrypt = file1.getPath();
        encrypt = encrypt.replaceFirst(original, encryption);
        File encryptionFile = new File(encrypt);
        FileInputStream in = new FileInputStream(file1.getPath());
        FileOutputStream out = new FileOutputStream(encryptionFile.getPath());

        int read;
        while ( (read = in.read()) != -1 ){
            out.write(read+1);
        }
        in.close();
        out.close();

//        for(File f:file.listFiles()){
//            System.out.println(f.getPath());
//        }



    }

    public static void encrypt(File rootFile , String encryptionFolder) throws IOException {

        for(File file:rootFile.listFiles()){
            if (file.exists()){
                if(file.isDirectory()){
                    encrypt(file , encryptionFolder);
                } else {
                    String encrypt = file.getPath();
                    encrypt = encrypt.replaceFirst(original, encryptionFolder);
                    File encryptionFile = new File(encrypt);
                    FileInputStream in = new FileInputStream(file.getPath());
                    FileOutputStream out = new FileOutputStream(encryptionFile.getPath());

                    int read;
                    while ( (read = in.read()) != -1 ){
                        out.write(read+1);
                    }
                    in.close();
                    out.close();
                }
            }
        }


    }


}
