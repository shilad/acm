/*
 * @(#)JTFTools.java   1.0 06/08/25
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

package acm.util;

import java.applet.Applet;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;

/* Class: JTFTools */

/**
 * This class provides a small collection of static utility methods
 * that are used elsewhere in the ACM packages.
 */

public class JTFTools {

/* Static method: createEmptyContainer() */
/**
 * Returns an empty lightweight container.  Several packages need to create
 * such components as placeholders.  Defining it in JTFTools gives those
 * pacakges access to a common mechanism.
 *
 * @usage Container comp = JTFTools.createEmptyContainer();
 * @return An empty lightweight container that can be used as a placeholder
 */
	public static Container createEmptyContainer() {
		return new EmptyContainer();
	}

/* Static method: getEnclosingFrame(comp) */
/**
 * Returns the frame that encloses the specified component.
 *
 * @usage Frame frame = JTFTools.getEnclosingFrame(comp);
 * @param comp The component at which to start the search
 * @return The nearest enclosing <code>Frame</code> object
 */
	public static Frame getEnclosingFrame(Component comp) {
		while (comp != null && !(comp instanceof Frame)) {
			comp = comp.getParent();
		}
		return (Frame) comp;
	}

/* Static method: getStandardFont(font) */
/**
 * Returns a font that will approximate the specified font in this environment.
 * This method is required because some browsers do not support the standard
 * fonts <code>Serif</code>, <code>SansSerif</code>, and <code>Monospaced</code>.
 *
 * @usage Font newFont = JTFTools.getStandardFont(font);
 * @param font The font being checked
 * @return The new font
 */
	public static Font getStandardFont(Font font) {
		if (!fontFamilyTableInitialized) initFontFamilyTable();
		if (font == null || fontFamilyTable == null) return font;
		String family = font.getFamily();
		if (fontFamilyTable.get(trimFamilyName(family)) != null) return font;
		if (family.equals("Serif") || family.equals("Times")) {
			family = getFirstAvailableFontSubstitution(SERIF_SUBSTITUTIONS);
		} else if (family.equals("SansSerif")) {
			family = getFirstAvailableFontSubstitution(SANSSERIF_SUBSTITUTIONS);
		} else if (family.equals("Monospaced")) {
			family = getFirstAvailableFontSubstitution(MONOSPACED_SUBSTITUTIONS);
		} else {
			return font;
		}
		if (family == null) return font;
		return new Font(family, font.getStyle(), font.getSize());
	}

/* Static method: decodeFont(str) */
/**
 * Decodes a font in the style of <code>Font.decode</code>.
 *
 * @usage Font font = JTFTools.decodeFont(str);
 * @param str The string to decode
 * @return The new font
 */
	public static Font decodeFont(String str) {
		return decodeFont(str, null);
	}

/* Static method: decodeFont(str, oldFont) */
/**
 * Decodes a font in the style of <code>Font.decode</code>.  The only difference
 * is that this method takes a font parameter that gives default values for
 * the different parts of the font.  If the family, size, or style is specified
 * as an asterisk, the corresponding value is taken from the supplied font.
 *
 * @usage Font font = JTFTools.decodeFont(str, oldFont);
 * @param str The string to decode
 * @param oldFont The font whose properties are used as defaults
 * @return The new font
 */
	public static Font decodeFont(String str, Font oldFont) {
		String familyName = str;
		int fontStyle = Font.PLAIN;
		int fontSize = 12;
		int hyphen = str.indexOf('-');
		if (hyphen >= 0) {
			familyName = str.substring(0, hyphen);
			str = str.substring(hyphen + 1);
			hyphen = str.indexOf('-');
			if (hyphen >= 0) {
				String token = str.substring(0, hyphen).toLowerCase();
				if (token.equals("*") && oldFont != null) {
					fontStyle = oldFont.getStyle();
				} else if (token.equals("plain")) {
					fontStyle = Font.PLAIN;
				} else if (token.equals("bold")) {
					fontStyle = Font.BOLD;
				} else if (token.equals("italic")) {
					fontStyle = Font.ITALIC;
				} else if (token.equals("bolditalic")) {
					fontStyle = Font.BOLD | Font.ITALIC;
				} else {
					throw new ErrorException("Illegal font style");
				}
				str = str.substring(hyphen + 1);
			}
			if (str.equals("*") && oldFont != null) {
				fontSize = oldFont.getSize();
			} else {
				try {
					fontSize = Integer.valueOf(str).intValue();
				} catch (NumberFormatException ex) {
					throw new ErrorException("Illegal font size");
				}
			}
		}
		if (familyName.equals("*")) {
			familyName = (oldFont == null) ? "Default" : oldFont.getName();
		} else {
			if (!fontFamilyTableInitialized) initFontFamilyTable();
			if (fontFamilyTable != null) {
				familyName = (String) fontFamilyTable.get(trimFamilyName(familyName));
				if (familyName == null) familyName = "Default";
			}
		}
		return getStandardFont(new Font(familyName, fontStyle, fontSize));
	}

/* Static method: decodeColor(name) */
/**
 * Decodes a color name.  This method is similar to <code>Color.decode</code>
 * except in that it allows named colors and system colors.
 *
 * @usage color = JTFTools.decodeColor(name);
 * @param name The string name of the color
 * @return The color corresponding to the specified name
 */
	public static Color decodeColor(String name) {
		if (name.equalsIgnoreCase("desktop")) return SystemColor.desktop;
		if (name.equalsIgnoreCase("activeCaption")) return SystemColor.activeCaption;
		if (name.equalsIgnoreCase("activeCaptionText")) return SystemColor.activeCaptionText;
		if (name.equalsIgnoreCase("activeCaptionBorder")) return SystemColor.activeCaptionBorder;
		if (name.equalsIgnoreCase("inactiveCaption")) return SystemColor.inactiveCaption;
		if (name.equalsIgnoreCase("inactiveCaptionText")) return SystemColor.inactiveCaptionText;
		if (name.equalsIgnoreCase("inactiveCaptionBorder")) return SystemColor.inactiveCaptionBorder;
		if (name.equalsIgnoreCase("window")) return SystemColor.window;
		if (name.equalsIgnoreCase("windowBorder")) return SystemColor.windowBorder;
		if (name.equalsIgnoreCase("windowText")) return SystemColor.windowText;
		if (name.equalsIgnoreCase("menu")) return SystemColor.menu;
		if (name.equalsIgnoreCase("menuText")) return SystemColor.menuText;
		if (name.equalsIgnoreCase("text")) return SystemColor.text;
		if (name.equalsIgnoreCase("textText")) return SystemColor.textText;
		if (name.equalsIgnoreCase("textHighlight")) return SystemColor.textHighlight;
		if (name.equalsIgnoreCase("textHighlightText")) return SystemColor.textHighlightText;
		if (name.equalsIgnoreCase("textInactiveText")) return SystemColor.textInactiveText;
		if (name.equalsIgnoreCase("control")) return SystemColor.control;
		if (name.equalsIgnoreCase("controlText")) return SystemColor.controlText;
		if (name.equalsIgnoreCase("controlHighlight")) return SystemColor.controlHighlight;
		if (name.equalsIgnoreCase("controlLtHighlight")) return SystemColor.controlLtHighlight;
		if (name.equalsIgnoreCase("controlShadow")) return SystemColor.controlShadow;
		if (name.equalsIgnoreCase("controlDkShadow")) return SystemColor.controlDkShadow;
		if (name.equalsIgnoreCase("scrollbar")) return SystemColor.scrollbar;
		if (name.equalsIgnoreCase("info")) return SystemColor.info;
		if (name.equalsIgnoreCase("infoText")) return SystemColor.infoText;
		if (name.equalsIgnoreCase("black")) return Color.black;
		if (name.equalsIgnoreCase("blue")) return Color.blue;
		if (name.equalsIgnoreCase("cyan")) return Color.cyan;
		if (name.equalsIgnoreCase("darkGray")) return Color.darkGray;
		if (name.equalsIgnoreCase("DARK_GRAY")) return Color.darkGray;
		if (name.equalsIgnoreCase("gray")) return Color.gray;
		if (name.equalsIgnoreCase("green")) return Color.green;
		if (name.equalsIgnoreCase("lightGray")) return Color.lightGray;
		if (name.equalsIgnoreCase("LIGHT_GRAY")) return Color.lightGray;
		if (name.equalsIgnoreCase("magenta")) return Color.magenta;
		if (name.equalsIgnoreCase("orange")) return Color.orange;
		if (name.equalsIgnoreCase("pink")) return Color.pink;
		if (name.equalsIgnoreCase("red")) return Color.red;
		if (name.equalsIgnoreCase("white")) return Color.white;
		if (name.equalsIgnoreCase("yellow")) return Color.yellow;
		try {
			return Color.decode(name);
		} catch (NumberFormatException ex) {
			throw new ErrorException("Illegal color value");
		}
	}

/* Static method: registerApplet(applet) */
/**
 * Adds this applet to a table indexed by the current thread.
 *
 * @usage JTFTools.registerApplet(applet);
 * @param applet The applet being registered
 */
	public static void registerApplet(Applet applet) {
		registerApplet(applet, Thread.currentThread());
		mostRecentApplet = applet;
	}

/* Static method: registerApplet(applet, thread) */
/**
 * Adds this applet to a table indexed by the specified thread.
 *
 * @usage JTFTools.registerApplet(applet);
 * @param applet The applet being registered
 * @param thread The thread used as the key
 */
	public static void registerApplet(Applet applet, Thread thread) {
		appletTable.put(thread, applet);
	}

/* Static method: getApplet() */
/**
 * Returns the current applet.  This implementation may fail in
 * multithreaded programs.  Such programs should therefore not
 * rely on this facility.  It is appropriate to use this facility,
 * for example, during the <code>init</code> method of a program,
 * which is the only context in which it is applied in the JTF tools.
 *
 * @usage Applet applet JTFTools.getApplet();
 * @return The currently running applet
 */
	public static Applet getApplet() {
		Applet applet = (Applet) appletTable.get(Thread.currentThread());
		if (applet == null) applet = mostRecentApplet;
		return applet;
	}

/* Static method: pause(milliseconds) */
/**
 * Delays the calling thread for the specified time, which is expressed in
 * milliseconds.  Unlike <code>Thread.sleep</code>, this method never throws an
 * exception.
 *
 * @usage JTFTools.pause(milliseconds);
 * @param milliseconds The sleep time in milliseconds
 */
	public static void pause(double milliseconds) {
		Applet applet = (Applet) appletTable.get(Thread.currentThread());
		if (applet == null) {
			applet = mostRecentApplet;
			appletTable.put(Thread.currentThread(), applet);
		}
		try {
			int millis = (int) milliseconds;
			int nanos = (int) Math.round((milliseconds - millis) * 1000000);
			Thread.sleep(millis, nanos);
		} catch (InterruptedException ex) {
			/* Empty */
		}
	}

/* Static method: terminateAppletThreads(applet) */
/**
 * Terminates all of the threads that are registered as belonging to the
 * specified applet.
 *
 * @usage JTFTools.terminateAppletThreads(applet);
 * @param applet The applet whose threads are being terminated
 */
	public static void terminateAppletThreads(Applet applet) {
		try {
			Thread myThread = Thread.currentThread();
			Class threadClass = Class.forName("java.lang.Thread");
			Method stop = threadClass.getMethod("stop", new Class[0]);
			Enumeration e = appletTable.elements();
			while (e.hasMoreElements()) {
				Thread t = (Thread) e.nextElement();
				if (t != myThread && t.isAlive() && isAnonymous(t) && applet == (Applet) appletTable.get(t)) {
					stop.invoke(t, new Object[0]);
				}
			}
		} catch (Exception ex) {
			/* Empty */
		}
	}

/* Static method: isAnonymous(t) */
/**
 * Returns <code>true</code> if the supplied thread is an anonymous
 * one created automatically by the system.
 *
 * @usage if (JTFTools.isAnonymous(t)) . . .
 * @param t The thread being tested
 */
	public static boolean isAnonymous(Thread t) {
		String name = t.getName();
		if (!name.startsWith("Thread-")) return false;
		for (int i = 7; i < name.length(); i++) {
			if (!Character.isDigit(name.charAt(i))) return false;
		}
		return true;
	}

/* Private method: initFontFamilyTable() */
/**
 * Initializes the list of font families.
 */
	private static void initFontFamilyTable() {
		fontFamilyTableInitialized = true;
		try {
			Class toolkitClass = Class.forName("java.awt.Toolkit");
			Method getFontList = toolkitClass.getMethod("getFontList", new Class[0]);
			String[] fonts = (String []) getFontList.invoke(Toolkit.getDefaultToolkit(), new Object[0]);
			fontFamilyTable = new Hashtable();
			for (int i = 0; i < fonts.length; i++) {
				fontFamilyTable.put(trimFamilyName(fonts[i]), fonts[i]);
			}
			fontFamilyTable.put("serif", getFirstAvailableFontSubstitution(SERIF_SUBSTITUTIONS));
			fontFamilyTable.put("sansserif", getFirstAvailableFontSubstitution(SANSSERIF_SUBSTITUTIONS));
			fontFamilyTable.put("monospaced", getFirstAvailableFontSubstitution(MONOSPACED_SUBSTITUTIONS));
		} catch (Exception ex) {
			fontFamilyTable = null;
		}
	}

/* Private method: getFirstAvailableFontSubstitution(fontOptions) */
/**
 * Returns the first family in the array of font options that is
 * actually installed.
 */
	private static String getFirstAvailableFontSubstitution(String[] fontOptions) {
		for (int i = 0; i < fontOptions.length; i++) {
			if (fontFamilyTable.get(trimFamilyName(fontOptions[i])) != null) return fontOptions[i];
		}
		return null;
	}

/* Private method: trimFamilyName(family) */
/**
 * Creates a canonical family name by converting the string to
 * lower case and removing spaces and hyphens.
 */
	private static String trimFamilyName(String family) {
		String str = "";
		for (int i = 0; i < family.length(); i++) {
			char ch = family.charAt(i);
			if (ch != ' ' && ch != '-') str += Character.toLowerCase(ch);
		}
		return str;
	}

/* Static variables */

	private static boolean fontFamilyTableInitialized = false;
	private static Hashtable fontFamilyTable = null;
	private static Hashtable appletTable = new Hashtable();
	private static Applet mostRecentApplet = null;

/* Font substitutions */

	private static final String[] SERIF_SUBSTITUTIONS = {
		"Serif", "Times", "TimesRoman", "Times-Roman"
	};

	private static final String[] SANSSERIF_SUBSTITUTIONS = {
		"SansSerif", "Helvetica", "Arial"
	};

	private static final String[] MONOSPACED_SUBSTITUTIONS = {
		"Monospaced", "Courier", "Monaco"
	};

}

/* Package class: EmptyContainer */

/**
 * This class represents a simple lightweight container.  The only difference
 * between EmptyContainer and Container is that Container is abstract and
 * can therefore not be instantiated.
 */

class EmptyContainer extends Container {
	public void update(Graphics g) {
		paint(g);
	}
}
