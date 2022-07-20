package org.grits.toolbox.glycanarray.library.om.layout;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;


public class LayoutLibrary
{
    private List<BlockLayout> m_blockLayout = new ArrayList<BlockLayout>();
    private List<SlideLayout> m_slideLayout = new ArrayList<SlideLayout>();
    
    @XmlElement(name = "blockLayout", required= true)
    public List<BlockLayout> getBlockLayout()
    {
        return m_blockLayout;
    }
    public void setBlockLayout(List<BlockLayout> a_blockLayout)
    {
        m_blockLayout = a_blockLayout;
    }
    @XmlElement(name = "slideLayout", required= true)
    public List<SlideLayout> getSlideLayout()
    {
        return m_slideLayout;
    }
    public void setSlideLayout(List<SlideLayout> a_slideLayout)
    {
        m_slideLayout = a_slideLayout;
    }
}
