package fileIO;
//Just for comparision of previous results
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CSVCompare {
    static class Compare{
        String count1;
        String count2;
    }
    static int getCount(File file) throws IOException {
        String line;
        BufferedReader br =null;
        int count = 0;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try{
        while(br!=null &&(line = br.readLine()) != null)
        {
            count++;
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
        br.close();

        return  count;
    }

    public static void main(String[] args) throws IOException {
        File file1 = new File("/Users/devan/Documents/ArchitecturalAnalysis/All Output");
        File file2 = new File("/Users/devan/Desktop/Architectural Refactoring Project/Documents from Company/projects/csv/test");
        HashMap<String,Compare> csvCount = new HashMap<>();
        for(File f: file1.listFiles()){
            Compare compare = new Compare();
            if(f.getName().endsWith(".csv")) {
                compare.count1 = (Integer.toString(getCount(f)));
            }
            csvCount.put(f.getName().substring(0,f.getName().length()-4),compare);
        }

        for(File f: file2.listFiles()){
            Compare compare = new Compare();
            if(f.getName().endsWith(".csv")) {
                csvCount.get(f.getName().substring(0,f.getName().length()-4)).count2 = (Integer.toString(getCount(f)));
            }
        }

        File output = new File("comparision.csv");
        FileWriter fileWriter = new FileWriter(output);
        fileWriter.append("Project Name,NewCount, OldCount\n");
        for(String key:csvCount.keySet()){
            fileWriter.append(key+","+csvCount.get(key).count1 + "," + csvCount.get(key).count2+"\n");
        }
        fileWriter.close();


    }
}