package org.emoflon.ibex.tgg.ui.debug.views;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.emoflon.ibex.tgg.ui.debug.api.Match;
import org.emoflon.ibex.tgg.ui.debug.api.Rule;
import org.emoflon.ibex.tgg.ui.debug.api.Victory;
import org.emoflon.ibex.tgg.ui.debug.options.IUserOptions;
import org.emoflon.ibex.tgg.ui.debug.views.treeContent.TreeNode;
import org.emoflon.ibex.tgg.ui.debug.views.treeContent.matchList.MatchListContentManager;
import org.emoflon.ibex.tgg.ui.debug.views.treeContent.matchList.MatchNode;
import org.emoflon.ibex.tgg.ui.debug.views.treeContent.matchList.RuleNode;

public class MatchListView extends Composite implements ISharedFocusElement {

	private Victory victory;
	private IVisualiser visualiser;
	private TreeViewer treeViewer;
	private MatchListContentManager contentManager;
	private Button applyButton;
	private MenuItem applyItem;
	private ProtocolView protocolView;
	private Match[] chosenMatch = new Match[1];
	private Collection<ISharedFocusElement> sharedFocusElements = new HashSet<>();
	private MenuItem expandAllItem;
	private MenuItem collapseAllItem;
	private final IUserOptions userOptions;

	private MatchListView(Composite parent, Collection<Rule> rules, IUserOptions userOptions, Victory victory) {
		super(parent, SWT.NONE);

		this.victory = victory;
		this.userOptions = userOptions;
		contentManager = new MatchListContentManager(rules, userOptions);
	}

