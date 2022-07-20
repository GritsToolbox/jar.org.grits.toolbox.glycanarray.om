package org.grits.toolbox.glycanarray.library.om.layout;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class SlideLayout
{
    private Integer m_id = null;
    private String m_name = null;
    private String m_description = null;
    private List<Block> m_block = new ArrayList<Block>();
     
    /*
     * get size (width and height)
     */
    private Integer m_width = null;
    private Integer m_height = null;
    
    
    @XmlAttribute(name = "id", required= true)
    public Integer getId()
    {
        return m_id;
    }
    public void setId(Integer a_id)
    {
        m_id = a_id;
    }
    @XmlAttribute(name = "name", required= true)
    public String getName()
    {
        return m_name;
    }
    public void setName(String a_name)
    {
        m_name = a_name;
    }
    @XmlElement(name = "block", required= true)
    public List<Block> getBlock()
    {
        return m_block;
    }
    public void setBlock(List<Block> a_blocks)
    {
        m_block = a_blocks;
    }
    
    public String getDescription()
    {
    	return m_description;
    }    
	public void setDescription(String a_description) {
		m_description = a_description;
	}
	
	
	
	@XmlAttribute(name="width") 
	public Integer getWidth(){
		return m_width;
	}
	public void setWidth(Integer a_width) {
		m_width = a_width;
	}
	
	@XmlAttribute(name="height")
	public Integer getHeight(){
		return m_height;
	}
	public void setHeight(Integer a_height) {
		m_height = a_height;
	}
	
	
	
}
