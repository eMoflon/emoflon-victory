package org.emoflon.ibex.tgg.ui.debug.options;

public class UserOptionsManager implements IUserOptions {

	private boolean isInvalid = false;

	private boolean displayFullRuleForMatches = true;

	private boolean displaySrcContextForMatches = true;
	private boolean displayTrgContextForMatches = true;
	private boolean displayCorrContextForMatches = true;

	private VisualizationLabelOptions corrLabelVisualization = VisualizationLabelOptions.ABBREVIATED;
	private VisualizationLabelOptions edgeLabelVisualization = VisualizationLabelOptions.ABBREVIATED;
	private VisualizationLabelOptions nodeLabelVisualization = VisualizationLabelOptions.ABBREVIATED;
	private int neighborhoodSize = 0;

	private ToolTipOption toolTipSetting = ToolTipOption.MINIMAL;

	public static final int MAX_NEIGHBOURHOOD_SIZE = 3;

	@Override
	public boolean displayFullRuleForMatches() {
		return displayFullRuleForMatches;
	}

	public void setDisplayFullRuleForMatches(boolean displayFullRuleForMatches) {
		if (this.displayFullRuleForMatches != displayFullRuleForMatches) {
			this.displayFullRuleForMatches = displayFullRuleForMatches;
			isInvalid = true;
		}
	}

	public boolean isInvalid() {
		return isInvalid;
	}

	public void revalidate() {
		isInvalid = false;
	}

	/**
	 * @return the visualization option for correspondence labels
	 */
	@Override
	public VisualizationLabelOptions getCorrLabelVisualization() {
		return corrLabelVisualization;
	}

	/**
	 * @param corrLabelVisualization the visualization option for correspondence
	 *                               labels
	 */
	public void setCorrLabelVisualization(VisualizationLabelOptions corrLabelVisualization) {
		if (this.corrLabelVisualization != corrLabelVisualization) {
			this.corrLabelVisualization = corrLabelVisualization;
			isInvalid = true;
		}
	}

	/**
	 * @return the visualization option for edge labels
	 */
	@Override
	public VisualizationLabelOptions getEdgeLabelVisualization() {
		return edgeLabelVisualization;
	}

	/**
	 * @param nodeLabelVisualization the visualization option for node labels
	 */
	public void setEdgeLabelVisualization(VisualizationLabelOptions edgeLabelVisualization) {
		if (this.edgeLabelVisualization != edgeLabelVisualization) {
			this.edgeLabelVisualization = edgeLabelVisualization;
			isInvalid = true;
		}
	}

	/**
	 * @return the visualization option for node labels
	 */
	@Override
	public VisualizationLabelOptions getNodeLabelVisualization() {
		return nodeLabelVisualization;
	}

	/**
	 * @param nodeLabelVisualization the visualization option for node labels
	 */
	public void setNodeLabelVisualization(VisualizationLabelOptions nodeLabelVisualization) {
		if (this.nodeLabelVisualization != nodeLabelVisualization) {
			this.nodeLabelVisualization = nodeLabelVisualization;
			isInvalid = true;
		}
	}

	/**
	 * @return the displaySrcContextForMatches
	 */
	@Override
	public boolean displaySrcContextForMatches() {
		return displaySrcContextForMatches;
	}

	/**
	 * @param displaySrcContextForMatches the displaySrcContextForMatches to set
	 */
	public void setDisplaySrcContextForMatches(boolean displaySrcContextForMatches) {
		if (this.displaySrcContextForMatches != displaySrcContextForMatches) {
			this.displaySrcContextForMatches = displaySrcContextForMatches;
			this.isInvalid = true;
		}
	}

	/**
	 * @return the displayTrgContextForMatches
	 */
	@Override
	public boolean displayTrgContextForMatches() {
		return displayTrgContextForMatches;
	}

	/**
	 * @param displayTrgContextForMatches the displayTrgContextForMatches to set
	 */
	public void setDisplayTrgContextForMatches(boolean displayTrgContextForMatches) {
		if (this.displayTrgContextForMatches != displayTrgContextForMatches) {
			this.displayTrgContextForMatches = displayTrgContextForMatches;
			isInvalid = true;
		}
	}

	/**
	 * @return the displayTrgContextForMatches
	 */
	@Override
	public boolean displayCorrContextForMatches() {
		return displayCorrContextForMatches;
	}

	/**
	 * @param displayTrgContextForMatches the displayTrgContextForMatches to set
	 */
	public void setDisplayCorrContextForMatches(boolean displayCorrContextForMatches) {
		if (this.displayCorrContextForMatches != displayCorrContextForMatches) {
			this.displayCorrContextForMatches = displayCorrContextForMatches;
			isInvalid = true;
		}
	}

	public enum VisualizationLabelOptions {
		FULLNAME("Show Edge Labels", "Show Node Labels", "Show Corr Labels"), //
		ABBREVIATED("Show Abbreviated Edge Labels", "Show Abbreviated Node Labels", "Show Abbreviated Corr Labels"), //
		NONE("Hide Edge Labels", "Hide Node Labels", "Hide Corr Labels");

		public static final int EDGE = 0;
		public static final int NODE = 1;
		public static final int CORR = 2;

		private String[] optionNames;

		private VisualizationLabelOptions(String... pOptionNames) {
			optionNames = pOptionNames;
		}

		public static String[] getOptionNames(int pOptionIndex) {
			String[] optionNames = new String[values().length];
			for (int i = 0; i < values().length; i++)
				optionNames[i] = values()[i].optionNames[pOptionIndex];
			return optionNames;
		}

		public static VisualizationLabelOptions get(String pOptionName) {
			for (VisualizationLabelOptions labelOption : values())
				for (String optionName : labelOption.optionNames)
					if (optionName.equals(pOptionName))
						return labelOption;

			throw new IllegalArgumentException("Unknown visualization option: " + pOptionName);
		}
	}

	public int getNeighborhoodSize() {
		return neighborhoodSize;
	}

	public void setNeighborhoodSize(int size) {
		if (neighborhoodSize != size && size >= 0 && size <= MAX_NEIGHBOURHOOD_SIZE) {
			neighborhoodSize = size;
			isInvalid = true;
		}
	}

	/**
	 * @return the toolTipSetting
	 */
	public ToolTipOption getToolTipSetting() {
		return toolTipSetting;
	}

	/**
	 * @param toolTipSetting the toolTipSetting to set
	 */
	public void setToolTipSetting(ToolTipOption toolTipSetting) {
		this.toolTipSetting = toolTipSetting;
	}
}
