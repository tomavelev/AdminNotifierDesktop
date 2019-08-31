package com.tomata.adminnotifier.model;

import java.io.Serializable;

import java.util.Date;

public class ObservedItem  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4662458094963468700L;
	private long id;
	private String name;
	private String url;
	private ComparationActivation comparationActivation;
	private String value;
	private boolean changeAfterMeetingCondition;
	private boolean active;
	private String tempValue;
	private Date timeOfTempValue;

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setComparationActivation(ComparationActivation comparationActivation) {
		this.comparationActivation = comparationActivation;
	}

	public ComparationActivation getComparationActivation() {
		return comparationActivation;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setChangeAfterMeetingCondition(boolean changeAfterMeetingCondition) {
		this.changeAfterMeetingCondition = changeAfterMeetingCondition;
	}

	public boolean isChangeAfterMeetingCondition() {
		return changeAfterMeetingCondition;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public void setTempValue(String tempValue) {
		this.tempValue = tempValue;
	}

	public String getTempValue() {
		return tempValue;
	}

	public void setTimeOfTempValue(Date timeOfTempValue) {
		this.timeOfTempValue = timeOfTempValue;
	}

	public Date getTimeOfTempValue() {
		return timeOfTempValue;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObservedItem other = (ObservedItem) obj;
		if (id != other.id)
				return false;
		return true;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
 builder.append("ObservedItem [");
 builder.append(", id=");
 builder.append(id);
 builder.append(", name=");
 builder.append(name);
 builder.append(", url=");
 builder.append(url);
 builder.append(", comparationActivation=");
 builder.append(comparationActivation);
 builder.append(", value=");
 builder.append(value);
 builder.append(", changeAfterMeetingCondition=");
 builder.append(changeAfterMeetingCondition);
 builder.append(", active=");
 builder.append(active);
 builder.append(", tempValue=");
 builder.append(tempValue);
 builder.append(", timeOfTempValue=");
 builder.append(timeOfTempValue);
 builder.append("]");
		return builder.toString();
	}
}
