package gitinfo;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileReader {

    File file;
    private BufferedReader br;
    private String line;

    public List<String> getFileData() {
        return fileData;
    }

    private List<String> fileData = new ArrayList<>();

    private final String COMMA_DELIMITER = ",";

    /**
     * Contructor to initialize the spreadsheet reader with a CSV File. The first line is always taken as headers of the CSV File.
     * @param sspath Spreadsheet File Path
     */
    public FileReader(String sspath) {
        file = new File(sspath);
        try {
            this.br = new BufferedReader(new java.io.FileReader(sspath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.line = "";
        this.read();
    }
    /**
     * ReadCSV Reades each line of the CSV Report and puts it in an object of Report Class which implments its structure.
     * @link internal is the Report object used inherited from the ReadSpradsheet
     */
    private  void read() {
        String data = null;
        try {
            List<String> row;

            while((line = br.readLine()) != null)
            {
                row = Arrays.asList(line.split(COMMA_DELIMITER));
                fileData.addAll(row);
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
            System.out.println(fileData.get(i));
    }
}
