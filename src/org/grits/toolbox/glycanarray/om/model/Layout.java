package org.grits.toolbox.glycanarray.om.model;

import javax.xml.bind.annotation.XmlAttribute;


public class Layout implements Comparable<Layout>{
	String id;
	String name;
	String description;
	
	@XmlAttribute
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@XmlAttribute
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@XmlAttribute
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	@Override
	public int compareTo(Layout o) {
		return this.name.compareTo(o.name);
	}

}
