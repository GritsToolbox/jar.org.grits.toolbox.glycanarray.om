package org.grits.toolbox.glycanarray.om.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="signalType")
@XmlEnum
public enum ValueType {

	MEDIANB ("Median-B"),
	MEANB ("Mean-B"),
	MEDIAN ("Median"),
	MEAN ("Mean"),
	MEDIANB_S ("MedianS-B"),
	MEANB_S ("MeanS-B");
	
	private String label;

	ValueType(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
}
