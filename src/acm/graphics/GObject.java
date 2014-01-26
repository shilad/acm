/*
 * @(#)GObject.java   1.0 06/08/25
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
import acm.util.JTFTools;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Method;

/* Abstract class: GObject */

/**
 * This class is the common superclass of all graphical objects that can
 * be displayed on a <a href="GCanvas.html"><code>GCanvas</code></a>.
 * Because it is an abstract class, you are not allowed to construct an
 * object whose class is <code>GObject</code> directly.  What you do
 * instead is construct one of the concrete subclasses like
 * <a href="GRect.html"><code>GRect</code></a> or
 * <a href="GLine.html"><code>GLine</code></a>.
 * The purpose of this class definition is to define methods that apply
 * to all graphical objects regardless of their specific class.
 */
public abstract class GObject implements Cloneable {


/* Protected constructor: GObject() */
/**
 * Constructs a new <code>GObject</code> and initializes its state.  This
 * constructor is never called explicitly, but is instead invoked by the
 * constructors of its subclasses.
 */
	protected GObject() {
		isVisible = true;
		mouseListenersEnabled = false;
	}

/* Abstract method: paint(g) */
/**
 * All subclasses of <code>GObject</code> must define a <code>paint</code>
 * method which allows the object to draw itself on the <code>Graphics</code>
 * context passed in as the parameter <code>g</code>.
 *
 * @usage gobj.paint(g);
 * @param g The graphics context into which the painting is done
 */
	public abstract void paint(Graphics g);

/* Abstract method: getBounds() */
/**
 * Returns the bounding box of this object, which is defined to be the
 * smallest rectangle that covers everything drawn by the figure.  The
 * coordinates of this rectangle do not necessarily match the location
 * returned by <a href="#getLocation()"><code>getLocation</code></a>.
 * Given a <a href="GLabel.html><code>GLabel</code></a> object, for
 * example, <a href="#getLocation()"><code>getLocation</code></a>
 * returns the coordinates of the point on the baseline at which the
 * string begins; <code>getBounds</code>, by contrast, returns a
 * rectangle that covers the entire window area occupied by the string.
 *
 * @usage GRectangle bounds = gobj.getBounds();
 * @return The bounding box for this object
 */
	public abstract GRectangle getBounds();

/* Method: setLocation(x, y) */
/**
 * Sets the location of this object to the point (<code>x</code>, <code>y</code>).
 *
 * @usage gobj.setLocation(x, y);
 * @param x The new x-coordinate for the object
 * @param y The new y-coordinate for the object
 */
	public void setLocation(double x, double y) {
		xc = x;
		yc = y;
		repaint();
	}

/* Method: setLocation(pt) */
/**
 * Sets the location of this object to the specified point.
 *
 * @usage gobj.setLocation(pt);
 * @param pt The new location for this object
 * @noshow
 */
	public final void setLocation(GPoint pt) {
		setLocation(pt.getX(), pt.getY());
	}

/* Method: getLocation() */
/**
 * Returns the location of this object as a <code>GPoint</code>.
 *
 * @usage GPoint pt = gobj.getLocation();
 * @return The location of this object as a <code>GPoint</code>
 */
	public GPoint getLocation() {
		return new GPoint(xc, yc);
	}

/* Method: getX() */
/**
 * Returns the x-coordinate of the object.
 *
 * @usage double x = gobj.getX();
 * @return The x-coordinate of the object
 */
	public double getX() {
		return xc;
	}

/* Method: getY() */
/**
 * Returns the y-coordinate of the object.
 *
 * @usage double y = gobj.getY();
 * @return The y-coordinate of the object
 */
	public double getY() {
		return yc;
	}

/* Method: move(dx, dy) */
/**
 * Moves the object on the screen using the displacements <code>dx</code> and <code>dy</code>.
 *
 * @usage gobj.move(dx, dy);
 * @param dx The distance to move the object in the x direction (positive is rightward)
 * @param dy The distance to move the object in the y direction (positive is downward)
 */
	public void move(double dx, double dy) {
		setLocation(xc + dx, yc + dy);
	}

/* Method: movePolar(r, theta) */
/**
 * Moves the object using displacements given in polar coordinates.  The
 * parameter <code>r</code> specifies the distance to move and <code>theta</code>
 * specifies the angle in which the motion occurs.  The angle is measured in
 * degrees increasing counterclockwise from the +x axis.
 *
 * @usage gobj.movePolar(r, theta);
 * @param r The distance to move
 * @param theta The angle in which to move, measured in degrees
 *              increasing counterclockwise from the +x axis
 */
	public final void movePolar(double r, double theta) {
		double radians = theta * Math.PI / 180;
		move(r * Math.cos(radians), -r * Math.sin(radians));
	}

/* Method: getSize() */
/**
 * Returns the size of the bounding box for this object.
 *
 * @usage GDimension size = gobj.getSize();
 * @return The size of this object
 */
	public GDimension getSize() {
		GRectangle bounds = getBounds();
		return new GDimension(bounds.getWidth(), bounds.getHeight());
	}

/* Method: getWidth() */
/**
 * Returns the width of this object, which is defined to be
 * the width of the bounding box.
 *
 * @usage double width = gobj.getWidth();
 * @return The width of this object on the screen
 */
	public double getWidth() {
		return getBounds().getWidth();
	}

/* Method: getHeight() */
/**
 * Returns the height of this object, which is defined to be
 * the height of the bounding box.
 *
 * @usage double height = gobj.getHeight();
 * @return The height of this object on the screen
 */
	public double getHeight() {
		return getBounds().getHeight();
	}

/* Method: contains(x, y) */
/**
 * Checks to see whether a point is inside the object.  By default, this
 * method simply checks to see if the point is inside the bounding box.
 * Many subclasses will need to override this to check whether the point
 * is contained in the shape.
 *
 * @usage if (gobj.contains(x, y)) . . .
 * @param x The x-coordinate of the point being tested
 * @param y The y-coordinate of the point being tested
 * @return <code>true</code> if the point (<code>x</code>,&nbsp;<code>y</code>) is inside
 *         the object, and <code>false</code> otherwise
 */
	public boolean contains(double x, double y) {
		return getBounds().contains(GMath.round(x), GMath.round(y));
	}

/* Method: contains(pt) */
/**
 * Checks to see whether a point is inside the object.
 *
 * @usage if (gobj.contains(pt)) . . .
 * @param pt The point being tested
 * @return <code>true</code> if the point is inside the object, and <code>false</code> otherwise
 */
	public final boolean contains(GPoint pt) {
		return contains(pt.getX(), pt.getY());
	}

/* Method: sendToFront() */
/**
 * Moves this object to the front of the display in the <i>z</i> dimension.  By
 * moving it to the front, this object will appear to be on top of the other graphical
 * objects on the display and may hide any objects that are further back.
 *
 * @usage gobj.sendToFront();
 */
	public void sendToFront() {
		if (myParent == null) return;
		if (myParent instanceof GCanvas) {
			((GCanvas) myParent).sendToFront(this);
		} else if (myParent instanceof GCompound) {
			((GCompound) myParent).sendToFront(this);
		} else {
			try {
				Class parentClass = myParent.getClass();
				Class[] types = { Class.forName("acm.graphics.GObject") };
				Object[] args = { this };
				Method fn = parentClass.getMethod("sendToFront", types);
				if (fn != null) fn.invoke(myParent, args);
			} catch (Exception ex) {
				/* Empty */
			}
		}
		if (mouseListenersEnabled) updateEnabledList();
	}

/* Method: sendToBack() */
/**
 * Moves this object to the back of the display in the <i>z</i> dimension.  By
 * moving it to the back, this object will appear to be behind the other graphical
 * objects on the display and may be obscured by other objects in front.
 *
 * @usage gobj.sendToBack();
 */
	public void sendToBack() {
		if (myParent == null) return;
		if (myParent instanceof GCanvas) {
			((GCanvas) myParent).sendToBack(this);
		} else if (myParent instanceof GCompound) {
			((GCompound) myParent).sendToBack(this);
		} else {
			try {
				Class parentClass = myParent.getClass();
				Class[] types = { Class.forName("acm.graphics.GObject") };
				Object[] args = { this };
				Method fn = parentClass.getMethod("sendToBack", types);
				if (fn != null) fn.invoke(myParent, args);
			} catch (Exception ex) {
				/* Empty */
			}
		}
		if (mouseListenersEnabled) updateEnabledList();
	}

/* Method: sendForward() */
/**
 * Moves this object one step toward the front in the <i>z</i> dimension.
 * If it was already at the front of the stack, nothing happens.
 *
 * @usage gobj.sendForward();
 */
	public void sendForward() {
		if (myParent == null) return;
		if (myParent instanceof GCanvas) {
			((GCanvas) myParent).sendForward(this);
		} else if (myParent instanceof GCompound) {
			((GCompound) myParent).sendForward(this);
		} else {
			try {
				Class parentClass = myParent.getClass();
				Class[] types = { Class.forName("acm.graphics.GObject") };
				Object[] args = { this };
				Method fn = parentClass.getMethod("sendForward", types);
				if (fn != null) fn.invoke(myParent, args);
			} catch (Exception ex) {
				/* Empty */
			}
		}
		if (mouseListenersEnabled) updateEnabledList();
	}

/* Method: sendBackward() */
/**
 * Moves this object one step toward the back in the <i>z</i> dimension.
 * If it was already at the back of the stack, nothing happens.
 *
 * @usage gobj.sendBackward();
 */
	public void sendBackward() {
		if (myParent == null) return;
		if (myParent instanceof GCanvas) {
			((GCanvas) myParent).sendBackward(this);
		} else if (myParent instanceof GCompound) {
			((GCompound) myParent).sendBackward(this);
		} else {
			try {
				Class parentClass = myParent.getClass();
				Class[] types = { Class.forName("acm.graphics.GObject") };
				Object[] args = { this };
				Method fn = parentClass.getMethod("sendBackward", types);
				if (fn != null) fn.invoke(myParent, args);
			} catch (Exception ex) {
				/* Empty */
			}
		}
		if (mouseListenersEnabled) updateEnabledList();
	}

/* Method: setColor(c) */
/**
 * Sets the color used to display this object.
 *
 * @usage gobj.setColor(color);
 * @param color The color used to display this object
 */
	public void setColor(Color c) {
		color = c;
		repaint();
	}

/* Method: getColor() */
/**
 * Returns the color used to display this object.
 *
 * @usage Color color = gobj.getColor();
 * @return The color used to display this object
 */
	public Color getColor() {
		GObject obj = this;
		while (obj.color == null) {
			GContainer parent = obj.getParent();
			if (parent instanceof GObject) {
				obj = (GObject) parent;
			} else if (parent instanceof Component) {
				return ((Component) parent).getForeground();
			} else {
				return Color.black;
			}
		}
		return obj.color;
	}

/* Method: setVisible(visible) */
/**
 * Sets whether this object is visible.
 *
 * @usage gobj.setVisible(visible);
 * @param visible <code>true</code> to make the object visible, <code>false</code> to hide it
 */
	public void setVisible(boolean visible) {
		isVisible = visible;
		repaint();
	}

/* Method: isVisible() */
/**
 * Checks to see whether this object is visible.
 *
 * @usage if (gobj.isVisible()) . . .
 * @return <code>true</code> if the object is visible, otherwise <code>false</code>
 */
	public boolean isVisible() {
		return isVisible;
	}

/* Method: toString() */
/**
 * Overrides the <code>toString</code> method in <code>Object</code> to produce
 * more readable output.
 * @noshow
 */
	public String toString() {
		String name = getClass().getName();
		if (name.startsWith("acm.graphics.")) {
			name = name.substring("acm.graphics.".length());
		}
		return name + "[" + paramString() + "]";
	}

/* Method: getParent() */
/**
 * Returns the parent of this object, which is the canvas or compound object in
 * which it is enclosed.
 *
 * @usage GContainer parent = gobj.getParent();
 * @return The parent of this object
 */
	public GContainer getParent() {
		return myParent;
	}

/* Method: pause(milliseconds) */
/**
 * Delays the calling thread for the specified time, which is expressed in
 * milliseconds.  Unlike <code>Thread.sleep</code>, this method never throws an
 * exception.
 *
 * @usage gobj.pause(milliseconds);
 * @param milliseconds The sleep time in milliseconds
 */
	public void pause(double milliseconds) {
		JTFTools.pause(milliseconds);
	}

/* Method: addMouseListener(listener) */
/**
 * Adds a mouse listener to this graphical object.
 *
 * @usage gobj.addMouseListener(listener);
 * @param listener Any object that implements the <code>MouseListener</code> interface
 */
	public void addMouseListener(MouseListener listener) {
		mouseListener = AWTEventMulticaster.add(mouseListener, listener);
		mouseListenersEnabled = true;
		updateEnabledList();
	}

/* Method: removeMouseListener(listener) */
/**
 * Removes a mouse listener from this graphical object.
 *
 * @usage gobj.removeMouseListener(listener);
 * @param listener The listener object to remove
 */
	public void removeMouseListener(MouseListener listener) {
		mouseListener = AWTEventMulticaster.remove(mouseListener, listener);
	}

/* Method: addMouseMotionListener(listener) */
/**
 * Adds a mouse motion listener to this graphical object.
 *
 * @usage gobj.addMouseMotionListener(listener);
 * @param listener Any object that implements the <code>MouseMotionListener</code> interface
 */
	public void addMouseMotionListener(MouseMotionListener listener) {
		mouseMotionListener = AWTEventMulticaster.add(mouseMotionListener, listener);
		mouseListenersEnabled = true;
		updateEnabledList();
	}

/* Method: removeMouseMotionListener(listener) */
/**
 * Removes a mouse motion listener from this graphical object.
 *
 * @usage gobj.removeMouseMotionListener(listener);
 * @param listener The listener object to remove
 */
	public void removeMouseMotionListener(MouseMotionListener listener) {
		mouseMotionListener = AWTEventMulticaster.remove(mouseMotionListener, listener);
	}

/* Method: addActionListener(listener) */
/**
 * Adds an action listener to this graphical object.
 *
 * @usage gobj.addActionListener(listener);
 * @param listener Any object that implements the <code>ActionListener</code> interface
 */
	public void addActionListener(ActionListener listener) {
		actionListener = AWTEventMulticaster.add(actionListener, listener);
	}

/* Method: removeActionListener(listener) */
/**
 * Removes an action listener from this graphical object.
 *
 * @usage gobj.removeActionListener(listener);
 * @param listener The listener object to remove
 */
	public void removeActionListener(ActionListener listener) {
		actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}

/* Method: fireActionEvent(actionCommand) */
/**
 * Triggers an action event for this graphical object with the specified
 * action command.
 *
 * @usage gobj.fireActionEvent(actionCommand);
 * @param actionCommand The action command to include in the event
 */
	public void fireActionEvent(String actionCommand) {
		fireActionEvent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, actionCommand));
	}

