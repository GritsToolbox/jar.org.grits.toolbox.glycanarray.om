package org.grits.toolbox.glycanarray.om.model;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.grits.toolbox.glycanarray.om.model.xml.BlockLayoutMapAdapter;
import org.grits.toolbox.glycanarray.om.model.xml.FeatureMeasurementMapAdapter;


@XmlRootElement(name="measurementSet")
public class MeasurementSet {
	
	PowerLevel powerLevel;
	Map<Feature, Measurement> measurementMap;
	Map<Well, SpotData> dataMap;
	
	public PowerLevel getPowerLevel() {
		return powerLevel;
	}
	public void setPowerLevel(PowerLevel powerLevel) {
		this.powerLevel = powerLevel;
	}
	
	@XmlJavaTypeAdapter(FeatureMeasurementMapAdapter.class)
	public Map<Feature, Measurement> getMeasurementMap() {
		return measurementMap;
	}
	public void setMeasurementMap(Map<Feature, Measurement> measurementMap) {
		this.measurementMap = measurementMap;
	}
	
	@XmlJavaTypeAdapter(BlockLayoutMapAdapter.class)
	public Map<Well, SpotData> getDataMap() {
		return dataMap;
	}
	
	public void setDataMap(Map<Well, SpotData> dataMap) {
		this.dataMap = dataMap;
	}
}
