package org.emoflon.ibex.tgg.ui.debug.api;

import java.io.IOException;
import java.util.Collection;

/**
 * Provides additional information and functionality to Victory that is not part
 * of the regular match selection workflow.
 */
public interface DataProvider {
	public Collection<Rule> getAllRules();

	public void saveModels(String[] saveLocations) throws IOException;

	public String[][] getDefaultSaveData();
}
