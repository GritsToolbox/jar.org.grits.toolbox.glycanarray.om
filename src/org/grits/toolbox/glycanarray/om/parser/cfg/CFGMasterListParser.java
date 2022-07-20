package org.grits.toolbox.glycanarray.om.parser.cfg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.eurocarbdb.MolecularFramework.io.SugarImporterException;
import org.eurocarbdb.MolecularFramework.io.GlycoCT.SugarExporterGlycoCTCondensed;
import org.eurocarbdb.MolecularFramework.io.namespace.GlycoVisitorToGlycoCT;
import org.eurocarbdb.MolecularFramework.sugar.Sugar;
import org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitorException;
import org.eurocarbdb.resourcesdb.Config;
import org.eurocarbdb.resourcesdb.GlycanNamescheme;
import org.eurocarbdb.resourcesdb.io.MonosaccharideConverter;
import org.grits.toolbox.glycanarray.library.om.ArrayDesignLibrary;
import org.grits.toolbox.glycanarray.library.om.feature.Glycan;
import org.grits.toolbox.glycanarray.library.om.feature.Linker;
import org.grits.toolbox.glycanarray.library.om.translation.GlycoVisitorNamespaceCfgArrayToCarbbank;
import org.grits.toolbox.glycanarray.library.om.translation.SugarImporterNCFG;
import org.grits.toolbox.glycanarray.om.util.LibraryUtils;

public class CFGMasterListParser {	
	MasterListConfiguration config;
	/**
	 * the map from CarbId to GlycoCT sequences (to be used for glycans that have carbId listed as part of the excel file)
	 */
	Map<String, String> carbIdSequenceMap = new HashMap<>();
	
