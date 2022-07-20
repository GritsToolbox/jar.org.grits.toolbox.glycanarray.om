package org.grits.toolbox.glycanarray.om.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="concentrationType")
@XmlEnum
public enum ConcentrationType {
	
	VALUE ("Value"),
	NEAT("Neat");
	
	String label;
	
	private ConcentrationType(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public static ConcentrationType lookUp( String _label ) {
		if (ConcentrationType.VALUE.getLabel().equals(_label))
			return ConcentrationType.VALUE;
		else if (ConcentrationType.NEAT.getLabel().equals(_label))
			return ConcentrationType.NEAT;
		
		return null;
	}

}
