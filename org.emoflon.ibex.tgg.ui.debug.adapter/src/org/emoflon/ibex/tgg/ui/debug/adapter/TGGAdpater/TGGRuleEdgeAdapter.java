package org.emoflon.ibex.tgg.ui.debug.adapter.TGGAdpater;

import org.emoflon.ibex.tgg.ui.debug.api.IRuleEdge;
import org.emoflon.ibex.tgg.ui.debug.api.IRuleNode;
import org.emoflon.ibex.tgg.ui.debug.enums.VictoryBindingType;
import org.emoflon.ibex.tgg.ui.debug.enums.VictoryDomainType;
import language.TGGRuleEdge;


public class TGGRuleEdgeAdapter implements IRuleEdge {

	private TGGRuleEdge edge;

	public TGGRuleEdgeAdapter(TGGRuleEdge edge) {
		this.edge = edge;

	}

	@Override
	public IRuleNode getSrcNode() {

		return new RuleNodeAdapter(edge.getSrcNode());
	}

	@Override
	public IRuleNode getTrgNode() {

		return new RuleNodeAdapter(edge.getTrgNode());
	}

	@Override
	public VictoryDomainType getDomainType() {
		return DomainTypeAdapter.adapt(edge.getDomainType());
	}

	@Override
	public VictoryBindingType getBindingType() {

		return BindingTypeAdapter.adapt(edge.getBindingType());
	}

	@Override
	public String getTypeName() {

		return edge.getType().getName();
	}
}
