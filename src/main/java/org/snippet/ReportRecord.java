package org.snippet;

public class ReportRecord {
	public String name, id, link;
	public int total, dailyNew;
	
	public ReportRecord(String name, String id, String link, int total, int dailyNew) {
		this.name = name;
		this.id = id;
		this.link = link;
		this.total = total;
		this.dailyNew = dailyNew;
	}
}
