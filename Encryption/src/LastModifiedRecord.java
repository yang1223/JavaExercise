import java.io.*;
import java.util.*;

/**
 * Created by Yang, Chi-Chang on 2016/5/10.
 */
public class LastModifiedRecord {

    static String encryption = Util.encryption;
    static String original = Util.original;

    private Map<String,Long> local;
    private Map<String,Long> share;
    private List<String> localToDelete = new LinkedList<String>();
    private List<String> shareToDelete = new LinkedList<String>();

    private List<String> openFile;

    private String localTsv;
    private String shareTsv;


    LastModifiedRecord(){
        localTsv = encryption + "/.local/lastModified.tsv";
        shareTsv = encryption + "/.share/lastModified.tsv";
        local = readLocal();
        share = readShare();
        openFile = readOpenFile();
        try {
            init();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void update(String localFilename, String shareFilename) {
        File localFile = new File(localFilename);
        local.put(localFile.getPath(), localFile.lastModified());
        File shareFile = new File(shareFilename);
        share.put(shareFile.getPath(), shareFile.lastModified());
    }

    public void write() throws IOException {
        new File(encryption + "/.local").mkdirs();
        new File(encryption + "/.share").mkdirs();

        BufferedWriter localFw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(localTsv), "UTF-8"));
        for(Map.Entry entry:local.entrySet()){
            localFw.write(entry.getKey() + "\t" + entry.getValue() + System.getProperty("line.separator"));
        }
        localFw.close();
        BufferedWriter shareFw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(shareTsv), "UTF-8"));
        for(Map.Entry entry:share.entrySet()){
            shareFw.write(entry.getKey() + "\t" + entry.getValue() + System.getProperty("line.separator"));
        }
        shareFw.close();
    }

    public void encryptDelete() {
        for(String filePath:localToDelete){
            local.remove(filePath);
            File file = new File(Util.getEncryptionFilePath(filePath));
            if (file.delete()) System.out.println("delete\t\t" + file.getPath());
            share.remove(file.getPath());
        }
    }

    public void decryptDelete() {
        for(String filePath:shareToDelete){
            share.remove(filePath);
            File file = new File(Util.getOriginalFilePath(filePath));
            if (file.delete()) System.out.println("delete\t\t" + file.getPath());
            local.remove(file.getPath());
        }
    }

    public boolean isLocalModified(File file) {
        String filePath = file.getPath();
        localToDelete.remove(filePath);
        return !local.containsKey(filePath)
                || file.lastModified() != local.get(filePath);
    }

    public boolean isShareModified(File file) {
        String filePath = file.getPath();
        shareToDelete.remove(filePath);
        return !share.containsKey(filePath)
                || file.lastModified() != share.get(filePath);
    }

    public boolean isEncrypted(File file){
        for(String s:openFile){
            if(file.getName().equals(s)) return false;
        }
        return true;
    }

    private Map<String,Long> readLocal(){
        Map<String,Long> local = new HashMap<String, Long>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(localTsv));
            while (br.ready()){
                String[] line = br.readLine().split("\\t");
                local.put(line[0], Long.parseLong(line[1]));
                localToDelete.add(line[0]);
            }
            br.close();
        } catch (IOException e) {

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
                shareToDelete.add(line[0]);
            }
            br.close();
        } catch (IOException e) {

        }
        return share;
    }

    public List<String> readOpenFile() {
        List<String> list = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(original + "/OpenList.xxx"));
            while (br.ready()) list.add(br.readLine());
            br.close();
        } catch (IOException e) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(encryption + "/OpenList.xxx"));
                while (br.ready()) list.add(br.readLine());
                br.close();
            } catch (IOException ee) {

            }
        }
        return list;
    }

    private static void init() throws IOException {
        File oFile = new File(original);
        File eFile = new File(encryption);
        File gitFile = new File(".gitignore");
        if (!eFile.exists()) {
            eFile.mkdirs();
            new File(encryption + "/.local").mkdirs();
            new File(encryption + "/.share").mkdirs();
            FileWriter fw = new FileWriter(encryption + "/.gitignore");
            fw.write(".local/" + System.getProperty("line.separator"));
            fw.close();
            System.out.println("Create encrypted folder!");
        }
        if (!oFile.exists()) {
            oFile.mkdirs();
            System.out.println("Create original folder!");
        }
        if (!gitFile.exists()) {
            FileWriter fw = new FileWriter(gitFile);
            fw.write("original/" + System.getProperty("line.separator"));
            fw.write("*.class" + System.getProperty("line.separator"));
            fw.close();
        }
    }

}
