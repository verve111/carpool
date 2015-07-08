package ru.poputi.enums;

public enum RouteModelType {
	DRIVER, FOUND, RECENT, FULL;

	public String getTemplateId() {
		if (this == DRIVER) {
			return "tableRow.ftl";
		} else if (this == FOUND) {
			return "foundTableRow.ftl";
		} else if (this == RECENT) {
			return "recentTableRow.ftl";
		}
		return null;
	}
}
