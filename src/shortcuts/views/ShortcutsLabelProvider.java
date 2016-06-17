package shortcuts.views;

import java.io.File;
import java.text.DateFormat;


import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;

import shortcuts.Activator;
import shortcuts.Shortcut;
import shortcuts.ShortcutsUtil;

class ShortcutsLabelProvider extends LabelProvider implements ITableLabelProvider {
	static ImageRegistry imageRegistry;

	public String getColumnText(Object obj, int index) {
		String columnText = "";
		Shortcut file = (Shortcut) obj;
		if (index == 1) { // Name
			columnText = file.getName();
		} else if (index == 2) { // Priority
			columnText = file.getPriority();
		} else if (index == 3) { // Location
			columnText = file.getLocation();
		} else if (index == 4) { // Size
			File f = new File(file.getLocation());
			columnText = f.exists() ? Long.toString(f.length()) : "";
		} else if (index == 5) { // Last Modified
			File f = new File(file.getLocation());
			DateFormat dataformat = DateFormat.getDateInstance(DateFormat.LONG);
			columnText = f.exists() ? dataformat.format(f.lastModified()) : "";
		}
		return columnText;
	}

	public Image getColumnImage(Object obj, int index) {
		if (index == 1) {
			return getImage(obj);
		}
		return null;
	}

	public Image getImage(Object obj) {
		Shortcut shortcut = (Shortcut) obj;
		String location = shortcut.getLocation();
		int dotPosition = shortcut.getLocation().lastIndexOf(".");
		String extension = dotPosition == -1 ? "" : location.substring(dotPosition);
		return getIcon(extension);
	}

	private static Image getIcon(String extension) {
		if (imageRegistry == null)
			imageRegistry = new ImageRegistry();
		Image image = imageRegistry.get(extension);
		if (image != null)
			return image;

		Program program = Program.findProgram(extension);
		ImageData imageData = (program == null ? null : program.getImageData());
		if (imageData != null) {
			image = new Image(Display.getCurrent(), imageData);
			imageRegistry.put(extension, image);
		} else {
			image = Activator.getDefault()
					.getImageDescriptor(ShortcutsUtil.ICON_SHORTCUT)
					.createImage();
		}

		return image;
	}
}