package org.emoflon.ibex.tgg.ui.debug.views;

import java.util.Collection;

import org.emoflon.ibex.tgg.ui.debug.api.Match;
import org.emoflon.ibex.tgg.ui.debug.api.Rule;
import org.emoflon.ibex.tgg.ui.debug.api.RuleApplication;

public interface IVisualiser {
	public void display(Rule rule);

	public void display(Match match);

	public void display(Collection<RuleApplication> ruleApplications);

	public void refresh();
}
