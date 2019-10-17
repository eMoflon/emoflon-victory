package org.emoflon.ibex.tgg.ui.debug.views.treeContent.protocol;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.emoflon.ibex.tgg.ui.debug.api.RuleApplication;
import org.emoflon.ibex.tgg.ui.debug.core.VictoryUI;
import org.emoflon.ibex.tgg.ui.debug.options.IUserOptions;
import org.emoflon.ibex.tgg.ui.debug.options.IUserOptions.ToolTipOption;
import org.emoflon.ibex.tgg.ui.debug.views.treeContent.TreeNode;

public class RuleApplicationNode extends TreeNode {

	private int index;
	private RuleApplication ruleApplication;
	private boolean markBold = false;
	private boolean highlighted = false;
	private final IUserOptions userOptions;

	public RuleApplicationNode(int index, RuleApplication step, IUserOptions userOptions) {
		this.index = index;
		this.ruleApplication = step;
		this.userOptions = userOptions;
	}

	public int getStep() {
		return index;
	}

	public void setBold(boolean bold) {
		markBold = bold;
	}

	public void highlight(boolean highlighted) {
		this.highlighted = highlighted;
	}

	public RuleApplication getModelChanges() {
		return ruleApplication;
	}

	@Override
	protected String getLabel() {
		setFontStyle(markBold ? SWT.BOLD : SWT.NORMAL);
		return "Rule application #" + index + ": " + ruleApplication.getRuleName();
	}

	@Override
	protected Image getImage() {
		return null;
	}

	@Override
	protected Color getForeground() {
		return null;
	}

	@Override
	protected Color getBackground() {
		if (highlighted) {
			return VictoryUI.getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
		}
		return null;
	}

	@Override
	protected String getToolTip() {
		if (userOptions.getToolTipSetting() == ToolTipOption.NONE)
			return "";
		String toolTip = "Rule application of rule \"" + ruleApplication.getRuleName() + "\".";
		if (userOptions.getToolTipSetting() == ToolTipOption.FULL)
			toolTip += " Select one or multiple rule applications to see them in the right panel.";
		return toolTip;
	}
}
