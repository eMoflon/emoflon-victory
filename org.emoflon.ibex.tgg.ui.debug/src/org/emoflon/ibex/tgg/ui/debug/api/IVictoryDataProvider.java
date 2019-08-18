package org.emoflon.ibex.tgg.ui.debug.api;

import java.io.IOException;
import java.util.Set;

public interface IVictoryDataProvider {
	
    public IRule getRule(String pRuleName); 
    public Set<IMatchVictory> getMatches();
    public Set<IMatchVictory> getMatches(IMatchVictory match);
	public Set<IMatchVictory> getMatches(String pRuleName);
    public Set<IObject> getMatchNeighbourhood(IMatchVictory match, int k);
    abstract public void saveModels() throws IOException;
    
}
