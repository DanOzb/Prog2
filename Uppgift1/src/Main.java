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
    
    private static void testGetEdgesMethod(){
        ListGraph<String> graph = new ListGraph<>();

        graph.add("NodeA");
        graph.add("NodeB");
        graph.add("NodeC");

        graph.connect("NodeA", "NodeB", "Connection1", 0);
        graph.connect("NodeA", "NodeC", "Connection2", 0);

        System.out.println(graph.getEdgesFrom("NodeA")); //2 edges
        System.out.println(graph.getEdgesFrom("NodeB")); //1 edge

        System.out.println(graph.getEdgesFrom("NodeD")); //testing exception
    }
   
    private static void testPathExistsMethod(){
        ListGraph<String> graph = new ListGraph<>();

        graph.add("NodeA");
        graph.add("NodeB");
        graph.add("NodeC");

        graph.connect("NodeA", "NodeB", "Connection1", 0);

        System.out.println("Node A and Node B: " + graph.pathExists("NodeA", "NodeB"));
        System.out.println("Node A and Node C: " + graph.pathExists("NodeA", "NodeC"));

        graph.connect("NodeC", "NodeB", "Connection1", 0);

        System.out.println("Node A and Node C: " + graph.pathExists("NodeA", "NodeC"));
    }
    
    private static void testDisconnectMethod(){
        ListGraph<String> graph = new ListGraph<>();

        graph.add("NodeA");
        graph.add("NodeB");
        graph.add("NodeC");
        graph.add("NodeD");

        graph.connect("NodeA", "NodeB", "Connection1", 0);
        graph.connect("NodeB", "NodeC", "Connection2", 0);
        System.out.println("Node A and Node B: " + graph.pathExists("NodeA", "NodeB"));

        graph.disconnect("NodeA", "NodeB");
        //checks if no such element exception works
        //System.out.println("Node A and Node B: " + graph.pathExists("NodeA", "NodeB"));

        try{
            graph.disconnect("NodeA", "NodeD");
        } catch(NoSuchElementException e){
            helpMethod2(graph);
        } 

    }

    private static void helpMethod2(ListGraph<String> graph){
        try{
            //checks if illegal state exception works
            System.out.println("Node A and Node D: " + graph.pathExists("NodeA", "NodeD"));
        } catch(IllegalStateException e){
            //do nothing
        } 
    }

    private static void testConnectionMethod(){
        ListGraph<String> graph = new ListGraph<>();
        graph.add("NodeA");
        graph.add("NodeB");
        graph.add("NodeC");

        graph.connect("NodeA", "NodeB", "Connection1", 0);
        graph.connect("NodeA", "NodeC", "Connection2", 0);

        System.out.println(graph.pathExists("Connection between Node A and B: " + "NodeA", "NodeC"));
        
        //checks if illegal argument exception works
        //graph.connect("NodeA", "NodeC", "Connection2", -1);

        //checks if illegal state exception works
        //graph.connect("NodeA", "NodeB", "Connection1", 0);

        //checks if no such element exception works
        //graph.connect("NodeA", "NodeD", "Connection1", 0);

    }
    public static void main(String[] args){

        //testRemoveMethod();
        //testGetEdgeBetweenMethod();
        //testGetEdgesMethod();
        //testPathExistsMethod();
        //testDisconnectMethod();
        //testConnectionMethod();
    }
}
