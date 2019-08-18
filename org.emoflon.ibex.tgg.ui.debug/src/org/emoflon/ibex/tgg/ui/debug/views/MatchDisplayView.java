package org.emoflon.ibex.tgg.ui.debug.views;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
//import org.emoflon.ibex.tgg.operational.matches.IMatch;
import org.emoflon.ibex.tgg.ui.debug.api.IMatchVictory;
import org.emoflon.ibex.tgg.ui.debug.api.IVictoryDataProvider;
import org.emoflon.ibex.tgg.ui.debug.options.UserOptionsManager;
import org.emoflon.ibex.tgg.ui.debug.views.visualisable.IMatchVisualisation;
import org.emoflon.ibex.tgg.ui.debug.views.visualisable.TGGRuleVisualisation;
import org.emoflon.ibex.tgg.ui.debug.views.visualisable.VisualisableElement;

public class MatchDisplayView extends Composite implements IVisualiser {

    private IVictoryDataProvider dataProvider;
    private UserOptionsManager userOptionsManager;

    private ScrolledComposite imageScroller;
    private Label imageContainer;

    private MatchDisplayView(Composite parent, IVictoryDataProvider pDataProvider,
	    UserOptionsManager pUserOptionsManager) {
	super(parent, SWT.NONE);

	dataProvider = pDataProvider;
	userOptionsManager = pUserOptionsManager;
    }

    private MatchDisplayView build() {
	setLayout(new GridLayout());

	imageScroller = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL);
	imageScroller.setLayoutData(new GridData(GridData.FILL_BOTH));
	imageScroller.setExpandHorizontal(true);
	imageScroller.setExpandVertical(true);
	imageScroller.setAlwaysShowScrollBars(true);

	imageContainer = new Label(imageScroller, SWT.BORDER | SWT.CENTER);
	imageContainer.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	imageContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
	imageContainer.setImage(null);
	imageContainer.pack();

	imageScroller.setContent(imageContainer);

	Composite buttonRow = new Composite(this, SWT.NONE);
	buttonRow.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	buttonRow.setLayout(new GridLayout(3, false));

	Button toggleFullRuleVisButton = new Button(buttonRow, SWT.TOGGLE);
	toggleFullRuleVisButton.setText("Full rule Vis");
	toggleFullRuleVisButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
	toggleFullRuleVisButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent pSelectionEvent) {
		userOptionsManager.setDisplayFullRuleForMatches(toggleFullRuleVisButton.getSelection());
		refresh();
	    }
	});

	Button saveModelsButton = new Button(buttonRow, SWT.PUSH);
	saveModelsButton.setText("Save Models");
	saveModelsButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
	saveModelsButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent pSelectionEvent) {
		try {
		    dataProvider.saveModels();
		} catch (IOException e) {
		    throw new IllegalArgumentException("Save Models has a problem.");
		}
	    }
	});

	Button terminateButton = new Button(buttonRow, SWT.PUSH);
	terminateButton.setText("Quit Debugger");
	terminateButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
	terminateButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent pSelectionEvent) {
		System.exit(0);
	    }
	});

	pack();
	return this;
    }

    public static MatchDisplayView create(Composite pParent, IVictoryDataProvider pDataProvider,
	    UserOptionsManager pUserOptionsManager) {
	return new MatchDisplayView(pParent, pDataProvider, pUserOptionsManager).build();
    }

    /*
     * display specific code
     */

    private Map<String, VisualisableElement> ruleElementMap = new HashMap<>();
    private Map<IMatchVictory, VisualisableElement> matchElementMap = new HashMap<>();

    private VisualisableElement currentElement;

    @Override
    public void display(String pRuleName) {

	if (!ruleElementMap.containsKey(pRuleName)) {
	    VisualisableElement ruleElement = new TGGRuleVisualisation(dataProvider.getRule(pRuleName),
		    userOptionsManager);
	    ruleElementMap.put(pRuleName, ruleElement);
	}

	currentElement = ruleElementMap.get(pRuleName);

	refresh();
    }

    @Override
    public void display(IMatchVictory pMatch) {

	if (!matchElementMap.containsKey(pMatch)) {
	    VisualisableElement matchElement = new IMatchVisualisation(pMatch, dataProvider, userOptionsManager);
	    matchElementMap.put(pMatch, matchElement);
	}

	currentElement = matchElementMap.get(pMatch);

	refresh();
    }

    @Override
    public void refresh() {
	if (userOptionsManager.isInvalid()) {
	    ruleElementMap.values().forEach(VisualisableElement::invalidate);
	    matchElementMap.values().forEach(VisualisableElement::invalidate);
	    userOptionsManager.revalidate();
	}

	displayImage(currentElement.getImage());
    }

    private void displayImage(byte[] pImageData) {
	Image image = new Image(Display.getCurrent(), new ByteArrayInputStream(pImageData));
	imageScroller.setMinSize(image.getBounds().width, image.getBounds().height);
	imageContainer.setImage(image);
    }
}