/* Method: fireActionEvent(e) */
/**
 * Triggers an action event for this graphical object.
 *
 * @usage gobj.fireActionEvent(e);
 * @param e The <code>ActionEvent</code> to fire
 */
	public void fireActionEvent(ActionEvent e) {
		if (actionListener != null) actionListener.actionPerformed(e);
	}

/* Unadvertised method: setParent(parent) */
/**
 * Sets the parent of this object, which should be called only by the
 * <code>GContainer</code> in which this is installed.
 * @noshow
 */
	public void setParent(GContainer parent) {
		myParent = parent;
	}

/* Protected method: fireMouseListeners(e) */
/**
 * Sends the event to the appropriate listener.
 *
 * @usage gobj.fireMouseListeners(e);
 * @param e The <code>MouseEvent</code> that triggered this response
 * @noshow
 */
	protected void fireMouseListeners(MouseEvent e) {
		switch (e.getID()) {
		  case MouseEvent.MOUSE_PRESSED:
			if (mouseListener != null) mouseListener.mousePressed(e);
			break;
		  case MouseEvent.MOUSE_RELEASED:
			if (mouseListener != null) mouseListener.mouseReleased(e);
			break;
		  case MouseEvent.MOUSE_CLICKED:
			if (mouseListener != null) mouseListener.mouseClicked(e);
			break;
		  case MouseEvent.MOUSE_EXITED:
			if (mouseListener != null) mouseListener.mouseExited(e);
			break;
		  case MouseEvent.MOUSE_ENTERED:
			if (mouseListener != null) mouseListener.mouseEntered(e);
			break;
		  case MouseEvent.MOUSE_MOVED:
			if (mouseMotionListener != null) mouseMotionListener.mouseMoved(e);
			break;
		  case MouseEvent.MOUSE_DRAGGED:
			if (mouseMotionListener != null) mouseMotionListener.mouseDragged(e);
			break;
		}
	}

