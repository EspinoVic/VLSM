package com.example.vlsm.alb;

public class Item {
	private String title;
	private String subtitle;
	private boolean active;

	public Item(String title, String subtitle, boolean active) {
		this.title = title;
		this.subtitle = subtitle;
		this.active = active;
	}

	public String getTitle() {
		return title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public boolean isActive() {
		return active;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
