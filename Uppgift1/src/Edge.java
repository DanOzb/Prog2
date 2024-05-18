import java.io.Serializable;

public class Edge<T> implements Serializable {
    private int weight; 
    private String name; 
    private T destination;
    
    Edge(int weight, String name, T destination){
        this.weight = weight;
        this.name = name;
        this.destination = destination;
    }

    public int getWeight(){
        return weight;
    }

    public String getName(){
        return name;
    }

    public T getDestination(){
        return destination;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    public String toString(){
        
        return String.format("Edge info: %s | %d | %s", name, weight, destination.toString());
    }
}