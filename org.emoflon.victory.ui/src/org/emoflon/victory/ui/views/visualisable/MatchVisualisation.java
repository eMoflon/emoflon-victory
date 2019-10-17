package org.emoflon.victory.ui.views.visualisable;

import java.util.Collection;
import java.util.HashSet;

import org.emoflon.victory.ui.api.DataProvider;
import org.emoflon.victory.ui.api.Graph;
import org.emoflon.victory.ui.api.Match;
import org.emoflon.victory.ui.options.IUserOptions;
import org.emoflon.victory.ui.plantuml.PlantUMLGenerator;

public class MatchVisualisation extends VisualisableElement {

	private Match match;
	private IUserOptions userOptions;
	private DataProvider dataProvider;

	public MatchVisualisation(Match match, IUserOptions userOptions, DataProvider dataProvider) {
		this.match = match;
		this.userOptions = userOptions;
		this.dataProvider = dataProvider;
	}

	@Override
	protected String generateVisualisationString() {
		Collection<Graph> graphs = new HashSet<>();
		graphs.add(match.getGraph(userOptions.getNeighborhoodSize()));
		graphs.add(match.getRule().getGraph());
		return PlantUMLGenerator.visualise(graphs, userOptions, dataProvider);
	}
}
