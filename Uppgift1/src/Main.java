import java.util.NoSuchElementException;

public class Main {

    public static void testRemoveMethod(){
        ListGraph<String> graph = new ListGraph<>();

        graph.add("NodeA");
        graph.add("NodeB");
        graph.add("NodeC");

        System.out.println(graph.getNodes());
        graph.remove("NodeA");
        System.out.println(graph.getNodes());
        graph.remove("NodeD");
    }

    public static void testGetEdgeBetweenMethod(){
        ListGraph<String> graph = new ListGraph<>();

        graph.add("NodeA");
        graph.add("NodeB");
        try{
            System.out.println("Edges: " + graph.getEdgeBetween("NodeA", "NodeB")); //without edges
        } catch(IllegalArgumentException e){
            System.err.println("Main Method: Illegal argument exception thrown");
            helpMethod1(graph);
        } 

        
    }

    private static void helpMethod1(ListGraph<String> graph){
        try {
            graph.connect("NodeA", "NodeB", "Something", 0);
            System.out.println(graph.getEdgeBetween("NodeA", "NodeB"));

            graph.remove("NodeA");
            System.out.println(graph.getEdgeBetween("NodeA", "NodeB")); //without NodeA
        } catch(NoSuchElementException e) {
            System.err.println("Main Method: No such element exception thrown");
        }
    }
    public static void main(String[] args){

        //testRemoveMethod();
        testGetEdgeBetweenMethod();
        
    }
}
