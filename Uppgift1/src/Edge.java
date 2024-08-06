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
        try{
            if(weight < 0)
                throw new IllegalArgumentException();
            this.weight = weight;
        } catch(IllegalArgumentException e){
            System.err.println("Weight can not be negative");
            e.printStackTrace();
            throw e;
        }
    }

    public String toString(){
        //vpl wants it this way
        return String.format("to %s by %s takes %d", destination, name, weight);
    }
}