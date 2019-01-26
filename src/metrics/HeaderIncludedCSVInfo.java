package metrics;

import java.util.ArrayList;
import java.util.Arrays;

public class HeaderIncludedCSVInfo {
    private String headerFileLocation;
    private String headerFileName;
    private ArrayList<String> filesIncluded;
    private double count;

    public String getHeaderFileLocation() {
        return headerFileLocation;
    }

    public void setHeaderFileLocation(String headerFileLocation) {
        this.headerFileLocation = headerFileLocation;
    }

    public String getHeaderFileName() {
        return headerFileName;
    }

    public void setHeaderFileName(String headerFileName) {
        this.headerFileName = headerFileName;
    }

    public ArrayList<String> getFilesIncluded() {
        return filesIncluded;
    }

    public void setFilesIncluded(ArrayList<String> filesIncluded) {
        this.filesIncluded = filesIncluded;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public void setFilesIncludedFromArray(String[] filesIncluded){
        this.filesIncluded = new ArrayList<>(Arrays.asList(filesIncluded));
    }
}
