package org.grits.toolbox.glycanarray.library.om.layout;

import javax.xml.bind.annotation.XmlAttribute;

import org.grits.toolbox.glycanarray.om.model.UnitOfLevels;

public class LevelUnit
{
	private Double m_concentration = null;
    private UnitOfLevels m_levelUnit = UnitOfLevels.FMOL;

   
    
    @XmlAttribute(name = "unit", required = true)
    public UnitOfLevels getLevelUnit()
    {
        return m_levelUnit;
    }
    public void setLevelUnit(UnitOfLevels  a_levelUnit)
    {
    	m_levelUnit = a_levelUnit;
    }
    
    @XmlAttribute(name = "level_concentration", required = true)
    public Double getConcentration()
    {
        return m_concentration;
    }
    public void setConcentration(Double a_concentration)
    {
        m_concentration = a_concentration;
    }
}
