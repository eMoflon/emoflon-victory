package org.emoflon.victory.ui.views.treeContent.matchList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.emoflon.victory.ui.api.Rule;
import org.emoflon.victory.ui.core.VictoryUI;
import org.emoflon.victory.ui.options.IUserOptions;
import org.emoflon.victory.ui.options.IUserOptions.ToolTipOption;
import org.emoflon.victory.ui.views.treeContent.TreeNode;

public class RuleNode extends TreeNode {

	private Rule rule;
	private final IUserOptions userOptions;

	private int timesApplied = 0;

	protected RuleNode(Rule rule, IUserOptions userOptions) {
		this.rule = rule;
		this.userOptions = userOptions;
	}

	public Rule getRule() {
		return rule;
	}

	@Override
	protected String getLabel() {
		this.setStrikethrough(!hasChildren() && !(timesApplied > 0));

		if (hasChildren() && !(timesApplied > 0)) {
			setFontStyle(SWT.BOLD);
		} else {
			setFontStyle(SWT.NORMAL);
		}
		return rule.getName() + " (matches: " + getChildren().size() + ", applied matches: " + timesApplied + ")";
	}

	@Override
	protected Image getImage() {
		return null;
	}

	@Override
	protected Color getForeground() {
		if (hasChildren()) {
			return VictoryUI.getDisplay().getSystemColor(SWT.COLOR_BLACK);
		} else {
			return VictoryUI.getDisplay().getSystemColor(SWT.COLOR_WHITE);
		}
	}

	@Override
	protected Color getBackground() {
		if (hasChildren()) {
			return VictoryUI.getDisplay().getSystemColor(SWT.COLOR_WHITE);
		} else {
			return VictoryUI.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);
		}
	}

	public int getTimesApplied() {
		return timesApplied;
	}

	public void increaseTimesApplied() {
		this.timesApplied++;
	}

	@Override
	protected String getToolTip() {
		if (userOptions.getToolTipSetting() == ToolTipOption.NONE) {
			return "";
		}
		String toolTip = "Rule " + getRule().getName() + ": ";
		if (this.hasChildren())
			toolTip += "This rule currently has " + this.getChildren().size() + " matches ";
		else
			toolTip += "This rule currently has no matches ";
		if (timesApplied >= 0)
			toolTip += "and has been applied " + timesApplied + " before.";
		else
			toolTip += "and has never been applied yet.";
		if (this.hasChildren() && this.userOptions.getToolTipSetting() == ToolTipOption.FULL) {
			toolTip += " Double click to apply a random match. Select to display the rule in the right panel.";
		}
		return toolTip;
	}
}
