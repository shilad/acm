/*
 * @(#)GCompound.java   1.0 06/08/25
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

package acm.graphics;

import acm.util.ErrorException;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;

/* Class: GCompound */

/**
 * This class defines a graphical object that consists of a collection
 * of other graphical objects.  Once assembled, the internal objects
 * can be manipulated as a unit.
 */
public class GCompound extends GObject implements GContainer, GScalable {

/* Constructor: GCompound() */
/**
 * Creates a new <code>GCompound</code> object with no internal components.
 *
 * @usage GCompound gcomp = new GCompound();
 */
	public GCompound() {
		contents = new ArrayList();
		complete = false;
	}

/* Method: add(gobj) */
/**
 * Adds a new graphical object to this <code>GCompound</code>.
 *
 * @usage gcomp.add(gobj);
 * @param gobj The graphical object to add
 */
	public void add(GObject gobj) {
		if (complete) {
			throw new ErrorException("You can't add objects to a GCompound that has been "
			                         + "marked as complete.");
		}
		synchronized (contents) {
			if (gobj.getParent() != null) gobj.getParent().remove(gobj);
			gobj.setParent(this);
			contents.add(gobj);
		}
		repaint();
	}

/* Method: add(gobj, x, y) */
/**
 * Adds the graphical object to this canvas and sets its location
 * to the point (<code>x</code>,&nbsp;<code>y</code>).
 *
 * @usage gc.add(gobj, x, y);
 * @param gobj The graphical object to add
 * @param x The new x-coordinate for the object
 * @param y The new y-coordinate for the object
 */
	public final void add(GObject gobj, double x, double y) {
		add(gobj);
		gobj.setLocation(x, y);
	}

/* Method: add(gobj, pt) */
/**
 * Adds the graphical object to this canvas and sets its location to the specified point.
 *
 * @usage gc.add(gobj, pt);
 * @param gobj The graphical object to add
 * @param pt A <code>GPoint</code> object giving the coordinates of the point
 */
	public final void add(GObject gobj, GPoint pt) {
		add(gobj, pt.getX(), pt.getY());
	}

/* Method: remove(gobj) */
/**
 * Removes a graphical object from this <code>GCompound</code>.
 *
 * @usage gcomp.remove(gobj);
 * @param gobj The graphical object to remove
 */
	public void remove(GObject gobj) {
		if (complete) {
			throw new ErrorException("You can't remove objects from a GCompound that has been "
			                         + "marked as complete.");
		}
		synchronized (contents) {
			contents.remove(gobj);
			gobj.setParent(null);
		}
		repaint();
	}

/* Method: removeAll() */
/**
 * Removes all graphical objects from this <code>GCompound</code>.
 *
 * @usage gcomp.removeAll();
 */
	public void removeAll() {
		if (complete) {
			throw new ErrorException("You can't remove objects from a GCompound that has been "
			                         + "marked as complete.");
		}
		synchronized (contents) {
			contents.clear();
		}
		repaint();
	}

/* Method: getElementCount() */
/**
 * Returns the number of graphical objects stored in this container.
 *
 * @usage int n = gcomp.getElementCount();
 * @return The number of graphical objects in this container
 */
	public int getElementCount() {
		return contents.size();
	}

/* Method: getElement(index) */
/**
 * Returns the graphical object at the specified index, numbering from back
 * to front in the the <i>z</i> dimension.
 *
 * @usage GObject gobj = gcomp.getElement(index);
 * @param index The index of the component to return
 * @return The graphical object at the specified index
 */
	public GObject getElement(int index) {
		return (GObject) contents.get(index);
	}

/* Method: getElementAt(x, y) */
/**
 * Returns the topmost graphical object that contains the point
 * (<code>x</code>, <code>y</code>), or <code>null</code> if no such
 * object exists.  Note that these coordinates are relative to the
 * location of the compound object and not to the canvas in which
 * it is displayed.
 *
 * @usage GObject gobj = gcomp.getElementAt(x, y);
 * @param x The x-coordinate of the point being tested
 * @param y The y-coordinate of the point being tested
 * @return The graphical object at the specified location, or <code>null</code>
 *         if no such object exists
 */
	public GObject getElementAt(double x, double y) {
		synchronized (contents) {
			for (int i = getElementCount() - 1; i >= 0; i--) {
				GObject gobj = getElement(i);
				if (gobj.contains(x, y)) return gobj;
			}
		}
		return null;
	}

/* Method: getElementAt(pt) */
/**
 * Returns the topmost graphical object that contains the specified point,
 * or <code>null</code> if no such object exists.
 *
 * @usage GObject gobj = gc.getElementAt(pt);
 * @param pt The coordinates being tested
 * @return The graphical object at the specified location, or <code>null</code>
 *         if no such object exists
 */
	public final GObject getElementAt(GPoint pt) {
		return getElementAt(pt.getX(), pt.getY());
	}

/* Method: iterator() */
/**
 * Returns an <code>Iterator</code> that cycles through the elements within
 * this container in the default direction, which is from back to front.
 * You can also run the iterator in the opposite direction by using the
 * <a href="#iterator(int)"><code>iterator</code></a><code>(</code><font
 * size=-1><i>direction</i></font><code>)</code> form of this method.
 *
 * <p>Applets that want to run in browsers, however, should avoid using
 * this method, because <code>Iterator</code> is not supported on 1.1 browsers.
 * For maximum portability, you should rely instead on the
 * <a href="GContainer.html#getElementCount()"><code>getElementCount</code></a>
 * and <a href="GContainer.html#getElement(int)"><code>getElement</code></a> methods,
 * which provide the same functionality in a browser-compatible way.
 *
 * @usage Iterator i = gc.iterator();
 * @return An <code>Iterator</code> ranging over the elements of the
 *         container from back to front
 */
	public Iterator iterator() {
		return iterator(GContainer.BACK_TO_FRONT);
	}

/* Method: iterator(direction) */
/**
 * Returns an <code>Iterator</code> that cycles through the elements
 * within this container in the specified direction, which must be one
 * of the constants <a href="GContainer.html#FRONT_TO_BACK"><code>GContainer.FRONT_TO_BACK</code></a>
 * or <a href="GContainer.html#BACK_TO_FRONT"><code>GContainer.BACK_TO_FRONT</code></a>.<p>
 *
 * <code>&nbsp;&nbsp;for (Iterator i = gc.iterator(direction); i.hasNext(); )</code>
 *
 * <p>Applets that want to run in browsers, however, should avoid using
 * this method, because <code>Iterator</code> is not supported on 1.1 browsers.
 * For maximum portability, you should rely instead on the
 * <a href="GContainer.html#getElementCount()"><code>getElementCount</code></a>
 * and <a href="GContainer.html#getElement(int)"><code>getElement</code></a> methods,
 * which provide the same functionality in a browser-compatible way.
 *
 * @usage Iterator i = gc.iterator(direction);
 * @return An <code>Iterator</code> ranging over the elements of the
 *         container in the specified direction
 */
	public Iterator iterator(int direction) {
		return createIterator(this, direction);
	}

/* Method: paint(g) */
/**
 * Implements the <code>paint</code> operation for this graphical object.  This method
 * is not called directly by clients.
 * @noshow
 */
	public void paint(Graphics g) {
		g = g.create();
		g.translate(GMath.round(getX()), GMath.round(getY()));
		synchronized (contents) {
			int nElements = contents.size();
			for (int i = 0; i < nElements; i++) {
				((GObject) contents.get(i)).paintObject(g);
			}
		}
	}

/* Method: scale(sx, sy) */
/**
 * Scales every object contained in this compound by the scale factors
 * <code>sx</code> and <code>sy</code>.  Automatic repaint is turned off
 * during the scaling operation so that at most one repaint is performed.
 *
 * @usage gcomp.scale(sx, sy);
 * @param sx The factor used to scale all coordinates in the x direction
 * @param sy The factor used to scale all coordinates in the y direction
 */
	public void scale(double sx, double sy) {
		Component comp = getComponent();
		boolean oldAutoRepaint = false;
		if (comp instanceof GCanvas) {
			oldAutoRepaint = ((GCanvas) comp).getAutoRepaintFlag();
			((GCanvas) comp).setAutoRepaintFlag(false);
		}
		for (int i = getElementCount() - 1; i >= 0; i--) {
			GObject gobj = getElement(i);
			gobj.setLocation(sx * gobj.getX(), sy * gobj.getY());
			if (gobj instanceof GScalable) {
				((GScalable) gobj).scale(sx, sy);
			}
		}
		if (comp instanceof GCanvas) {
			((GCanvas) comp).setAutoRepaintFlag(oldAutoRepaint);
		}
		repaint();
	}

/* Method: scale(sf) */
/**
 * Scales the object on the screen by the scale factor <code>sf</code>, which applies
 * in both dimensions.
 *
 * @usage gcomp.scale(sf);
 * @param sf The factor used to scale all coordinates in both dimensions
 */
	public final void scale(double sf) {
		scale(sf, sf);
	}

/* Method: getBounds() */
/**
 * Returns the bounding rectangle for this compound object, which consists of
 * the union of the bounding rectangles for each of the components.
 *
 * @usage GRectangle bounds = gcomp.getBounds();
 * @return A <code>GRectangle</code> that bounds the components of this object
 */
	public GRectangle getBounds() {
		GRectangle bounds = new GRectangle();
		synchronized (contents) {
			int nElements = contents.size();
			for (int i = 0; i < nElements; i++) {
				if (i == 0) {
					bounds = new GRectangle(((GObject) contents.get(i)).getBounds());
				} else {
					bounds.add(((GObject) contents.get(i)).getBounds());
				}
			}
		}
		bounds.translate(getX(), getY());
		return bounds;
	}

/* Method: contains(x, y) */
/**
 * Checks to see whether a point is "inside" the compound, which means that it is
 * inside one of the components.
 *
 * @usage if (gcomp.contains(x, y)) . . .
 * @param x The x-coordinate of the point being tested
 * @param y The y-coordinate of the point being tested
 * @return <code>true</code> if the point (<code>x</code>,&nbsp;<code>y</code>) is inside
 *         the compound, and <code>false</code> otherwise
 */
	public boolean contains(double x, double y) {
		double cx = x - getX();
		double cy = y - getY();
		synchronized (contents) {
			int nElements = contents.size();
			for (int i = 0; i < nElements; i++) {
				if (((GObject) contents.get(i)).contains(cx, cy)) return true;
			}
		}
		return false;
	}

/* Method: getCanvasPoint(localPoint) */
/**
 * Converts the location of the specified point in this compound to
 * the corresponding point in the enclosing canvas.
 *
 * @usage canvasPoint = gcomp.getCanvasPoint(localPoint);
 * @param localPoint The coordinates in the space of the compound
 * @return The coordinates in the space of the enclosing <code>GCanvas</code>
 */
	public final GPoint getCanvasPoint(GPoint localPoint) {
		return getCanvasPoint(localPoint.getX(), localPoint.getY());
	}

/* Method: getCanvasPoint(x, y) */
/**
 * Converts the location of the specified point in this compound to
 * the corresponding point in the enclosing canvas.
 *
 * @usage canvasPoint = gcomp.getCanvasPoint(x, y);
 * @param x The x coordinate in the space of the compound
 * @param y The y coordinate in the space of the compound
 * @return The coordinates in the space of the enclosing <code>GCanvas</code>
 */

