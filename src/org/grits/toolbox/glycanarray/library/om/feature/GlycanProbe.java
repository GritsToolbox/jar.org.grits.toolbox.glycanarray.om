package org.grits.toolbox.glycanarray.library.om.feature;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.grits.toolbox.glycanarray.library.om.annotation.ProbeMetadata;

//@XmlRootElement(name = "glycanProbe")
//@XmlType
public class GlycanProbe implements Probe
{
    private Integer m_id = null;
    private String m_name = null;
    private String m_comment = null;
    private Integer m_linker = null;
//    private List<Glycan> m_glycan = null; 
    private List<Classification> m_classification = new ArrayList<Classification>();      
    private ProbeMetadata m_probeMetadata = new ProbeMetadata();
    private List<Ratio> m_ratio = new ArrayList<Ratio>();
	private String m_commentPurity = null;    
	private boolean m_visible;    
    private Integer m_originalSourceId = null;
    

    @XmlAttribute(name = "id", required = true)
    public Integer getId()
    {
        return m_id;
    }
    public void setId(Integer id)
    {
        this.m_id = id;
    }
    
    @XmlAttribute(name = "name", required = true)
    public String getName()
    {
        return m_name;
    }
    public void setName(String a_name)
    {
        this.m_name = a_name;
    }
    
    @XmlAttribute(name = "linkerId", required = true)
    public Integer getLinker()
    {
        return m_linker;
    }
    public void setLinker(Integer a_linker)
    {
        this.m_linker = a_linker;
    }

	public ProbeMetadata getProbeMetadata() {
		return m_probeMetadata;
	}

	public void setProbeMetadata(ProbeMetadata m_probeMetadata) {
		this.m_probeMetadata = m_probeMetadata;
	}
	
    @XmlAttribute(name = "comment", required = true)
    public String getComment()
    {
        return m_comment;
    }
    public void setComment(String a_comment)
    {
        this.m_comment = a_comment;
    }

    @XmlElement (name = "classification", required = false)
    public List<Classification> getClassification()
    {
        return m_classification;
    }

    public void setClassification(List<Classification> a_classification)
    {
        this.m_classification = a_classification;
    }
    
    
    @XmlElement (name = "glycanRatio", required = false)
    public List<Ratio> getRatio()
    {
        return m_ratio;
    }

    public void setRatio(List<Ratio> a_ratio)
    {
    	this.m_ratio = a_ratio;
    }
    
    @XmlAttribute(name = "commentPurity", required = true)
    public String getCommentPurity()
    {
        return m_commentPurity ;
    }
    public void setCommentPurity(String a_commentPurity)
    {
        this.m_commentPurity = a_commentPurity;
    }
    

    @XmlAttribute(name = "visible", required = true)
    public boolean getVisible()
    {
        return m_visible;
    }
    public void setVisible(boolean t_visible)
    {
        this.m_visible = t_visible;
    }

    @XmlAttribute(name = "originalSourceId", required = false)
    public Integer getOriginalSourceId()
    {
        return m_originalSourceId;
    }
    public void setOriginalSourceId(Integer a_originalSourceId)
    {
        this.m_originalSourceId = a_originalSourceId;
    }
    
    
}
