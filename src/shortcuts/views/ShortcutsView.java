package shortcuts.views;

import java.util.Iterator;


import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.jface.action.*;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import shortcuts.Activator;
import shortcuts.Shortcut;
import shortcuts.ShortcutsUtil;
import shortcuts.views.dialog.AddDialog;

@SuppressWarnings("deprecation")
public class ShortcutsView extends ViewPart {
	private Table table;
	private TableViewer tableViewer;
	private ShortcutsFilter filter;

	private Action addShortcut;
	private Action removeShortcut;
	private Action doubleClickAction;

	private final String NAME_COLUMN = "Name";
	private final String PRIORITY_COLUMN = "Priority";
	private final String LOCATION_COLUMN = "Location";
	private final String SIZE_COLUMN = "Size (bytes)";
	private final String LAST_MODIFIED_COLUMN = "Last Modified";
	
	public FontData FONT_ARIAL_8 = new FontData("Arial", 8, SWT.NONE);

	private String[] columnNames = new String[] { NAME_COLUMN, PRIORITY_COLUMN,
			SIZE_COLUMN, LOCATION_COLUMN, LAST_MODIFIED_COLUMN };

	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);

		Label searchLabel = new Label(parent, SWT.NONE);
		searchLabel.setImage(Activator.getDefault()
				.getImageDescriptor(ShortcutsUtil.ICON_SEARCHFILE).createImage());

		final Text searchText = new Text(parent, SWT.SEARCH);
		searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));
		searchText.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
				filter.setSearchText(searchText.getText());
				tableViewer.refresh();
			}

			public void keyPressed(KeyEvent e) {
			}
		});

		createTable(parent);
		createTableViewer();

		tableViewer.setInput(getViewSite());

		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		updateTitle();
		packColumns();
	}

	private void packColumns() {
		table.getColumn(1).pack();
		table.getColumn(2).pack();
		//table.getColumn(3).pack();
		table.getColumn(4).pack();
		table.getColumn(5).pack();
	}

	public TableViewer getTableViewer() {
		return tableViewer;
	}

	private void createTableViewer() {
		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		tableViewer.setColumnProperties(columnNames);
		tableViewer.setContentProvider(new ShortcutsContentProvider());
		tableViewer.setLabelProvider(new ShortcutsLabelProvider());
		tableViewer.setSorter(new ShortcutsSorter());
		
		filter = new ShortcutsFilter();
		tableViewer.addFilter(filter);
	}

	private void createTable(Composite parent) {
		int style = SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER;

		GridLayout layout = new GridLayout(1, true);

		table = new Table(parent, style);
		table.setLayout(layout);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 3;
		table.setLayoutData(gridData);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn column = new TableColumn(table, SWT.CENTER, 0);
		column = new TableColumn(table, SWT.LEFT, 1);
		column.setText(NAME_COLUMN);

		column = new TableColumn(table, SWT.LEFT, 2);
		column.setText(PRIORITY_COLUMN);
		table.setSortColumn(column);

		column = new TableColumn(table, SWT.LEFT, 3);
		column.setText(LOCATION_COLUMN);
		column.setWidth(320);

		column = new TableColumn(table, SWT.LEFT, 4);
		column.setText(SIZE_COLUMN);

		column = new TableColumn(table, SWT.LEFT, 5);
		column.setText(LAST_MODIFIED_COLUMN);

		table.setFont(new Font(table.getDisplay(), FONT_ARIAL_8));
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		Menu menu = menuMgr.createContextMenu(tableViewer.getControl());
		tableViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, tableViewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(addShortcut);
		manager.add(removeShortcut);
	}

	private void makeActions() {
		addShortcut = new Action() {
			public void run() {
				AddDialog add = new AddDialog(tableViewer.getControl()
						.getShell());
				add.open();

				tableViewer.refresh();
				packColumns();
				updateTitle();
			}
		};
		addShortcut.setToolTipText("Add new shortcut");
		addShortcut.setImageDescriptor(Activator.getDefault().getImageDescriptor(
				ShortcutsUtil.ICON_ADDFILE));

		removeShortcut = new Action() {
			@SuppressWarnings("unchecked")
			public void run() {
				ISelection selection = tableViewer.getSelection();
				Iterator<Shortcut> itr = ((IStructuredSelection) selection)
						.iterator();

				while (itr.hasNext()) {
					ShortcutsUtil.removeShortcut(itr.next());
				}

				tableViewer.refresh();
				updateTitle();
			}
		};
		removeShortcut.setToolTipText("Remove shortcut");
		removeShortcut.setImageDescriptor(Activator.getDefault()
				.getImageDescriptor(ShortcutsUtil.ICON_REMOVEFILE));

		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = tableViewer.getSelection();
				Shortcut shortcut = (Shortcut) ((IStructuredSelection) selection)
						.getFirstElement();

				ShortcutsUtil.launchShortcut(shortcut);
			}
		};
	}

	private void hookDoubleClickAction() {
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	public void setFocus() {
		tableViewer.getControl().setFocus();
	}

	private void updateTitle() {
		setTitle("Contains "
				+ String.valueOf(tableViewer.getTable().getItemCount())
				+ " shortcuts");
	}
}