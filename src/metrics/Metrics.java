package metrics;

import org.jgrapht.graph.*;
import org.jgrapht.io.*;

import java.io.OutputStream;
import java.util.*;

//Metrics only which are involved in any method calling in the Project

/**
 * Metrics class keeps a set of static methods which calculate some value given the graph input. It is mostly called by {@link MetricsContainer}.
 * @author Devansh Tiwari
 */

public class Metrics {


    /**
     * inoutDegree calculates the indegree and outgegree of of the given node in the graph.
     * @param g Graph g is Directed weighted graph.
     * @param v Vertex v is the node for the in-degree and out-degree is to be calculated.
     * @return Returns an array of size 2. First one is in-degree and second is out-degree.
     */
    public static double[] inoutDegree(DirectedWeightedPseudograph<String, DefaultWeightedEdge> g, String v) {
        double[] inout = {0.0, 0.0};
        for (DefaultWeightedEdge e : g.incomingEdgesOf(v))
            inout[0] += g.getEdgeWeight(e);
        for (DefaultWeightedEdge e : g.outgoingEdgesOf(v))
            inout[1] += g.getEdgeWeight(e);
        return inout;
    }

    /**
     *
     * @param g
     * @return
     */
    public static double cuttingNumber(DirectedWeightedPseudograph g) {
        double cnum = 0;
        double e = g.edgeSet().size(), n = g.vertexSet().size();
        if (e > 7 * n) {
            cnum = e * e * e / (29 * n * n);
        } else if (e > 4 * n) {
//            System.out.println("Exception for " + graph.getProjectName());
            cnum = e * e * e / (64 * n * n);
        } else {
            return -1;
        }
        return cnum;
    }

    public static LinkedHashMap<String, double[]> matrix(DirectedWeightedPseudograph<String, DefaultWeightedEdge> receivedGraph) throws Exception {

        OutputStream output = new OutputStream() {
            private StringBuilder string = new StringBuilder();

            @Override
            public void write(int b) {
                this.string.append((char) b);
            }

            //Netbeans IDE automatically overrides this toString()
            public String toString() {
                return this.string.toString();
            }
        };

        CSVExporter csvExporter = new CSVExporter<String, DefaultWeightedEdge>();
        csvExporter.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_ZERO_WHEN_NO_EDGE, true);
        csvExporter.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_NODEID, true);
        csvExporter.setFormat(CSVFormat.MATRIX);

        LinkedHashMap<String, double[]> linkedHashMap = new LinkedHashMap<>();
        LinkedHashMap<String, TreeMap> matrixx = new LinkedHashMap<>();

//        ComponentNameProvider<String> componentNameProvider = new ComponentNameProvider<String>() {
//            @Override
//            public String getName(String s) {
//                return s;
//            }
//        };
        //Replacement for the above definition as lambda
        ComponentNameProvider<String> componentNameProvider = s -> s;
        csvExporter.setVertexIDProvider(componentNameProvider);
        csvExporter.exportGraph(receivedGraph, output);

        Scanner scanner = new Scanner(output.toString());

        String[] v = scanner.nextLine().split(",", -1);
        int l = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] splits = line.split(",", -1);
            String vertex = splits[0];
            double row[] = new double[splits.length - 1];
            for (int i = 1; i < splits.length; i++) {
                row[i - 1] = Integer.parseInt(splits[i]);
            }
            if (!vertex.equals(v[++l]))
                throw new Exception("Should not happen");
            linkedHashMap.put(vertex, row);
        }

        if (linkedHashMap.size() == 0)
            return null;
        return linkedHashMap;
    }
}
