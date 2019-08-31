package com.tomata.adminnotifier.ui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
//Downloaded from https://jdatepicker.org/
 // option 2: https://github.com/LGoodDatePicker/LGoodDatePicker//Source code: https://github.com/JDatePicker/JDatePicker
//Binary: https://search.maven.org/remotecontent?filepath=org/jdatepicker/jdatepicker/1.3.4/jdatepicker-1.3.4.jar
import javax.swing.text.DefaultFormatter;


import com.tomata.adminnotifier.dao.DB;
import com.tomata.adminnotifier.dao.ObservedItemDao;
import com.tomata.adminnotifier.i18n.I18n;
import com.tomata.adminnotifier.model.ComparationActivation;
import com.tomata.adminnotifier.model.ObservedItem;

public class ObservedItemAddEditView extends JPanel {

	protected JPanel parent;
	protected ObservedItem observedItem;

	protected JTextField fieldId;
	protected JTextField fieldName;
	protected JTextField fieldUrl;
	protected JComboBox<ComparationActivation> fieldComparationActivation;
	protected JTextField fieldValue;
	protected JCheckBox fieldChangeAfterMeetingCondition;
	protected JCheckBox fieldActive;
	protected JTextField fieldTempValue;

	public ObservedItemAddEditView(JPanel p, ObservedItem observedItem) {
		this.parent = p;
		this.observedItem = observedItem;

		Border border = getBorder();
		Border margin = new EmptyBorder(10, 10, 10, 10);
		setBorder(new CompoundBorder(border, margin));

		GridBagLayout panelGridBagLayout = new GridBagLayout();
		panelGridBagLayout.columnWidths = new int[] { 86, 86, 0 };
		panelGridBagLayout.rowHeights = new int[] { 20, 20, 20, 20, 20, 20, 20, 20, 20, 20,  0 };
		panelGridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		panelGridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,  Double.MIN_VALUE };
		setLayout(panelGridBagLayout);

			
				Properties p1 = new Properties();
				p1.put("text.today", I18n.s("text.today") );
				p1.put("text.month", I18n.s("text.month") );
				p1.put("text.year", I18n.s("text.year") );
				
		Vector itemsComparationActivation = new Vector<>();
		itemsComparationActivation.add("Equal");
		itemsComparationActivation.add("LessEqual");
		itemsComparationActivation.add("GreaterEqual");
		itemsComparationActivation.add("Greater");
		itemsComparationActivation.add("Less");
		//itemsComparationActivation.add(I18n.s("Equal"));
		//itemsComparationActivation.add(I18n.s("LessEqual"));
		//itemsComparationActivation.add(I18n.s("GreaterEqual"));
		//itemsComparationActivation.add(I18n.s("Greater"));
		//itemsComparationActivation.add(I18n.s("Less"));

		fieldId = new JTextField();
		fieldName = new JTextField();
		fieldUrl = new JTextField();
		fieldComparationActivation = new JComboBox(itemsComparationActivation);
		fieldValue = new JTextField();
		fieldChangeAfterMeetingCondition = new JCheckBox(I18n.s("change_after_meeting_condition"));
		fieldActive = new JCheckBox(I18n.s("active"));
		fieldTempValue = new JTextField();

		addLabelAndTextField(I18n.s("id") + ":", idRowIndex(), fieldId);
		addLabelAndTextField(I18n.s("name") + ":", nameRowIndex(), fieldName);
		addLabelAndTextField(I18n.s("url") + ":", urlRowIndex(), fieldUrl);
		addLabelAndTextField(I18n.s("comparation_activation") + ":", comparationActivationRowIndex(), fieldComparationActivation);
		addLabelAndTextField(I18n.s("value") + ":", valueRowIndex(), fieldValue);
		addLabelAndTextField(I18n.s("change_after_meeting_condition") + ":", changeAfterMeetingConditionRowIndex(), fieldChangeAfterMeetingCondition);
		addLabelAndTextField(I18n.s("active") + ":", activeRowIndex(), fieldActive);
		addLabelAndTextField(I18n.s("temp_value") + ":", tempValueRowIndex(), fieldTempValue);
	
