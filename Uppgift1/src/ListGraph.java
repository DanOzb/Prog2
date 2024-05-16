import java.io.Serializable;
import java.util.*;

public class ListGraph<T> implements Serializable{
    private List<T> nodes; 
    private List<List<Edge<T>>> adjacencyList;

    ListGraph(){
        nodes = new ArrayList<>();
        adjacencyList = new ArrayList<>();
    }

    public void add(T node){
        if(!nodes.contains(node))
            nodes.add(node);
        adjacencyList.add(new ArrayList<Edge<T>>());
    }

    //inte klar
    public void connect(T node1, T node2, String name, int weight){
        try{
            Edge<T> edge1 = new Edge<T>(weight, name, node2);
            Edge<T> edge2 = new Edge<T>(weight, name, node1);
            adjacencyList.get(nodes.indexOf(node1)).add(edge1);
            adjacencyList.get(nodes.indexOf(node2)).add(edge2);
        } catch(IllegalArgumentException e){
            e.getStackTrace();
        }
    }

    public void disconnect(T node1, T node2) {
        int index1 = nodes.indexOf(node1);
        int index2 = nodes.indexOf(node2);
    
        // Check if either node does not exist in the graph
        if (index1 == -1 || index2 == -1) {
            throw new NoSuchElementException("One or both of the specified nodes do not exist in the graph.");
        }
    
        // Get the edge from node1 to node2
        Edge<T> edgeFromNode1ToNode2 = null;
        List<Edge<T>> edgesFromNode1 = adjacencyList.get(index1);
        for (Edge<T> edge : edgesFromNode1) {
            if (edge.getDestination().equals(node2)) {
                edgeFromNode1ToNode2 = edge;
                break;
            }
        }
    
        // Get the edge from node2 to node1
        Edge<T> edgeFromNode2ToNode1 = null;
        List<Edge<T>> edgesFromNode2 = adjacencyList.get(index2);
        for (Edge<T> edge : edgesFromNode2) {
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
    

    //Checks path with dfs
    public boolean pathExists(T from, T to){
        if(from == to)
            return true;
        if(!nodes.contains(from) || !nodes.contains(to))
            return false;

        boolean[] isVisited = new boolean[nodes.size()];
        isVisited[nodes.indexOf(from)] = true;
        for(int i = 0; i < isVisited.length; i++){
            isVisited[i] = false;
        }
        boolean result = dfs(from, to, isVisited);
        return result;
    }

    public List<T> getNodes() {
        return new ArrayList<>(nodes);
    }
    
    public List<Edge<T>> getEdgesFrom(T node) {
        int index = nodes.indexOf(node);
        if (index == -1) {
            throw new NoSuchElementException("Node not found in the graph.");
        }
    
        return new ArrayList<>(adjacencyList.get(index));
    }

    //getEdgesBetween method | tested
    public Edge<T> getEdgeBetween(T from, T to) throws NoSuchElementException, IllegalArgumentException{
        try{
            int index1 = nodes.indexOf(from);
            int index2 = nodes.indexOf(to);
        
            if (index1 == -1 || index2 == -1) {
                throw new NoSuchElementException();
            }
        
            List<Edge<T>> edgesFromNode = adjacencyList.get(index1);
        
            for (Edge<T> edge : edgesFromNode) {
                if (edge.getDestination().equals(to)) {
                    return edge;
                }
            }
        
            throw new IllegalArgumentException();
        } catch(NoSuchElementException e) {
            System.err.println("One or both of the specified nodes do not exist in the graph.");
            e.printStackTrace();
            throw e;
        } catch(IllegalArgumentException e){
            System.err.println("No edge exists between the specified nodes.");
            e.printStackTrace();
            throw e;
        }
        

    }
    
    //Remove node method | tested
    public void remove(T node) throws NoSuchElementException{
        try{
            int index = nodes.indexOf(node);
            if(index == -1)
                throw new NoSuchElementException();
            
            adjacencyList.remove(index);
            nodes.remove(node);
                    
            for (List<Edge<T>> edges : adjacencyList) {
                edges.removeIf(edge -> edge.getDestination().equals(node));
            }
        } catch (NoSuchElementException e){
            System.err.println("node doesn't exist");
            e.printStackTrace();
            throw e;
        }
    }


      //DFS recursive implementation
      private boolean dfs(T from, T to, boolean[] isVisited){
        if(from == to)
            return true;
        if(!nodes.contains(from) || !nodes.contains(to))
            return false;
        if(!isVisited[nodes.indexOf(from)])
            isVisited[nodes.indexOf(from)] = true;

        List<Edge<T>> list = new ArrayList<>(adjacencyList.get(nodes.indexOf(from)));
        for(Edge<T> e : list){
            //System.out.println(list.indexOf(e));
            if(!isVisited[nodes.indexOf(e.getDestination())]){
                //System.out.println("Edges destination: " + e.getDestination());
                if(dfs(nodes.get(nodes.indexOf(e.getDestination())), to, isVisited)){
                    return true;
                }
                isVisited[nodes.indexOf(e.getDestination())] = true;
            }
        }
        return false;
    }
}