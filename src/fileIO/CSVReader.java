package fileIO;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CSVReader {

    File ssFile;
    String[] headers;
    private BufferedReader br;
    private String line;

    public List<CSVData> getFileData() {
        return fileData;
    }

    private List<CSVData> fileData = new ArrayList<CSVData>();

    private final String COMMA_DELIMITER = ",";

    /**
     * Contructor to initialize the spreadsheet reader with a CSV File. The first line is always taken as headers of the CSV File.
     * @param sspath Spreadsheet File Path
     */
    public CSVReader(String sspath, Boolean headerPresent) {
        ssFile = new File(sspath);
        try {
            this.br = new BufferedReader(new FileReader(sspath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.line = "";
        if(headerPresent)
            this.setHeaders();
        this.read();
    }

    public CSVReader(String sspath){
        this(sspath,false);
    }

    /**
     * The First Line of the CSV is BY DEFAULT set as headers.
     */
    protected void setHeaders() {
        try {
            if ((line = br.readLine()) != null) {
                this.headers = line.split(COMMA_DELIMITER,-1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read function here calls readCSV.
     */
    public void read(){
        readCSV();
    }

    /**
     * ReadCSV Reades each line of the CSV Report and puts it in an object of Report Class which implments its structure.
     * @link internal is the Report object used inherited from the ReadSpradsheet
     */
    private  void readCSV() {
        CSVData csvData = null;
        try {
//            br = new BufferedReader(new FileReader(ssFile)); // Just in case headers are already read.
//            br.readLine();
            String[] row;

            while((line = br.readLine()) != null)
            {
                csvData = new CSVData();
                row = line.split(COMMA_DELIMITER,-1);
                if(row.length!=4)
                    System.out.println("CSV File Error, does not have complete row in file " + ssFile.getName());
                csvData.setFile1(row[0]);
                csvData.setFunction1(row[1]);
                csvData.setFile2(row[2]);
                csvData.setFunction2(row[3]);
                fileData.add(csvData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Console Out prints the whole report in the console.
     */
    public void consoleOut()
    {
        for(int i = 0;i < fileData.size();i++)
            System.out.println(fileData.get(i).getFile1() + "/"+ fileData.get(i).getFunction1() + "-->" + fileData.get(i).getFile2() + "/"+ fileData.get(i).getFunction2() );
    }
}