	public GPoint getCanvasPoint(double x, double y) {
		for (GContainer c = this; c instanceof GCompound; ) {
			GCompound comp = (GCompound) c;
			x += comp.getX();
			y += comp.getY();
			c = comp.getParent();
		}
		return new GPoint(x, y);
	}

/* Method: getLocalPoint(canvasPoint) */
/**
 * Converts the location of the specified point on the enclosing canvas
 * to the corresponding point in the space of this compound.
 *
 * @usage localPoint = gcomp.getLocalPoint(canvasPoint);
 * @param canvasPoint The coordinates in the space of the enclosing <code>GCanvas</code>
 * @return The coordinates in the space of the compound
 */
	public final GPoint getLocalPoint(GPoint canvasPoint) {
		return getLocalPoint(canvasPoint.getX(), canvasPoint.getY());
	}

/* Method: getLocalPoint(x, y) */
/**
 * Converts the specified point on the enclosing canvas to the
 * corresponding point in the space of this compound.
 *
 * @usage localPoint = gcomp.getCanvasPoint(x, y);
 * @param x The x coordinate in the space of the space of the enclosing <code>GCanvas</code>
 * @param y The y coordinate in the space of the space of the enclosing <code>GCanvas</code>
 * @return The coordinates in the space of the compound
 */

