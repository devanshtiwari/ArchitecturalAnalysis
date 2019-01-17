package projectReader;

import java.io.File;

public class FilePoint {
    private File file;
    private String function;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
    public Boolean nullCheck(){
        if(file==null || function == null)
            return true;
        else
            return false;
    }
}
