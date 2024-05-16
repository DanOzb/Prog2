public class Main {

    public static void main(String[] args){
        ListGraph<Nodes> graph = new ListGraph<>();
        Nodes node = new Nodes();
        Nodes node2 = new Nodes();
        Nodes node3 = new Nodes();

        graph.add(node);
        graph.add(node2);
        graph.add(node3);
        graph.remove(node3);

        graph.connect(node, node2, "something", 3);
        graph.disconnect(node,node2);

        boolean result = graph.pathExists(node, node2);
        System.out.println(result);
    }
}