	public GPoint getLocalPoint(double x, double y) {
		for (GContainer c = this; c instanceof GCompound; ) {
			GCompound comp = (GCompound) c;
			x -= comp.getX();
			y -= comp.getY();
			c = comp.getParent();
		}
		return new GPoint(x, y);
	}

/* Protected method: markAsComplete() */
/**
 * Calling this method makes it illegal to add or remove elements from the
 * compound object.  Subclasses can invoke this method to protect the
 * integrity of the structure from changes by the client.
 *
 * @usage gcomp.markAsComplete();
 */
	public void markAsComplete() {
		complete = true;
	}

/* Inherited method: getSize() */
/**
 * @inherited GObject#GDimension getSize()
 * Returns the size of the bounding box for this object.
 */

/* Inherited method: getWidth() */
/**
 * @inherited GObject#double getWidth()
 * Returns the width of this object, which is defined to be
 * the width of the bounding box.
 */

/* Inherited method: getHeight() */
/**
 * @inherited GObject#double getHeight()
 * Returns the height of this object, which is defined to be
 * the height of the bounding box.
 */

/* Inherited method: setLocation(x, y) */
/**
 * @inherited GObject#void setLocation(double x, double y)
 * Sets the location of the <code>GCompound</code> to the point (<code>x</code>, <code>y</code>).
 */

/* Inherited method: setLocation(pt) */
/**
 * @inherited GObject#void setLocation(GPoint pt)
 * Sets the location of this object to the specified point.
 */

/* Inherited method: getLocation() */
/**
 * @inherited GObject#GPoint getLocation()
 * Returns the location of this object as a <code>GPoint</code>.
 */

/* Inherited method: getX() */
/**
 * @inherited GObject#double getX()
 * Returns the x-coordinate of the object.
 */

/* Inherited method: getY() */
/**
 * @inherited GObject#double getY()
 * Returns the y-coordinate of the object.
 */

/* Inherited method: move(dx, dy) */
/**
 * @inherited GObject#void move(double dx, double dy)
 * Moves the object on the screen using the displacements <code>dx</code> and <code>dy</code>.
 */

/* Inherited method: movePolar(r, theta) */
/**
 * @inherited GObject#void movePolar(double r, double theta)
 * Moves the object using displacements given in polar coordinates.
 */

/* Inherited method: contains(pt) */
/**
 * @inherited GObject#boolean contains(GPoint pt)
 * Checks to see whether a point is inside the object.
 */

/* Inherited method: sendToFront() */
/**
 * @inherited GObject#void sendToFront()
 * Moves this object to the front of the display in the <i>z</i> dimension.
 */

/* Inherited method: sendToBack() */
/**
 * @inherited GObject#void sendToBack()
 * Moves this object to the back of the display in the <i>z</i> dimension.
 */

/* Inherited method: sendForward() */
/**
 * @inherited GObject#void sendForward()
 * Moves this object one step toward the front in the <i>z</i> dimension.
 */

/* Inherited method: sendBackward() */
/**
 * @inherited GObject#void sendBackward()
 * Moves this object one step toward the back in the <i>z</i> dimension.
 */

/* Inherited method: setVisible(visible) */
/**
 * @inherited GObject#void setVisible(boolean visible)
 * Sets the visibility status of the <code>GCompound</code>.
 */

/* Inherited method: isVisible() */
/**
 * @inherited GObject#boolean isVisible()
 * Checks to see whether the object is visible.
 */

/* Inherited method: addMouseListener(listener) */
/**
 * @inherited GObject#void addMouseListener(MouseListener listener)
 * Adds a mouse listener to this graphical object.
 */

/* Inherited method: removeMouseListener(listener) */
/**
 * @inherited GObject#void removeMouseListener(MouseListener listener)
 * Removes a mouse listener from this graphical object.
 */

/* Inherited method: addMouseMotionListener(listener) */
/**
 * @inherited GObject#void addMouseMotionListener(MouseMotionListener listener)
 * Adds a mouse motion listener to this graphical object.
 */

/* Inherited method: removeMouseMotionListener(listener) */
/**
 * @inherited GObject#void removeMouseMotionListener(MouseMotionListener listener)
 * Removes a mouse motion listener from this graphical object.
 */

/* Protected method: sendToFront(gobj) */
/**
 * Implements the <code>sendToFront</code> function from the <code>GContainer</code>
 * interface.  Clients should not be calling this method, but the semantics of
 * interfaces forces it to be public.
 * @noshow
 */
	protected void sendToFront(GObject gobj) {
		synchronized (contents) {
			int index = contents.indexOf(gobj);
			if (index >= 0) {
				contents.remove(index);
				contents.add(gobj);
			}
		}
		repaint();
	}

/* Protected method: sendToBack(gobj) */
/**
 * Implements the <code>sendToBack</code> function from the <code>GContainer</code>
 * interface.  Clients should not be calling this method, but the semantics of
 * interfaces forces it to be public.
 * @noshow
 */
	protected void sendToBack(GObject gobj) {
		synchronized (contents) {
			int index = contents.indexOf(gobj);
			if (index >= 0) {
				contents.remove(index);
				contents.add(0, gobj);
			}
		}
		repaint();
	}

/* Protected method: sendForward(gobj) */
/**
 * Implements the <code>sendForward</code> function from the <code>GContainer</code>
 * interface.  Clients should not be calling this method, but the semantics of
 * interfaces forces it to be public.
 * @noshow
 */
	protected void sendForward(GObject gobj) {
		synchronized (contents) {
			int index = contents.indexOf(gobj);
			if (index >= 0) {
				contents.remove(index);
				contents.add(Math.min(contents.size(), index + 1), gobj);
			}
		}
		repaint();
	}

/* Protected method: sendBackward(gobj) */
/**
 * Implements the <code>sendBackward</code> function from the <code>GContainer</code>
 * interface.  Clients should not be calling this method, but the semantics of
 * interfaces forces it to be public.
 * @noshow
 */
	protected void sendBackward(GObject gobj) {
		synchronized (contents) {
			int index = contents.indexOf(gobj);
			if (index >= 0) {
				contents.remove(index);
				contents.add(Math.max(0, index - 1), gobj);
			}
		}
		repaint();
	}

/* Protected method: fireMouseListeners(e) */
/**
 * Dispatches a mouse event to the topmost child that covers the location
 * in the event <code>e</code>.
 * @noshow
 */
	protected void fireMouseListeners(MouseEvent e) {
		if (super.areMouseListenersEnabled()) {
			super.fireMouseListeners(e);
			return;
		}
		GPoint pt = new GPoint(e.getX() - getX(), e.getY() - getY());
		GObject gobj = getElementAt(pt);
		MouseEvent newEvent = null;
		if (gobj != lastObject) {
			if (lastObject != null) {
				newEvent = new GMouseEvent(lastObject, MouseEvent.MOUSE_EXITED, e);
				lastObject.fireMouseListeners(newEvent);
			}
			if (gobj != null) {
				newEvent = new GMouseEvent(gobj, MouseEvent.MOUSE_ENTERED, e);
				gobj.fireMouseListeners(newEvent);
			}
		}
		lastObject = gobj;
		if (dragObject != null) gobj = dragObject;
		if (gobj != null) {
			int id = e.getID();
			if (id != MouseEvent.MOUSE_EXITED && id != MouseEvent.MOUSE_ENTERED) {
				if (id == MouseEvent.MOUSE_PRESSED) {
					dragObject = gobj;
				} else if (id == MouseEvent.MOUSE_RELEASED) {
					dragObject = null;
				}
				newEvent = new GMouseEvent(gobj, id, e);
				gobj.fireMouseListeners(newEvent);
			}
		}
		if (newEvent != null && newEvent.isConsumed()) e.consume();
	}

/* Protected method: areMouseListenersEnabled() */
/**
 * Returns <code>true</code> if mouse listeners have ever been assigned to
 * this object or to any of the contained objects.
 *
 * @usage if (gcomp.areMouseListenersEnabled()) . . .
 * @return <code>true</code> if mouse listeners have been enabled in this object
 * @noshow
 */
	protected boolean areMouseListenersEnabled() {
		if (super.areMouseListenersEnabled()) return true;
		synchronized (contents) {
			int nElements = contents.size();
			for (int i = 0; i < nElements; i++) {
				GObject gobj = (GObject) contents.get(i);
				if (gobj.areMouseListenersEnabled()) return true;
			}
		}
		return false;
	}

/* Protected method: createIterator(container, direction) */
/**
 * Creates a new iterator for a container.  This method is broken out
 * as a static method because it is also called by <code>GCanvas</code>.
 * The method must use reflection to evaluate the iterator to avoid
 * having to complete the <code>GContainer</code> class, which will
 * not succeed under 1.0 browsers.
 */
	protected static Iterator createIterator(GContainer container, int direction) {
		try {
			Class iteratorClass = Class.forName("acm.graphics.GIterator");
			Class[] types = { Class.forName("acm.graphics.GContainer"), Integer.TYPE };
			Object[] args = { container, new Integer(direction) };
			Constructor constructor = iteratorClass.getConstructor(types);
			return (Iterator) constructor.newInstance(args);
		} catch (Exception ex) {
			throw new ErrorException("Unable to create an Iterator on this platform.");
		}
	}

/* Private state */

