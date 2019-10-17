package org.emoflon.ibex.tgg.ui.debug.plantuml

import org.emoflon.ibex.tgg.ui.debug.options.UserOptionsManager.VisualizationLabelOptions
import org.apache.commons.lang3.StringUtils
import java.util.Map
import org.emoflon.ibex.tgg.ui.debug.api.Node
import org.emoflon.ibex.tgg.ui.debug.options.IUserOptions
import java.util.ArrayList
import org.emoflon.ibex.tgg.ui.debug.api.enums.Domain
import org.emoflon.ibex.tgg.ui.debug.api.enums.Action
import org.emoflon.ibex.tgg.ui.debug.api.Graph
import org.emoflon.ibex.tgg.ui.debug.api.DataProvider
import org.emoflon.ibex.tgg.ui.debug.api.Edge
import org.emoflon.ibex.tgg.ui.debug.api.enums.EdgeType

class PlantUMLGenerator {
	static val createColor = "SpringGreen"
	static val contextColor = "Black"
	static val translateColor = "Gainsboro"
	static val srcColor = "LightYellow"
	static val trgColor = "MistyRose"
	static val corrColor = "LightCyan"
	static val objectMappingColor = "Blue"
	static val edgeArrow = "-->"
	static val objectMappingArrow = "..."
	static val corrEdgeArrow = "..."
	static val plantUMLPreamble = '''
		hide empty members
		hide circle
		hide stereotype
		
		skinparam shadowing false
		
		skinparam class {
			BorderColor<<CREATE>> «createColor»
			BorderColor<<TRANSLATE>> «translateColor»
			BorderColor<<OTHER>> «contextColor»
			BackgroundColor<<TRG>> «trgColor»
			BackgroundColor<<SRC>> «srcColor»
			BackgroundColor<<CORR>> «corrColor»
			ArrowColor «contextColor»
		}
		
		skinparam object {
			BorderColor «contextColor»
			BackgroundColor<<TRG>> «trgColor»
			BackgroundColor<<SRC>> «srcColor»
			BackgroundColor<<CORR>> «corrColor» 
			ArrowColor «contextColor»
		}
	'''

	def static String visualise(Iterable<Graph> graphs, IUserOptions userOptions, DataProvider dataProvider) {
		val nodesToIDsGlobal = graphs.flatMap[g|g.nodes].toInvertedMap[n|n.idForNode]

		// Graph elements to visualize
		val corrEdges = graphs.toInvertedMap[g|g.getVisibleCorrEdges(userOptions)]
		val srcNodes = graphs.toInvertedMap[g|g.getVisibleNodes(Domain.SRC, userOptions, corrEdges.get(g))]
		val trgNodes = graphs.toInvertedMap[g|g.getVisibleNodes(Domain.TRG, userOptions, corrEdges.get(g))]
		val normalEdges = graphs.toInvertedMap[g|g.getVisibleEdges(srcNodes.get(g) + trgNodes.get(g), userOptions)]
		// Graph collection wide elements
		val matchEdges = graphs.flatMap[g|g.edges].getMatchEdges(srcNodes.values.flatten + trgNodes.values.flatten,
			userOptions)

		val header = '''
			center header
			«IF nodesToIDsGlobal.empty»
				The graph does not contain any nodes.
			«ENDIF»
			«IF !nodesToIDsGlobal.keySet.exists[n | n.action !== Action.CREATE]»
				The graph does not contain any context nodes.
			«ENDIF»
			«IF !nodesToIDsGlobal.empty && srcNodes.values.flatten.empty && trgNodes.values.flatten.empty»
				All elements of the graph are hidden according to visualization options.
			«ENDIF»
				
			endheader
		'''
		'''
			@startuml
			«plantUMLPreamble»
			«header»
			«FOR graph : graphs»
				«visualise(nodesToIDsGlobal, srcNodes.get(graph), trgNodes.get(graph), corrEdges.get(graph), normalEdges.get(graph), userOptions)»
			«ENDFOR»
			«matchEdges.visualizeEdges(nodesToIDsGlobal, userOptions.edgeLabelVisualization)»
			«footerUserOptionsInformation(userOptions)»
			@enduml
		'''
	}

