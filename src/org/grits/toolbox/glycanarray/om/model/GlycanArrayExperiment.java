package org.grits.toolbox.glycanarray.om.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="glycanarray")
public class GlycanArrayExperiment {

	public static final String CURRENT_VERSION = "1.0";
	
	String name;
	String fileType;   //GenePix or Proscan
	List<Slide> slides;
	StatisticalMethod method;
	ValueType signalType;
	PowerLevel selectedPowerLevel;
	
	public PowerLevel getSelectedPowerLevel() {
		return selectedPowerLevel;
	}
	
	public void setSelectedPowerLevel(PowerLevel selectedPowerLevel) {
		this.selectedPowerLevel = selectedPowerLevel;
	}
	
	@XmlAttribute
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlAttribute
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	@XmlElementWrapper(name="slide")
	public List<Slide> getSlides() {
		return slides;
	}
	public void setSlides(List<Slide> slides) {
		this.slides = slides;
	}
	
	@XmlAttribute
	public StatisticalMethod getMethod() {
		return method;
	}
	public void setMethod(StatisticalMethod method) {
		this.method = method;
	}
	
	@XmlAttribute
	public ValueType getSignalType() {
		return signalType;
	}
	public void setSignalType(ValueType signalType) {
		this.signalType = signalType;
	}
	public void addSlide(Slide slide) {
		if (slides == null)
			slides = new ArrayList<Slide>();
		if (!slides.contains(slide))
			slides.add(slide);
	}
}
