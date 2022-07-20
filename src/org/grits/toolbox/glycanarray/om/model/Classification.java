package org.grits.toolbox.glycanarray.om.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="classification")
public class Classification {
	
	String name;
	String value;
	
	@XmlAttribute
	public String getName() {
		return name;
	}
	
	@XmlAttribute
	public String getValue() {
		return value;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	

}