	def static String visualise(Graph graph, IUserOptions userOptions, DataProvider dataProvider) {
		val nodesToIDs = graph.nodes.toInvertedMap[n|n.idForNode]

		val corrEdges = graph.getVisibleCorrEdges(userOptions)
		val srcNodes = graph.getVisibleNodes(Domain.SRC, userOptions, corrEdges)
		val trgNodes = graph.getVisibleNodes(Domain.TRG, userOptions, corrEdges)
		val normalEdges = graph.getVisibleEdges(srcNodes + trgNodes, userOptions)
		val matchEdges = graph.getMatchEdges(srcNodes + trgNodes, userOptions)

		val header = '''
			center header
			«IF graph.nodes.empty»
				The graph does not contain any nodes.
			«ENDIF»
			«IF !graph.nodes.exists[n | n.action !== Action.CREATE]»
				The graph does not contain any context nodes.
			«ENDIF»
			«IF !graph.nodes.empty && srcNodes.empty && trgNodes.empty»
				All elements of the graph are hidden according to visualization options.
			«ENDIF»
				
			endheader
		'''
		'''
			@startuml
			«plantUMLPreamble»
			«header»
			«visualise(nodesToIDs, srcNodes, trgNodes, corrEdges, normalEdges, userOptions)»
			«matchEdges.visualizeEdges(nodesToIDs, userOptions.edgeLabelVisualization)»
			«footerUserOptionsInformation(userOptions)»
			@enduml
		'''
	}

	private def static String visualise(Map<Node, String> nodesToIDs, Iterable<Node> srcNodes, Iterable<Node> trgNodes,
		Iterable<Edge> corrEdges, Iterable<Edge> normalEdges, IUserOptions userOptions) {
		'''
			together {
				«srcNodes.toInvertedMap[n | nodesToIDs.get(n)].visualizeNodeGroup(userOptions.nodeLabelVisualization)»
				«trgNodes.toInvertedMap[n | nodesToIDs.get(n)].visualizeNodeGroup(userOptions.nodeLabelVisualization)»
				«normalEdges.visualizeEdges(nodesToIDs, userOptions.edgeLabelVisualization)»
				«corrEdges.visualizeEdges(nodesToIDs, userOptions.corrLabelVisualization)»
			}
		'''
	}

	private def static String visualizeNodeGroup(Map<Node, String> nodesToIDs,
		VisualizationLabelOptions nodeLabelOptions) {
		'''
			together {
				«FOR node : nodesToIDs.keySet»
					«node.visualizeNode(nodesToIDs.get(node), nodeLabelOptions)»
				«ENDFOR»
			}
		'''
	}

	private def static String visualizeNode(Node n, String id, VisualizationLabelOptions nodeLabelOptions) {
		val stereotypes = n.getStereotypes()
		'''
			class "«n.getNodeLabel(nodeLabelOptions)»" as «id»«FOR stereotype : stereotypes» <<«stereotype»>>«ENDFOR» {
				«FOR attribute : n.attributes»
					«attribute»
				«ENDFOR»
			}
		'''
	}

	private def static String visualizeEdges(Iterable<Edge> edges, Map<Node, String> nodesToIDs,
		VisualizationLabelOptions edgeLabelOptions) {
		'''
			«FOR edge : edges»
				«edge.visualizeEdge(nodesToIDs, edgeLabelOptions)»
			«ENDFOR»
		'''
	}

	private def static String visualizeEdge(Edge e, Map<Node, String> nodesToIDs,
		VisualizationLabelOptions edgeLabelOptions) {
		val color = if (e.type == EdgeType.MATCH)
				objectMappingColor
			else
				switch (e.action) {
					case Action.CREATE: createColor
					case Action.CONTEXT: contextColor
					case Action.TRANSLATE: translateColor
				}

		val arrow = switch (e.type) {
			case EdgeType.CORR: corrEdgeArrow
			case EdgeType.MATCH: objectMappingArrow
			case EdgeType.NORMAL: edgeArrow
		}

		val label = e.getEdgeLabel(edgeLabelOptions)

		val arrowWithColor = '''«arrow.charAt(0)»[#«color»]«arrow.substring(1)»'''
		'''«nodesToIDs.get(e.srcNode)» «arrowWithColor» «nodesToIDs.get(e.trgNode)» : "«label»"'''
	}

	private def static String[] getStereotypes(Node n) {
		val stereotypes = new ArrayList<String>();
		stereotypes.add(switch (n.domain) {
			case Domain.SRC: "SRC"
			case Domain.TRG: "TRG"
		})
		stereotypes.add(switch (n.action) {
			case Action.CREATE: "CREATE"
			case Action.TRANSLATE: "TRANSLATE"
			case Action.CONTEXT: "OTHER"
		})
		return stereotypes
	}

	private def static String getAbbreviatedName(String name, VisualizationLabelOptions labelOptions) {
		switch (labelOptions) {
			case FULLNAME: return '''«name»'''
			case ABBREVIATED: return '''«StringUtils.abbreviateMiddle(name, "...", 10)»'''
			case NONE: return ''''''
		}
	}

