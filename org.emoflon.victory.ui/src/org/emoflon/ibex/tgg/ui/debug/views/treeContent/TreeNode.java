package org.emoflon.ibex.tgg.ui.debug.views.treeContent;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextStyle;
import org.emoflon.ibex.tgg.ui.debug.core.VictoryUI;

public abstract class TreeNode {
	private String font = "Monospaced";
	private int fontSize = 10;
	private int fontStyle = SWT.NORMAL;
	private boolean strikethrough = false;

	private TreeNode parent;
	private Collection<TreeNode> children = new HashSet<>();

	public void addChild(TreeNode node) {
		if (node.parent != null)
			throw new IllegalStateException("The specified node is already the child of another node");

		children.add(node);
		node.parent = this;
	}

	public void removeChild(TreeNode node) {
		if (children.contains(node)) {
			children.remove(node);
			node.parent = null;
		}
	}

	public void clearChildren() {
		for (TreeNode child : children)
			removeChild(child);
	}

	public void removeFromParent() {
		parent.removeChild(this);
	}

	public final StyledString getCellLabel() {
		return new StyledString(getLabel(), new Styler() {
			@Override
			public void applyStyles(TextStyle pTextStyle) {
				pTextStyle.font = FontDescriptor.createFrom(new FontData(font, fontSize, fontStyle))
						.createFont(VictoryUI.getDisplay());
				pTextStyle.strikeout = strikethrough;
			}
		});
	}

	public void setFont(String pFont) {
		font = pFont;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public void setFontStyle(int fontStyle) {
		this.fontStyle = fontStyle;
	}

	public void setStrikethrough(boolean strikethrough) {
		this.strikethrough = strikethrough;
	}

	protected abstract String getLabel();

	protected abstract String getToolTip();

	protected abstract Image getImage();

	protected abstract Color getForeground();

	protected abstract Color getBackground();

	/**
	 * Called when this node is disposed. Specifically intended to dispose of any
	 * image resources that might have been cached by a subclass.<br>
	 * Recursively calls this method on its children. Any subclasses that override
	 * this method must take care to call this implementation at some point.
	 */
	protected void dispose() {
		for (TreeNode child : children)
			child.dispose();
	}

	public TreeNode getParent() {
		return parent;
	}

	public Collection<TreeNode> getChildren() {
		return children;
	}

	public boolean hasChildren() {
		return !children.isEmpty();
	}

	public final static class TreeRootNode extends TreeNode {
		@Override
		public String getLabel() {
			return null;
		}

		@Override
		public Image getImage() {
			return null;
		}

		@Override
		protected Color getForeground() {
			return null;
		}

		@Override
		protected Color getBackground() {
			return null;
		}

		@Override
		protected String getToolTip() {
			return null;
		}
	}
}
