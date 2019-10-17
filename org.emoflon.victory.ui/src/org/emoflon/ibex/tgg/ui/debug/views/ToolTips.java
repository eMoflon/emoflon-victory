package org.emoflon.ibex.tgg.ui.debug.views;

import org.emoflon.ibex.tgg.ui.debug.options.IUserOptions;

public enum ToolTips {
	USEROPTION_NEIGHBORHOODSIZE("Set number of connected neighboring elements to show",
			"The neighborhood size slider enables you to define the number of visualized connected neighboring elements with each of the 3 levels of increment"),
	USEROPTION_SHOW_ELEMENTS("Choose which elements of the graphs to show",
			"You can choose whether or not to see the created, source, target and correspondence elements for a match by checking/unchecking the check-boxes corresponding to them"),
	USEROPTION_LABEL_STYLE("Choose whether to show full names, abbreviated names, or none",
			"Choose to see the full label names, an abbreviation of the labels or hide the label names completely for the edges, nodes and correspondence respectively"),
	MATCHDISPLAY_USEROPTION_BUTTON("Open menu to select visualization options",
			"Opens a menu in which you can change the graph visualization."

	), MATCHDISPLAY_TERMINATE_BUTTON("Close the debugger", "Clicking this button will close the debugger"),
	MATCHDISPLAY_RESTART_BUTTON("Close and restart the debugger",
			"Close and restart the debugger to start the process again from the initial models"),
	MATCHDISPLAY_SAVE_MODELS_BUTTON("Save the current state of the models",
			"Save the current state of the model. The saved models are added as (Source, Target and Correspondence) xmi files to the \"Instances\" folder"),
	MATCHDISPLAY_IMAGECONTAINER("Visualization of the elements selected on the left",
			"Visualization of the rules, matches or past rule applications selected in the left panes"),
	MATCHLIST_TREE("This panel shows all rules of the TGG as well of the found applicable matches of the rules.",
			"This panel shows all rules of the TGG as well of the found applicable matches of the rules.\nRules that have no matches have a dark background.If they have never been applied they have strike-out text. Rules with available matches have a white background. If they have never been applied before they have bold text."),
	MATCHLIST_APPLY_BUTTON("Applies the selected rule or match.",
			"Applies the selected rule or match. For rules, a random match of the rule is applied. Double-clicking a rule/match applies is an alternative to using this button."),
	MATCHLIST_COLLAPSE_BUTTON("Collapses all tree elements."), MATCHLIST_EXPAND_BUTTON("Expands all tree elements."),
	PROTOCOL_VIEW("List of previous rule applications.",
			"This panel shows all previous rule applications. Select one or multiple elements to visualize the rule applications.");

	private String shortDescription;
	private String longDescription;

	private ToolTips(String shortDescription, String longDescription) {
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
	}

	private ToolTips(String description) {
		this(description, description);
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public String getDescription(IUserOptions.ToolTipOption toolTipSetting) {
		switch (toolTipSetting) {
		case MINIMAL:
			return shortDescription;
		case FULL:
			return longDescription;
		default:
			return "";
		}
	}
}
