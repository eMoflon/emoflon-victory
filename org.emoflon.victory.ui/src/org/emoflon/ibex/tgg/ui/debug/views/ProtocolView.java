package org.emoflon.ibex.tgg.ui.debug.views;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.emoflon.ibex.tgg.ui.debug.api.RuleApplication;
import org.emoflon.ibex.tgg.ui.debug.options.IUserOptions;
import org.emoflon.ibex.tgg.ui.debug.views.treeContent.protocol.ProtocolContentManager;
import org.emoflon.ibex.tgg.ui.debug.views.treeContent.protocol.RuleApplicationNode;

public class ProtocolView extends Composite implements ISharedFocusElement {

	private IVisualiser visualiser;

	private TreeViewer treeViewer;
	private ProtocolContentManager contentManager;
	private final IUserOptions userOptions;

	private ProtocolView(Composite parent, IUserOptions userOptions) {
		super(parent, SWT.NONE);
		this.userOptions = userOptions;
		contentManager = new ProtocolContentManager(userOptions);
	}

	private ProtocolView build() {
		setLayout(new GridLayout());

		treeViewer = new TreeViewer(this, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.MULTI);
		treeViewer.setContentProvider(contentManager.getTreeContentManager());
		treeViewer.setLabelProvider(contentManager.getTreeContentManager().getCellLabelProvider());
		treeViewer.setComparator(contentManager.getProtocolNodeComparator());
		treeViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		treeViewer.setInput("root");

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent pEvent) {

				if (pEvent.getSelection().isEmpty())
					return;
				else
					sharedFocusElements.forEach(ISharedFocusElement::focusRemoved);

				if (pEvent.getSelection() instanceof IStructuredSelection) {
					@SuppressWarnings("unchecked")
					List<Object> selection = pEvent.getStructuredSelection().toList();

					Collection<RuleApplication> ruleApplications = new HashSet<>();
					for (Object element : selection)
						if (element instanceof RuleApplicationNode)
							ruleApplications.add(((RuleApplicationNode) element).getModelChanges());
					visualiser.display(ruleApplications);
				}
			}
		});

		this.updateToolTips();
		pack();
		return this;

	}

	public static ProtocolView create(Composite parent, IUserOptions userOptions) {
		return new ProtocolView(parent, userOptions).build();
	}

	public void registerVisualiser(IVisualiser visualiser) {
		this.visualiser = visualiser;
	}

	public void highlight(String ruleName) {
		contentManager.highlight(ruleName);
		treeViewer.refresh();
	}

	public void populate(List<RuleApplication> ruleApplications) {
		contentManager.populate(ruleApplications);
		treeViewer.refresh();
	}

	private Collection<ISharedFocusElement> sharedFocusElements = new HashSet<>();

	@Override
	public void focusRemoved() {
		treeViewer.setSelection(null);
	}

	@Override
	public void registerSharedFocus(ISharedFocusElement sharedFocusElement) {
		sharedFocusElements.add(sharedFocusElement);
	}

	public void updateToolTips() {
		treeViewer.getControl().setToolTipText(ToolTips.PROTOCOL_VIEW.getDescription(userOptions.getToolTipSetting()));
	}

}
