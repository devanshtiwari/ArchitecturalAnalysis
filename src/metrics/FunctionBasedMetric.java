package metrics;

import java.util.Arrays;
import java.util.List;

public class FunctionBasedMetric extends MetricsClass {

    String fileName;
    String functionName;
    String onlyFunctionName;

    int numOfParameters = -1;
    int numOfParametersGraph = -1;

    public List<String> getRow(){
        return Arrays.asList(fileName, functionName, Double.toString(inDegree), Double.toString(outDegree),
                Double.toString(fanInVisibility), Double.toString(fanOutVisibility),
                Integer.toString(numOfParameters), Integer.toString(numOfParametersGraph));

    }

    static List<String> getHeader(){
        return Arrays.asList("File Name", "Function Name", "In Degrees", "OutDegrees",
                "Fan In Visibility", "Fan Out Visibility",
                "Number of Parameters", "Number of Parameters(Graph)");
    }

}
