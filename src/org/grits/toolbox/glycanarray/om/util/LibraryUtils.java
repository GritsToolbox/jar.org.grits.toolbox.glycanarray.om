package org.grits.toolbox.glycanarray.om.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.grits.toolbox.glycanarray.library.om.ArrayDesignLibrary;
import org.grits.toolbox.glycanarray.library.om.LibraryInterface;
import org.grits.toolbox.glycanarray.library.om.feature.Feature;
import org.grits.toolbox.glycanarray.library.om.feature.Glycan;
import org.grits.toolbox.glycanarray.library.om.feature.GlycanProbe;
import org.grits.toolbox.glycanarray.library.om.feature.Linker;
import org.grits.toolbox.glycanarray.library.om.layout.BlockLayout;
import org.grits.toolbox.glycanarray.library.om.layout.SlideLayout;
import org.grits.toolbox.glycanarray.om.model.Block;
import org.grits.toolbox.glycanarray.om.model.FileWrapper;
import org.grits.toolbox.glycanarray.om.model.GlycanArrayExperiment;
import org.grits.toolbox.glycanarray.om.model.Layout;
import org.grits.toolbox.glycanarray.om.model.Slide;
import org.grits.toolbox.glycanarray.om.model.StatisticalMethod;
import org.grits.toolbox.glycanarray.om.model.ValueType;
import org.grits.toolbox.glycanarray.om.parser.ParserConfiguration;
import org.grits.toolbox.glycanarray.om.parser.cfg.CFGMasterListParser;
import org.grits.toolbox.glycanarray.om.parser.cfg.GalFileParser;
import org.grits.toolbox.glycanarray.om.parser.cfg.MasterListConfiguration;
import org.grits.toolbox.util.structure.glycan.util.FilterUtils;

public class LibraryUtils {
	
	// needs to be done to initialize static variables to parse glycan sequence
	//private static GlycanWorkspace glycanWorkspace = new GlycanWorkspace(null, false, new GlycanRendererAWT());
	//private static boolean isInitializedGWS = false;
	//private static Logger logger = Logger.getLogger(LibraryUtils.class);
	
	/**
	 * return the max id for the slide layouts in the library
	 * @param library existing library
	 * @return the max/last id for the slide layout, 0 if there are no slide layouts in the library
	 */
	public static Integer getLastSlideLayoutId (ArrayDesignLibrary library) {
		int maxId = 0;
		for (SlideLayout layout: library.getLayoutLibrary().getSlideLayout()) {
			if (layout.getId() != null && layout.getId() > maxId)
				maxId = layout.getId();
		}
		return maxId;
	}
	
	/**
	 * return the max id for the block layouts in the library
	 * @param library existing library
	 * @return the max/last id for the block layout, 0 if there are no block layouts in the library
	 */
	public static Integer getLastBlockLayoutId (ArrayDesignLibrary library) {
		int maxId = 0;
		for (BlockLayout layout: library.getLayoutLibrary().getBlockLayout()) {
			if (layout.getId() != null && layout.getId() > maxId)
				maxId = layout.getId();
		}
		return maxId;
	}
	
	/**
	 * return the max id for the glycans in the library
	 * @param library existing library
	 * @return the max/last id for the glycans, 0 if there are no glycans in the library
	 */
	public static Integer getLastGlycanId (ArrayDesignLibrary library) {
		int maxId = 0;
		for (Glycan g: library.getFeatureLibrary().getGlycan()) {
			if (g.getId() != null && g.getId() > maxId)
				maxId = g.getId();
		}
		return maxId;
	}
	
	/**
	 * return the max id for the glycan probes in the library
	 * @param library existing library
	 * @return the max/last id for the glycan probes, 0 if there are no glycan probes in the library
	 */
	public static Integer getLastGlycanProbeId (ArrayDesignLibrary library) {
		int maxId = 0;
		for (GlycanProbe g: library.getFeatureLibrary().getGlycanProbe()) {
			if (g.getId() != null && g.getId() > maxId)
				maxId = g.getId();
		}
		return maxId;
	}
	
	/**
	 * return the max id for the features in the library
	 * @param library existing library
	 * @return the max/last id for the features, 0 if there are no featuress in the library
	 */
	public static Integer getLastFeatureId (ArrayDesignLibrary library) {
		int maxId = 0;
		for (Feature g: library.getFeatureLibrary().getFeature()) {
			if (g.getId() != null && g.getId() > maxId)
				maxId = g.getId();
		}
		return maxId;
	}
	
	/**
	 * return the max id for the Linkers in the library
	 * @param library existing library
	 * @return the max/last id for the linkers, 0 if there are no linkers in the library
	 */
	public static Integer getLastLinkerId (ArrayDesignLibrary library) {
		int maxId = 0;
		for (Linker g: library.getFeatureLibrary().getLinker()) {
			if (g.getId() != null && g.getId() > maxId)
				maxId = g.getId();
		}
		return maxId;
	}
	
