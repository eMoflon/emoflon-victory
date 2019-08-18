package org.emoflon.ibex.tgg.ui.debug.api;

import java.util.List;

public interface IRule {
	
 List<IRuleNode> getNodes();
 List<IRuleEdge> getEdges();
 
}
