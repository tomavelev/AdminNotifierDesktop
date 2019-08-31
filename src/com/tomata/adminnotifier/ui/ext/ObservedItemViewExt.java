package com.tomata.adminnotifier.ui.ext;

import com.tomata.adminnotifier.ui.ObservedItemView;
import java.awt.Component;
import javax.swing.table.TableColumn;

import javax.swing.JPanel;

import com.tomata.adminnotifier.model.ObservedItem;

public class ObservedItemViewExt extends ObservedItemView {

	public ObservedItemViewExt() {
		super();
		removeIdColumns();
		
		next.getParent().getParent().remove(next.getParent());
	}

	protected void removeIdColumns() {
		TableColumn column = null;
		column = jTable.getColumnModel().getColumn(0);
		jTable.getColumnModel().removeColumn(column);
		

		column = jTable.getColumnModel().getColumn(1);
		jTable.getColumnModel().removeColumn(column);
		column = jTable.getColumnModel().getColumn(1);
		jTable.getColumnModel().removeColumn(column);


		column = jTable.getColumnModel().getColumn(2);
		jTable.getColumnModel().removeColumn(column);
		column = jTable.getColumnModel().getColumn(2);
		jTable.getColumnModel().removeColumn(column);
	}

	@Override
	protected Component observedItemAddEditView(JPanel p, ObservedItem observedItem) {
		return new ObservedItemAddEditViewExt(p, observedItem);
	}

	@Override
	protected int deleteColumn() {
		return 4;
	}

	public void reload() {
		load();
	}

}
