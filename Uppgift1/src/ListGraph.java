import java.io.Serializable;
import java.util.*;

public class ListGraph<T> implements Serializable{
    private List<T> nodes; 
    @SuppressWarnings("rawtypes")
    private List<List<Edge>> adjacencyList;

    ListGraph(){
        nodes = new ArrayList<>();
        adjacencyList = new ArrayList<>();
    }

    @SuppressWarnings("rawtypes")
    public void add(T node){
        if(!nodes.contains(node))
            nodes.add(node);
        adjacencyList.add(new ArrayList<Edge>());
    }

    //inte klar
    @SuppressWarnings("rawtypes")
    public void connect(T node1, T node2, String name, int weight){
        try{
            Edge edge1 = new Edge<T>(weight, name, node2);
            Edge edge2 = new Edge<T>(weight, name, node1);
            adjacencyList.get(nodes.indexOf(node1)).add(edge1);
            adjacencyList.get(nodes.indexOf(node2)).add(edge2);
        } catch(IllegalArgumentException e){
            e.getStackTrace();
        }
    }

    @SuppressWarnings("rawtypes")
    public void disconnect(T node1, T node2) {
        int index1 = nodes.indexOf(node1);
        int index2 = nodes.indexOf(node2);
    
        // Check if either node does not exist in the graph
        if (index1 == -1 || index2 == -1) {
            throw new NoSuchElementException("One or both of the specified nodes do not exist in the graph.");
        }
    
        // Get the edge from node1 to node2
        Edge edgeFromNode1ToNode2 = null;
        List<Edge> edgesFromNode1 = adjacencyList.get(index1);
        for (Edge edge : edgesFromNode1) {
            if (edge.getDestination().equals(node2)) {
                edgeFromNode1ToNode2 = edge;
                break;
            }
        }
    
        // Get the edge from node2 to node1
        Edge edgeFromNode2ToNode1 = null;
        List<Edge> edgesFromNode2 = adjacencyList.get(index2);
        for (Edge edge : edgesFromNode2) {
            if (edge.getDestination().equals(node1)) {
                edgeFromNode2ToNode1 = edge;
                break;
            }
        }
    
        // Check if either edge does not exist
        if (edgeFromNode1ToNode2 == null || edgeFromNode2ToNode1 == null) {
            throw new IllegalStateException("No edge exists between the specified nodes.");
        }
    
        // Remove the edges from both directions
        edgesFromNode1.remove(edgeFromNode1ToNode2);
        edgesFromNode2.remove(edgeFromNode2ToNode1);
    }
    

    //inte klar
    @SuppressWarnings("rawtypes")
    public boolean pathExists(T from, T to){
        if(from == to)
            return true;
        if(!nodes.contains(from) || !nodes.contains(to))
            return false;
        List<Edge> list = new ArrayList<>(adjacencyList.get(nodes.indexOf(from)));

        Boolean[] isVisited = new Boolean[list.size()];
        for(Edge e : adjacencyList.get(nodes.indexOf(from))){
            System.out.println(list.indexOf(e));
            if(!isVisited[list.indexOf(e)]){
                if(pathExists(nodes.get(nodes.indexOf(e.getDestination())), to)){
                    return true;
                }
            }
        }
        return false;
    }

    public List<T> getNodes() {
        return new ArrayList<>(nodes);
    }
    
    @SuppressWarnings("rawtypes")
    public List<Edge> getEdgesFrom(T node) {
        int index = nodes.indexOf(node);
        if (index == -1) {
            throw new NoSuchElementException("Node not found in the graph.");
        }
    
        return new ArrayList<>(adjacencyList.get(index));
    }

    @SuppressWarnings("rawtypes")
    public Edge getEdgeBetween(T from, T to) {
        int index1 = nodes.indexOf(from);
        int index2 = nodes.indexOf(to);
    
        if (index1 == -1 || index2 == -1) {
            throw new NoSuchElementException("One or both of the specified nodes do not exist in the graph.");
        }
    
        List<Edge> edgesFromNode = adjacencyList.get(index1);
    
        for (Edge edge : edgesFromNode) {
            if (edge.getDestination().equals(to)) {
                return edge;
            }
        }
    
        throw new IllegalArgumentException("No edge exists between the specified nodes.");

    }

    
    

    @SuppressWarnings("rawtypes")
    public void remove(T node) {
        int index = nodes.indexOf(node);
        if(index == -1)
            throw new NoSuchElementException();

        List<Edge> edgesToRemove = adjacencyList.get(index);
        adjacencyList.remove(index);
        nodes.remove(node);

        for (List<Edge> edges : adjacencyList) {
            edges.removeIf(edge -> edge.getDestination().equals(node));
        }
    }


}