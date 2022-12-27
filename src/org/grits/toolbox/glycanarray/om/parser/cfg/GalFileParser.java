package org.grits.toolbox.glycanarray.om.parser.cfg;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Logger;
import org.grits.toolbox.glycanarray.library.om.ArrayDesignLibrary;
import org.grits.toolbox.glycanarray.library.om.feature.Feature;
import org.grits.toolbox.glycanarray.library.om.feature.Glycan;
import org.grits.toolbox.glycanarray.library.om.feature.GlycanProbe;
import org.grits.toolbox.glycanarray.library.om.feature.Linker;
import org.grits.toolbox.glycanarray.library.om.feature.Ratio;
import org.grits.toolbox.glycanarray.library.om.layout.Block;
import org.grits.toolbox.glycanarray.library.om.layout.BlockLayout;
import org.grits.toolbox.glycanarray.library.om.layout.LevelUnit;
import org.grits.toolbox.glycanarray.library.om.layout.SlideLayout;
import org.grits.toolbox.glycanarray.library.om.layout.Spot;
import org.grits.toolbox.glycanarray.om.model.UnitOfLevels;
import org.grits.toolbox.glycanarray.om.parser.ParserConfiguration;
import org.grits.toolbox.glycanarray.om.util.LibraryUtils;

public class GalFileParser {
	
	private static Logger logger = Logger.getLogger(GalFileParser.class);
	
	ParserConfiguration config;
	
