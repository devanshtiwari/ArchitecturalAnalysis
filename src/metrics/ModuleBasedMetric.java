package metrics;

import java.util.Arrays;
import java.util.List;

public class ModuleBasedMetric extends MetricsClass {

    String moduleName;

    int numOfFiles = -1;
    int numOfFunctions = -1;
    int fileMaxSize = -1;
    double avgFileSize = -1;
    double fileSizeDeviation = -1;
    double instability = -1;
    double abstraction = -1;
    double distance = -1;
    double dependentModules = -1;
    double avgParameters = -1;

    int numOfFilesGraph = -1;
    int numOfFunctionsGraph = -1;
    int fileMaxSizeGraph = -1;
    double avgFileSizeGraph = -1;
    double fileSizeDeviationGraph = -1;
    double avgParametersGraph = -1;


    public List<String> getRow(){
        return Arrays.asList(moduleName, Integer.toString(numOfFiles), Integer.toString(numOfFunctions), Integer.toString(fileMaxSize),
                Double.toString(avgFileSize), Double.toString(fileSizeDeviation), Integer.toString(numOfFilesGraph), Integer.toString(numOfFunctionsGraph),
                Integer.toString(fileMaxSizeGraph), Double.toString(avgFileSizeGraph), Double.toString(fileSizeDeviationGraph),
                Double.toString(instability), Double.toString(inDegree), Double.toString(outDegree), Double.toString(fanInVisibility),
                Double.toString(fanOutVisibility),
                Double.toString(abstraction), Double.toString(distance), Double.toString(dependentModules),
                Double.toString(avgParameters), Double.toString(avgParametersGraph));
    }

    static List<String> getHeader(){
        return Arrays.asList("Module Name", "Number of Files", "Number of Functions", "File Max Size",
                "Average File Size", "File Size Deviation", "Number of Files(Graph)", "Number of Functions(Graph)",
                "File Maximum Size(Graph)", "Average File Size(Graph)","File Size Deviation(Graph)",
                "Instability", "In Degree", "Out Degree", "Fan In Visibility",
                "Fan Out Visibility",
                "Abstraction", "Distance", "Dependent Modules",
                "Average Parameters", "Average Parameters(Graph)");
    }


}
