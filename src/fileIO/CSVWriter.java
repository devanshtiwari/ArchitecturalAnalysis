package fileIO;

import metrics.MetricsPOJO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

    public void writez(ArrayList<ArrayList<String>> csvData) throws Exception {
        if(fileWriter == null)
            throw new Exception("File not set. Call setFile Function before calling write");
        for(ArrayList line:csvData){
            for(Object cell:line){
                fileWriter.append((String) cell);
                fileWriter.append(COMMA_DELIMITER);
            }
            fileWriter.append(NEW_LINE_SEPARATOR);
        }
        fileWriter.close();
    }


    public void writeMetric(List<MetricsPOJO> metricsPOJOS) throws IOException, IllegalAccessException {
//        System.out.println("writer called");

//        fileWriter.append("Name, Call Complexity, Dependency Concentration (In), Dependency Concentration (Out),Depenedencies, File Size Deviation, File size (Ave), File size (Max), Files, Functions, Normalized Cycle Edges, Normalized Cycle Paths, Weighted Dependency Cost, Weighted Dependency Cost SQ, File Cutting Number, Function Cutting Number, File Density, Function Density \n");
        fileWriter.append(metricsPOJOS.get(0).getHeaders());
        for(MetricsPOJO metricsPOJO: metricsPOJOS){

            fileWriter.append(metricsPOJO.getValues());
//            fileWriter.append(metricsPOJO.getProjectName());
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(Double.toString(metricsPOJO.getComplexity()));
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(Double.toString(metricsPOJO.getIncomingDependency()));
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(Double.toString(metricsPOJO.getOutgoingDependency()));
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(Double.toString(metricsPOJO.getDependencies()));
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(Double.toString(metricsPOJO.getDeviation()));
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(Double.toString(metricsPOJO.getAvgFileSize()));
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(Integer.toString(metricsPOJO.getFileMaxSize()));
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(Integer.toString(metricsPOJO.getNumOfFiles()));
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(Integer.toString(metricsPOJO.getNumOfFunctions()));
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(Double.toString(metricsPOJO.getNormalizedCycleEdges()));
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(Double.toString(metricsPOJO.getNormalizedCyclePaths()));
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(Double.toString(metricsPOJO.getWeightedDependencyCost()));
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(Double.toString(metricsPOJO.getWeightedDependencyCostSQ()));
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(Double.toString(metricsPOJO.getFileCuttingNumber()));
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(Double.toString(metricsPOJO.getFunctionCuttingNumber()));
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(Double.toString(metricsPOJO.getFileGraphDensity()));
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(Double.toString(metricsPOJO.getFunctionGraphDensity()));
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(NEW_LINE_SEPARATOR);
        }
    }

    public void close() throws IOException {
        fileWriter.flush();
        fileWriter.close();
    }
}
