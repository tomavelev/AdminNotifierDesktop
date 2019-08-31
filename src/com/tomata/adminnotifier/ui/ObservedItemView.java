package com.tomata.adminnotifier.ui;

import java.sql.Connection;
import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.util.concurrent.ExecutionException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.tomata.adminnotifier.i18n.I18n;
import com.tomata.adminnotifier.dao.DB;
import com.tomata.adminnotifier.dao.QueryResult;
import com.tomata.adminnotifier.dao.ObservedItemDao;
import com.tomata.adminnotifier.model.ObservedItem;
import com.tomata.adminnotifier.i18n.I18n;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.table.*;
import javax.swing.event.*;

public class ObservedItemView extends JPanel {

	public static final int PAGING = Integer.MAX_VALUE;
	protected DefaultTableModel tm;
	protected JTable jTable;
	protected long offset = 0;
	protected long total = 0;
	protected JButton first;
	protected JButton previous;
	protected JButton next;
	protected JButton last;
	protected JLabel pagingLabel;

	// fields
	public ObservedItemView() {

		initiUsualStuff();
		load();
	}

	private void initiUsualStuff() {
		tm = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tm.addColumn(I18n.s("id"));
		tm.addColumn(I18n.s("name"));
		tm.addColumn(I18n.s("url"));
		tm.addColumn(I18n.s("comparation_activation"));
		tm.addColumn(I18n.s("value"));
		tm.addColumn(I18n.s("change_after_meeting_condition"));
		tm.addColumn(I18n.s("active"));
		tm.addColumn(I18n.s("temp_value"));
		tm.addColumn(I18n.s("time_of_temp_value"));
		tm.addColumn(I18n.s("delete"));
		jTable = new JTable(tm);
		selectionListener = new ListSelectionListener() {
			
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
				jTable.getSelectionModel().setValueIsAdjusting(true);
				int selectedColumn = jTable.getSelectedColumn();
				int selectedRow = jTable.getSelectedRow();
				if (selectedColumn != -1 && selectedRow != -1) {
					
					if (selectedColumn == deleteColumn()) {
						delete((ObservedItem) result.getList().get(selectedRow));
					}
					else {
						addEdit((ObservedItem) result.getList().get(selectedRow));
					}
				} else {
					//DO nothing
				}
			}
			}
		};
		jTable.getSelectionModel().addListSelectionListener(selectionListener);
		jTable.setIntercellSpacing(new Dimension(5, 5));
		jTable.setRowHeight(30);
		jTable.getColumnModel().getColumn(9).setCellRenderer(new TableCellRenderer() {

			JButton btn = new JButton("");
			public Component getTableCellRendererComponent(JTable table, Object value,
	               boolean isSelected, boolean hasFocus, int row, int column) {
			    if (isSelected) {
			    	btn.setForeground(table.getSelectionForeground());
			    	btn.setBackground(table.getSelectionBackground());
			    } else{
			    	btn.setForeground(table.getForeground());
			    	btn.setBackground(UIManager.getColor("Button.background"));
			    }
			    btn.setText( (value == null) ? "" : value.toString() );
			    return btn;
			}});
		setLayout(new BorderLayout());
		add(new JScrollPane(jTable), BorderLayout.CENTER);
	

		JPanel south = new JPanel(new BorderLayout());
		JPanel paging = new JPanel(new GridLayout(1,4, 2,2));

		pagingLabel = new JLabel("", SwingConstants.CENTER);
		first = new JButton(I18n.s("first_page"));
		first.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				offset = 0;
				load();
				
			}
		});
		paging.add(first);
		previous = new JButton(I18n.s("previous_page"));
		previous.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				offset = offset - PAGING;
				if (offset < 0) {
					offset = 0;
				}
				load();
				
			}
		});
		paging.add(previous);
		next = new JButton(I18n.s("next_page"));
		next.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				offset = offset + PAGING;
				if (offset >= total) {
					offset = total - PAGING;
				}
				load();
				
			}
		});
		paging.add(next);
		
		last = new JButton(I18n.s("last_page"));
		last.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				offset = total - PAGING;
				load();
				
			}
		});
		paging.add(last);
		
		south.add(paging, BorderLayout.CENTER);
		JButton add = new JButton();
		add.setText(I18n.s("add"));
		add.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
					ObservedItem observedItem = new ObservedItem();
					addEdit(observedItem);
				
			}
		});
		south.add(pagingLabel, BorderLayout.NORTH);
		south.add(add, BorderLayout.SOUTH);
		add(south, BorderLayout.SOUTH);
		setLabels();
		load();
	
	}
	protected int deleteColumn() {
		return 9;
	}

	protected void addEdit(ObservedItem observedItem) {
		JPanel p = this;
		Container parent2 = p.getParent();
		parent2.remove(p);
		parent2.add(observedItemAddEditView(p, observedItem));
		parent2.revalidate();
	}

	protected Component observedItemAddEditView(JPanel p, ObservedItem observedItem) {
		return new ObservedItemAddEditView(p, observedItem);
	}

	protected void setLabels() {
		StringBuilder sb = new StringBuilder();
		sb.append(total > 0 ? offset + 1 : offset);
		sb.append(" - ");
		sb.append(((offset + PAGING) > total ? total : (offset + PAGING)));
		sb.append(" ").append(I18n.s("total")).append(" ").append(total);
    	pagingLabel.setText(sb.toString());
    	
    	if (total > tm.getRowCount()) {
    		if(offset > 0) {
    			first.setEnabled(true);
    			previous.setEnabled(true);
    		} else {
    			first.setEnabled(false);
    			previous.setEnabled(false);
    		}
    	} else {
    		first.setEnabled(false);
			previous.setEnabled(false);
    	}
    	
    	if(offset + PAGING < total) {
    		
    		long remaining = total % PAGING;
    		if(remaining == 0) {
    			remaining = total - PAGING;
    		} else {
    			remaining = total - remaining;
    		}
    		next.setEnabled(true);
    		last.setEnabled(true);
    	} else {
    		next.setEnabled(false);
    		last.setEnabled(false);
    	}
    	
	}

	protected void delete(final ObservedItem observedItem) {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected void done() {
				load();
			}
			@Override
			protected Void doInBackground() throws Exception {
				try (Connection conn = DB.init()) {
					observedItemDao(conn).removeRow(observedItem.getId() + "");
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}};
		worker.execute();
	}

	protected QueryResult<ObservedItem> result;
	protected ListSelectionListener selectionListener;

	protected void load() {
		SwingWorker<QueryResult<ObservedItem>, Void> worker = new SwingWorker<QueryResult<ObservedItem>, Void>() {

			@Override
			protected void done() {
				try {
					result = get();
					total = result.getCount();
					while (tm.getRowCount() > 0) {
						tm.removeRow(0);
					}
					for (int i = 0; i < result.getList().size(); i++) {
						ObservedItem observedItem = (ObservedItem) result.getList().get(i);
						addRow(observedItem);
					}
					
					tm.fireTableDataChanged();
					setLabels();

				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
			@Override
			protected QueryResult<ObservedItem> doInBackground() throws Exception {
				try (Connection conn = DB.init()) {
					return observedItemDao(conn).select(PAGING, offset, "");
				} catch (Exception e) {
					e.printStackTrace();
				}
				return new QueryResult<>(new ArrayList<>(), 0l);
			}};
		worker.execute();
	}

	protected ObservedItemDao observedItemDao(Connection conn) {
		return new ObservedItemDao(conn);
	}

	protected void addRow(ObservedItem observedItem) {
		tm.addRow(new Object[] { observedItem.getId() + "", observedItem.getName() + "", observedItem.getUrl() + "", observedItem.getComparationActivation() + "", observedItem.getValue() + "", observedItem.isChangeAfterMeetingCondition() + "", observedItem.isActive() + "", observedItem.getTempValue() + "", observedItem.getTimeOfTempValue() + "", I18n.s("delete")});
	}
}