	private boolean complete;
	private ArrayList contents;
	private GObject lastObject;
	private GObject dragObject;
}

/* Package class: GIterator */

/**
 * Implements an iterator class for any object that implements
 * <code>GContainer</code> (i.e., <a href="GCanvas.html"><code>GCanvas</code></a>
 * and <a href="GCompound.html"><code>GCompound</code></a>).  The usual method
 * for using this class is to write something like</p>
 *
 * <code>&nbsp;&nbsp;for (Iterator i = gc.iterator(direction); i.hasNext(); )</code>
 *
 * where <code>gc</code> is the graphic container.  The enumeration supports
 * traversal in two directions.  By default, it starts with the front
 * element and works toward the back (as would be appropriate, for
 * example, when trying to find the topmost component for a mouse click).
 * You can, however, also process the elements of the container from back
 * to front (as would be useful when drawing elements of the container,
 * when the front objects should be drawn last).  To specify the direction
 * of the traversal, specify either <code>GContainer.FRONT_TO_BACK</code> or
 * <code>GContainer.BACK_TO_FRONT</code> in the <code>getEnumeration</code> call.
 */
class GIterator implements Iterator {

/* Constructor: GIterator(container, direction) */
/**
 * Creates a new <code>GIterator</code> that runs through the
 * container in the specified direction (<code>GContainer.FRONT_TO_BACK</code>
 * or <code>GContainer.BACK_TO_FRONT</code>).
 *
 * @usage Iterator i = new GIterator(container, direction);
 * @param container The <code>GContainer</code> whose elements the iterator should return
 * @param direction The direction in which to process the elements
 */
	public GIterator(GContainer container, int direction) {
		switch (direction) {
		  case GContainer.FRONT_TO_BACK: case GContainer.BACK_TO_FRONT:
			dir = direction;
			break;
		  default:
			throw new ErrorException("Illegal direction for iterator");
		}
		cont = container;
		index = 0;
		nElements = container.getElementCount();
	}

/* Method: hasNext() */
/**
 * Returns <code>true</code> if the iterator has more elements.  Implements
 * the <code>hasNext</code> method for the <code>Iterator</code> interface.
 *
 * @usage while (i.hasNext()) . . .
 * @return <code>true</code> if the iterator has more elements, <code>false</code> otherwise
 */
	public boolean hasNext() {
		return index < nElements;
	}

/* Method: next() */
/**
 * Returns the next element from the iterator.  Implements the <code>next</code>
 * method for the <code>Iterator</code> interface.
 *
 * @usage Object element = i.next();
 * @return The next element from the iterator
 */
	public Object next() {
		if (dir == GContainer.FRONT_TO_BACK) {
			return cont.getElement(nElements - index++ - 1);
		} else {
			return cont.getElement(index++);
		}
	}

/* Method: nextElement() */
/**
 * Returns the next element from the iterator as a <code>GObject</code>.  This
 * method is callable only if the iterator is declared as a <code>GIterator</code>.
 *
 * @usage GObject element = i.nextElement();
 * @return The next element from the iterator as a <code>GObject</code>
 */
	public GObject nextElement() {
		return (GObject) next();
	}

/* Method: remove() */
/**
 * Removes the current element from its container.  Implements the <code>remove</code>
 * method for the <code>Iterator</code> interface.
 *
 * @usage i.remove();
 */
	public void remove() {
		if (dir == GContainer.FRONT_TO_BACK) {
			cont.remove(cont.getElement(nElements - --index - 1));
		} else {
			cont.remove(cont.getElement(--index));
		}
		nElements--;
	}

/* Private state */

	private GContainer cont;
	private int dir;
	private int index;
	private int nElements;
}
