package shortcuts.views;


import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import shortcuts.Shortcut;

public class ShortcutsSorter extends ViewerSorter {
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		Shortcut shortcut1 = (Shortcut) e1;
		Shortcut shortcut2 = (Shortcut) e2;

		String priority1 = shortcut1.getPriority().toLowerCase();
		String priority2 = shortcut2.getPriority().toLowerCase();

		if (priority1.equals("high") && priority2.equals("medium"))
			return -1;
		else if (priority1.equals("high") && priority2.equals("low"))
			return -1;
		else if (priority1.equals("medium") && priority2.equals("low"))
			return -1;
		else if (priority1.equals("medium") && priority2.equals("high"))
			return 1;
		else if (priority1.equals("low") && priority2.equals("medium"))
			return 1;
		else if (priority1.equals("low") && priority2.equals("high"))
			return 1;
		else
			return 0;
	}
}