	/**
	 * Look for a linker in the library with the given name
	 * @param library existing library
	 * @param linkerList current linkers added so far during parsing
	 * @param linkerName linker name to check
	 * @return linker if found, null otherwise
	 */
	public static Set<Linker> getLinkerByName (ArrayDesignLibrary library, List<Linker> linkerList, String linkerName) {
		Set<Linker> linkers = new HashSet<Linker>();
		for (Linker linker: library.getFeatureLibrary().getLinker()) {
			if (linker.getName().equalsIgnoreCase(linkerName))
				linkers.add(linker);
		}
		for (Linker linker: linkerList) {
			if (linker.getName().equalsIgnoreCase(linkerName))
				linkers.add(linker);
		}
		return linkers;
	}
	
	/**
	 * Look for an existing glycan in the library with the given name
	 * @param library existing library
	 * @param glycanList current glycans added so far during parsing
	 * @param glycanName glycan name to search
	 * @return glycan if found, null otherwise
	 */
	public static Glycan getGlycanByName (ArrayDesignLibrary library, List<Glycan> glycanList, String glycanName) {
		for (Glycan glycan: library.getFeatureLibrary().getGlycan()) {
			if (glycan.getName().equalsIgnoreCase(glycanName))
				return glycan;
		}
		for (Glycan glycan: glycanList) {
			if (glycan.getName().equalsIgnoreCase(glycanName))
				return glycan;
		}
		return null;
	}
	
	/**
	 * Look for an existing glycan probe in the library with the given name
	 * @param library existing library
	 * @param probeList current probes added so far during parsing
	 * @param glycanName glycan probe name to search
	 * @return glycanProbe if found, null otherwise
	 */
	public static GlycanProbe getGlycanProbeByName (ArrayDesignLibrary library, List<GlycanProbe> probeList, String name) {
		for (GlycanProbe glycanProbe: library.getFeatureLibrary().getGlycanProbe()) {
			if (glycanProbe.getName().equalsIgnoreCase(name))
				return glycanProbe;
		}
		for (GlycanProbe glycanProbe: probeList) {
			if (glycanProbe.getName().equalsIgnoreCase(name))
				return glycanProbe;
		}
		return null;
	}
	
	/**
	 * serialize the ArrayDesignLibrary into the given file
	 * 
	 * @param a_library library to save
	 * @param a_fileName location to save (full path)
	 * @throws IOException
	 * @throws JAXBException
	 */
	@SuppressWarnings("rawtypes")
	private static void saveLibrary(ArrayDesignLibrary a_library, String a_fileName)
            throws IOException, JAXBException {
        FileWriter t_writer = new FileWriter(new File(a_fileName));
        List<Class> contextList = new ArrayList<Class>(Arrays.asList(FilterUtils.filterClassContext));
		contextList.addAll(Arrays.asList(FilterUtils.filterClassContext));
	    contextList.add(ArrayDesignLibrary.class);
		JAXBContext context = JAXBContext.newInstance(contextList.toArray(new Class[contextList.size()]));
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(a_library, t_writer);
        
        t_writer.close();
    }
	
	/**
	 * Initializes graphic options of GlycanWorkspace.
	 */
	/*private static void initGraphicOptions() {
		if ( isInitializedGWS )
			return;
		// Set orientation of glycan: RL - right to left, LR - left to right, TB - top to bottom, BT - bottom to top
		glycanWorkspace.getGraphicOptions().ORIENTATION = GraphicOptions.RL;
		// Set flag to show information such as linkage positions and anomers
		glycanWorkspace.getGraphicOptions().SHOW_INFO = true;
		// Set flag to show mass
		glycanWorkspace.getGraphicOptions().SHOW_MASSES = false;
		// Set flag to show reducing end
		glycanWorkspace.getGraphicOptions().SHOW_REDEND = true;

//		glycanWorkspase.setDisplay(GraphicOptions.DISPLAY_NORMAL);
//		glycanWorkspase.setNotation(GraphicOptions.NOTATION_CFG);

		isInitializedGWS = true;
	}*/
	
