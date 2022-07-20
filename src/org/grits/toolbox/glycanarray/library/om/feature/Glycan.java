package org.grits.toolbox.glycanarray.library.om.feature;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.apache.log4j.Logger;
import org.grits.toolbox.glycanarray.library.om.annotation.ProbeMetadata;
import org.grits.toolbox.util.structure.glycan.filter.om.FilterSetting;


public class Glycan implements Probe, Cloneable
{
	private static Logger logger = Logger.getLogger(Glycan.class);
	
    public static String SEQUENCE_TYPE_TEXT = "2D_TEXT";
	public static String SEQUENCE_TYPE_CT = "GlycoCT";
	public static String SEQUENCE_TYPE_CFG = "CFG_IUPAC";
	public static String SEQUENCE_TYPE_GWS = "GWS";
	public static String SEQUENCE_TYPE_WURCS = "WURCS";
	public static String SEQUENCE_TYPE_GTC = "GlyTouCan_ID";
	public static String SEQUENCE_TYPE_OTHER = "Other";
    
    private Integer m_id = null;
    private String m_sequence = null;	
    private String m_sequenceType = Glycan.SEQUENCE_TYPE_CT;
    private String m_name = null;
    private String m_originalSequenceType = null;
    private String m_originalSequence = null;
    private FilterSetting m_filterSetting = null;  
    private ProbeMetadata m_probeMeta = new ProbeMetadata();
	private String m_comments = null;
	private String m_GlyTouCanId = null;
	private List<Classification> m_classification = new ArrayList<Classification>();


	@XmlElement(name = "classification", required = false)
    public List<Classification> getClassification()
    {
        return m_classification ;
    }

    public void setClassification(List<Classification> a_classification)
    {
        m_classification = a_classification;
    }
	
    @XmlElement(name = "sequence")
    public String getSequence()
    {
        return m_sequence;
    }
    public void setSequence(String a_sequence)
    {
        m_sequence = a_sequence;
    }
        
    @XmlAttribute(name = "sequenceType", required= true)
    public String getSequenceType()
    {
        return m_sequenceType;
    }
    public void setSequenceType(String a_sequenceType)
    {
        m_sequenceType = a_sequenceType;
    }
    
    @XmlAttribute(name = "id", required= true)
    public Integer getId()
    {
        return m_id;
    }
    public void setId(Integer id)
    {
        m_id = id;
    }
 /*   
    @XmlAttribute(name = "accession", required= true)
    public String getAccessionNumber()
    {
        return m_accessionNumber;
    }
    public void setAccessionNumber(String accessionNumber)
    {
        m_accessionNumber = accessionNumber;
    }
 */   
    @XmlAttribute(name = "name", required= true)
    public String getName()
    {
        return m_name;
    }
    public void setName (String a_name)
    {
        m_name = a_name;
    }

    @XmlElement(name = "Original_sequence", required= false)
    public String getOrigSequence()
    {
        return m_originalSequence;
    }
    public void setOrigSequence(String a_origSequence)
    {
        m_originalSequence = a_origSequence;
    }
    
    @XmlAttribute(name = "Original_sequenceType", required= false)
    public String getOriginalSequenceType()
    {
        return m_originalSequenceType;
    }
    public void setOriginalSequenceType(String a_origSequenceType)
    {
        m_originalSequenceType = a_origSequenceType;
    }
    
    @XmlElement(name = "filterSetting", required= false)
    public FilterSetting getFilterSetting()
    {
        return m_filterSetting;
    }
    public void setFilterSetting(FilterSetting a_filterSetting)
    {
    	m_filterSetting = a_filterSetting;
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
	
    @XmlElement (name = "comment", required = false)
    public String getComment()
    {
        return m_comments;
    }
	public void setComment(String a_comments) {
		m_comments = a_comments;
	}
	

	@XmlAttribute(name = "glyTouCanId", required=false)
    public String getGlyTouCanId()
    {
        return m_GlyTouCanId;
    }
    public void setGlyTouCanId (String a_GlyTouCanId)
    {
        m_GlyTouCanId = a_GlyTouCanId;
    }
    

    
    
    
    @Override
    public Glycan clone() {
        Glycan glycan = new Glycan();
        try{
        	glycan = (Glycan)super.clone();
        }catch(Exception e) {
        	logger.error("Error in cloning Glycan class object", e);
        }
        return glycan;
    }
    
    
}
