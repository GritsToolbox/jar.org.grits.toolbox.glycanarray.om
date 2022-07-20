package org.grits.toolbox.glycanarray.library.om.metadata;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class LibraryMetaData
{
    private List<ProbeClassifier> m_probeClassifiers = new ArrayList<ProbeClassifier>();

    @XmlElement(name = "probeClassifiers", required = true)
    public List<ProbeClassifier> getProbeClassifiers()
    {
        return m_probeClassifiers;
    }

    public void setProbeClassifiers(List<ProbeClassifier> a_probeClassifiers)
    {
        m_probeClassifiers = a_probeClassifiers;
    }
}
