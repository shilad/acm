/*
 * @(#)TableLayout.java   1.0 06/08/25
 */

// ************************************************************************
// * Copyright (c) 2006 by the Association for Computing Machinery        *
// *                                                                      *
// * The Java Task Force seeks to impose few restrictions on the use of   *
// * these packages so that users have as much freedom as possible to     *
// * use this software in constructive ways and can make the benefits of  *
// * that work available to others.  In view of the legal complexities    *
// * of software development, however, it is essential for the ACM to     *
// * maintain its copyright to guard against attempts by others to        *
// * claim ownership rights.  The full text of the JTF Software License   *
// * is available at the following URL:                                   *
// *                                                                      *
// *          http://www.acm.org/jtf/jtf-software-license.pdf             *
// *                                                                      *
// ************************************************************************

package acm.gui;

import acm.util.ErrorException;
import acm.util.OptionTable;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.HashMap;

public class TableLayout implements LayoutManager2 {

/** Do not resize component */
	public static final int NONE = GridBagConstraints.NONE;

/** Resize component in horizontal direction only */
	public static final int HORIZONTAL = GridBagConstraints.HORIZONTAL;

/** Resize component in vertical direction only */
	public static final int VERTICAL = GridBagConstraints.VERTICAL;

/** Resize component in both directions */
	public static final int BOTH = GridBagConstraints.BOTH;

/** Center table in the container */
	public static final int CENTER = 10;

/** Align table horizontally at the left of its container */
	public static final int LEFT = 11;

/** Align table horizontally at the right of its container */
	public static final int RIGHT = 12;

/** Align table vertically at the top of its container */
	public static final int TOP = 13;

/** Align table vertically at the bottom of its container */
	public static final int BOTTOM = 14;

/** Expand table to fill its container */
	public static final int FILL = BOTH;

/* Constructor: TableLayout() */
/**
 * Creates a new <code>TableLayout</code> object with no limits
 * on the number of rows and columns.
 *
 * @usage TableLayout layout = new TableLayout();
 */
	public TableLayout() {
		this(0, 0);
	}

/* Constructor: TableLayout(rows, columns) */
/**
 * Creates a new <code>TableLayout</code> object with the specified
 * number of rows and columns.
 *
 * @usage TableLayout layout = new TableLayout(rows, columns);
 * @param rows The number of rows, or 0 for no limit
 * @param columns The number of columns, or 0 for no limit
 */
	public TableLayout(int rows, int columns) {
		this(rows, columns, 0, 0);
	}

/* Constructor: TableLayout(rows, columns, hgap, vgap) */
/**
 * Creates a new <code>TableLayout</code> object with the specified
 * row count, column count, alignment, horizontal gap, and vertical gap.
 *
 * @usage TableLayout layout = new TableLayout(rows, columns, hgap, vgap);
 * @param rows The number of rows, or 0 for no limit
 * @param columns The number of columns, or 0 for no limit
 * @param hgap The horizontal gap between columns
 * @param vgap The vertical gap between rows
 */
	public TableLayout(int rows, int columns, int hgap, int vgap) {
		nRows = rows;
		nColumns = columns;
		hGap = hgap;
		vGap = vgap;
		horizontalAlignment = CENTER;
		verticalAlignment = CENTER;
		defaultFill = BOTH;
		constraintTable = new HashMap();
		layoutTable = null;
	}

/* Method: setColumnCount(columns) */
/**
 * Resets the number of columns in the table.
 *
 * @usage layout.setColumnCount(columns);
 * @param columns The new number of columns
 */
	public void setColumnCount(int columns) {
		nColumns = columns;
		if (targetContainer != null) targetContainer.invalidate();
	}

/* Method: getColumnCount() */
/**
 * Returns the number of columns in the table.
 *
 * @usage int columns = layout.getColumnCount();
 * @return The  number of columns
 */
	public int getColumnCount() {
		return nColumns;
	}

/* Method: setRowCount(rows) */
/**
 * Resets the number of rows in the table.
 *
 * @usage layout.setRowCount(rows);
 * @param rows The new number of rows
 */
	public void setRowCount(int rows) {
		nRows = rows;
		if (targetContainer != null) targetContainer.invalidate();
	}

/* Method: getRowCount() */
/**
 * Returns the number of rows in the table.
 *
 * @usage int rows = layout.getRowCount();
 * @return The  number of rows
 */
	public int getRowCount() {
		return nRows;
	}

/* Method: setHorizontalAlignment(align) */
/**
 * Sets the horizontal alignment for the table.  The legal values
 * are <code>CENTER</code>, <code>LEFT</code>, <code>RIGHT</code>, and
 * <code>FILL</code>.
 *
 * @usage layout.setHorizontalAlignment(align);
 * @param align The horizontal alignment for the table
 */
	public void setHorizontalAlignment(int align) {
		horizontalAlignment = align;
		if (targetContainer != null) targetContainer.invalidate();
	}

/* Method: getHorizontalAlignment() */
/**
 * Returns the horizontal alignment for the table.
 *
 * @usage int align = layout.getHorizontalAlignment();
 * @return The horizontal alignment for the table
 */
	public int getHorizontalAlignment() {
		return horizontalAlignment;
	}

/* Method: setVerticalAlignment(align) */
/**
 * Sets the vertical alignment for the table.  The legal values
 * are <code>CENTER</code>, <code>TOP</code>, <code>BOTTOM</code>, and
 * <code>FILL</code>.
 *
 * @usage layout.setVerticalAlignment(align);
 * @param align The vertical alignment for the table
 */
	public void setVerticalAlignment(int align) {
		verticalAlignment = align;
		if (targetContainer != null) targetContainer.invalidate();
	}

/* Method: getVerticalAlignment() */
/**
 * Returns the vertical alignment for the table.
 *
 * @usage int align = layout.getVerticalAlignment();
 * @return The vertical alignment for the table
 */
	public int getVerticalAlignment() {
		return verticalAlignment;
	}

/* Method: setDefaultFill(fill) */
/**
 * Sets the default fill parameter for components in the table.  The legal values
 * are <code>NONE</code>, <code>HORIZONTAL</code>, <code>VERTICAL</code>, and
 * <code>BOTH</code>.
 *
 * @usage layout.setDefaultFill(fill);
 * @param fill The default fill parameter for components in the table
 */
	public void setDefaultFill(int fill) {
		defaultFill = fill;
		if (targetContainer != null) targetContainer.invalidate();
	}

/* Method: getDefaultFill() */
/**
 * Returns the default fill parameter for components in the table.
 *
 * @usage int fill = layout.getDefaultFill();
 * @return The default fill parameter for components in the table
 */
	public int getDefaultFill() {
		return defaultFill;
	}

/* Method: setHgap(pixels) */
/**
 * Sets the horizontal gap between components.
 *
 * @usage layout.setHgap(pixels);
 * @param pixels The gap between components in pixels
 */
	public void setHgap(int pixels) {
		hGap = pixels;
		if (targetContainer != null) targetContainer.invalidate();
	}

/* Method: getHgap() */
/**
 * Returns the horizontal gap between components.
 *
 * @usage int hgap = layout.getHgap();
 * @return The horizontal gap between components
 */
	public int getHgap() {
		return hGap;
	}

/* Method: setVgap(pixels) */
/**
 * Sets the vertical gap between components.
 *
 * @usage layout.setVgap(pixels);
 * @param pixels The gap between components in pixels
 */
	public void setVgap(int pixels) {
		vGap = pixels;
		if (targetContainer != null) targetContainer.invalidate();
	}

/* Method: getVgap() */
/**
 * Returns the vertical gap between components.
 *
 * @usage int vgap = layout.getVgap();
 * @return The vertical gap between components
 */
	public int getVgap() {
		return vGap;
	}

/* Method: setStrictGridBagModel(flag) */
/**
 * Sets a flag indicating whether the layout manager should match the
 * model used in <code>GridBagLayout</code> even when that interpretation
 * seems wrong.  The differences show up in the following areas:
 *
 * <ul>
 * <li><i>Calculation of cell size.</i>  <code>GridBagLayout</code> uses
 * the minimum layout size of the component to determine the size of each
 * cell; <code>TableLayout</code> uses the preferred size.  In practice,
 * these two values are often the same, but it seems that the preferred
 * size makes considerably more sense in terms of how this layout is used.
 *
 * <li><i>Treatment of cells spanning several rows.</i>  In
 * <code>GridBagLayout</code>, each new row begins after the last
 * multirow cell in that row, even if there is  space to its left.
 * By default, <code>TableLayout</code> uses the HTML model in which
 * the cells begin at the left margin unless that column is itself
 * already occupied.
 * </ul>
 *
 * @usage layout.setStrictGridBagModel(flag);
 * @param flag <code>true</code> to use a strict <code>GridBagLayout</code> model
 * @noshow
 */
	public void setStrictGridBagModel(boolean flag) {
		useStrictGridBagModel = flag;
		if (targetContainer != null) targetContainer.invalidate();
	}

/* Method: isStrictGridBagModel() */
/**
 * Returns <code>true</code> if this layout manager is treating multirow
 * cells exactly as <code>GridBagLayout</code> does.
 *
 * @usage if (layout.isStrictGridBagModel()) . . .
 * @return <code>true</code> if this manager is using the strict <code>GridBagLayout</code> model
 * @noshow
 */
	public boolean isStrictGridBagModel() {
		return useStrictGridBagModel;
	}

/* Method: setConstraints(comp, constraints) */
/**
 * Sets the constraints for the component to a copy of the supplied
 * constraints.
 *
 * @usage layout.setConstraints(comp, constraints);
 * @param comp The component to be constrained
 * @param constraints The constraints object used to specify the layout
 */
	public void setConstraints(Component comp, GridBagConstraints constraints) {
		constraintTable.put(comp, constraints.clone());
		if (targetContainer != null) targetContainer.invalidate();
	}

/* Method: setConstraints(comp, constraints) */
/**
 * Sets the constraints for the component to the constraints
 * specified by the string.
 *
 * @usage layout.setConstraints(comp, constraints);
 * @param comp The component to be constrained
 * @param constraints A string specifying the constraints
 */
	public void setConstraints(Component comp, String constraints) {
		setConstraints(comp, new TableConstraints(constraints));
	}

/* Method: getConstraints(comp) */
/**
 * Returns a copy of the constraints requested for the specified component.  The
 * constraints value is always converted to a <code>TableConstraints</code> so that
 * clients can use this class in preference to <code>GridBagConstraints</code>
 * without needing a type cast.
 *
 * @usage TableConstraints tc = layout.getConstraints(comp);
 * @param comp The component whose constraints are requested
 * @return A copy of the constraints object used to specify the layout
 */
	public TableConstraints getConstraints(Component comp) {
		GridBagConstraints gbc = lookupConstraints(comp);
		return (gbc == null) ? null : new TableConstraints(gbc);
	}


/* LayoutManager interface */

/* Method: addLayoutComponent(str, comp) */
/**
 * Adds a component to the layout.  Implements <code>LayoutManager</code>.
 *
 * @param str The constraint string
 * @param comp The component to be added
 * @noshow
 */
	public void addLayoutComponent(String constraints, Component comp) {
		addLayoutComponent(comp, constraints);
	}

/* Method: removeLayoutComponent(comp) */
/**
 * Removes the specified component from the layout.
 *
 * @param comp The component to be removed
 * @noshow
 */
	public void removeLayoutComponent(Component comp) {
		constraintTable.remove(comp);
		if (targetContainer != null) targetContainer.invalidate();
	}

/* Method: preferredLayoutSize(target) */
/**
 * Calculates the preferred size for the <code>FrameLayout</code> component
 * when laid out in the <code>target</code> container.
 *
 * @param target The container in which the layout is done
 * @return The preferred dimensions for the layout
 * @noshow
 */
	public Dimension preferredLayoutSize(Container target) {
		if (target.getComponentCount() == 0) return new Dimension(0, 0);
		return processLayout(target, PREFERRED_LAYOUT_SIZE_MODE);
	}

/* Method: minimumLayoutSize(target) */
/**
 * Calculates the minimum size for the <code>FrameLayout</code> component
 * when laid out in the <code>target</code> container.
 *
 * @param target The container in which the layout is done
 * @return The minimum dimensions for the layout
 * @noshow
 */
	public Dimension minimumLayoutSize(Container target) {
		if (target.getComponentCount() == 0) return new Dimension(0, 0);
		return processLayout(target, MINIMUM_LAYOUT_SIZE_MODE);
	}

/* Method: layoutContainer(target) */
/**
 * Lays out the components in the target container.
 *
 * @param target The container in which the layout is done
 * @noshow
 */
	public void layoutContainer(Container target) {
		targetContainer = target;
		processLayout(target, LAYOUT_CONTAINER_MODE);
	}

/* Method: addLayoutComponent(comp, constraints) */
/**
 * Adds a component to the layout.  The <code>TableLayout</code>
 * class overrides this method to allow for string constraints.
 *
 * @param comp The component to be added
 * @param constraints The constraint object
 * @noshow
 */
	public void addLayoutComponent(Component comp, Object constraints) {
		if (constraints == null) {
			constraints = new TableConstraints("");
			((TableConstraints) constraints).fill = defaultFill;
		} else if (constraints instanceof String) {
			OptionTable options = new OptionTable(((String) constraints).toLowerCase(), TableConstraints.LEGAL_KEYS);
			constraints = new TableConstraints(options);
			if (!options.isSpecified("fill")) {
				((TableConstraints) constraints).fill = (options.isSpecified("anchor")) ? NONE : defaultFill;
			}
		} else if (!(constraints instanceof GridBagConstraints)) {
			throw new ErrorException("TableLayout: Illegal constraints");
		}
		constraintTable.put(comp, constraints);
		if (targetContainer != null) targetContainer.invalidate();
	}

/* Method: maximumLayoutSize(target) */
/**
 * Calculates the maximum size for the <code>FrameLayout</code> component
 * when laid out in the <code>target</code> container.
 *
 * @param target The container in which the layout is done
 * @return The maximum dimensions for the layout
 * @noshow
 */
	public Dimension maximumLayoutSize(Container target) {
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

/* Method: getLayoutAlignmentX(target) */
/**
 * Returns the alignment along the <i>x</i>-axis as described in the
 * <code>LayoutManager2</code> interface.
 *
 * @param target The container in which the layout is done
 * @return A value between 0 and 1 indicating where this component should align
 * @noshow
 */
	public float getLayoutAlignmentX(Container target) {
		return 0.5F;
	}

/* Method: getLayoutAlignmentY(target) */
/**
 * Returns the alignment along the <i>y</i>-axis as described in the
 * <code>LayoutManager2</code> interface.
 *
 * @param target The container in which the layout is done
 * @return A value between 0 and 1 indicating where this component should align
 * @noshow
 */
	public float getLayoutAlignmentY(Container target) {
		return 0.5F;
	}

/* Method: invalidateLayout(target) */
/**
 * Indicates that the layout is no longer valid.
 *
 * @param target The container in which the layout is done
 * @noshow
 */
	public void invalidateLayout(Container target) {
		synchronized (target.getTreeLock()) {
			if (!layoutInProgress) layoutTable = null;
		}
	}

/* Override method: toString() */
/**
 * Creates a printable representation of the layout.
 * @noshow
 */
	public String toString() {
		String str = getClass().getName();
		str += "[rows=" + nRows + ",columns=" + nColumns;
		if (hGap != 0) str += ",hgap=" + hGap;
		if (vGap != 0) str += ",vgap=" + vGap;
		str += "]";
		return str;
	}

/* Protected method: lookupConstraints(comp) */
/**
 * Returns the constraints object for the specified component.
 *
 * @usage GridBagConstraints gbc = layout.lookupConstraints(comp);
 * @param comp The component to be constrained
 * @return The constraints object used to specify the layout
 */
	protected GridBagConstraints lookupConstraints(Component comp) {
		return (GridBagConstraints) constraintTable.get(comp);
	}

/* Protected method: lookupConstraints(comp, target) */
/**
 * Returns the <code>TableConstraints</code> object actually applied to the specified
 * component when it is laid out in the target container.  In the result of this method
 * the values of the <code>gridx</code>, <code>gridx</code>, <code>gridwidth</code>,
 * and <code>gridheight</code> fields are filled in according to the actual position
 * in the grid.
 *
 * @usage TableConstraints tc = layout.lookupConstraints(comp, target);
 * @param comp The component to be constrained
 * @param target The container in which the layout is done
 * @return The constraints object actually applied to the layout
 */
	protected TableConstraints lookupConstraints(Component comp, Container target) {
		synchronized (target.getTreeLock()) {
			if (layoutTable == null) computeLayoutTable(target);
			return (TableConstraints) layoutTable.get(comp);
		}
	}

/* Protected method: getMinimumComponentSize(comp) */
/**
 * This method returns the minimum size of the component for this layout.  Subclasses
 * can override this methods to establish specific bounds for certain component
 * classes, such as the code for scrollbars given here.
 *
 * @usage Dimension size = layout.getMinimumComponentSize(comp);
 * @param comp The component whose size is being tested
 * @return The minimum size for that component
 */
	private Dimension getMinimumComponentSize(Component comp) {
		if (isScrollbar(comp)) return getMinimumScrollbarSize(comp);
		return comp.getMinimumSize();
	}

/* Protected method: getPreferredComponentSize(comp) */
/**
 * This method returns the preferred size of the component for this layout.  Subclasses
 * can override this methods to establish specific bounds for certain component
 * classes, such as the code for scrollbars given here.
 *
 * @usage Dimension size = layout.getPreferredComponentSize(comp);
 * @param comp The component whose size is being tested
 * @return The preferred size for that component
 */
	private Dimension getPreferredComponentSize(Component comp) {
		if (isScrollbar(comp)) return getMinimumScrollbarSize(comp);
		return comp.getPreferredSize();
	}

/* Protected method: isScrollbar(comp) */
/**
 * Determines whether the component is a scroll bar.  This code is complicated only
 * because of the patch system that allows this code to work in JDK1.1 browsers.
 * If the patch system is loaded, button classes will not ordinarily be a
 * subclass of Scrollbar or JScrollBar but will implement the ScrollbarLike interface.
 */
	private boolean isScrollbar(Component comp) {
		if (comp instanceof Scrollbar || comp instanceof JScrollBar) return true;
		try {
			return Class.forName("ScrollbarLike").isInstance(comp);
		} catch (Exception ex) {
			return false;
		}
	}

/* Protected method: getMinimumScrollbarSize(comp) */
/**
 * Determines the minimum dimensions for this scrollbar, making sure it has enough
 * space to be displayed with a nonzero slider area.
 */
	private Dimension getMinimumScrollbarSize(Component comp) {
		Dimension size = comp.getMinimumSize();
		try {
			Class scrollbarClass = comp.getClass();
			Method getOrientation = scrollbarClass.getMethod("getOrientation", new Class[0]);
			int orientation = ((Integer) getOrientation.invoke(comp, new Object[0])).intValue();
			if (orientation == Scrollbar.HORIZONTAL) {
				size.width = Math.max(size.width, MINIMUM_SCROLLBAR_SIZE);
			} else {
				size.height = Math.max(size.height, MINIMUM_SCROLLBAR_SIZE);
			}
		} catch (Exception ex) {
			throw new ErrorException(ex);
		}
		return size;
	}

/* Private method: processLayout(target, caller) */
/**
 * Common code for the different methods requiring at least some
 * layout computation. The caller is one of the three constants
 * LAYOUT_CONTAINER_MODE, PREFERRED_LAYOUT_SIZE_MODE, or MINIMUM_LAYOUT_SIZE_MODE,
 * depending on the caller.
 */
	private Dimension processLayout(Container target, int caller) {
		Dimension result = null;
		synchronized (target.getTreeLock()) {
			layoutInProgress = true;
			if (layoutTable == null) computeLayoutTable(target);
			int nr = getIntProperty("nRows");
			int nc = getIntProperty("nColumns");
			int[] heights = new int[nr];
			int[] widths = new int[nc];
			int nComponents = target.getComponentCount();
			for (int i = 0; i < nComponents; i++) {
				Component comp = target.getComponent(i);
				TableConstraints tc = (TableConstraints) layoutTable.get(comp);
				int column = tc.gridx;
				int row = tc.gridy;
				int width = getIntProperty("width" + column);
				int height = getIntProperty("height" + row);
				if (width == 0) {
					if (caller == MINIMUM_LAYOUT_SIZE_MODE || useStrictGridBagModel) {
						width = getMinimumComponentSize(comp).width;
					} else {
						width = getPreferredComponentSize(comp).width;
					}
					width += 2 * tc.ipadx + tc.insets.left + tc.insets.right;
				}
				if (height == 0) {
					if (caller == MINIMUM_LAYOUT_SIZE_MODE || useStrictGridBagModel) {
						height = getMinimumComponentSize(comp).height;
					} else {
						height = getPreferredComponentSize(comp).height;
					}
					height += 2 * tc.ipady + tc.insets.top + tc.insets.bottom;
				}
				widths[column] = Math.max(widths[column], width);
				heights[row] = Math.max(heights[row], height);
			}
			int width = hGap;
			int height = vGap;
			double[] weightX = new double[nc];
			double[] weightY = new double[nr];
			double totalX = 0;
			double totalY = 0;
			for (int column = 0; column < nc; column++) {
				width += widths[column] + hGap;
				weightX[column] = getDoubleProperty("weightx" + column);
				totalX += weightX[column];
			}
			for (int row = 0; row < nr; row++) {
				height += heights[row] + vGap;
				weightY[row] = getDoubleProperty("weighty" + row);
				totalY += weightY[row];
			}
			if (caller == LAYOUT_CONTAINER_MODE) {
				Dimension size = target.getSize();
				Insets insets = target.getInsets();
				size.width -= insets.left + insets.right;
				size.height -= insets.top + insets.bottom;
				int extraX = size.width - width;
				int extraY = size.height - height;
				int startX = insets.left;
				int startY = insets.top;
				if (totalX == 0) {
					switch (horizontalAlignment) {
					  case LEFT:
						extraX = 0;
						break;
					  case CENTER:
						startX += extraX / 2;
						extraX = 0;
						break;
					  case RIGHT:
						startX += extraX;
						extraX = 0;
						break;
					  case FILL:
						totalX = nc;
						for (int column = 0; column < nc; column++) {
							weightX[column] = 1;
						}
						break;
					}
				}
				if (totalY == 0) {
					switch (verticalAlignment) {
					  case TOP:
						extraY = 0;
						break;
					  case CENTER:
						startY += extraY / 2;
						extraY = 0;
						break;
					  case BOTTOM:
						startY += extraY;
						extraY = 0;
						break;
					  case FILL:
						totalY = nr;
						for (int row = 0; row < nr; row++) {
							weightY[row] = 1;
						}
						break;
					}
				}
				int[] xc = new int[nc];
				int x = hGap + startX;
				for (int column = 0; column < nc; column++) {
					xc[column] = x;
					if (extraX > 0) {
						int pad = (int) Math.round(extraX * weightX[column] / totalX);
						widths[column] += pad;
						extraX -= pad;
						totalX -= weightX[column];
					}
					x += widths[column] + hGap;
				}
				int[] yc = new int[nr];
				int y = vGap + startY;
				for (int row = 0; row < nr; row++) {
					yc[row] = y;
					if (extraY > 0) {
						int pad = (int) Math.round(extraY * weightY[row] / totalY);
						heights[row] += pad;
						extraY -= pad;
						totalY -= weightY[row];
					}
					y += heights[row] + vGap;
				}
				for (int i = 0; i < nComponents; i++) {
					Component comp = target.getComponent(i);
					TableConstraints tc = (TableConstraints) layoutTable.get(comp);
					int column = tc.gridx;
					int row = tc.gridy;
					int bx = xc[column] + tc.insets.left;
					int by = yc[row] + tc.insets.top;
					int bw = widths[column];
					for (int ix = 1; ix < tc.gridwidth && column + ix < nc; ix++) {
						bw += widths[column + ix] + hGap;
					}
					bw -= tc.insets.left + tc.insets.right;
					int bh = heights[row];
					for (int iy = 1; iy < tc.gridheight && row + iy < nr; iy++) {
						bh += heights[row + iy] + vGap;
					}
					bh -= tc.insets.top + tc.insets.bottom;
					Dimension pSize = (useStrictGridBagModel) ? getMinimumComponentSize(comp) : getPreferredComponentSize(comp);
					Rectangle bounds = computeCellBounds(new Rectangle(bx, by, bw, bh), pSize, tc);
					comp.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
				}
			} else {
				result = new Dimension(width, height);
			}
			layoutInProgress = false;
		}
		return result;
	}

/* Private method: computeLayoutTable(target) */
/**
 * Updates the layoutTable constraints for each component in <code>target</code>.
 * All elements whose <code>gridx</code> or <code>gridy</code> constraints
 * are <code>RELATIVE</code> are changed to explicit grid references by
 * interpreting the <code>gridwidth</code> and <code>gridheight</code>
 * constraints.
 *
 * @usage computeLayoutTable(target);
 * @param target The target container
 */
	private void computeLayoutTable(Container target) {
		int[] unfinishedSpans = null;
		int row = 0;
		int column = 0;
		int layoutColumns = nColumns;
		int nComponents = target.getComponentCount();
		boolean nextEndRow = false;
		layoutTable = new HashMap();
		for (int i = 0; i < nComponents; i++) {
			Component comp = target.getComponent(i);
			TableConstraints tc = getConstraints(comp);
			if (tc.gridx != GridBagConstraints.RELATIVE) column = tc.gridx;
			if (tc.gridy != GridBagConstraints.RELATIVE) row = tc.gridy;
			if (nRows > 0 && row >= nRows) {
				throw new ErrorException("TableLayout: Too many rows specified");
			}
			while (unfinishedSpans != null && column < unfinishedSpans.length && unfinishedSpans[column] > 0) {
				column++;
				if (layoutColumns > 0 && column >= layoutColumns) {
					for (int c = 0; c < unfinishedSpans.length; c++) {
						if (unfinishedSpans[c] > 0) unfinishedSpans[c]--;
					}
					row++;
					column = getFirstAvailableColumn(unfinishedSpans);
				}
			}
			tc.gridx = column;
			tc.gridy = row;
			boolean endrow = nextEndRow;
			setMaxProperty("width" + column, tc.width);
			setMaxProperty("height" + row, tc.height);
			setMaxProperty("weightx" + column, tc.weightx);
			setMaxProperty("weighty" + row, tc.weighty);
			int colspan = 1;
			switch (tc.gridwidth) {
			  case GridBagConstraints.REMAINDER:
				endrow = true;
				if (layoutColumns > 0) colspan = layoutColumns - column;
				break;
			  case GridBagConstraints.RELATIVE:
				if (layoutColumns <= 0) {
					throw new ErrorException("TableLayout: Illegal to use gridwidth=RELATIVE in first row");
				}
				colspan = layoutColumns - column - 1;
				nextEndRow = true;
				break;
			  default:
				colspan = tc.gridwidth;
				endrow = (nColumns > 0 && column + tc.gridwidth >= nColumns);
				break;
			}
			if (colspan > 1 && unfinishedSpans != null) {
				for (int c = column; c < Math.min(unfinishedSpans.length, column + colspan); c++) {
					if (unfinishedSpans[c] != 0) {
						throw new ErrorException("TableLayout: Overlapping cells");
					}
				}
			}
			int rowspan = 1;
			switch (tc.gridheight) {
			  case GridBagConstraints.REMAINDER:
				rowspan = Integer.MAX_VALUE;
				break;
			  case GridBagConstraints.RELATIVE:
				throw new ErrorException("TableLayout: Illegal to use gridheight=RELATIVE");
			  default:
				rowspan = tc.gridheight;
				break;
			}
			if (rowspan > 1) {
				if (unfinishedSpans == null) {
					unfinishedSpans = new int[column + colspan];
				} else if (unfinishedSpans.length < column + colspan) {
					int[] oldSpans = unfinishedSpans;
					unfinishedSpans = new int[column + colspan];
					System.arraycopy(oldSpans, 0, unfinishedSpans, 0, oldSpans.length);
				}
				for (int c = column; c < column + colspan; c++) {
					unfinishedSpans[c] = rowspan;
				}
			}
			tc.gridwidth = colspan;
			tc.gridheight = rowspan;
			layoutTable.put(comp, tc);
			column += colspan;
			while (endrow || (layoutColumns > 0 && column >= layoutColumns)) {
				if (layoutColumns <= 0) layoutColumns = column;
				if (unfinishedSpans != null) {
					for (int c = 0; c < unfinishedSpans.length; c++) {
						if (unfinishedSpans[c] > 0) unfinishedSpans[c]--;
					}
				}
				row++;
				column = getFirstAvailableColumn(unfinishedSpans);
				endrow = false;
			}
		}
		if (layoutColumns <= 0) layoutColumns = column;
		setIntProperty("nColumns", layoutColumns);
		setIntProperty("nRows", row + 1);
	}

/* Private method: computeCellBounds(enclosure, psize, tc) */
/**
 * Computes the bounds for the cell based on the bounds of the enclosure,
 * the preferred size of the object, and the constraints in tc.
 */
	private Rectangle computeCellBounds(Rectangle enclosure, Dimension psize, TableConstraints tc) {
		int x = enclosure.x;
		int y = enclosure.y;
		int width = enclosure.width;
		int height = enclosure.height;
		if (tc.fill == TableConstraints.NONE || tc.fill == TableConstraints.VERTICAL) {
			width = psize.width;
		}
		if (tc.fill == TableConstraints.NONE || tc.fill == TableConstraints.HORIZONTAL) {
			height = psize.height;
		}
		if (width != enclosure.width) {
			switch (tc.anchor) {
			  case TableConstraints.NORTH: case TableConstraints.CENTER: case TableConstraints.SOUTH:
				x += (enclosure.width - width) / 2;
				break;
			  case TableConstraints.NORTHEAST: case TableConstraints.EAST: case TableConstraints.SOUTHEAST:
				x += enclosure.width - width;
				break;
			}
		}
		if (height != enclosure.height) {
			switch (tc.anchor) {
			  case TableConstraints.WEST: case TableConstraints.CENTER: case TableConstraints.EAST:
				y += (enclosure.height - height) / 2;
				break;
			  case TableConstraints.SOUTHWEST: case TableConstraints.SOUTH: case TableConstraints.SOUTHEAST:
				y += enclosure.height - height;
				break;
			}
		}
		return new Rectangle(x, y, width, height);
	}

/* Private method: setMaxProperty(key, value) */
/**
 * Sets the property for the specified key to the maximum of its
 * previous value and the specified new value.
 */
	private void setMaxProperty(String key, int value) {
		setIntProperty(key, Math.max(value, getIntProperty(key)));
	}

/* Private method: setMaxProperty(key, value) */
/**
 * Sets the property for the specified key to the maximum of its
 * previous value and the specified new value.
 */
	private void setMaxProperty(String key, double value) {
		setDoubleProperty(key, Math.max(value, getDoubleProperty(key)));
	}

/* Private method: setIntProperty(key, value) */
/**
 * Sets the property for the specified key to the integer value.
 */
	private void setIntProperty(String key, int value) {
		layoutTable.put(key, new Integer(value));
	}

/* Private method: getIntProperty(key) */
/**
 * Gets the integer property associated with the specified key.  If no value
 * has been set, the method returns 0.
 */
	private int getIntProperty(String key) {
		Object binding = layoutTable.get(key);
		if (binding == null) return 0;
		return ((Integer) binding).intValue();
	}

/* Private method: setDoubleProperty(key, value) */
/**
 * Sets the property for the specified key to the double value.
 */
	private void setDoubleProperty(String key, double value) {
		layoutTable.put(key, new Double(value));
	}

/* Private method: getDoubleProperty(key) */
/**
 * Gets the double property associated with the specified key.  If no value
 * has been set, the method returns 0.
 */
	private double getDoubleProperty(String key) {
		Object binding = layoutTable.get(key);
		if (binding == null) return 0.0;
		return ((Double) binding).doubleValue();
	}

/* Private method: getFirstAvailableColumn(unfinishedSpans) */
/**
 * Gets the first available column in the next row of the table,
 * taking into account whether multirow cells are being handled
 * as in GridBagLayout.
 */
	private int getFirstAvailableColumn(int[] unfinishedSpans) {
		if (useStrictGridBagModel && unfinishedSpans != null) {
			for (int column = unfinishedSpans.length; column > 0; column--) {
				if (unfinishedSpans[column - 1] > 0) return column;
			}
		}
		return 0;
	}

/* Private constants */
	private static final int LAYOUT_CONTAINER_MODE = 0;
	private static final int MINIMUM_LAYOUT_SIZE_MODE = 1;
	private static final int PREFERRED_LAYOUT_SIZE_MODE = 2;

	private static final int MINIMUM_SCROLLBAR_SIZE = 100;


/* Private instance variables */

	private Container targetContainer;
	private HashMap constraintTable;
	private HashMap layoutTable;
	private boolean useStrictGridBagModel;
	private boolean layoutInProgress;
	private int nRows;
	private int nColumns;
	private int horizontalAlignment;
	private int verticalAlignment;
	private int defaultFill;
	private int hGap;
	private int vGap;

}
