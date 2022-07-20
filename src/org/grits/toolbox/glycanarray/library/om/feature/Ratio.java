package org.grits.toolbox.glycanarray.library.om.feature;

import javax.xml.bind.annotation.XmlAttribute;

public class Ratio
{
    private Integer m_itemId = null;
    private double m_ratio = 0.0;

	@XmlAttribute(name = "itemID", required = true)
    public Integer getItemId()
    {
        return m_itemId;
    }

    public void setItemId(Integer a_itemId)
    {
    	m_itemId = a_itemId;
    }

    @XmlAttribute(name = "itemRatio", required = true)
    public double getItemRatio()
    {
        return m_ratio;
    }

    public void setItemRatio(double  a_ratio)
    {
    	m_ratio = a_ratio;
    }
}
