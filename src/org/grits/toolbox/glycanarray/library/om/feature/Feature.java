package org.grits.toolbox.glycanarray.library.om.feature;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"ratio"})
public class Feature
{
    private Integer m_id = null;
    private String m_name = null;
    private List<Ratio> m_ratio = new ArrayList<Ratio>();   

    
    @XmlAttribute(name = "name", required= true)
    public String getName()
    {
        return m_name;
    }
    public void setName(String a_name)
    {
        m_name = a_name;
    }
    
    @XmlAttribute(name = "id", required= true)
    public Integer getId()
    {
        return m_id;
    }
    public void setId(Integer a_id)
    {
        m_id = a_id;
    }
    
    @XmlElement (name = "probeRatio", required = false)
    public List<Ratio> getRatio()
    {
        return m_ratio;
    }

    public void setRatio(List<Ratio> a_ratio)
    {
    	m_ratio = a_ratio;
    }

}
