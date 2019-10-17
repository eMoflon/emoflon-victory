package org.emoflon.ibex.tgg.ui.debug.core;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.emoflon.ibex.tgg.ui.debug.api.DataPackage;
import org.emoflon.ibex.tgg.ui.debug.api.DataProvider;
import org.emoflon.ibex.tgg.ui.debug.api.Match;
import org.emoflon.ibex.tgg.ui.debug.api.Victory;
import org.emoflon.ibex.tgg.ui.debug.options.IUserOptions.ToolTipOption;
import org.emoflon.ibex.tgg.ui.debug.options.UserOptionsManager;
import org.emoflon.ibex.tgg.ui.debug.views.MatchDisplayView;
import org.emoflon.ibex.tgg.ui.debug.views.MatchListView;
import org.emoflon.ibex.tgg.ui.debug.views.ProtocolView;

public class VictoryUI implements IExitCodeReceiver, Consumer<DataPackage> {

	private static Display display;

	public static Display getDisplay() {
		return display;
	}

	private Victory victory;
	private Shell shell;
	private boolean exitCode;
	private DataProvider dataProvider;
	private MatchListView matchListView;
	private ProtocolView protocolView;
	private MatchDisplayView matchDisplayView;
	private UserOptionsManager userOptionsManager;

	public VictoryUI(Victory victory, DataProvider dataProvider) {
		this.victory = victory;
		this.dataProvider = dataProvider;
		userOptionsManager = new UserOptionsManager();

		initUI();
	}

	private void initUI() {
		Thread.currentThread().setName("Victory-UI thread");

		display = new Display();
		shell = new Shell(display);

		shell.setLayout(new GridLayout());

		SashForm mainSashForm = new SashForm(shell, SWT.HORIZONTAL);
		mainSashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		mainSashForm.setLayout(new GridLayout());
		mainSashForm.setBackground(display.getSystemColor(SWT.COLOR_GRAY));

		SashForm leftPanelSashForm = new SashForm(mainSashForm, SWT.VERTICAL);
		leftPanelSashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		leftPanelSashForm.setLayout(new GridLayout());
		leftPanelSashForm.setBackground(display.getSystemColor(SWT.COLOR_GRAY));

		matchListView = MatchListView.create(leftPanelSashForm, dataProvider.getAllRules(), userOptionsManager,
				victory);
		matchListView.setLayoutData(new GridData(GridData.FILL_BOTH));

		protocolView = ProtocolView.create(leftPanelSashForm, userOptionsManager);
		protocolView.setLayoutData(new GridData(GridData.FILL_BOTH));

		matchListView.setProtocolView(protocolView);

		matchListView.registerSharedFocus(protocolView);
		protocolView.registerSharedFocus(matchListView);

		leftPanelSashForm.setWeights(new int[] { 60, 40 });

		matchDisplayView = MatchDisplayView.create(mainSashForm, this, dataProvider, userOptionsManager);
		matchDisplayView.setLayoutData(new GridData(GridData.FILL_BOTH));

		mainSashForm.setWeights(new int[] { 30, 70 });

		matchListView.registerVisualiser(matchDisplayView);
		protocolView.registerVisualiser(matchDisplayView);

		Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);

		MenuItem helpMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		helpMenuHeader.setText("&Help");

		Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
		helpMenuHeader.setMenu(helpMenu);

		MenuItem helpOpenHandbookItem = new MenuItem(helpMenu, SWT.PUSH);
		helpOpenHandbookItem.setText("&Open Handbook");
		helpOpenHandbookItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					try {
						Desktop.getDesktop().browse(new URI(
								"https://docs.google.com/document/d/1eKiEVsrNf-GLORP2BFzLK5-s8YoumV1yxY-L7IWfdxM/edit?usp=sharing"));
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		MenuItem helpToolTipsItem = new MenuItem(helpMenu, SWT.CASCADE);
		helpToolTipsItem.setText("&Tooltips");

		Menu helpToolTipsMenu = new Menu(helpMenu);
		helpToolTipsItem.setMenu(helpToolTipsMenu);

		MenuItem helpToolTipsOff = new MenuItem(helpToolTipsMenu, SWT.RADIO);
		helpToolTipsOff.setText("Show no tooltips");
		helpToolTipsOff.setSelection(userOptionsManager.getToolTipSetting() == ToolTipOption.NONE);
		MenuItem helpToolTipsMinimal = new MenuItem(helpToolTipsMenu, SWT.RADIO);
		helpToolTipsMinimal.setText("Show minimal tooltips");
		helpToolTipsMinimal.setSelection(userOptionsManager.getToolTipSetting() == ToolTipOption.MINIMAL);
		MenuItem helpToolTipsFull = new MenuItem(helpToolTipsMenu, SWT.RADIO);
		helpToolTipsFull.setText("Show full tooltips");
		helpToolTipsFull.setSelection(userOptionsManager.getToolTipSetting() == ToolTipOption.FULL);

		SelectionListener tooltipSelectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent pSelectionEvent) {
				ToolTipOption selectedToolTipSetting;
				if (pSelectionEvent.getSource() == helpToolTipsFull)
					selectedToolTipSetting = ToolTipOption.FULL;
				else if (pSelectionEvent.getSource() == helpToolTipsMinimal)
					selectedToolTipSetting = ToolTipOption.MINIMAL;
				else
					selectedToolTipSetting = ToolTipOption.NONE;
				if (userOptionsManager.getToolTipSetting() != selectedToolTipSetting) {
					userOptionsManager.setToolTipSetting(selectedToolTipSetting);
					matchDisplayView.updateToolTips();
					matchListView.updateToolTips();
				}
			}
		};
		helpToolTipsFull.addSelectionListener(tooltipSelectionListener);
		helpToolTipsMinimal.addSelectionListener(tooltipSelectionListener);
		helpToolTipsOff.addSelectionListener(tooltipSelectionListener);

	}

	public boolean run() {
		shell.open();
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		display.dispose();

		return exitCode;
	}

	public void exit(boolean restart) {
		exitCode = restart;
		shell.dispose();
	}

	@Override
	public void accept(DataPackage dataPackage) {
		matchListView.populate(dataPackage.getMatches());
		protocolView.populate(dataPackage.getRuleApplications());
	}

	public Match getSelectedMatch() {
		return matchListView.getChosenMatch();
	}
}
