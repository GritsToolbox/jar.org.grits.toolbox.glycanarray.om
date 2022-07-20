package org.grits.toolbox.glycanarray.library.om.annotation;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;


public class SupplierLibrary
{
    private List<ProbeMetadata> m_suppleInfo = new ArrayList<ProbeMetadata>();

    @XmlElement(name = "probemetadata", required = true)
    public List<ProbeMetadata> getProbeMeta()
    {
        return m_suppleInfo;
    }

    public void setProbeMeta(List<ProbeMetadata> a_suppleInfo)
    {
        m_suppleInfo = a_suppleInfo;
    }
}