	Map<String, String> linkerList = new HashMap<>();
	/**
	 * characters to replace in the sequences and the character to replace with
	 */
	private HashMap<String, String> m_replaceSet = new HashMap<String, String>();
	private MonosaccharideConverter t_msdb;
	private GlycoVisitorToGlycoCT t_visitorToGlycoCT;
	// used for keep statistics for translation errors due to unknown residues
    HashMap<String, Integer> t_unknownResidue = new HashMap<String, Integer>();

	
	/**
	 * parse CFG masterlist file, extract glycans with their sequences and add them into the library
	 * configuration (config field) is used to determine which sheet to parse from the excel file and
	 * which columns to extract
	 * 
	 * @param filePath full path of the Excel file
	 * @param library library to add the glycans into
	 * @param versionString version of CFG array (to be added as a comment for the glycans)
	 * @return list of errorMessages if there are any errors during parsing
	 * @throws IOException
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 */
	public List<String> parse (String filePath, ArrayDesignLibrary library, String versionString) throws IOException, EncryptedDocumentException, InvalidFormatException {
		File file = new File(filePath);
		if (!file.exists())
			throw new FileNotFoundException(filePath + " does not exist!");
		
		// initialization
    	t_msdb = new MonosaccharideConverter(new Config());
    	t_visitorToGlycoCT = new GlycoVisitorToGlycoCT(t_msdb , GlycanNamescheme.CARBBANK);
    	t_visitorToGlycoCT.setUseStrict(false);
    	
		//Create Workbook instance holding reference to .xls file
		Workbook workbook = WorkbookFactory.create(file);
        
		// beta
        this.m_replaceSet.put("β", "b");
        // alpha
        this.m_replaceSet.put("α", "a");
        // beta 2
        this.m_replaceSet.put("ß", "b");
        // delta
        this.m_replaceSet.put("Δ", "d");
        // long dash
        this.m_replaceSet.put("–", "-");
        // space
        m_replaceSet.put(" ", "");
        // strange space
        m_replaceSet.put("\\u00A0", "");
		
		// get the sheet with the masterlist numbers and structures
    	Sheet sheet = workbook.getSheetAt(config.getSheetNumber());
    	//Iterate through each row one by one
        Iterator<Row> rowIterator = sheet.iterator();
        if (rowIterator.hasNext())
        	rowIterator.next();      // skip the header line
        Integer glycanCounter = LibraryUtils.getLastGlycanId(library)+1;
        boolean started = false;
       
        List<String> errorMessages = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell firstColumnCell = row.getCell(0);
            if (!started) {
            	if (firstColumnCell != null && firstColumnCell.getCellType() != CellType.BLANK) {
            		//check the numeric start
            		if (firstColumnCell.getCellType() == CellType.NUMERIC) 
            			started = true;
            		else if (firstColumnCell.getCellType() == CellType.STRING) {
            			try {
            				Integer.parseInt(firstColumnCell.getStringCellValue());
            				started = true;
            			} catch (NumberFormatException e) {
            				// do nothing
            			}
            		}
            	}
            }
            if (!started) 
            	// skip this line
            	continue;
            // stop at the first empty row after starting extracting the glycans
            if (started && 
            		(firstColumnCell == null || firstColumnCell.getCellType() == CellType.BLANK)) // skip these rows since these are the linkers at the end
            	break;
            Cell masterListCell = row.getCell(config.getMasterListColumn());
            if (masterListCell != null) {
            	String glycanName = "";
            	if (masterListCell.getCellType() == CellType.STRING) {
	            	glycanName = masterListCell.getStringCellValue().trim();
	            	// check if the name is without space "234 Sp0" vs. "234Sp0"
	            	// if so, split the number and the linker and add a space
	            	if (glycanName.indexOf(" ") == -1) {
	            		String[] splitted = LibraryUtils.splitGlycanIdAndLinker (glycanName);
	            		glycanName = splitted[0] + " " + splitted[1];
	            		//glycanName = splitted[0];
	            		glycanName = glycanName.trim(); // in case linker is empty
	            	} else {
	            		String[] splitted = glycanName.split(" "); // only get the first part
	            		if (splitted[1].startsWith("Sp")) {
	            			//glycanName = splitted[0];
	            			glycanName = splitted[0] + " " + splitted[1];
	            		} else {
	            			// controls
	            			glycanName = "";
	            		}
	            	}
            	} else if (masterListCell.getCellType() == CellType.NUMERIC)  // sometimes there is no linker, only 597 for example
            		glycanName += (int)(masterListCell.getNumericCellValue());
            	
            	if (glycanName.isEmpty()) {
            		// skip this
            		continue;
            	}
            	Glycan glycan = LibraryUtils.getGlycanByName(library, new ArrayList<>(), glycanName);
            	if (glycan == null)  {  // create a new one and add it to the library
            		glycan = new Glycan();
                	glycan.setName(glycanName);
                	glycan.setId(glycanCounter++);
                	library.getFeatureLibrary().getGlycan().add(glycan);
            	} // else update existing data
            	
            	Cell structureCell = row.getCell(config.getStructureColumn());
            	if (structureCell != null) {
            		String origStr = structureCell.getStringCellValue();
            		// replace β and α with b and a respectively since they don't show up on the UI properly
            	    for (String t_key : this.m_replaceSet.keySet()) {
                    	origStr = origStr.replace(t_key, this.m_replaceSet.get(t_key));
                    }
                    glycan.setOrigSequence(origStr);
            		glycan.setOriginalSequenceType("CFG Internal");
            	}
            
            	if (config.getCarbIdColumn() != -1) {
            		Cell commentCell = row.getCell(config.getCarbIdColumn());
            		if (commentCell != null && commentCell.getCellType() == CellType.STRING) {
            			String carbId = commentCell.getStringCellValue();
            			glycan.setComment(carbId);
            		}
            	} else {
            		glycan.setComment(versionString);
            	}
            			/*if (!carbId.isEmpty()) {
            				//extract carbId from the CFG link
            				String carbIdLink = carbId.substring(carbId.indexOf("carbId=")+7);
            				carbId = carbIdLink.substring(0, carbIdLink.indexOf("&"));
            				// lookup GlycoCT sequence from the map
            				String glycoCT = carbIdSequenceMap.get(carbId);
            				if (glycoCT != null) {
            					glycan.setSequence(glycoCT);
            					glycan.setSequenceType(Glycan.SEQUENCE_TYPE_CT);
            				} else { // translate to GlycoCT using a converter (CFG internal IUPAC like format -> GlycoCT)
            					String sequence = glycan.getOrigSequence();
            					try {
            						if (sequence == null) 
            							throw new InvalidFormatException ("No sequence for glycan " + glycan.getName());
            						if (glycan.getOrigSequence().split("-").length >= 2) { // remove linker at the end before translation
            							sequence = CFGMasterListParser.getSequence(glycan.getOrigSequence());
            							String linkerFromSeq = CFGMasterListParser.getLinker(glycan.getOrigSequence());
            							String linkerFromName = CFGMasterListParser.getLinkerFromGlycanName(glycanName);
            	    					if (!linkerFromSeq.equals(linkerFromSeq)) {
            	    						// should have matched 
            	    						errorMessages.add("Linker in ID " + linkerFromName + " does not match the one in the sequence: "
            	    								+ glycan.getOrigSequence());
            	    					}
            						} // else -> no linker at the end
            						glycoCT = translateSequence (sequence);
	            					glycan.setSequence(glycoCT);
	            					glycan.setSequenceType(Glycan.SEQUENCE_TYPE_CT);
	            					
	            					String linkerFromSeq = CFGMasterListParser.getLinker(glycan.getOrigSequence());
            						String linkerFromName = CFGMasterListParser.getLinkerFromGlycanName(glycanName);
	            					if (!linkerFromSeq.equals(linkerFromName)) {
	            						// should have matched 
	            						errorMessages.add("Linker in ID " + linkerFromName + " does not match the one in the sequence: "
	            								+ glycan.getOrigSequence());
	            					}
            					} catch (InvalidFormatException e) {
            						errorMessages.add("Translation error at row " + row.getRowNum() + "---" + e.getMessage()  + " for sequence: " + sequence);
            					}
            				}
            			} 
            		}*/
            		
            	
        		// translate to GlycoCT using a converter (CFG internal IUPAC like format -> GlycoCT)
        		String sequence = glycan.getOrigSequence();
        		if (sequence != null)  {
					try {	
						if (glycan.getOrigSequence().split("-").length >= 2) { // remove linker at the end before translation
							sequence = getSequence(glycan.getOrigSequence(), glycan.getName());
							carbIdSequenceMap.put (glycan.getName(), sequence);
							/*String linkerFromSeq = CFGMasterListParser.getLinker(glycan.getOrigSequence());
							String linkerFromName = CFGMasterListParser.getLinkerFromGlycanName(glycanName);
	    					if (!linkerFromSeq.equalsIgnoreCase(linkerFromName)) {
	    						// should have matched 
	    						errorMessages.add("Linker in ID " + glycan.getName() + " does not match the one in the sequence: "
	    								+ glycan.getOrigSequence());
	    					}*/
						} // else -> no linker at the end
						String glycoCT = translateSequence (sequence);
						glycan.setSequence(glycoCT);
						glycan.setSequenceType(Glycan.SEQUENCE_TYPE_CT);    					
					} catch (IllegalArgumentException e) {
						errorMessages.add("Translation error at row " + row.getRowNum() + "---" + e.getMessage() + " for sequence: " + sequence);
					}
        		}
            }
        }
        
