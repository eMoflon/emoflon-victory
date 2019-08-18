package org.emoflon.ibex.tgg.ui.debug.adapter.TGGAdpater;

import org.eclipse.emf.ecore.EAttribute;
import org.emoflon.ibex.tgg.ui.debug.api.IAttribute;

public class EAttributteAdapter implements IAttribute {

	private EAttribute attr;

	public EAttributteAdapter(EAttribute attr) {
		super();
		this.attr = attr;
	}

	public EAttribute unWrap(){

		return attr;

	}

	@Override
	public String getName() {

		return attr.getName();
	}

	@Override
	public String getTypeName() {

		return attr.getEType().getName();
	}



}
