package org.emoflon.ibex.tgg.ui.debug.adapter.TGGAdpater;

import org.emoflon.ibex.tgg.ui.debug.enums.VictoryBindingType;


import language.BindingType;

public class BindingTypeAdapter {
	public static VictoryBindingType adapt(BindingType value) {
		switch (value) {
		case CONTEXT:
			return VictoryBindingType.CONTEXT;
		case CREATE:
			return VictoryBindingType.CREATE;
		case DELETE:
			return VictoryBindingType.DELETE;
		case NEGATIVE:
			return VictoryBindingType.NEGATIVE;
		}

		throw new IllegalArgumentException("Adapter doesn't handle case: " + value);
	}
}