	/**
	 * 
	 * @param library library (with the glycans)
	 * @param fileName name of the excel file to write to
	 * @return # of glycans where no cartoon can be generated
	 * @throws IOException
	 */
	/*public static int exportGlycansToExcel (ArrayDesignLibrary library, String fileName) throws IOException {
		if (library.getFeatureLibrary() == null || library.getFeatureLibrary().getGlycan() == null) 
			return -1;
		
		initGraphicOptions();
		
		List<org.apache.poi.ss.usermodel.Picture> m_lPictures = new ArrayList<>(); 
		
		FileOutputStream ontologyWriter = new FileOutputStream(fileName);
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		CellStyle rowStyle = wb.createCellStyle();
		rowStyle.setWrapText(true);
		
		Row headerRow = sheet.createRow(0);
		Cell cell1 = headerRow.createCell(0);
		cell1.setCellValue("Glycan Name");
		Cell cell2 = headerRow.createCell(1);
		cell2.setCellValue("Original Structure");
		cell2.setCellStyle(rowStyle);
		Cell cell3 = headerRow.createCell(2);
		cell3.setCellValue("GlycoCT");
		cell3.setCellStyle(rowStyle);
		Cell cell4 = headerRow.createCell(3);
		cell4.setCellValue("Cartoon");
		int i=1;
		int noCartoon = 0;
		for (Glycan glycan: library.getFeatureLibrary().getGlycan()) {
			Row row = sheet.createRow(i);
			cell1 = row.createCell(0);
			cell1.setCellValue(glycan.getName());
			cell2 = row.createCell(1);
			cell2.setCellStyle(rowStyle);
			if (glycan.getOrigSequence() != null) cell2.setCellValue(glycan.getOrigSequence());
			cell3 = row.createCell(2);
			cell3.setCellStyle(rowStyle);
			if (glycan.getSequence() != null) cell3.setCellValue(glycan.getSequence());
			cell4 = row.createCell(3);
			if (glycan.getSequence() != null) {
				try {
					LibraryUtils.writeCartoon (wb, sheet, i, 3, glycan.getSequence(), m_lPictures);
				} catch (Exception e) {
					// do nothing
					logger.error("Could not write the cartoon for glycan " + glycan.getName());
					noCartoon ++;
				}
			} else {
				noCartoon ++;
			}
			i++;
		}
		
		//resize pictures
		for (Picture pic: m_lPictures) {
			pic.resize();
		}
		
		wb.write(ontologyWriter);
		ontologyWriter.close();
		wb.close();
		
		return noCartoon;
	}
	
	private static void writeCartoon(Workbook wb, Sheet sheet, int i, int j, String sequence,
			List<Picture> m_lPictures) throws Exception {
		org.eurocarbdb.application.glycanbuilder.Glycan glycan = org.eurocarbdb.application.glycanbuilder.Glycan.fromGlycoCTCondensed(sequence);
		BufferedImage t_image = glycanWorkspace.getGlycanRenderer()
				.getImage(new Union<org.eurocarbdb.application.glycanbuilder.Glycan>(glycan), true, false, true, 0.5d);
		
	     ExcelWriterHelper helper = new ExcelWriterHelper();
	     helper.writeCellImage(wb, sheet, i, j, t_image, m_lPictures);
		
	}*/

	/**
	 * utility method to retrieve glycanId and linker name from concatenated version e.g "234Sp0" -> glycanId = "234", linkerName = "Sp0"
	 * if the glycanName starts with a letter, there is no splitting. 1st string is the glycanId and second string is empty string in the returned array
	 * 
	 * @param glycanName to parse
	 * @return a string array of 2 values, 1st is the glycanId, 2nd is the linkerName (if no linker, empty string)
	 */
	public static String[] splitGlycanIdAndLinker (String glycanName) {
		String[] splitted = new String[2];
		String linkerName = "";
		String glycanPart = "";
		String letters = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
		if (glycanName.isEmpty() || letters.contains(glycanName.substring(0,1))) {
			// should have started with number (234Sp0)
			// otherwise, there is only linker, no glycan (as in controls, landing lights etc.)
			splitted[0] = "";
			splitted[1] = glycanName;
			return splitted;
		}
				
		boolean linkerStarted = false;
		for (int i=0; i < glycanName.length(); i++) {
			if (letters.contains(glycanName.substring(i, i+1)) || linkerStarted) {
				// found the first letter
				linkerName += glycanName.charAt(i);
			    linkerStarted = true;
			} else if (!linkerStarted) {
				glycanPart += glycanName.charAt(i);
			} 
		}
		splitted[0] = glycanPart;
		splitted[1] = linkerName;
		return splitted;
	}
	
