package org.emoflon.victory.ui.api;

import org.emoflon.victory.ui.api.enums.Action;
import org.emoflon.victory.ui.api.enums.EdgeType;

/**
 * A edge used within a {@link Graph}. Provides all info necessary for
 * visualization by Victory.
 */
public interface Edge {
	public String getLabel();

	public Node getSrcNode();

	public Node getTrgNode();

	public EdgeType getType();

	public Action getAction();
}