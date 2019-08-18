package org.emoflon.ibex.tgg.ui.debug.plantuml
import java.util.Map
import java.util.Collection
import org.emoflon.ibex.tgg.ui.debug.options.IUserOptions
import org.emoflon.ibex.tgg.ui.debug.options.IBeXOp
import org.emoflon.ibex.tgg.ui.debug.api.IObject
import org.emoflon.ibex.tgg.ui.debug.enums.VictoryBindingType
import org.emoflon.ibex.tgg.ui.debug.enums.VictoryDomainType
import org.emoflon.ibex.tgg.ui.debug.api.IAttribute
import org.emoflon.ibex.tgg.ui.debug.api.IRuleCorr
import org.emoflon.ibex.tgg.ui.debug.api.IMatchVictory
import org.emoflon.ibex.tgg.ui.debug.api.IRule
import org.emoflon.ibex.tgg.ui.debug.api.IRuleNode

class VictoryPlantUMLGenerator {
	
	def static String visualiseTGGRule(IRule rule, IUserOptions userOptions) {
		'''
			@startuml
			«plantUMLPreamble»

			«visualiseRule(rule, false, true, userOptions.op)»
			
			@enduml
		'''
	}
	
	def static String visualiseMatch(IMatchVictory match, IRule rule, Collection<IObject> matchNeighborhood, IUserOptions userOptions) {
		
		// TODO implement usage of actual match neighborhood
		
		val paramToNodeMap = match.parameterNames.toInvertedMap[param | rule.nodes.findFirst[node | param === node.name]]
		val paramToNodeIdMap = paramToNodeMap.mapValues[idForNode(it)]
		val nonCorrParamToEObjectMap = match.parameterNames.filter[paramToNodeMap.get(it).domainType !== VictoryDomainType.CORR].toInvertedMap[match.get(it) as IObject]
		val eObjectMapping = nonCorrParamToEObjectMap.values.toInvertedMap[
			val id = labelFor(it) + "_" + indexFor(it)
			val label = id + " : " + it.className
			id->label
		]
		
		'''
			@startuml
			«plantUMLPreamble»
			
			«visualiseRule(rule, true, userOptions.displayFullRuleForMatches, userOptions.op)»
			
			«visualiseEObjectGraph(eObjectMapping)»
			
			«FOR String param : nonCorrParamToEObjectMap.keySet»
				«paramToNodeIdMap.get(param)» #.[#Blue]..# «eObjectMapping.get(nonCorrParamToEObjectMap.get(param)).key»
			«ENDFOR»
			
			@enduml
		'''
	}
	
	private def static String plantUMLPreamble(){
		'''
			hide empty members
			hide circle
			hide stereotype
			
			skinparam shadowing false
			
			skinparam class {
				BorderColor<<CREATE>> SpringGreen
				BorderColor<<TRANSLATE>> Gold
				BorderColor<<OTHER>> Black
				BackgroundColor<<TRG>> MistyRose
				BackgroundColor<<SRC>> LightYellow
				BackgroundColor<<CORR>> LightCyan 
				ArrowColor Black
			}	
		'''
	}
	