	/** generate the library from GAL files and master lists for different versions
	 *  the file names are hardcoded and the steps 3 and 4 need to be repeated for each version to get 
	 *  all the versions to the library
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ArrayDesignLibrary library = new ArrayDesignLibrary();
			
			PrintStream errorOut = new PrintStream(new FileOutputStream("doc" + File.separator + "erroroutput.txt"));
			
			// STEP 1
			//load mapping from carbId to GlycoCT
			//Map<String, String> map = CarbIdGlycoCTParser.parse("doc/structures.xlsx");
			
			// add the mapping to the master list parser
			CFGMasterListParser mparser = new CFGMasterListParser();	
			//mparser.setCarbIdSequenceMap(map);
		
			// STEP 2
			// get the linkers into the library
			mparser.parseLinkerFile("doc/CFG MasterLists and GAL files/LinkerList.xlsx", library);
			
			MasterListConfiguration mconfig = new MasterListConfiguration();
			
	/*		System.out.println("Getting version 2.1 masterlist");
			errorOut.println("Getting version 2.1 masterlist");
			// version 2.1   -- no GlycoCT structures
			// STEP 3   == 
			mconfig = new MasterListConfiguration();
			mconfig.setSheetNumber(0);
			mconfig.setMasterListColumn(1);
			mconfig.setStructureColumn(2);
			mconfig.setCarbIdColumn(-1);   // no carbLink
			// set the config specific to the version
			mparser.setConfig(mconfig);
			// get the glycans into the library
			List<String> errors = mparser.parse("doc/CFG MasterLists and GAL files/CFG MasterLists"
					+ "/v2.1 printing list chart number masterlist correlation.xls", library, "CFG_V2.1");
			for (String error: errors) {
				errorOut.println(error);
			}
			//END - STEP 3*/
			
			System.out.println("Getting version 3.0 masterlist");
			errorOut.println("Getting version 3.0 masterlist");
			// version 3.0   -- no GlycoCT structures
			// STEP 3   == 
			mconfig = new MasterListConfiguration();
			mconfig.setSheetNumber(0);
			mconfig.setMasterListColumn(1);
			mconfig.setStructureColumn(2);
			mconfig.setCarbIdColumn(-1);   // no carbLink
			// set the config specific to the version
			mparser.setConfig(mconfig);
			// get the glycans into the library
			List<String> errors = mparser.parse("doc/CFG MasterLists and GAL files/CFG MasterLists"
					+ "/Printing list chart number masterlist correlation v3.0 in correct format.xls", library, "CFG_V3.0");
			//END - STEP 3
			for (String error: errors) {
				errorOut.println(error);
			}
			
			System.out.println("Getting version 3.1 masterlist");
			errorOut.println("Getting version 3.1 masterlist");
			// version 3.1   -- no GlycoCT structures
			// STEP 3   == 
			mconfig = new MasterListConfiguration();
			mconfig.setSheetNumber(1);
			mconfig.setMasterListColumn(1);
			mconfig.setStructureColumn(2);
			mconfig.setCarbIdColumn(-1);   // no carbLink
			// set the config specific to the version
			mparser.setConfig(mconfig);
			// get the glycans into the library
			errors = mparser.parse("doc/CFG MasterLists and GAL files/CFG MasterLists"
					+ "/v3.1 database list with a's and b's.xls", library, "CFG_V3.1");
			//END - STEP 3
			for (String error: errors) {
				errorOut.println(error);
			}
			
			System.out.println("Getting version 3.2 masterlist");
			errorOut.println("Getting version 3.2 masterlist");
			// version 3.2   -- no GlycoCT structures
			// STEP 3   == 
			mconfig = new MasterListConfiguration();
			mconfig.setSheetNumber(2);
			mconfig.setMasterListColumn(1);
			mconfig.setStructureColumn(2);
			mconfig.setCarbIdColumn(-1);   // no carbLink
			// set the config specific to the version
			mparser.setConfig(mconfig);
			// get the glycans into the library
			errors = mparser.parse("doc/CFG MasterLists and GAL files/CFG MasterLists"
					+ "/v3.2 ordered list for database.xls", library, "CFG_V3.2");
			//END - STEP 3
			for (String error: errors) {
				errorOut.println(error);
			}
			
			System.out.println("Getting version 4.0 masterlist");
			errorOut.println("Getting version 4.0 masterlist");
			// version 4.0   -- no GlycoCT structures
			// STEP 3   == 
			mconfig = new MasterListConfiguration();
			mconfig.setSheetNumber(2);
			mconfig.setMasterListColumn(1);
			mconfig.setStructureColumn(2);
			mconfig.setCarbIdColumn(-1);   // no carbLink
			// set the config specific to the version
			mparser.setConfig(mconfig);
			// get the glycans into the library
			errors = mparser.parse("doc/CFG MasterLists and GAL files/CFG MasterLists"
					+ "/Corrected Masterlist v4.0.xls", library, "CFG_V4.0");
			//END - STEP 3
			for (String error: errors) {
				errorOut.println(error);
			}
			