	private def static String getNodeLabel(Node n, VisualizationLabelOptions labelOptions) {
		if (labelOptions == VisualizationLabelOptions.NONE) {
			'''«getAbbreviatedName(n.name, labelOptions)» : «getAbbreviatedName(n.type, VisualizationLabelOptions.ABBREVIATED)»'''
		} else {
			'''«getAbbreviatedName(n.name, labelOptions)» : «getAbbreviatedName(n.type, labelOptions)»'''
		}
	}

	private def static String getEdgeLabel(Edge e, VisualizationLabelOptions labelOptions) {
		if (labelOptions == VisualizationLabelOptions.NONE) {
			''''''
		} else {
			'''«getAbbreviatedName(e.label, labelOptions)»'''
		}
	}

	private def static String idForNode(Node node) {
		'''«node.type»_«java.util.UUID.randomUUID().toString().replace("-","_")»'''
	}

	private def static String footerUserOptionsInformation(IUserOptions userOptions) {
		'''
			center footer
			«IF !userOptions.displayFullRuleForMatches»
				Created elements are hidden.
			«ELSE»
				Created elements are shown.
			«ENDIF»
			«IF !userOptions.displaySrcContextForMatches»
				SRC elements are hidden.
			«ELSE»
				SRC elements are shown.
			«ENDIF»
			«IF !userOptions.displayTrgContextForMatches»
				TRG elements are hidden.
			«ELSE»
				TRG elements are shown.
			«ENDIF»
			«IF !userOptions.displayCorrContextForMatches»
				CORR elements are hidden.
			«ELSE»
				CORR elements are shown.
			«ENDIF»
			endfooter
		'''
	}

	private def static boolean shallBeDisplayed(Node n, IUserOptions userOptions) {
		if (n.domain === Domain.SRC && !userOptions.displaySrcContextForMatches)
			return false;
		if (n.domain === Domain.TRG && !userOptions.displayTrgContextForMatches)
			return false;
		if (n.action === Action.CREATE && !userOptions.displayFullRuleForMatches)
			return false;
		return true;
	}

	private def static boolean shallBeDisplayed(Edge e, IUserOptions userOptions) {
		if (e.type === EdgeType.CORR && !userOptions.displayCorrContextForMatches)
			return false;
		if (e.srcNode.domain === Domain.SRC && e.trgNode.domain === Domain.SRC &&
			!userOptions.displaySrcContextForMatches)
			return false;
		if (e.srcNode.domain === Domain.TRG && e.trgNode.domain === Domain.TRG &&
			!userOptions.displayTrgContextForMatches)
			return false;
		if (e.action === Action.CREATE && !userOptions.displayFullRuleForMatches)
			return false;
		return true;
	}

	private def static boolean isContainedIn(Node n, Iterable<Edge> edges) {
		return edges.exists[e|e.srcNode === n || e.trgNode === n]
	}

	private def static boolean isContainedIn(Edge e, Iterable<Node> nodes) {
		return nodes.exists[n|n === e.srcNode] && nodes.exists[n|n === e.trgNode]
	}

	private def static Iterable<Node> getVisibleNodes(Graph graph, Domain domain, IUserOptions userOptions,
		Iterable<Edge> corrEdges) {
		return graph.nodes.filter [ n |
			n.domain === domain && (n.shallBeDisplayed(userOptions) || n.isContainedIn(corrEdges))
		]
	}

	private def static Iterable<Edge> getVisibleEdges(Graph graph, Iterable<Node> visibleNodes,
		IUserOptions userOptions) {
		graph.edges.filter [ e |
			e.type === EdgeType.NORMAL && e.shallBeDisplayed(userOptions) && e.isContainedIn(visibleNodes)
		]
	}

	private def static Iterable<Edge> getVisibleCorrEdges(Graph graph, IUserOptions userOptions) {
		return graph.edges.filter[e|e.type === EdgeType.CORR && e.shallBeDisplayed(userOptions)]
	}

	private def static Iterable<Edge> getMatchEdges(Graph graph, Iterable<Node> visibleNodes,
		IUserOptions userOptions) {
		return graph.edges.getMatchEdges(visibleNodes, userOptions)
	}

	private def static Iterable<Edge> getMatchEdges(Iterable<Edge> allEdges, Iterable<Node> visibleNodes,
		IUserOptions userOptions) {
		return allEdges.filter [ e |
			e.type === EdgeType.MATCH && e.shallBeDisplayed(userOptions) && (e.isContainedIn(visibleNodes))
		]
	}

}
