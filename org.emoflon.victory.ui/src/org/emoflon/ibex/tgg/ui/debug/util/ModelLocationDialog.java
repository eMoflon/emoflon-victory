package org.emoflon.ibex.tgg.ui.debug.util;

import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ModelLocationDialog {
	private String[] selectorHints;
	private String[][] defaultLocationData;
	private Consumer<String[]> buttonAction;

	public ModelLocationDialog(String[] selectorHints, String[][] defaultLocationData, String pButtonText,
			Consumer<String[]> buttonAction) {
		this.selectorHints = selectorHints;
		this.defaultLocationData = defaultLocationData;
		this.buttonAction = buttonAction;
	}

	public void build(Shell parentShell) {
		Shell dialogShell = new Shell(parentShell, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.ON_TOP);

		dialogShell.setLayout(new GridLayout());

		Composite panel = new Composite(dialogShell, SWT.NONE);
		panel.setLayoutData(new GridData(GridData.FILL_BOTH));
		panel.setLayout(new GridLayout());

		LocationSelector srcSelector = new LocationSelector(panel, selectorHints[0], defaultLocationData[0][0],
				defaultLocationData[0][1], defaultLocationData[0][2]).build(dialogShell);
		srcSelector.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		LocationSelector trgSelector = new LocationSelector(panel, selectorHints[1], defaultLocationData[1][0],
				defaultLocationData[1][1], defaultLocationData[1][2]).build(dialogShell);
		trgSelector.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		LocationSelector corrSelector = new LocationSelector(panel, selectorHints[2], defaultLocationData[2][0],
				defaultLocationData[2][1], defaultLocationData[2][2]).build(dialogShell);
		corrSelector.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		LocationSelector protocolSelector = new LocationSelector(panel, selectorHints[3], defaultLocationData[3][0],
				defaultLocationData[3][1], defaultLocationData[3][2]).build(dialogShell);
		protocolSelector.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button actionButton = new Button(panel, SWT.PUSH);
		actionButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		actionButton.setText("Save all");
		actionButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent pEvent) {
				buttonAction.accept(new String[] { srcSelector.getFileLocation(), trgSelector.getFileLocation(),
						corrSelector.getFileLocation(), protocolSelector.getFileLocation() });
				dialogShell.close();
				dialogShell.dispose();
			}
		});

		panel.pack();
		dialogShell.pack();

		dialogShell.open();
	}

	private static class LocationSelector extends Composite {

		private String selectorHint;
		private String defaultDirectory;
		private String defaultName;
		private String fileExtension;

		private Text location;

		private LocationSelector(Composite parent, String selectorHint, String defaultDirectory, String defaultName,
				String fileExtension) {
			super(parent, SWT.NONE);
			this.selectorHint = selectorHint;
			this.defaultDirectory = defaultDirectory;
			this.defaultName = defaultName;
			this.fileExtension = fileExtension;
		}

		private LocationSelector build(Shell parentShell) {
			setLayout(new GridLayout(2, false));

			location = new Text(this, SWT.SINGLE);
			location.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			location.setText(defaultDirectory + defaultName + "." + fileExtension);
			location.setMessage(selectorHint);

			Button selectButton = new Button(this, SWT.PUSH);
			selectButton.setText("Change...");
			selectButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent pEvent) {
					FileDialog dialog = new FileDialog(parentShell, SWT.SAVE);
					dialog.setFilterPath(defaultDirectory);
					dialog.setFileName(defaultName);
					dialog.setFilterExtensions(new String[] { fileExtension });
					dialog.setFilterNames(new String[] { fileExtension });
					dialog.setOverwrite(true);

					String result = dialog.open();
					if (result != null)
						location.setText(result + "." + fileExtension);
				}
			});

			return this;
		}

		private String getFileLocation() {
			return location.getText();
		}
	}
}