/* Protected method: areMouseListenersEnabled() */
/**
 * Returns <code>true</code> if mouse listeners have ever been assigned to
 * this object.
 *
 * @usage if (gobj.areMouseListenersEnabled()) . . .
 * @return <code>true</code> if mouse listeners have been enabled in this object
 * @noshow
 */
	protected boolean areMouseListenersEnabled() {
		return mouseListenersEnabled;
	}

/* Protected method: start() */
/**
 * Starts a <code>GraphicsProgram</code> containing this object.
 *
 * @usage gobj.start();
 * @noshow
 */
	protected void start() {
		start(null);
	}

/* Protected method: start(args) */
/**
 * Starts a <code>GraphicsProgram</code> containing this object, passing
 * it the specified arguments.
 *
 * @usage gobj.start();
 * @param args The array of arguments
 * @noshow
 */
	protected void start(String[] args) {
		try {
			Class programClass = Class.forName("acm.program.GraphicsProgram");
			Class gObjectClass = Class.forName("acm.graphics.GObject");
			Class[] types = { gObjectClass, args.getClass() };
			Object[] params = { this, args };
			Method startGraphicsProgram = programClass.getMethod("startGraphicsProgram", types);
			startGraphicsProgram.invoke(null, params);
		} catch (Exception ex) {
			throw new ErrorException(ex);
		}
	}

