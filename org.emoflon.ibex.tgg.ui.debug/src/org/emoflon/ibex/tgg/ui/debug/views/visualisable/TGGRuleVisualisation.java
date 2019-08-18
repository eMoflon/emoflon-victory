package org.emoflon.ibex.tgg.ui.debug.views.visualisable;

import org.emoflon.ibex.tgg.ui.debug.api.IRule;
import org.emoflon.ibex.tgg.ui.debug.options.IUserOptions;
import org.emoflon.ibex.tgg.ui.debug.plantuml.VictoryPlantUMLGenerator;

//import language.TGGRule;

public class TGGRuleVisualisation extends VisualisableElement {

    private IRule rule;
    private IUserOptions userOptions;

    public TGGRuleVisualisation(IRule iRuleAdapter, IUserOptions pUserOptions) {
	if (iRuleAdapter == null)
	    throw new IllegalArgumentException("Unknown rule");

	rule = iRuleAdapter;

	userOptions = pUserOptions;
    }

    @Override
    protected String generateVisualisationString() {
	return VictoryPlantUMLGenerator.visualiseTGGRule(rule, userOptions);
    }
}
