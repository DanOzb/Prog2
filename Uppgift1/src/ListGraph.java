import java.io.Serializable;
import java.util.*;

public class ListGraph<T> implements Serializable{
    private List<T> nodes; 
    private List<List<Edge<T>>> adjacencyList;

    ListGraph(){
        nodes = new ArrayList<>();
        adjacencyList = new ArrayList<>();
    }

    //add node method
    public void add(T node){
        if(!nodes.contains(node))
            nodes.add(node);
        adjacencyList.add(new ArrayList<Edge<T>>());
    }

    //connect two nodes method | tested
    public void connect(T node1, T node2, String name, int weight) throws NoSuchElementException, IllegalArgumentException, IllegalStateException{
        try{
            if(weight < 0)
                throw new IllegalArgumentException();
            else if(!nodes.contains(node1) || !nodes.contains(node2))
                throw new NoSuchElementException();
            else if(getEdgeBetween(node1, node2) != null)
                throw new IllegalStateException();
            
            Edge<T> edge1 = new Edge<T>(weight, name, node2);
            Edge<T> edge2 = new Edge<T>(weight, name, node1);
            adjacencyList.get(nodes.indexOf(node1)).add(edge1);
            adjacencyList.get(nodes.indexOf(node2)).add(edge2);

        } catch(IllegalArgumentException e){
            System.err.println("The given weight can not be negative");
            e.getStackTrace();
            throw e;
        } catch(IllegalStateException e){
            System.err.println("An edge between the two nodes already exists");
            e.getStackTrace();
            throw e;
        } catch(NoSuchElementException e){
            System.err.println("One or both nodes doesn't exist");
            e.getStackTrace();
            throw e;
        } 
    }

    //disconnect 2 nodes method | tested
    public void disconnect(T node1, T node2) throws NoSuchElementException, IllegalStateException{
        try{
            int index1 = nodes.indexOf(node1);
            int index2 = nodes.indexOf(node2);
        
            Edge<T> edge = getEdgeBetween(node1, node2);

            System.out.println(edge);
            if(edge == null)
                throw new IllegalStateException();

            adjacencyList.get(index1).removeIf(index -> index == edge);
            adjacencyList.get(index2).removeIf(index -> index == edge);

        } catch(NoSuchElementException e){
            //getEdgeBetween prints and throws the exception
        } catch(IllegalStateException e){
            System.err.println("No edge exists between the specified nodes.");
            e.printStackTrace();
            throw e;
        }
    }
    
    //Checks if path exists method | tested
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

    //get all nodes method
    public List<T> getNodes() {
        return new ArrayList<>(nodes);
    }
    
    //get edges from a node method | tested
    public List<Edge<T>> getEdgesFrom(T node) throws NoSuchElementException {
        int index = nodes.indexOf(node);

        try{
            if (index == -1) {
                throw new NoSuchElementException();
            }
        } catch (NoSuchElementException e) {
            System.err.println("node doesn't exist");
        }

        return new ArrayList<>(adjacencyList.get(index));
    }

    //get edges between two nodes method | tested
    public Edge<T> getEdgeBetween(T from, T to) throws NoSuchElementException{
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
        
        } catch(NoSuchElementException e) {
            System.err.println("One or both of the specified nodes do not exist in the graph.");
            e.printStackTrace();
            throw e;
        } 
        
        return null;
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