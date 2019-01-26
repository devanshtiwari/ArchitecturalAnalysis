package metrics;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class FileHeaderCSVInfo {

    String fileName;
    ArrayList<String> headers;


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ArrayList<String> getHeaders() {
        return headers;
    }

    public void setHeaders(ArrayList<String> headers) {
        this.headers = headers;
    }

    public void setHeadersFromArray(String[] headers){
        this.headers = new ArrayList<>(Arrays.asList(headers));
    }
}
