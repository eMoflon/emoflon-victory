package org.emoflon.ibex.tgg.ui.debug.api;

import java.util.List;

import org.emoflon.ibex.tgg.ui.debug.api.enums.Action;
import org.emoflon.ibex.tgg.ui.debug.api.enums.Domain;

/**
 * A node used within a {@link Graph}. Provides all info necessary for
 * visualization by Victory.
 */
public interface Node {
	public String getType();

	public String getName();

	public Domain getDomain();

	public Action getAction();

	public List<String> getAttributes();
}
