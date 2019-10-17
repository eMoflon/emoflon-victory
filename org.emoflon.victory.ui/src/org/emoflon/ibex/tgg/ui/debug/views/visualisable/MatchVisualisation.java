package org.emoflon.ibex.tgg.ui.debug.views.visualisable;

import java.util.Collection;
import java.util.HashSet;

import org.emoflon.ibex.tgg.ui.debug.api.DataProvider;
import org.emoflon.ibex.tgg.ui.debug.api.Graph;
import org.emoflon.ibex.tgg.ui.debug.api.Match;
import org.emoflon.ibex.tgg.ui.debug.options.IUserOptions;
import org.emoflon.ibex.tgg.ui.debug.plantuml.PlantUMLGenerator;

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
