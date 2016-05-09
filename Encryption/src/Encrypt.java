import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yang, Chi-Chang on 2016/5/9.
 */
public class Encrypt {

    static String original = "original";
    static String encryption = ".encrypted";

    public static void main(String[] args) throws IOException {

        LastModifiedRecord record = new LastModifiedRecord();

        File file = new File(original);
        File eFile = new File(encryption);
        if (!eFile.exists()){
            eFile.mkdirs();
            new File(encryption + "/.local").mkdirs();
            new File(encryption + "/.share").mkdirs();
        }

        encrypt(file, encryption, record);
        record.write();
    }

    public static void encrypt(File rootFile , String encryptionFolder , LastModifiedRecord record) throws IOException {

        for(File file:rootFile.listFiles()){
            if (file.exists()){
                if(file.isDirectory()){
                    String encrypt = file.getPath();
                    encrypt = encrypt.replaceFirst(original, encryptionFolder);
                    File encryptionFile = new File(encrypt);
                    encryptionFile.mkdirs();
                    encrypt(file, encryptionFolder,record);

                } else {
                    if(record.isModified(file)){
                        File encryptionFile = new File( getEncryptionFilePath(file.getPath()) );
                        FileInputStream in = new FileInputStream(file.getPath());
                        FileOutputStream out = new FileOutputStream(encryptionFile.getPath());
                        int read;
                        while ( (read = in.read()) != -1 ) out.write(read + 1);
                        in.close();
                        out.close();
                        record.encrypt(file.getPath(), encryptionFile.getPath());
                        System.out.println("encrypted " + file.getPath());
                    }
                }
            }
        }
    }

    public static String getEncryptionFilePath(String filePath){
        return filePath.replaceFirst(original, encryption);
    }

    public static class LastModifiedRecord {
        private Map<String,Long> local;
        private Map<String,Long> share;

        private String localTsv;
        private String shareTsv;

        LastModifiedRecord(){
            localTsv = encryption + "/.local/lastModified.tsv";
            shareTsv = encryption + "/.share/lastModified.tsv";
            local = readLocal();
            share = readShare();
        }

        public void encrypt(String filename , String encryptionFilename) {
            File file = new File(filename);
            local.put(file.getPath() , file.lastModified());
            File encryptionFile = new File(encryptionFilename);
            share.put(encryptionFile.getPath() , encryptionFile.lastModified());
        }

        public void write() throws IOException {
            FileWriter localFw = new FileWriter(localTsv);
            for(Map.Entry entry:local.entrySet()){
                localFw.write(entry.getKey() + "\t" + entry.getValue() + System.getProperty("line.separator"));
            }
            localFw.close();
            FileWriter shareFw = new FileWriter(shareTsv);
            for(Map.Entry entry:local.entrySet()){
                shareFw.write(entry.getKey() + "\t" + entry.getValue() + System.getProperty("line.separator"));
            }
            shareFw.close();
        }

        public boolean isModified(File file){
            if (local.containsKey(file.getPath())) return file.lastModified() != local.get(file.getPath());
            return true;
        }

        private Map<String,Long> readLocal(){
            Map<String,Long> local = new HashMap<String, Long>();
            try{
                BufferedReader br = new BufferedReader(new FileReader(localTsv));
                while (br.ready()){
                    String[] line = br.readLine().split("\\t");
                    local.put(line[0],Long.parseLong(line[1]));
                }
                br.close();
            } catch (IOException e) {
                System.out.println("");
            }
            return local;
        }

        private Map<String,Long> readShare() {
            Map<String,Long> share = new HashMap<String, Long>();
            try {
                BufferedReader br = new BufferedReader(new FileReader(shareTsv));
                while (br.ready()){
                    String[] line = br.readLine().split("\\t");
                    share.put(line[0],Long.parseLong(line[1]));
                }
                br.close();
            } catch (IOException e) {
                System.out.println("");
            }
            return share;
        }
    }


}
