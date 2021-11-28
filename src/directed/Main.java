package directed;

public class Main {

	public static void main(String[] args) {
		Graph graph = new Graph();
		graph.addNode("X");
		graph.addNode("A");
		graph.addNode("B");
		graph.addNode("P");
		/*
		graph.addEdge("A", "B");
		graph.addEdge("B", "D");
		graph.addEdge("D", "C");
		graph.addEdge("A", "C");
		//graph.removeNode("B");
		*/
		graph.addEdge("X", "A");
		graph.addEdge("X", "B");
		graph.addEdge("A", "P");
		graph.addEdge("B", "P");
		//graph.addEdge("P", "X");
		graph.print();
		System.out.println("-".repeat(10));
		graph.traverseDepthFirst("A");
		System.out.println("-".repeat(10));
		graph.traverseBreadthFirst("A");
		System.out.println("-".repeat(10));
		System.out.println(graph.topologicalSort());
		System.out.println("-".repeat(10));
		System.out.println(graph.hasCycle());
	}

}
