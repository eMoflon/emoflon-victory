package org.emoflon.ibex.tgg.ui.debug.plantuml;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.MapExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.emoflon.ibex.tgg.ui.debug.api.IAttribute;
import org.emoflon.ibex.tgg.ui.debug.api.IMatchVictory;
import org.emoflon.ibex.tgg.ui.debug.api.IObject;
import org.emoflon.ibex.tgg.ui.debug.api.IRule;
import org.emoflon.ibex.tgg.ui.debug.api.IRuleCorr;
import org.emoflon.ibex.tgg.ui.debug.api.IRuleEdge;
import org.emoflon.ibex.tgg.ui.debug.api.IRuleNode;
import org.emoflon.ibex.tgg.ui.debug.enums.VictoryBindingType;
import org.emoflon.ibex.tgg.ui.debug.enums.VictoryDomainType;
import org.emoflon.ibex.tgg.ui.debug.options.IBeXOp;
import org.emoflon.ibex.tgg.ui.debug.options.IUserOptions;

@SuppressWarnings("all")
public class VictoryPlantUMLGenerator {
  public static String visualiseTGGRule(final IRule rule, final IUserOptions userOptions) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("@startuml");
    _builder.newLine();
    String _plantUMLPreamble = VictoryPlantUMLGenerator.plantUMLPreamble();
    _builder.append(_plantUMLPreamble);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    String _visualiseRule = VictoryPlantUMLGenerator.visualiseRule(rule, false, true, userOptions.getOp());
    _builder.append(_visualiseRule);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("@enduml");
    _builder.newLine();
    return _builder.toString();
  }
  
  public static String visualiseMatch(final IMatchVictory match, final IRule rule, final Collection<IObject> matchNeighborhood, final IUserOptions userOptions) {
    String _xblockexpression = null;
    {
      final Function1<String, IRuleNode> _function = (String param) -> {
        final Function1<IRuleNode, Boolean> _function_1 = (IRuleNode node) -> {
          String _name = node.getName();
          return Boolean.valueOf((param == _name));
        };
        return IterableExtensions.<IRuleNode>findFirst(rule.getNodes(), _function_1);
      };
      final Map<String, IRuleNode> paramToNodeMap = IterableExtensions.<String, IRuleNode>toInvertedMap(match.getParameterNames(), _function);
      final Function1<IRuleNode, String> _function_1 = (IRuleNode it) -> {
        return VictoryPlantUMLGenerator.idForNode(it);
      };
      final Map<String, String> paramToNodeIdMap = MapExtensions.<String, IRuleNode, String>mapValues(paramToNodeMap, _function_1);
      final Function1<String, Boolean> _function_2 = (String it) -> {
        VictoryDomainType _domainType = paramToNodeMap.get(it).getDomainType();
        return Boolean.valueOf((_domainType != VictoryDomainType.CORR));
      };
      final Function1<String, IObject> _function_3 = (String it) -> {
        Object _get = match.get(it);
        return ((IObject) _get);
      };
      final Map<String, IObject> nonCorrParamToEObjectMap = IterableExtensions.<String, IObject>toInvertedMap(IterableExtensions.<String>filter(match.getParameterNames(), _function_2), _function_3);
      final Function1<IObject, Pair<String, String>> _function_4 = (IObject it) -> {
        Pair<String, String> _xblockexpression_1 = null;
        {
          String _labelFor = it.labelFor(it);
          String _plus = (_labelFor + "_");
          String _indexFor = it.indexFor(it);
          final String id = (_plus + _indexFor);
          String _className = it.getClassName();
          final String label = ((id + " : ") + _className);
          _xblockexpression_1 = Pair.<String, String>of(id, label);
        }
        return _xblockexpression_1;
      };
      final Map<IObject, Pair<String, String>> eObjectMapping = IterableExtensions.<IObject, Pair<String, String>>toInvertedMap(nonCorrParamToEObjectMap.values(), _function_4);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("@startuml");
      _builder.newLine();
      String _plantUMLPreamble = VictoryPlantUMLGenerator.plantUMLPreamble();
      _builder.append(_plantUMLPreamble);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _visualiseRule = VictoryPlantUMLGenerator.visualiseRule(rule, true, userOptions.displayFullRuleForMatches(), userOptions.getOp());
      _builder.append(_visualiseRule);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _visualiseEObjectGraph = VictoryPlantUMLGenerator.visualiseEObjectGraph(eObjectMapping);
      _builder.append(_visualiseEObjectGraph);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      {
        Set<String> _keySet = nonCorrParamToEObjectMap.keySet();
        for(final String param : _keySet) {
          String _get = paramToNodeIdMap.get(param);
          _builder.append(_get);
          _builder.append(" #.[#Blue]..# ");
          String _key = eObjectMapping.get(nonCorrParamToEObjectMap.get(param)).getKey();
          _builder.append(_key);
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.newLine();
      _builder.append("@enduml");
      _builder.newLine();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  private static String plantUMLPreamble() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("hide empty members");
    _builder.newLine();
    _builder.append("hide circle");
    _builder.newLine();
    _builder.append("hide stereotype");
    _builder.newLine();
    _builder.newLine();
    _builder.append("skinparam shadowing false");
    _builder.newLine();
    _builder.newLine();
    _builder.append("skinparam class {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("BorderColor<<CREATE>> SpringGreen");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("BorderColor<<TRANSLATE>> Gold");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("BorderColor<<OTHER>> Black");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("BackgroundColor<<TRG>> MistyRose");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("BackgroundColor<<SRC>> LightYellow");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("BackgroundColor<<CORR>> LightCyan ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("ArrowColor Black");
    _builder.newLine();
    _builder.append("}\t");
    _builder.newLine();
    return _builder.toString();
  }
  
  private static String visualiseRule(final IRule rule, final boolean groupFullRule, final boolean showCreated, final IBeXOp op) {
    String _xblockexpression = null;
    {
      final Function1<IRuleNode, VictoryDomainType> _function = (IRuleNode it) -> {
        return it.getDomainType();
      };
      final Map<VictoryDomainType, List<IRuleNode>> nodeGroupMap = IterableExtensions.<VictoryDomainType, IRuleNode>groupBy(rule.getNodes(), _function);
      final Function1<IRuleNode, String> _function_1 = (IRuleNode it) -> {
        return VictoryPlantUMLGenerator.idForNode(it);
      };
      final Map<IRuleNode, String> nodeIdMap = IterableExtensions.<IRuleNode, String>toInvertedMap(rule.getNodes(), _function_1);
      StringConcatenation _builder = new StringConcatenation();
      {
        if (groupFullRule) {
          _builder.append("together {");
        }
      }
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      {
        boolean _containsKey = nodeGroupMap.containsKey(VictoryDomainType.SRC);
        if (_containsKey) {
          _builder.append("together {");
          _builder.newLine();
          {
            List<IRuleNode> _get = nodeGroupMap.get(VictoryDomainType.SRC);
            for(final IRuleNode node : _get) {
              {
                if ((showCreated || (node.getBindingType() != VictoryBindingType.CREATE))) {
                  _builder.append("\t");
                  String _visualiseRuleNode = VictoryPlantUMLGenerator.visualiseRuleNode(nodeIdMap.get(node), VictoryPlantUMLGenerator.getColorDefinitions(node.getBindingType(), node.getDomainType(), op));
                  _builder.append(_visualiseRuleNode, "\t");
                  _builder.newLineIfNotEmpty();
                }
              }
            }
          }
          _builder.append("}");
          _builder.newLine();
        }
      }
      _builder.newLine();
      {
        boolean _containsKey_1 = nodeGroupMap.containsKey(VictoryDomainType.TRG);
        if (_containsKey_1) {
          _builder.append("together {");
          _builder.newLine();
          {
            List<IRuleNode> _get_1 = nodeGroupMap.get(VictoryDomainType.TRG);
            for(final IRuleNode node_1 : _get_1) {
              {
                if ((showCreated || (node_1.getBindingType() != VictoryBindingType.CREATE))) {
                  _builder.append("\t");
                  String _visualiseRuleNode_1 = VictoryPlantUMLGenerator.visualiseRuleNode(nodeIdMap.get(node_1), VictoryPlantUMLGenerator.getColorDefinitions(node_1.getBindingType(), node_1.getDomainType(), op));
                  _builder.append(_visualiseRuleNode_1, "\t");
                  _builder.newLineIfNotEmpty();
                }
              }
            }
          }
          _builder.append("}");
          _builder.newLine();
        }
      }
      _builder.newLine();
      {
        boolean _containsKey_2 = nodeGroupMap.containsKey(VictoryDomainType.CORR);
        if (_containsKey_2) {
          {
            List<IRuleNode> _get_2 = nodeGroupMap.get(VictoryDomainType.CORR);
            for(final IRuleNode node_2 : _get_2) {
              final IRuleCorr corrNode = ((IRuleCorr) node_2);
              _builder.newLineIfNotEmpty();
              {
                if ((showCreated || (corrNode.getBindingType() != VictoryBindingType.CREATE))) {
                  String _get_3 = nodeIdMap.get(corrNode.getSource());
                  String _get_4 = nodeIdMap.get(corrNode.getTarget());
                  String _typeName = corrNode.getTypeName();
                  VictoryBindingType _bindingType = corrNode.getBindingType();
                  boolean _tripleEquals = (_bindingType == VictoryBindingType.CREATE);
                  String _visualiseRuleCorrEdge = VictoryPlantUMLGenerator.visualiseRuleCorrEdge(_get_3, _get_4, _typeName, _tripleEquals);
                  _builder.append(_visualiseRuleCorrEdge);
                  _builder.newLineIfNotEmpty();
                }
              }
            }
          }
        }
      }
      _builder.newLine();
      {
        List<IRuleEdge> _edges = rule.getEdges();
        for(final IRuleEdge edge : _edges) {
          {
            if (((edge.getDomainType() != VictoryDomainType.CORR) && (showCreated || (edge.getBindingType() != VictoryBindingType.CREATE)))) {
              String _get_5 = nodeIdMap.get(edge.getSrcNode());
              String _get_6 = nodeIdMap.get(edge.getTrgNode());
              String _typeName_1 = edge.getTypeName();
              VictoryBindingType _bindingType_1 = edge.getBindingType();
              boolean _tripleEquals_1 = (_bindingType_1 == VictoryBindingType.CREATE);
              String _visualiseRuleEdge = VictoryPlantUMLGenerator.visualiseRuleEdge(_get_5, _get_6, _typeName_1, _tripleEquals_1);
              _builder.append(_visualiseRuleEdge);
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      _builder.newLine();
      {
        if (groupFullRule) {
          _builder.append("}");
        }
      }
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  private static String visualiseEObjectGraph(final Map<IObject, Pair<String, String>> eObjectMapping) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("together {");
    _builder.newLine();
    {
      Set<IObject> _keySet = eObjectMapping.keySet();
      for(final IObject object : _keySet) {
        _builder.append("\t");
        _builder.append("object \"");
        String _value = eObjectMapping.get(object).getValue();
        _builder.append(_value, "\t");
        _builder.append("\" as ");
        String _key = eObjectMapping.get(object).getKey();
        _builder.append(_key, "\t");
        _builder.append(" <<BLACK>> <<SRC>> {");
        _builder.newLineIfNotEmpty();
        {
          List<IAttribute> _classAttribute = object.getClassAttribute();
          for(final IAttribute attr : _classAttribute) {
            _builder.append("\t");
            _builder.append("\t");
            String _typeName = attr.getTypeName();
            _builder.append(_typeName, "\t\t");
            _builder.append(" ");
            String _name = attr.getName();
            _builder.append(_name, "\t\t");
            _builder.append(" ");
            Object _feature = object.getFeature(attr);
            _builder.append(_feature, "\t\t");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        {
          List<IObject> _contents = object.getContents();
          for(final IObject contentObject : _contents) {
            {
              boolean _containsKey = eObjectMapping.containsKey(contentObject);
              if (_containsKey) {
                _builder.append("\t");
                String _key_1 = eObjectMapping.get(object).getKey();
                _builder.append(_key_1, "\t");
                _builder.append(" --> ");
                String _key_2 = eObjectMapping.get(contentObject).getKey();
                _builder.append(_key_2, "\t");
                _builder.append(" : ");
                String _containingFeatureName = contentObject.getContainingFeatureName();
                _builder.append(_containingFeatureName, "\t");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  private static String visualiseRuleNode(final String ruleId, final String colorDefinitions) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("class ");
    _builder.append(ruleId);
    _builder.append(" ");
    _builder.append(colorDefinitions);
    return _builder.toString();
  }
  
  private static String visualiseRuleEdge(final String srcNodeId, final String trgNodeId, final String edgeId, final boolean bindingTypeCreate) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(srcNodeId);
    _builder.append(" -");
    {
      if (bindingTypeCreate) {
        _builder.append("[#SpringGreen]");
      }
    }
    _builder.append("-> ");
    _builder.append(trgNodeId);
    _builder.append(" : \"");
    _builder.append(edgeId);
    _builder.append("\"");
    return _builder.toString();
  }
  
  private static String visualiseRuleCorrEdge(final String srcNodeId, final String trgNodeId, final String edgeId, final boolean bindingTypeCreate) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(srcNodeId);
    _builder.append(" ...");
    {
      if (bindingTypeCreate) {
        _builder.append("[#SpringGreen]");
      }
    }
    _builder.append(" ");
    _builder.append(trgNodeId);
    _builder.append(" : \"");
    _builder.append(edgeId);
    _builder.append("\"");
    return _builder.toString();
  }
  
  private static String idForNode(final IRuleNode node) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\"");
    String _name = node.getName();
    _builder.append(_name);
    _builder.append(" : ");
    String _typeName = node.getTypeName();
    _builder.append(_typeName);
    _builder.append("\"");
    return _builder.toString();
  }
  
  private static String getColorDefinitions(final VictoryBindingType binding, final VictoryDomainType domain, final IBeXOp op) {
    String _xblockexpression = null;
    {
      String bindingColour = "OTHER";
      if ((binding == VictoryBindingType.CREATE)) {
        if ((((op == IBeXOp.INITIAL_FWD) && (domain == VictoryDomainType.SRC)) || ((op == IBeXOp.INITIAL_BWD) && (domain == VictoryDomainType.TRG)))) {
          bindingColour = "TRANSLATE";
        } else {
          bindingColour = "CREATE";
        }
      }
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<<");
      _builder.append(bindingColour);
      _builder.append(">> <<");
      _builder.append(domain);
      _builder.append(">>");
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
}
