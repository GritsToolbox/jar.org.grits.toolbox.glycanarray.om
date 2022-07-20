package org.grits.toolbox.glycanarray.om.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Concentration {
	
	ConcentrationType type = ConcentrationType.VALUE;
	Double value=0.0;
	UnitOfMeasurement unit = UnitOfMeasurement.MICROML;
	
	public Concentration() {
	}
	
	public Concentration(ConcentrationType t, Double v, UnitOfMeasurement u) {
		this.type = t;
		this.value = v;
		this.unit = u;
	}
	
	@XmlAttribute
	public ConcentrationType getType() {
		return type;
	}
	
	@XmlAttribute
	public UnitOfMeasurement getUnit() {
		return unit;
	}
	
	@XmlAttribute
	public Double getValue() {
		return value;
	}
	
	public void setType(ConcentrationType type) {
		this.type = type;
	}
	
	public void setValue(Double value) {
		this.value = value;
	}
	
	public void setUnit(UnitOfMeasurement unit) {
		this.unit = unit;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Concentration) {
			if (type != null && !type.equals(ConcentrationType.VALUE)) {
				// only check for type 
				return type.equals(((Concentration) obj).getType());
			}
			if (type != null && type.equals(((Concentration) obj).getType())) {
				if (unit == null && ((Concentration)obj).getUnit() == null) {
					if (value == null && ((Concentration)obj).getValue() == null)
						return true;
				}
				else if (unit != null && unit.equals(((Concentration) obj).getUnit())) {
					if (value != null && value.equals(((Concentration) obj).getValue())) 
						return true;
				}
			}
		}
		
		return false;
	}
	
	public static Concentration fromString(String string) {
		if (string != null) {
			String[] words = string.split(" ");
			// first part is the number
			// second part is the unit
			if (words.length == 2) {   // value type
				Double concentrationValue = Double.parseDouble(words[0].trim());
				UnitOfMeasurement unitValue = UnitOfMeasurement.lookUp(words[1].trim());
				Concentration con = new Concentration();
				con.setType(ConcentrationType.VALUE);
				con.setUnit(unitValue);
				con.setValue(concentrationValue);
				
				return con;
			}
			else if (words.length == 1) { // NEAT or N/A
				Concentration con = new Concentration();
				con.setType(ConcentrationType.lookUp(words[0].trim()));
				con.setUnit(null);
				con.setValue(null);
				
				return con;
			}
		}
		return null;
		
	}
	
	@Override
	public String toString() {
		if (this.type.equals(ConcentrationType.VALUE)) {
			return this.value + " " + this.unit.getLabel();
		}
		else
			return this.type.getLabel();
	}
}