	public void parse (String filePath, ArrayDesignLibrary library, String versionString, Integer numOfReplicates, boolean txtFileVersion) throws IOException {
		File file = new File(filePath);
		if (!file.exists())
			throw new FileNotFoundException(filePath + " does not exist!");
		
		Scanner scan = new Scanner(file);
		boolean dataStarts = false;
		String version="1.0";
		String type = "GenePix ArrayList V1.0";
		Integer prevBlockLocation = -1;
		Boolean first = false;
		Block block = null;
		int maxRow = 0;
		int maxColumn = 0;
		int groupId = 1;
		int maxGroup = 0;
		Map <String, Feature> glycanMap = new HashMap<>();
		Map <String, Integer> glycanGroupMap = new HashMap<>();
		Integer gIdCounter = LibraryUtils.getLastGlycanId(library)+1;
		Integer pIdCounter = LibraryUtils.getLastGlycanProbeId(library)+1;
		Integer fIdCounter = LibraryUtils.getLastFeatureId(library)+1;
		Integer lIdCounter = LibraryUtils.getLastLinkerId(library)+1;
		List<Glycan> glycanList = new ArrayList<>();
		List<Feature> featureList = new ArrayList<>();
		List<GlycanProbe> probeList = new ArrayList<>();
		List<Linker> linkerList = new ArrayList<>();
		List<BlockLayout> layoutList = new ArrayList<>();
		
		SlideLayout slideLayout = new SlideLayout();
		slideLayout.setName(versionString);
		if (filePath.lastIndexOf(File.separator) != -1)
			slideLayout.setDescription(filePath.substring(filePath.lastIndexOf(File.separator)+1));
		else 
			slideLayout.setDescription(filePath);
		
		slideLayout.setWidth(1);   // 1 dimensional by default
		//slideLayout.setDescription(type);
		slideLayout.setId(LibraryUtils.getLastSlideLayoutId(library)+1);
		
		int blockLayoutIdCounter = LibraryUtils.getLastBlockLayoutId(library)+1;
		
		//TODO determine the level unit
		LevelUnit levelUnit = new LevelUnit();
		levelUnit.setConcentration(100.0);
		levelUnit.setLevelUnit(UnitOfLevels.MICROMOL);
		
		List<LevelUnit> levels = new ArrayList<>();
		levels.add(levelUnit);
		
		BlockLayout blockLayout=null;
		
		Integer prevMetaRow = -1;
		Integer prevMetaColumn = -1;
		
		if (txtFileVersion) { // data starts immediately, meta row/meta column version
			dataStarts = true;
			first = true;
			prevMetaRow = 1;
			prevMetaColumn = 1;
		}
		
		while(scan.hasNext()){
	        String curLine = scan.nextLine();
	        String[] splitted = curLine.split("\t");
	        if (splitted.length == 0)
	        	continue;
	        String firstColumn = splitted[0].trim();
			// get the version
	        if (firstColumn.equals("ATF")) {
	        	version = splitted[1].trim();
	        }
	        if (firstColumn.contains("Type=")) {
	        	type = firstColumn.substring(firstColumn.indexOf("Type=")+5);
	        }
	        if (firstColumn.contains("BlockCount=")) {
	        	try {
	        		if (firstColumn.contains("\"BlockCount=")) {
		        		String blockCount = firstColumn.substring(firstColumn.indexOf("BlockCount=")+11);
		        		Integer height = Integer.parseInt(blockCount.substring(0, blockCount.length()-1));
		        		slideLayout.setHeight(height);
	        		} else { // no quotes at the beginning and end
	        			String blockCount = firstColumn.substring(firstColumn.indexOf("BlockCount=")+11);
	        			Integer height = Integer.parseInt(blockCount);
	        			slideLayout.setHeight(height);
	        		}
	        	} catch (NumberFormatException e) {
	        		slideLayout.setHeight(32);   // default
	        	}
	        }
	        String blockColumn = null;
	        if (config.getBlockColumn() != -1) {
	        	blockColumn = splitted[config.getBlockColumn()].trim();
		        if (blockColumn.indexOf("Block") != -1 && splitted.length > 1) {
		        	dataStarts = true;
		        	prevBlockLocation=1;
		        	first = true;
		        	continue; // skip to next line 
		        }
	        }
	        if (dataStarts) {
	        	try {
	        		if (blockColumn != null) {
		        		// firstColumn is the location of the block
		        		int blockLocation = Integer.parseInt(blockColumn.trim());
		        		if (prevBlockLocation != -1 && blockLocation != prevBlockLocation) { // go to next block
		        			// set the row/column/levels/groupnum for the previous block layout
		        			blockLayout.setColumnNum(maxColumn);
		        			blockLayout.setRowNum(maxRow);
		        			blockLayout.setLevelUnit(levels);
		        			//Integer numberOfGroups = blockLayout.getColumnNum() * blockLayout.getRowNum() / blockLayout.getReplicNum();
		        			blockLayout.setGroupNum(maxGroup);
		        			layoutList.add(blockLayout);
		        			
		        			// start a new one
		        			blockLayout = new BlockLayout();
		        			blockLayout.setId(blockLayoutIdCounter++);
		        			blockLayout.setName(versionString + "-" + blockLocation);
		        			blockLayout.setReplicNum(numOfReplicates);
		        			
		        			prevBlockLocation = blockLocation;
		        			block = new Block();
		        			block.setLayoutId(blockLayout.getId());
		        			block.setColumn(1);   // we assume one dimension for the slide
		        			block.setRow(blockLocation);
		        			slideLayout.getBlock().add(block);
		        			
		        			//re-init counters and maps
		        			glycanMap = new HashMap<>();
		        			glycanGroupMap = new HashMap<>();
		        			maxRow = 0;
		        			maxColumn = 0;
		        			maxGroup = 0;
		        			groupId = 1;
			     		}
		        		else if (first) {
		        			// first one
		        			blockLayout = new BlockLayout();
		        			blockLayout.setId(blockLayoutIdCounter++);
		        			blockLayout.setName(versionString + "-" + blockLocation);
		        			blockLayout.setReplicNum(numOfReplicates);
		        			
		        			block = new Block();
		        			block.setLayoutId(blockLayout.getId());
		        			block.setColumn(1);   // we assume one dimension for the slide
		        			block.setRow(blockLocation);
		        			slideLayout.getBlock().add(block);
		        			first = false;
		        		}
	        		} else { // txt file version
	        			String metaColumn = splitted[config.getMetaColumn()].trim();
	        			String metaRow = splitted[config.getMetaRow()].trim();
	        			int metaRowIdx = Integer.parseInt(metaRow);
	        			int metaColumnIdx = Integer.parseInt(metaColumn);
	        			// formula for block location - x * y + (x-1) (4 - y) => 4x + y - 4
	        			// x = 8, y= 4 should give 32, x=8, y=3 should give 31 etc.
        				int blockLocation = (4 * metaRowIdx) + metaColumnIdx - 4;
	        			if (prevMetaColumn != -1 && prevMetaRow != -1 &&
	        					(metaColumnIdx != prevMetaColumn || prevMetaRow != metaRowIdx)) {
	        				// new block
	        				blockLayout.setColumnNum(maxColumn);
		        			blockLayout.setRowNum(maxRow);
		        			blockLayout.setLevelUnit(levels);
		        			//Integer numberOfGroups = blockLayout.getColumnNum() * blockLayout.getRowNum() / blockLayout.getReplicNum();
		        			blockLayout.setGroupNum(maxGroup);
		        			layoutList.add(blockLayout);
		        			
		        			// start a new one
		        			blockLayout = new BlockLayout();
		        			blockLayout.setId(blockLayoutIdCounter++);
		        			blockLayout.setName(versionString + "-" + blockLocation);
		        			blockLayout.setReplicNum(numOfReplicates);
		        			
		        			prevMetaColumn = metaColumnIdx;
	        				prevMetaRow = metaRowIdx;
	        				
		        			block = new Block();
		        			block.setLayoutId(blockLayout.getId());
		        			block.setColumn(1);   // we assume one dimension for the slide
		        			block.setRow(blockLocation);
		        			slideLayout.getBlock().add(block);
		        			
		        			//re-init counters and maps
		        			glycanMap = new HashMap<>();
		        			glycanGroupMap = new HashMap<>();
		        			maxRow = 0;
		        			maxColumn = 0;
		        			maxGroup = 0;
		        			groupId = 1;
	        				
	        				prevMetaColumn = metaColumnIdx;
	        				prevMetaRow = metaRowIdx;
			     		}
		        		else if (first) {
		        			// first one
		        			blockLayout = new BlockLayout();
		        			blockLayout.setId(blockLayoutIdCounter++);
		        			blockLayout.setName(versionString + "-" + blockLocation);
		        			blockLayout.setReplicNum(numOfReplicates);
		        			
		        			block = new Block();
		        			block.setLayoutId(blockLayout.getId());
		        			block.setColumn(1);   // we assume one dimension for the slide
		        			block.setRow(blockLocation);
		        			slideLayout.getBlock().add(block);
		        			first = false;
		        		}
	        		}
	        		
	        		// second and third column are spot indices
	        		int x = Integer.parseInt(splitted[config.getCoordinateColumnX()].trim()); // column
	        		int y = Integer.parseInt(splitted[config.getCoordinateColumnY()].trim()); // row
	        		String glycanName = splitted[config.getNameColumn()].trim(); // name of the probe
	        		String probeName = glycanName;
	        		
	        		if (y > maxRow)
	        			maxRow = y;
	        		if (x > maxColumn)
	        			maxColumn = x;
	        		
	        		Spot spot = new Spot();
	        		spot.setX(x);
	        		spot.setY(y);
	        	
	        		blockLayout.getSpot().add(spot);
	        	
	        		if (glycanName.equals("0") || glycanName.equals("\"0\"") 
	        				|| glycanName.equalsIgnoreCase("empty") || glycanName.equalsIgnoreCase("\"empty\"")) {
	        				//|| glycanName.equalsIgnoreCase("Grid Marker") || glycanName.equalsIgnoreCase("\"Grid Marker\"")) {  //empty spots and grid markers
	        			spot.setFeatureId(null);    //what to set for empty spots ==> no feature assigned for the spot
	        		}
	        		else {
	        			if (glycanName.startsWith("\"")) {
	        				// remove the quotes
	        				glycanName = glycanName.substring(1, glycanName.length()-1);
	        				probeName = glycanName;
	        			} 
	        			glycanName = glycanName.trim();
	        			probeName = probeName.trim();
	        			if (glycanMap.get(probeName) != null) {
	        				// already created the feature
	        				spot.setFeatureId(glycanMap.get(probeName).getId());
	        				spot.setGroup(glycanGroupMap.get(probeName));
	        				spot.setConcentration(levelUnit);    
	        			} else {
	        				String letters = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
	        				Linker linker = null;
	        				String linkerName = null;
	        				String levelString = null;   //TODO parse this to add level to the probe
	        				if (letters.contains(glycanName.substring(0,1))) { // if glycanName does not start with a number, it does not follow the convention 234 Sp0
	        					// no glycan name  e.g. "Grid Marker" or "Human IgG"
	        					linkerName = glycanName;
	        					glycanName = "";	
	        				} else {
		        				//split name to find the linker
		        				String[] nameSplit = glycanName.split(" ");
		        				boolean splitRequired = false;
		        				if (nameSplit.length > 1) {
		        					linkerName = nameSplit[1];
		        					if (!letters.contains(linkerName.substring(0,1))) {
		        						//version 3.2 e.g. "260Sp0 100uM"
		        						glycanName = nameSplit[0];
		        						splitRequired = true;
		        						levelString = nameSplit[1]; // 100uM
		        						LevelUnit newLevel = addLevel (levelString, levels);
		        						if (newLevel != null) {
		        							levelUnit = newLevel;
		        						}
		        						
		        					}
		        				} else {
		        					splitRequired = true; // no space between glycanId and linker
		        				}
		        				if (splitRequired) { // glycanId + linker are concatenated without a space (older CFG versions, e.g. 260Sp0)
		        					String[] sp = LibraryUtils.splitGlycanIdAndLinker(glycanName);
		        					linkerName = sp[1];
		        					//glycanName = sp[0];
		        					glycanName = sp[0] + " " + sp[1];
		        					glycanName = glycanName.trim(); // in case of an empty linker
		        					probeName = sp[0] + " " + sp[1];
		        				}
	        				}
	        				
	        				// check again after splitting glycanName
	        				if (glycanMap.get(probeName) != null) {
		        				// already created the feature
		        				spot.setFeatureId(glycanMap.get(probeName).getId());
		        				spot.setGroup(glycanGroupMap.get(probeName));
		        				spot.setConcentration(levelUnit);    
	        				} else {	
	        					Linker alternateLinker = null;
	        					List<GlycanProbe> probes = new ArrayList<GlycanProbe>();
		        				if (linkerName != null && !linkerName.isEmpty()) {
			        				Set<Linker> existingList = LibraryUtils.getLinkerByName(library, linkerList, linkerName);
		        					if (existingList.isEmpty()) {
		        						// should not happen, all the linkers should already be loaded into the library
		        						logger.warn("Linker with name " + linkerName + " cannot be found in the library. Creating here...");
		        						System.err.println("Linker with name " + linkerName + " cannot be found in the library. Creating here...");
		        						linker = new Linker();
			        					linker.setId(lIdCounter++);
			        					linker.setName(linkerName);
			        					linkerList.add(linker);
		        					} else {
		        						linker = existingList.iterator().next();
		        						for(Linker l: existingList) {
		        							if (l != linker)
		        								alternateLinker = l;
		        						}		
		        					}
		        				}
		        				
		        				Feature feature = new Feature();
		        				feature.setId(fIdCounter++);
		        				feature.setName(probeName);
		        				spot.setConcentration(levelUnit);     // all the same concentration level
		        				spot.setFeatureId(feature.getId());
		        				
		        				if (groupId > maxGroup)
		        					maxGroup = groupId;
		        				
		        				spot.setGroup(groupId++);
		        				
		        				Glycan glycan = null;
			        			glycanGroupMap.put(probeName, spot.getGroup());
			        			glycanMap.put(probeName, feature);
			        			if (!glycanName.isEmpty()) {
			        				glycan = LibraryUtils.getGlycanByName(library, glycanList, glycanName);
			        				if (glycan == null) {
			        					// should not happen, all the glycans should already be loaded into the library
		        						logger.warn("Glycan with name " + glycanName + " cannot be found in the library. Creating here...");
		        						System.err.println("Glycan with name " + glycanName + " cannot be found in the library. Creating here...");
			        					glycan = new Glycan();
			        					glycan.setId(gIdCounter++);
			        					glycan.setName(glycanName);
			        					glycanList.add(glycan);
			        				}
		        				}
		        				
		        				GlycanProbe probe = LibraryUtils.getGlycanProbeByName(library, probeList, probeName);
		        				if (probe == null) {
			        				probe = new GlycanProbe();
			        				probe.setId(pIdCounter++);
			        				probe.setName(probeName);
			        				if (linker != null) probe.setLinker(linker.getId());
			        				if (glycan != null) {
				        				probe.setRatio(new ArrayList<>());
				        				Ratio probeRatio = new Ratio();
				        				probeRatio.setItemId(glycan.getId());
				        				probeRatio.setItemRatio(100);
				        				probe.getRatio().add(probeRatio);
			        				}
			        				probeList.add(probe);
		        				}
		        				probes.add(probe);
		        				
		        				if (alternateLinker != null) { // create another probe with the same glycan but alternateLinker
		        					probe = new GlycanProbe();
			        				probe.setId(pIdCounter++);
			        				probe.setName(probeName);
			        				if (linker != null) probe.setLinker(alternateLinker.getId());
			        				if (glycan != null) {
			        					probe.setRatio(new ArrayList<>());
				        				Ratio probeRatio = new Ratio();
				        				probeRatio.setItemId(glycan.getId());
				        				probeRatio.setItemRatio(100);
				        				probe.getRatio().add(probeRatio);
			        				}
			        				probeList.add(probe);
			        				probes.add(probe);
		        				}
		        				
		        				feature.setRatio(new ArrayList<>());
		        				for (GlycanProbe p: probes) {
		        					Ratio r = new Ratio();
		        					r.setItemId(p.getId());
		        					r.setItemRatio(100);
		        					feature.getRatio().add(r);
		        				}
		        				
		        				featureList.add(feature);
	        				}
	        			}
	        		}
	        		
	        	} catch (NumberFormatException e) {
	        		// should not occur
	        		logger.error("Value should have been a number", e);
	        		scan.close();
	        		throw new IOException("Value should have been a number: " + e.getMessage());
	        	}
	        }
	        		
		}
		
		scan.close();
		
		// add the last blockLayout
		blockLayout.setColumnNum(maxColumn);
		blockLayout.setRowNum(maxRow);
		blockLayout.setLevelUnit(levels);
		//Integer numberOfGroups = blockLayout.getColumnNum() * blockLayout.getRowNum() / blockLayout.getReplicNum();
		blockLayout.setGroupNum(maxGroup);
		if (txtFileVersion) {
			// height of the slide layout is not set
			if (slideLayout.getHeight() == null || slideLayout.getHeight() == 0) 
				slideLayout.setHeight(slideLayout.getBlock().size());
		}
		
		layoutList.add(blockLayout);
		
		
		// add the newly created layouts to the library
		library.getLayoutLibrary().getSlideLayout().add(slideLayout);
		library.getLayoutLibrary().getBlockLayout().addAll(layoutList);
		library.getFeatureLibrary().getFeature().addAll(featureList);
		library.getFeatureLibrary().getGlycan().addAll(glycanList);
		library.getFeatureLibrary().getGlycanProbe().addAll(probeList);
		library.getFeatureLibrary().getLinker().addAll(linkerList);
	}

