package org.emoflon.ibex.tgg.ui.debug.api.impl;

import java.util.Collection;
import java.util.Collections;

import org.emoflon.ibex.tgg.ui.debug.api.Edge;
import org.emoflon.ibex.tgg.ui.debug.api.Graph;
import org.emoflon.ibex.tgg.ui.debug.api.Node;

/**
 * A basic implementation of the {@link Graph} interface. Can be created using a
 * {@link GraphBuilder}.
 */
public class GraphImpl implements Graph {
	private Collection<Node> nodes;
	private Collection<Edge> edges;

	GraphImpl(Collection<Node> nodes, Collection<Edge> edges) {
		this.nodes = Collections.unmodifiableCollection(nodes);
		this.edges = Collections.unmodifiableCollection(edges);
	}

	public Collection<Node> getNodes() {
		return nodes;
	}

	public Collection<Edge> getEdges() {
		return edges;
	}
}
