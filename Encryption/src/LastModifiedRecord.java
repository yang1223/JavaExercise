import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yang, Chi-Chang on 2016/5/10.
 */
public class LastModifiedRecord {

    static String encryption = Util.encryption;

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

    public void update(String localFilename, String shareFilename) {
        File localFile = new File(localFilename);
        local.put(localFile.getPath(), localFile.lastModified());
        File shareFile = new File(shareFilename);
        share.put(shareFile.getPath(), shareFile.lastModified());
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

    public boolean isLocalModified(File file){
        if (local.containsKey(file.getPath())) return file.lastModified() != local.get(file.getPath());
        return true;
    }

    public boolean isShareModified(File file){
        if (share.containsKey(file.getPath())) return file.lastModified() != share.get(file.getPath());
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