			System.out.println("Getting version 4.1 masterlist");
			errorOut.println("Getting version 4.1 masterlist");
			// version 4.1   -- no GlycoCT structures
			// STEP 3   == 
			mconfig = new MasterListConfiguration();
			mconfig.setSheetNumber(0);
			mconfig.setMasterListColumn(1);
			mconfig.setStructureColumn(2);
			mconfig.setCarbIdColumn(-1);   // no carbLink
			// set the config specific to the version
			mparser.setConfig(mconfig);
			// get the glycans into the library
			errors = mparser.parse("doc/CFG MasterLists and GAL files/CFG MasterLists"
					+ "/CFGv4-1_Masterlist-Chart-GeneID-Structure.xls", library, "CFG_V4.1");
			//END - STEP 3
			for (String error: errors) {
				errorOut.println(error);
			}
			
			System.out.println("Getting version 4.2 masterlist");
			errorOut.println("Getting version 4.2 masterlist");
			// version 4.1   -- no GlycoCT structures
			// STEP 3   == 
			mconfig = new MasterListConfiguration();
			mconfig.setSheetNumber(0);
			mconfig.setMasterListColumn(1);
			mconfig.setStructureColumn(3);
			mconfig.setCarbIdColumn(16);   
			// set the config specific to the version
			mparser.setConfig(mconfig);
			// get the glycans into the library
			errors = mparser.parse("doc/CFG MasterLists and GAL files/CFG MasterLists"
					+ "/CFGv4_2_Masterlist with cartoons.xls", library, "CFG_V4.2");
			//END - STEP 3
			for (String error: errors) {
				errorOut.println(error);
			}

			System.out.println("Getting version 5.0 masterlist");
			errorOut.println("Getting version 5.0 masterlist");
			// version 5.0
			// STEP 3   == same as version 5.2
			mconfig = new MasterListConfiguration();
			mconfig.setSheetNumber(1);
			mconfig.setMasterListColumn(1);
			mconfig.setStructureColumn(3);
			mconfig.setCarbIdColumn(18);
			// set the config specific to the version
			mparser.setConfig(mconfig);
			// get the glycans into the library
			errors = mparser.parse("doc/CFG MasterLists and GAL files/CFG MasterLists"
					+ "/CFGv5_Masterlist-with-cartoons_v071111.xls", library, "CFG_V5.0");
			//END - STEP 3
			for (String error: errors) {
				errorOut.println(error);
			}
			
			System.out.println("Getting version 5.1 masterlist");
			errorOut.println("Getting version 5.1 masterlist");
			// version 5.0
			// STEP 3   == same as version 5.2
			mconfig = new MasterListConfiguration();
			mconfig.setSheetNumber(1);
			mconfig.setMasterListColumn(1);
			mconfig.setStructureColumn(3);
			mconfig.setCarbIdColumn(18);
			// set the config specific to the version
			mparser.setConfig(mconfig);
			// get the glycans into the library
			errors = mparser.parse("doc/CFG MasterLists and GAL files/CFG MasterLists"
					+ "/CFGv5.1_Masterlist-with-cartoons_v071111.xls", library, "CFG_V5.1");
			//END - STEP 3
			for (String error: errors) {
				errorOut.println(error);
			}
			
			System.out.println("Getting version 5.2 masterlist");
			errorOut.println("Getting version 5.2 masterlist");
			// STEP 3
			mconfig = new MasterListConfiguration();
			mconfig.setSheetNumber(1);
			mconfig.setMasterListColumn(1);
			mconfig.setStructureColumn(3); 
			mconfig.setCarbIdColumn(18);   // the column number for the carbIdLink column
			// set the config specific to the version
			mparser.setConfig(mconfig);
			// get the glycans into the library
			errors = mparser.parse("doc/CFG MasterLists and GAL files/CFG MasterLists"
					+ "/CFGv5.2_Masterlist-with-cartoons_v011514.xls", library, "CFG_V5.2");
			//END - STEP 3
			for (String error: errors) {
				errorOut.println(error);
			}
			
			System.out.println("Getting version 5.3 masterlist");
			errorOut.println("Getting version 5.3 masterlist");
			// STEP 3
			mconfig = new MasterListConfiguration();
			mconfig.setSheetNumber(1);
			mconfig.setMasterListColumn(1);
			mconfig.setStructureColumn(3); 
			mconfig.setCarbIdColumn(18);   // the column number for the carbIdLink column
			// set the config specific to the version
			mparser.setConfig(mconfig);
			// get the glycans into the library
			errors = mparser.parse("doc/CFG MasterLists and GAL files/CFG MasterLists"
					+ "/CFGv5.3_Masterlist-with-cartoons_v081614.xls", library, "CFG_V5.3");
			//END - STEP 3
			for (String error: errors) {
				errorOut.println(error);
			}
			
