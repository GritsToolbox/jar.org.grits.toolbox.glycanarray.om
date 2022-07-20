package org.grits.toolbox.glycanarray.library.om.layout;

import javax.xml.bind.annotation.XmlAttribute;

public class Block
{
    private Integer m_row = null;
    private Integer m_column = null;
    private Integer m_layoutId = null;
    
    // to save block name into Block class
    private String m_name = null;
    
    @XmlAttribute(name = "row", required= true)
    public Integer getRow()
    {
        return m_row;
    }
    public void setRow(Integer a_x)
    {
    	m_row = a_x;
    }
    @XmlAttribute(name = "column", required= true)
    public Integer getColumn()
    {
        return m_column;
    }
    public void setColumn(Integer a_y)
    {
    	m_column = a_y;
    }
    @XmlAttribute(name = "layoutId", required= true)
    public Integer getLayoutId()
    {
        return m_layoutId;
    }
    public void setLayoutId(Integer a_layoutId)
    {
        m_layoutId = a_layoutId;
    }
    
    //Save block name into Block class
    @XmlAttribute(name = "name", required= true)
    public String getBlockName()
    {
        return m_name;
    }
    public void setBlockName(String a_name)
    {
        m_name = a_name;
    }
    
}
