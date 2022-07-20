package org.grits.toolbox.glycanarray.library.om.layout;

import javax.xml.bind.annotation.XmlAttribute;

import org.grits.toolbox.glycanarray.om.model.UnitOfLevels;

public class Spot
{
    LevelUnit concentration;
    private Integer m_featureId = null;
    private Integer m_group = null;
    private Integer m_x = null;
    private Integer m_y = null;
    private String m_featureName = null;
    
    public LevelUnit getConcentration() {
		return concentration;
	}
    
    public void setConcentration(LevelUnit concentration) {
		this.concentration = concentration;
	}
    
    @XmlAttribute(name = "featureId", required= true)
    public Integer getFeatureId()
    {
        return m_featureId;
    }
    public void setFeatureId(Integer a_featureId)
    {
    	m_featureId = a_featureId;
    }
    @XmlAttribute(name = "group", required= true)
    public Integer getGroup()
    {
        return m_group;
    }
    public void setGroup(Integer a_group)
    {
    	m_group = a_group;
    }
    
    @XmlAttribute(name = "x", required= true)
    public Integer getX()
    {
        return m_x;
    }
    public void setX(Integer a_x)
    {
        m_x = a_x;
    }
    
    @XmlAttribute(name = "y", required= true)
    public Integer getY()
    {
        return m_y;
    }
    public void setY(Integer a_y)
    {
        m_y = a_y;
    }
    
    @XmlAttribute(name = "featureName", required= true)
    public String getFeatureName()
    {
        return m_featureName;
    }
    public void setFeatureName(String a_featureName)
    {
    	m_featureName = a_featureName;
    }
}
