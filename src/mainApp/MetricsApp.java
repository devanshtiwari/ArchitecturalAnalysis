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

        File CSVdir = new File("/Users/devan/Documents/ArchitecturalAnalysis/CSV");
        HashMap<String, ArrayList<Pair<String, File>>> allCSVFiles = getFiles(CSVdir);

        List<List<String>> nodeModuleCSVCombined = new ArrayList<>();

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
            //Check whether top 3 files with highest indegree are library, ie. they have library or lib as substring in their path.
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

            nodeModuleCSV.remove(0);
            nodeModuleCSVCombined.addAll(nodeModuleCSV);

            System.out.println((System.currentTimeMillis()-start)/1000D + " Seconds.");
        }

        csvWriter1.setFile("ModuleNode", "ModuleNodeCombined");
        csvWriter1.writez(nodeModuleCSVCombined);

        System.out.println("Hit Percentage: " + (countLib/(double)total));
        csvWriter1.setFile("AllProjects");
        csvWriter1.writez(wholeProjectCSV);
    }
}
