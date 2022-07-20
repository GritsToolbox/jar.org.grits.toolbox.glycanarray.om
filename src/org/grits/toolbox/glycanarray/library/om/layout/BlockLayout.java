package org.grits.toolbox.glycanarray.library.om.layout;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;


public class BlockLayout
{
    private List<Spot> m_spot = new ArrayList<Spot>();
    private List<LevelUnit> m_levelUnit = new ArrayList<LevelUnit>();
    private String m_name = null;
    private Integer m_id = null;
    private String m_comment = null;
    private Integer m_groupNum = null;
    private Integer m_replicNum = null;
    private Integer m_levelNum = null;
    private Integer m_columnNum = null;
    private Integer m_rowNum = null;
    
        
    @XmlElement(name = "spot", required= true)
    public List<Spot> getSpot()
    {
        return m_spot;
    }
    public void setSpot(List<Spot> a_spots)
    {
        m_spot = a_spots;
    }
    
    @XmlElement(name = "level_unit", required= true)
    public List<LevelUnit> getLevelUnit()
    {
        return m_levelUnit;
    }
    public void setLevelUnit(List<LevelUnit> a_levelUnit)
    {
    	m_levelUnit = a_levelUnit;
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
    @XmlAttribute(name = "id", required= true)
    public Integer getId()
    {
        return m_id;
    }
    public void setId(Integer a_id)
    {
        m_id = a_id;
    }
    @XmlElement(name = "comment", required= false)
    public String getComment()
    {
        return m_comment;
    }
    public void setComment(String a_comment)
    {
        m_comment = a_comment;
    }
    
    @XmlAttribute(name = "NumOfGroups", required= true)
    public Integer getGroupNum()
    {
        return m_groupNum;
    }
    public void setGroupNum(Integer a_groupNum)
    {
    	m_groupNum = a_groupNum;
    }
    @XmlAttribute(name = "NumOfReplicates", required= true)
    public Integer getReplicNum()
    {
        return m_replicNum;
    }
    public void setReplicNum(Integer a_replicNum)
    {
    	m_replicNum = a_replicNum;
    }
    @XmlAttribute(name = "NumOfLevels", required= true)
    public Integer getLevelNum()
    {
        return m_levelNum;
    }
    public void setLevelNum(Integer a_levelNum)
    {
    	m_levelNum = a_levelNum;
    }
    @XmlAttribute(name = "NumOfColumn", required= true)
    public Integer getColumnNum()
    {
        return m_columnNum;
    }
    public void setColumnNum(Integer a_columnNum)
    {
    	m_columnNum = a_columnNum;
    }
    @XmlAttribute(name = "NumOfRow", required= true)
    public Integer getRowNum()
    {
        return m_rowNum;
    }
    public void setRowNum(Integer a_rowNum)
    {
    	m_rowNum = a_rowNum;
    }

    
}