			System.out.println("Getting version 5.4 masterlist");
			errorOut.println("Getting version 5.4 masterlist");
			// STEP 3
			mconfig.setSheetNumber(3);
			mconfig.setMasterListColumn(4);
			mconfig.setStructureColumn(5); 
			mconfig.setCarbIdColumn(-1);   // no carbLink
			// set the config specific to the version
			mparser.setConfig(mconfig);
			// get the glycans into the library
			errors = mparser.parse("doc/CFG MasterLists and GAL files/CFG MasterLists"
					+ "/CFG array masterlist version comparison.xlsx", library, "CFG_V5.4");
			//END - STEP 3
			for (String error: errors) {
				errorOut.println(error);
			}
			
			// Statistics about Unknown Residues
			errorOut.println("Unknown Residues Summary");
			HashMap<String, Integer> errorMap = mparser.getUnknownResidue();
			for (String t_key : errorMap.keySet()) {
	            errorOut.println(t_key + " " + errorMap.get(t_key));
	        }
			
			ParserConfiguration config;
			GalFileParser parser;
			//GAL Files (and some txt files) - Create Slide Layouts for each version
			System.out.println("Getting version 5.4");
			// STEP 4
			config = new ParserConfiguration();
			config.setBlockColumn(0);
			config.setCoordinateColumnY(1);
			config.setCoordinateColumnX(2);
			config.setNameColumn(3);
			parser = new GalFileParser();
			parser.setConfig(config);
			// parse the GAL File to create slide/block layouts
			parser.parse("doc/CFG MasterLists and GAL files/CFG Gal files/CFGv5.4-fixed.gal", library, "CFG_V5.4", 6, false);
			// END - STEP 4
			
			System.out.println("Getting version 5.3");
			// STEP 4
			config = new ParserConfiguration();
			config.setBlockColumn(0);
			config.setCoordinateColumnY(1);
			config.setCoordinateColumnX(2);
			config.setNameColumn(4);
			parser = new GalFileParser();
			parser.setConfig(config);
			// parse the GAL File to create slide/block layouts
			parser.parse("doc/CFG MasterLists and GAL files/CFG Gal files/CFG5.3-fixed.gal", library, "CFG_V5.3", 6, false);
			// END - STEP 4
			
			System.out.println("Getting version 5.2");
			// STEP 4
			config = new ParserConfiguration();
			config.setBlockColumn(0);
			config.setCoordinateColumnY(1);
			config.setCoordinateColumnX(2);
			config.setNameColumn(3);
			parser = new GalFileParser();
			parser.setConfig(config);
			// parse the GAL File to create slide/block layouts
			parser.parse("doc/CFG MasterLists and GAL files/CFG Gal files/GenePix_CFG_V5.2_Slide-fixed.gal", library, "CFG_V5.2", 6, false);
			// END - STEP 4
			
			System.out.println("Getting version 5.1");
			// STEP 4
			config = new ParserConfiguration();
			config.setBlockColumn(-1);
			config.setMetaRow(0);
			config.setMetaColumn(1);
			config.setCoordinateColumnX(3);  // column
			config.setCoordinateColumnY(2);  // row
			config.setNameColumn(4);
			parser = new GalFileParser();
			parser.setConfig(config);
			// parse the txt File to create slide/block layouts
			parser.parse("doc/CFG MasterLists and GAL files/CFG Gal files/CFGv5.1_gene ID-fixed.txt", library, "CFG_V5.1", 6, true);
			// END - STEP 4
			
			System.out.println("Getting version 5.1-v2");
			// STEP 4
			config = new ParserConfiguration();
			config.setBlockColumn(-1);
			config.setMetaRow(0);
			config.setMetaColumn(1);
			config.setCoordinateColumnX(3);  // column
			config.setCoordinateColumnY(2);  // row
			config.setNameColumn(4);
			parser = new GalFileParser();
			parser.setConfig(config);
			// parse the txt File to create slide/block layouts
			parser.parse("doc/CFG MasterLists and GAL files/CFG Gal files/CFGv5.1_gene ID-fixedv2.txt", library, "CFG_V5.1v2", 6, true);
			// END - STEP 4
			
			
			System.out.println("Getting version 5.0");
			// STEP 4
			config = new ParserConfiguration();
			config.setBlockColumn(0);
			config.setCoordinateColumnX(1);  // row/column switched from 5.2
			config.setCoordinateColumnY(2);
			config.setNameColumn(4);
			parser = new GalFileParser();
			parser.setConfig(config);
			// parse the GAL File to create slide/block layouts
			parser.parse("doc/CFG MasterLists and GAL files/CFG Gal files/CFGv5-fixed2.gal", library, "CFG_V5.0", 6, false);
			// END - STEP 4
			
