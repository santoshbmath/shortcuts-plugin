package shortcuts.views;

import java.util.ArrayList;
import java.util.List;


import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import shortcuts.Shortcut;
import shortcuts.ShortcutsUtil;

public class ShortcutsContentProvider implements IStructuredContentProvider {
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object parent) {
		List<Shortcut> shortcuts = new ArrayList<Shortcut>();
		shortcuts = ShortcutsUtil.getShortcuts();

		return shortcuts.toArray();
	}
}