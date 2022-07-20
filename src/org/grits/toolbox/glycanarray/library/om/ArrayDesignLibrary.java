package org.grits.toolbox.glycanarray.library.om;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.grits.toolbox.glycanarray.library.om.feature.FeatureLibrary;
import org.grits.toolbox.glycanarray.library.om.layout.LayoutLibrary;
import org.grits.toolbox.glycanarray.library.om.metadata.LibraryMetaData;

@XmlRootElement(name = "arrayDesignLibrary")
@XmlType(propOrder={"featureLibrary","layoutLibrary","metadata"})//,"supplierLibrary"})
public class ArrayDesignLibrary
{
    private FeatureLibrary m_featureLibrary = new FeatureLibrary();
    private LayoutLibrary m_layoutLibrary = new LayoutLibrary();
    private LibraryMetaData m_metadata = new LibraryMetaData();
//    private SupplierLibrary m_supplier = new SupplierLibrary();
    private String m_name = null;
    private String version="1.0.0";
    
    @XmlAttribute(name="version", required=true)
    public String getVersion() {
		return version;
	}
    
    public void setVersion(String version) {
		this.version = version;
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
    @XmlElement(name = "featureLibrary", required= false)
    public FeatureLibrary getFeatureLibrary()
    {
        return m_featureLibrary;
    }
    public void setFeatureLibrary(FeatureLibrary a_featureLibrary)
    {
        m_featureLibrary = a_featureLibrary;
    }
    @XmlElement(name = "layoutLibrary", required= false)
    public LayoutLibrary getLayoutLibrary()
    {
        return m_layoutLibrary;
    }
    public void setLayoutLibrary(LayoutLibrary a_layoutLibrary)
    {
        m_layoutLibrary = a_layoutLibrary;
    }
    
    @XmlElement(name = "metadata", required= false) //backbonetype and clissifier
    public LibraryMetaData getMetadata()
    {
        return m_metadata;
    }
    public void setMetadata(LibraryMetaData a_metadata)
    {
        m_metadata = a_metadata;
    }

/*    @XmlElement(name = "probemetadata", required= false) //supplier information for glycan probes, linkers and oligosaccharides
    public SupplierLibrary getSupplierLibrary()
    {
        return m_supplier;
    }
    public void setSupplierLibrary (SupplierLibrary a_supplier)
    {
        m_supplier = a_supplier;
    }
*/    
    
}