			System.out.println("Getting version 5.0");
			// STEP 4
			config = new ParserConfiguration();
			config.setBlockColumn(0);
			config.setCoordinateColumnX(1);  // row/column switched from 5.2
			config.setCoordinateColumnY(2);
			config.setNameColumn(4);
			parser = new GalFileParser();
			parser.setConfig(config);
			// parse the GAL File to create slide/block layouts
			parser.parse("doc/CFG MasterLists and GAL files/CFG Gal files/CFGv5-fixedv2.gal", library, "CFG_V5.0v2", 6, false);
			
			System.out.println("Getting version 4.2");
			// STEP 4
			config = new ParserConfiguration();
			config.setBlockColumn(-1);
			config.setMetaRow(0);
			config.setMetaColumn(1);
			config.setCoordinateColumnX(3);  // column
			config.setCoordinateColumnY(2);  // row
			config.setNameColumn(4);
			parser = new GalFileParser();
			parser.setConfig(config);
			// parse the txt File to create slide/block layouts
			parser.parse("doc/CFG MasterLists and GAL files/CFG Gal files/CFGv4.2_GeneID-fixed.txt", library, "CFG_V4.2", 6, true);
			// END - STEP 4
			
			System.out.println("Getting version 4.1");
			// STEP 4
			config = new ParserConfiguration();
			config.setBlockColumn(0);
			config.setCoordinateColumnX(1);  // column
			config.setCoordinateColumnY(2);  // row
			config.setNameColumn(4);
			parser = new GalFileParser();
			parser.setConfig(config);
			// parse the GAL File to create slide/block layouts
			parser.parse("doc/CFG MasterLists and GAL files/CFG Gal files/CFGv4.1 Gal file-fixed.gal", library, "CFG_V4.1", 6, false);
			// END - STEP 4
			
			System.out.println("Getting version 4.0");
			// STEP 4
			config = new ParserConfiguration();
			config.setBlockColumn(0);
			config.setCoordinateColumnX(1);  // column
			config.setCoordinateColumnY(2);  // row
			config.setNameColumn(3);
			parser = new GalFileParser();
			parser.setConfig(config);
			// parse the GAL File to create slide/block layouts
			parser.parse("doc/CFG MasterLists and GAL files/CFG Gal files/CFGv4.0 GAL-fixed.gal", library, "CFG_V4.0", 6, false);
			// END - STEP 4
			
			System.out.println("Getting version 3.2");
			// STEP 4
			config = new ParserConfiguration();
			config.setBlockColumn(0);
			config.setCoordinateColumnX(1);  // column
			config.setCoordinateColumnY(2);  // row
			config.setNameColumn(4);
			parser = new GalFileParser();
			parser.setConfig(config);
			// parse the GAL File to create slide/block layouts
			parser.parse("doc/CFG MasterLists and GAL files/CFG Gal files/CFG v3.2 GAL-fixed.gal", library, "CFG_V3.2", 12, false);
			// END - STEP 4
			
			System.out.println("Getting version 3.1");
			// STEP 4
			config = new ParserConfiguration();
			config.setBlockColumn(0);
			config.setCoordinateColumnX(1);  // column
			config.setCoordinateColumnY(2);  // row
			config.setNameColumn(3);
			parser = new GalFileParser();
			parser.setConfig(config);
			// parse the GAL File to create slide/block layouts
			parser.parse("doc/CFG MasterLists and GAL files/CFG Gal files/v3.1-fixed.gal", library, "CFG_V3.1", 6, false);
			// END - STEP 4
			
			System.out.println("Getting version 3.0");
			// STEP 4
			config = new ParserConfiguration();
			config.setBlockColumn(-1);
			config.setMetaRow(0);
			config.setMetaColumn(1);
			config.setCoordinateColumnX(3);  // column
			config.setCoordinateColumnY(2);  // row
			config.setNameColumn(4);
			parser = new GalFileParser();
			parser.setConfig(config);
			// parse the txt File to create slide/block layouts
			parser.parse("doc/CFG MasterLists and GAL files/CFG Gal files/v3.0 8x4x9x16 GeneID 013107-sorted-fixed.txt", library, "CFG_V3.0", 6, true);
			// END - STEP 4
			
		/*	System.out.println("Getting version 2.1");   //TODO how many replicates ??? name = 9 Sp8_100uM-P25
			// STEP 4
			config = new ParserConfiguration();
			config.setBlockColumn(-1);
			config.setMetaRow(0);
			config.setMetaColumn(1);
			config.setCoordinateColumnX(2);  // column
			config.setCoordinateColumnY(3);  // row
			config.setNameColumn(4);
			parser = new GalFileParser();
			parser.setConfig(config);
			// parse the txt File to create slide/block layouts
			parser.parse("doc/CFG MasterLists and GAL files/V2.1 8x4x16x9 GeneID linked 062506-sorted.txt", library, "CFG_V2.1", 60, true);
			// END - STEP 4
		*/
			// STEP 5
			// save the library
			LibraryUtils.saveLibrary(library, "lib/cfg_lib.xml");
			
