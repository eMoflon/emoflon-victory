package org.emoflon.victory.ui.views.visualisable;

import org.emoflon.victory.ui.api.DataProvider;
import org.emoflon.victory.ui.api.Rule;
import org.emoflon.victory.ui.options.IUserOptions;
import org.emoflon.victory.ui.plantuml.PlantUMLGenerator;

public class RuleVisualisation extends VisualisableElement {

	private Rule rule;
	private IUserOptions userOptions;
	private DataProvider dataProvider;

	public RuleVisualisation(Rule rule, IUserOptions userOptions, DataProvider dataProvider) {
		this.rule = rule;
		this.userOptions = userOptions;
		this.dataProvider = dataProvider;
	}

	@Override
	protected String generateVisualisationString() {
		return PlantUMLGenerator.visualise(rule.getGraph(), userOptions, dataProvider);
	}
}
