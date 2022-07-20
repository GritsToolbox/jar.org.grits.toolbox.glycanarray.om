package org.grits.toolbox.glycanarray.om.model.xml;

import javax.xml.bind.annotation.XmlElement;

import org.grits.toolbox.glycanarray.om.model.Feature;
import org.grits.toolbox.glycanarray.om.model.Measurement;


public class MeasurementSetEntry {

	@XmlElement Feature feature;
	@XmlElement Measurement measurement;
	
	public MeasurementSetEntry() {
	}
	
	public MeasurementSetEntry ( Feature f, Measurement m) {
		this.feature = f;
		this.measurement = m;
	}
}
