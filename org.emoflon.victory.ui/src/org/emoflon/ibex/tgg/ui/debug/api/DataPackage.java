package org.emoflon.ibex.tgg.ui.debug.api;

import java.util.Collection;
import java.util.List;

/**
 * Contains all the information required by Victory to display a single
 * selection step to the user.
 */
public interface DataPackage {
	public Collection<Match> getMatches();

	public List<RuleApplication> getRuleApplications();
}
