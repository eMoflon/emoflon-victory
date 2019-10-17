package org.emoflon.victory.ui.views;

import java.util.Collection;

import org.emoflon.victory.ui.api.Match;
import org.emoflon.victory.ui.api.Rule;
import org.emoflon.victory.ui.api.RuleApplication;

public interface IVisualiser {
	public void display(Rule rule);

	public void display(Match match);

	public void display(Collection<RuleApplication> ruleApplications);

	public void refresh();
}