/* Protected method: getObjectColor() */
/**
 * This method returns the color set for this specific object, which may
 * be <code>null</code>.  It differs from <code>getColor</code> in that
 * it does not walk up the containment chain.
 * @noshow
 */
	protected Color getObjectColor() {
		return color;
	}

/* Protected method: paramString() */
/**
 * Returns a string indicating the parameters of this object.
 * @noshow
 */
	protected String paramString() {
		String param = "";
		if (this instanceof GResizable) {
			GRectangle r = getBounds();
			param += "bounds=(" + r.getX() + ", " + r.getY() + ", "
			         + r.getWidth() + ", " + r.getHeight() + ")";
		} else {
			GPoint pt = getLocation();
			param += "location=(" + pt.getX() + ", " + pt.getY() + ")";
		}
		if (color != null) {
			param += ", color=" + colorName(color);
		}
		if (this instanceof GFillable) {
			param += ", filled=" + ((GFillable) this).isFilled();
			Color fillColor = ((GFillable) this).getFillColor();
			if (fillColor != null && fillColor != color) {
				param += ", fillColor=" + colorName(fillColor);
			}
		}
		return param;
	}

/* Protected static method: colorName(color) */
/**
 * Translates a color to a string representation.
 * @noshow
 */
	protected static String colorName(Color color) {
		if (color.equals(Color.black)) return "BLACK";
		if (color.equals(Color.blue)) return "BLUE";
		if (color.equals(Color.cyan)) return "CYAN";
		if (color.equals(Color.darkGray)) return "DARK_GRAY";
		if (color.equals(Color.gray)) return "GRAY";
		if (color.equals(Color.green)) return "GREEN";
		if (color.equals(Color.lightGray)) return "LIGHT_GRAY";
		if (color.equals(Color.magenta)) return "MAGENTA";
		if (color.equals(Color.orange)) return "ORANGE";
		if (color.equals(Color.pink)) return "PINK";
		if (color.equals(Color.red)) return "RED";
		if (color.equals(Color.white)) return "WHITE";
		if (color.equals(Color.yellow)) return "YELLOW";
		return "0x" + Integer.toString(color.getRGB() & 0xFFFFFF, 16).toUpperCase();
	}

