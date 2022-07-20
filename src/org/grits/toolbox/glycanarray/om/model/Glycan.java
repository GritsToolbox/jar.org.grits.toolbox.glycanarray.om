package org.grits.toolbox.glycanarray.om.model;

import java.util.List;


public class Glycan {
	List<GlycanMoiety> glycanMoieties;	
	Linker linker;
	List<Classification> classifications;
	String comment;			//Yukie added 12 Sep 2018
	Integer probeId;
	
	public String getComment() {
		return comment;
	}	
	public void setComment(String a_comment) {
		this.comment = a_comment;
	}
	
	public List<GlycanMoiety> getGlycanMoieties() {
		return glycanMoieties;
	}
	public void setGlycanMoieties(List<GlycanMoiety> glycanMoieties) {
		this.glycanMoieties = glycanMoieties;
	}
	
	public Linker getLinker() {
		return linker;
	}
	public void setLinker(Linker linker) {
		this.linker = linker;
	}
	
	public List<Classification> getClassifications() {
		return classifications;
	}
	
	public void setClassifications(List<Classification> classifications) {
		this.classifications = classifications;
	}
	
	public Integer getProbeId() {
		return probeId;
	}
	public void setProbeId(Integer probeId) {
		this.probeId = probeId;
	}
}
