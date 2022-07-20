package org.grits.toolbox.glycanarray.om.model.xml;

import javax.xml.bind.annotation.XmlElement;

import org.grits.toolbox.glycanarray.om.model.MeasurementSet;
import org.grits.toolbox.glycanarray.om.model.PowerLevel;


public class MeasurementMapEntry {

	@XmlElement MeasurementSet set;
	@XmlElement PowerLevel power;
	
	@SuppressWarnings("unused")
	private MeasurementMapEntry() {} //Required by JAXB

	public MeasurementMapEntry(PowerLevel n, MeasurementSet s)
	{
	    this.set   = s;
	    this.power = n;
	}
	
}
