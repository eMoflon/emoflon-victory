package org.emoflon.ibex.tgg.ui.debug.views.treeContent.matchList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.emoflon.ibex.tgg.ui.debug.api.Match;
import org.emoflon.ibex.tgg.ui.debug.core.VictoryUI;
import org.emoflon.ibex.tgg.ui.debug.options.IUserOptions;
import org.emoflon.ibex.tgg.ui.debug.options.IUserOptions.ToolTipOption;
import org.emoflon.ibex.tgg.ui.debug.views.treeContent.TreeNode;

public class MatchNode extends TreeNode {

	private Match match;
	private final IUserOptions userOptions;

	protected MatchNode(Match match, IUserOptions userOptions) {
		this.match = match;
		this.userOptions = userOptions;
	}

	public Match getMatch() {
		return match;
	}

	@Override
	protected String getLabel() {
		return match.getName() + (match.isBlocked() ? "[" + match.getBlockingReason() + "]" : "");
	}

	@Override
	protected Image getImage() {
		return null;
	}

	@Override
	protected Color getForeground() {
		if (match.isBlocked())
			return VictoryUI.getDisplay().getSystemColor(SWT.COLOR_RED);
		else
			return null;
	}

	@Override
	protected Color getBackground() {
		return null;
	}

	@Override
	protected String getToolTip() {
		if (userOptions.getToolTipSetting() == ToolTipOption.NONE)
			return "";
		String toolTip = "Match \"" + match.getName() + "\". ";
		if (match.isBlocked())
			toolTip += "This match is blocked. Reason: " + match.getBlockingReason();
		else if (userOptions.getToolTipSetting() == ToolTipOption.FULL)
			toolTip += "Double click to apply the match. Select to display the match in the right panel.";
		return toolTip;
	}
}
