package org.emoflon.ibex.tgg.ui.debug.api;


import java.util.List;


public interface IObject {
	
	String indexFor(IObject object) ;
	String labelFor(IObject object) ;
	Object getFeature(IAttribute attr);	
	List<IObject> getContents();
	String getContainingFeatureName();
	String getClassName();
	List<IAttribute> getClassAttribute();

}
