package org.grits.toolbox.glycanarray.om.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="well")
public class Well {

	Integer x;
	Integer y;	
	Integer diameter;
	
	public Well() {
		//required by JAXB
	}
	
	public Well(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setDiameter(Integer i) {
		this.diameter = i;
	}
	
	@XmlAttribute
	public Integer getDiameter() {
		return diameter;
	}
	
	@XmlAttribute
	public Integer getX() {
		return x;
	}
	
	public void setX(Integer x) {
		this.x = x;
	}
	
	@XmlAttribute
	public Integer getY() {
		return y;
	}
	
	public void setY(Integer y) {
		this.y = y;
	}
	
	@Override
	public String toString() {
		return y + "-" + x; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Well) {
			if (x != null && y != null)
				return x.equals(((Well)obj).x) && y.equals(((Well)obj).y);
			else 
				return x == ((Well)obj).x && y == ((Well)obj).y;
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}
