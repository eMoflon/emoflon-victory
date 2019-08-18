package org.emoflon.ibex.tgg.ui.debug.views;

//import org.emoflon.ibex.tgg.operational.matches.IMatch;
import org.emoflon.ibex.tgg.ui.debug.api.IMatchVictory;

public interface IVisualiser {
	/**
	 * Called to request the visualisation of a TGG rule.
	 * 
	 * @param pRuleName
	 *            the name of the rule to be visualised
	 */
	public default void display(String pRuleName) {
		// don't do anything
	}

	/**
	 * Called to request the visualisation of a match.
	 * 
	 * @param pMatch
	 *            the match to be visualised
	 */
	public default void display(IMatchVictory pMatch) {
		// don't do anything
	}

	/**
	 * Called to force a refresh of the currently visualised element.
	 */
	public default void refresh() {
		// don't do anything
	}
}
