package org.emoflon.ibex.tgg.ui.debug.api;

import java.util.Collection;

/**
 * Defines how multiple {@link RuleApplication}s are merged together into a
 * single Graph for joint visualization.
 */
public interface RuleApplicationMerger {
	/**
	 * Builds a graph for the specified collection of rule-applications.<br>
	 * Which elements of the rule-applications are included is up to the
	 * implementation to decide.
	 * 
	 * @param ruleApplications
	 *            the rule-applications to build the graph for
	 * @param neighbourhoodSize
	 *            the neighbourhood size setting made by the user
	 * @return a graph for the specified collection of rule-applications
	 */
	public Graph getMergedGraph(Collection<RuleApplication> ruleApplications, int neighbourhoodSize);
}
