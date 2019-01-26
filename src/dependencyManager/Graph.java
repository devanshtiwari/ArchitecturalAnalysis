package dependencyManager;


import metrics.DependencyCSVData;
import guru.nidi.graphviz.attribute.Color;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import org.jgrapht.graph.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static guru.nidi.graphviz.model.Factory.*;
import static java.lang.Boolean.TRUE;

public class Graph {

    private DirectedPseudograph<String, DefaultEdge> tempfGraph = new DirectedPseudograph<>(DefaultEdge.class);

    public HashMap<String, Cluster> getClusterHashMap() {
        return clusterHashMap;
    }

    private HashMap<String, Cluster> clusterHashMap = new HashMap();

    public HashMap<String, Double> Ce = new HashMap<>();
    public HashMap<String, Double> Ca = new HashMap<>();

//    double Ce = 0;
//    double Ca = 0;

    private String projectName;

    private DirectedWeightedPseudograph<String, DefaultWeightedEdge> functionGraph = new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class) {
        @Override
        public DefaultWeightedEdge addEdge(String sourceVertex, String targetVertex) {
            DefaultWeightedEdge edge = this.getEdge(sourceVertex, targetVertex);
            if (edge != null) {
                this.setEdgeWeight(edge, this.getEdgeWeight(edge) + 1);
                return edge;
            }
            return super.addEdge(sourceVertex, targetVertex);
        }
    };

    //Main File and Function
    public String mainFile = "";
    public String mainFunc = "";
    public String mainModule ="";

    //Number of Functions in File
    private Map<String, Integer> functionsInFile = new HashMap<>();

    private DirectedWeightedPseudograph<String, DefaultWeightedEdge> fileGraph = new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class) {
        @Override
        public DefaultWeightedEdge addEdge(String sourceVertex, String targetVertex) {
            DefaultWeightedEdge edge = this.getEdge(sourceVertex, targetVertex);
            if (edge != null) {
                this.setEdgeWeight(edge, this.getEdgeWeight(edge) + 1);
                return edge;
            }
            return super.addEdge(sourceVertex, targetVertex);
        }
    };

    public DirectedWeightedPseudograph<String, DefaultWeightedEdge> getDirectoryGraph() {
        return directoryGraph;
    }

    private DirectedWeightedPseudograph<String, DefaultWeightedEdge> directoryGraph = new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class) {
        @Override
        public DefaultWeightedEdge addEdge(String sourceVertex, String targetVertex) {
            DefaultWeightedEdge edge = this.getEdge(sourceVertex, targetVertex);
            if (edge != null) {
                this.setEdgeWeight(edge, this.getEdgeWeight(edge) + 1);
                return edge;
            }
            return super.addEdge(sourceVertex, targetVertex);
        }
    };




    public String getProjectName() {
        return projectName;
    }

    public Map<String, Integer> getFunctionsInFile() {
        return functionsInFile;
    }

    public DirectedWeightedPseudograph<String, DefaultWeightedEdge> getFileGraph() {
        return fileGraph;
    }

    public DirectedWeightedPseudograph<String, DefaultWeightedEdge> getFunctionGraph() {
        return functionGraph;
    }

    public Graph( String projectName) {
        this.projectName = projectName;
    }



    public void setDependencyCSVData(List<DependencyCSVData> dependencyCsvDataList, Boolean generateGraph){
        mapToGraph(dependencyCsvDataList);
        if (generateGraph == TRUE)
            generateGraph();
    }
    public void setFunctionCSVData(){

    }
    public void setFileInfoCSVData(){

    }
    public void setHeaderIncludeInfoCSVData(){

    }


    private void mapToGraph(List<DependencyCSVData> dependencyCsvDataList) {


        Set<String> functionsSet = new HashSet<>();
        Set<String> callerSet = new HashSet<>();
        Set<String> calleeSet = new HashSet<>();

        for (DependencyCSVData dependencyCsvData : dependencyCsvDataList) {
            String fileV1 = dependencyCsvData.getFile1();
            String fileV2 = dependencyCsvData.getFile2();

            if(fileV1.contains("example") || fileV1.contains("test") || fileV1.contains("demo"))
                continue;
            String funcV1 = dependencyCsvData.getFile1() + "/" + dependencyCsvData.getFunction1();
            String funcV2 = dependencyCsvData.getFile2() + "/" + dependencyCsvData.getFunction2();

            callerSet.add(fileV1);
            calleeSet.add(fileV2);

            if (dependencyCsvData.getFile1().endsWith("main.c") && dependencyCsvData.getFunction1().equals("main")) {
                if (!mainFunc.equals(funcV1))
                    System.out.println("Main Encountered: " + funcV1);
                mainFunc = funcV1;
                mainFile = dependencyCsvData.getFile1();
                if (dependencyCsvData.getFile1().lastIndexOf("/")>=0)
                    mainModule = dependencyCsvData.getFile1().substring(0, dependencyCsvData.getFile1().lastIndexOf("/"));
                else
                    mainModule = "Root/" + projectName;
                } else if (dependencyCsvData.getFile2().endsWith("main.c") && dependencyCsvData.getFunction1().endsWith("main")) {
                if (!mainFunc.equals(funcV1))
                    System.out.println("Main Encountered: " + funcV1);
                mainFunc = funcV2;
                mainFile = dependencyCsvData.getFile2();
                if (dependencyCsvData.getFile2().lastIndexOf("/")>=0)
                    mainModule = dependencyCsvData.getFile2().substring(0, dependencyCsvData.getFile2().lastIndexOf("/"));
                else
                    mainModule = "Root/" + projectName;
            } else if (mainFile.equals("") && dependencyCsvData.getFunction1().equals("main")) {
                if (!mainFunc.equals(funcV1))
                    System.out.println("Main Encountered: " + funcV1);
                mainFunc = funcV1;
                mainFile = dependencyCsvData.getFile1();
                if (dependencyCsvData.getFile1().lastIndexOf("/")>=0)
                    mainModule = dependencyCsvData.getFile1().substring(0, dependencyCsvData.getFile1().lastIndexOf("/"));
                else
                    mainModule = "Root/" + projectName;
            } else if (mainFile.equals("") && dependencyCsvData.getFunction1().endsWith("main")) {
                if (!mainFunc.equals(funcV1))
                    System.out.println("Main Encountered: " + funcV1);
                mainFunc = funcV1;
                mainFile = dependencyCsvData.getFile1();
                if (dependencyCsvData.getFile1().lastIndexOf("/")>=0)
                    mainModule = dependencyCsvData.getFile1().substring(0, dependencyCsvData.getFile1().lastIndexOf("/"));
                else
                    mainModule = "Root/" + projectName;

            }

            if (!functionsSet.contains(funcV1)) {
                if (functionsInFile.containsKey(fileV1))
                    functionsInFile.put(fileV1, functionsInFile.get(fileV1) + 1);
                else
                    functionsInFile.put(fileV1, 1);
            }
            functionsSet.add(funcV1);
            if (!functionsSet.contains(funcV2)) {
                if (functionsInFile.containsKey(fileV2))
                    functionsInFile.put(fileV2, functionsInFile.get(fileV2) + 1);
                else
                    functionsInFile.put(fileV2, 1);
            }
            functionsSet.add(funcV2);

            //Part where it skips adding loops in functions.
//            if(funcV1.equals(funcV2))
//                continue;


            tempfGraph.addVertex(funcV1);
            tempfGraph.addVertex(funcV2);

            functionGraph.addVertex(funcV1);
            functionGraph.addVertex(funcV2);


            tempfGraph.addEdge(funcV1, funcV2);

            functionGraph.addEdge(funcV1, funcV2);

            fileGraph.addVertex(fileV1);
            fileGraph.addVertex(fileV2);

//            tempfGraph.addVertex(fileV1);

//            tempfGraph.addVertex(fileV2);
//            System.out.println("fileV1"+ fileV1 + " fileV2" + fileV2);

            //Part where it skips adding loop to file
            if (fileV1.equals(fileV2))
                continue;

//            tempfGraph.addEdge(fileV1,fileV2);

            DefaultWeightedEdge edge = fileGraph.addEdge(fileV1, fileV2);

            String dir1 = fileV1;
            String dir2 = fileV2;
            if (fileV1.lastIndexOf("/")>=0)
                dir1 = fileV1.substring(0,fileV1.lastIndexOf("/"));
            else
                dir1 = "Root/" + projectName;
            if(fileV2.lastIndexOf("/")>=0)
                dir2 = fileV2.substring(0,fileV2.lastIndexOf("/"));
            else
                dir2 = "Root/" + projectName;

            directoryGraph.addVertex(dir1);
            directoryGraph.addVertex(dir2);
            directoryGraph.addEdge(dir1,dir2);

            if(clusterHashMap.containsKey(dir1)) {
                clusterHashMap.put(dir1, clusterHashMap.get(dir1).addNode(fileV1));
                clusterHashMap.put(dir1, clusterHashMap.get(dir1).addNode(fileV2));
                if (dir1.equals(dir2)) {
                    clusterHashMap.put(dir1, clusterHashMap.get(dir1).addEdge(edge));
                }//
            }else{
                Cluster newCluster = new Cluster();
                clusterHashMap.put(dir1, newCluster.addNode(fileV1));
                clusterHashMap.put(dir1, newCluster.addNode(fileV2));
                if (dir1.equals(dir2)) {
                    clusterHashMap.put(dir1, newCluster.addEdge(edge));
                }
            }

            Ce.putIfAbsent(dir1, 0D);
            Ca.putIfAbsent(dir2, 0D);

            //Just to keep values even if zero
            Ce.putIfAbsent(dir2, 0D);
            Ca.putIfAbsent(dir1, 0D);

            if(!dir1.equals(dir2)) {
                Ce.put(dir1, Ce.get(dir1) + 1);
                Ca.put(dir2, Ca.get(dir2) + 1);
            }
        }

        //todo
        callerSet.removeAll(calleeSet);
        if (mainFile.equals("")) {
            if (!callerSet.isEmpty()) {
                String f = callerSet.toArray()[0].toString();
                for(String file : calleeSet){
//                    if(f.length()>file.length())
//                        f = file;
                    if(file.contains("main"))
                        f = file;
                }
                System.out.println("Size of possible mainFile Options: " + calleeSet.size());
                mainFile = f;
                System.out.println(mainFile);
            }
        }
        System.out.println("Main File: " + mainFile);
    }

    public void generateGraph() {
        MutableGraph g = mutGraph("Dependency Graph").setDirected(true).use((gr, ctx) -> {
            for (DefaultWeightedEdge e : functionGraph.edgeSet()) {
                String[] splits = e.toString().substring(1, e.toString().length() - 1).split(":");
                linkAttrs().add(Style.BOLD, Label.of(Double.toString(functionGraph.getEdgeWeight(e))), Color.RED);
                nodeAttrs().add(Color.RED);
                mutNode(splits[0].trim()).addLink(mutNode(splits[1].trim()));
            }
        });

        try {
            Graphviz.fromGraph(g).width(10000).scale(2).render(Format.PNG).toFile(new File(projectName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void generateGraph2() {
        MutableGraph g = mutGraph("Dependency Graph").setDirected(true).use((gr, ctx) -> {
            for (DefaultWeightedEdge e : directoryGraph.edgeSet()) {
                String[] splits = e.toString().substring(1, e.toString().length() - 1).split(":");
                linkAttrs().add(Style.BOLD, Label.of(Double.toString(functionGraph.getEdgeWeight(e))), Color.RED);
                nodeAttrs().add(Color.RED);
                mutNode(splits[0].trim()).addLink(mutNode(splits[1].trim()));
            }
            for(String v:  directoryGraph.vertexSet()){
                mutNode(v);
            }
        });

        try {
            Graphviz.fromGraph(g).width(10000).scale(1).render(Format.PNG).toFile(new File(projectName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

