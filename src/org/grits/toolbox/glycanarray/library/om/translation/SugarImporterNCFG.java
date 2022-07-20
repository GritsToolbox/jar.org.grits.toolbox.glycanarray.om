/**
 *
 */
package org.grits.toolbox.glycanarray.library.om.translation;

import java.util.ArrayList;
import java.util.Iterator;

import org.eurocarbdb.MolecularFramework.io.SugarImporterException;
import org.eurocarbdb.MolecularFramework.io.SugarImporterText;
import org.eurocarbdb.MolecularFramework.io.iupac.IupacSubTree;
import org.eurocarbdb.MolecularFramework.sugar.GlycoEdge;
import org.eurocarbdb.MolecularFramework.sugar.GlycoconjugateException;
import org.eurocarbdb.MolecularFramework.sugar.Linkage;
import org.eurocarbdb.MolecularFramework.sugar.UnvalidatedGlycoNode;

/**
 * Galb1-4(Fuca1-3)GlcNAcb1-2Mana
 *
 * start ::= residue { linkageposition "-" linkageposition { subbranch } residue }
 *
 * linkageposition ::= number | "?"
 *
 * residue ::= { modification } symbol { symbol }
 *
 * subbranch ::= "(" fullresidue { { subbranch } fullresidue } ")"
 *
 * fullresidue ::= residue linkageposition "-" linkageposition
 *
 * symbol ::= character | number
 *
 * modification ::= "[" symbol { symbol } "]" | "(" number symbol { symbol } ")"
 *
 */
public class SugarImporterNCFG extends SugarImporterText
{
    /**
     * start ::= residue linkageposition "-" { linkageposition { subbranch }
     * residue }
     */
    @Override
    protected void start() throws SugarImporterException
    {
        try
        {
            this.clear();
            // prepare the objects
            UnvalidatedGlycoNode t_objResiduumChild = new UnvalidatedGlycoNode();
            UnvalidatedGlycoNode t_objResiduumParent = new UnvalidatedGlycoNode();
            GlycoEdge t_objEdge = new GlycoEdge();
            Linkage t_objLinkage = new Linkage();
            // residue 
            // extract the residue name and set in child
            t_objResiduumChild = this.residue();
            while (this.m_cToken != '$')
            {
                // residue { linkageposition "-" linkageposition 
                // parse the linkage
                t_objLinkage.addChildLinkage(this.linkageposition());
                if (this.m_cToken == '-')
                {
                    this.nextToken();
                }
                else
                {
                    throw new SugarImporterException("IUPAC005", this.m_iPosition);
                }
                t_objLinkage.addParentLinkage(this.linkageposition());
                t_objEdge.addGlycosidicLinkage(t_objLinkage);
                // residue { linkageposition "-" linkageposition { subbranch }
                // check if there is subbranch
                ArrayList<IupacSubTree> t_aIupacSubtree = new ArrayList<IupacSubTree>();
                while (this.m_cToken == '(' && !this.isNextTokenNumber())
                {
                    t_aIupacSubtree.add(this.subbranch());
                }
                // residue { linkageposition "-" linkageposition { subbranch } residue }
                // parse the parent residue
                t_objResiduumParent = this.residue();
                this.m_objSugar.addEdge(t_objResiduumParent, t_objResiduumChild, t_objEdge);
                // add subtrees
                for (Iterator<IupacSubTree> t_iterSubtree = t_aIupacSubtree
                        .iterator(); t_iterSubtree.hasNext();)
                {
                    IupacSubTree t_objTree = t_iterSubtree.next();
                    this.m_objSugar.addEdge(t_objResiduumParent, t_objTree.getGlycoNode(),
                            t_objTree.getGlycoEdge());
                }
                // prepare for next residue if there is one
                t_objResiduumChild = t_objResiduumParent;
                t_objResiduumParent = new UnvalidatedGlycoNode();
                t_objEdge = new GlycoEdge();
                t_objLinkage = new Linkage();
            }
            if (!this.finished())
            {
                throw new SugarImporterException("IUPAC002", this.m_iPosition);
            }
        }
        catch (GlycoconjugateException e)
        {
            throw new SugarImporterException("COMMON013", this.m_iPosition);
        }

    }

