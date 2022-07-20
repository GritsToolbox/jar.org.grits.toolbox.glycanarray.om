package org.grits.toolbox.glycanarray.om.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="level")
public class Levels {
	
	double value;
	String unit;
	

	@XmlAttribute
	public String getUnit() {
		return unit;
	}
	
	@XmlAttribute
	public double getValue() {
		return value;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public void setValue(double d) {
		this.value = d;
	}
	
	
	public Levels (double d, String string) {
		this.value = d;
		this.unit = string;
	}
	
}
