package gitinfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    FileWriter fileWriter ;

    File output;
    public CSVWriter(File file, Boolean check)  throws IOException {
        output = new File(file.getName()+".csv");
        fileWriter = new FileWriter(output,true);
        if(check == true){
            fileWriter.append("Name,Commits,Stars,Fork,Open Issues,Subscribers Count,Age\n");
        }
    }

    public CSVWriter(File file) throws IOException {
        this(file, false);
    }

    public void write(List<String> list)  throws IOException {
        for(String val:list) {
            fileWriter.append(val);
            fileWriter.append(COMMA_DELIMITER);
        }
        fileWriter.append(NEW_LINE_SEPARATOR);
    }

    public void close() throws IOException {
        fileWriter.flush();
        fileWriter.close();
    }
}