		fieldId.setText(observedItem.getId()+"");
		fieldName.setText(observedItem.getName() != null ? observedItem.getName() : ""+"");
		fieldUrl.setText(observedItem.getUrl() != null ? observedItem.getUrl() : ""+"");
		if(observedItem.getComparationActivation() != null) {
			fieldComparationActivation.setSelectedIndex(ComparationActivation.valueOf(observedItem.getComparationActivation().name()).ordinal());
		}
		fieldValue.setText(observedItem.getValue() != null ? observedItem.getValue() : ""+"");
		fieldChangeAfterMeetingCondition.setSelected(observedItem.isChangeAfterMeetingCondition());
		fieldActive.setSelected(observedItem.isActive());
		fieldTempValue.setText(observedItem.getTempValue() != null ? observedItem.getTempValue() : ""+"");

		JButton ok = new JButton(I18n.s("ok"));
		ok.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		JButton cancel = new JButton(I18n.s("cancel"));
		cancel.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				returnToList();
			}
		});
		JPanel footer = new JPanel();
		footer.setLayout(new GridLayout(1, 2));
		footer.add(ok);
		footer.add(cancel);
		
	    GridBagConstraints gridBagConstraintForLabel = new GridBagConstraints();
	    gridBagConstraintForLabel.fill = GridBagConstraints.BOTH;
	    gridBagConstraintForLabel.insets = new Insets(0, 0, 5, 5);
	    gridBagConstraintForLabel.gridx = 0;
	    gridBagConstraintForLabel.gridy = footerY();
	    gridBagConstraintForLabel.gridwidth = 2;
	    add(footer, gridBagConstraintForLabel);
	
		focusFirst();
	}


	protected int idRowIndex() {
		return 0;
	}

	protected int nameRowIndex() {
		return 1;
	}

	protected int urlRowIndex() {
		return 2;
	}

	protected int comparationActivationRowIndex() {
		return 3;
	}

	protected int valueRowIndex() {
		return 4;
	}

	protected int changeAfterMeetingConditionRowIndex() {
		return 5;
	}

	protected int activeRowIndex() {
		return 6;
	}

	protected int tempValueRowIndex() {
		return 7;
	}

	protected int timeOfTempValueRowIndex() {
		return 8;
	}
	protected int footerY() {
		return 9;
	}
	public void focusFirst() {
		fieldId.requestFocus();
	}

	protected void returnToList() {
		
		Container parent2 = getParent();
		parent2.remove(this);
		parent2.add(observedItemView());
		parent2.revalidate();
	}

	protected JPanel observedItemView() {
		return new ObservedItemView();
	}
	

	protected void addLabelAndTextField(String labelText, int yPos, JComponent field) {

		if (yPos >= 0) {
	    JLabel label = new JLabel(labelText);
	    GridBagConstraints gridBagConstraintForLabel = new GridBagConstraints();
	    gridBagConstraintForLabel.fill = GridBagConstraints.BOTH;
	    gridBagConstraintForLabel.insets = new Insets(0, 0, 5, 5);
	    gridBagConstraintForLabel.gridx = 0;
	    gridBagConstraintForLabel.gridy = yPos;
	    add(label, gridBagConstraintForLabel);

	    GridBagConstraints gridBagConstraintForTextField = new GridBagConstraints();
	    gridBagConstraintForTextField.fill = GridBagConstraints.BOTH;
	    gridBagConstraintForTextField.insets = new Insets(0, 0, 5, 0);
	    gridBagConstraintForTextField.gridx = 1;
	    gridBagConstraintForTextField.gridy = yPos;
	    add(field, gridBagConstraintForTextField);
	}
	    //add(textField, gridBagConstraintForTextField);
	    //textField.setColumns(10);
	}

	protected void save() {
		observedItem.setId(Long.parseLong(fieldId.getText()));
		observedItem.setName(fieldName.getText());
		observedItem.setUrl(fieldUrl.getText());
		observedItem.setComparationActivation(ComparationActivation.values()[fieldComparationActivation.getSelectedIndex()]);
		observedItem.setValue(fieldValue.getText());
		observedItem.setChangeAfterMeetingCondition(fieldChangeAfterMeetingCondition.isSelected());
		observedItem.setActive(fieldActive.isSelected());
		observedItem.setTempValue(fieldTempValue.getText());
	
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected void done() {
				returnToList();
				
			}
			@Override
			protected Void doInBackground() throws Exception {
				try (Connection conn = DB.init()) {
					observedItemDao(conn).saveOrUpdate(observedItem);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}};
		worker.execute();
	}

	protected ObservedItemDao observedItemDao(Connection conn) {
		return new ObservedItemDao(conn);
	}

}