	/**
	 * look for given level in the levels, if not exists, add and return, if exists return
	 * @param levelString 100uM, 10uM etc.
	 * @param levels existing levels
	 * @return added or found levelunit corresponding to the levelstring
	 */
	private LevelUnit addLevel(String levelString, List<LevelUnit> levels) {
		// parse level String to get the integer part and the unit part
		String numbers = "0123456789";
		String concentration ="";
		String unit = "";
		int i=0;
		while (i < levelString.length()) {
			if (numbers.contains(levelString.subSequence(i, i+1)))
				concentration += levelString.subSequence(i, i+1);
			else
				unit += levelString.subSequence(i, i+1);
			i++;
		}
		try {
			Double con = Double.parseDouble(concentration);
			UnitOfLevels unitLevel = UnitOfLevels.lookUp(unit);
			if (unit.equals("uM"))
				unitLevel = UnitOfLevels.MICROMOL;
			
			
			for (LevelUnit u: levels) {
				if (u.getConcentration().equals(con) && unitLevel != null && unitLevel.equals(u.getLevelUnit()))
					return u;
			}
			// does not exists, add
			if (unitLevel != null) {
				LevelUnit newLevelUnit = new LevelUnit();
				newLevelUnit.setConcentration(con);
				newLevelUnit.setLevelUnit(unitLevel);
				levels.add(newLevelUnit);
				return newLevelUnit;
			}
		} catch (NumberFormatException e) {
			return null;
		}
		
		return null;
	}

	public void setConfig(ParserConfiguration config) {
		this.config = config;
	}
	
	public ParserConfiguration getConfig() {
		return config;
	}
}
