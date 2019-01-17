package dependencyManager;


import fileIO.CSVData;
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

    HashMap<String, Cluster> clusterHashMap = new HashMap();

    public HashMap<String, Double> Ce = new HashMap<>();
    public HashMap<String, Double> Ca = new HashMap<>();

//    double Ce = 0;
//    double Ca = 0;

    private String projectName;

    DirectedWeightedPseudograph<String, DefaultWeightedEdge> functionGraph = new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class) {
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

    //Number of Functions in File
    private Map<String, Integer> functionsInFile = new HashMap<>();

    DirectedWeightedPseudograph<String, DefaultWeightedEdge> fileGraph = new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class) {
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

    DirectedWeightedPseudograph<String, DefaultWeightedEdge> directoryGraph = new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class) {
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

    public Graph(List<CSVData> csvDataList, String projectName, Boolean generateGraph) {
        this.projectName = projectName;
        mapToGraph(csvDataList);
        if (generateGraph == TRUE)
            generateGraph();
    }

    private void mapToGraph(List<CSVData> csvDataList) {


        Set<String> functionsSet = new HashSet<>();
        Set<String> callerSet = new HashSet<>();
        Set<String> calleeSet = new HashSet<>();

        for (CSVData csvData : csvDataList) {
            String fileV1 = csvData.getFile1();
            String fileV2 = csvData.getFile2();

            if(fileV1.contains("example") || fileV1.contains("test") || fileV1.contains("demo"))
                continue;
            String funcV1 = csvData.getFile1() + "/" + csvData.getFunction1();
            String funcV2 = csvData.getFile2() + "/" + csvData.getFunction2();

            callerSet.add(fileV1);
            calleeSet.add(fileV2);

            if (csvData.getFile1().endsWith("main.c") && csvData.getFunction1().equals("main")) {
                if (!mainFunc.equals(funcV1))
                    System.out.println("Main Encountered: " + funcV1);
                mainFunc = funcV1;
                mainFile = csvData.getFile1();
            } else if (csvData.getFile2().endsWith("main.c") && csvData.getFunction1().endsWith("main")) {
                if (!mainFunc.equals(funcV1))
                    System.out.println("Main Encountered: " + funcV1);
                mainFunc = funcV2;
                mainFile = csvData.getFile2();
            } else if (mainFile.equals("") && csvData.getFunction1().equals("main")) {
                if (!mainFunc.equals(funcV1))
                    System.out.println("Main Encountered: " + funcV1);
                mainFunc = funcV1;
                mainFile = csvData.getFile1();
            } else if (mainFile.equals("") && csvData.getFunction1().endsWith("main")) {
                if (!mainFunc.equals(funcV1))
                    System.out.println("Main Encountered: " + funcV1);
                mainFunc = funcV1;
                mainFile = csvData.getFile1();
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

