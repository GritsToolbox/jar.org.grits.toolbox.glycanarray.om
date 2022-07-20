package org.grits.toolbox.glycanarray.om.model;

import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.grits.toolbox.glycanarray.om.model.xml.BlockLayoutMapAdapter;
import org.grits.toolbox.glycanarray.om.model.xml.PowerLevelMeasurementMapAdapter;


@XmlRootElement(name="block")
public class Block {
	String blockLayoutName;
	String name;
	Well position;
	String sampleId;
	Concentration concentration;
	
	Map<Well, SpotData> layoutData;   // data for each spot from the layout, does not contain the actual values
	
	Map<PowerLevel, MeasurementSet> measurementSetMap;  // powerLevel -> MeasurementSet
	
	@XmlAttribute
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Well getPosition() {
		return position;
	}
	public void setPosition(Well position) {
		this.position = position;
	}
	
	@XmlTransient
	public String getSampleId() {
		return sampleId;
	}
	public void setSampleId(String sample) {
		this.sampleId = sample;
	}
	
	public Concentration getConcentration() {
		return concentration;
	}
	public void setConcentration(Concentration concentration) {
		this.concentration = concentration;
	}
	
	@XmlJavaTypeAdapter(BlockLayoutMapAdapter.class)
	public Map<Well, SpotData> getLayoutData() {
		return layoutData;
	}
	public void setLayoutData(Map<Well,SpotData> data) {
		this.layoutData = data;
	}
	
	@XmlJavaTypeAdapter(PowerLevelMeasurementMapAdapter.class)
	public Map<PowerLevel, MeasurementSet> getMeasurementSetMap() {
		return measurementSetMap;
	}
	public void setMeasurementSetMap(Map<PowerLevel, MeasurementSet> measurementMap) {
		this.measurementSetMap = measurementMap;
	}
	public String getBlockLayoutName() {
		return blockLayoutName;
	}
	public void setBlockLayoutName(String blockLayoutName) {
		this.blockLayoutName = blockLayoutName;
	}
}
