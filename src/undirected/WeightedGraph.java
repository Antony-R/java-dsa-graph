package undirected;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

public class WeightedGraph {
	private class Node {
		private String label;
		private List<Edge> edges = new ArrayList<>();

		public Node(String label) {
			this.label = label;
		}

		@Override
		public String toString() {
			return label;
		}

		public void addEdge(Node toNode, int weight) {
			edges.add(new Edge(this, toNode, weight));
		}

		public List<Edge> getEdges() {
			return edges;
		}

	}

	private class Edge {
		private Node fromNode;
		private Node toNode;
		private int weight;

		public Edge(Node fromNode, Node toNode, int weight) {
			this.fromNode = fromNode;
			this.toNode = toNode;
			this.weight = weight;
		}

		@Override
		public String toString() {
			return fromNode + "->" + toNode;
		}

	}

	private Map<String, Node> nodes = new HashMap<>();

	public void addNode(String label) {
		nodes.putIfAbsent(label, new Node(label));
	}

	public void addEdge(String from, String to, int weight) {
		Node fromNode = nodes.get(from);
		if (fromNode == null)
			throw new IllegalArgumentException();

		Node toNode = nodes.get(to);
		if (toNode == null)
			throw new IllegalArgumentException();

		fromNode.addEdge(toNode, weight);
		toNode.addEdge(fromNode, weight);
	}

	public void print() {
		for (Node node : nodes.values()) {
			List<Edge> edges = node.getEdges();
			if (!edges.isEmpty())
				System.out.println(node + " is connected to " + edges);
		}
	}

	private class NodeEntry {
		private Node node;
		private int priority;

		public NodeEntry(Node node, int priority) {
			this.node = node;
			this.priority = priority;
		}

	}

	public String getShortestPath(String from, String to) {
		
		Node fromNode = nodes.get(from);
		if (fromNode == null) return "From node not found.";
		Node toNode = nodes.get(to);
		if (toNode == null) return "To node not found";
		
		PriorityQueue<NodeEntry> queue = new PriorityQueue<>(Comparator.comparingInt(ne -> ne.priority));
		Set<Node> visited = new HashSet<>();
		Map<Node, Integer> distances = new HashMap<>();
		Map<Node, Node> previousNodes = new HashMap<>();
		
		for (Node node : nodes.values()) {
			distances.put(node, Integer.MAX_VALUE);
		}
		distances.replace(fromNode, 0);

		queue.add(new NodeEntry(fromNode, 0));

		while (!queue.isEmpty()) {
			Node current = queue.remove().node;
			visited.add(current);

			for (Edge edge : current.getEdges()) {
				if (visited.contains(edge.toNode))
					continue;
				int newDistance = distances.get(current) + edge.weight;

				if (newDistance < distances.get(edge.toNode)) {
					distances.replace(edge.toNode, newDistance);
					previousNodes.put(edge.toNode, current);
					queue.add(new NodeEntry(edge.toNode, newDistance));
				}
			}
		}
		//return distances.get(toNode);
		
		return buildPath(previousNodes, toNode);
	}
	
	private String buildPath(Map<Node, Node> previousNodes, Node toNode) {
		
		String path = "";
		
		Stack<Node> stack = new Stack<>();
		stack.push(toNode);
		Node previous = previousNodes.get(toNode);
		
		while (previous != null) {
			stack.push(previous);
			previous = previousNodes.get(previous);
		}
		
		while(!stack.empty()) {
			path += stack.pop().label + "->";
		}
		
		return path;
	}
	
	public boolean hasCycle() {
		Set<Node> visited = new HashSet<>();
		for (Node node: nodes.values()) {
			if (!visited.contains(node) && hasCycle(node, null, visited)) return true;
		}
		return false;
	}
	
	private boolean hasCycle(Node node, Node parent, Set<Node> visited) {
		visited.add(node);
		for (Edge edge: node.getEdges()) {
			if (edge.toNode == parent) continue;
			if (visited.contains(edge.toNode) || hasCycle(edge.toNode, node, visited)) {
				return true;
			}
		}
		return false;
	}
	
	public WeightedGraph getMinimumSpanningTree() {
		WeightedGraph tree = new WeightedGraph();
		PriorityQueue<Edge> edges = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
		
		if (nodes.isEmpty()) return tree;
		
		Node startNode = nodes.values().iterator().next();
		edges.addAll(startNode.getEdges());
		tree.addNode(startNode.label);
		
		if (edges.isEmpty()) return tree;
		
		while (tree.nodes.size()  < nodes.size()) {
			System.out.println("PQ: " + edges);
			Edge minEdge = edges.remove();
			System.out.println("min edge: " + minEdge);
			Node nextNode = minEdge.toNode;
			System.out.println("next node: " + nextNode);
			
			if (tree.containsNode(nextNode.label)) continue;
			
			tree.addNode(nextNode.label);
			tree.addEdge(minEdge.fromNode.label, nextNode.label, minEdge.weight);
			
			for (Edge edge: nextNode.getEdges()) {
				if (!tree.containsNode(edge.toNode.label)) {
					System.out.println("For, edge: " + edge);
					edges.add(edge);
				}
			}
		}
		
		return tree;
		
	}
	
	public boolean containsNode(String label) {
		return nodes.containsKey(label);
	}
}
