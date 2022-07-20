package org.grits.toolbox.glycanarray.om.model;

import org.grits.toolbox.util.structure.glycan.filter.om.FilterSetting;

public class GlycanMoiety {
	Integer glycanId;
	String sequence;
	FilterSetting m_filterSetting;
	String orinalSequence;
	String glyTouCanId;
   
	public FilterSetting getFilterSetting() {
		return m_filterSetting;
	}
	public void setFilterSetting(FilterSetting a_filterSetting) {
		this.m_filterSetting = a_filterSetting;
	}
	
	public Integer getGlycanId() {
		return glycanId;
	}
	
	public String getSequence() {
		return sequence;
	}
	
	public void setGlycanId(Integer glycanId) {
		this.glycanId = glycanId;
	}
	
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
	public void setOrinalSequence(String sequence) {
		this.orinalSequence = sequence;
	}
	public String getOrinalSequence() {
		return orinalSequence;
	}
	
	public void setGlyTouCanID(String glyTouCanId) {
		this.glyTouCanId = glyTouCanId;
	}
	public String getGlyTouCanID() {
		return glyTouCanId;
	}

}
