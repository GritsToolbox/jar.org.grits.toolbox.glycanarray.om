package org.grits.toolbox.glycanarray.library.om.feature;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"glycan", "linker", "feature", "glycanProbe"})
public class FeatureLibrary
{
    private List<Feature> m_feature = new ArrayList<Feature>();
    private List<Linker> m_linker = new ArrayList<Linker>();
    private List<GlycanProbe> m_probe = new ArrayList<GlycanProbe>();
    private List<Glycan> m_glycan = new ArrayList<Glycan>();
    
    
    @XmlElement(required= false)
    public List<Feature> getFeature()
    {
        return m_feature;
    }
    public void setFeature(List<Feature> a_features)
    {
        m_feature = a_features;
    }
    
    @XmlElement(required= false)
    public List<Linker> getLinker()
    {
        return m_linker;
    }
    public void setLinker(List<Linker> linker)
    {
        m_linker = linker;
    }

    @XmlElement(required= false)
    public List<GlycanProbe> getGlycanProbe()
    {
        return m_probe;
    }
    public void setGlycanProbe(List<GlycanProbe> probes)
    {
        m_probe = probes;
    }
    
    @XmlElement(required= false)
	public List<Glycan> getGlycan() 
    {
		return m_glycan;
	}
	public void setGlycan(List<Glycan> glycan) 
	{
		m_glycan = glycan;
	}
}
