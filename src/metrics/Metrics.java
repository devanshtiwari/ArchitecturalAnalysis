package metrics;

import dependencyManager.Cluster;
import dependencyManager.Graph;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.BiconnectivityInspector;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.graph.*;
import org.jgrapht.io.*;

import java.io.OutputStream;
import java.util.*;

//Metrics only which are involved in any method calling in the Project

public class Metrics {

    private Graph graph;
    private HashSet<DefaultWeightedEdge> cycleAllEdges = new HashSet<>();
    private HashSet<DefaultWeightedEdge> cycleMinEdges = new HashSet<>();

    public static DirectedWeightedPseudograph<String, DefaultWeightedEdge> sfilegraph;

    public Metrics(Graph graph) {
        this.graph = graph;
        ArrayList pathSoFar = new ArrayList<String>();
        if(graph.mainFile.isEmpty())
            graph.mainFile = graph.getFileGraph().vertexSet().iterator().next();
        pathSoFar.add(graph.mainFile);
        System.out.println("Main Function: " + graph.mainFunc);
        filegraph = (DirectedWeightedPseudograph<String, DefaultWeightedEdge>) graph.getFileGraph().clone();
        findPathWithCycles(graph.mainFile, pathSoFar);
    }

    public int numberOfFiles() {
        int files = graph.getFileGraph().vertexSet().size();
        return files;
    }

    public int numberOfFunctions() {
        int functions = graph.getFunctionGraph().vertexSet().size();
        return functions;
    }

    public int maxFunctions() {
        int count = 0;
        for (String key : graph.getFunctionsInFile().keySet()) {
            if (count < graph.getFunctionsInFile().get(key))
                count = graph.getFunctionsInFile().get(key);
        }
        return count;
    }

    public double averageFileFunctions() {
        double avg = 0;
        avg = (double) numberOfFunctions() / numberOfFiles();
        return avg;
    }

    public double sizeDeviation() {
        double deviation = 0;
        double mu = averageFileFunctions(), D = 0;
        Map<String, Integer> fileSize = graph.getFunctionsInFile();

        for (String key : fileSize.keySet()) {
            D += Math.abs(fileSize.get(key) - mu);
        }

        deviation = 1.0 - D / (mu * numberOfFunctions());
        System.out.println("File Size Deviation: " + deviation);
        return deviation;
    }

    public double callComplexity() {

        double complexity;
        double numerator = 0;
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> fileG = graph.getFileGraph();

        for (String v : fileG.vertexSet()) {
            double[] inout = inoutDegree(fileG, v);
            numerator += inout[0] * inout[1];
        }
        complexity = numerator / ((numberOfFiles() - 1) * (numberOfFiles() - 1));
        System.out.println("Call Complexity: " + complexity);
        return complexity;
    }

    static double[] inoutDegree(DirectedWeightedPseudograph<String, DefaultWeightedEdge> g, String v) {
        double[] inout = {0.0, 0.0};
        for (DefaultWeightedEdge e : g.incomingEdgesOf(v))
            inout[0] += g.getEdgeWeight(e);
        for (DefaultWeightedEdge e : g.outgoingEdgesOf(v))
            inout[1] += g.getEdgeWeight(e);
        return inout;
    }

