package org.emoflon.victory.ui.views.visualisable;

import java.util.Collection;

import org.emoflon.victory.ui.api.DataProvider;
import org.emoflon.victory.ui.api.Graph;
import org.emoflon.victory.ui.api.RuleApplication;
import org.emoflon.victory.ui.options.IUserOptions;
import org.emoflon.victory.ui.plantuml.PlantUMLGenerator;

public class RuleApplicationVisualisation extends VisualisableElement {

	private Collection<RuleApplication> ruleApplications;
	private IUserOptions userOptions;
	private DataProvider dataProvider;

	public RuleApplicationVisualisation(Collection<RuleApplication> ruleApplications, IUserOptions userOptions,
			DataProvider dataProvider) {
		this.ruleApplications = ruleApplications;
		this.userOptions = userOptions;
		this.dataProvider = dataProvider;
	}

	@Override
	protected String generateVisualisationString() {
		Graph[] graph = new Graph[1];
		ruleApplications.stream().findAny().ifPresent(ruleApplication -> graph[0] = ruleApplication.getMerger()
				.getMergedGraph(ruleApplications, userOptions.getNeighborhoodSize()));
		return PlantUMLGenerator.visualise(graph[0], userOptions, dataProvider);
	}

}
