package org.grits.toolbox.glycanarray.om.model.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class DoubleDoubleMapEntry {
	@XmlElement Double concentration;
	@XmlElement List<Double> values;
	
	public DoubleDoubleMapEntry() {
	}
	
	public DoubleDoubleMapEntry ( Double c, List<Double> v) {
		this.concentration = c;
		this.values = v;
	}

}
