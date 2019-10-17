package org.emoflon.victory.ui.views;

public interface ISharedFocusElement {

	public void registerSharedFocus(ISharedFocusElement sharedFocusElement);

	public void focusRemoved();
}