	private MatchListView build() {
		setLayout(new GridLayout(1, false));

		treeViewer = new TreeViewer(this, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.SINGLE);
		treeViewer.setContentProvider(contentManager.getTreeContentManager());
		treeViewer.setLabelProvider(contentManager.getTreeContentManager().getCellLabelProvider());
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 3;
		treeViewer.getControl().setLayoutData(gridData);
		treeViewer.setInput("root");

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent pEvent) {

				applyButton.setEnabled(false);
				protocolView.highlight(null);

				if (!pEvent.getSelection().isEmpty()) {
					sharedFocusElements.forEach(ISharedFocusElement::focusRemoved);
				}

				if (pEvent.getSelection() instanceof IStructuredSelection) {
					Object selectedElement = pEvent.getStructuredSelection().getFirstElement();
					if (selectedElement instanceof MatchNode) {
						Match match = ((MatchNode) selectedElement).getMatch();
						visualiser.display(match);
						applyButton.setEnabled(!match.isBlocked());
						applyItem.setEnabled(!match.isBlocked());
					} else if (selectedElement instanceof RuleNode) {
						protocolView.highlight(((RuleNode) selectedElement).getRule().getName());
						visualiser.display(((RuleNode) selectedElement).getRule());
						List<TreeNode> matchNodes = ((RuleNode) selectedElement).getChildren().stream()
								.filter(c -> c instanceof MatchNode && !((MatchNode) c).getMatch().isBlocked())
								.collect(Collectors.toList());
						applyButton.setEnabled(!matchNodes.isEmpty());
						applyItem.setEnabled(!matchNodes.isEmpty());
					}
				}
			}
		});
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent pEvent) {
				Object selection = treeViewer.getStructuredSelection().getFirstElement();
				if (selection instanceof MatchNode) {
					applyMatch((MatchNode) selection);
				} else if (selection instanceof RuleNode) {
					applyRandomMatch((RuleNode) selection);
				}
			}
		});

		final Menu treeMenu = new Menu(treeViewer.getTree());
		treeViewer.getTree().setMenu(treeMenu);
		expandAllItem = new MenuItem(treeMenu, SWT.PUSH);
		expandAllItem.setText("Expand All");
		expandAllItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent pSelectionEvent) {
				treeViewer.expandAll();
			}
		});
		collapseAllItem = new MenuItem(treeMenu, SWT.PUSH);
		collapseAllItem.setText("Collapse All");
		collapseAllItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent pSelectionEvent) {
				treeViewer.collapseAll();
			}
		});

		applyItem = new MenuItem(treeMenu, SWT.PUSH);
		applyItem.setText("Apply");

		Composite c = new Composite(this, SWT.NORMAL);
		c.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
		c.setLayout(new GridLayout(3, false));

		applyButton = new Button(c, SWT.PUSH);
		applyButton.setText("Apply");
		applyButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

		SelectionAdapter applySelectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent pSelectionEvent) {
				Object selection = treeViewer.getStructuredSelection().getFirstElement();
				if (selection instanceof MatchNode && !((MatchNode) selection).getMatch().isBlocked())
					applyMatch((MatchNode) selection);
				else if (selection instanceof RuleNode)
					applyRandomMatch((RuleNode) selection);
			}
		};
		applyButton.addSelectionListener(applySelectionAdapter);
		applyItem.addSelectionListener(applySelectionAdapter);

		this.updateToolTips();
		pack();
		return this;
	}

	public static MatchListView create(Composite parent, Collection<Rule> rules, IUserOptions userOptions,
			Victory victory) {
		return new MatchListView(parent, rules, userOptions, victory).build();
	}

	public void registerVisualiser(IVisualiser visualiser) {
		this.visualiser = visualiser;
	}

	/**
	 * Populates the list-view with the given collection of matches.
	 * 
	 * @param pMatches the collection of matches to populate the list-view with
	 */
	public void populate(Collection<Match> matches) {
		applyButton.setEnabled(false);
		applyItem.setEnabled(false);
		contentManager.populate(matches);
		treeViewer.refresh();
		Object selectedElement = treeViewer.getStructuredSelection().getFirstElement();
		if(selectedElement != null) {
			if (selectedElement instanceof MatchNode) {
				Match match = ((MatchNode) selectedElement).getMatch();
				applyButton.setEnabled(!match.isBlocked());
				applyItem.setEnabled(!match.isBlocked());
			} else if (selectedElement instanceof RuleNode) {
				protocolView.highlight(((RuleNode) selectedElement).getRule().getName());
				List<TreeNode> matchNodes = ((RuleNode) selectedElement).getChildren().stream()
						.filter(c -> c instanceof MatchNode && !((MatchNode) c).getMatch().isBlocked())
						.collect(Collectors.toList());
				applyButton.setEnabled(!matchNodes.isEmpty());
				applyItem.setEnabled(!matchNodes.isEmpty());
			}
		}
	}

	/**
	 * Blocks the calling thread until a chosen match is available.
	 * 
	 * @return the match chosen by the user
	 */
	public Match getChosenMatch() {
		synchronized (chosenMatch) {
			while (chosenMatch[0] == null)
				try {
					chosenMatch.wait();
				} catch (InterruptedException pIE) {
					// TODO calling thread was interrupted. What now..?
				}
			Match match = chosenMatch[0];
			chosenMatch[0] = null;
			return match;
		}
	}

	@Override
	public void focusRemoved() {
		treeViewer.setSelection(null);
	}

	@Override
	public void registerSharedFocus(ISharedFocusElement sharedFocusElement) {
		sharedFocusElements.add(sharedFocusElement);
	}

	private void applyMatch(MatchNode matchNode) {
		synchronized (chosenMatch) {
			victory.setSelectedMatch(matchNode.getMatch());
			RuleNode ruleNode = (RuleNode) matchNode.getParent();
			ruleNode.increaseTimesApplied();
			chosenMatch.notify();
		}
	}

	private void applyRandomMatch(RuleNode ruleNode) {
		synchronized (ruleNode) {
			List<TreeNode> matchNodes = ruleNode.getChildren().stream()
					.filter(c -> c instanceof MatchNode && !((MatchNode) c).getMatch().isBlocked())
					.collect(Collectors.toList());
			if (matchNodes.isEmpty()) {
				return;
			}
			int i = new Random().nextInt(matchNodes.size());
			applyMatch(((MatchNode) matchNodes.get(i)));
		}
	}

	public void updateToolTips() {
		treeViewer.getControl().setToolTipText(ToolTips.MATCHLIST_TREE.getDescription(userOptions.getToolTipSetting()));
		applyButton.setToolTipText(ToolTips.MATCHLIST_APPLY_BUTTON.getDescription(userOptions.getToolTipSetting()));
		applyItem.setToolTipText(ToolTips.MATCHLIST_APPLY_BUTTON.getDescription(userOptions.getToolTipSetting()));
		collapseAllItem
				.setToolTipText(ToolTips.MATCHLIST_COLLAPSE_BUTTON.getDescription(userOptions.getToolTipSetting()));
		expandAllItem
				.setToolTipText(ToolTips.MATCHLIST_EXPAND_BUTTON.getDescription(userOptions.getToolTipSetting()));
	}

	/**
	 * @param protocolView the protocolView to set
	 */
	public void setProtocolView(ProtocolView protocolView) {
		this.protocolView = protocolView;
	}
}
