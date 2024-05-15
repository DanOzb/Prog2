import java.io.Serializable;
import java.util.*;

public class ListGraph<T> implements Serializable{
    private List<T> nodes; 
    private List<List<Edge>> adjacencyList;

    ListGraph(){
        nodes = new ArrayList<>();
        adjacencyList = new ArrayList<>();
    }

    public void add(T node){
        if(!nodes.contains(node))
            nodes.add(node);
        adjacencyList.add(new ArrayList<Edge>());
    }

    //inte klar
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

    //inte klar
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

}