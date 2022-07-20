package org.grits.toolbox.glycanarray.om.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType(name="unitOfLevels")
@XmlEnum
public enum UnitOfLevels {

	FMOL("fmol/spot"),
	MICROL("ul/spot"),
	MMOL("mM"),	
	MICROMOL("uM"),
	MICROML("ug/ml"),
	MILLML("mg/ml"),
	PMOL("pmol/ul");

	String label;
	
	private UnitOfLevels(String label) {
		this.label = label;
	}

	@XmlValue
	public String getLabel() {
		return label;
	}

	public static UnitOfLevels lookUp(String _label) {
		if (UnitOfLevels.MICROML.getLabel().equals(_label))
			return UnitOfLevels.MICROML;
		else if (UnitOfLevels.MICROL.getLabel().equals(_label))
			return UnitOfLevels.MICROL;
		else if (UnitOfLevels.FMOL.getLabel().equals(_label))
			return UnitOfLevels.FMOL;
		else if (UnitOfLevels.MMOL.getLabel().equals(_label))
			return UnitOfLevels.MMOL;
		else if (UnitOfLevels.MICROMOL.getLabel().equals(_label))
			return UnitOfLevels.MICROMOL;
		else if (UnitOfLevels.MILLML.getLabel().equals(_label))
			return UnitOfLevels.MILLML;
		else if (UnitOfLevels.PMOL.getLabel().equals(_label))
			return UnitOfLevels.PMOL;
		
		return null;
	}
}