	private def static String visualiseRule(IRule rule, boolean groupFullRule, boolean showCreated, IBeXOp op) {
		
		val nodeGroupMap = rule.nodes.groupBy[it.domainType]
		val nodeIdMap = rule.nodes.toInvertedMap[idForNode]
		
		'''
			«IF groupFullRule»together {«ENDIF»
			
			«IF nodeGroupMap.containsKey(VictoryDomainType.SRC)»
				together {
					«FOR node : nodeGroupMap.get(VictoryDomainType.SRC)»
						«IF showCreated || node.bindingType !== VictoryBindingType.CREATE»
							«visualiseRuleNode(nodeIdMap.get(node), getColorDefinitions(node.bindingType, node.domainType, op))»
						«ENDIF»
					«ENDFOR»
				}
			«ENDIF»
			
			«IF nodeGroupMap.containsKey(VictoryDomainType.TRG)»
				together {
					«FOR node : nodeGroupMap.get(VictoryDomainType.TRG)»
						«IF showCreated || node.bindingType !== VictoryBindingType.CREATE»
							«visualiseRuleNode(nodeIdMap.get(node), getColorDefinitions(node.bindingType, node.domainType, op))»
						«ENDIF»
					«ENDFOR»
				}
			«ENDIF»
			
			«IF nodeGroupMap.containsKey(VictoryDomainType.CORR)»
				«FOR node : nodeGroupMap.get(VictoryDomainType.CORR)»
					«val corrNode = node as IRuleCorr»
					«IF showCreated || corrNode.bindingType !== VictoryBindingType.CREATE» 
						«visualiseRuleCorrEdge(nodeIdMap.get(corrNode.source), nodeIdMap.get(corrNode.target), corrNode.typeName, corrNode.bindingType === VictoryBindingType.CREATE)»
					«ENDIF»
				«ENDFOR»
			«ENDIF»
			
			«FOR edge : rule.edges»
				«IF edge.domainType !== VictoryDomainType.CORR && (showCreated || edge.bindingType !== VictoryBindingType.CREATE)»
					«visualiseRuleEdge(nodeIdMap.get(edge.srcNode), nodeIdMap.get(edge.trgNode), edge.typeName, edge.bindingType === VictoryBindingType.CREATE)»
				«ENDIF»
			«ENDFOR»
			
			«IF groupFullRule»}«ENDIF»
		'''
	}

	
	private def static String visualiseEObjectGraph(Map<IObject, Pair<String, String>> eObjectMapping) {
		'''
		together {
			«FOR object: eObjectMapping.keySet»
				object "«eObjectMapping.get(object).value»" as «eObjectMapping.get(object).key» <<BLACK>> <<SRC>> {
					«FOR IAttribute attr : object.classAttribute»
						«attr.typeName» «attr.name» «object.getFeature(attr)»
					«ENDFOR»
				}
				
				«FOR contentObject : object.contents»
					«IF eObjectMapping.containsKey(contentObject)»
						«eObjectMapping.get(object).key» --> «eObjectMapping.get(contentObject).key» : «contentObject.containingFeatureName»
					«ENDIF»
				«ENDFOR»
			«ENDFOR»
		}
		'''
	}
	
	
	private def static String visualiseRuleNode(String ruleId, String colorDefinitions) {
		'''class «ruleId» «colorDefinitions»'''
	}

	private def static String visualiseRuleEdge(String srcNodeId, String trgNodeId, String edgeId, boolean bindingTypeCreate) {
		'''«srcNodeId» -«IF (bindingTypeCreate)»[#SpringGreen]«ENDIF»-> «trgNodeId» : "«edgeId»"'''
	}
	
	private def static String visualiseRuleCorrEdge(String srcNodeId, String trgNodeId, String edgeId, boolean bindingTypeCreate) {
		'''«srcNodeId» ...«IF (bindingTypeCreate)»[#SpringGreen]«ENDIF» «trgNodeId» : "«edgeId»"'''
	}
	
	private def static String idForNode(IRuleNode node) {
		'''"«node.name» : «node.typeName»"'''
	}
	
	private def static String getColorDefinitions(VictoryBindingType binding, VictoryDomainType domain, IBeXOp op) {
		
		var bindingColour = "OTHER"
		if(binding === VictoryBindingType.CREATE)
			if((op === IBeXOp.INITIAL_FWD && domain === VictoryDomainType.SRC)
				|| (op === IBeXOp.INITIAL_BWD && domain === VictoryDomainType.TRG))
					bindingColour = "TRANSLATE"
			else
				bindingColour = "CREATE"
		
		'''<<«bindingColour»>> <<«domain»>>'''
	}

}