    private boolean isNextTokenNumber()
    {
        int t_iDigit = this.m_strText.charAt(this.m_iPosition + 1);
        if (t_iDigit > 47 && t_iDigit < 58)
        {
            return true;
        }
        return false;
    }

    /**
     * subbranch ::= "(" fullresidue { { subbranch } fullresidue } ")"
     *
     * @throws SugarImporterException
     * @throws GlycoconjugateException
     */
    private IupacSubTree subbranch() throws SugarImporterException, GlycoconjugateException
    {
        IupacSubTree t_objTreeChild;
        IupacSubTree t_objTreeParent;
        // "(" 
        if (this.m_cToken != '(')
        {
            throw new SugarImporterException("IUPAC000", this.m_iPosition);
        }
        this.nextToken();
        // "(" fullresidue 
        t_objTreeChild = this.fullresidue();
        this.m_objSugar.addNode(t_objTreeChild.getGlycoNode());
        while (this.m_cToken != ')')
        {
            // "(" fullresidue { { subbranch } 
            ArrayList<IupacSubTree> t_aIupacSubtree = new ArrayList<IupacSubTree>();
            while (this.m_cToken == '(')
            {
                t_aIupacSubtree.add(this.subbranch());
            }
            // "(" fullresidue { { subbranch } fullresidue } 
            t_objTreeParent = this.fullresidue();
            this.m_objSugar.addEdge(t_objTreeParent.getGlycoNode(), t_objTreeChild.getGlycoNode(),
                    t_objTreeChild.getGlycoEdge());
            // add subtrees
            for (Iterator<IupacSubTree> t_iterSubtree = t_aIupacSubtree.iterator(); t_iterSubtree
                    .hasNext();)
            {
                IupacSubTree t_objTree = t_iterSubtree.next();
                this.m_objSugar.addEdge(t_objTreeParent.getGlycoNode(), t_objTree.getGlycoNode(),
                        t_objTree.getGlycoEdge());
            }
            t_objTreeChild = t_objTreeParent;
        }
        this.nextToken();
        return t_objTreeChild;
    }

    /**
     * fullresidue ::= residue linkageposition "-" linkageposition
     *
     * @throws SugarImporterException
     * @throws GlycoconjugateException
     */
    private IupacSubTree fullresidue() throws SugarImporterException, GlycoconjugateException
    {
        // prepare the objects
        IupacSubTree t_objTree = new IupacSubTree();
        UnvalidatedGlycoNode t_objNode = new UnvalidatedGlycoNode();
        GlycoEdge t_objEdge = new GlycoEdge();
        Linkage t_objLinkage = new Linkage();
        // residue
        t_objNode = this.residue();
        // residue linkageposition
        t_objLinkage.addChildLinkage(this.linkageposition());
        // residue linkageposition "-" 
        if (this.m_cToken != '-')
        {
            throw new SugarImporterException("IUPAC005", this.m_iPosition);
        }
        this.nextToken();
        // residue linkageposition "-" linkageposition
        t_objLinkage.addParentLinkage(this.linkageposition());
        t_objEdge.addGlycosidicLinkage(t_objLinkage);
        t_objTree.setGlycoEdge(t_objEdge);
        t_objTree.setGlycoNode(t_objNode);
        return t_objTree;
    }

    /**
     * linkageposition ::= number
     *
     * @throws SugarImporterException
     */
    private int linkageposition() throws SugarImporterException
    {
        return this.number();
    }

