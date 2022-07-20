package org.grits.toolbox.glycanarray.om.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.grits.toolbox.glycanarray.om.Config;


@XmlRootElement(name="file")
public class FileWrapper { 
	String originalPath;
	String name;
	String type;
	List<PowerLevel> powerLevels;
	String flourophore="";//Config.FLOUROPHORES[0];  // default
	String scanner;
	String powerLevel;
	
	public FileWrapper() {
	}
	
	public FileWrapper (String name, String type) {
		this.originalPath = name;
		this.name = name;
		this.type = type;
	}

	public String getOriginalPath() {
		return originalPath;
	}
	
	public void setOriginalPath(String originalPath) {
		this.originalPath = originalPath;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<PowerLevel> getPowerLevels() {
		return powerLevels;
	}

	public void setPowerLevel(List<PowerLevel> powerLevel) {
		this.powerLevels = powerLevel;
	}
	
	@XmlAttribute
	public String getFlourophore() {
		return flourophore;
	}
	
	public void setFlourophore(String flourophore) {
		this.flourophore = flourophore;
	}
	
	public void addFlourophore (String flourophore) {
		if (this.flourophore == null || this.flourophore.length() < 1)
			this.flourophore = flourophore;
		else {
			this.flourophore += ", " + flourophore;
		}
	}
	
	@XmlAttribute
	public String getPowerLevel() {
		return powerLevel;
	}	
	public void setPowerLevel(String powerLevel) {
		this.powerLevel = powerLevel;
	}
	
	
	
	@XmlAttribute
	public String getScanner() {
		return scanner;
	}
	
	public void setScanner(String scanner) {
		this.scanner = scanner;
	}
	
	public void addPowerLevel (PowerLevel powerLevel) {
		if (this.powerLevels == null)
			this.powerLevels = new ArrayList<>();
		
		if (!this.powerLevels.contains(powerLevel))
			this.powerLevels.add(powerLevel);		
	}
	
	public String getPowerLevelString () {
		if (this.powerLevels == null)
			return "";
		else {
			String powerLevelString="";
			for (PowerLevel powerLevel : powerLevels) {
				powerLevelString += powerLevel.getPowerLevel().toString() + ", ";
			}
			
			if (powerLevelString.lastIndexOf(",") != -1) 
				return powerLevelString.substring(0, powerLevelString.lastIndexOf(","));
			else 
				return powerLevelString;
		}
	}
};
