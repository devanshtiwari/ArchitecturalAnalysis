package fileIO;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private FileWriter fileWriter ;
    private String writeDir;

    public CSVWriter(File file)  throws IOException {
        File output = new File(file.getName() + ".csv");
        fileWriter = new FileWriter(output);
    }

    public CSVWriter(String writeDir){
        this.writeDir = writeDir;

    }

    public void setFile(String subDir, File file) throws IOException {
        File directory = new File(writeDir+File.separator+subDir);
        directory.mkdirs();
        this.fileWriter = new FileWriter(new File(directory,file.getName()+".csv"));
    }

    public void setFile(File file) throws IOException {
        this.fileWriter = new FileWriter(new File(writeDir,file.getName()+".csv"));
    }

    public void setFile(String file) throws IOException {
        setFile(new File(file));
    }

    public void setFile(String subDir, String file) throws IOException {
        setFile(subDir, new File(file));
    }

    public void writez(List<List<String>> csvData) throws Exception {
        if(fileWriter == null)
            throw new Exception("File not set. Call setFile Function before calling write");
        for(List line:csvData){
            for(Object cell:line){
                fileWriter.append((String) cell);
                fileWriter.append(COMMA_DELIMITER);
            }
            fileWriter.append(NEW_LINE_SEPARATOR);
        }
        fileWriter.close();
    }


    public void close() throws IOException {
        fileWriter.flush();
        fileWriter.close();
    }
}