			System.out.println("Exporting sequences");
			LibraryUtils.exportGlycans(library, "lib/sequences.txt", false);
			LibraryUtils.exportGlycans(library, "lib/failedsequences.txt", true);
			
			System.out.println("Printing Sequence parser results");
			try {
				File fout = new File("lib/map.txt");
				FileOutputStream fos = new FileOutputStream(fout);
			 
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
				for (String name: mparser.getCarbIdSequenceMap().keySet()) {
					bw.write(name);
					bw.write("\t" + mparser.getCarbIdSequenceMap().get(name));
					bw.newLine();
				}
				
				bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println("Printing linker results");
			try {
				File fout = new File("lib/linkers.txt");
				FileOutputStream fos = new FileOutputStream(fout);
			 
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
				for (String linker: mparser.getLinkerList().keySet()) {
					bw.write(linker);
					bw.write("\t" + mparser.getLinkerList().get(linker));
					bw.newLine();
				}
				
				bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		//	System.out.println("Exporting glycans into Excel");
		//	int noGlycans = LibraryUtils.exportGlycansToExcel(library, "doc/GlycansFromLibrary.xlsx");
		//	System.out.println("Could not generate image for " + noGlycans);
			
			// test parsing GPR file with "ArrayIt ImageGene format using GenePix parser
			System.out.println("Parsing GPR file for version 5.0");
			Layout slideLayout = new Layout();
			slideLayout.setId("2");
			slideLayout.setName("CFGv5.gal");
			FileWrapper fileWrapper = new FileWrapper ("/Users/sena/Desktop/GlycanArrayData/Glygen-CFGData/"
					+ "CFG Site Data-earlierVersions/A2_1to10_15381_V5.0_DATA.gpr.txt", "GenePix");
			Slide slide = new Slide();
			slide.setName("Slide 1");
			slide.setLayout(slideLayout);
			List<Block> blocks = LibraryInterface.getBlocksForLayout(library, "5");
			slide.setBlocks(blocks);
			GlycanArrayExperiment experiment = new GlycanArrayExperiment();
			experiment.addSlide (slide);
			experiment.setSignalType(ValueType.MEDIAN);
			experiment.setMethod(StatisticalMethod.ELIMINATE);
			experiment.setFileType("GenePix");
			experiment.setName("A2_1to10_15381_V5.0");
			GlycanArrayParserUtils.processGenePixFile(fileWrapper, experiment, slide);
			System.out.println("Parsed GPR File for version 5.0");
			
			errorOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	/**
	 * check if the slide layout name exists in the library
	 * @param library existing library
	 * @return the slide id if the slide name exists (if not, return -1)
	 */
	public static Integer getSlideLayoutNameExist (ArrayDesignLibrary library, String slideName) {
		int sId = -1;
		for (SlideLayout layout: library.getLayoutLibrary().getSlideLayout()) {
			if (layout.getName() != null && layout.getName().equals(slideName))
				sId = layout.getId();
		}
		return sId;
	}
	
	/**
	 * check if the block layout name exists in the library
	 * @param library existing library
	 * @return the block id if the block name exists (if not, return -1)
	 */
	public static Integer getBlockLayoutNameExist (ArrayDesignLibrary library, String slideName) {
		int bId = -1;
		for (BlockLayout layout: library.getLayoutLibrary().getBlockLayout()) {
			if (layout.getName() != null && layout.getName().equals(slideName))
				bId = layout.getId();
		}
		return bId;
	}
	
	
	

	private static void exportGlycans(ArrayDesignLibrary library, String filePath, boolean failedOnly) {
		try {
			File fout = new File(filePath);
			FileOutputStream fos = new FileOutputStream(fout);
		 
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		 
			for (Glycan glycan: library.getFeatureLibrary().getGlycan()) {
				if (failedOnly && (glycan.getSequence() == null || glycan.getSequence().isEmpty())) {
					bw.write(glycan.getName());
					if (glycan.getOrigSequence() != null) {
						bw.write("\t" + glycan.getOrigSequence().trim());
					} else {
						bw.write("\tNo Sequence");
					}
					bw.newLine();
				} else if (!failedOnly && glycan.getOrigSequence() != null) {
					bw.write(glycan.getName());
					bw.write("\t" + glycan.getOrigSequence().trim());
					bw.newLine();
				} else if (!failedOnly){
					bw.write(glycan.getName());
					bw.write("\tNo Sequence");
					bw.newLine();
				}
			}
			
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
