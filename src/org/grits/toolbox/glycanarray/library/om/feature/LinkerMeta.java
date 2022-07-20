package org.grits.toolbox.glycanarray.library.om.feature;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

//@XmlRootElement(name = "linkerMeta")
//@XmlType
public class LinkerMeta implements Probe
{
    private String m_comment = null;
    private String m_type = null;
    private String m_natural = null;
    private Integer m_suppId = null;
    private String m_catalog = null;
    private String m_lot = null;
    private String m_date_reciev = null;
    private String m_url = null;

    
       
    @XmlElement (name = "comment", required = false)
    public String getComment()
    {
        return m_comment;
    }

    public void setComment(String a_comment)
    {
    	m_comment = a_comment;
    }
    
    @XmlElement (name = "type", required = false)
    public String getType()
    {
        return m_type;
    }

    public void setType(String a_type)
    {
    	m_type = a_type;
    }
    
    @XmlElement (name = "natural", required = false)
    public String getNatural()
    {
        return m_natural;
    }

    public void setNatural(String a_natural)
    {
    	m_natural = a_natural;
    }
           
    @XmlElement (name = "suppId", required = false)
    public Integer getSuppId()
    {
        return m_suppId;
    }

    public void setSuppId(Integer a_suppId)
    {
    	m_suppId = a_suppId;
    }
    
    @XmlElement (name = "catalog", required = false)
    public String getCatalog()
    {
        return m_catalog;
    }

    public void setCatalog(String a_catalog)
    {
    	m_catalog = a_catalog;
    }
    
    @XmlElement (name = "lot", required = false)
    public String getLot()
    {
        return m_lot;
    }

    public void setLot(String a_lot)
    {
    	m_lot = a_lot;
    }
    
    @XmlElement (name = "date_reciev", required = false)
    public String getDateReciev()
    {
        return m_date_reciev;
    }

    public void setDateReciev(String a_date_reciev)
    {
    	m_date_reciev = a_date_reciev;
    }
    
    @XmlElement (name = "url", required = false)
    public String getUrl()
    {
        return m_url;
    }

    public void setUrl(String a_url)
    {
    	m_url = a_url;
    }
}
