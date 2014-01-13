package edu.oregonstate.cope.eclipse.ui.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class ProjectSelectionPage extends Dialog {

	private List<String> projects;

	public ProjectSelectionPage(Shell parentShell, List<String> projects) {
		super(parentShell);
		this.projects = projects;
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setSize(400, 600);
		Label label = new Label(composite, SWT.BORDER);	
		label.setText("Please select the project you would like us to record");
		
		Composite buttonsParent = new Composite(composite, SWT.NONE);
		buttonsParent.setLayout(new GridLayout(2, false));
		buttonsParent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Button selectAll = new Button(buttonsParent, SWT.NONE);
		selectAll.setText("Select all");
		Button selectNone = new Button(buttonsParent, SWT.NONE);
		selectNone.setText("Select none");
		
		Composite projectList = new Composite(composite, SWT.BORDER);
		projectList.setLayout(new GridLayout(1, true));
		projectList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Table table = new Table(projectList, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL);
		table.setLayout(new GridLayout(1,true));
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table.setHeaderVisible(false);
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setText("Project");
		
		List<TableItem> tableItems = new ArrayList<TableItem>();
		for (String project : projects) {
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(project);
			tableItem.setChecked(true);
			tableItems.add(tableItem);
		}
		
		tableColumn.pack();
		
		return super.createContents(parent);
	}
}
