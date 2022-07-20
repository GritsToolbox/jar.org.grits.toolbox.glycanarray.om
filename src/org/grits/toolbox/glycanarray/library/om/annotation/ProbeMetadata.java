package org.grits.toolbox.glycanarray.library.om.annotation;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;

import org.grits.toolbox.glycanarray.library.om.feature.Probe;


//@XmlRootElement(name = "oligosaccharideMeta")
//@XmlType
public class ProbeMetadata implements Probe
{
    private String m_solvent = null;
    private String m_quantity = null;
    private String m_concentration = null;
    private String m_molWeight = null;
    private String m_staffName = null;
    private String m_date_prepared = null;
    private Boolean m_date_prep_flag = null; 
    private String m_qcFilePath = null;
    private String m_date_lastUpdate = null;
    private String m_suppName = null;
    private String m_catalog = null;
    private String m_lot = null;
    private String m_date_reciev = null;
    private Boolean m_date_reciev_flag = null; 
    private String m_date_open = null;
    private Boolean m_date_open_flag = null; 
    private String m_comment_purity = null;
    private String m_comment = null;
    private String m_sourceRef = null;
    private List<WorkingSolution> m_workSol = null;
    private List<StockSolution> m_stockSol = null;

    
    
    @XmlElement (name = "solvent", required = false)
    public String getSolvent()
    {
        return m_solvent;
    }

    public void setSolvent(String a_solvent)
    {
    	m_solvent = a_solvent;
    }
    
    @XmlElement (name = "quantity", required = false)
    public String getQuantity()
    {
        return m_quantity;
    }

    public void setQuantity(String a_quantity)
    {
    	m_quantity = a_quantity;
    }
    
    @XmlElement (name = "concentration", required = false)
    public String getConcentration()
    {
        return m_concentration;
    }

    public void setConcentration(String a_concentration)
    {
    	m_concentration = a_concentration;
    }
    
    @XmlElement (name = "staff", required = false)
    public String getStaff()
    {
        return m_staffName;
    }

    public void setStaff(String a_staffName)
    {
    	m_staffName = a_staffName;
    }
    
    @XmlElement (name = "suppName", required = false)
    public String getSuppName()
    {
        return m_suppName;
    }

    public void setSuppName(String a_suppName)
    {
    	m_suppName = a_suppName;
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
    
    @XmlElement (name = "dateRecievFlag", required = false)
    public Boolean getDateRecievFlag()
    {
        return m_date_reciev_flag;
    }

    public void setDateRecievFlag(Boolean a_date_reciev_flag)
    {
    	m_date_reciev_flag = a_date_reciev_flag;
    }
    
    
    @XmlElement (name = "molWeight", required = false)
    public String getMolWeight()
    {
        return m_molWeight;
    }

    public void setMolWeight(String a_molWeight)
    {
    	m_molWeight = a_molWeight;
    }
    
    @XmlElement (name = "datePrep", required = false)
    public String getDatePrep()
    {
        return m_date_prepared;
    }

    public void setDatePrep(String a_date_prepared)
    {
    	m_date_prepared = a_date_prepared;
    }
    
    @XmlElement (name = "datePrepFlag", required = false)
    public Boolean getDatePrepFlag()
    {
        return m_date_prep_flag;
    }

    public void setDatePrepFlag(Boolean a_date_prep_flag)
    {
    	m_date_prep_flag = a_date_prep_flag;
    }
    
    @XmlElement (name = "dateUpdate", required = false)
    public String getDateUpdate()
    {
        return m_date_lastUpdate;
    }

    public void setDateUpdate(String a_date_lastUpdate)
    {
    	m_date_lastUpdate = a_date_lastUpdate;
    }        

    @XmlElement (name = "date_open", required = false)
    public String getDateOpen()
    {
        return m_date_open;
    }
    public void setDateOpen(String a_date_open)
    {
    	m_date_open = a_date_open;
    }
    
    @XmlElement (name = "dateRecievFlag", required = false)
    public Boolean getDateOpenFlag()
    {
        return m_date_open_flag;
    }

    public void setDateOpenFlag(Boolean a_date_open_flag)
    {
    	m_date_open_flag = a_date_open_flag;
    }   
    
    
    @XmlElement (name = "qc_filepath", required = false)
    public String getQcFilePath()
    {
        return m_qcFilePath;
    }
    public void setQcFilePath(String a_qcFilePath)
    {
    	m_qcFilePath = a_qcFilePath;
    }
    
    @XmlElement(name = "workSolution", required = true)
    public List<WorkingSolution> getWorkSolution()
    {
        return m_workSol;
    }
    public void setWorkSolution(List<WorkingSolution> a_workSol)
    {
    	m_workSol = a_workSol;
    }
    
    @XmlElement(name = "stockSolution", required = true)
    public List<StockSolution> getStockSolution()
    {
        return m_stockSol;
    }
    public void setStockSolution(List<StockSolution> a_stockSol)
    {
    	m_stockSol = a_stockSol;
    }

	public String getComment_purity() {
		return m_comment_purity;
	}

	public void setComment_purity(String a_comment_purity) {
		this.m_comment_purity = a_comment_purity;
	}

	public String getComment() {
		return m_comment;
	}

	public void setComment(String a_comment) {
		this.m_comment = a_comment;
	}

	
	public String getSourceRef() {
		return m_sourceRef;
	}

	public void setSourceRef(String a_sourceRef) {
		this.m_sourceRef = a_sourceRef;
	}
}
