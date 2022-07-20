package org.grits.toolbox.glycanarray.library.om.annotation;


import javax.xml.bind.annotation.XmlAttribute;


public class StockSolution
{
//    private Integer m_id = null;
    private String m_location = null;
    private String m_boxLocation = null;
    private String m_date = null;
    
    @XmlAttribute(name = "location", required= false)
    public String getLocation()
    {
        return m_location;
    }
    public void setLocation(String a_location)
    {
        m_location = a_location;
    }

    
    @XmlAttribute(name = "boxLocation", required= false)
    public String getBoxLocation()
    {
        return m_boxLocation;
    }
    public void setBoxLocation(String a_boxLocation)
    {
        m_boxLocation = a_boxLocation;
    }
    
    
    public String getDate() {
    	return m_date;
    }
	public void setDate(String a_date) {
		m_date = a_date;	
	}
}
