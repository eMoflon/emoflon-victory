package org.emoflon.victory.ui.views.treeContent.protocol;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.emoflon.victory.ui.api.RuleApplication;
import org.emoflon.victory.ui.options.IUserOptions;
import org.emoflon.victory.ui.views.treeContent.TreeContentManager;
import org.emoflon.victory.ui.views.treeContent.TreeNode;

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
