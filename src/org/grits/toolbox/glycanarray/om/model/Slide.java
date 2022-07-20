package org.grits.toolbox.glycanarray.om.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="slide")
public class Slide {

	String name;
	Layout layout;
	List<Block> blocks;
	
	List<FileWrapper> files;
	
	@XmlAttribute
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Layout getLayout() {
		return layout;
	}
	public void setLayout(Layout layout) {
		this.layout = layout;
	}
	
	@XmlElementWrapper(name="block")
	public List<Block> getBlocks() {
		return blocks;
	}
	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}
	
	@XmlElementWrapper(name="file")
	public List<FileWrapper> getFiles() {
		return files;
	}
	public void setFiles(List<FileWrapper> files) {
		this.files = files;
	}
	public void addFile (FileWrapper file) {
		if (files == null) {
			files = new ArrayList<FileWrapper>();
		}
		files.add(file);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Slide) {
			if (((Slide) obj).getName().equals(name)) {
				if (((Slide) obj).getLayout() != null && this.layout != null && ((Slide) obj).getLayout().id.equals(this.layout.id))
					return true;
				else if (((Slide) obj).getLayout() == null && this.layout == null)
					return true;
			}
		}
		
		return false;
	}
}
