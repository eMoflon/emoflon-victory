package org.emoflon.victory.ui.api;

/**
 * A representation of the application of a rule in a previous step.
 */
public interface RuleApplication {
	public int getIndex();

	public String getRuleName();

	public RuleApplicationMerger getMerger();
}
