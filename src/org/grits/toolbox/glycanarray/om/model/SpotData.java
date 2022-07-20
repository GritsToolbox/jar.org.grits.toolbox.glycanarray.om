package org.grits.toolbox.glycanarray.om.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author sena
 *
 * Object holding data for each spot(well) in the block (pad) for the experiment
 */
@XmlRootElement(name="spot-data")
public class SpotData {
	
	Well position;
	Coordinate coordinates;
	
	Integer fPixels;
	Integer bPixels;
	
	Integer flags;
	
	Double mean;
	Double median;
	Double stdev;
	
	Double bMean;
	Double bMedian;
	Double bStDev;
	
	Double meanMinusB;
	Double medianMinusB;
	
	Double percentageOneSD;
	Double percentageTwoSD;
	Double percentageSaturated;
	
	Double totalIntensity;
	
	Double snRatio;
	
	Feature feature;  
	Integer group;
	
	Double concentration;
	UnitOfLevels probeLevelUnit;
	
	@XmlAttribute
	public Integer getGroup() {
		return group;
	}
	public void setGroup(Integer group) {
		this.group = group;
	}
	
	public Well getPosition() {
		return position;
	}

	@XmlAttribute
	public Double getStdev() {
		return stdev;
	}

	public void setStdev(Double stdev) {
		this.stdev = stdev;
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	@XmlAttribute
	public Double getConcentration() {
		return concentration;
	}

	public void setConcentration(Double double1) {
		this.concentration = double1;
	}
	
	@XmlAttribute
	public UnitOfLevels getProbeLevelUnit() {
		return probeLevelUnit;
	}

	public void setProbeLevelUnit(UnitOfLevels unitOfLevels) {
		this.probeLevelUnit = unitOfLevels;
	}

	public void setPosition(Well position) {
		this.position = position;
	}
	
	public void setCoordinates(Coordinate coordinates) {
		this.coordinates = coordinates;
	}
	
	public Coordinate getCoordinates() {
		return coordinates;
	}

	@XmlAttribute
	public Double getMean() {
		return mean;
	}

	public void setMean(Double mean) {
		this.mean = mean;
	}

	@XmlAttribute
	public Double getMedian() {
		return median;
	}

	public void setMedian(Double median) {
		this.median = median;
	}

	@XmlAttribute
	public Double getbMean() {
		return bMean;
	}

	public void setbMean(Double bMean) {
		this.bMean = bMean;
	}

	@XmlAttribute
	public Double getbMedian() {
		return bMedian;
	}

	public void setbMedian(Double bMedian) {
		this.bMedian = bMedian;
	}

	@XmlAttribute
	public Double getbStDev() {
		return bStDev;
	}

	public void setbStDev(Double bStDev) {
		this.bStDev = bStDev;
	}

	@XmlAttribute
	public Double getMeanMinusB() {
		return meanMinusB;
	}

	public void setMeanMinusB(Double meanMinusB) {
		this.meanMinusB = meanMinusB;
	}

	@XmlAttribute
	public Double getMedianMinusB() {
		return medianMinusB;
	}

	public void setMedianMinusB(Double medianMinusB) {
		this.medianMinusB = medianMinusB;
	}

	@XmlAttribute
	public Integer getfPixels() {
		return fPixels;
	}

	public void setfPixels(Integer fPixels) {
		this.fPixels = fPixels;
	}

	@XmlAttribute
	public Integer getbPixels() {
		return bPixels;
	}

	public void setbPixels(Integer bPixels) {
		this.bPixels = bPixels;
	}

	@XmlAttribute
	public Integer getFlags() {
		return flags;
	}

	public void setFlags(Integer flags) {
		this.flags = flags;
	}

	@XmlAttribute
	public Double getPercentageOneSD() {
		return percentageOneSD;
	}

	public void setPercentageOneSD(Double percentageOneSD) {
		this.percentageOneSD = percentageOneSD;
	}

	@XmlAttribute
	public Double getPercentageTwoSD() {
		return percentageTwoSD;
	}

	public void setPercentageTwoSD(Double percentageTwoSD) {
		this.percentageTwoSD = percentageTwoSD;
	}

	@XmlAttribute
	public Double getPercentageSaturated() {
		return percentageSaturated;
	}

	public void setPercentageSaturated(Double percentageSaturated) {
		this.percentageSaturated = percentageSaturated;
	}

	@XmlAttribute
	public Double getSnRatio() {
		return snRatio;
	}

	public void setSnRatio(Double snRatio) {
		this.snRatio = snRatio;
	}
	
	@XmlAttribute
	public Double getTotalIntensity() {
		return totalIntensity;
	}
	
	public void setTotalIntensity(Double totalIntensity) {
		this.totalIntensity = totalIntensity;
	}
}
