package org.emoflon.ibex.tgg.ui.debug.views.visualisable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import net.sourceforge.plantuml.SourceStringReader;

public abstract class VisualisableElement {

	private String visualisationString;
	private byte[] image;
	private boolean isInvalid = true;

	/**
	 * Returns the image for this VisualisationElement. If the element was
	 * invalidated, the image is refreshed first.
	 * 
	 * @return the image for this VisualisationElement
	 */
	public byte[] getImage() {
		if (isInvalid)
			refresh();

		return image;
	}

	/**
	 * Refreshes the image for this VisualisationElement.
	 */
	public void refresh() {
		String newVisualisation = generateVisualisationString();
		if (!Objects.equals(visualisationString, newVisualisation)) {
			visualisationString = newVisualisation;
			generatePlantUMLImage();
		}
		isInvalid = false;
	}

	/**
	 * Invalidates the cached image for this VisualisableElement.<br>
	 * Calling this method will cause the next call to <code>getImage()</code> to
	 * trigger a refresh before returning the image.
	 */
	public void invalidate() {
		isInvalid = true;
	}

	protected abstract String generateVisualisationString();

	private void generatePlantUMLImage() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			new SourceStringReader(visualisationString).outputImage(outputStream);
		} catch (IOException pIOE) {
		}
		image = outputStream.toByteArray();
	}
}
