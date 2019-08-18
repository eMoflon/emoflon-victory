package org.emoflon.ibex.tgg.ui.debug.core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
//import org.emoflon.ibex.tgg.operational.monitoring.IVictoryDataProvider;
import org.emoflon.ibex.tgg.ui.debug.api.IVictoryDataProvider;

//import org.emoflon.ibex.tgg.operational.monitoring.IbexController;
//import org.emoflon.ibex.tgg.ui.debug.adapter.core.IbexController;
import org.emoflon.ibex.tgg.ui.debug.options.IBeXOp;
import org.emoflon.ibex.tgg.ui.debug.options.UserOptionsManager;
import org.emoflon.ibex.tgg.ui.debug.views.MatchDisplayView;
import org.emoflon.ibex.tgg.ui.debug.views.MatchListView;
import org.emoflon.ibex.tgg.ui.debug.api.IVictoryController;;
//import org.emoflon.ibex.tgg.ui.debug.adapter.core.UIController;


public class IbexDebugUI implements Runnable {

	private static Display display;

	private IbexDebugUI(IVictoryDataProvider pDataProvider, IBeXOp pOp,IVictoryController controller ) {
		dataProvider = pDataProvider;
		userOptionsManager = new UserOptionsManager(pOp);
		this.controller = controller;
		
	}

	/**
	 * Creates and returns a new IBeX debugging UI.
	 * <p>
	 * If the <code>pRunOnNewThread</code> flag is set to <code>true</code>, then
	 * the debugging UI is automatically started on its own thread. No further
	 * initialization is required.
	 * <p>
	 * If the flag is set to false, then the UI needs to be specifically started by
	 * calling {@link IbexDebugUI#run()}. Note that the UI will use the calling
	 * thread as its event loop, thereby blocking any execution of other statements
	 * until the UI is closed.
	 * 
	 * @return the IBeX debugging UI that was created
	 */
	public static IbexDebugUI create(IVictoryDataProvider pDataProvider, IBeXOp pOp, IVictoryController controller) {
		Thread.currentThread().setName("IbexDebugUI - SWT UI thread");
		return new IbexDebugUI(pDataProvider, pOp, controller);
	}

	public static Display getDisplay() {
		return display;
	}

	// -----------------------------------

	private IVictoryController controller;
	private IVictoryDataProvider dataProvider;
	private MatchListView matchListView;
	private MatchDisplayView matchDisplayView;
	private UserOptionsManager userOptionsManager;

	@Override
	public void run() {
		display = new Display();
		Shell shell = new Shell(display);

		initUI(shell);

		shell.open();
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		display.dispose();
		System.exit(0);
	}

	private void initUI(Shell pShell) {
		GridLayout layout = new GridLayout(2, false);
		pShell.setLayout(layout);

		SashForm sashForm = new SashForm(pShell, SWT.HORIZONTAL);
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		sashForm.setBackground(display.getSystemColor(SWT.COLOR_GRAY));

		matchListView = MatchListView.create(sashForm);
		matchListView.setLayoutData(new GridData(GridData.FILL_BOTH));

		matchDisplayView = MatchDisplayView.create(sashForm, dataProvider, userOptionsManager);
		matchDisplayView.setLayoutData(new GridData(GridData.FILL_BOTH));

		sashForm.setWeights(new int[] { 30, 70 });

		matchListView.registerVisualiser(matchDisplayView);
		
		controller.init(matchListView);

		// controller = new UIController(matchListView);
		synchronized (this) {
			notify();
		}
	}
//
//	    /**
//	     * Returns the IBeX-Controller for this debugging UI.<br>
//	     * If the UI has not finished initializing, this method will block the calling
//	     * thread until the IBeX-Controller is available.
//	     * 
//	     * @return the IBeX-Controller for this debugging UI
//	     */
	    public IVictoryController getIbexController() {
	    	
		synchronized (this) {
		    while (controller == null)
			try {
			    wait();
			} catch (InterruptedException pIE) {
			    // TODO calling thread was interrupted. What now..?
			}
		    return controller;
		}
	    
	    }


}
