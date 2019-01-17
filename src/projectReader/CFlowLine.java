package projectReader;

import java.io.File;

public class CFlowLine {

    class Function{
        String name;
        String returntype;
        String parameters;
        String funSourceFile;
    }

    String line;
    File file;
    int lineno;
    int depth;
    Function function = new Function();

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFunction() {
        return function.name;
    }

    public void setFunction(String function) {
        this.function.name = function;
    }

}
