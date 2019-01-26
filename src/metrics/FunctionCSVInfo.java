package metrics;

import java.util.ArrayList;
import java.util.Arrays;

public class FunctionCSVInfo {
    String fileName;
    String functionName;
    ArrayList<String> parameters;
    int count;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setParametersFromArray(String[] parameters){
        this.parameters = new ArrayList<>(Arrays.asList(parameters));
    }



}
