package fileIO;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVReader {

    private File ssFile;
    private String[] headers;
    private BufferedReader br;
    private String line;

    private ArrayList<List<String>> csvData = new ArrayList<>();


    public ArrayList<List<String>> getCsvData() {
        return csvData;
    }

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
                this.headers = line.split(COMMA_DELIMITER,0);
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
        try {
//            br = new BufferedReader(new FileReader(ssFile)); // Just in case headers are already read.
//            br.readLine();
            String[] row;

            while((line = br.readLine()) != null)
            {
                row = line.split(COMMA_DELIMITER,0);
                if(row.length != 0)
                    csvData.add(Arrays.asList(row));
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
        for(List<String> row : csvData)
            for(String str: row){
                System.out.println(str + ",");
            }
        System.out.println();
    }
}
