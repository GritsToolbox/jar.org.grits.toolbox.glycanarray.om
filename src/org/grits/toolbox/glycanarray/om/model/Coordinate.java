package org.grits.toolbox.glycanarray.om.model;

public class Coordinate {
	Double xCoord;
	Double yCoord;
	Double diameter;
	
	public Coordinate() {
		//required by JAXB
	}
	
	public Coordinate(double coordX, double coordY) {
		this.xCoord = coordX;
		this.yCoord = coordY;
	}
	public Double getxCoord() {
		return xCoord;
	}
	public void setxCoord(Double xCoord) {
		this.xCoord = xCoord;
	}
	public Double getyCoord() {
		return yCoord;
	}
	public void setyCoord(Double yCoord) {
		this.yCoord = yCoord;
	}
	public Double getDiameter() {
		return diameter;
	}
	public void setDiameter(Double diameter) {
		this.diameter = diameter;
	}
	
	
}
