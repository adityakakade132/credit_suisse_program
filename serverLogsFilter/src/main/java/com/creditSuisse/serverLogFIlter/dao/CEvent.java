package com.creditSuisse.serverLogFIlter.dao;

public class CEvent {
	private String eventId;
	private String eventDuration;
	private String type;
	private String host;
	private String alert;
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getEventDuration() {
		return eventDuration;
	}
	public void setEventDuration(String eventDuration) {
		this.eventDuration = eventDuration;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getAlert() {
		return alert;
	}
	public void setAlert(String alert) {
		this.alert = alert;
	}
	@Override
	public String toString() {
		return "CEvent [eventId=" + eventId + ", eventDuration=" + eventDuration + ", type=" + type + ", host=" + host
				+ ", alert=" + alert + "]";
	}
	public CEvent(String eventId, String eventDuration, String type, String host, String alert) {
		super();
		this.eventId = eventId;
		this.eventDuration = eventDuration;
		this.type = type;
		this.host = host;
		this.alert = alert;
	}
	
}
