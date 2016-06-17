package shortcuts.views.dialog;


import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;

import shortcuts.Shortcut;
import shortcuts.ShortcutsUtil;

public class AddDialog extends TrayDialog {

	public AddDialog(Shell shell) {
		super(shell);
	}

	private Text nameText;
	private Text locationText;
	private Combo priorityCombo;

	protected Control createDialogArea(Composite parent) {
		Composite comp = (Composite) super.createDialogArea(parent);
		getShell().setText("Add New Location");

		comp.setLayout(new GridLayout(4, false));

		Label lblNewLabel = new Label(comp, SWT.NONE);
		lblNewLabel.setText("Name:");

		nameText = new Text(comp, SWT.BORDER);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 150;
		nameText.setLayoutData(gd_text);

		Label lblNewLabel_2 = new Label(comp, SWT.NONE);
		lblNewLabel_2.setText("Priority:");

		priorityCombo = new Combo(comp, SWT.READ_ONLY);
		priorityCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		priorityCombo.add("High");
		priorityCombo.add("Medium");
		priorityCombo.add("Low");

		priorityCombo.select(1);

		Label lblNewLabel_1 = new Label(comp, SWT.NONE);
		lblNewLabel_1.setText("Location:");

		locationText = new Text(comp, SWT.BORDER);
		locationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 2, 1));

		Button btnNewButton = new Button(comp, SWT.NONE);
		btnNewButton.setText(" Browse ");
		btnNewButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				locationText.setText(handleBrowse());
			}
		});
		
		setHelpAvailable(false);

		return comp;
	}

	private String handleBrowse() {
		FileDialog dialog = new FileDialog(getShell());

		return dialog.open();
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == 0) {
			if (locationText.getText().trim().equals("")) {
				MessageDialog.openInformation(getShell(), "Missing Location",
						"Location should not be empty.");
				return;
			}

			Shortcut shortcut = new Shortcut();
			String name = nameText.getText();

			if (name.equals("")) {
				name = locationText.getText().substring(
						locationText.getText().lastIndexOf("\\") + 1);
			}

			shortcut.setName(name);
			shortcut.setLocation(locationText.getText());
			shortcut.setPriority(priorityCombo.getText());

			ShortcutsUtil.saveShortcut(shortcut);
			close();
		} else {
			close();
		}
	}
}