    public double[] inoutDependency() {
        double dep[] = {0.0, 0.0};
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> fileG = graph.getFileGraph();
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> funcG = graph.getFunctionGraph();
        double[] filesumInOut = {0.0, 0.0}, funcsumInOut = {0.0, 0.0};

        for (String v : fileG.vertexSet()) {
            double[] inout = inoutDegree(fileG, v);
            //Squaring each indegree and outdegree
//            System.out.println("Indegree: " + inout[0]);

            filesumInOut[0] += inout[0] * inout[0];
            filesumInOut[1] += inout[1] * inout[1];
            //Experimental Temporary
//            filesumInOut[0] += inout[0];
//            filesumInOut[1] += inout[1];

        }
        for (String v : funcG.vertexSet()) {
            double[] inout = inoutDegree(funcG, v);
//            System.out.println("Indegree : " + inout[0]);
            funcsumInOut[0] += inout[0] * inout[0];
            funcsumInOut[1] += inout[1] * inout[1];

            //Experiment Another
//            funcsumInOut[0] += funcG.inDegreeOf(v) * funcG.inDegreeOf(v);
//            funcsumInOut[1] += funcG.outDegreeOf(v) * funcG.outDegreeOf(v);

            //Expiermental Temporary
//            funcsumInOut[0] += inout[0];
//            funcsumInOut[1] += inout[1];
        }
//        if(filesumInOut[0]>funcsumInOut[0])
//            System.out.println("File In: " + filesumInOut[0] + "Function In: " + funcsumInOut[0]);
//        if(filesumInOut[1]>funcsumInOut[1])
//            System.out.println("File Out: " + filesumInOut[1] + "Function Out: " + funcsumInOut[1]);

        dep[0] = 1.0 - (numberOfFiles() * filesumInOut[0]) / (numberOfFunctions() * funcsumInOut[0]);
        dep[1] = 1.0 - (numberOfFiles() * filesumInOut[1]) / (numberOfFunctions() * funcsumInOut[1]);

        System.out.println("Incoming concentration: " + dep[0]);
        System.out.println("Outgoing Concentration: " + dep[1]);
        return dep;
    }

    DirectedWeightedPseudograph<String, DefaultWeightedEdge> filegraph;

    void findPathWithCycles(String source, ArrayList<String> pathSoFar) {

        //Some problem with the csv
        if (graph.getProjectName().equals("ImageMagick"))
            return;

        for (String node : Graphs.successorListOf(filegraph, source)) {

            if (pathSoFar.contains(node)) {
                Iterator<String> pathSoFarIterator = pathSoFar.iterator();
                String n1 = "";
                if (pathSoFarIterator.hasNext())
                    n1 = pathSoFarIterator.next();
                while (pathSoFarIterator.hasNext()) {
                    if (pathSoFarIterator.hasNext()) {
                        String n2 = pathSoFarIterator.next();
                        cycleAllEdges.add(filegraph.getEdge(n1, n2));
                        n1 = n2;
                    }
                }
                cycleAllEdges.add(filegraph.getEdge(source, node));
                cycleMinEdges.add(filegraph.getEdge(source, node));
                //Removing edge attempt
                filegraph.removeEdge(filegraph.getEdge(source, node));
            } else {
                ArrayList<String> newPath = new ArrayList<>(pathSoFar);
                newPath.add(node);
                findPathWithCycles(node, newPath);
            }

        }
        pathSoFar.clear();
    }


    public double normalizedCycleEdges() {

        double nce = 0;
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> funcG = graph.getFunctionGraph();
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> fileG = graph.getFileGraph();

        double numerator = 0;
        for (DefaultWeightedEdge e : cycleMinEdges) {
            numerator += fileG.getEdgeWeight(e);
        }
        double edgesetsize = 0;

        for (DefaultWeightedEdge e : funcG.edgeSet()) {
            edgesetsize += funcG.getEdgeWeight(e);
        }

        nce = 1.0 - numerator / (double) edgesetsize;
        System.out.println("NCE: " + nce);
        return nce;
    }

    public double normalizedCyclePaths() {

        double ndp = 0;
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> funcG = graph.getFunctionGraph();
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> fileG = graph.getFileGraph();

        double numerator = 0;
        for (DefaultWeightedEdge e : cycleAllEdges) {
            numerator += fileG.getEdgeWeight(e);
        }
        double edgesetsize = 0;

        for (DefaultWeightedEdge e : funcG.edgeSet()) {
            edgesetsize += funcG.getEdgeWeight(e);
        }

        ndp = 1.0 - numerator / edgesetsize;
        System.out.println("NDP: " + ndp);

        return ndp;
    }

    public double weightedDependencyCost() {
        double wdc = 0;
        //Skipping right now
        return wdc;
    }


    public double gdependency() {
        double d;
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> fileG = graph.getFileGraph();
        double w = 0;
        for (DefaultWeightedEdge e : fileG.edgeSet()) {
            w += fileG.getEdgeWeight(e);
        }
        d = w / (fileG.vertexSet().size() * fileG.vertexSet().size());
        System.out.println("Dependency: " + d);
        return d;
    }

    public double weightedDependencyCostSQ() {
        double wdc = 0;
        return wdc;
    }

