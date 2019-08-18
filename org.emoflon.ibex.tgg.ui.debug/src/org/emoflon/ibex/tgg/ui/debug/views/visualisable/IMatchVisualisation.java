package org.emoflon.ibex.tgg.ui.debug.views.visualisable;

import java.util.Collection;

//import org.eclipse.emf.ecore.EObject;
//import org.emoflon.ibex.tgg.operational.matches.IMatch;
import org.emoflon.ibex.tgg.ui.debug.api.IMatchVictory;
import org.emoflon.ibex.tgg.ui.debug.api.IRule;
import org.emoflon.ibex.tgg.ui.debug.api.IVictoryDataProvider;
import org.emoflon.ibex.tgg.ui.debug.api.IObject;
import org.emoflon.ibex.tgg.ui.debug.options.IUserOptions;
import org.emoflon.ibex.tgg.ui.debug.plantuml.VictoryPlantUMLGenerator;

//import language.TGGRule;

public class IMatchVisualisation extends VisualisableElement {

    private IMatchVictory match;
    private IVictoryDataProvider dataProvider;
    private IUserOptions userOptions;

    public IMatchVisualisation(IMatchVictory pMatch, IVictoryDataProvider pDataProvider, IUserOptions pUserOptions) {
	match = pMatch;
	dataProvider = pDataProvider;
	userOptions = pUserOptions;
    }

    @Override
    protected String generateVisualisationString() {
    IRule rule = dataProvider.getRule(match.getRuleName());
	if (rule == null)
	    throw new IllegalArgumentException("Unknown rule");

	Collection<IObject> matchNeighborhood = dataProvider.getMatchNeighbourhood(match, 1);

	return VictoryPlantUMLGenerator.visualiseMatch(match, rule, matchNeighborhood, userOptions);
    }

}
