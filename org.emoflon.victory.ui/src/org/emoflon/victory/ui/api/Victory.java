package org.emoflon.victory.ui.api;

import org.emoflon.victory.ui.core.VictoryUI;

/**
 * The main interaction point for any adapter to use Victory. Provides 
 * methods for initializing the Victory UI and interacting with it.
 */
public final class Victory {
	private VictoryUI ui;
	private final Match[] selectedMatch = new Match[1];

	@SuppressWarnings("deprecation")
	public boolean run(DataProvider dataProvider, Runnable matchProvider) {
		if (ui != null)
			throw new IllegalStateException("Victory has already been initialised.");
		ui = new VictoryUI(this, dataProvider);

		Thread matchProviderThread = new Thread(matchProvider);
		matchProviderThread.start();

		boolean exitCode = ui.run();
		ui = null;

		if (matchProviderThread.isAlive())
			try {
				matchProviderThread.join(500);
			} catch (InterruptedException pIE) {
			} finally {
				if (matchProviderThread.isAlive())
					matchProviderThread.stop();
			}

		return exitCode;
	}

	/** CONCURRENCY: This method is only called by the match-providing thread */
	public Match selectMatch(DataPackage dataPackage) {
		if (ui == null)
			throw new IllegalStateException("Victory has not been initialised yet.");

		VictoryUI.getDisplay().syncExec(() -> ui.accept(dataPackage));

		synchronized (selectedMatch) {
			while (selectedMatch[0] == null)
				try {
					selectedMatch.wait();
				} catch (InterruptedException pIE) {
					// calling thread was interrupted
				}
			Match match = selectedMatch[0];
			selectedMatch[0] = null;
			return match;
		}
	}

	public void setSelectedMatch(Match match) {
		synchronized (selectedMatch) {
			selectedMatch[0] = match;
			selectedMatch.notify();
		}
	}
}
