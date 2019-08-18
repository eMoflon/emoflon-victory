package org.emoflon.ibex.tgg.ui.debug.api;



public interface IRuleCorr extends IHasDomainAndBindingType{
	IRuleNode getSource();
	IRuleNode getTarget();
	String getTypeName();
}
