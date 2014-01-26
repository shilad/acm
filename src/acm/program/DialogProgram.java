/*
 * @(#)DialogProgram.java   1.0 06/08/25
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

import acm.io.IOModel;

/* Class: DialogProgram() */

/**
 * This class is a standard subclass of <code><a href="Program.html">Program</a></code>
 * that takes its input from a <code>IODialog</code> object.
 */
public abstract class DialogProgram extends Program {

/* Constructor: DialogProgram() */
/**
 * Creates a new dialog program.
 *
 * @usage DialogProgram program = new DialogProgram();
 */
	public DialogProgram() {
		/* Empty */
	}

/* Method: run() */
/**
 * Contains the code to be executed for each specific program subclass.  If
 * you are defining your own <code>DialogProgram</code> class, you need to
 * override the definition of <code>run</code> so that it contains the code
 * for your application.
 */
	public void run() {
		/* Empty */
	}


/* Method: getInputModel() */
/**
 * Returns the <code>IOModel</code> used for program input.
 *
 * @usage IOModel io = program.getInputModel();
 * @return The <code>IOModel</code> used for program input
 */
	public IOModel getInputModel() {
		return getDialog();
	}

/* Method: getOutputModel() */
/**
 * Returns the <code>IOModel</code> used for program output.
 *
 * @usage IOModel io = program.getOutputModel();
 * @return The <code>IOModel</code> used for program output
 */
	public IOModel getOutputModel() {
		return getDialog();
	}

/* Inherited method: print(value) */
/**
 * @inherited Program#void print(String value)
 * Displays the argument value, allowing for the possibility of more
 * output in the same dialog.
 */

/* Inherited method: println() */
/**
 * @inherited Program#void println()
 * Completes the output line and displays the dialog.
 */

/* Inherited method: println(value) */
/**
 * @inherited Program#void println(String value)
 * Adds the value to the current output line and then displays the dialog.
 */

/* Inherited method: readLine() */
/**
 * @inherited Program#String readLine()
 * Puts up a dialog box asking the user for a line of text.
 */

/* Inherited method: readLine(prompt) */
/**
 * @inherited Program#String readLine(String prompt)
 * Puts up a dialog box asking the user for a line of text.
 */

/* Inherited method: readInt() */
/**
 * @inherited Program#int readInt()
 * Puts up a dialog box asking the user for an integer.
 */

/* Inherited method: readInt(prompt) */
/**
 * @inherited Program#int readInt(String prompt)
 * Puts up a dialog box asking the user for an integer.
 */

/* Inherited method: readDouble() */
/**
 * @inherited Program#double readDouble()
 * Puts up a dialog box asking the user for a double-precision number.
 */

/* Inherited method: readDouble(prompt) */
/**
 * @inherited Program#double readDouble(String prompt)
 * Puts up a dialog box asking the user for a double-precision number.
 */

/* Inherited method: readBoolean() */
/**
 * @inherited Program#boolean readBoolean()
 * Puts up a dialog box asking the user for a <code>true</code>/<code>false</code> value.
 */

/* Inherited method: readBoolean(prompt) */
/**
 * @inherited Program#boolean readBoolean(String prompt)
 * Puts up a dialog box asking the user for a <code>true</code>/<code>false</code> value.
 */

/* Inherited method: readBoolean(prompt) */
/**
 * @inherited Program#boolean readBoolean(String prompt, trueLabel, falseLabel)
 * Puts up a dialog box asking the user for a boolean value chosen from
 * buttons with the specified labels.
 */

/* Inherited method: getConsole() */
/**
 * @inherited Program#IOConsole getConsole()
 * Returns the console associated with this program.
 */

/* Inherited method: getDialog() */
/**
 * @inherited Program#IODialog getDialog()
 * Returns the dialog used for user interaction.
 */

/* Inherited method: getReader() */
/**
 * @inherited Program#BufferedReader getReader()
 * Returns a <code>BufferedReader</code> whose input comes from the console.
 */

/* Inherited method: getWriter() */
/**
 * @inherited Program#PrintWriter getWriter()
 * Returns a <code>PrintWriter</code> whose output is directed to the console.
 */

/* Inherited method: setTitle(title) */
/**
 * @inherited Program#void setTitle(String title)
 * Sets the title of this program.
 */

/* Inherited method: getTitle() */
/**
 * @inherited Program#String getTitle()
 * Gets the title of this program.
 */

/* Inherited method: pause(milliseconds) */
/**
 * @inherited Program#void pause(double milliseconds)
 * Delays the calling thread for the specified time.
 */
}
