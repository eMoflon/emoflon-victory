package org.emoflon.ibex.tgg.ui.debug.api;



public interface IRuleEdge extends IHasDomainAndBindingType {
	
	IRuleNode getSrcNode();
	IRuleNode getTrgNode();
	String getTypeName();
	

}
