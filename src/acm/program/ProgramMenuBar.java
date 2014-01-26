/*
 * @(#)ProgramMenuBar.java   1.0 06/08/25
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

import acm.io.IOConsole;
import acm.util.ErrorException;
import acm.util.Platform;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/* Class: ProgramMenuBar */

/**
 * This class standardizes the menu bars used in the ACM program package.
 */
public class ProgramMenuBar extends JMenuBar {

/* Constructor: ProgramMenuBar() */
/**
 * Creates a <code>ProgramMenuBar</code> with no menus.
 *
 * @usage ProgramMenuBar mbar = new ProgramMenuBar();
 */
	public ProgramMenuBar() {
		listener = new ProgramMenuListener(this);
		init();
	}

/* Method: init() */
/**
 * Initializes the menu bar.  Subclasses that wish to change the composition
 * of the menu bar beyond the default <code>File</code> and <code>Edit</code>
 * menus should override this method with one that adds the desired menus.
 *
 * @usage init();
 */
	public void init() {
		addFileMenu();
		addEditMenu();
	}

/* Method: addFileMenu() */
/**
 * Installs the <code>File</code> menu.
 *
 * @usage mbar.addFileMenu();
 */
	public void addFileMenu() {
		if (patchModeInEffect("File")) return;
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		addFileMenuItems(fileMenu);
		add(fileMenu);
	}

/* Method: addEditMenu() */
/**
 * Installs the <code>Edit</code> menu.
 *
 * @usage mbar.addEditMenu();
 */
	public void addEditMenu() {
		if (patchModeInEffect("Edit")) return;
		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic('E');
		addEditMenuItems(editMenu);
		add(editMenu);
	}

/* Method: addFileMenuItems(menu) */
/**
 * Adds the standard <code>File</code> items to the specified menu.  Subclasses
 * can override this method to change the list of items.
 *
 * @usage mbar.addFileMenuItems(menu);
 * @param menu The menu to which the <code>File</code> items are added
 */
	public void addFileMenuItems(JMenu menu) {
		menu.add(createStandardMenuItem("Save"));
		menu.add(createStandardMenuItem("Save As"));
		menu.addSeparator();
		menu.add(createStandardMenuItem("Print"));
		menu.add(createStandardMenuItem("Print Console"));
		menu.add(createStandardMenuItem("Script"));
		menu.addSeparator();
		menu.add(createStandardMenuItem("Quit"));
	}

/* Method: addEditMenuItems(menu) */
/**
 * Adds the standard <code>Edit</code> items to the specified menu.  Subclasses
 * can override this method to change the list of items.
 *
 * @usage mbar.addEditMenuItems(menu);
 * @param menu The menu to which the <code>Edit</code> items are added
 */
	public void addEditMenuItems(JMenu menu) {
		menu.add(createStandardMenuItem("Cut"));
		menu.add(createStandardMenuItem("Copy"));
		menu.add(createStandardMenuItem("Paste"));
		menu.add(createStandardMenuItem("Select All"));
	}

/* Method: createStandardMenuItem(action) */
/**
 * Creates one of the standard menu items implemented by the <code>ProgramMenuBar</code>.
 * The menu item is identified by its action command, which must be one of the following:
 * <code>Copy</code>, <code>Cut</code>, <code>Paste</code>, <code>Print</code>,
 * <code>Print Console</code>, <code>Quit</code>, <code>Save</code>, <code>Save As</code>,
 * <code>Script</code>, <code>Select All</code>.
 *
 * @usage JMenuItem item = mbar.addStandardItem(action);
 * @param action The action command identifying the menu to be added
 */
	public JMenuItem createStandardMenuItem(String action) {
		JMenuItem item = null;
		if (action.equals("Quit")) {
			if (Platform.isMac()) {
				item = createStandardItem("Quit", 'Q');
			} else {
				item = createStandardItem("Exit", 0);
			}
		} else if (action.equals("Cut")) {
			item = createStandardItem((Platform.isMac()) ? "Cut" : "Cut (x)", 'X');
		} else if (action.equals("Copy")) {
			item = createStandardItem((Platform.isMac()) ? "Copy" : "Copy (c)", 'C');
		} else if (action.equals("Paste")) {
			item = createStandardItem((Platform.isMac()) ? "Paste" : "Paste (v)", 'V');
		} else if (action.equals("Save")) {
			item = createStandardItem("Save", 'S');
		} else if (action.equals("Save As")) {
			item = createStandardItem("Save As...", 0);
		} else if (action.equals("Print")) {
			item = createStandardItem("Print...", 'P');
		} else if (action.equals("Print Console")) {
			item = createStandardItem("Print Console", 0);
		} else if (action.equals("Script")) {
			item = createStandardItem("Script...", 0);
		} else if (action.equals("Select All")) {
			item = createStandardItem("Select All", 'A');
		} else {
			throw new ErrorException("Illegal menu item: " + action);
		}
		item.setActionCommand(action);
		item.addActionListener(listener);
		return item;
	}

/* Method: getProgram() */
/**
 * Returns the program associated with this menu bar.
 *
 * @usage Program program = mbar.getProgram();
 * @return The <code>Program</code> object that controls this menu bar
 */
	public Program getProgram() {
		return activeProgram;
	}

/* Method: setConsole(console) */
/**
 * Sets the console associated with the menu bar.  Each console calls this method
 * whenever it acquires the keyboard focus, which allows the menu bar to work with
 * multiple consoles.
 *
 * @usage mbar.setConsole(console);
 * @param console The <code>IOConsole</code> object to be used with the menu bar
 */
	public void setConsole(IOConsole console) {
		activeConsole = console;
	}

/* Method: getConsole() */
/**
 * Returns the console associated with this menu bar.
 *
 * @usage IOConsole console = mbar.getConsole();
 * @return The <code>IOConsole</code> object currently associated with the menu bar
 */
	public IOConsole getConsole() {
		return activeConsole;
	}

/* Method: setEnabled(action, flag) */
/**
 * Enables or disables the menu item that generates the specified action
 * command.
 *
 * @usage mbar.setEnabled(action, flag);
 * @param action The action command triggered by the menu item
 * @param flag <code>true</code> to enable the item, <code>false</code> to disable it
 */
	public void setEnabled(String action, boolean flag) {
		int nMenus = getMenuCount();
		for (int i = 0; i < nMenus; i++) {
			setEnabled(getMenu(i), action, flag);
		}
	}

/* Method: createOldStyleMenuBar */
/**
 * Creates a <code>MenuBar</code> that has the same effect as the
 * specified <code>JMenuBar</code>.
 *
 * @usage MenuBar oldMenuBar = mbar.createOldStyleMenuBar();
 * @return A <code>MenuBar</code> whose actions are paired with the original
 */
	public MenuBar createOldStyleMenuBar() {
		MenuBar mbar = new MenuBar();
		int nMenus = getMenuCount();
		for (int i = 0; i < nMenus; i++) {
			mbar.add(createOldStyleMenu(getMenu(i)));
		}
		return mbar;
	}

/* Package private method: setProgram(program) */
/**
 * Sets the program associated with this menu bar.  This method is called
 * automatically by the <code>Program</code> class after creating the menu bar.
 *
 * @usage mbar.setProgram(program);
 * @param program The <code>Program</code> object that controls this menu bar
 */
	void setProgram(Program program) {
		activeProgram = program;
	}

/* Private method: createStandardItem(label, key) */
/**
 * Creates a menu item from the specified label and keystroke accelerator.
 */
	private JMenuItem createStandardItem(String label, int key) {
		JMenuItem item = new JMenuItem(label);
		if (key != 0) {
			if (Platform.isMac()) {
				item.setAccelerator(KeyStroke.getKeyStroke((char) key, KeyEvent.CTRL_MASK));
			} else {
				item.setMnemonic(key);
			}
		}
		return item;
	}

/* Private method: patchModeInEffect(menu) */
/**
 * This method is required only if the patches to support 1.1 operation are in effect.
 * In those environments, the <code>JMenu</code> class doesn't exist, and each package
 * has its own definition.  It is therefore essential to call the initializers in the
 * package from the menu bar, if a local definition of <code>JMenu</code> exists.
 */
	private boolean patchModeInEffect(String menuName) {
		try {
			String className = getClass().getName();
			String prefix = className.substring(0, className.lastIndexOf('.') + 1);
			Class jMenuClass = Class.forName(prefix + "JMenu");
			Class[] types1 = { Class.forName("java.lang.String") };
			Object[] args1 = { menuName };
			Constructor newJMenu = jMenuClass.getConstructor(types1);
			JMenu menu = (JMenu) newJMenu.newInstance(args1);
			menu.setMnemonic(menuName.charAt(0));
			Class[] types2 = { Class.forName("acm.program.ProgramMenuBar"), Class.forName("java.lang.String") };
			Object[] args2 = { this,  "add" + menuName + "MenuItems" };
			Method addViaCallback = jMenuClass.getMethod("addViaCallback", types2);
			addViaCallback.invoke(menu, args2);
			add(menu);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

/* Private method: createOldStyleMenu */
/**
 * Creates a <code>Menu</code> that has the same effect as the
 * specified <code>JMenu</code>.
 */
	private Menu createOldStyleMenu(JMenu jmenu) {
		Menu menu = new Menu(jmenu.getText());
		int nItems = jmenu.getItemCount();
		for (int i = 0; i < nItems; i++) {
			menu.add(createOldStyleMenuItem((JMenuItem) jmenu.getItem(i)));
		}
		return menu;
	}

/* Private method: createOldStyleMenuItem */
/**
 * Creates a <code>MenuItem</code> that has the same effect as the
 * specified <code>JMenuItem</code>.
 */
	private MenuItem createOldStyleMenuItem(Object jitem) {
		if (jitem == null) {
			return new MenuItem("-");
		} else if (jitem instanceof JMenu) {
			return createOldStyleMenu((JMenu) jitem);
		} else if (jitem instanceof JCheckBoxMenuItem) {
			return new OldStyleCheckBoxMenuItem((JCheckBoxMenuItem) jitem);
		} else if (jitem instanceof JMenuItem) {
			return new OldStyleMenuItem((JMenuItem) jitem);
		}
		throw new ErrorException("Unsupported menu item type");
	}

/* Private method: setEnabled(menu, action, flag) */
/**
 * Updates the enabled state of everything in the menu that has the specified action.
 */
	private void setEnabled(JMenu item, String action, boolean flag) {
		JMenu menu = (JMenu) item;
		int nItems = menu.getItemCount();
		for (int i = 0; i < nItems; i++) {
			JMenuItem subItem = (JMenuItem) menu.getItem(i);
			if (subItem != null) setEnabled(subItem, action, flag);
		}
	}

/* Private method: setEnabled(item, action, flag) */
/**
 * Updates the enabled state of the menu item if it has the specified action.
 */
	private void setEnabled(JMenuItem item, String action, boolean flag) {
		if (action.equals(item.getActionCommand())) item.setEnabled(flag);
	}

/* Private instance variables */

	private Program activeProgram;
	private ProgramMenuListener listener;
	private IOConsole activeConsole;
}

/* Package class: ProgramMenuListener */

/**
 * This class implements the listener for the standard menu items that
 * forwards their action back to the program.
 */
class ProgramMenuListener implements ActionListener {

/* Constructor: ProgramMenuListener(mbar) */
/**
 * Creates a new listener for the standard menu items that will be added to this
 * menu bar.
 */
	public ProgramMenuListener(ProgramMenuBar mbar) {
		menuBar = mbar;
	}

/* Method: actionPerformed(e) */
/**
 * Responds to an action event in the corresponding menu.   The effect of an
 * action event is to forward the action command back to the program.
 */
	public void actionPerformed(ActionEvent e) {
		menuBar.getProgram().menuAction(e.getActionCommand());
	}

/* Private instance variables */

	private ProgramMenuBar menuBar;

}

/* Package class: OldStyleMenuItem */

/**
 * This class represents a standard Macintosh <code>MenuItem</code> that listens to
 * a <code>JMenuItem</code> and tracks its changes.
 */
class OldStyleMenuItem extends MenuItem implements ActionListener, ChangeListener {

/* Constructor: OldStyleMenuItem(jitem) */
/**
 * Creates a new <code>MenuItem</code> that tracks the changes in the specified
 * <code>JMenuItem</code>.
 */
	public OldStyleMenuItem(JMenuItem jitem) {
		super(jitem.getText());
		twin = jitem;
		addActionListener(this);
		twin.addChangeListener(this);
		setEnabled(twin.isEnabled());
		KeyStroke accelerator = twin.getAccelerator();
		if (accelerator != null) setShortcut(createShortcut(accelerator));
	}

/* Method: actionPerformed(e) */
/**
 * Responds to an action event in the Mac menu and forwards it along to
 * the actual <code>JMenuItem</code> that the client has created.
 */
	public void actionPerformed(ActionEvent e) {
		if (e != e) /* Avoid the unused parameter warning */;
		twin.doClick(0);
	}

/* Method: stateChanged(e) */
/**
 * Monitors the state of the <code>JMenuItem</code> and replicates changes
 * in the enabled state.
 */
	public void stateChanged(ChangeEvent e) {
		setEnabled(twin.isEnabled());
	}

/* Private method: createShortcut(accelerator) */
/**
 * Creates an old-style menu shortcut from the new-style accelerator.
 */
	private MenuShortcut createShortcut(KeyStroke accelerator) {
		boolean isShifted = (accelerator.getModifiers() & Event.SHIFT_MASK) != 0;
		return new MenuShortcut(accelerator.getKeyCode(), isShifted);
	}

/* Private instance variables */

	private JMenuItem twin;
}

/* Package class: OldStyleCheckBoxMenuItem */

/**
 * This class represents a standard Macintosh <code>CheckBoxMenuItem</code> that
 * listens to a <code>JCheckBoxMenuItem</code> and tracks its changes.
 */
class OldStyleCheckBoxMenuItem extends CheckboxMenuItem implements ActionListener, ChangeListener {

/* Constructor: OldStyleCheckBoxMenuItem(jitem) */
/**
 * Creates a new <code>CheckBoxMenuItem</code> that tracks the changes in the specified
 * <code>JCheckBoxMenuItem</code>.
 */
	public OldStyleCheckBoxMenuItem(JCheckBoxMenuItem jitem) {
		super(jitem.getText());
		twin = jitem;
		addActionListener(this);
		twin.addChangeListener(this);
		setState(twin.getState());
		setEnabled(twin.isEnabled());
		KeyStroke accelerator = twin.getAccelerator();
		if (accelerator != null) setShortcut(createShortcut(accelerator));
	}

/* Method: actionPerformed(e) */
/**
 * Responds to an action event in the Mac menu and forwards it along to
 * the actual <code>JMenuItem</code> that the client has created.
 */
	public void actionPerformed(ActionEvent e) {
		if (e != e) /* Avoid the unused parameter warning */;
		twin.doClick(0);
	}

/* Method: stateChanged(e) */
/**
 * Monitors the state of the <code>JMenuItem</code> and replicates changes
 * in the enabled state.
 */
	public void stateChanged(ChangeEvent e) {
		setState(twin.getState());
		setEnabled(twin.isEnabled());
	}

/* Private method: createShortcut(accelerator) */
/**
 * Creates an old-style menu shortcut from the new-style accelerator.
 */
	private MenuShortcut createShortcut(KeyStroke accelerator) {
		boolean isShifted = (accelerator.getModifiers() & Event.SHIFT_MASK) != 0;
		return new MenuShortcut(accelerator.getKeyCode(), isShifted);
	}

/* Private instance variables */

	private JCheckBoxMenuItem twin;
}
