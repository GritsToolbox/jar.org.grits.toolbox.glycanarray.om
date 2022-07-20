package org.grits.toolbox.glycanarray.library.om.metadata;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ProbeClassifier
{
    private String m_id = null;
    private String m_name = null;
    private String m_description = null;
    private boolean m_multiSelect = false;
    private List<String> m_values = new ArrayList<>();

    @XmlAttribute(name = "id", required = true)
    public String getId()
    {
        return m_id;
    }

    public void setId(String a_id)
    {
        m_id = a_id;
    }

    @XmlAttribute(name = "name", required = true)
    public String getName()
    {
        return m_name;
    }

    public void setName(String a_name)
    {
        m_name = a_name;
    }

    @XmlAttribute(name = "description", required = false)
    public String getDescription()
    {
        return m_description;
    }

    public void setDescription(String a_description)
    {
        m_description = a_description;
    }

    @XmlElement(name = "value", required = true)
    public List<String> getValues()
    {
        return m_values;
    }

    public void setValues(List<String> a_values)
    {
        m_values = a_values;
    }

    @XmlAttribute(name = "multi", required = false)
    public boolean isMultiSelect()
    {
        return m_multiSelect;
    }

    public void setMultiSelect(boolean a_multiSelect)
    {
        m_multiSelect = a_multiSelect;
    }

}
