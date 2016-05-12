import java.io.File;

/**
 * Created by Yang, Chi-Chang on 2016/5/10.
 */
public class Util {

    public static final String original = "original";
    public static final String encryption = ".encrypted";

    public static String getEncryptionFilePath(String filePath){
        return filePath.replaceFirst(original, encryption);
    }


    public static String getOriginalFilePath(String filePath){
        return filePath.replaceFirst(encryption, original);
    }

    public static boolean couldDecrypt(File file){
        String filePath = file.getPath();
        return !(filePath.endsWith(".local") || filePath.endsWith(".share") || filePath.endsWith(".gitignore"));
    }

}
