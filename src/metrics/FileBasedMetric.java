package metrics;

import java.util.Arrays;
import java.util.List;

public class FileBasedMetric extends MetricsClass {

    String fileName;

    int numOfFunctions = -1;
    int externalFunctionsCalled = -1;
    double avgParameters = -1;

    int numOfFunctionsGraph = -1;
    int avgParametersGraph = -1;

    public List<String> getRow(){
        return Arrays.asList(fileName, Integer.toString(numOfFunctions), Integer.toString(externalFunctionsCalled), Double.toString(avgParameters),
                Integer.toString(numOfFunctionsGraph), Integer.toString(avgParametersGraph));
    }

    static List<String> getHeader(){
        return Arrays.asList("File Name", "Numbe of functions", "External Functions Called", "Average Parameters",
                "Number of Functions(Graph)", "Average Parameters(Graph)");
    }

}
