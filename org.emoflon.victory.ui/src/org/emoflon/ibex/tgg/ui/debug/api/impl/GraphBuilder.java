package org.emoflon.ibex.tgg.ui.debug.api.impl;

import java.util.Collection;
import java.util.HashSet;

import org.emoflon.ibex.tgg.ui.debug.api.Edge;
import org.emoflon.ibex.tgg.ui.debug.api.Graph;
import org.emoflon.ibex.tgg.ui.debug.api.Node;
import org.emoflon.ibex.tgg.ui.debug.api.enums.Action;
import org.emoflon.ibex.tgg.ui.debug.api.enums.EdgeType;

/**
 * Builds a {@link Graph} object from the provided {@link Node}s and
 * {@link Edge}s. Can also be used to merge multiple Graphs into a single one.
 */
public class GraphBuilder {
	private Collection<Node> nodes = new HashSet<>();
	private Collection<Edge> edges = new HashSet<>();

	public GraphBuilder() {
	}

	public GraphBuilder(Graph graph) {
		addGraph(graph);
	}

	public GraphBuilder addGraph(Graph graph) {
		if (graph == null)
			return this;
		nodes.addAll(graph.getNodes());
		edges.addAll(graph.getEdges());
		return this;
	}

	public GraphBuilder addNode(Node nodeGroup) {
		nodes.add(nodeGroup);
		return this;
	}

	public GraphBuilder addEdge(Edge edge) {
		edges.add(edge);
		return this;
	}

	public void addEdge(String label, Node srcNode, Node trgNode, EdgeType type, Action action) {
		addEdge(new EdgeImpl(label, srcNode, trgNode, type, action));
	}

	public GraphImpl build() {
		return new GraphImpl(nodes, edges);
	}
}
