package metrics;

import dependencyManager.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MetricsInputContent {

    private String projectName;
    private Graph graph = new Graph("");
    private ArrayList<HeaderIncludedCSVInfo> headerIncludedCSVInfoArrayList;
    private HashMap<String, FunctionCSVInfo> functionCSVInfoArrayList;
    private ArrayList<FileHeaderCSVInfo> fileHeaderCSVInfoArrayList;
    private HashMap<String, HashSet<String>> filetoFunctionMap;
    private HashMap<String, HashSet<String>> moduletoFileMap;

    public String getProjectName() {
        return projectName;
    }

    public Graph getGraph() {
        return graph;
    }

    public ArrayList<HeaderIncludedCSVInfo> getHeaderIncludedCSVInfoArrayList() {
        return headerIncludedCSVInfoArrayList;
    }

    public HashMap<String, FunctionCSVInfo> getFunctionCSVInfoArrayList() {
        return functionCSVInfoArrayList;
    }

    public ArrayList<FileHeaderCSVInfo> getFileHeaderCSVInfoArrayList() {
        return fileHeaderCSVInfoArrayList;
    }

    public HashMap<String, HashSet<String>> getFiletoFunctionMap() {
        return filetoFunctionMap;
    }

    public HashMap<String, HashSet<String>> getModuletoFileMap() {
        return moduletoFileMap;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setHeaderIncludedCSVInfoArrayList(ArrayList<HeaderIncludedCSVInfo> headerIncludedCSVInfoArrayList) {
        this.headerIncludedCSVInfoArrayList = headerIncludedCSVInfoArrayList;
    }

    public void setFunctionCSVInfoArrayList(HashMap<String, FunctionCSVInfo> functionCSVInfoArrayList) {
        this.functionCSVInfoArrayList = functionCSVInfoArrayList;
    }

    public void setFileHeaderCSVInfoArrayList(ArrayList<FileHeaderCSVInfo> fileHeaderCSVInfoArrayList) {
        this.fileHeaderCSVInfoArrayList = fileHeaderCSVInfoArrayList;
    }

    public void setFiletoFunctionMap(HashMap<String, HashSet<String>> filetoFunctionMap) {
        this.filetoFunctionMap = filetoFunctionMap;
    }

    public void setModuletoFileMap(HashMap<String, HashSet<String>> moduletoFileMap) {
        this.moduletoFileMap = moduletoFileMap;
    }
}
