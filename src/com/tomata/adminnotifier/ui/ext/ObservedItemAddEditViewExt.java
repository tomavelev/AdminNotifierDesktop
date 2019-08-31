package com.tomata.adminnotifier.ui.ext;

import javax.swing.JPanel;

import com.tomata.adminnotifier.ui.ObservedItemView;
import com.tomata.adminnotifier.model.ObservedItem;
import com.tomata.adminnotifier.ui.ObservedItemAddEditView;

public class ObservedItemAddEditViewExt extends ObservedItemAddEditView {

	public ObservedItemAddEditViewExt(JPanel p, ObservedItem observedItem) {
		super(p, observedItem);
	}

	
	@Override
	protected void returnToList() {
		super.returnToList();
		AdminNotifierService.restart();
	}
	@Override
	protected JPanel observedItemView() {
		return Main.COMP;
	}
	
	@Override
	protected int idRowIndex() {
		return -1;
	}
	
	@Override
	protected int tempValueRowIndex() {
		return -1;
	}
	
	@Override
	protected int timeOfTempValueRowIndex() {
		return -1;
	}
	
	
	@Override
	protected int nameRowIndex() {
		return 0;
	}
	
	@Override
	protected int urlRowIndex() {
		return 1;
	}
	
	@Override
	protected int comparationActivationRowIndex() {
		return 2;
	}
	
	@Override
	protected int valueRowIndex() {
		return 3;
	}
	
	@Override
	protected int changeAfterMeetingConditionRowIndex() {
		return 4;
	}
	
	@Override
	protected int activeRowIndex() {
		return 5;
	}
	
	@Override
	protected int footerY() {
		return 7;
	}
}
