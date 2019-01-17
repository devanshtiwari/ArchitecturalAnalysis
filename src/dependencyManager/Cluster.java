package dependencyManager;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.HashSet;

public class Cluster {
    HashSet<String> nodes= new HashSet<>();
    HashSet <DefaultWeightedEdge> edges = new HashSet<>();

    public double getNodes() {
        return (double) nodes.size();
    }

    public double getEdges() {
        return (double) edges.size();
    }

    Cluster addNode(String n){
        this.nodes.add(n);
        return this;
    }
    Cluster addEdge(DefaultWeightedEdge e){
        edges.add(e);
        return this;
    }
}
