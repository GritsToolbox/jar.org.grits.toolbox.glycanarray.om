package org.grits.toolbox.glycanarray.library.om.feature;

import javax.xml.bind.annotation.XmlAttribute;

public class Classification
{
    private String m_classifierId = null;
    private String m_value = null;

    @XmlAttribute(name = "classifierId", required = true)
    public String getClassifierId()
    {
        return m_classifierId;
    }

    public void setClassifierId(String a_clasifierId)
    {
        m_classifierId = a_clasifierId;
    }

    @XmlAttribute(name = "value", required = true)
    public String getValue()
    {
        return m_value;
    }

    public void setValue(String a_value)
    {
        m_value = a_value;
    }
}
