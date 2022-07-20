package org.grits.toolbox.glycanarray.om.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="method")
@XmlEnum
public enum StatisticalMethod {
	AVERAGE ("Average"),
	ELIMINATE ("Elimination");
	
	String label;
	
	private StatisticalMethod(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
}
