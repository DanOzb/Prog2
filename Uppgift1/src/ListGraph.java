import java.io.Serializable;
import java.util.*;

public class ListGraph<T> implements Serializable{
    private List<T> nodes; 
    private List<List<Edge<T>>> adjacencyList;
    private Map<T,LinkedList<Edge<T>>> shortestPathMap = new HashMap<>();
    private Map<T, Integer> nodesMap = new HashMap<>();


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
            Edge<T> edge = getEdgeBetween(node1, node2);
            Edge<T> edge2 = getEdgeBetween(node2, node1);

            int index1 = nodes.indexOf(node1);
            int index2 = nodes.indexOf(node2);

            if(edge == null)
                throw new IllegalStateException();

            adjacencyList.get(index1).removeIf(index -> index == edge || index == edge2);
            adjacencyList.get(index2).removeIf(index -> index ==  edge || index == edge2);


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
    public Set<T> getNodes() {
        return new HashSet<>(nodes);
    }
    
    //get edges from a node method | tested
    public List<Edge<T>> getEdgesFrom(T node) throws NoSuchElementException {
        try{
            int index = nodes.indexOf(node);
            if (index == -1) {
                throw new NoSuchElementException();
            } else {
                return new ArrayList<>(adjacencyList.get(index));
            }
        } catch (NoSuchElementException e) {
            System.err.println("node doesn't exist");
            e.printStackTrace();
            throw e;
        }

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

    //set edge connection method | tested
    public void setConnectionWeight(T node1, T node2, int newWeight) throws IllegalArgumentException, NoSuchElementException{
        String noNodeMessage = "There is no edge between the nodes";
        try {    
            if (newWeight < 0) {
                throw new IllegalArgumentException();
            }
            int index1 = nodes.indexOf(node1);
            int index2 = nodes.indexOf(node2);

            if (index1 == -1 || index2 == -1) {
                noNodeMessage = "One or both of the nodes do not exist";
                throw new NoSuchElementException();
            }
            Edge<T> edge = getEdgeBetween(node1, node2);

            if (edge == null) {
                throw new NoSuchElementException();
            }
            edge.setWeight(newWeight);
    
            for (Edge<T> e : adjacencyList.get(index2)) {
                if (e.getDestination().equals(node1)) {
                    e.setWeight(newWeight);
                    break;
                }
            }
        } catch(IllegalArgumentException e){ 
            System.err.println("Weight can not be negative");
            e.printStackTrace();
            throw e;
        } catch(NoSuchElementException e){ 
            System.err.println(noNodeMessage);
            e.printStackTrace();
            throw e;
        }
    }

    //find shortest path between nodes | tested
    public List<Edge<T>> getPath(T sourceNode, T targetNode){
        if(!pathExists(sourceNode, targetNode))
            return null;
        Set<T> settled = new HashSet<>();
        Set<T> unsettled = new HashSet<>();
        unsettled.add(sourceNode);
        for(T node : nodes){
            nodesMap.put(node, Integer.MAX_VALUE);
            shortestPathMap.put(node, new LinkedList<>());
        }
        nodesMap.put(sourceNode, 0);
        
        while(!unsettled.isEmpty()){
            T currentNode = getLowestWeightNode(unsettled, nodesMap);
            unsettled.remove(currentNode);
            for(Edge<T> edge : adjacencyList.get(nodes.indexOf(currentNode))){
                T nextNode = edge.getDestination();

                if(!settled.contains(nextNode)){
                    calculateShortestPath(edge, edge.getWeight() ,currentNode);
                    unsettled.add(nextNode);
                    if(nodesMap.get(currentNode) < nodesMap.get(nextNode))
                        nodesMap.put(nextNode, edge.getWeight() + nodesMap.get(currentNode));
                } 
            }
            settled.add(currentNode);
        }
        return shortestPathMap.get(targetNode);
    }

    public String toString(){
        String nodeText = ""; 
        for(T node  : nodes){
            nodeText += node.toString() + ": \n";
            for(Edge<T> edge : adjacencyList.get(nodes.indexOf(node))){
                nodeText += edge.toString() + "\n";
            }
            nodeText += "\n \n";
        }
        return nodeText;
    }

    //get node with lowest weight method
    private T getLowestWeightNode(Set<T> set, Map<T, Integer> map){
        T lowestWeightNode = null;
        int minimum = Integer.MAX_VALUE;
        for(T node : set){
            int nodeWeight = map.get(node);
            if(nodeWeight < minimum){
                minimum = nodeWeight;
                lowestWeightNode = node;
            }
        }
        return lowestWeightNode;
    }
    //calculate shortest path between nodes method
    private void calculateShortestPath(Edge<T> edge, int edgeWeight ,T currentNode){
        int sourceWeight = nodesMap.get(currentNode);
        T nextNode = edge.getDestination();
        if(sourceWeight + edgeWeight < nodesMap.get(nextNode)){
            nodesMap.put(nextNode, sourceWeight + edgeWeight);
            LinkedList<Edge<T>> shortestPath = new LinkedList<>(shortestPathMap.get(currentNode));
            shortestPath.add(edge);
            shortestPathMap.put(nextNode, shortestPath);
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
            if(!isVisited[nodes.indexOf(e.getDestination())]){
                if(dfs(nodes.get(nodes.indexOf(e.getDestination())), to, isVisited)){
                    return true;
                }
                isVisited[nodes.indexOf(e.getDestination())] = true;
            }
        }
        return false;
    }
}