/* Protected method: paintObject(g) */
/**
 * Paints the object by setting up the necessary parameters and then
 * dispatching to the <code>paint</code> procedure for this object.
 * @noshow
 */
	protected void paintObject(Graphics g) {
		if (!isVisible()) return;
		Color oldColor = g.getColor();
		if (color != null) g.setColor(color);
		
		Graphics2D g2 = (Graphics2D) g.create();
		g2.rotate(rotation, xc + rotationAnchorX, yc + rotationAnchorY);
		
		paint(g2);
		
		if (color != null) g.setColor(oldColor);
	}

/* Protected method: getComponent() */
/**
 * Returns the component in which this object is installed, or <code>null</code>
 * if none exists.
 *
 * @usage Component comp = gobj.getComponent();
 * @return The component in which this object is installed, or <code>null</code> if none exists
 * @noshow
 */
	protected Component getComponent() {
		GContainer parent = getParent();
		while (parent instanceof GObject) {
			parent = ((GObject) parent).getParent();
		}
		return (parent instanceof Component) ? (Component) parent : null;
	}

/* Protected method: updateEnabledList() */
/**
 * Tells the parent to update its list of enabled objects.
 * @noshow
 */
	protected void updateEnabledList() {
		Component comp = getComponent();
		if (comp instanceof GCanvas) {
			((GCanvas) comp).updateEnabledList();
		}
	}

/* Protected method: repaint() */
/**
 * Signals that the object needs to be repainted.
 * @noshow
 */
	protected void repaint() {
		GContainer parent = getParent();
		while (parent instanceof GObject) {
			parent = ((GObject) parent).getParent();
		}
		if (parent instanceof GCanvas) {
			((GCanvas) parent).conditionalRepaint();
		}
	}
	
    public double getRotation() {
        return rotation;
    }
    
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
    
    public double getRotationAnchorX() {
        return rotationAnchorX;
    }
    
    public void setRotationAnchorX(double rotationAnchorX) {
        this.rotationAnchorX = rotationAnchorX;
    }
    
    public double getRotationAnchorY() {
        return rotationAnchorY;
    }
    
    public void setRotationAnchorY(double rotationAnchorY) {
        this.rotationAnchorY = rotationAnchorY;
    }
    

/* Private state */

    private GContainer myParent;
	private Color color;
	private double xc, yc;
	private double rotation, rotationAnchorX, rotationAnchorY;
	private boolean isVisible;
	private boolean mouseListenersEnabled;
	private MouseListener mouseListener;
	private MouseMotionListener mouseMotionListener;
	private ActionListener actionListener;
}