    public double cyclomaticComplexity() {
        double cycl = 0;
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> fileG = graph.getFileGraph();
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> funcG = graph.getFunctionGraph();

        double totalweight = 0;
        for (DefaultWeightedEdge e : funcG.edgeSet()) {
            totalweight += funcG.getEdgeWeight(e);
        }
        cycl = 1.0 - (double) (fileG.vertexSet().size()-2)/(fileG.edgeSet().size());
        cycl = 1.0 - (double) (funcG.vertexSet().size() - 2) / totalweight;
        System.out.println("Cyclomatic Complexity: " + cycl);
        return cycl;
    }

    public class MapUtil {
        public <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
            List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
            list.sort(Map.Entry.comparingByValue());

            Map<K, V> result = new LinkedHashMap<>();
            for (Map.Entry<K, V> entry : list) {
                result.put(entry.getKey(), entry.getValue());
            }
            return result;
        }
    }

    public void stronglyConnected(){
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> fileG = graph.getFileGraph();
        LinkedHashMap<String, Integer> inDegrees = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> outDegrees = new LinkedHashMap<>();
        for(String v : fileG.vertexSet()){
//            double[] inout = inoutDegree(fileG,v);
            inDegrees.put(v,fileG.inDegreeOf(v));
            outDegrees.put(v, fileG.outDegreeOf(v));
        }
        MapUtil mapUtil = new MapUtil();
        LinkedHashMap<String, Integer> insorted =(LinkedHashMap<String, Integer>) mapUtil.sortByValue(inDegrees);
        LinkedHashMap<String, Integer> outsorted =(LinkedHashMap<String, Integer>) mapUtil.sortByValue(outDegrees);
        List<String> reverseOrderedKeysIn = new ArrayList<String>(insorted.keySet());
        List<String> reverseOrderedKeysOut = new ArrayList<String>(outsorted.keySet());
        Collections.reverse(reverseOrderedKeysIn);
        Collections.reverse(reverseOrderedKeysOut);
        System.out.println("Indegrees");
        int totalEdges = fileG.edgeSet().size();
        for (String key : reverseOrderedKeysIn) {
            System.out.print(key + ": " + (double)insorted.get(key)/totalEdges +"; ");
        }
        System.out.println("\nOutdegrees");
        for (String key : reverseOrderedKeysOut) {
            System.out.print(key + ": " + (double) outsorted.get(key)/totalEdges +"; ");
        }

        KosarajuStrongConnectivityInspector<String, DefaultWeightedEdge> kosarajuStrongConnectivityInspector = new KosarajuStrongConnectivityInspector<>(graph.getFileGraph());
        List<Set<String>> sets = kosarajuStrongConnectivityInspector.stronglyConnectedSets();
        for(Set<String> st : sets){
            if(st.size()>1)
                System.out.println(st);
        }
    }

    public static double cuttingNumber(DirectedWeightedPseudograph g){
        double cnum = 0;
        double e = g.edgeSet().size(), n = g.vertexSet().size();
        if(e>7*n){
            cnum = e*e*e/(29*n*n);
        }
        else if(e>4*n)
        {
//            System.out.println("Exception for " + graph.getProjectName());
            cnum = e*e*e/(64*n*n);
        }
        else
        {
            return -1;
        }
        return cnum;
    }

    public double density(DirectedWeightedPseudograph g){
        double density = 0;
        density = (double)g.edgeSet().size()/(double)(g.vertexSet().size()*(g.vertexSet().size()-1));
        return density;
    }

    public double fileCuttingNumber(){
        return cuttingNumber(graph.getFileGraph());
    }

    public double functionCuttingNumber(){
        return cuttingNumber(graph.getFunctionGraph());
    }

    public double fileDensity(){
        return density(graph.getFileGraph());
    }

    public double functionDensity(){
        return density(graph.getFunctionGraph());
    }

    public static LinkedHashMap<String, double[]> matrix(DirectedWeightedPseudograph<String, DefaultWeightedEdge> receivedGraph) throws ExportException {

        OutputStream output = new OutputStream()
        {
            private StringBuilder string = new StringBuilder();
            @Override
            public void write(int b) {
                this.string.append((char) b );
            }

            //Netbeans IDE automatically overrides this toString()
            public String toString(){
                return this.string.toString();
            }
        };


        CSVExporter csvExporter = new CSVExporter<String, DefaultWeightedEdge>();
        csvExporter.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_ZERO_WHEN_NO_EDGE,true);
        csvExporter.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_NODEID,true);
        csvExporter.setFormat(CSVFormat.MATRIX);

        LinkedHashMap<String, double[]> linkedHashMap = new LinkedHashMap<>();
        LinkedHashMap<String, TreeMap> matrixx = new LinkedHashMap<>();

        ComponentNameProvider<String> componentNameProvider = new ComponentNameProvider<String>() {
            @Override
            public String getName(String s) {
                return s;
            }
        };
        csvExporter.setVertexIDProvider(componentNameProvider);
        csvExporter.exportGraph(receivedGraph, output);

        Scanner scanner = new Scanner(output.toString());

        String []v = scanner.nextLine().split(",",-1);
        int l = 0;
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] splits = line.split(",", -1);
            String vertex = splits[0];
            double row[] = new double[splits.length - 1];
            for (int i = 1; i < splits.length; i++) {
                row[i - 1] = Integer.parseInt(splits[i]);
            }
            if(!vertex.equals(v[++l]))
                System.out.println("ALARM! ALARM! ALARM! ALARM!");
            linkedHashMap.put(vertex, row);
        }

        if(linkedHashMap.size()==0)
            return null;
        return linkedHashMap;

    }


    public void connectedSets(){
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> g = graph.getFileGraph();
        BiconnectivityInspector con = new BiconnectivityInspector(g);
        System.out.println("\nConnected Sets: " + con.getConnectedComponents());
        Iterator itr = con.getConnectedComponents().iterator();
        int countSingletons = 0;
        while(itr.hasNext()){
            if(((AsSubgraph) itr.next()).vertexSet().size() == 1)
                countSingletons ++;
        }
        System.out.println("Singletons: " + countSingletons);
        System.out.println("Rest: " + (con.getConnectedComponents().size()-countSingletons));
    }


    public double QValue(){
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> g = graph.getDirectoryGraph();
        double Qval = 0;
        double n = 0;
        for(DefaultWeightedEdge e:graph.getFileGraph().edgeSet()){
            n += 2*graph.getFileGraph().getEdgeWeight(e);
        }
        for(String v:g.vertexSet()){
            double ai = 0, eii=0;
            if(g.getEdge(v,v) !=null)
                eii = 2* g.getEdgeWeight(g.getEdge(v,v));
            double[] inoutDegree  = inoutDegree(g,v);
            ai = inoutDegree[1] +  (eii/2);
//            System.out.println("eii : " + eii);
//            System.out.println("n: " + n);
//            System.out.println("ai: " + ai );
            Qval += (eii/n - (ai/n)*(ai/n));
        }
        System.out.println("QValue: " + Qval);
        return Qval;
    }

    public double avgClusterDensity(){
        HashMap<String, Cluster> clusterMap = graph.getClusterHashMap();
        double avg = 0;
        double denominator = 0;
        for(String str:clusterMap.keySet()){
            double dens = clusterMap.get(str).getEdges()/((clusterMap.get(str).getNodes()+1.0)*(clusterMap.get(str).getNodes()));
            System.out.println(str + ": " + dens);
            avg += dens * clusterMap.get(str).getNodes();
            denominator += clusterMap.get(str).getNodes();
        }
//        denominator /= (double) clusterMap.size();
        avg /=  denominator;
        System.out.println("Graph Cluster wise Average: " + avg);
        return avg;
    }

    public double instability(){
        Double instability = 0D;

        //Library Detection
        System.out.println("CA AND CE: " + graph.Ca + "and " + graph.Ce);
        for(String cluster: graph.Ce.keySet()){
            Double temp = instability;
            if(graph.Ca.get(cluster) + graph.Ce.get(cluster)==0)
                instability += 0;
            else
                instability += graph.Ce.get(cluster)/(graph.Ca.get(cluster) + graph.Ce.get(cluster));
            System.out.println(cluster + " : " + (instability-temp));
        }
        System.out.println("Average Instability: " + instability/graph.Ca.size());
        return instability;
    }

}
