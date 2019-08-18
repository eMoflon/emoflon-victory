package org.emoflon.ibex.tgg.ui.debug.api;

import java.util.Collection;


public interface IMatchVictory {

	String getRuleName() ;
	Collection<String> getParameterNames();
	Object get(String name);
	String getPatternName();

}
