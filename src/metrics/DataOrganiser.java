package metrics;

import dependencyManager.Graph;
import fileIO.CSVReader;
import javafx.util.Pair;

import java.io.File;
import java.util.*;

public class DataOrganiser {

    private String projectName;
    private MetricsInputContent metricsInputContent;

    public MetricsInputContent getMetricsInputContent() {
        return metricsInputContent;
    }

    public void delegate(String projectName, ArrayList<Pair<String, File>> csvFiles) throws Exception {
        this.projectName  = projectName;
        metricsInputContent= new MetricsInputContent();
        metricsInputContent.setProjectName(projectName);
        System.out.println("--------------------------------\n" + projectName + "\n--------------------------------");
        for(Pair<String, File> pair: csvFiles) {
            if (pair.getKey().equals("Dependencies"))
                evaluateDependency(pair.getValue());
            else if (pair.getKey().equals("FileInfo"))
                evaluateFileInfo(pair.getValue());
            else if (pair.getKey().equals("FunctionInfo"))
                evaluateFunctionInfo(pair.getValue());
            else if (pair.getKey().equals("HeaderIncludeInfo"))
                evaluateHeaderIncludeInfo(pair.getValue());
            else
                throw new Exception("Wrong input from CSV Directory");
        }

    }

    private void evaluateHeaderIncludeInfo(File csvFile) {
        CSVReader csvReader = new CSVReader(csvFile.getAbsolutePath(),true);
        csvReader.read();
        mapToHeaderIncludedCSVInfo(csvReader.getCsvData());
    }

    private void mapToHeaderIncludedCSVInfo(ArrayList<List<String>> csvData) {
        ArrayList<HeaderIncludedCSVInfo> data = new ArrayList<>();
        for(List<String> row: csvData){
            if(row.size()!=4) {
//                System.out.println("Problem with the size of the input of Header to File Info CSV File");
                continue;
            }
            HeaderIncludedCSVInfo headerIncludedCSVInfo = new HeaderIncludedCSVInfo();
            headerIncludedCSVInfo.setHeaderFileLocation(row.get(0));
            headerIncludedCSVInfo.setHeaderFileName(row.get(1));
            headerIncludedCSVInfo.setFilesIncludedFromArray(row.get(2).split(";",0));
            headerIncludedCSVInfo.setCount(Double.parseDouble(row.get(3)));
            data.add(headerIncludedCSVInfo);
        }
        metricsInputContent.setHeaderIncludedCSVInfoArrayList(data);
    }

    private void evaluateFunctionInfo(File csvFile) {
        CSVReader csvReader = new CSVReader(csvFile.getAbsolutePath(),true);
        csvReader.read();
        mapToFunctionCSVInfo(csvReader.getCsvData());
    }

    private void mapToFunctionCSVInfo(ArrayList<List<String>> csvData) {
        HashMap<String, FunctionCSVInfo> data = new HashMap<>();
        HashMap<String,HashSet<String>> filetoFunctionMap = new HashMap<>();
        HashMap<String,HashSet<String>> moduleToFileList = new HashMap<>();

        for(List<String> row: csvData){
            if(row.size()!=4) {
//                System.out.println("Problem with the size of the input of Function Info CSV File");
                continue;
            }
            FunctionCSVInfo functionCSVInfo = new FunctionCSVInfo();
            String file = row.get(0);
            functionCSVInfo.setFileName(file);
            String dir = "";

            filetoFunctionMap.computeIfPresent(functionCSVInfo.getFileName(),(k,v) ->{v.add(row.get(1));return v;});
            filetoFunctionMap.putIfAbsent(functionCSVInfo.getFileName(),new HashSet<>(Arrays.asList(row.get(1))));

            if (file.lastIndexOf("/")>=0)
                dir = file.substring(0, file.lastIndexOf("/"));
            else
                dir = "Root/" + projectName;

            moduleToFileList.computeIfPresent(dir,(k,v) -> {v.add(file);return v;});
            moduleToFileList.putIfAbsent(dir,new HashSet<>(Arrays.asList(file)));

            functionCSVInfo.setFunctionName(row.get(1));
            functionCSVInfo.setParametersFromArray(row.get(2).split(";",0));
            functionCSVInfo.setCount(Integer.parseInt(row.get(3)));
            data.put(file + "/" + functionCSVInfo.getFunctionName(),functionCSVInfo);
        }
        metricsInputContent.setFunctionCSVInfoArrayList(data);
        metricsInputContent.setFiletoFunctionMap(filetoFunctionMap);
        metricsInputContent.setModuletoFileMap(moduleToFileList);
    }

    private void evaluateFileInfo(File csvFile) {
        CSVReader csvReader = new CSVReader(csvFile.getAbsolutePath(),true);
        csvReader.read();
        mapToFileHeaderCSVInfo(csvReader.getCsvData());

    }

    private void mapToFileHeaderCSVInfo(ArrayList<List<String>> csvData) {
        ArrayList<FileHeaderCSVInfo> data = new ArrayList<>();
        for(List<String> row: csvData){
            if(row.size()<1) {
//                System.out.println("Problem with the size of the input of File Header Info CSV File");
                continue;
            }
            FileHeaderCSVInfo fileHeaderCSVInfo = new FileHeaderCSVInfo();
            fileHeaderCSVInfo.setFileName(row.get(0));
            if(row.size() == 1)
                fileHeaderCSVInfo.setHeadersFromArray(new String []{});
            else
                fileHeaderCSVInfo.setHeadersFromArray(row.get(1).split(";",0));

            //In case ctags can not detect any functions in a c file, but that file is there, it should be added.
            String dir;
            if (row.get(0).lastIndexOf("/")>=0)
                dir = row.get(0).substring(0, row.get(0).lastIndexOf("/"));
            else
                dir = "Root/" + projectName;

            metricsInputContent.getModuletoFileMap().computeIfPresent(dir,(k,v)->{v.add(row.get(0));return v;});
            metricsInputContent.getModuletoFileMap().putIfAbsent(dir, new HashSet<>(Arrays.asList(row.get(0))));
            metricsInputContent.getFiletoFunctionMap().computeIfPresent(row.get(0),(k,v)-> v);
            metricsInputContent.getFiletoFunctionMap().putIfAbsent(row.get(0), new HashSet<>(Arrays.asList()));

            data.add(fileHeaderCSVInfo);
        }
        metricsInputContent.setFileHeaderCSVInfoArrayList(data);
    }

    private void evaluateDependency(File csvFile) {
            CSVReader csvReader = new CSVReader(csvFile.getAbsolutePath(), true);
            csvReader.read();
            Graph graph = new Graph(projectName);
            ArrayList<DependencyCSVData> dependencyCSVDataList = mapToDependencyCSVData(csvReader.getCsvData());
            //Add true here to generate dependency graph image of file and module graph.
            graph.setDependencyCSVData(dependencyCSVDataList,true);
            metricsInputContent.setGraph(graph);

    }

    private ArrayList<DependencyCSVData> mapToDependencyCSVData(ArrayList<List<String>> csvData) {
        ArrayList<DependencyCSVData> data = new ArrayList<>();
        for(List<String> row: csvData){
            if(row.size()!=4) {
//                System.out.println("Problem with the size of the input of Dependency CSV File");
                continue;
            }
            DependencyCSVData dependencyCSVData = new DependencyCSVData();
            dependencyCSVData.setFile1(row.get(0));
            dependencyCSVData.setFunction1(row.get(1));
            dependencyCSVData.setFile2(row.get(2));
            dependencyCSVData.setFunction2(row.get(3));
            data.add(dependencyCSVData);
        }
        return data;
    }
}
