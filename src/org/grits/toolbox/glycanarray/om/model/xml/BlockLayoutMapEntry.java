package org.grits.toolbox.glycanarray.om.model.xml;

import javax.xml.bind.annotation.XmlElement;

import org.grits.toolbox.glycanarray.om.model.SpotData;
import org.grits.toolbox.glycanarray.om.model.Well;


public class BlockLayoutMapEntry {
	
	@XmlElement SpotData spot;
	@XmlElement Well well;
	
	public BlockLayoutMapEntry() {
	}
	
	public BlockLayoutMapEntry ( Well w, SpotData s) {
		this.spot = s;
		this.well = w;
	}

}
