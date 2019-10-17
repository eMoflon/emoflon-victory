package org.emoflon.ibex.tgg.ui.debug.api;

/**
 * A representation of a match, providing all necessary info for visualization
 * by Victory.
 */
public interface Match {
	public String getName();

	public boolean isBlocked();

	public String getBlockingReason();

	public Rule getRule();

	/**
	 * The returned graph should explicitly contain:<br>
	 * 1) all model-nodes that are part of the match<br>
	 * 2) all edges between those model-nodes<br>
	 * 3) edges from each rule-node to its matched model-node
	 * <p>
	 * The rule-nodes in 3) should NOT be included in the nodes of this graph. They
	 * should only be referenced by the edges.
	 * 
	 * @param neighbourhoodSize
	 *            the neighbourhood size setting for which the graph should be built
	 * @return a graph for this match
	 */
	public Graph getGraph(int neighbourhoodSize);
}
