package org.emoflon.ibex.tgg.ui.debug.adapter.TGGAdpater;

import org.emoflon.ibex.tgg.ui.debug.enums.VictoryDomainType;

import language.DomainType;

public class DomainTypeAdapter {
	public static VictoryDomainType adapt(DomainType value) {
		switch (value) {
		case TRG:
			return VictoryDomainType.TRG;
		case SRC:
			return VictoryDomainType.SRC;
		case CORR:
			return VictoryDomainType.CORR;
		}

		throw new IllegalArgumentException("Adapter doesn't handle case: " + value);
	}
}
