package org.emoflon.ibex.tgg.ui.debug.adapter.TGGAdpater;

import java.util.List;

import java.util.stream.Collectors;
import language.TGGRule;
import org.emoflon.ibex.tgg.ui.debug.api.IRule;
import org.emoflon.ibex.tgg.ui.debug.api.IRuleEdge;
import org.emoflon.ibex.tgg.ui.debug.api.IRuleNode;



public class RuleAdapter implements IRule {


	private TGGRule rule;
	public RuleAdapter(TGGRule rule) {
		super();
		this.rule = rule;
	}

	@Override
	public List<IRuleNode> getNodes() {

		return rule.getNodes()//
				.stream()//
				.map(m -> new RuleNodeAdapter(m))
				.collect(Collectors.toList());
	}

	@Override
	public List<IRuleEdge> getEdges() {

		return rule.getEdges()//
				.stream()//
				.map(m -> new TGGRuleEdgeAdapter(m))
				.collect(Collectors.toList());
	}


}






