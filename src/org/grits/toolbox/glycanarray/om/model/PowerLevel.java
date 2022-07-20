package org.grits.toolbox.glycanarray.om.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.grits.toolbox.glycanarray.om.Config;


@XmlRootElement
public class PowerLevel {
	Double powerLevel=100.0;
	String flourophore="";//Config.FLOUROPHORES[0];  // default
	Double pmtGain;
	
	public String getFlourophore() {
		return flourophore;
	}
	
	public void setFlourophore(String flourophore) {
		this.flourophore = flourophore;
	}

	public Double getPowerLevel() {
		return powerLevel;
	}
	
	public void setPowerLevel(Double powerLevel) {
		this.powerLevel = powerLevel;
	}
	
	public void setPmtGain(Double pmtGain) {
		this.pmtGain = pmtGain;
	}
	
	public Double getPmtGain() {
		return pmtGain;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PowerLevel) {
			if (powerLevel != null && powerLevel.equals(((PowerLevel) obj).getPowerLevel()) && flourophore != null && flourophore.equals(((PowerLevel) obj).getFlourophore()))
				return true;
			else if (flourophore == null && powerLevel != null && powerLevel.equals(((PowerLevel) obj).getPowerLevel()))
				return true;
			else
				return false;
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		if (powerLevel != null) {
			if (flourophore != null)
				return powerLevel.hashCode() + flourophore.hashCode();
			else
				return powerLevel.hashCode();
		}
		return super.hashCode();
	}

}
