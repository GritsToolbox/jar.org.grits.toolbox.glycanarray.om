package org.grits.toolbox.glycanarray.library.om.feature;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.log4j.Logger;
import org.grits.toolbox.glycanarray.library.om.annotation.ProbeMetadata;

@XmlRootElement(name = "linker")
@XmlType
public class Linker implements Cloneable
{
	private static Logger logger = Logger.getLogger(Linker.class);
	
    private Integer m_id = null;
    private String m_name = null;
    private String m_sequence = null;
    //private String m_linkerType = null;
    private String m_natural = null;
//    private List<ProbeMetadata> m_probeMeta = new ArrayList<ProbeMetadata>();
    private ProbeMetadata m_probeMeta = new ProbeMetadata();
	private List<Classification> m_classification = new ArrayList<Classification>();
	private String m_comment = null;
	private Integer pubChemId = null;
    
    @XmlAttribute(name = "id", required= true)
    public Integer getId()
    {
        return m_id;
    }
    public void setId(Integer a_id)
    {
        m_id = a_id;
    }
    
    @XmlAttribute(name = "name", required= true)
    public String getName()
    {
        return m_name;
    }
    public void setName(String a_name)
    {
        m_name = a_name;
    }
    
    @XmlElement(name = "sequence", required= false)
    public String getSequence()
    {
        return m_sequence;
    }
    public void setSequence(String a_sequence)
    {
        m_sequence = a_sequence;
    }
    
    //Commented by Yukie (13 Apr) instead of this, use classification
    /*@XmlElement(name = "linkertype", required= false)
    public String getLinkerType()
    {
        return m_linkerType;
    }
    public void setLinkerType(String a_linkerType)
    {
        m_linkerType = a_linkerType;
    }
    */

    @XmlElement (name = "natural", required = false)
    public String getNatural()
    {
        return m_natural;
    }

    public void setNatural(String a_natural)
    {
    	m_natural = a_natural;
    }

    @XmlElement(name = "probeMeta", required = false)
    public ProbeMetadata getProbeMeta()
    {
        return m_probeMeta;
    }

    public void setProbeMeta(ProbeMetadata a_probeMeta)
    {
    	m_probeMeta = a_probeMeta;
    }
    

    @XmlElement(name = "classification", required = false)
    public List<Classification> getClassification()
    {
        return m_classification ;
    }

    public void setClassification(List<Classification> a_classification)
    {
        m_classification = a_classification;
    }
    
    
    @XmlAttribute (name = "comment", required = false)
    public String getComment()
    {
        return m_comment;
    }

    public void setComment (String a_comment)
    {
    	m_comment = a_comment;
    }
    
    @XmlAttribute (name = "pubchemid", required = false)
    public Integer getPubChemId() {
		return pubChemId;
	}
    
    public void setPubChemId(Integer pubChemId) {
		this.pubChemId = pubChemId;
	}
    
    @Override
    public Linker clone() {
        Linker linker = new Linker();
        try{
        	linker = (Linker)super.clone();
        }catch(Exception e) {
        	logger.error("Error in cloning Linker class object", e);
        }
        return linker;
    }
    
}
