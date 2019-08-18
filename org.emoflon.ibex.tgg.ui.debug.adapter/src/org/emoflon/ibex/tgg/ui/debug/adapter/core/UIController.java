package org.emoflon.ibex.tgg.ui.debug.adapter.core;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.swt.widgets.Display;
import org.emoflon.ibex.tgg.operational.matches.IMatch;
import org.emoflon.ibex.tgg.operational.monitoring.IbexController;
import org.emoflon.ibex.tgg.ui.debug.views.MatchListView;
import org.emoflon.ibex.tgg.ui.debug.adapter.TGGAdpater.MatchAdapter;
import org.emoflon.ibex.tgg.ui.debug.api.IVictoryController;
import org.emoflon.ibex.tgg.ui.debug.core.IbexDebugUI;


public class UIController extends IbexController implements IVictoryController {

	private MatchListView matchListView; // TODO convert to interface

	@Override
	public void init(MatchListView matchListView) {
		this.matchListView = matchListView;
	}

	@Override
	public void update(ObservableEvent eventType, Object... additionalInformation) {
		// CONCURRENCY: Ibex thread only

		// TODO implement
	}

	@Override
	public IMatch chooseOneMatch(Map<IMatch, Collection<IMatch>> matches) {
		// CONCURRENCY: Ibex thread only

		synchronized (this) {
		    while (IbexDebugUI.getDisplay() == null)
			try {
				// specific wait time for testing POC
			    wait(1000); 
			} catch (InterruptedException pIE) {
			    // TODO calling thread was interrupted. What now..?
			}
		}
		
		Display disp = IbexDebugUI.getDisplay();

		disp.syncExec(() -> {
			matchListView.populate(matches.keySet().stream()//
				.map(m -> new MatchAdapter(m))
				.collect(Collectors.toList()));
		});



		return  ((MatchAdapter) matchListView.getChosenMatch()).unWrap();
	}

	@Override
	protected int getRequestedMatchCount() {

		// TODO implement actual user-specified setting

		return 100;
	}

}
