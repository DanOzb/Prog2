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
        graph.add("NodeD");

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

        graph.disconnect("NodeB", "NodeA");
        System.out.println(graph.getEdgesFrom("NodeA"));
        System.out.println(graph.getEdgesFrom("NodeB"));

        try{
            //graph.disconnect("NodeA", "NodeD");
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
    
    private static void testSetWeightMethod(){
        ListGraph<String> graph = new ListGraph<>();
        graph.add("NodeA");
        graph.add("NodeB");
        graph.add("NodeC");

        graph.connect("NodeA", "NodeB", "something", 0);
        graph.connect("NodeA", "NodeC", "something", 0);

        System.out.println("Should be weight 0: " + graph.getEdgeBetween("NodeA", "NodeB"));
        System.out.println("Should be weight 0: " + graph.getEdgeBetween("NodeA", "NodeC")); 
 

        graph.setConnectionWeight("NodeA", "NodeB", 5);

        System.out.println("Should be weight 5: " + graph.getEdgeBetween("NodeA", "NodeB")); 

        graph.setConnectionWeight("NodeA", "NodeB", -1);
        //graph.setConnectionWeight("NodeA", "NodeC", 5);
        //graph.setConnectionWeight("NodeA", "NodeD", 5);
    }

    public static void testGetPathMethod(){
        ListGraph<String> graph = new ListGraph<>();

        graph.add("NodeA");
        graph.add("NodeB");
        graph.add("NodeC");
        graph.add("NodeD");
        graph.add("NodeE");
        graph.add("NodeF");

        graph.connect("NodeA", "NodeB", "A to B", 10);
        graph.connect("NodeA", "NodeC", "A to C", 15);

        graph.connect("NodeC", "NodeE", "C to E", 10);

        graph.connect("NodeB", "NodeD", "B to D", 12);
        graph.connect("NodeB", "NodeF", "B to F", 15);

        graph.connect("NodeD", "NodeF", "D to F", 1);
        graph.connect("NodeD", "NodeE", "D to E", 2);

        graph.connect("NodeF", "NodeE", "F to E", 5);

        System.out.println(graph.getPath("NodeA", "NodeE"));

    }

    public static void testToStringMethod(){
        ListGraph<String> graph = new ListGraph<>();

        graph.add("NodeA");
        graph.add("NodeB");
        graph.add("NodeC");
        graph.add("NodeD");
        graph.add("NodeE");
        graph.add("NodeF");

        graph.connect("NodeA", "NodeB", "A to B", 10);
        graph.connect("NodeA", "NodeC", "A to C", 15);

        graph.connect("NodeC", "NodeE", "C to E", 10);

        graph.connect("NodeB", "NodeD", "B to D", 12);
        graph.connect("NodeB", "NodeF", "B to F", 15);

        graph.connect("NodeD", "NodeF", "D to F", 1);
        graph.connect("NodeD", "NodeE", "D to E", 2);

        graph.connect("NodeF", "NodeE", "F to E", 5);

        System.out.println(graph.toString());
    }

    public static void main(String[] args){

        //testRemoveMethod();
        //testGetEdgeBetweenMethod();
        //testGetEdgesMethod();
        //testPathExistsMethod();
        //testDisconnectMethod();
        //testConnectionMethod();
        //testSetWeightMethod();
        //testGetPathMethod();
        //testToStringMethod();
    }
}
