package org.grits.toolbox.glycanarray.om.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Measurement {

	StatisticalMethod statisticalMethod;
	ValueType valueType;
	
	List<SpotData> data;
	
	@XmlAttribute
	public StatisticalMethod getStatisticalMethod() {
		return statisticalMethod;
	}

	public void setStatisticalMethod(StatisticalMethod statisticalMethod) {
		this.statisticalMethod = statisticalMethod;
	}

	@XmlAttribute
	public ValueType getValueType() {
		return valueType;
	}

	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}

	@XmlElementWrapper(name="data-value")
	public List<SpotData> getData() {
		return data;
	}

	public void setData(List<SpotData> data) {
		this.data = data;
	}

	public void addData(SpotData data2) {
		if (this.data == null)
			this.data = new ArrayList<>();
		// check if it already contains the data point
		for (SpotData spotData : data) {
			if (spotData.coordinates != null && spotData.coordinates.equals(data2))  // do not add again
				return;
		}
		this.data.add(data2);
	}

	public void removeData(SpotData spotData) {
		if (data != null) {
			data.remove(spotData);
		}
	}

	
}
