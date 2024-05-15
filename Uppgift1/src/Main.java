public class Main {

    public static void main(String[] args){
        ListGraph<Nodes> graph = new ListGraph<>();
        Nodes node = new Nodes();
        Nodes node2 = new Nodes();

        graph.add(node);
        graph.add(node2);

        graph.connect(node, node2, "something", 3);

        boolean result = graph.pathExists(node, node2);
        System.out.println(result);
    }
}
