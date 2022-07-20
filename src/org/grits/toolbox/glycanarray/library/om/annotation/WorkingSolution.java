package org.grits.toolbox.glycanarray.library.om.annotation;

public class WorkingSolution
{
    private String m_location = null;
    private String m_boxLocation = null;
    private String m_date = null;
    
    public String getLocation()
    {
        return m_location;
    }
    public void setLocation(String a_location)
    {
        m_location = a_location;
    }
 
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
