package org.emoflon.ibex.tgg.ui.debug.views.treeContent.protocol;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.emoflon.ibex.tgg.ui.debug.api.RuleApplication;
import org.emoflon.ibex.tgg.ui.debug.options.IUserOptions;
import org.emoflon.ibex.tgg.ui.debug.views.treeContent.TreeContentManager;
import org.emoflon.ibex.tgg.ui.debug.views.treeContent.TreeNode;

public class ProtocolContentManager {

	private TreeContentManager manager = new TreeContentManager();

	private List<RuleApplicationNode> protocolNodes;
	private final IUserOptions userOptions;

	public ProtocolContentManager(IUserOptions userOptions) {
		this.userOptions = userOptions;
		protocolNodes = new LinkedList<>();
	}

	public void populate(List<RuleApplication> ruleApplications) {
		for (int i = protocolNodes.size(); i < ruleApplications.size(); i++) {
			RuleApplicationNode node = new RuleApplicationNode(i, ruleApplications.get(i), userOptions);
			protocolNodes.add(node);
			manager.getRoot().addChild(node);
		}
	}

	public void highlight(String ruleName) {
		for (TreeNode node : manager.getRoot().getChildren()) {
			if (!(node instanceof RuleApplicationNode)) {
				continue;
			}
			RuleApplicationNode ruleNode = (RuleApplicationNode) node;
			if (ruleName != null && ruleNode.getModelChanges().getRuleName().equals(ruleName)) {
				ruleNode.highlight(true);
			} else {
				ruleNode.highlight(false);
			}
		}
	}

	public TreeContentManager getTreeContentManager() {
		return manager;
	}

	public ViewerComparator getProtocolNodeComparator() {
		return new ViewerComparator() {
			@Override
			public int compare(Viewer pViewer, Object pElement1, Object pElement2) {
				if (!(pElement1 instanceof RuleApplicationNode && pElement2 instanceof RuleApplicationNode))
					throw new IllegalStateException(
							"Protocol view tree must not contain any elements other than ProtocolNodes");

				return ((RuleApplicationNode) pElement2).getStep() - ((RuleApplicationNode) pElement1).getStep();
			}
		};
	}
}
