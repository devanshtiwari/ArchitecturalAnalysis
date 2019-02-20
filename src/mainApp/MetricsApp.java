package mainApp;

import fileIO.CSVWriter;
import fileIO.DirManager;
import fileIO.exceptions.InvalidMasterSourceCodeDirectory;
import javafx.util.Pair;
import metrics.*;

import java.io.File;
import java.util.*;

public class MetricsApp {


    private static HashMap<String, ArrayList<Pair<String, File>>> getFiles(File CSVDir) throws InvalidMasterSourceCodeDirectory {
        HashMap<String, ArrayList<Pair<String, File>>> allCSVFiles = new HashMap<>();

        ArrayList<File> CSVDirs = DirManager.getDirectories(CSVDir);
        for(File subDir : CSVDirs){
            ArrayList<File> csvFiles = DirManager.getFiles(subDir);
            for(File csvFile: csvFiles){
                if(!csvFile.getName().endsWith(".csv"))
                    continue;
                Pair<String, File> pair = new Pair<>(subDir.getName(),csvFile);
                allCSVFiles.computeIfPresent(csvFile.getName().substring(0,csvFile.getName().length()-4),(k,v) -> {v.add(pair);return v;});
                allCSVFiles.putIfAbsent(csvFile.getName().substring(0, csvFile.getName().length() - 4), new ArrayList<>(Arrays.asList(pair)));
            }
        }
        return allCSVFiles;
    }

    public static void main(String[] args) throws Exception {

//        CSVWriter csvWriter = new CSVWriter(new File("/Users/devan/Desktop/Architectural Refactoring Project/Documents from Company/projects/csv/myCSVNew"));
//        File dir = new File("/Users/devan/Desktop/Architectural Refactoring Project/Documents from Company/projects/csv/");
        File dir = new File("/Users/devan/Documents/ArchitecturalAnalysis/CSV");
        List<MetricsPOJO> metricsPOJOS = new ArrayList<>();
        List<MetricsContainer> metricsContainers = new ArrayList<>();
        HashMap<String, ArrayList<Pair<String, File>>> allCSVFiles = getFiles(dir);

        List<List<String>> wholeProjectCSV = new ArrayList<>();
        List<List<String>> nodeModuleCSV;
        List<List<String>> nodeFileCSV;
        List<List<String>> nodeFunctionCSV;
        CSVWriter csvWriter1 = new CSVWriter("CSVMetrics");

        wholeProjectCSV.add(MetricsContainer.getHeader());

        int count = 0;
        int total = allCSVFiles.keySet().size();
        double countLib = 0;
        for (String projectName : allCSVFiles.keySet()) {
            System.out.println(++count + "/" + total);
            long start = System.currentTimeMillis();
            DataOrganiser dataOrganiser = new DataOrganiser();
            dataOrganiser.delegate(projectName,allCSVFiles.get(projectName));
            MetricsInputContent metricsInputContent = dataOrganiser.getMetricsInputContent();

            MetricsContainer metricsContainer  = new MetricsContainer(metricsInputContent);

            metricsContainer.calculateStats();
            if(metricsContainer.isLib > 0)
                countLib++;
            wholeProjectCSV.add(metricsContainer.getRow());
            nodeModuleCSV = metricsContainer.getModuleNodeMetricsCSV();
            nodeFileCSV = metricsContainer.getFileNodeMetricsCSV();
            nodeFunctionCSV = metricsContainer.getFunctionNodeMetricsCSV();
            csvWriter1.setFile("ModuleNode", projectName );
            csvWriter1.writez(nodeModuleCSV);
            csvWriter1.setFile("FileNode", projectName);
            csvWriter1.writez(nodeFileCSV);
            csvWriter1.setFile("FunctionNode", projectName);
            csvWriter1.writez(nodeFunctionCSV);

            System.out.println((start-System.currentTimeMillis())/1000D + " Seconds.");

            //            //        FileReader csvReader = new FileReader("/Users/devan/Documents/ArchitecturalAnalysis/Output Good/bzip2.csv",true);
//            CSVReader csvReader = new CSVReader(f.getAbsolutePath(),true);
//            csvReader.read();
//
//            MetricsPOJO metricsPOJO = new MetricsPOJO();
//
//            metricsPOJO.setProjectName(f.getName().substring(0, f.getName().length() - 4));
//            System.out.println("--------------------------------\n" + metricsPOJO.getProjectName() + "\n--------------------------------");
//            Graph graph;
////            if(!(f.getName().contains("bash") || f.getName().contains("dia")))
////                graph = new Graph(csvReader.getFileData(), metricsPOJO.getProjectName(), true);
////            else
////                graph = new Graph(csvReader.getFileData(), metricsPOJO.getProjectName(), false);
//            graph = new Graph(csvReader.getFileData(), metricsPOJO.getProjectName(), false);
//            System.out.println(graph.getClusterHashMap());

//            Metrics metrics = new Metrics(metricsInputContent.getGraph());
//            MetricsPOJO metricsPOJO = new MetricsPOJO();
//
//            metricsPOJO.setProjectName(projectName);
//
//            metricsPOJO.setComplexity(metrics.callComplexity());
//            metricsPOJO.setAvgFileSize(metrics.averageFileFunctions());
//            metricsPOJO.setFileSizeDeviation(metrics.sizeDeviation());
//            metricsPOJO.setFileMaxSize(metrics.maxFunctions());
//            metricsPOJO.setNumOfFiles(metrics.numberOfFiles());
//            metricsPOJO.setNumOfFunctions(metrics.numberOfFunctions());
//            double inout[] = metrics.inoutDependency();
//            metricsPOJO.setIncomingDepeConc(inout[0]);
//            metricsPOJO.setOutgoingDepConc(inout[1]);
//            metricsPOJO.setNormalizedCyleEdges(metrics.normalizedCycleEdges());
//            metricsPOJO.setNormalizedCyclePaths(metrics.normalizedCyclePaths());
//            metricsPOJO.setWeightedDependencyCost(metrics.weightedDependencyCost());
//            metricsPOJO.setDependencies(metrics.gdependency());
//            metricsPOJO.setWeightedDependencyCostSQ(metrics.weightedDependencyCostSQ());
//            metricsPOJO.setFileCuttingNumber(metrics.fileCuttingNumber());
//            metricsPOJO.setFunctionCuttingNumber(metrics.functionCuttingNumber());
//            metricsPOJO.setFileGraphDensity(metrics.fileDensity());
//            metricsPOJO.setFunctionGraphDensity(metrics.functionDensity());
//            metricsPOJO.setQValue(metrics.QValue());
//            metricsPOJO.setAvgClusterDependency(metrics.avgClusterDensity());
//            metrics.cyclomaticComplexity();
//            metricsPOJOS.add(metricsPOJO);

//            metrics.matrix();
//            metrics.instability();

//            metrics.QValue();
//            metrics.stronglyConnected();
//            metrics.connectedSets();
//            metrics.avgClusterDensity();
        }

        System.out.println("Hit Percentage: " + (countLib/(double)total));
        csvWriter1.setFile("AllProjects");
        csvWriter1.writez(wholeProjectCSV);

//        csvWriter.writeMetric(metricsPOJOS);
//        csvWriter.close();
//        csvWriter1.close();


    }
}
