package mainApp;

import dependencyManager.Graph;
import fileIO.CSVReader;
import fileIO.CSVWriter;
import metrics.Metrics;
import metrics.MetricsPOJO;
import org.jgrapht.io.ExportException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MetricsApp {

    static List<File> getFiles(File dir) {
        List<File> csvFiles = new ArrayList<>();
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".csv"))
                csvFiles.add(file);
        }
        return csvFiles;
    }

    public static void main(String[] args) throws IOException, ExportException, IllegalAccessException {

        CSVWriter csvWriter = new CSVWriter(new File("/Users/devan/Desktop/Architectural Refactoring Project/Documents from Company/projects/csv/myCSVNew"));
//        File dir = new File("/Users/devan/Desktop/Architectural Refactoring Project/Documents from Company/projects/csv/");
        File dir = new File("/Users/devan/Documents/ArchitecturalAnalysis/All Output/");
        List<MetricsPOJO> metricsPOJOS = new ArrayList<>();
        List<File> csvFiles = getFiles(dir);

        for (File f : csvFiles) {
            //        FileReader csvReader = new FileReader("/Users/devan/Documents/ArchitecturalAnalysis/Output Good/bzip2.csv",true);
            CSVReader csvReader = new CSVReader(f.getAbsolutePath(),true);
            csvReader.read();

            MetricsPOJO metricsPOJO = new MetricsPOJO();

            metricsPOJO.setProjectName(f.getName().substring(0, f.getName().length() - 4));
            System.out.println("--------------------------------\n" + metricsPOJO.getProjectName() + "\n--------------------------------");
            Graph graph;
//            if(!(f.getName().contains("bash") || f.getName().contains("dia")))
//                graph = new Graph(csvReader.getFileData(), metricsPOJO.getProjectName(), true);
//            else
//                graph = new Graph(csvReader.getFileData(), metricsPOJO.getProjectName(), false);
            graph = new Graph(csvReader.getFileData(), metricsPOJO.getProjectName(), false);
            System.out.println(graph.getClusterHashMap());

            Metrics metrics = new Metrics(graph);

            metricsPOJO.setComplexity(metrics.callComplexity());
            metricsPOJO.setAvgFileSize(metrics.averageFileFunctions());
            metricsPOJO.setFileSizeDeviation(metrics.sizeDeviation());
            metricsPOJO.setFileMaxSize(metrics.maxFunctions());
            metricsPOJO.setNumOfFiles(metrics.numberOfFiles());
            metricsPOJO.setNumOfFunctions(metrics.numberOfFunctions());
            double inout[] = metrics.inoutDependency();
            metricsPOJO.setIncomingDepeConc(inout[0]);
            metricsPOJO.setOutgoingDepConc(inout[1]);
            metricsPOJO.setNormalizedCyleEdges(metrics.normalizedCycleEdges());
            metricsPOJO.setNormalizedCyclePaths(metrics.normalizedCyclePaths());
            metricsPOJO.setWeightedDependencyCost(metrics.weightedDependencyCost());
            metricsPOJO.setDependencies(metrics.gdependency());
            metricsPOJO.setWeightedDependencyCostSQ(metrics.weightedDependencyCostSQ());
            metricsPOJO.setFileCuttingNumber(metrics.fileCuttingNumber());
            metricsPOJO.setFunctionCuttingNumber(metrics.functionCuttingNumber());
            metricsPOJO.setFileGraphDensity(metrics.fileDensity());
            metricsPOJO.setFunctionGraphDensity(metrics.functionDensity());
            metricsPOJO.setQValue(metrics.QValue());
            metricsPOJO.setAvgClusterDependency(metrics.avgClusterDensity());
//            metrics.cyclomaticComplexity();
            metricsPOJOS.add(metricsPOJO);

            metrics.matrix();
            metrics.instability();

//            metrics.QValue();
//            metrics.stronglyConnected();
            metrics.connectedSets();
//            metrics.avgClusterDensity();
        }
        csvWriter.writeMetric(metricsPOJOS);
        csvWriter.close();
    }
}
