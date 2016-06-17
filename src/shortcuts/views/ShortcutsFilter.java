package shortcuts.views;


import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import shortcuts.Shortcut;

public class ShortcutsFilter extends ViewerFilter {
	private String searchString;

	public void setSearchText(String s) {
		this.searchString = s.toLowerCase(); // If you use matches, then change
												// this to "*."+s+".*"
	}

	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (searchString == null || searchString.length() == 0) {
			return true;
		}
		Shortcut shortcut = (Shortcut) element;

		// We can use matches() instead of contains().
		if (shortcut.getName().toLowerCase().contains(searchString)) {
			return true;
		}
		if (shortcut.getLocation().toLowerCase().contains(searchString)) {
			return true;
		}

		return false;
	}
}
