package org.emoflon.ibex.tgg.ui.debug.options;

import org.emoflon.ibex.tgg.ui.debug.options.UserOptionsManager.VisualizationLabelOptions;

public interface IUserOptions {

	public boolean displayFullRuleForMatches();

	/**
	 * @return the visualization option for correspondence labels
	 */
	public VisualizationLabelOptions getCorrLabelVisualization();

	public int getNeighborhoodSize();

	/**
	 * @return the visualization option for edge labels
	 */
	public VisualizationLabelOptions getEdgeLabelVisualization();

	/**
	 * @return the visualization option for node labels
	 */
	public VisualizationLabelOptions getNodeLabelVisualization();

	public boolean displayTrgContextForMatches();

	public boolean displaySrcContextForMatches();

	public boolean displayCorrContextForMatches();

	public ToolTipOption getToolTipSetting();

	public enum ToolTipOption {
		FULL, MINIMAL, NONE
	}
}
