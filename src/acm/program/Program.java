/*
 * @(#)Program.java   1.0 06/08/25
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

package acm.program;

import acm.gui.TableLayout;
import acm.io.IOConsole;
import acm.io.IODialog;
import acm.io.IOModel;
import acm.util.ErrorException;
import acm.util.JTFTools;
import acm.util.MediaTools;
import acm.util.Platform;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageProducer;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/* Class: Program */

/**
 * This class is the superclass for all executable
 * programs in the <code>acm.program</code> package.  Its principal
 * role is to unify the concepts of applets and applications in a single
 * class, although it also provides applications with many other useful
 * facilities not traditionally available in applications.
 *
 * <p>In many programming environments, objects that are specific instances
 * of a <code>Program</code> subclass will run automatically without any
 * special action on your part.  For maximum portability, you might want
 * to define a static <code>main</code> method as described in the comments
 * for the standard implementation of <a href="#main(String[])"><code>main</code></a>.
 */
public abstract class Program extends JApplet
  implements IOModel, Runnable, MouseListener, MouseMotionListener, KeyListener, ActionListener {

/** Constant specifying the north edge of the container */
	public static final String NORTH = BorderLayout.NORTH;

/** Constant specifying the south edge of the container */
	public static final String SOUTH = BorderLayout.SOUTH;

/** Constant specifying the east edge of the container */
	public static final String EAST = BorderLayout.EAST;

/** Constant specifying the west edge of the container */
	public static final String WEST = BorderLayout.WEST;

/** Constant specifying the center of the container */
	public static final String CENTER = BorderLayout.CENTER;

/* Default constructor: Program */
/**
 * This code initializes the program data structures.
 */
	protected Program() {
		JTFTools.registerApplet(this);
		shown = false;
		parameterTable = null;
		finalizers = new ArrayList();
		isAppletMode = false;
		macMenuBarFlag = true;
		myTitle = getClass().getName();
		myTitle = myTitle.substring(myTitle.lastIndexOf(".") + 1);
		appletStub = new ProgramAppletStub(this);
		setAppletStub(appletStub);
		Container contentPane = getContentPane();
		initContentPane(contentPane);
		setVisible(false);
		setConsole(createConsole());
		myDialog = createDialogIO();
		myDialog.setAssociatedConsole(myConsole);
		myMenuBar = createMenuBar();
		myMenuBar.setProgram(this);
	}

/* Method: run() */
/**
 * Contains the code to be executed for each specific program subclass.  If
 * you are defining your own program, you need to override the definition of
 * <code>run</code> so that it contains the code for your application.
 */
	public void run() {
		/* Empty */
	}

/* Method: init() */
/**
 * The init method is called at startup time before the run method is
 * called.  Subclasses can override this method to perform any
 * initialization code that would ordinarily be included in an applet
 * <code>init</code> method.  This method is used only for certain styles
 * of application development that have their roots in the applet world;
 * other styles will not ordinarily use or override this method.
 *
 * @usage program.init();
 */
	public void init() {
		/* Empty */
	}

/* Method: print(value) */
/**
 * Displays the argument value on the console, leaving the cursor at the end of
 * the output.  The <code>print</code> method is overloaded so that
 * <code>value</code> can be of any type.
 *
 * @usage program.print(value);
 * @param value The value to be displayed
 */
	public void print(String value) {
		getOutputModel().print(value);
	}

/**
 * Makes sure that <code>print</code> can display a <code>boolean</code>.
 * @noshow
 */
	public final void print(boolean x) {
		print("" + x);
	}

/**
 * Makes sure that <code>print</code> can display a <code>char</code>.
 * @noshow
 */
	public final void print(char x) {
		print("" + x);
	}

/**
 * Makes sure that <code>print</code> can display a <code>double</code>.
 * @noshow
 */
	public final void print(double x) {
		print("" + x);
	}

/**
 * Makes sure that <code>print</code> can display a <code>float</code>.
 * @noshow
 */
	public final void print(float x) {
		print("" + x);
	}

/**
 * Makes sure that <code>print</code> can display an <code>int</code>.
 * @noshow
 */
	public final void print(int x) {
		print("" + x);
	}

/**
 * Makes sure that <code>print</code> can display a <code>long</code>.
 * @noshow
 */
	public final void print(long x) {
		print("" + x);
	}

/**
 * Makes sure that <code>print</code> can display an <code>Object</code>.
 * @noshow
 */
	public final void print(Object x) {
		print("" + x);
	}

/* Method: println() */
/**
 * Advances the console cursor to the beginning of the next line.
 *
 * @usage program.println();
 */
	public void println() {
		getOutputModel().println();
	}

/* Method: println(value) */
/**
 * Displays the argument value on the console and then advances the cursor
 * to the beginning of the next line.  The <code>println</code> method is
 * overloaded so that <code>value</code> can be of any type.
 *
 * @usage program.println(value);
 * @param value The value to be displayed
 */
	public void println(String value) {
		getOutputModel().println(value);
	}

/**
 * Makes sure that <code>println</code> can display a <code>boolean</code>.
 * @noshow
 */
	public final void println(boolean x) {
		println("" + x);
	}

/**
 * Makes sure that <code>println</code> can display a <code>char</code>.
 * @noshow
 */
	public final void println(char x) {
		println("" + x);
	}

/**
 * Makes sure that <code>println</code> can display a <code>double</code>.
 * @noshow
 */
	public final void println(double x) {
		println("" + x);
	}

/**
 * Makes sure that <code>println</code> can display a <code>float</code>.
 * @noshow
 */
	public final void println(float x) {
		println("" + x);
	}

/**
 * Makes sure that <code>println</code> can display an <code>int</code>.
 * @noshow
 */
	public final void println(int x) {
		println("" + x);
	}

/**
 * Makes sure that <code>println</code> can display a <code>long</code>.
 * @noshow
 */
	public final void println(long x) {
		println("" + x);
	}

/**
 * Makes sure that <code>println</code> can display an <code>Object</code>.
 * @noshow
 */
	public final void println(Object x) {
		println("" + x);
	}

/* Method: showErrorMessage(msg) */
/**
 * Displays the error message in the standard output model.
 *
 * @usage showErrorMessage(msg);
 * @param msg The error msg to be displayed
 */
	public void showErrorMessage(String msg) {
		getOutputModel().showErrorMessage(msg);
	}

/* Method: readLine() */
/**
 * Reads and returns a line of input from the console.  The end-of-line
 * characters that terminate the input are not included in the returned
 * string.
 *
 * @usage String str = program.readLine();
 * @return The next line of input as a <code>String</code>
 */
	public final String readLine() {
		return readLine(null);
	}

/* Method: readLine(prompt) */
/**
 * Prompts the user for a line of input.  The end-of-line characters
 * that terminate the input are not included in the returned string.
 *
 * @usage String str = program.readLine(prompt);
 * @param prompt The prompt string to display to the user
 * @return The next line of input as a <code>String</code>
 */
	public String readLine(String prompt) {
		return getInputModel().readLine(prompt);
	}

/**
 * Prompts the user for a single character of input.
 *
 * @usage String str = program.readChar(prompt);
 * @param prompt The prompt string to display to the user
 */
    public char readChar(String prompt) {
        while(true) {
            String response = readLine(prompt);
            if(response.length() == 1)
                return response.charAt(0);
            println("You must enter exactly one letter.");
        }
    }

/* Method: readInt() */
/**
 * Reads and returns an integer value from the user.  If the user types
 * a value that is not a legal integer, the method ordinarily offers the
 * user a chance to reenter the data, although this behavior can be
 * changed using the
 * <a href="#setExceptionOnError(boolean)"><code>setExceptionOnError</code></a> method.
 *
 * @usage int n = program.readInt();
 * @return The value of the input interpreted as a decimal integer
 */
	public final int readInt() {
		return readInt(null, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

/* Method: readInt(low, high) */
/**
 * Reads and returns an integer value from the user, which is constrained to
 * be within the specified inclusive range.  If the user types a value
 * that is not a legal integer, the method ordinarily offers the user a chance
 * to reenter the data, although this behavior can be changed using the
 * <a href="#setExceptionOnError(boolean)"><code>setExceptionOnError</code></a> method.
 *
 * @usage int n = program.readInt(low, high);
 * @param low The lowest value in the permitted range
 * @param high The highest value in the permitted range
 * @return The value of the input interpreted as a decimal integer
 */
	public final int readInt(int low, int high) {
		return readInt(null, low, high);
	}

/* Method: readInt(prompt) */
/**
 * Prompts the user to enter an integer, which is then returned as the value
 * of this method.  If the user types a value that is not a legal integer,
 * the method ordinarily offers the user a chance to reenter the data,
 * although this behavior can be changed using the
 * <a href="#setExceptionOnError(boolean)"><code>setExceptionOnError</code></a> method.
 *
 * @usage int n = program.readInt(prompt);
 * @param prompt The prompt string to display to the user
 * @return The value of the input interpreted as a decimal integer
 */
	public final int readInt(String prompt) {
		return readInt(prompt, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

/* Method: readInt(prompt, low, high) */
/**
 * Prompts the user to enter an integer, which is then returned as the value
 * of this method.  The value must be within the inclusive range between
 * <code>low</code> and <code>high</code>.  If the user types a value that
 * is not a legal integer or is outside the specified range, the method
 * ordinarily offers the user a chance to reenter the data,
 * although this behavior can be changed using the
 * <a href="#setExceptionOnError(boolean)"><code>setExceptionOnError</code></a> method.
 *
 * @usage int n = console.readInt(prompt, low, high);
 * @param prompt The prompt string to display to the user
 * @param low The lowest value in the permitted range
 * @param high The highest value in the permitted range
 * @return The value of the input interpreted as a decimal integer
 */
	public int readInt(String prompt, int low, int high) {
		return getInputModel().readInt(prompt, low, high);
	}

/* Method: readDouble() */
/**
 * Reads and returns a double-precision value from the user.  If the user
 * types a value that is not a legal number, the method ordinarily offers
 * the user a chance to reenter the data, although this behavior can be
 * changed using the
 * <a href="#setExceptionOnError(boolean)"><code>setExceptionOnError</code></a> method.
 *
 * @usage double d = program.readDouble();
 * @return The value of the input interpreted as a <code>double</code>
 */
	public final double readDouble() {
		return readDouble(null, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

/* Method: readDouble(low, high) */
/**
 * Reads and returns a double-precision value from the user, which is
 * constrained to be within the specified inclusive range.  If the user
 * types a value that is not a legal number, the method ordinarily offers
 * the user a chance to reenter the data, although this behavior can be
 * changed using the
 * <a href="#setExceptionOnError(boolean)"><code>setExceptionOnError</code></a> method.
 *
 * @usage double d = program.readDouble(low, high);
 * @param low The lowest value in the permitted range
 * @param high The highest value in the permitted range
 * @return The value of the input interpreted as a <code>double</code>
 */
	public final double readDouble(double low, double high) {
		return readDouble(null, low, high);
	}

/* Method: readDouble(prompt) */
/**
 * Prompts the user to enter an double-precision number, which is then
 * returned as the value of this method.  If the user types a value that
 * is not a legal number, the method ordinarily offers the user a chance to
 * reenter the data,  although this behavior can be changed using the
 * <a href="#setExceptionOnError(boolean)"><code>setExceptionOnError</code></a> method.
 *
 * @usage double d = program.readDouble(prompt);
 * @param prompt The prompt string to display to the user
 * @return The value of the input interpreted as a <code>double</code>
 */
	public final double readDouble(String prompt) {
		return readDouble(prompt, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

/* Method: readDouble(prompt, low, high) */
/**
 * Prompts the user to enter an double-precision number, which is then returned
 * as the value of this method.  The value must be within the inclusive range
 * between <code>low</code> and <code>high</code>.  If the user types a value
 * that is not a legal number, the method ordinarily offers the user a chance
 * to reenter the data,  although this behavior can be changed using the
 * <a href="#setExceptionOnError(boolean)"><code>setExceptionOnError</code></a> method.
 *
 * @usage d = program.readDouble(prompt, low, high);
 * @param prompt The prompt string to display to the user
 * @param low The lowest value in the permitted range
 * @param high The highest value in the permitted range
 * @return The value of the input interpreted as a <code>double</code>
 */
	public double readDouble(String prompt, double low, double high) {
		return getInputModel().readDouble(prompt, low, high);
	}

/* Method: readBoolean() */
/**
 * Reads and returns a boolean value (<code>true</code> or <code>false</code>).
 * The input must match one of these strings, ignoring case.  If the user
 * types a value that is not one of these possibilities, the method ordinarily
 * offers the user a chance to reenter the data, although this behavior
 * can be changed using the
 * <a href="#setExceptionOnError(boolean)"><code>setExceptionOnError</code></a> method.
 *
 * @usage boolean flag = program.readBoolean();
 * @return The value of the input interpreted as a boolean value
 */
	public final boolean readBoolean() {
		return readBoolean(null);
	}

/* Method: readBoolean(prompt) */
/**
 * Prompts the user to enter a boolean value, which is returned as
 * the value of this method.  If the user types a value that is not a
 * legal boolean value, the method ordinarily offers the user a chance
 * to reenter the data, although this behavior can be changed using the
 * <a href="#setExceptionOnError(boolean)"><code>setExceptionOnError</code></a> method.
 *
 * @usage boolean flag = program.readBoolean(prompt);
 * @param prompt The prompt string to display to the user
 * @return The value of the input interpreted as a boolean value
 */
	public final boolean readBoolean(String prompt) {
		return readBoolean(prompt, "true", "false");
	}

/* Method: readBoolean(prompt, trueLabel, falseLabel) */
/**
 * Prompts the user to enter a boolean value, which is matched against the
 * labels provided.  If the user enters a value that is not one of the two
 * choices, <code>readBoolean</code> ordinarily offers the user a chance
 * to reenter the data, although this behavior can be changed using the
 * <a href="#setExceptionOnError(boolean)"><code>setExceptionOnError</code></a> method.
 *
 * @usage boolean flag = program.readBoolean(prompt);
 * @param prompt The prompt string to display to the user
 * @param trueLabel The string used to indicate <code>true</code>
 * @param falseLabel The string used to indicate <code>false</code>
 * @return The value of the input interpreted as a boolean value
 */
	public boolean readBoolean(String prompt, String trueLabel, String falseLabel) {
		return getInputModel().readBoolean(prompt, trueLabel, falseLabel);
	}

/* Method: isAppletMode() */
/**
 * Returns <code>true</code> if this program is running as an applet in a browser.
 *
 * @usage if (isAppletMode()) . . .
 * @return <code>true</code> if this program is running as an applet, <code>false</code> otherwise
 * @noshow
 */
	public boolean isAppletMode() {
		if (!started) {
			throw new ErrorException("You can't call isAppletMode from the constructor");
		}
		return isAppletMode;
	}

/* Method: setConsole(console) */
/**
 * Sets the console associated with this program.
 *
 * @usage program.setConsole(console);
 * @param console The <code>IOConsole</code> object used for this program
 */
	public void setConsole(IOConsole console) {
		myConsole = console;
	}

/* Method: getConsole() */
/**
 * Returns the console associated with this program.
 *
 * @usage IOConsole console = program.getConsole();
 * @return The <code>IOConsole</code> object used for this program
 */
	public IOConsole getConsole() {
		return myConsole;
	}

/* Method: getDialog() */
/**
 * Returns the dialog used for user interaction.
 *
 * @usage IODialog dialog = program.getDialog();
 * @return The <code>IODialog</code> object used for this program
 */
	public IODialog getDialog() {
		return myDialog;
	}

/* Method: getInputModel() */
/**
 * Returns the <code>IOModel</code> used for program input, which will
 * typically be either the default <code>IOConsole</code> or <code>IODialog</code> object.
 *
 * @usage IOModel io = program.getInputModel();
 * @return The <code>IOModel</code> used for program input
 */
	public IOModel getInputModel() {
		return getConsole();
	}

/* Method: getOutputModel() */
/**
 * Returns the <code>IOModel</code> used for program output, which will
 * typically be either the default <code>IOConsole</code> or <code>IODialog</code> object.
 *
 * @usage IOModel io = program.getOutputModel();
 * @return The <code>IOModel</code> used for program output
 */
	public IOModel getOutputModel() {
		return getConsole();
	}

/* Method: getReader() */
/**
 * Returns a <code>BufferedReader</code> whose input comes from the console.
 *
 * @usage BufferedReader rd = getReader();
 * @return A <code>Reader</code> for use with this console
 */
	public BufferedReader getReader() {
		return getConsole().getReader();
	}

/* Method: getWriter() */
/**
 * Returns a <code>PrintWriter</code> whose output is directed to the console.
 *
 * @usage PrintWriter wr = getWriter();
 * @return A <code>PrintWriter</code> for use with this console
 */
	public PrintWriter getWriter() {
		return getConsole().getWriter();
	}

/* Method: getRegionPanel(region) */
/**
 * Gets the <code>JPanel</code> for the specified region.
 *
 * @usage JPanel panel = getRegionPanel(region);
 * @param region The region of the window (<code>NORTH</code>, <code>SOUTH</code>,
 *               <code>EAST</code>, <code>WEST</code>, or <code>CENTER</code>)
 * @return The <code>JPanel</code> for that subregion
 * @noshow
 */
	public JPanel getRegionPanel(String region) {
		if (region.equals(NORTH)) {
			return northPanel;
		} else if (region.equals(SOUTH)) {
			return southPanel;
		} else if (region.equals(WEST)) {
			return westPanel;
		} else if (region.equals(EAST)) {
			return eastPanel;
		} else if (region.equals(CENTER)) {
			return centerPanel;
		} else {
			throw new ErrorException("getRegionPanel: Illegal region " + region);
		}
	}

/* Method: add(comp, region, constraints) */
/**
 * Adds the component to the specified border region with the indicated
 * constraints object.
 *
 * @usage add(comp, region, constraints);
 * @param comp The component to be added
 * @param region The region of the window (<code>NORTH</code>, <code>SOUTH</code>,
 *               <code>EAST</code>, <code>WEST</code>, or <code>CENTER</code>)
 * @param constraints The constraints object
 * @noshow
 */
	public void add(Component comp, String region, Object constraints) {
		if (region.equals(NORTH)) {
			northPanel.add(comp, constraints);
		} else if (region.equals(SOUTH)) {
			southPanel.add(comp, constraints);
		} else if (region.equals(WEST)) {
			westPanel.add(comp, constraints);
		} else if (region.equals(EAST)) {
			eastPanel.add(comp, constraints);
		} else if (region.equals(CENTER)) {
			centerPanel.add(comp, constraints);
		} else {
			throw new ErrorException("add: Illegal region " + region);
		}
	}

/* Method: addActionListeners() */
/**
 * Adds the program as an <code>ActionListener</code> to every button in
 * the structure that does not have a listener already.
 *
 * @usage addActionListeners();
 */
	public void addActionListeners() {
		addActionListeners(this);
	}

/* Method: addActionListeners(listener) */
/**
 * Adds the specified listener to every button in
 * the structure that does not have a listener already.
 *
 * @usage addActionListeners(listener);
 * @param listener The <code>ActionListener</code> to be added
 */
	public void addActionListeners(ActionListener listener) {
		addActionListeners(getContentPane(), listener);
	}

/* Method: setTitle(title) */
/**
 * Sets the title of this program.  The title appears in the title bar
 * when the program is running as an application.
 *
 * @usage setTitle(title);
 * @param title The title for this program
 */
	public void setTitle(String title) {
		myTitle = title;
		if (programFrame != null) programFrame.setTitle(title);
	}

/* Method: getTitle() */
/**
 * Gets the title of this program.
 *
 * @usage String title = getTitle();
 * @return The title in use for this program
 */
	public String getTitle() {
		return myTitle;
	}

/* Method: getMenuBar() */
/**
 * Returns the menu bar associated with this program.  Note that this menu bar
 * cannot be set by clients, although it can be changed initially by overriding
 * the <code>createMenuBar</code> factory method.
 *
 * @usage ProgramMenuBar mbar = getMenuBar();
 * @return The menu bar in use for this program
 */
	public ProgramMenuBar getMenuBar() {
		return myMenuBar;
	}

/* Method: start(args) */
/**
 * Starts the program using the specified argument list.
 *
 * @usage program.start(args);
 * @param args An array of strings passed to the program
 */
	public void start(String[] args) {
		if (parameterTable == null) parameterTable = createParameterTable(args);
		if (getParent() == null) initApplicationFrame();
		validate();
		setVisible(true);
		if (programFrame != null) {
			programFrame.validate();
			int nComponents = centerPanel.getComponentCount();
			nComponents += northPanel.getComponentCount();
			nComponents += southPanel.getComponentCount();
			nComponents += westPanel.getComponentCount();
			nComponents += eastPanel.getComponentCount();
			if (nComponents > 0) {
				programFrame.setVisible(true);
				shown = true;
			}
			circumventFrameSizeBug(programFrame, programBounds.getSize());
		}
		started = true;
		init();
		if (programFrame != null && myMenuBar != null) {
			installMenuBar(myMenuBar, programFrame);
		}
		validate();
		startRun();
	}

/* Method: exit() */
/**
 * Exits from the program.  Subclasses should override this method if they need
 * to perform any actions before shutting down the program, such as asking the
 * user to save any unsaved files.  Any clients that do override this method
 * should call <code>super.exit()</code> at the end of their processing.
 *
 * @usage program.exit();
 */
	public void exit() {
		synchronized (finalizers) {
			int nFinalizers = finalizers.size();
			for (int i = 0; i < nFinalizers; i++) {
				Object obj = finalizers.get(i);
				try {
					Class c = obj.getClass();
					Method exit = c.getMethod("exit", new Class[0]);
					exit.invoke(obj, new Object[0]);
				} catch (Exception ex) {
					throw new ErrorException(ex);
				}
			}
		}
		JTFTools.terminateAppletThreads(this);
		if (!isAppletMode) System.exit(0);
	}

/* Method: addExitHook(obj) */
/**
 * Requests that the program call the <code>exit</code> method in the
 * specified object before exiting.
 *
 * @usage program.addExitHook(obj);
 */
	public void addExitHook(Object obj) {
		synchronized (finalizers) {
			finalizers.add(obj);
		}
	}

/* Method: pause(milliseconds) */
/**
 * Delays the calling thread for the specified time, which is expressed in
 * milliseconds.  Unlike <code>Thread.sleep</code>, this method never throws an
 * exception.
 *
 * @usage program.pause(milliseconds);
 * @param milliseconds The sleep time in milliseconds
 */
	public void pause(double milliseconds) {
		JTFTools.pause(milliseconds);
	}

/**********************************************************************/
/* Listener methods                                                   */
/**********************************************************************/

/* Method: mouseClicked (implements MouseListener) */
/**
 * Called when the mouse is clicked.  A call to <code>mouseClicked</code>
 * is always preceded by both a <code>mousePressed</code> and a
 * <code>mouseReleased</code> event for the same source.
 */
	public void mouseClicked(MouseEvent e) { }

/* Method: mousePressed (implements MouseListener) */
/**
 * Called when the mouse button is pressed.
 */
	public void mousePressed(MouseEvent e) { }

/* Method: mouseReleased (implements MouseListener) */
/**
 * Called when the mouse button is released.
 */
	public void mouseReleased(MouseEvent e) { }

/* Method: mouseEntered (implements MouseListener) */
/**
 * Called when the mouse enters the source (which may be
 * either a component or a <code>GObject</code>).
 */
	public void mouseEntered(MouseEvent e) { }

/* Method: mouseExited (implements MouseListener) */
/**
 * Called when the mouse exits the source (which may be
 * either a component or a <code>GObject</code>).
 */
	public void mouseExited(MouseEvent e) { }

/* Method: mouseMoved (implements MouseMotionListener) */
/**
 * Called when the mouse is moved.
 */
	public void mouseMoved(MouseEvent e) { }

/* Method: mouseDragged (implements MouseMotionListener) */
/**
 * Called when the mouse is dragged with the button down.  Java
 * makes several guarantees about dragging.  First, a
 * <code>mouseDragged</code> call is always preceded by a
 * <code>mousePressed</code> call for the same source.  If the
 * mouse is pressed elsewhere and then enters a source with
 * the button down, no drag event occurs.  Moreover, once the
 * mouse button goes down in a particular source, only that
 * source will receive mouse events until the button goes up.
 * Those events, moreover, are reported even in the mouse
 * travels outside the domain of the object.
 */
	public void mouseDragged(MouseEvent e) { }

/* Method: keyTyped (implements KeyListener) */
/**
 * Called when a key is typed (i.e., pressed and released).
 */
	public void keyTyped(KeyEvent e) { }

/* Method: keyPressed (implements KeyListener) */
/**
 * Called when a key is pressed.
 */
	public void keyPressed(KeyEvent e) { }

/* Method: keyReleased (implements KeyListener) */
/**
 * Called when a key is released.
 */
	public void keyReleased(KeyEvent e) { }

/* Method: actionPerformed (implements ActionListener) */
/**
 * Called when a component (typically a button) is activated.
 */
	public void actionPerformed(ActionEvent e) { }

/**********************************************************************/
/* Factory methods                                                    */
/**********************************************************************/

/* Factory method: createProgramFrame() */
/**
 * Creates the frame containing the program.
 *
 * @usage Frame frame = program.createProgramFrame();
 * @return The newly allocated <code>Frame</code> object
 */
	protected Frame createProgramFrame() {
		Frame frame = new ProgramFrame(getTitle());
		frame.setLayout(new BorderLayout());
		return frame;
	}

/* Factory method: createConsole() */
/**
 * Creates the console used by the <code>ConsoleProgram</code>.  Subclasses can
 * override this method to create their own console types.
 *
 * @usage IOConsole console = program.createConsole();
 * @return The console to be used by the program
 */
	protected IOConsole createConsole() {
		return IOConsole.SYSTEM_CONSOLE;
	}

/* Factory method: createDialogIO() */
/**
 * Creates the dialog used for interaction (primarily by the <code>DialogProgram</code>
 * class).  Subclasses can override this method to create their own dialog types.
 *
 * @usage IODialog dialog = program.createDialogIO();
 * @return The dialog to be used by the program
 */
	protected IODialog createDialogIO() {
		return new IODialog(getContentPane());
	}

/* Factory method: createMenuBar() */
/**
 * Creates a menu bar for use with the program.
 *
 * @usage ProgramMenuBar menuBar = createMenuBar();
 * @return A menu bar for use with this <code>Program</code>
 * @noshow
 */
	protected ProgramMenuBar createMenuBar() {
		return new ProgramMenuBar();
	}

/**********************************************************************/
/* Overrides of existing methods                                      */
/**********************************************************************/

/* Overridden method: getPreferredSize() */
/**
 * Returns the preferred size of the content pane.
 *
 * @usage Dimension size = getPreferredSize();
 * @return The preferred size of the content pane
 * @noshow
 */
	public Dimension getPreferredSize() {
		return computeProgramBounds().getSize();
	}


/* Overridden method: getWidth() */
/**
 * Returns the width of the central region.
 *
 * @usage int width = getWidth();
 * @return The width of the central region
 * @noshow
 */
	public int getWidth() {
		return getCentralRegionSize().width;
	}

/* Overridden method: getHeight() */
/**
 * Returns the height of the central region.
 *
 * @usage int height = getHeight();
 * @return The height of the central region
 * @noshow
 */
	public int getHeight() {
		return getCentralRegionSize().height;
	}

/* Overridden method: getParameter(name) */
/**
 * Returns the parameter associated with name.
 *
 * @usage String value = getParameter(name);
 * @param name The name of the parameter
 * @return The value associated with the parameter, or <code>null</code> if none
 * @noshow
 */
	public String getParameter(String name) {
		String value = null;
		if (parameterTable != null) {
			value = (String) parameterTable.get(name.toLowerCase());
		}
		if (value != null) return value;
		return super.getParameter(name);
	}

/* Overridden method: setLayout(layout) */
/**
 * Sets the layout manager for the central region of the content pane.
 *
 * @usage setLayout(layout);
 * @param layout The layout manager to use
 * @noshow
 */
	public void setLayout(LayoutManager layout) {
		if (isRootPaneCheckingEnabled()) {
			centerPanel.setLayout(layout);
		} else {
			super.setLayout(layout);
		}
	}

/* Overridden method: getLayout() */
/**
 * Gets the layout manager for the central region of the content pane.
 *
 * @usage LayoutManager layout = setLayout();
 * @return The active layout manager
 * @noshow
 */
	public LayoutManager getLayout() {
		if (isRootPaneCheckingEnabled()) {
			return centerPanel.getLayout();
		} else {
			return super.getLayout();
		}
	}

/* Overridden method: setBackground(color) */
/**
 * Sets the background for the central region of the content pane.
 *
 * @usage setBackground(color);
 * @param color The new background color
 * @noshow
 */
	public void setBackground(Color color) {
		if (isRootPaneCheckingEnabled()) {
			centerPanel.setBackground(color);
		}
		super.setBackground(color);
	}

/* Overridden method: addImpl(comp, constraints, index) */
/**
 * Adds the specified component to the content pane using the specified constraints and index.
 */
	protected void addImpl(Component comp, Object constraints, int index) {
		if (isRootPaneCheckingEnabled()) {
			if (constraints == null) {
				centerPanel.add(comp, index);
			} else if (constraints.equals(NORTH)) {
				northPanel.add(comp, index);
			} else if (constraints.equals(SOUTH)) {
				southPanel.add(comp, index);
			} else if (constraints.equals(WEST)) {
				westPanel.add(comp, index);
			} else if (constraints.equals(EAST)) {
				eastPanel.add(comp, index);
			} else if (constraints.equals(CENTER)) {
				centerPanel.add(comp, index);
			} else {
				centerPanel.add(comp, constraints, index);
			}
			if (!shown && programFrame != null) {
				programFrame.setVisible(true);
				shown = true;
			}
		} else {
			super.addImpl(comp, constraints, index);
		}
	}

/* Overridden method: remove(index) */
/**
 * Removes the component at the specified index from the central region.
 *
 * @usage remove(index);
 * @param index The index position of the component to remove
 * @noshow
 */
	public void remove(int index) {
		if (isRootPaneCheckingEnabled()) {
			centerPanel.remove(index);
		} else {
			super.remove(index);
		}
	}

/* Overridden method: remove(comp) */
/**
 * Removes the specified component from the central region.
 *
 * @usage remove(comp);
 * @param comp The component to remove
 * @noshow
 */
	public void remove(Component comp) {
		if (isRootPaneCheckingEnabled()) {
			centerPanel.remove(comp);
		} else {
			super.remove(comp);
		}
	}

/* Overridden method: removeAll() */
/**
 * Removes all components from the central region.
 *
 * @usage removeAll();
 * @noshow
 */
	public void removeAll() {
		if (isRootPaneCheckingEnabled()) {
			centerPanel.removeAll();
		} else {
			super.removeAll();
		}
	}

/* Overridden method: validate() */
/**
 * Forwards validate to the content pane.
 *
 * @usage validate();
 * @noshow
 */
	public void validate() {
		if (isRootPaneCheckingEnabled()) getContentPane().validate();
		super.validate();
	}

/* Overridden method: repaint() */
/**
 * Forwards repaint to the content pane.
 *
 * @usage repaint();
 * @noshow
 */
	public void repaint() {
		if (isRootPaneCheckingEnabled()) getContentPane().repaint();
		super.repaint();
	}

/* Overridden method: start() */
/**
 * Starts the program when it is running in application mode.  Note that this
 * overloads the <code>start</code> method in <code>Applet</code> and therefore
 * will be called as part of applet startup.
 *
 * @usage program.start();
 * @noshow
 */
	public final void start() {
	    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
	    double width = 0.8 * Math.min(size.width, size.height * 4 / 3);
	    resize((int) Math.rint(width), (int) Math.rint(width * 3 / 4));
		
	    isAppletMode = getParent() != null;
		if (isAppletMode) {
			if (!started) {
				started = true;
				validate();
				setVisible(true);
				mainThread = Thread.currentThread();
				startRun();
			}
		} else {
			start(null);
		}
	}

/* Overridden method: destroy() */
/**
 * Called when the program has been told to destroy itself.  The code here
 * simply stops the main thread.  This implementation uses reflection to invoke
 * stop to avoid the fact that stop is deprecated, but there isn't a
 * a good alternative here.  The dangers that led to its deprecation can't
 * in fact apply because the entire program is going away.
 *
 * @usage program.destroy();
 * @noshow
 */
	public void destroy() {
		if (mainThread != null) {
			try {
				Class threadClass = Class.forName("java.lang.Thread");
				Method stop = threadClass.getMethod("stop", new Class[0]);
				stop.invoke(mainThread, new Object[0]);
			} catch (Exception ex) {
				/* Empty */
			}
		}
	}

/* Static method: main(args) */
/**
 * Every application must either contain a "Main-Class" entry in its
 * manifest file or include a main method that looks like this, where
 * <code>MyClass</code> is the name of the program class:
 *
 * <code><p>
 * &nbsp;&nbsp;&nbsp;public static void main(String[] args)<br>
 * &nbsp;&nbsp;&nbsp;{<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;new MyClass().start();<br>
 * &nbsp;&nbsp;&nbsp;}<br>
 * </code><p>
 *
 * If the program needs the command line arguments, the <code>args</code> array
 * can be passed to the <code>start</code> method and then retrieved using
 * the <code>getArgumentArray</code> method.
 *
 * @param args An array of string arguments
 */
	public static void main(String[] args) {
		HashMap ht = createParameterTable(args);
		String className = (String) ht.get("code");
		Class mainClass = null;
		Program program = null;
		if (className == null) {
			try {
				className = System.getProperty("java.main");
			} catch (Exception ex) {
				/* Empty */
			}
		}
        if (className == null && System.getProperty("sun.java.command") != null) {
            // these are java classes that "wrap" other java programs
            List<String> javaWrappers = Arrays.asList(
                    "com.intellij.rt.execution.application.AppMain" // intellij
            );
            List<String> tokens = new ArrayList<String>(Arrays.asList(System.getProperty("sun.java.command").split("\\s+")));
            while (tokens.size() > 0 && javaWrappers.contains(tokens.get(0))) {
                tokens.remove(0);
            }
            if (tokens.size() > 0) {
                className = tokens.get(0);
            }

        }
		if (className == null) {
			String commandLine = getCommandLine();
			className = readMainClassFromCommandLine(commandLine);
		}
		if (className != null) {
			if (className.endsWith(".class")) {
				className = className.substring(0, className.length() - 6);
			}
			className = className.replace('/', '.');
			try {
				mainClass = Class.forName(className);
			} catch (ClassNotFoundException ex) {
				/* Empty */
			}
		}
		if (mainClass != null) {
			try {
				Object obj = mainClass.newInstance();
				if (obj instanceof Program) {
					program = (Program) obj;
					program.setStartupObject(null);
				} else {
					className = (String) ht.get("program");
					if (className == null) {
						throw new ErrorException("Main class does not specify a program");
					}
					program = (Program) Class.forName(className).newInstance();
					program.setStartupObject(obj);
				}
			} catch (IllegalAccessException ex) {
				/* Empty */
			} catch (InstantiationException ex) {
				/* Empty */
			} catch (ClassNotFoundException ex) {
				/* Empty */
			}

		}
		if (program == null) throw new ErrorException("Cannot determine the main class.");
		program.setParameterTable(ht);
		program.start();
	}

/**********************************************************************/
/* Menu handling methods                                              */
/**********************************************************************/

/* Method: menuAction(cmd) */
/**
 * Called whenever an action event is detected in the menu bar.  Most of
 * these actions are simply passed on to the appropriate console.
 */
	public void menuAction(String cmd) {
		IOConsole console = myConsole;
		if (myMenuBar != null) {
			console = myMenuBar.getConsole();
			if (console == null) console = myConsole;
		}
		if (console == null) return;
		if (cmd.equals("Quit")) {
			exit();
		} else if (cmd.equals("Print")) {
			Frame frame = JTFTools.getEnclosingFrame(this);
			if (frame == null) return;
			PrintJob pj = frame.getToolkit().getPrintJob(frame, myTitle, null);
			if (pj == null) return;
			frame.printAll(pj.getGraphics());
			pj.end();
		} else if (cmd.equals("Cut")) {
			console.cut();
		} else if (cmd.equals("Copy")) {
			console.copy();
		} else if (cmd.equals("Paste")) {
			console.paste();
		} else if (cmd.equals("Select All")) {
			console.selectAll();
		} else if (cmd.equals("Save")) {
			console.save();
		} else if (cmd.equals("Save As")) {
			console.saveAs();
		} else if (cmd.equals("Script")) {
			console.script();
		} else if (cmd.equals("Print Console")) {
			console.printConsole();
		}
	}

/* Protected method: setMacMenuBarFlag(flag) */
/**
 * Sets a flag indicating whether applications running on the Macintosh
 * should use standard Mac menus.  The default is <code>true</code>.
 * Setting this value to <code>false</code> means that Mac programs
 * use the same in-window <code>JMenuBar</code> approach used on other
 * platforms.
 *
 * @usage setMacMenuBarFlag(flag);
 * @param flag <code>true</code> to use Mac menu style; <code>false</code> otherwise
 */
	protected void setMacMenuBarFlag(boolean flag) {
		macMenuBarFlag = flag;
	}

/* Protected method: getMacMenuBarFlag() */
/**
 * Retrieves the setting of the Mac menu bar flag.
 *
 * @usage boolean flag = getMacMenuBarFlag();
 * @return <code>true</code> if Mac menu style is supported; <code>false</code> otherwise
 */
	protected boolean getMacMenuBarFlag() {
		return macMenuBarFlag;
	}

/**********************************************************************/
/* Protected methods                                                  */
/**********************************************************************/

/* Protected method: getBorder(side) */
/**
 * Returns the component installed as a border on the specified side, which must
 * be one of the constants from <code>BorderLayout</code> (<code>NORTH</code>,
 * <code>SOUTH</code>, <code>EAST</code>, <code>WEST</code>).
 *
 * @usage getBorder(side, comp);
 * @param side The side (<code>NORTH</code>, <code>SOUTH</code>, <code>EAST</code>,
 *             or <code>WEST</code>)
 * @return The component used as a border on the specified side
 * @noshow
 */
	protected Component getBorder(String side) {
		if (side.equals(NORTH)) return northBorder;
		if (side.equals(SOUTH)) return southBorder;
		if (side.equals(EAST)) return eastBorder;
		if (side.equals(WEST)) return westBorder;
		throw new ErrorException("Illegal border specification - " + side);
	}

/* Protected method: getArgumentArray() */
/**
 * Retrieves the array of arguments passed in from the command line, or
 * <code>null</code> if no arguments are available.
 *
 * @usage String[] args = getArgumentArray();
 * @return The array of command-line arguments
 * @noshow
 */
	protected String[] getArgumentArray() {
		return (parameterTable == null) ? null : (String []) parameterTable.get("ARGS");
	}

/* Protected method: isStarted() */
/**
 * Checks to see whether this program has started, usually by checking to see
 * whether some pane exists.  Subclasses can override this method to ensure
 * that their structures are visible before proceeding.
 * @noshow
 */
	protected boolean isStarted() {
		IOConsole console = getConsole();
		if (console == null) return false;
		if (console.getParent() == null) return true;
		Dimension size = console.getSize();
		return (console.isShowing()) && (size.width != 0) && (size.height != 0);
	}

/* Protected method: startHook() */
/**
 * Performs class-specific initialization for the program just before
 * it starts.
 * @noshow
 */
	protected void startHook() {
		/* Empty */
	}

/* Protected method: endHook() */
/**
 * Performs class-specific cleanup for the program just after
 * it finishes.
 * @noshow
 */
	protected void endHook() {
		/* Empty */
	}

/* Protected method: setAppletStub(stub) */
/**
 * Sets the applet stub for this program in a way that makes it possible for
 * clients to retrieve it.
 *
 * @usage setAppletStub(stub);
 * @param stub The applet stub
 */
	protected void setAppletStub(AppletStub stub) {
		appletStub = stub;
		setStub(stub);
	}

/* Protected method: getAppletStub() */
/**
 * Retrieves the applet stub.
 *
 * @usage AppletStub stub = getAppletStub();
 * @return The applet stub
 */
	protected AppletStub getAppletStub() {
		return appletStub;
	}

/* Protected method: setParameterTable(ht) */
/**
 * Sets the parameter table for this program.
 *
 * @usage setParameterTable(ht);
 * @param ht The parameter table
 */
	protected void setParameterTable(HashMap ht) {
		parameterTable = ht;
	}

/* Protected method: getParameterTable() */
/**
 * Retrieves the parameter table.
 *
 * @usage ParameterTable ht = getParameterTable();
 * @return The parameter table
 */
	protected HashMap getParameterTable() {
		return parameterTable;
	}

/* Protected method: setStartupObject(obj) */
/**
 * Sets the object that is created when the program is started so that
 * it can be retrieved later by <code>getStartupObject</code>.
 *
 * @usage setStartupObject(obj);
 * @param obj The startup object
 */
	protected void setStartupObject(Object obj) {
		startupObject = obj;
	}

/* Protected method: getStartupObject() */
/**
 * Retrieves the object that was created when this program was started
 * if that object is something other than a <code>Program</code>.  In
 * the normal case of running a <code>Program</code> object, this method
 * will return <code>null</code>.
 *
 * @usage Object obj = getStartupObject();
 * @return The startup object
 */
	protected Object getStartupObject() {
		return startupObject;
	}

/* Protected static method: getCommandLine() */
/**
 * Returns the command line that invoked this program, or <code>null</code>
 * if no command line is available.
 *
 * @usage String line = Program.getCommandLine();
 * @return The command line that invoked this program
 * @noshow
 */
	protected static String getCommandLine() {
		switch (Platform.getPlatform()) {
		  case Platform.UNIX: case Platform.MAC:
			return getShellCommandLine();
		  case Platform.WINDOWS:
			return DOSCommandLine.getCommandLine();
		  default:
			return null;
		}
	}

/* Protected method: startRun() */
/**
 * Initializes and runs the run method.
 */
	protected void startRun() {
		ProgramStartupListener listener = new ProgramStartupListener();
		Component root = getRootPane();
		if (root.isShowing()) {
			root.addComponentListener(listener);
			root.validate();
			JTFTools.pause(STARTUP_DELAY);
			synchronized (listener) {
				while (!isStarted()) {
					try {
						listener.wait(STARTUP_CYCLE);
					} catch (InterruptedException ex) {
						/* Empty */
					}
				}
			}
			root.update(root.getGraphics());
		}
		root.setCursor(Cursor.getDefaultCursor());
		initFinished = true;
		startHook();
		runHook();
		endHook();
		if (!root.isShowing() && !getContentPane().isShowing()) exit();
	}

/* Protected method: runHook() */
/**
 * Calls the run method in the program.  Subclasses can override this
 * method to transfer control somewhere else.
 */
	protected void runHook() {
		run();
	}

/* Protected static method: createParameterTable(args) */
/**
 * Creates a hash table containing the parameters specified in the
 * argument list.  Parameters are taken to be any argument that matches
 * the template
 *
 * <p>&nbsp;&nbsp;&nbsp;<i>name</i><code>=</code><i>value</i>
 *
 * All other arguments are collected and placed in an entry under
 * the key <code>"ARGS"</code>.  All named parameters are converted to
 * lower case to preserve the case-insensitive semantics of
 * <code>getParameter</code>.
 *
 * @usage HashMap ht = createParameterTable(args);
 * @param args The array of strings passed to the application
 * @return A <code>HashMap</code> containing the parameter bindings
 */
	protected static HashMap createParameterTable(String[] args) {
		if (args == null) return null;
		HashMap ht = new HashMap();
		ArrayList v = new ArrayList();
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			int equals = arg.indexOf('=');
			if (equals > 0) {
				String name = arg.substring(0, equals).toLowerCase();
				String value = arg.substring(equals + 1);
				ht.put(name, value);
			} else {
				v.add(arg);
			}
		}
		String[] newArgs = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			newArgs[i] = (String) v.get(i);
		}
		ht.put("ARGS", newArgs);
		return ht;
	}

/* Protected static method: readMainClassFromCommandLine(line) */
/**
 * Attempts to read the name of the main class from the specified command
 * line.  This strategy is a heuristic and will probably fail in many
 * cases, but it will probably work in enough contexts to be useful.
 * As noted in the documentation for the <code>main</code> method,
 * programs can always avoid the need for this method by supplying their
 * own version of <code>main</code>.
 *
 * @usage String className = readMainClassFromCommandLine(line);
 * @param line The command line
 * @return The name of the main class, or <code>null</code>
 */
	protected static String readMainClassFromCommandLine(String line) {
		if (line == null) return null;
		boolean jarFlag = false;
		try {
			StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(line));
			tokenizer.resetSyntax();
			tokenizer.wordChars(33, 255);
			tokenizer.quoteChar('"');
			tokenizer.quoteChar('\'');
			tokenizer.whitespaceChars(' ', ' ');
			tokenizer.whitespaceChars('\t', '\t');
			boolean cmdRead = false;
			while (true) {
				int tt = tokenizer.nextToken();
				String token = tokenizer.sval;
				switch (tt) {
				  case StreamTokenizer.TT_EOF:
					return null;
				  case StreamTokenizer.TT_WORD: case '"': case '\'':
					break;
				  default:
					return null;
				}
				if (cmdRead) {
					if (token.startsWith("-")) {
						if (token.equals("-jar")) {
							jarFlag = true;
						} else if (token.equals("-cp") || token.equals("-classpath")) {
							tokenizer.nextToken();
						}
					} else {
						if (jarFlag) return readMainClassFromManifest(token);
						return token;
					}
				} else {
					cmdRead = true;
				}
			}
		} catch (IOException ex) {
			/* Empty */
		}
		return null;
	}

/**********************************************************************/
/* Private methods                                                    */
/**********************************************************************/

/* Private method: initContentPane(contentPane) */
/**
 * Initializes the content pane to contain its five regions.
 */
 	private void initContentPane(Container contentPane) {
		contentPane.setLayout(new BorderLayout());
		northPanel = new JPanel();
		southPanel = new JPanel();
		eastPanel = new JPanel();
		westPanel = new JPanel();
		centerPanel = new JPanel();
		northPanel.setLayout(new TableLayout(1, 0, 5, 5));
		southPanel.setLayout(new TableLayout(1, 0, 5, 5));
		westPanel.setLayout(new TableLayout(0, 1, 5, 5));
		eastPanel.setLayout(new TableLayout(0, 1, 5, 5));
		centerPanel.setLayout(new GridLayout(1, 0));
		contentPane.add(northPanel, NORTH);
		contentPane.add(southPanel, SOUTH);
		contentPane.add(eastPanel, EAST);
		contentPane.add(westPanel, WEST);
		contentPane.add(centerPanel, CENTER);
	}

/* Private method: addActionListeners(comp, listener) */
/**
 * Recursively adds the specified listener as an <code>ActionListener</code> to
 * every button in the hierarchy.  Reflection is used because there are many
 * possible classes of button-like objects.
 */
	private void addActionListeners(Component comp, ActionListener listener) {
		if (isButton(comp)) {
			if (!hasActionListener(comp)) {
				try {
					Class[] types = { Class.forName("java.awt.event.ActionListener") };
					Object[] args = { listener };
					Method addActionListener = comp.getClass().getMethod("addActionListener", types);
					addActionListener.invoke(comp, args);
				} catch (Exception ex) {
					throw new ErrorException(ex);
				}
			}
		} else if (comp instanceof Container) {
			Container container = (Container) comp;
			int nComponents = container.getComponentCount();
			for (int i = 0; i < nComponents; i++) {
				addActionListeners(container.getComponent(i), listener);
			}
		}
	}

/* Private method: isButton(comp) */
/**
 * Determines whether the component is a button.  This code is complicated only
 * because of the patch system that allows this code to work in JDK1.1 browsers.
 * If the patch system is loaded, button classes will not ordinarily be a
 * subclass of Button or JButton but will implement the ButtonLike interface.
 */
	private boolean isButton(Component comp) {
		if (comp instanceof Button || comp instanceof JButton) return true;
		try {
			return Class.forName("ButtonLike").isInstance(comp);
		} catch (Exception ex) {
			return false;
		}
	}

/* Private method: hasActionListener(comp) */
/**
 * Returns true if the component has at least one action listener.  The method
 * returns false if the Java runtime is too old to determine the answer.
 */
	private boolean hasActionListener(Component comp) {
		try {
			Class[] types = { Class.forName("java.lang.Class") };
			Object[] args = { Class.forName("java.awt.event.ActionListener") };
			Method getListeners = comp.getClass().getMethod("getListeners", types);
			EventListener[] listeners = (EventListener[]) getListeners.invoke(comp, args);
			return (listeners.length > 0);
		} catch (Exception ex) {
			return false;
		}
	}

/* Private method: initApplicationFrame() */
/**
 * Creates the program frame and puts the program in it.
 */
	private void initApplicationFrame() {
		programFrame = createProgramFrame();
		((ProgramAppletStub) appletStub).setFrame(programFrame);
		programFrame.add(this, BorderLayout.CENTER);
		programFrame.addWindowListener(new ProgramWindowListener(this));
		programBounds = computeProgramBounds();
		Insets insets = programFrame.getInsets();
		int frameWidth = programBounds.width + insets.left + insets.right;
		int frameHeight = programBounds.height + insets.top + insets.bottom;
		programFrame.setBounds(programBounds.x, programBounds.y, frameWidth, frameHeight);
	}

/* Private method: installMenuBar(menuBar, frame) */
/**
 * Installs the specified menu bar in the frame.
 */
	private void installMenuBar(ProgramMenuBar menuBar, Frame frame) {
		if (Platform.isMac() && macMenuBarFlag) {
			frame.setMenuBar(menuBar.createOldStyleMenuBar());
		} else {
			try {
				Class frameClass = frame.getClass();
				Class[] types = { menuBar.getClass() };
				Object[] args = { menuBar };
				Method setJMenuBar = frameClass.getMethod("setJMenuBar", types);
				setJMenuBar.invoke(frame, args);
			} catch (Exception ex) {
				frame.setMenuBar(menuBar.createOldStyleMenuBar());
				frame.pack();
			}
		}
	}

/* Private method: decodeSizeParameter(name, value, max) */
/**
 * Decodes a size parameter.
 *
 * @usage int size = decodeSizeParameter(name, value, max);
 * @param name The name of the parameter
 * @param value The default value if the parameter is unspecified
 * @param max The maximum value if the parameter is specified as a percentage
 * @return The integer denoting the size
 */
	private int decodeSizeParameter(String name, int value, int max) {
		String str = getParameter(name);
		if (str == null) {
			try {
				Class mainClass = getClass();
				Field field = mainClass.getField("APPLICATION_" + name);
				Object obj = field.get(null);
				if (obj instanceof Integer) return ((Integer) obj).intValue();
				if (obj instanceof String) {
					str = (String) obj;
				} else {
					return value;
				}
			} catch (Exception ex) {
				return value;
			}
		}
		if (str.equals("*")) str = "100%";
		if (str.endsWith("%")) {
			int percent = Integer.parseInt(str.substring(0, str.length() - 1));
			return (int) Math.round(percent / 100.0 * max);
		} else {
			return Integer.parseInt(str);
		}
	}

/* Private method: computeProgramBounds() */
/**
 * Sets the bounds for this program as specified in the parameters.
 */
	private Rectangle computeProgramBounds() {
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		int width = decodeSizeParameter("WIDTH", DEFAULT_WIDTH, size.width);
		int height = decodeSizeParameter("HEIGHT", DEFAULT_HEIGHT, size.height);
		int x = decodeSizeParameter("X", (width >= size.width) ? 0 : DEFAULT_X, size.width);
		int y = decodeSizeParameter("Y", (height >= size.height) ? 0 : DEFAULT_Y, size.height);
		return new Rectangle(x, y, width, height);
	}

/* Private method: getCentralRegionSize() */
/**
 * Returns the size of the central region.  If the content pane has
 * not been validated, this method computes its preferred size by
 * subtracting the sizes required for the side panels from the size
 * of the entire frame.
 */
	private Dimension getCentralRegionSize() {
		if (centerPanel == null) return super.getSize();
		if (initFinished) return centerPanel.getSize();
		Dimension size = (programFrame == null) ? super.getSize() : programFrame.getSize();
		size.width -= westPanel.getPreferredSize().width;
		size.width -= eastPanel.getPreferredSize().width;
		size.height -= northPanel.getPreferredSize().height;
		size.height -= southPanel.getPreferredSize().height;
		return size;
	}

/* Private method: circumventFrameSizeBug(frame, size) */
/**
 * In some versions of the JDK, calling getInsets on a Frame fails to return
 * the insets correctly until the frame is validated.  On those systems, it
 * is impossible to compute the correct frame size in advance.  The workaround
 * is to check that the size of the content pane is equal to what it was supposed
 * to be after installing and validating it.  If it is, do nothing to avoid another
 * validation repaint.  If not, adjust the size of the frame by however much the
 * content pane is too small.
 */
	private void circumventFrameSizeBug(Frame frame, Dimension size) {
		Container contentPane = getContentPane();
		Dimension actualSize = contentPane.getSize();
		if (size.equals(actualSize) || actualSize.width == 0 || actualSize.height == 0) return;
		Dimension frameSize = frame.getSize();
		frameSize.width += size.width - actualSize.width;
		frameSize.height += size.height - actualSize.height;
		frame.setSize(frameSize.width, frameSize.height);
		frame.validate();
	}

/* Private static method: readMainClassFromManifest(jarName) */
/**
 * Attempts to read the name of the main class from the manifest of
 * the specified JAR file.
 *
 * @usage String className = readMainClassFromManifest(jarName);
 * @param jarName The name of the JAR file
 * @return The name of the main class, or <code>null</code>
 */
	private static String readMainClassFromManifest(String jarName) {
		try {
			ZipFile jarFile = new ZipFile(jarName);
			ZipEntry entry = jarFile.getEntry("META-INF/MANIFEST.MF");
			if (entry == null) return null;
			BufferedReader rd = new BufferedReader(new InputStreamReader(jarFile.getInputStream(entry)));
			for (String line = rd.readLine(); line != null; line = rd.readLine()) {
				if (line.startsWith("Main-Class:")) {
					return line.substring("Main-Class:".length()).trim();
				}
			}
			return null;
		} catch (IOException ex) {
			return null;
		}
	}

/* Private static method: getShellCommandLine() */
/**
 * Attempts to return the command line for Unix systems and MacOS X.
 * This code is adapted from a more general command-line scanner written
 * by Erik Forslin.
 *
 * @usage String line = getShellCommandLine();
 * @return The shell command line that invoked this process, or <code>null</code>
 *         if no command line is available
 */
	private static String getShellCommandLine() {
		try {
			String option = (Platform.isMac()) ? "command" : "args";
			String[] argv = { "bash", "-c", "ps -p $PPID -o " + option };
			Process p = Runtime.getRuntime().exec(argv);
			p.waitFor();
			if (p.getErrorStream().read() != -1) return null;
			BufferedReader rd = new BufferedReader(new InputStreamReader(p.getInputStream()));
			rd.readLine(); /* Ignore header */
			return rd.readLine();
		} catch (Exception ex) {
			return null;
		}
	}

/* Private constants */

	private static final int STARTUP_DELAY = 1000;
	private static final int STARTUP_CYCLE = 300;
	private static final int THREAD_BUFFER = 10;
	private static final int DEFAULT_X = 16;
	private static final int DEFAULT_Y = 40;
	private static final int DEFAULT_WIDTH = 754;
	private static final int DEFAULT_HEIGHT = 492;

/* Private instance variables */

	private Frame programFrame;
	private HashMap optionTable;
	private ArrayList finalizers;
	private AppletStub appletStub;
	private String myTitle;
	private ProgramMenuBar myMenuBar;
	private HashMap parameterTable;
	private Component northBorder;
	private Component southBorder;
	private Component eastBorder;
	private Component westBorder;
	private JPanel northPanel;
	private JPanel southPanel;
	private JPanel eastPanel;
	private JPanel westPanel;
	private JPanel centerPanel;
	private IOConsole myConsole;
	private IODialog myDialog;
	private Object startupObject;
	private Thread mainThread;
	private Rectangle programBounds;
	private boolean started;
	private boolean shown;
	private boolean initFinished;
	private boolean isAppletMode;
	private boolean macMenuBarFlag;

}

/* Package class: ProgramWindowListener */

/**
 * This class implements a simple window listener for programs whose only
 * function is to exit if the window is closed.
 */

class ProgramWindowListener implements WindowListener {

	public ProgramWindowListener(Program program) {
		owner = program;
	}

/* WindowListener interface */

	public void windowClosing(WindowEvent e) {
		((Component) e.getSource()).setVisible(false);
		owner.exit();
	}

	public void windowOpened(WindowEvent e) { }
	public void windowClosed(WindowEvent e) { }
	public void windowIconified(WindowEvent e) { }
	public void windowDeiconified(WindowEvent e) { }
	public void windowActivated(WindowEvent e) { }
	public void windowDeactivated(WindowEvent e) { }

/* Private instance variables */

	private Program owner;

}

/* Package class: ProgramStartupListener */

/**
 * This class implements a component listener that supports the program
 * startup logic.
 */

class ProgramStartupListener implements ComponentListener {

/* ComponentListener interface */

	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }

	public void componentResized(ComponentEvent e) {
		componentShown(e);
	}

	public void componentShown(ComponentEvent e) {
		synchronized (this) {
			notifyAll();
		}
	}
}

/* Package class: ProgramFrame */

/**
 * This frame represents the visible component that encloses for the program.
 */
class ProgramFrame extends Frame {

	public ProgramFrame(String title) {
		super(title);
	}

	public void update(Graphics g) {
		paint(g);
	}
}

/* Package class: ProgramRootPaneLayout */

/**
 * This layout manager ensures that all components in the root pane are sized
 * to fill the available space.
 */
class ProgramRootPaneLayout implements LayoutManager {

	public void addLayoutComponent(String constraints, Component comp) {
		/* Empty */
	}

	public void removeLayoutComponent(Component comp) {
		/* Empty */
	}

	public Dimension preferredLayoutSize(Container parent) {
		synchronized (parent.getTreeLock()) {
			return parent.getSize();
		}
	}

	public Dimension minimumLayoutSize(Container parent) {
		synchronized (parent.getTreeLock()) {
			return parent.getSize();
		}
	}

	public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			Dimension psize = parent.getSize();
			Insets insets = parent.getInsets();
			int x = insets.left;
			int y = insets.top;
			int width = psize.width - insets.left - insets.right;
			int height = psize.height - insets.top - insets.bottom;
			for (int i = 0; i < parent.getComponentCount(); i++) {
				parent.getComponent(i).setBounds(x, y, width, height);
			}
		}
	}
}

/* Package class: ProgramAppletStub */

/**
 * This class implements both the AppletStub and AppletContext interfaces and allows
 * standard applications to run with the same capabilities as an applet.
 */
class ProgramAppletStub implements AppletContext, AppletStub {
	public ProgramAppletStub(Program program) {
		applet = program;
	}

	public void setFrame(Frame frame) {
		enclosure = frame;
	}

	public boolean isActive() {
		return true; // fix this
	}

	public URL getDocumentBase() {
		return getCodeBase();
	}

	public URL getCodeBase() {
		try {
			return new URL("file:" + getCanonicalPath("."));
		} catch (MalformedURLException ex) {
			throw new ErrorException("Error: Illegal document base URL");
		}
	}

	public String getParameter(String name) {
		return null;
	}

	public AppletContext getAppletContext() {
		return this;
	}

	public void appletResize(int width, int height) {
		if (enclosure == null) {
			if (!recursiveResizeCheck) {
				recursiveResizeCheck = true;
				applet.resize(width, height);
				applet.validate();
				recursiveResizeCheck = false;
			}
		} else {
			enclosure.setSize(width, height);
			enclosure.validate();
		}
	}

	public AudioClip getAudioClip(URL url) {
		AudioClip clip = null;
		if (clip == null) clip = getNewAudioClip(url);
		return clip;
	}

	public Image getImage(URL url) {
		try {
			Object content = url.getContent();
			if (content instanceof ImageProducer) {
				return applet.createImage((ImageProducer) content);
			}
		} catch (IOException ex) {
			/* Ignore the exception and fail */
		}
		return null;
	}

	public Applet getApplet(String name) {
		return null;
	}

	public Enumeration getApplets() {
		return new Vector().elements();
	}

	public void showDocument(URL url) {
		if (applet != null) applet.getAppletContext().showDocument(url);
	}

	public void showDocument(URL url, String target) {
		if (applet != null) applet.getAppletContext().showDocument(url, target);
	}

	public void showStatus(String status) {
		if (applet == null) {
			System.out.println(status);
		} else {
			applet.showStatus(status);
		}
	}

	public void setStream(String key, InputStream stream) {
		throw new ErrorException("setStream: unimplemented operation");
	}

	public InputStream getStream(String key) {
		throw new ErrorException("getStream: unimplemented operation");
	}

	public Iterator getStreamKeys() {
		throw new ErrorException("getStreamKeys: unimplemented operation");
	}

	private String getCanonicalPath(String start) {
		String path = new File(start).getAbsolutePath();
		int sp;
		while ((sp = path.indexOf(' ')) != -1) {
			path = path.substring(0, sp) + "%20" + path.substring(sp + 1);
		}
		return path;
	}

	private AudioClip getNewAudioClip(URL url) {
		synchronized (this) {
			try {
				Class[] argTypes = new Class[1];
				Object[] args = new Object[1];
				args[0] = url;
				Class type = Class.forName("java.applet.Applet");
				argTypes[0] = Class.forName("java.net.URL");
				Method fn = type.getMethod("newAudioClip", argTypes);
				return (AudioClip) fn.invoke(null, args);
			} catch (Exception ex) {
				return null;
			}
		}
	}

/* Private instance variables */

	private Applet applet;
	private Frame enclosure;
	private boolean recursiveResizeCheck;

}

class DefaultActionListener implements ActionListener {

	public DefaultActionListener() {
		/* Empty */
	}

	public void actionPerformed(ActionEvent e) {
		Component comp = (Component) e.getSource();
		Program program = findProgram(comp);
		if (program != null && countActionListeners(comp) > 1) {
			try {
				Class[] types = { Class.forName("java.awt.event.ActionListener") };
				Object[] args = { this };
				Method removeActionListener = comp.getClass().getMethod("removeActionListener", types);
				removeActionListener.invoke(comp, args);
				return;
			} catch (Exception ex) {
				throw new ErrorException(ex);
			}
		}
		String message = "No ActionListener is attached";
		if (comp instanceof Button) {
			message += " to button " + ((Button) comp).getLabel();
		} else {
			try {
				Method getText = comp.getClass().getMethod("getText", new Class[0]);
				message += " to button " + (String) getText.invoke(comp, new Object[0]);
			} catch (Exception ex) {
				throw new ErrorException(ex);
			}
		}
		if (program == null) {
			throw new ErrorException(message);
		} else {
			program.getDialog().showErrorMessage(message);
		}
	}

	protected static int countActionListeners(Component comp) {
		try {
			Class actionListenerClass = Class.forName("java.awt.event.ActionListener");
			Class[] types = { Class.forName("java.lang.Class") };
			Method getListeners = comp.getClass().getMethod("getListeners", types);
			Object[] args = { actionListenerClass };
			EventListener[] listeners = (EventListener []) getListeners.invoke(comp, args);
			return listeners.length;
		} catch (Exception ex) {
			return -1;
		}
	}

	private Program findProgram(Component comp) {
		if (comp instanceof Program) {
			return (Program) comp;
		} else if (comp != null) {
			return findProgram(comp.getParent());
		} else {
			return null;
		}
	}
}


/* Package class: DOSCommandLine */

/**
 * This class contains code to return the command line from a DOS prompt
 * on a Windows system and was originally designed by Erik Forslin at
 * Stanford.  The code requires the inclusion of a native library called
 * GCMDLN.DLL; if this library is not present, the code for getCommandLine
 * creates one in the current working directory.
 */

class DOSCommandLine {

/* Method: getCommandLine() */
/**
 * Returns the command line that invoked this program.
 */
	public static String getCommandLine() {
		try {
			System.loadLibrary("GCMDLN.DLL");
		} catch (UnsatisfiedLinkError err) {
			try {
				String cwd = System.getProperty("user.home");
				InputStream in = MediaTools.getHexInputStream(hexjar);
				ZipInputStream zin = new ZipInputStream(in);
				zin.getNextEntry();
				String fullPath = cwd + "\\GCMDLN.DLL";
				FileOutputStream out = new FileOutputStream(fullPath);
				for (int i = 0; i < UNCOMPRESSED_SIZE; i++) {
					out.write(zin.read());
				}
				out.close();
				System.load(fullPath);
			} catch (Exception ex) {
				throw new ErrorException(ex);
			}
		}
		return getLine();
	}

/* Native method: getLine() */
/**
 * This method header provides the link to the GCMDLN.DLL file.
 */
	private static native String getLine();

/**
 * This hex string corresponds to the compressed contents of the GCMDLN.DLL
 * file.  It would be easier to include the DLL in the acm.jar file, but
 * it is safer to make sure all data is part of the code.  Some adopters
 * may not know how to work with jar files that contain data.
 */
	private static final String[] hexjar = {
		"504B03041400080008001C4205330000000000000000000000000A0000004743",
		"4D444C4E2E444C4CEDBD0B605355B6307CD2A4252D2909D042C50201AAE28098",
		"366993347DA4D0948A14022929155AA83498D6D2D6F49C429562EB69B5E9218A",
		"8E33E39DAB73EDA05E67F48EDC511966E46A4AB1056490878F2A8C96B1EAC120",
		"14A9A540ECF9D6DAE7A40F1F33DEFFCE7FFFC770CACA7EADBDF6DA7BAFBDF65A",
		"FBEC84BC3B7650728AA214008240517B28F1B1527FFFE10126CCFCD304EA95C8",
		"3FCFDA235BFAE759F9EEB21A6DB5A7EA2E4FC926ED8692CACA2A5A7BA74BEB61",
		"2AB56595DAECE50EEDA6AA52D782E8E8A80489C685E7A3C25F3D58EA0C414ABB",
		"D9B99BC45DCEAF20DCF5E6DDCE36125A9C1748A820E5BBDE34395F2161060957",
		"966D7063FD1FE2D56EA3A8A53205F5ABC77F9E15CAEBA126CC1A2F1B1745D911",
		"41CC6B9F081F1A80F524A921718D383E14351252BB243C78C2245431AD19CE27",
		"646A296A1384A67B286AEEDF1ACCA70127E26F8F373E47AB292A7774C67A711E",
		"7EE85940BBB6D0103E9E2731641FE13BF46881CA024F69095D42517F9E22D2C4",
		"4C690C861F2BFC5B20A25169384E0D12AD86EFE0F917785C15551BA05D95D837",
		"82B7E33B780BFF06EBD79E6BCFB5E7DA73EDB9F65C7BAE3DD79E6BCFB5E7DA73",
		"EDF9073EABB8B3422C387A1A3B97A7E427404ED13E257578F881F215DEBC2067",
		"5302427005B72AC885093B9E049797FBB4689F02EB7336153B2873AA9B7BA0F6",
		"9EC7E1839F02DE1ED7EFEC8AC09410AB04FACD7E75934106D9CB34DEE8382FA5",
		"E172143B1330742ABDB1733152AB6AFC4CB89FA2D8CB61DEE478C8A1558D47D0",
		"B91453ED3D4AF9BE72D94E2DC4F9D3C06AB3BF50DD848DF2EF86638A89E2AF57",
		"53D4993F63D68C09D832F66CA7E151A870161CD19D0AACDA310E91B72EE08D2A",
		"12D1F2572349646A39540B237499F1423805C8671E435A8F40397F879CA202BF",
		"8256E69B49D1B632211A4373783E7C32E1FC2C40E04B11F5568CED0006024E76",
		"308CC9766F072AE532FE6170F0B9BEE681C2427ABE53483634501A21B6D48B83",
		"E3A463F858A85628C42A8067769942F04604A6F25F40CF0AF5FE80861D94334A",
		"60F03505C9B0FA5CC1E2A27D2AAA5CE5E6A1027F170C39647B6DBD1CA3D20B96",
		"4B8CCA9C8EFCA99B5E86D6BDF5414B1F1DCE0E8531896D2618104B17AD12188D",
		"536094C251A865AEEF553735E3748999FCEC2141807CC0F370451A07C9BB02C3",
		"0B79EC908C9960E962A21C05983BF88D00ED21713973B388F82964C150C9BDF5",
		"48989E1C6A32CA31DC209B1714385B6F601A673BCB29B948FB0ABE01C8171676",
		"702E1E0BF57EFECF90017D6C53413F40C6E868A039814DD7405206239E3909D8",
		"A51314FC1BD079B740644D07830A34DC8A121CDC182279F414773F8AB810AB11",
		"D311584B384A3E63559077B85CC9D74F86D63B20F2C6248C38B9BEC0782E02B0",
		"C384A36CA7C24227283DE78B3BDA748F121AE3441A85CE823FAAA0CD37E2E043",
		"EFB7BCCD5DA8990A6C6672139A0F42D56358F592E74B141C77E64C98A91B6328",
		"EA75106B0DD7F986B20187A5B0463DBAA14B9E0B7AFFBA6260C5AD815EF0DB41",
		"9280ADBF205B7A0166523F90634E5E8D52A7C1D18C8D173B1707813719A5B1D5",
		"A6F9B51D65B35EC518CDE97381637A665B02042C0FAB0EE7221C333D511C05B3",
		"F2A9703470DAAD055EF6C4C3072F1304A1D03D17D30998BE00A2508853C2C7C1",
		"189BEB35CC2C2F691FA7FA3128D40BC852BD8656029F0D1371F8F889C0754739",
		"883D8C938AD7014976BFAAA35C0659143F4B4A0AB15A601AF4481B2E067650A0",
		"274047DC38DDD164799CF90BAA92EC044597357F3D647459634089501D2047B1",
		"73A1AAB88AB86E21763DAA10585CCC6DA1E536533EBCDC6EF9D672A3E3BB7244",
		"7A3984DE9855A72CD7F05E10E04287109BB01EE7A9784D47B9C6ADC5C9104069",
		"708C125798BA49850A2D270186709C9DCF04D551C8E5CC17538962CA20A6E688",
		"A93431152BA6B2C5D43831952BA62ECB482A3F1DB9C30C1E33CAA3F98F60200A",
		"61F2B99CF5E2B29C2C2819B525FC804FC4F3231E992416BBAFE2FF032B74D982",
		"D83F2EA7D46BEB814A7982D2BC653E1DCEE5CE1722CD5B0C1833604C87311DC6",
		"AC18B30A915CEE52C1FB0A54B684071F803626A53F8FC178B38981E63C1343CD",
		"6901C5C96761F37C3C8C0ECCA65E8021029E195CD01D1857F165629C4C2B7B59",
		"A0E7E0ACC2AA18B7429C3B3BFF2F2049E5D4E889EAE07F46F43C88D9499810BD",
		"BFC359E02E1D5ED9DC60B340DFCD25E3AA76AF86EC02E13DF7520C77AE45E97C",
		"CF6D2589F562C24412A524C1A66326B513458D99DB66C07D0273DA7418439CB6",
		"F910EB0A4742F7C7401E62BA6F8EC5B6D7127524EC4446E92CBD40046E7101FF",
		"3B6A58E0E67D5BE0A64902E7DD16335AD8B8AEC0385E0862DF70C5ABDC5B50C8",
		"7E194584AC79805ECDA62B76501A39632D57F02727E098BB8230DC67A6A034D8",
		"7A8172A4D3CE874F45CD49A6633C30C1D6F752CC74589D8128A8763E9AE8331C",
		"5A6A07B264C20DE39E285C9F1A770B3678B3D8A0FA2546E5657A6011C97250D3",
		"80A6EFD9EC0AB1B098ED54B39FF579191557D46399FE0A08DE663DD0CF46B666",
		"D5071D7CD20C912BC2C8121C8DFA5E98A24902D3534E15F0B7CE02D6F6AB9A05",
		"26D559AE0C719326CD24738339BD9783119DEEE43F05A12A0493A1B916144560",
		"026C69D0CE4DA41F5C17BF1B36D90EF66CAEE3B506D80D1D7CDD6C71D356B207",
		"40618020EED4EEC0B953C2274AA7AF05F1023358A5C0AE57529D560540781417",
		"8D68AC3FA1B11DCB2DED9E8F56390B7CD90931A0839602571BD9AD093994BAE9",
		"0C4574502EDA307FC1B8C9E756F814B77AB313347B2825541DDC1AC60D9AD3B1",
		"C1FA65AF2740E0E03F9945B8A24DACC8883742E263CAB7F998003CC8DF82F63F",
		"03F52F52D9FA4620822331FDC966615B3934AFE1285490F9AD79D440BB8C5E3A",
		"D0AE64C603A7B9E29E96C6BDC575B03D337D0A7FFBE5704EE5C3CEF90C8D624D",
		"6F44AB8D6AC951F87254EEA76125DAF9495AC25E21BD40C851B280A45864B37C",
		"BCF5E7FA93E2B8F81427A1369B213055CD273B73148D4C54F9C0EA801AC63850",
		"C775E45EEABEDECFFABF0606B2C904B2824067160CB363B801F763307FBC194C",
		"44C33285351089268C02A2CA11267C3325267E034C04A2081EA2342C62AFCAD5",
		"DB3D30FB42B2928889753DEEAFEB8A8BD6B0FB73410FBC8E4C7219205E89BE87",
		"C8B84E63EF575274946FB10A98B22353116C57423899DB41CF5F8514FE495868",
		"EC8DB0863A158D435AD800B7BE0EABCEA13F699E3E1F52CE02302A6E0633804B",
		"464B1174FC4566524097964ECB163BF94498CF421F1D216B8D48ED60BEF029FE",
		"0B5F06D9F9ABD0076ED072B5D09B6E2296208ED06A91C4AAC04F9CFC208C34C7",
		"DB1A32D20BE9EB56F16F01BEA5A3D03B8E36C2CA3D3993ACDC2E85FC6BD30CE6",
		"B840AAF117AF82F1351DA3DE995DE1C8296A71BDBFB0681DEC45423236C41F21",
		"4818C5167BD1547770452AFD71F32A6581770A77B94B8EB5E84830ED599B52E1",
		"353798663313F4079B8FAB1F6FB772C7FE7376E01621024630BC55D9A25EDCAA",
		"54EFFEC03AC09E8685AD00EB502C896B99B9D8DA741008D349CDC799BD0D97B5",
		"7478C3E548E6F750ACE85C2C50A8911A4CB04EDE83D65A95C30811D6C085DCC0",
		"6952F41AD14ACAD1CC0872FDC945FAE38130EBC206D35AFA323277E3C03E1933",
		"93AD0F52B4CA972B6B30CF66145C7B204C7F527FD09C1704B6C11939F645F371",
		"3005B1F5716B170B114B988BADCAA683742ED663A2B0FD74689F36359FA41700",
		"DA75EADDC7061E107B1501FDB40A11A4BF01B57AF7C18146B1400EB94284F58C",
		"1B244E244D0169E8F519B455C0FF019996B35A4A90AF5B73B003C77A85C359A0",
		"17CCE92839203717D00655C080BF5EED4551EA0CA75B61871162F34112DB0CC4",
		"FEF526E7A25B330E8684EB66C2B88F7CB6A0BDC0C70CEA0F721DBC05F63F8E01",
		"5F6BB0FDB33079A79D6F9B8E92256A4C22553720BE4F958155A0421456B00573",
		"0B77CE07B2207F3AAFB84680C3152BDAB640FB0ED0685CBA1BD7CE49FD80A5A3",
		"3CAC88B945789FEBB35CA4555DE18882321298219A53ECE01626927B7327E607",
		"C20905B0BB997A58084AA956DD46F30CAE939E207F73A3398EB92C065F71E9A5",
		"50DDE170CCEB741C1BB4DA9D0E07684703D4E12D013A6915FF6EBC28F5A068E8",
		"390EC72A3B9885098025BC8F363D2434FCD020D85CDEB5A0AE00C0BE5B8F4BB8",
		"2360B7BC492B2D1DB402DCA06CD87C311719E933CDA0A3ACA638E62A7E0CCCEB",
		"B472FC2A7E53BCB41A19855E088C87EEF3CA1964DB018AABB16E17EA10701160",
		"B37D02F7BE5BC04CECB2F5CEBF87D234D693A066368A3438271C28277A3C58C7",
		"E3C0B50AF925ACAD571178974F94E3FEA9723F8F34CE84111A069186E1C7D2F8",
		"320C37B2747487C39868361DFDE1708F0211CBE5AB3BF47E33D848E594FA71BF",
		"1B5FA0825AAB26DA7527EE9CF44FF87634A307E53B717F6666B807D1EF7C48F4",
		"8C1975687FAD208AB303898676F232A2F25E04C15FC5DD82D6C0369B934BDE05",
		"6910161AE5A053E5C6F7B36E7C712BA408EFBBD10F2F2751A156316C4ABCCB76",
		"C62CB2104A5B8FAC2B16088D91D2A235A399E8387CF8703AF6A366C2A56EF97E",
		"D6AF68A61EE7287BC70ADFD20465E3192CBA053F9A6522DAD97907B8FDCD32EE",
		"34F88756859DAC3A5874BC7011E4BE5EC9F571CEFCB65EB4FF0F99CD74B42FDE",
		"CAB6AB7CF1BFB4BCE539EF53587D8A072C076A145014063AEFA0BA69129AEB85",
		"CAE693DE22A5BA09D72A7B359C51B1254A749DCF0451475D95A99B3E2516F36A",
		"AF0DD6A2CA9BB39ACB52B0834A75F31B981FDB0D0DB69D820FF93ECBD1FA9B7C",
		"8A95ED3D61DCB67CB6D4A4A4B8742CE6A663F942F9C7AC5F6539B1F5AF5C1138",
		"B88D971FA6283FB76D2D13D995B3168DF44069E3E51D9047321A31C3D178F9D1",
		"504613662C6CBCFCD350067AE781C4C6CBBE50C60398A16DBCFC4828E341CC50",
		"375E7E0C33C641462BFA36B56BC1D83A51E8DDB636300EFB6A174E80D628848E",
		"A14E8914189510BB05E687E88D3D1B6A36F5A0D831D1B0289576FE2174FE0A3B",
		"44090267FCF06177CB6C4A53DA86C74776B42EBC15091A1FC0BC1E981CD05E5E",
		"176F8706C0F5C0EDDB6B1BF4D9FA4A77227A0797D757EA8DC66821B6B6A2C3B9",
		"2849B77A914E0753CB9E5542FD5541D0B9A06E2F5915781D42DDFC029E5ED806",
		"399B06E6C307D41705B91A15779FD2C1179185AD18EFA7EF638704BADEA7AAE5",
		"B21FC174A17395EF6E547907F5C7F5037A41385A540C74A124734B2EA9AB9F4E",
		"EA0296D3C1BB494209F5CB654041C90750F32A1EF12E526172341DA88CCD181E",
		"096C277765027380593657A1DC43F4682CB65B2E38F858916411C9C65D9BFBB4",
		"080418049DBB99CB8AB773597176BE2F5E44C28334DF8B09340C57E359742ADB",
		"ACCD20453E3C3E7338BDCD2DB8F91F2FD0FB2DAA3779743A22ACECE064CF05AE",
		"AFFD33B9E561CC5237C7623DE9A444DDF4313A6EFB50A990D31275D311C868BC",
		"8C32AE6EFA8292F62A9FADC1BEB2E5674DC405CE27BA85D140A6FB19177A6419",
		"C4ADF0D537705DE0D182F30FCDA615D6CEC6E4EC3854B480CBF29672B97CD0FD",
		"345429E00333B14B1009837261F759E29DF35F03215FB63CBE90F5CB0BD99E20",
		"DF0FB60B77CADDE4424BF97D70660A1C7C0F04EE0692F3EE945065077F10B335",
		"5A19E55E4DCA7690599F1F98BBD2D78C0364F7114430EF1E4176ED424A79BF68",
		"9482BEDC0A81EF591C58CE87A3C83F0DC618CC46E39BE8B31DEC1873B4F493D0",
		"D1D22C378E13EF06D16FDBDA8AA352888733E488895F890B020C50FDC01B0DB8",
		"3FA67DDDD4889566F8145F3740CC6B22C6ADA063BBE2F8176324035795C30E25",
		"6C3D0E6B082C4E36816082D6095C7070D3D15675BE8E3905C4A09DCC6E530047",
		"05C28902FECC8590C9AA6C1C7A1A1CF0ADA708E63A72621489988C1C56F448F9",
		"676021926359A56078135185D80668A008CF93FC30827C5A10ED0CA5CF409880",
		"9D6006E3298FE3F5B178F2005B2713CDB7C02C765178772790591EC5DF890BD6",
		"5B14044CB17305FC093C2D03178D9953C0DB80477ED57095720116BC9D9F3C8D",
		"6CBBD0C7A9DE8CC0B8027EC685D03152243166ADFC9720051D403F0F0F9C24A6",
		"9D3E916B36836226DBF9C5688FA16F573E91EF0F001A6C41B14DD09FE2A20EF0",
		"B1F1148BDF81DE22178DE75A5C5F1B6671C7E61D65F7292C5FD52CFB2371D02E",
		"D4847147E44756D8F9DD33C41961A6B29D9A1085B6506658718748685E3416EC",
		"C48FF6CB613E558B97647BCD6C38860AAEABB8C3DD80F2707532A94B50194579",
		"DCEA0E30130917E4A40D142758094771E03BAEC0383D069D20A641BD92B710FD",
		"D83B3C26602EF01F005207CF00568778B60938E86B5DEA2E04B787039DCCE5A4",
		"81E169095FBD1D34C1064B17BD8ECB996F3631ABC5E3A67888C659C29FC67321",
		"58C3CF9F0702B569FC51745F0AB91CDD08CA33124AB384B28CA008B5F3F91A29",
		"E7EEF3A8FCB91CAB25FC49449E0195A741E52976DE0E455C4EF62D43C4095A28",
		"21DA2DE1F9C8563C20C699F7E0F111032D94CEC47DC8CE5F4FD09CFC640C6121",
		"3879FD5920D3C72D2BB544E32993BAE959A2EB2C87E85BB9ACF942A4651FAC3C",
		"259765084574A1883514592AB4603B6D58DF9B534A42414C9AEDF3E928CE3A7F",
		"3869C0A46138A9C3A46E3869C5A4554A72D6A5420339FF3A444F364F64605F25",
		"0760E356F00CAA002EA7949CD8ACBF0BA69607DBB55CC59BF1D81067979FFD3D",
		"538B062ED4E08B8328EEB73C03E61A3D9BE515F454968FA655B9B41C84658F02",
		"DCCA8E3D93C8A742899F13315E506E05FFEF8D1E98868B2FFC56EFDFD9FB3048",
		"D86148EEDC0F1F6FF441F285175E5887CBA8F16C5CB8B489C0A6E9F4D978BB90",
		"8CF842EC0E62E5CB5EA764B8B946CB7021B63485F3D023ABA5D3D3DF6A3BD7E4",
		"EF6CC6B4964E71F8569D2F50EF8E52EFF65BDA374F9F77208BFBA0FD4C986F2F",
		"A9B0470BCFC517B8B7D8D3F28BBF5DD81AB6B0C9CF1C5BB7060D40E4C9D7CC5F",
		"41071E5BB63B7D84A8BD5CC6BF318BD892241F7261CC9CC3C54EB12AFFC99C31",
		"38C1D1386EBC7229E1BD3607B5CC5ABD7F23B7DD86CDE1D9C8948616F4EA34AD",
		"DBC3917A4B0BF508F833F103ED61B4462CD26211100D7CD2D9808514767E6F20",
		"5BEF6707B33CD7B183776C8E091139D0D0AE9548CC64074B3C93D9C17B374F08",
		"D139D070461BE81945E5752E2F58CC9BA6E1F642CC9A7832110E271E1B9FB45C",
		"8419295037E5E3C8C37660DE656D02A172B17EDDC2F43E1014CF97385DCEE199",
		"5237CF02549C7CB69E970D0B80978C8C773A0E82FAC1B350B1A1FE1C6CED7FC2",
		"F3AEBCF3AD139B8EAB9BFE0377FBDD59827AF7F1338F52229510059F6A65FB69",
		"85B7A8F7B7BE7FB1030BAD72EE62E0E656A7ACE9387D23CCB77AF7154BD7E618",
		"6E556FEB63262857EEC0FE5A2D5DB55FE7E4B446C04C9F1040BE3B946C7DAFC2",
		"D30E9A9B486457388A241A3DFCF530F43E6F36D4E52E10F17CF65922AACF06D6",
		"8983A780255CDB9F95D55027802CDE06A2A23F98C5B58B854A6B3A6EB760E474",
		"F2C760A648556F34520F4478A763A8F787A43E106D9E7E0ACF0E27F0EF002E3F",
		"484E5403723070613660BFE79F8FC3F9288F7157E372357F0D4A38AF47288FE6",
		"1FBC0C8B91BC56F1A6230934E7068798495DE198125F0E6C87A908CC6307BF19",
		"9BDD82D931EC6090518F64B73DB11D579DD2128EC3219E7C70C9784AEF654E59",
		"2ED10A7306AD716B4182F9763CD0E4FAC4FCFBF165CA61D41FE4A49EA9F266B4",
		"9129CE51B46167BD39CA36320439B0A5A266190CAFD76CE454361C828DDEA539",
		"1A6B802725E932E0A33EBAB50527BA65A92EDE1A382316E0A2AFD7B412596ED9",
		"AE9B0E496BE0332FE18EADEF1198180B89D32A275F7D065809C8BDF53DA37458",
		"0F9FDA8F3A2C9AFFE525D4612C39FDA09849E5DFF04FA352EC0A9F3F7C58D5E1",
		"B6CAC4453C72D00BBE2BBA888CAC034C61057BC3F390A0D81BD081A476EE2173",
		"EA8DC693E5AEF0BDB8F88090B5A30D8B7DAA86B65D246C094CE2F21314F3ECAA",
		"C62BE89F7A22597F8CA5DDF3053449169C86CB836D41030B4E55C00DCDAB53B1",
		"9DC1F6F36AEEEDB2830A1877DF76593630E6CDEBE326DC06EAC29B175437BF0F",
		"050E5F7C2237D9BBAA9F7306BDAB06B955FD03FB64DE221543B75F512C61AF64",
		"D6CACB33EFE06E57586E57320BD92B5A9CDB869AEBB8432702BEA5618A4B2766",
		"ADDD933D14C9CC0106665D1788F72DE9192E7972CF7EC086C2082C2C54A09BE3",
		"50726B1420A0F2BC7EEF1D0A6E958A83CF954AAF4389EFDC8F8D69B56890FD04",
		"98E9573783BB47CD630621A7FDAAA23CD3CBA86E2FB67C541BC67D24CF1B146B",
		"59DE030290F39EE543BA98CB5371F7282CF728190B7B55FB3AE1792AF7D68973",
		"97DE9B4503BF4BE50A661632951898E6BBBD87E4BF22728B4584DF7B14848A92",
		"BB4DE15D26C615DC6D4AEF32250C72400E0E2E5BDF4F314ACB87EA26B48AC0D1",
		"F3A98E718580ADF4162BBC80BD0C3E95F06959A664D6B7C2A0B4E4A987DA6114",
		"5B205E734343BD9A62267087C8989E0840A391D7913819DDEC3DD99171819B09",
		"920686751456A102D27F22E92F7C4D84EF4858C5416F84375B071E7C9F00AEFB",
		"255C99286C78867F0A198CF6438A4BC6D30E72D4D17E5A2DCF55BD86E71C8E15",
		"C27B6239A922D18EB42B4992B36AC442F699166C8C0AE50EE52E12A3B99A86BA",
		"451413C9AE570C911C768B5260CA1CB014AA55C27B244BA8D68C7D2143849C8B",
		"2522AE68C02385030471DE01DFD2896757F87263C078F5E1513228A8FD2A211A",
		"F12DE18852AB60372963C8EA206B087531E67BD391CD35EB8A0F8A4B888BC635",
		"85B6633B63F0657BEDD08A5D3CB32908B1F2C47A3CA864147AFFBA0E565C8364",
		"C54A0430EA74EFCFC237D2621560D667681457B7A5CB9BA3C1BA8145E50A377E",
		"87801C201508B18F4BA52A6692503BD21A763C70945DA614BC19DE6D0A219CB4",
		"92A3619502D759BC4E3C6725AB5909FBE73DC0FBC980FC58CFA2667FFD65AEA3",
		"CC4F9673930E97332C04DBE01D5EABD26B55B07EE512A6BF5CC15D25E766ED9F",
		"ABE5752A728425B182AF14F165D153B85BFE82AA0623EF8A775570F322EED0BC",
		"B7DBCFA87C8B35596CEEA0C0B69C4557CEB72388371F341082E1DA650DF661D2",
		"AEE86AE0218289706CE736E62DBC20631B0C1F44DF7AB9CA9BABF066297D4B20",
		"A2F46629D8D25F65537A61B177EFAF50705A7316B51E186A6FF2C37CB52C5BC4",
		"C823B72888ACC1DAFCE2D2F1597625D741CE64885253894A0DCD087013EB55DC",
		"FBF35680629BDAFE859A3BF42DC5D6CF2D0FB29FF5DD66B9E0DB6A0E7233BD79",
		"1AF0DED40FAF82D2810E30296C10917F60B9A87EC482676279C1F6CB8ADBD8CB",
		"99DEBCC1DA88F2CC4208B9750ACB3A25B388BD2CA9B969B8DC067D4B6544CFED",
		"403D77A3A8E766B2ED3DA30B9FDCF12D55B75C09D4BC185170F72BBD754A2E4F",
		"33EF882C2F8827F6EA879F256F0A83A055DA05C57256C8F42D4D0CA2D65BC715",
		"F5FBE2AF7A8B34A027B17E91C65BA8F4DEAE40C572BB123E2D854AA6A075E938",
		"502693419908A04CC6296AE21BEA27A332797BB432F165EFC8E6DE0E684999C6",
		"B7FC5B3AA489300DD9A1E5AE417D9C17F4652705BD4AEF525910D4DC2A952F47",
		"E65D18F466270D9E4923C6F319135AD2BE5B711C8B543703E56532EFED41DF5A",
		"7D1066AA7D48B18C1DCA844D04F2B04BC503B6204C00D1914C502A2605303896",
		"E5B8AF0C0DEF2B4740F3D111A1F19E30B2AF2C23FB4AC4F70E36EC2B30DE9B89",
		"CEDDAC04AAA876198D9C097A87F9111BCCEBF7A92E70753894DEFBC8F8DE0E9F",
		"4A698383618D9074F4100C6B041956A2A38FBC4186E83C8E9C990CEB111C56A2",
		"9A97F58C2AAC1B1ED665A386D5A6F1CABDD9714172161212EF18C01FA502D9F6",
		"A9ECE93E07ECD120994ED03105209E5A5FDA0360A7D58F678F0827CEB1798342",
		"201A440F16B27EE0040FCBBF8D68BF6E2FD36FF9381003FDE02C73F206E70C8D",
		"7F9B19CF76C4588A82DE22A5E773089904EEC3C0C431185188D18D08BCA55BDD",
		"84474BDEE984E0220DA761AF08A020E8186E3BE910B7754776A8620A47B2E6AC",
		"1A44CFCF979BCD25CE61FAC7BFC731FDCC54EED126B14CB005D9760567862AEF",
		"D367504F7C77D52EDD91AD17E61C613649AD400BE5DA758162F63E25457A00DC",
		"01F39EF3D8831BA10791A46484F33E4B37338157831BC47593636E3ACEC1FFE1",
		"0A7AABB76BBC32E8096B12D4CD09686AFBCFDC0BCD1E3BBDB8F9202853683DFF",
		"B2828B9A0756D18590D4E60D6E4341C1AB2E6853724B1496254A662D2C3322A1",
		"F537726FE3FA09FAB69A4014BD4567E7AC6DC9F6020CA9193D977716CC8259E3",
		"03F361518968DBC9862962CFBA6E480D3598F178389E7776D6ED0AC98C188476",
		"94DCBD0A6F9D021AE4EE457DA16EF211A32224B04B945E40588108DE154A89AF",
		"625160C70F0B6CFD9C86FAF13070442261319D8FB48C92DD60E4D6966C342900",
		"257A446E23EF438BCCB7B745925B622F9D38176904FDD67C901EEF8DF22E9D88",
		"BA005424D3273F86CB1DB29260B5F7E39D2F5F9DCC9BC14CB34CC71D989904B4",
		"2CC45A60C649D62EE47895BE850A22FB6D3D1C795DAE166F93E1412379FD03A6",
		"2CB97F0986AE0A2DE5E6E3F472BDBF353B4159C05D66AF283C89973E00AF9B56",
		"CD3BD6328EED92B16764CC007700EF48B663A081E010FB573938EB74C4C517A0",
		"7A9488F705E20199751D78FFA643BC02F724794564DDA7A4CA35EEE7D1418AE3",
		"05A18D27BC3146F10D958C4E1875F706788E756FBD6BF856DD4E09396A0F952A",
		"A57035E0CB05F1C25F4F20016FFA511CDE20945E8535D6F74EC557154A3026C8",
		"552B7072D1D978F7737250E2DE83ACF83E27C795A177570B2CC9E4E6C98DE50A",
		"FE50403A6E71F27FB84A3C27F1C0452F1EB8E0E924B9CB22D695D111E20D16E7",
		"589F84DF88CDE12D2505DFF105716BB626287A36CF26E76F8C740039C50CA346",
		"6B485ECE9098F7114C93908C9756F05D09FFA6405E951C1EF514705B1394814A",
		"DF33E40BAF9C80735970A91DBF724B4F6E95B1EDB2263F9D2E6630E7A15CF627",
		"6168689BFC28DB27E8F781C2F80D259335D23C971504C4394DBDF4B4DF804142",
		"D1137E832E281D16F8B3AF0ED4A0AF6E28A0F4D57D1390FBEA82D0884A6A637A",
		"EB446CE338BDB1652A4CBFD4D01781702F2415A42D4E26B5C64D1C6EEF34D430",
		"340DD0375DDA475A9B842169AECB3B55149F8D62A4737118359C58D7D12285A1",
		"FE637F25561242DD5D16EA6E388E893434A111FA9BFD4F82FE2788FD9F3CD2FF",
		"2C32AAF3DA3B7C5943C3B16F8663412916BACF5C807E211820F806B19D3B26EF",
		"B40CD52A2D83EA07B6828ABBD445588B817503AB87BDACF4DC7CF15921E16769",
		"19F8E6F84F58C89E5178546C8F5C7E404868B6A7C3DC27F89642F10E088F4272",
		"C77AF8D80EB00B60CEB1D6889671AD39B296C5B2D69C3020DBB2388CED94B35D",
		"84F8E161E2BEDBA8615C8225633BC3D8AE30C47A6E186B8788C376220E2E6728",
		"6D194D430F8116200E4003A004A0000681973E002EFBE15E6FF623BD10F210F2",
		"109E85F02C847D10F641D80F613F8483100E4208C6CE23419F02153025EF930F",
		"864643C04EE703AC07A03388DB51BCEE60C70EE470745CECBD94033C8E191118",
		"8E10AE8F4E0CA2BD2ACD41C2C81C447F73F1D920B4DB9D89ED5EFA0046BA2593",
		"F437342797C539998773722A83CC0922EF388BE3001113406B8E7CCEB196C572",
		"F61C19BDF364F47E3F8A34F226E18CCCD539C024B3D0320A73C7283C401A99B3",
		"7372F63CB2AC7EC009CA602CEDED103C0EF024C033002F02BC02B017E0ED4C32",
		"37F130D6F110C6411807610C8431106A20C47215842A0895102A215440A8183B",
		"37E218F1F0D9978927929426C63A6A6E72E4C0F5A8B920E9B1F3303A6FCCDC89",
		"EB075FD2E2255E77AE55BC019C337DE40D2759EF59E4D52D79F349AB511FC02E",
		"A6F186EDC199EAC077C4D9091A7BF990DB0E044A4113C353EABD812CFFEC042D",
		"B75AC955ABF085EE020B9D9040CFF5196A39D5CBDEA5094A6FAE8ADDFAB28262",
		"26B9893D92FDB292B7A21EC87F5919E8287D842814708EF19294DE5FCA9137CD",
		"8D750A6C88D1702B54DC4A9579859209DF235E225EF19AAE191CD02829022628",
		"9AC28B14DEBB55856BC46F37B067B5E4E4962B560E74C899EB4BDBF00EB4D7A6",
		"8431E17295960FBC79418FCAB25A51330E2CAA3EA05BC06D53B1020C0CDE29F8",
		"881CF37A57812BC729F1DAABD27248CDA2FBC56E5150B45CB02917E22581F76B",
		"7BC8F5A8182E67D0620BAA1F980D2816D8ADA6E15BDD3E3C97BCDAF839D50776",
		"DB40B37FDB2473DAAB831C1E2FE2EB971C4BDFD673E5F13E5B8FDD21C43E2F7D",
		"DB623DD46C046B8AA2C0DF70A0E767EB3F4C3BB9BCDE8D8DE6BC3BD4CD993884",
		"5969F2F64613FE3C82BA7901646C6CDC12371E2A60FBF33ED8C86E89A0D47FC8",
		"8D01CF2E4EFD6034A9A1B2747BD4DC0AA5FCA8E5C39A8881AC9B1A98CDE532B7",
		"693B6EA8CF88AD370B7837291A196F3EC81DDB361984B41F126613AD5CC2F260",
		"CFDCDFDF7C9CB9A55CBDC6F2D6FD61DC07FAE3CD27B74EF2297E86DDE2740B2D",
		"27BC266E68DB39F6B2A67E5C96975002991B69E4CC5343E48CF7FEE601F5C38F",
		"626BD3313BC4C80964E25EC896468A5E4046D1572D340F6CD58432E5CBEAFA9B",
		"07EA35ECA0A63EC2BA53C20884D171FA838007FCF8083F9A2CCB11E0E7CAB673",
		"211E4E9C990EC4C1F518716134E4564DDA76729170937BAE1B597D915C8EB45C",
		"5537FD941C0493BBCAF16E7257B9D9BF13B1E9AD6E9598E11EBC0BAF285BB78B",
		"5794C9BDA09DD9DBC9E59E537785EA2CC53AD1EEFD62C6CE5C48B6E1075E0245",
		"13929EEECB0BAE2857F9F2FA5694CBF02EAB581831601B9431D789D78C149EA8",
		"86BC49DAC0CD3084C910530466B65945BC29C251F41EE8096DD96246B8037304",
		"FCB6015A732063C836E9389A1A0578B706BF28E374701F707442CCA54E397191",
		"1234CC78D08BEA667273E5A65630BF64216B959E07FBF9AD2226F32979ABC494",
		"B09FC8C12A1A46030C231B90315FA2C25853BCAE43DC19E8292122EAA627A8E1",
		"3DFB0B91082D1642B58135C5A231426C1D6CF3DF4483272264F04C613B2503E3",
		"14183CF3C1E09921193C2AD1E061F67BA706E21AFF8A3F8D02B10910A3484C01",
		"8B1AA982C10A74F1447E9CD80633009D50376FC61B0712D7E54AB709E697DF7F",
		"4A7CF38B5F8AE037E2EDFCC23197C2CAE5D297512CD1F8C28891759CC10B5EBC",
		"E66DA8E757E2CB8B5CA4B3F514B9FEA43F69B9CA4C8029E1EBBE06BBF34C132E",
		"4E4665B9C8A80AF8DF1FC3AC2AD433A2DD2CAE6E6F115EE9563F74015F0629F8",
		"D61E349A8B8205FCBFF0D28B870E75D333F8CE5334B2973A0BEC7CFD97E4CB1A",
		"2808DEFADE80C9C92FE9138D6D4B07FD136E7130D76B3B65E9F484719DF60281",
		"E9E57F312008057C0E8F6F407B80C05D3C12883317F532B7037B11FA811C2FA3",
		"1ABE4DEE748CB6C645B273BE87AC15C90A4C0F3F95D08B21A6FE7260D65CD4C3",
		"CC06C272203C42B5C031FA4C13BF18859499F5E6E9E496F96A277FFF57D00DD4",
		"139FC2520ED8F427D130AC473760EF47E806C0E40EF56C9E2251FE164120152B",
		"918A76F20691147358EFE7A79E44EF45E35E8D93F5F687C0ABE4BAA402E5A91F",
		"4B2E0BEC294E7EF6E7E2B0E31541AE36C89E8BF432BDE05B32A21F8326315BDF",
		"43319305FCF257A8F95DE40A24D7C9FFC787C471E905BA518463723F00146253",
		"A30C5FB82BF8DFBE8D2FF295FCAF49A8E27F49420DFF180963F85612C6F10F60",
		"98C2DF47925ABE868409FCDD249CCB6F20E17C7E0D0975BC838406FE7612C6F3",
		"8B4868E22D6F8BF706F428B2FBAD42AD959F4BB2B2792D0973F938122E25528D",
		"57029424CCE72912AEE6078F60B896EF23E17A9E276129DF434237DF4DC20AFE",
		"2809ABF90324A4793F09B7F07B48B895DF75446461371EB5F1BFC6DCDD7814C3",
		"FF8244C965A0ED248A3781F84612C55FA8E16B49947C91F16E124525C3AF27D1",
		"2731BA8A449FC6E81212C515C36792E8F3183590B6E71787E683CEE722B8E817",
		"1F02A991D16A4BF8D687C4EFECA88E907B040AA92C4B81850D52E1F93F6361AD",
		"B28D94552BA12CB9899439F91358563C32DD8750DFE6A842745448A745A2F3EF",
		"844E8E2654A8C1C2ED5261AB5818132A8CC1C21D52E126B1302E541887858F4B",
		"8576B1303E54188F854F48857AB1501B2AD462E19352E114B1630952C712B063",
		"4F4B1DBB7298746CF4F74F9D7ABF5D7AF0FC156CD6B028BCF9B44FA67E419110",
		"B88067030705C807EFB235821475CAD43B150935E7B87676BFB6F8E0288F1C5F",
		"1CE225BC4BFBC89E91C685A5CA980550E9C6D412197343542F3DBD9DD7A46685",
		"31D741E6C4D41239A3012F94DDA788EA658E73F815A41DD7FB8FF5B07E59C7A5",
		"7DC4C28D6B0D035E80CCE7C000543A75691FFE2214FDCC460E0A90FE11C8ED44",
		"FA6F02FDFF62DBC3022DC3FC6427A0625D827C8161FC156E9AE3DF07B78736FD",
		"86509FD83A2155CDAC862EE1AEC6CBC0A39FD03AD5748CB1B52E93B516C82066",
		"4137119C453E8CF9645D710777001C213CF79977F1DFE99BB86541AE2008581A",
		"D3D78CAAFD8CA6FD0B0DA4C24C5F93EB8BD7FBD7B1DD427107F0B09F9BC2A94D",
		"87A01BD04ABCE91CF3114626379EC60DB1F1AFF86939C61C844CFCA652BCFB89",
		"4A506F7DEFA2456A4E7E1A8D94649FADD7AE171617B81FAF44DB24D62F1E1429",
		"BDE9581E881DB9641ED5158E5938506D186107C3C46B8960F336FD15D51EDE7C",
		"209B2D73CACBF49899384629BD5A8F733A455B44EF37335AF5CFFC3EC59B3856",
		"7681891362F71283CB5BDFDD2CA89BF0F08E1CCAC667721DAC5F8E570FEB4EC0",
		"BEE4E2B97E2FF38EA39C72F24F9C25FB1B6ADC40ACB41FF3255160B80F902FA9",
		"02A901666A417918FFE743E496531FACE8BBBA6C3DD86881C44BB96CB875E8F1",
		"448189B193AFBA9231808D0CD5380AF8754882B39D0A547245F1968F98086E7A",
		"0BF48AAB8F6B1620918E5D74F0CBC6E337BD0605F22E0DBF2738033D2E5F9E66",
		"0534652FE0EF198F7E4E1CE1E42F5EB4CA62469B677BC4AF540D6083CF1FC406",
		"BB7CAE03FCB67760BB383CE621E7210E2E4FF3C94D60BBD52BB94B7AFF57BFBB",
		"F481FC2D6E0876C38BCFB5E608FA83A98B85CDE174387B26ECD231AE7D8DE40B",
		"FEF8F5C9096461AA420BF3822F47105766B9C9FD3CCAD1F33029683D1F403932",
		"391CB8DF122972E39D05E8D2819028251FF8AE281D9044C95C14B3ED7A2E2F86",
		"B3696E33C5D14AABE52DB0C70EA2993EAF5D668B693B1012B553E85E81ADF38E",
		"4C12B52174D4046FFD3BDEA203DEA2C3E622EDB0BC691D0E717C41DE8A12C6C8",
		"9B56923730388E5A2EAA9BD0AF94BE72E953A44802A73C260ADC7EE97832707D",
		"48C882E3A0D193689411397B87638EA291140F75EDFCCE40C8C8127DB92EDB01",
		"A40B52D52BB28322A70D899CBAE933287438C46272E84AC60C97C23BE87E7451",
		"E801462BE85BCC45F1C029AEB5FA78F5232F113B371EA4F7DB55CFE01608BD09",
		"23BDC9947A937A74B8373D637BB340EA4DCFE8DEF4307150D7CEBFFB45C8C0A4",
		"AD5DB6C364E9802DF7ED36618A67391CC021A370380211226388E8207D2D253D",
		"32171DA623D1103CDC0944CD450730D5CBBFDE4924FDCC1A62E876C36C16ED37",
		"1729611E5BC42B396326154F2E3E1F47BEAFE8C79586EE3BDEB4B2D8B4EAA673",
		"642C7D7931B0DE3438CFBC308EAC38AFAD1B7AF00E91087B88F1FD92089C4211",
		"F835EAACA2A0345AAAB7C5D10A7AEBF73A1D05FC7C62BAAA029AD0A05D0EC741",
		"D30B386660CFCF119853FC6FF0EA0D3A8D742224A1D6C0E7A456976D3FB978C4",
		"9C2A2012D03D9A0134963B405F0C046E1447CD673B85763374D4CF678D93F4C5",
		"A58FAE1FB8F4115A987BCC45FBE939057CCB9B78CF679A5867B41E09F5CB5CD4",
		"8DE3DBCD172326D7E973BDC81B8E4A9AE4FFEA79EA76C7F79FA73EBD929CDD3D",
		"E910CF53B518EE82BC530094E3479EA722F1BF7F9E8A583F7C9E2AD16021A806",
		"7003AC07580D6007C805B03AFE51E7A9643404ECF43300BB00F63BFE37CE53E7",
		"AE1A3E4FEDCEFFC1F3D4F9F9644E1079471AC4D703B4E4FF9DF354C4FE71E7A9",
		"84EE7FEB3C55A27D0A78E805380BD00F10045040910A60E6AA7FD479AA384626",
		"F8B402D8014A57FD83CE53C55323723B147F50227DD967D6D798654E9F6DD02E",
		"C476E3E263827A6650887D075F8DF589DF1BC7F02809F196BD107B18F16CFD7A",
		"5B9FBECF9B4C0E0E6047C410C9C116EB8E8375E8AEAE865D762158D7D2ADE0DE",
		"B6C3A007F5072DEDF44C2FE81EE93D9DF825F2334A72B58168A8C039E036979E",
		"F2C652A0D0D5DC0DAEF2135594E6F577F04C27E68D2D55A1DC6ED8AD5FFF23E4",
		"B6E4F5885F2E693E85B775570C7FB36432F95A8994CD9FEE4563CAF714A6B80E",
		"3B7FEC53F48CFDE3D9C1B4DA1B30C3FF29BEC616D1593E51DE5D2E275F3271F0",
		"0F921309883C85757CD93A95A46E9F3A28D976054EBE11E8BF815F27817833C4",
		"DD878141277F2F441D4EBE060228A880407815D977F2EB1169F88B264EFE2662",
		"17A695CBF9E4FF128431F6123927D8257002583549E4C0AAF526501FA6FDF4B9",
		"F95969D3A61D6C38AD0D6B57643DD8332669EAA18F4CF3C70BEAD7FD6B860560",
		"A9C35950AE58CD9F3E00DDEDC763DC6EBCFE2B1EE3D29BB9A2533EDB2B30D13D",
		"38D1797BDAF054D057275C7A7FCED0BCCB5CDF3254E9EC6717D8CE89EA972EC8",
		"2F5A86BC79831E2B7EA7652DEC43AFE1C52491A8C00487C96A395BAFCC166C2E",
		"E2E9BF0ED8BE0489ECF6DA8274384C3AD89B83356877A6593A6BE45E2658AE80",
		"DD6E90D4C6BB538401BC181A24DF01BF01BF64ECCBEB5F61B587B04EE1976E5C",
		"CF93034267016FDF0B1DDB526A49273F23318E3F009B073708AEAB8ABD7FAE4C",
		"BD3B62DB84722518B5AE3072781F88E296E7AA772BB2D81EF2A5DB9C40B77AF7",
		"841CF6F22DDC31B0412FCF631498D6FBD9CBBAAD51EC65F3FD6098EA0272B4F3",
		"2E0BB40A2FA429B21025F017F6CA2DEB8A99B04BDDD21D2A22FCE511E49B64FD",
		"76B74283BBA9B2D3768512625F1185947CD1360A8B792BBECFE5F282FCDD2009",
		"C456F5E32A0A1E18594568321E86BD9DB3C57071DE22BFB77E3F2C178B4DA56E",
		"AA21E37274859D4BC6ABDC78744B6BD9FAA332669ACF7694180F98154DB2C649",
		"7BBBB97E3F3D8DBD2AD00AEE62007FA087FFD35F9189BE1C2F7300B7ED0380C1",
		"4C2B28704806A072C4023D60B944AF866919B13C1FEE1497C3296FD11EB4290B",
		"1CFC0B1F8F383B53434608FD0DF90D9793C470033A1F33339CE0EE7CF45FC4DD",
		"E986345A47AF1073702F3142A179FF580ED00465490FE2686D41816452E22F04",
		"A944BB0DFABA91B381057538508C9D985200CF6804AE0F3AB0C8592EE35DA461",
		"C004CF270990ECA3D12C5D78EA79987F7B2F5A94F587031AB6DE2FD05160F77B",
		"657887648FB97E2F3DCEC1FF6E2F71B50E83C1C2E5F5F2CF8105CADFD705860B",
		"DE2AE46CF805F3C6CB14B9FFA6DC1CC9DD96ABFE83222B90EFE48EB65F511670",
		"7520845F0D646F9635AC2BA6D5E5612DB66F5A56053B6D43D4EA40548B2DA8F7",
		"77DABEA1AC3004F7C408F7287C791356D8F1FBD9C0FF477D38C8F1204D61073B",
		"D47FB04D9863D3882E13791BB6147CFC037872CE44E2258C4BA7F7816D73E934",
		"D78DA5974A1362E462ECB4FCC41A28FA5E7F6DFC413A0F3DAB7AD51FB25EBE63",
		"B716F79E1BA37A5BC7D137814E9A239E1798829E08D3A9CD61619F990621D60D",
		"B14ED37E663C9E531FD31F047515F947FC8EA627ECD20763FDBAD0E3044662C6",
		"FB99B9C8341E56E88F5FBA20317A81EBE33A805D0D7780EBC4507E2CB018E2F8",
		"0D6EBCB60238C7CE1C3B79EC8B63DDE30F32FD972E82F9099D038CEC04CDA5CF",
		"E4C73C132C80B759E9515B00B73672D93C28B83E3F21467F12624A88A92E7D78",
		"A99BFD90E20E7127C03F85768AC70E886FA1B0A6237485A0348112EFC838B8EE",
		"F61E7CEB289DF3D0B1AD51E08F9A3EA0DF6E3A48AF900E7F02E3BB0BB88EF64F",
		"34CEF1DD5CD41BE4D5403B38C06FC1CE7F99BD20E03B82B7F59DEC3E45E3697C",
		"49C0C4DF40DE159CC040C6281BD1E36A60F617AF5BA3F777700B83A66E3AA5C9",
		"4F9F379DA26F6AEAA53F6FE73590150B59A7202B02B2DE09FCBC789DC8F3C221",
		"A807C1376210C440B8E1E9F5DFFE75EE6BCFB5E7FFBE27D74351770054033400",
		"3C0CF034C02B0007004E01F0008300CA1A8A9A04A0055800900A9003B01AC00D",
		"500BD002F034C06E80A3003C40184D5171004900568065006B01F087E99B009E",
		"047805E075804300EF017C06D00FA061282A1E20096021C04A8062806A800700",
		"9E00F82DC09F000E01F402F40344D5525402404AADD84FD472EC244AF338C0A2",
		"2A8F6D4B196DF7546D70D5007F9B6A3654795CAE05A51515040FF17362C47518",
		"4A3BA7C00E2EC54B219E535193037528081D2EDA5952C190F8E251F1AC0AF20B",
		"F81475B7CB53E9AAD0279106A8117E303C3D756C3BCFC58DA4E7CEA434F3678E",
		"A42B205E0DE0612AE9B24D2EADCBE3A9F268292A3A8AA2F2972E7738C41C4C52",
		"8EDB962D1E95A4B297E765DDB66C2467658A2EC91C1D758B96FC970D25D5D515",
		"651B4AE8B2AACAD07FDB80FF670353535679979676BBB4251BE8B25A97B6D6E5",
		"A9419CAA8D2437AF6C83A7AAA66A23AD5DB0CC96AF5D29F21565AF7095D4B8B4",
		"1BAA2A69A827D61F69E0A61A6D0D535D5DE5811257C926ED46E8C326187F6D59",
		"254437119C058469E4D1843C3295257756B8B47415E094D165251565F7BAB46E",
		"5749B5D837C433221EF2EDAAAC62EE726B6BAA4B36B808ED8AAACD65A32A12FA",
		"23F5527EB05E0D5DFA37EA2563BD6A06D8AE2DF3D04C4985762353B9411AC08A",
		"8A61FE0D3F487F5D55A50BA4F0D6121A032D8D5D1CA69F681EDBEFAA6A57250E",
		"684D15A44B5DB5651B46E14A63E4DA52EDDA40BB4AC9C88C9E7BC4317E0B6713",
		"534197D16E8FABA4144668C3DDDFC6FFE171912AE1FF0301E8D151DF11207749",
		"8DD6E3BA8771D5604338FB92606047689767535925F4590B7D2EABD496809455",
		"323538809B4BEA16FC23654767FEC13EB82A61D6AA2A37B92A69C0465CD30FE2",
		"9678EE6210B166986E12E26EACA882E6607954579555D2A42AE494BA4AC5311C",
		"591ACE32D2BB45F3E60D8FC3D2B23B3D259E3AC48BC2B52865DB700A664545D9",
		"C5FF4025159636B560C1022A2DF43FAA5496001653797765D5E6CA8CB17A84BD",
		"71AC1EF9C54D625AFBAD3F373557FAFB76C9F7FDE592FF00E587FF9ABEE76F2C",
		"46A36CE4EFC73EA3EB3F201BF90BFB917FA1BA387EB230B9223C629C32326ABC",
		"2A7A825A3371D2E498D82953E3AE9B767DFCF41933B5B366CF49B8E1C69BE6DE",
		"FC9379F36F5970AB2E31496F484E319ACCA996B4F48C4C6BD6C245D9B69CC5B9",
		"B72DB97D69DEB2E5F6152B1DF9AB9C05AB0BEF58B3B6A878DDFA923B3794BA36",
		"DEE52E2BBFBB62536555F53D9E1A9AA9DDBCA5EEDEFBB6D66FBB7F783E3C378F",
		"9D9F940594E6B66165B6C80331D01B0E1751215995A58EEAB2CA45552018A3F6",
		"8B4DA994E6BED4B174A22C9406361D692F2B28AB2CADDAECA0C942A0207F558D",
		"CBB3FCCE72207BDBC80AC9827A50B6B4A486CE227ADD5E55CD54933C312D92A1",
		"F28062C95DAE85555BB22806088DDEC1B0FD461B3875B6B1FCA8168F4D77403A",
		"373775D3A654DC6961EF8467BE360F1E2D46EAE0A1F2F26E2D2DBDB58EAC077B",
		"1E45650164BB36B836DDE9F260DEB2AADAE1F8F20D7415461DAE6A3A9499C5DC",
		"C5D4C0402D612A088D25A0E730CCAAF69421BB79259E0D6EDCD35D777A1869DD",
		"2D29A924516807E9235DA489B4900ED2807A754803EB635DAC43394A68C6535A",
		"4268E478CA482CDFCD786AA4BC025769A54B4AE433622CAFAA92643818292CA1",
		"B12ED6437CC4431C2CA7FED99FEFEAA0B9FFDB1A6BD49FEC47FE8DD158A3FEC2",
		"7EE4DF688DF5CFFE7C774EFFB9F6ACFFE9DFDFEB3FFEC550316352A3CB7E7497",
		"A57E8F4DFFD8FE8EF4FBDB3978B15BDC3F26E6519AD8BC91FDA46E05A5A95F21",
		"FA61B5CB29CDBDCBC578B61DF61880D1765396F64E66E34697470B7B87077D19",
		"B44AEF7481155DEAA2451378B3BB6C839BE483FBE761AA43D6AA646DDD541305",
		"861DB86E60BCD5C08EEA5AA00567C9F5EDFFDEAEA664A3ABA28E18AC65950C38",
		"655B5C1B1862079754A2995D434781B986FF0BDEB0E95BBA200A7CD931DC8598",
		"9A155A0559DA1AA00366419DE4E681CB25597ED032ECC67FAB43513FD021ED8F",
		"EF50D4DFE890F6FB3A04CF2A89BF61CE37969455A08B34AA77385FD73B29CD4C",
		"00D8EF60DB83DD0F3641D80B614BC49D91ECCDB0DDC2AE0B9B2FECC1B015C38E",
		"0C1B33ECCFB04DC36E8D9BB6F420BDA1424A137687282763FE6FBCFFE68347FA",
		"DA7BC49B24A1A76536D824D6EFE2AEBF67249E83670CEBBF9FE63FCBF98A5206",
		"D6E3A2AA4D9B4048969655BAB2A80C92C3783CE03BE5139FF1B6528AFA18739D",
		"E231820DECCA97A8D147314FC8245B36ABB4D443CE6636634E5E552953E1CA05",
		"D21540985A2ECF0F49DEF0194EEAA8D64299F9F2FCE15398EBE50ED1E2254E16",
		"4595C9168F49AF425C72A25380B1E1331D27A6864F755461B9E05B13342A82C4",
		"45F25391BAC89F64B4FF27D277D0A562268C29A673CA2A5CF975D52EEA25B1B4",
		"C44333D568986751F782255A01CBE45B9E00C58CF41F6B2F030F1046E04B0A79",
		"B08D78B10EDA03BE684D169587F8DF2DA0CEFD408D02EA21794159A96B91BBC4",
		"935F9587A7020BEB6817B5FCFBE914C0CC937E678377EFA9AAA39424B508A617",
		"2A51B572A7781A428668BD7C55A59BF4BFD4B6650318ECD021E8048D46FBE3F2",
		"02E829E913B5386CA9ABA4F63B5DA71EA16CA8AFBE93FF35F15416D9F1DB8210",
		"5B6ECBC378107317D97138A99A101FD2EC6B08972B5D62F2BAB01FF4BCA84361",
		"2BE98A55959BC103A266021EB48FC723A407D097BB5CD47D21DA2B1817FA12B9",
		"614BC1EF975C7A989B49A42D07D0A6A80A22655540DF254E33F57B71E6713051",
		"1220E7EEB0E151CFAF0A4D05F5EA583C18F7D4B0A58BF28030C9837A96D16928",
		"FF451861C8EEF21047AF7283288A30D8EF22ADFCB20D774BB269F9EE5A81A5E9",
		"272DD6D5805B955F06525643A41562545DA8C7808C8A9CA25E1BC125E37DBB6D",
		"E532DBD2B1279C234FE8FF5C7D0C74056EEE216881F476801DA843407BFF9AE8",
		"92514A84905BB7A4A4B6645DC9864DEBA48D6A5DF672C728A47577C110436835",
		"FDF7B4FDB5E7DA73EDB9F65C7BAE3DD79E6BCF3FEF53BF8A1ABE73927E13A5C9",
		"5C40FDE01D947531E2990B3E0D517F9B2E1209A728FF78293D1DE20A29FEF351",
		"71FC3D7BA5147F7854FC9151F11DA3E28F8E8A3F362AFED35171BCE0330E600B",
		"00B2893EBDCE4569B05C0121FE9C657729A5C1326BA9F843933110E27F6BDFBB",
		"81D24C82D00FE164081F8710BFF4BF16C2EB203440380DC2E09D94E67A08F743",
		"180FE19310421FA9C1124A83EDF210E27B806E08EF85F00084F8BBE2FBF14774",
		"207CA5441C6799C47328A4BE95FE7BF93F54FEDFC5FFA1F2FF279F4552F86378",
		"0AF915A31FFCA6E5E8F48E724AB3E741310F7FCDF9C7D25EF4F751FE579EFFB7",
		"F0210B5390A586BF61BDFE81BA07420797CF7D2C866DCF8A61E3BFF5A0C853D6",
		"6D0D247C1EF0DB777EB853FB03741B87C4D02A85AFFE48FC2C29DC0DF86FFFBA",
		"F7D7D3A84F7FCDFF7ACD0FD3DFD646622B4041ADF8B0F8432DB5EEC3F20F93BE",
		"8B7FA2FBA39ECB1495B84DACA90DFFE975A3CBF18E3D862843532851CF20C8A5",
		"10751CEA0EFC6512FC3512FC6114D43DA89BF0B7F295521829855152D9F85165",
		"53A4BA18AA25BA1A290FF5D52409307F96949F2CA5B3A4F422296D97EAAC94F2",
		"1D525820D12F94DAAE90F237619F01AA0150C779245E2324FC0689E746297C40",
		"2A67A5F226890EFEFAF4CD00BF92D26D123FCF48EDFDBB94FF0789BF2352F9FB",
		"5279DC38712C426B777B19A5690168026800D80AB0058006A8002805580B6007",
		"C8064803D001CC07980B9000A005880788038801D000A80094000A000A60D04D",
		"69CE02F4029C728BF8EF407818603FC01E805D00CF003C01F038C00E8026806A",
		"80528048C5888E41FDF35805A55900F12DA09BECDBFF3EDC2F3DA8CBBE4F772F",
		"A046E837548BF1BE0BE2FB77BB239FFA9F3CF6ECFF59FD1EE059DB3272770E9F",
		"6FC767406801B88312F7EB5F00BC0A80DF30B90830113AA4035806B049365227",
		"9512E5753345EC090AEFA79F00F80A00070A7F92334F86A770FFA32E5C7BAE3D",
		"D79E6BCFB5E7DAF3FFBF07ACA952D81FC3752E5DAD6E9BEE21DDBFE8FE5DF7B2",
		"EE635DAFEE9CEEFAC494C48CC4ECC4AD890F24FE34717FE247895F27BA939E4B",
		"7A35E958D207491F27F149E793E47ABDDEA65FA2AFD46FD1DFAFFF37FD9FF46F",
		"EA0FEB8FEB3FD47FAA3FABEFD75FD18F37680CD71BB4865443A3C16BF899E15F",
		"0D2F1AF61AF6190E1902860B86A02132599B3C3FF997C9FB926F4CB925C593F2",
		"60CAE329BF4C7939E58F296FA61C4A399ED29DF271CAA7296753BE4AB9924219",
		"C71955C689C629C6EB8DB38C37187F62BCD5986C4C37AE35DE69DC64AC353E6A",
		"DC693C6FFCDA1866BAD16431D59B58D323A6F74D7F310D99AE332F33AF326F34",
		"3F6EFE95B9C37CD23C25353B7565EA33A9BF4D7D2DF583D4ABA9919638CB32CB",
		"1ACB6396A72CCF5AFE60D9678949FB224D9EBE283D3F7D6DFA8674777A757A57",
		"FA91F453E99FA50BE9E333A666CCC85892F164C6EF33CE650C6544654ECC5C98",
		"B92C73556673E6C399BB32FF90D999F997CC4F32FB3283996840E38F84CB752A",
		"DD645DAA2E4767D7ADD6DDA973EBF6E9F489A6C49589AB139549C9494FE87F0F",
		"637754FF895E658833DC6E586FB8DFF0A8E129C37F183A0D1F187A0CB2644DF2",
		"ECE48C645BF2F2E4BAE4E6642EF977C9AF261F49EE49FE2CF97CF250F2CF5376",
		"A7F8530EA79C480933C6C2F8688DF38D06E3EDC64263B1D16DDC6BFCD0F88DF1",
		"27268769BBE9B0E91DD349D317A6CB26A579AA79817991F93FCDAF99FDE637CD",
		"87CC7F3107CC17CC85A9AED4FAD4C753DB52F7C2E87C9EDA9FAAB0CCB0FCC492",
		"665962D968612DFF6AF9BDE5354BBBA5DF12917663DAFCB4BC3447DA9D696569",
		"D56975693F4F7B21ED3FD3FE98E64F7B2BEDABB409E9F1E937A6EBD3D3D397A4",
		"3B6024DDE9BF4A7F2EFD77E91DE97F491F488FC898949195B13263634645464D",
		"4643C64319BE8C9F65FC6BC67319BFCBA07414C583887EA9BBAA93255E9FA84D",
		"BC1546CB96684FBC3391496C487C30914B7C34F15789AF25B6271E4E3C96F841",
		"E2C7896712CF272A92C6274D4C9A997443D24F92744919494B92DC497549F727",
		"ED48BA98F430C8D6DB295AE31A90844A73A339227572EAF4D49B526F4D4D4ECD",
		"48B5A52E4975A4AE4EE5527F95FA62EAABA97CEA9554CA926D999B6E4D5F9CBE",
		"1C246135F4A039FD85F483E947D3DF49EF0669E849EF4DE7D3CFA6A764A46558",
		"33B2337281F33F66BC937132A327A33783CF88CCD467BA322B32AB33E9CC2D99",
		"5B331B329B325B328F6776679ECAECC9ECCD840E5A29AA1782345DBDEE331D95",
		"1899F81CF4E944E2ED492B92D62555243D96F49BA45D49BB93FE9CF44ED2A749",
		"6793AE244DD0CFD61BF42BF56BF425FA7BF4B5FA06FD43FA47F43FD33FA5DFA9",
		"FFADFE15FD6BFA0EFD01587F5FEBA30D330C46C35DC93B927F91FC52726FF2B4",
		"94969487C97AFA3A45674C315A61E5DC657CC9F8AAB1C25C6B6E31FFD4FC8CF9",
		"25F351581F5F9A07CD51A9B1303649B05296A796A4568044B4A45AD272D30AD2",
		"8AD3DC30D7F7A535A5FD2EED789A3DFDCEF4A6F447D3DF4B673376643C95F19B",
		"8C97335ECBE8CC389F3103564507AC04743CF107B5E374DB752F412F2FEA6C89",
		"9D89D393B6C0ACFC22E9DF929E497A3EE9C5A47793AE265DA7D7EA13F473F5F3",
		"F53AE8A3499FA67F50BF5DBF43FFB8FE09FD903ED290609867D0190C0693A1D0",
		"C01A5E054DE237EC371C301C361C35BC63E836CC4E9E0BDA44976C486E029DF2",
		"1CAC91DDA0598E2677279F49BE987C35795E4A724A56CAED291B53AA5276A5F4",
		"805E9962FCA5F105E3CBC60BC65B4D06D32AD326539F69BCF956B3196464BF39",
		"27F52EE8F54BA93BD2F18E4F36F4E1ACAE4F17973837D10032B823F1C9C4A713",
		"5F4CDC9578005A701BB7185F34EE32FA8D6926ABC96E5A6F7A32E3E98C173376",
		"65ECC9D89BB13FE340063AD2314083D66DD1EDD71DD0A9123589E844A3EFA2D0",
		"6974313A9DEEACAE5F17D4291255893189F1890989F3A1A534D0C24B13F313D7",
		"2696265624D2A0917B92FA92FA938249BB52F6A4EC8575BF3FE500ACFDA329EF",
		"80BE3C05FDEA4DE1A16F7D29FD29832941D09B0AA31234A7C618638C33C6836E",
		"4830CE05FDA0030D6132A6811C641B738D4B8D7663BE7135C8C47A63B591366E",
		"3536189B8C2DC6EDC61DC6C78D4F189F343E0DFD5B6F2ABDF6AEFDDA73EDB9F6",
		"5C7BAE3DD79E6BCFB5E7FFA3CFFF01504B07086CDD77F6A744000000A0000050",
		"4B010214001400080008001C4205336CDD77F6A744000000A000000A00000000",
		"0000000000000000000000000047434D444C4E2E444C4C504B05060000000001",
		"00010038000000DF4400000000"
	};
	private static final int UNCOMPRESSED_SIZE = 0xA000;

}
