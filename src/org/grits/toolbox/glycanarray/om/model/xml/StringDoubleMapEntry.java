package org.grits.toolbox.glycanarray.om.model.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class StringDoubleMapEntry {

	@XmlAttribute String concentration;
	@XmlAttribute Double value;
	
	public StringDoubleMapEntry() {
	}
	
	public StringDoubleMapEntry (String c, Double v) {
		this.concentration = c;
		this.value = v;
	}
}