    /**
     * resiude ::= { modification } symbol { symbol }
     *
     * @throws SugarImporterException
     * @throws GlycoconjugateException 
     */
    private UnvalidatedGlycoNode residue() throws SugarImporterException, GlycoconjugateException
    {
        UnvalidatedGlycoNode t_residue = new UnvalidatedGlycoNode();
        ArrayList<IupacSubTree> t_modification = new ArrayList<IupacSubTree>();
        while ( this.m_cToken == '[' || this.m_cToken == '(' )
        {
            t_modification.add(this.modification());
        }
        int t_iStartPosition = this.m_iPosition;
        boolean t_bLoop = true;
        do
        {
            this.symbol();
            if (this.m_cToken == '-')
            {
                t_bLoop = false;
            }
            else if (this.m_cToken == '$')
            {
                t_bLoop = false;
            }
            else
            {
                boolean t_bNumber = true;
                int t_iCounter = 0;
                do
                {
                    if (this.m_iLength > this.m_iPosition + t_iCounter)
                    {
                        int t_iDigit = this.m_strText.charAt(this.m_iPosition + t_iCounter);
                        if (t_iDigit > 47 && t_iDigit < 58)
                        {
                            t_bNumber = true;
                        }
                        else if (this.m_strText.charAt(this.m_iPosition + t_iCounter) == '-')
                        {
                            t_bNumber = false;
                            t_bLoop = false;
                        }
                        else
                        {
                            t_bNumber = false;
                        }
                    }
                    t_iCounter++;
                } while (t_bNumber);
            }
        } while (t_bLoop);
        String t_strResidueName = this.m_strText.substring(t_iStartPosition, this.m_iPosition);
        t_residue.setName(t_strResidueName);
        this.m_objSugar.addNode(t_residue);
        for (IupacSubTree t_mod : t_modification)
        {
            this.m_objSugar.addNode(t_mod.getGlycoNode());
            this.m_objSugar.addEdge(t_residue, t_mod.getGlycoNode(), t_mod.getGlycoEdge());
        }
        return t_residue;
    }

    /**
     * symbol ::= character | number
     *
     * @throws SugarImporterException
     */
    private void symbol() throws SugarImporterException
    {
        int t_iDigit = this.m_cToken;
        if (t_iDigit > 47 && t_iDigit < 58)
        {
            this.number();
        }
        else
        {
            this.character();
        }
    }

    /**
     * modification ::= "[" number symbol { symbol } "]" | "(" number symbol { symbol } ")" 
     * 
     * @return name Modication
     * @throws SugarImporterException
     */
    private IupacSubTree modification() throws SugarImporterException, GlycoconjugateException
    {
        UnvalidatedGlycoNode t_objNode = new UnvalidatedGlycoNode();
        GlycoEdge t_objEdge = new GlycoEdge();
        Linkage t_objLinkage = new Linkage();
        IupacSubTree t_modification = new IupacSubTree();
        if (this.m_cToken == '[')
        {
            this.nextToken();
            t_objLinkage.addChildLinkage(1);
            t_objLinkage.addParentLinkage(this.number());
            int t_iStartPosition = this.m_iPosition;
            this.symbol();
            while (this.m_cToken != ']')
            {
                this.symbol();
            }
            String t_strResidueName = this.m_strText.substring(t_iStartPosition, this.m_iPosition);
            this.nextToken();
            t_objNode.setName(t_strResidueName);
            t_modification.setGlycoNode(t_objNode);
            t_objEdge.addGlycosidicLinkage(t_objLinkage);
            t_modification.setGlycoEdge(t_objEdge);
        }
        else if (this.m_cToken == '(')
        {
            this.nextToken();
            t_objLinkage.addChildLinkage(1);
            t_objLinkage.addParentLinkage(this.number());
            int t_iStartPosition = this.m_iPosition;
            this.symbol();
            while (this.m_cToken != ')')
            {
                this.symbol();
            }
            String t_strResidueName = this.m_strText.substring(t_iStartPosition, this.m_iPosition);
            this.nextToken();
            t_objNode.setName(t_strResidueName);
            t_modification.setGlycoNode(t_objNode);
            t_objEdge.addGlycosidicLinkage(t_objLinkage);
            t_modification.setGlycoEdge(t_objEdge);
        }
        else
        {
            throw new SugarImporterException("IUPAC000", this.m_iPosition);
        }
        return t_modification;
    }

    private void clear()
    {
    }
}
