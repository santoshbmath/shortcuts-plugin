package shortcuts;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShortcutsUtil {
	private static String ROOT_TAG = "shortcuts-data";
	private static String SHORTCUTS_TAG = "shortcuts";
	private static String SHORTCUT_TAG = "shortcut";
	private static String NAME_TAG = "name";
	private static String LOCATION_TAG = "location";
	private static String PRIORITY_TAG = "priority";

	public static String ICON_ADDFILE = "icons/add.png";
	public static String ICON_REMOVEFILE = "icons/Actions-edit-clear-icon.png";
	public static String ICON_SEARCHFILE = "icons/Actions-page-zoom-icon.png";
	public static String ICON_SHORTCUT = "icons/sc.png";

	public static void saveShortcut(Shortcut shortcut) {
		List<Shortcut> shortcuts = getShortcuts();
		shortcuts.add(shortcut);
		getStore().setValue(ROOT_TAG, convertToString(shortcuts, SHORTCUTS_TAG));
	}

	public static List<Shortcut> getShortcuts() {
		final List<Shortcut> data = new ArrayList<Shortcut>();
		final String stringData = getStore().getString(ROOT_TAG);

		if (stringData.length() == 0) {
			return data;
		}

		try {
			XMLMemento rootMemento = XMLMemento.createReadRoot(new StringReader(stringData));
			IMemento mementos[] = rootMemento.getChildren(SHORTCUT_TAG);
			for (int i = 0; i < mementos.length; i++) {
				IMemento memento = mementos[i];
				
				Shortcut shortcut = new Shortcut();
				shortcut.setName(memento.getString(NAME_TAG));
				shortcut.setLocation(memento.getString(LOCATION_TAG));
				shortcut.setPriority(memento.getString(PRIORITY_TAG));
				
				data.add(shortcut);
			}
		} catch (WorkbenchException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static void removeShortcut(Shortcut shortcut) {
		List<Shortcut> shortcuts = getShortcuts();
		shortcuts.remove(shortcut);
		getStore().setValue(ROOT_TAG, convertToString(shortcuts, SHORTCUTS_TAG));
	}

	private static IPreferenceStore getStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	private static String convertToString(List<Shortcut> shortcuts, String root) {
		if (shortcuts != null && shortcuts.size() > 0) {
			final XMLMemento rootMemento = XMLMemento.createWriteRoot(root);
			Iterator<Shortcut> itr = shortcuts.iterator();
			while (itr.hasNext()) {
				Shortcut shortcut = itr.next();
				
				IMemento memento = rootMemento.createChild(SHORTCUT_TAG);
				memento.putString(NAME_TAG, shortcut.getName());
				memento.putString(LOCATION_TAG, shortcut.getLocation());
				memento.putString(PRIORITY_TAG, shortcut.getPriority());
			}

			StringWriter writer = new StringWriter();
			try {
				rootMemento.save(writer);
				return writer.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public static void launchShortcut(Shortcut shortcut) {
		try {
			@SuppressWarnings("unused")
			Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + shortcut.getLocation());
			// p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
