package org.grits.toolbox.glycanarray.om.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="unit")
@XmlEnum
public enum UnitOfMeasurement {

	MICROML("ug/ml"),
	MGML("mg/ml"),
	mM("mM"),
	DILUTION("1/x"),
	HA("HA"),
	BACTERIA("10^ x cell/ml"),
	NEAT("Neat");   //Yukie added for Neat selection: deactivate value and unit at WizardThree (15 Aug 2018)

	String label;
	
	private UnitOfMeasurement(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public static UnitOfMeasurement lookUp(String _label) {
		if (UnitOfMeasurement.MICROML.getLabel().equals(_label))
			return UnitOfMeasurement.MICROML;
		else if (UnitOfMeasurement.MGML.getLabel().equals(_label))
			return UnitOfMeasurement.MGML;
		else if (UnitOfMeasurement.mM.getLabel().equals(_label))
			return UnitOfMeasurement.mM;
		else if (UnitOfMeasurement.DILUTION.getLabel().equals(_label))
			return UnitOfMeasurement.DILUTION;
		else if (UnitOfMeasurement.HA.getLabel().equals(_label))
			return UnitOfMeasurement.HA;
		else if (UnitOfMeasurement.BACTERIA.getLabel().equals(_label))
			return UnitOfMeasurement.BACTERIA;
		else if (UnitOfMeasurement.NEAT.getLabel().equals(_label))
			return UnitOfMeasurement.NEAT;
		
		return null;
	}
}
