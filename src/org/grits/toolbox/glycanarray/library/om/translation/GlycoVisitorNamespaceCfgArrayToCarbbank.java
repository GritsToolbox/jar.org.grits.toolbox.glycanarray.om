package org.grits.toolbox.glycanarray.library.om.translation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eurocarbdb.MolecularFramework.sugar.GlycoEdge;
import org.eurocarbdb.MolecularFramework.sugar.GlycoconjugateException;
import org.eurocarbdb.MolecularFramework.sugar.Monosaccharide;
import org.eurocarbdb.MolecularFramework.sugar.NonMonosaccharide;
import org.eurocarbdb.MolecularFramework.sugar.Substituent;
import org.eurocarbdb.MolecularFramework.sugar.Sugar;
import org.eurocarbdb.MolecularFramework.sugar.SugarUnitAlternative;
import org.eurocarbdb.MolecularFramework.sugar.SugarUnitCyclic;
import org.eurocarbdb.MolecularFramework.sugar.SugarUnitRepeat;
import org.eurocarbdb.MolecularFramework.sugar.UnvalidatedGlycoNode;
import org.eurocarbdb.MolecularFramework.util.traverser.GlycoTraverser;
import org.eurocarbdb.MolecularFramework.util.traverser.GlycoTraverserSimple;
import org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitor;
import org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitorException;

public class GlycoVisitorNamespaceCfgArrayToCarbbank implements GlycoVisitor
{
    private HashMap<String, String> m_translationTable = new HashMap<>();
    private List<String> m_unknownResidues = new ArrayList<>();

    public List<String> getUnknownResidues()
    {
        return this.m_unknownResidues;
    }

    public void setUnknownResidues(List<String> a_unknownResidues)
    {
        this.m_unknownResidues = a_unknownResidues;
    }

    public GlycoVisitorNamespaceCfgArrayToCarbbank()
    {
        this.m_translationTable.put("GlcNAcb", "b-D-GlcpNAc");
        this.m_translationTable.put("GlcNGcb", "b-D-GlcpNGc");
        this.m_translationTable.put("GlcNAca", "a-D-GlcpNAc");
        this.m_translationTable.put("GlcNAb", "b-D-GlcpNA");
        this.m_translationTable.put("GlcNAc", "?-D-GlcpNAc");
        this.m_translationTable.put("Galb", "b-D-Galp");
        this.m_translationTable.put("Gal", "?-D-Galp");
        this.m_translationTable.put("Manb", "b-D-Manp");
        this.m_translationTable.put("Man", "?-D-Manp");
        this.m_translationTable.put("Mana", "a-D-Manp");
        this.m_translationTable.put("Neu5Aca", "a-D-Neup5Ac");
        this.m_translationTable.put("Neu5Acb", "b-D-Neup5Ac");
        this.m_translationTable.put("Fuca", "a-L-Fucp");
        this.m_translationTable.put("Fucb", "b-L-Fucp");
        this.m_translationTable.put("Gala", "a-D-Galp");
        this.m_translationTable.put("GalNAca", "a-D-GalpNAc");
        this.m_translationTable.put("GalNAc", "?-D-GalpNAc");
        this.m_translationTable.put("Glcb", "b-D-Glcp");
        this.m_translationTable.put("Glc", "?-D-Glcp");
        this.m_translationTable.put("GlcAa", "a-D-GlcpA");
        this.m_translationTable.put("GalNAcb", "b-D-GalpNAc");
        this.m_translationTable.put("Neu5Gca", "a-D-Neup5Gc");
        this.m_translationTable.put("Neu5Gcb", "b-D-Neup5Gc");
        this.m_translationTable.put("Xyla", "a-D-Xylp");
        this.m_translationTable.put("OSO3", "Sulfate");
        this.m_translationTable.put("S", "Sulfate");
        this.m_translationTable.put("Glca", "a-D-Glcp");
        this.m_translationTable.put("Rhaa", "a-L-Rhap");
        this.m_translationTable.put("GlcAb", "b-D-GlcpA");
        this.m_translationTable.put("KDNa", "a-D-KDNp");
        this.m_translationTable.put("P", "P");
        this.m_translationTable.put("Ac", "Ac");
    }

    @Override
    public void clear()
    {
        this.m_unknownResidues.clear();
    }

    @Override
    public GlycoTraverser getTraverser(GlycoVisitor a_visitor) throws GlycoVisitorException
    {
        return new GlycoTraverserSimple(a_visitor);
    }

    @Override
    public void visit(Monosaccharide a_arg0) throws GlycoVisitorException
    {
        // nothing to do
    }

    @Override
    public void visit(NonMonosaccharide a_arg0) throws GlycoVisitorException
    {
        // nothing to do
    }

    @Override
    public void visit(Substituent a_arg0) throws GlycoVisitorException
    {
        // nothing to do
    }

    @Override
    public void visit(SugarUnitCyclic a_arg0) throws GlycoVisitorException
    {
        // nothing to do
    }

    @Override
    public void visit(GlycoEdge a_arg0) throws GlycoVisitorException
    {
        // nothing to do
    }

    @Override
    public void visit(SugarUnitAlternative a_arg0) throws GlycoVisitorException
    {
        throw new GlycoVisitorException("SugarUnitAlternative are not supported.");
    }

    @Override
    public void visit(UnvalidatedGlycoNode a_glycoNode) throws GlycoVisitorException
    {
        try
        {
            String t_name = this.m_translationTable.get(a_glycoNode.getName());
            if (t_name == null)
            {
                this.m_unknownResidues.add(a_glycoNode.getName());
            }
            else
            {
                a_glycoNode.setName(t_name);
            }
        }
        catch (GlycoconjugateException t_exception)
        {
            throw new GlycoVisitorException(t_exception.getMessage(), t_exception);
        }
    }

    @Override
    public void start(Sugar a_sugar) throws GlycoVisitorException
    {
        this.clear();
        GlycoTraverser t_traverser = this.getTraverser(this);
        t_traverser.traverseGraph(a_sugar);
    }

    @Override
    public void visit(SugarUnitRepeat a_repeat) throws GlycoVisitorException
    {
        GlycoTraverser t_traverser = this.getTraverser(this);
        t_traverser.traverseGraph(a_repeat);
    }

}