        workbook.close();
        return errorMessages;
	}
	
	private String getSequence(String a_sequence, String name)
    {
        int t_index = a_sequence.lastIndexOf("-");
        String cut = a_sequence.substring(t_index);
        linkerList.put(cut, name);
        return a_sequence.substring(0, t_index).trim();
    }
	
	public String translateSequence(String origSequence) throws IllegalArgumentException {
		
		initializeIfNecessary();
		
		// replace β and α with b and a respectively since they don't show up on the UI properly
	    for (String t_key : this.m_replaceSet.keySet()) {
	    	origSequence = origSequence.replace(t_key, this.m_replaceSet.get(t_key));
        }
	    
		// parse the sequence
        SugarImporterNCFG t_importer = new SugarImporterNCFG();
        try
        {
            Sugar t_sugar = t_importer.parse(origSequence);
            GlycoVisitorNamespaceCfgArrayToCarbbank t_visitor = new GlycoVisitorNamespaceCfgArrayToCarbbank();
            try
            {
                t_visitor.start(t_sugar);
                if ( t_visitor.getUnknownResidues().size() == 0 ) {   
                	// translation successful
                	// use on the sugar and get a sugar in the right namespace
                	t_visitorToGlycoCT.start(t_sugar);
                	t_sugar = t_visitorToGlycoCT.getNormalizedSugar();
                	SugarExporterGlycoCTCondensed t_exporter = new SugarExporterGlycoCTCondensed();
                    t_exporter.start(t_sugar);
                    return t_exporter.getHashCode();
                } else {
                	for (String t_residue : t_visitor.getUnknownResidues()) {
                        Integer t_counter = t_unknownResidue.get(t_residue);
                        if ( t_counter == null ) {
                            t_unknownResidue.put(t_residue, 1);
                        }
                        else {
                            t_unknownResidue.put(t_residue, t_counter + 1);
                        }
                    }
                	throw new IllegalArgumentException("Unknown residues: " + t_visitor.getUnknownResidues().toString());
                }
            }
            catch (GlycoVisitorException e)
            {
                throw new IllegalArgumentException("Visitor exception: " + e.getMessage(), e);
            }
            
        } 
        catch (SugarImporterException e)
        {
            throw new IllegalArgumentException("Parsing error in sequence " + origSequence + " at position " + Integer.toString(e.getPosition()) + ": " + e.getErrorCode() + " " + e.getErrorText());
        }
	}

	private void initializeIfNecessary() {
		if (this.m_replaceSet.isEmpty()) {
			// beta
	        m_replaceSet.put("β", "b");
	        // alpha
	        m_replaceSet.put("α", "a");
	        // beta 2
	        m_replaceSet.put("ß", "b");
	        // delta
	        m_replaceSet.put("Δ", "d");
	        // long dash
	        m_replaceSet.put("–", "-");
	        // space
	        m_replaceSet.put(" ", "");
	        // strange space
	        m_replaceSet.put("\\u00A0", "");
		}
		
		if (t_msdb == null || t_visitorToGlycoCT == null) {
	        // initialization
	    	t_msdb = new MonosaccharideConverter(new Config());
	    	t_visitorToGlycoCT = new GlycoVisitorToGlycoCT(t_msdb , GlycanNamescheme.CARBBANK);
	    	t_visitorToGlycoCT.setUseStrict(false);
		}
	}

	/**
	 * parse list of CFG linkers. Expects an excel file with two columns: 1st column is the name of the linker and the 2nd column is the structure
	 * 
	 * @param filePath full path of the excel file
	 * @param library library to add linkers into
	 * @throws IOException
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 */
	public void parseLinkerFile (String filePath, ArrayDesignLibrary library) throws IOException, EncryptedDocumentException, InvalidFormatException {
		File file = new File(filePath);
		if (!file.exists())
			throw new FileNotFoundException(filePath + " does not exist!");
		
		// beta
        this.m_replaceSet.put("β", "b");
        // alpha
        this.m_replaceSet.put("α", "a");
        // beta 2
        this.m_replaceSet.put("ß", "b");
        // delta
        this.m_replaceSet.put("Δ", "d");
        this.m_replaceSet.put("–", "-");
		
		//Create Workbook instance holding reference to .xls file
		Workbook workbook = WorkbookFactory.create(file);
    	Sheet sheet = workbook.getSheetAt(0);
    	//Iterate through each row one by one
        Iterator<Row> rowIterator = sheet.iterator();
        if (rowIterator.hasNext())
        	rowIterator.next();      // skip the header line
        Integer linkerIdCounter = LibraryUtils.getLastLinkerId(library)+1;
        List<Linker> linkerList = new ArrayList<>();
        
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell nameCell = row.getCell(0);
            Cell sequenceCell = row.getCell(1);
            Cell pubChemCell = row.getCell(2);
            if (nameCell != null && nameCell.getCellType() == CellType.STRING) {
            	String name = nameCell.getStringCellValue();
            	Linker existing = null;
            	Set<Linker> existingList = LibraryUtils.getLinkerByName(library, linkerList, name);
            	if (existingList.isEmpty()) {
            		existing = new Linker ();
            		existing.setId(linkerIdCounter++);
            		existing.setName(name);
            		linkerList.add(existing);
            	} else {
            		existing = existingList.iterator().next();
            	}
            	if (sequenceCell != null && sequenceCell.getCellType() == CellType.STRING) {
            		String origStr = sequenceCell.getStringCellValue();
            		// replace β and α with b and a respectively since they don't show up on the UI properly
            	    for (String t_key : this.m_replaceSet.keySet()) {
                    	origStr = origStr.replace(t_key, this.m_replaceSet.get(t_key));
                    }
            		existing.setSequence(origStr);
            	}
            	if (pubChemCell != null) 
            		if (pubChemCell.getCellType() == CellType.NUMERIC) {
            			Double pubChemId = pubChemCell.getNumericCellValue();
            			if (pubChemId != null) existing.setPubChemId(pubChemId.intValue());
            		} else if (pubChemCell.getCellType() == CellType.STRING) {
            			String pubChemString = pubChemCell.getStringCellValue();
            			String[] ids = pubChemString.split(",");
            			if (ids != null && ids.length > 1) {
            				existing.setPubChemId(Integer.parseInt(ids[0]));
            				existing.setName(existing.getName());
            				// create another linker
            				Linker duplicate = new Linker();
            				duplicate.setId(linkerIdCounter++);
            				duplicate.setName(existing.getName());
            				duplicate.setSequence(existing.getSequence());
            				duplicate.setPubChemId(Integer.parseInt(ids[1].trim()));
            				linkerList.add(duplicate);
            			}
            	}
            }
        }
        
        library.getFeatureLibrary().getLinker().addAll(linkerList);
        workbook.close();
	}
	
	public void setConfig(MasterListConfiguration config) {
		this.config = config;
	}
	
	public MasterListConfiguration getConfig() {
		return config;
	}
	
	public void setCarbIdSequenceMap(Map<String, String> carbIdSequenceMap) {
		this.carbIdSequenceMap = carbIdSequenceMap;
	}
	
	public Map<String, String> getCarbIdSequenceMap() {
		return carbIdSequenceMap;
	}
	
	public HashMap<String, Integer> getUnknownResidue() {
		return t_unknownResidue;
	}
	
	public Map<String, String> getLinkerList() {
		return linkerList;
	}
	
	public static void main(String[] args) {
		CFGMasterListParser parser = new CFGMasterListParser();
        String glycanSequence = "GlcNAcb1-4GlcNAcb1-4GlcNAcb1-4GlcNAcb1-4GlcNAcb1-4GlcNAcb1";
        String glycoCT = parser.translateSequence(glycanSequence);
        System.out.println("GlycoCT\n" + glycoCT);
	}
}
