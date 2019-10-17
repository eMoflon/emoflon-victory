package org.emoflon.ibex.tgg.ui.debug.api;

import java.util.Collection;

/**
 * A graph that can be visualized by Victory. See also {@link Node} and
 * {@link Edge}.
 * <p>
 * Note that when visualizing a Graph-object, Victory will display only those
 * nodes that are in the Graph-object's node-set, and only those edges which are
 * contained in the edge-set. Nodes that are referenced by contained edges, but
 * not contained in the node-set, as well as edges referencing nodes in the
 * node-set, but not contained in the edge-set, will not be displayed.<br>
 * This allows for the construction of subgraphs that can be displayed both
 * independently as well as together, without loosing any of the edges that
 * connect elements between subgraphs.
 */
public interface Graph {
	public Collection<Node> getNodes();

	public Collection<Edge> getEdges();
}
