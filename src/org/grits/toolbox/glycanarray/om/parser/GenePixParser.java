package org.grits.toolbox.glycanarray.om.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.grits.toolbox.glycanarray.om.model.Block;
import org.grits.toolbox.glycanarray.om.model.Coordinate;
import org.grits.toolbox.glycanarray.om.model.Feature;
import org.grits.toolbox.glycanarray.om.model.FileWrapper;
import org.grits.toolbox.glycanarray.om.model.GlycanArrayExperiment;
import org.grits.toolbox.glycanarray.om.model.Measurement;
import org.grits.toolbox.glycanarray.om.model.MeasurementSet;
import org.grits.toolbox.glycanarray.om.model.PowerLevel;
import org.grits.toolbox.glycanarray.om.model.Slide;
import org.grits.toolbox.glycanarray.om.model.SpotData;
import org.grits.toolbox.glycanarray.om.model.Well;
import org.grits.toolbox.glycanarray.om.util.GlycanArrayParserUtils;

public class GenePixParser implements IGlycanArrayExperimentParser {
	
	private static Logger logger = Logger.getLogger(GenePixParser.class);
	
	ParserConfiguration config;

	@Override
	public void parse(FileWrapper fileWrapper, GlycanArrayExperiment experiment,
			Slide slide) throws IOException {
		// tab separated file
		File file = new File(fileWrapper.getName());
		if (!file.exists())
			throw new FileNotFoundException(fileWrapper.getName() + " does not exist!");
		
		Scanner scan = new Scanner(file);
		boolean dataStarts = false;
		boolean tableStarts = false;
		List<Block> blocks =  slide.getBlocks();
		int index = 0;
		int prevBlockLocation=-1;
		PowerLevel power = new PowerLevel();
		Double pmtGain = null;
		Map<PowerLevel, MeasurementSet> measurementMap= null;
		MeasurementSet set = null;
		Block block = null;
		boolean first = false;
		String wavelength = "";
		
		int prevMetaColumn = -1;
		int prevMetaRow = -1;
		
		
		//Yukie: get the column and row numbers from slide layout
		List<Slide> expSlides = experiment.getSlides();
		Integer slideRow = -1;
		Integer slideCol = -1;
		for (Slide expSlide : expSlides) {
			if(expSlide.getName().equals(slide.getName())) {	//find the slide layout for this scan data
				List<Block> tmpBlocks = expSlide.getBlocks();
				for(Block tmpBlock : tmpBlocks) {
					String bName = tmpBlock.getName();			//bName like 'Block 1-1' and 'Block 2-8'
					
					Pattern p = Pattern.compile("\\d+");
					Matcher m = p.matcher(bName);

					int valRowCol = -1;
					int i = 0;
					while(m.find()) {
						valRowCol = Integer.valueOf(m.group());
						if(i==0 && valRowCol > slideRow)	//the first number = Row
							slideRow = valRowCol;		//find the max row = row size					
						
						if(i==1 && valRowCol > slideCol)	//the second number = Column
							slideCol = valRowCol;		//find the max column = column size
						i++;
					}
				}
			}
		}
		
		
	    while(scan.hasNext()){
	        String curLine = scan.nextLine();
	        String[] splitted = curLine.split("\t");
	        if (splitted.length == 0)
	        	continue;
	        if (curLine.toLowerCase().contains("end raw data"))
	        	continue;
	        String firstColumn = splitted[0].trim();
	        if (firstColumn.indexOf("Creator") != -1) {
	        	if(firstColumn.lastIndexOf("\"") != -1) {
	        		fileWrapper.setScanner(firstColumn.substring(firstColumn.indexOf("=")+1, firstColumn.lastIndexOf("\"")));
	        	} else {
	        		fileWrapper.setScanner(firstColumn.substring(firstColumn.indexOf("=")+1));
	        	}
	        }
	        if (firstColumn.indexOf("PMTGain") != -1) {
	        	if(firstColumn.lastIndexOf("\"") != -1) {
	        		pmtGain = Double.parseDouble(firstColumn.substring(firstColumn.indexOf("=")+1, firstColumn.lastIndexOf("\"")));
	        	} else {
	        		pmtGain = Double.parseDouble(firstColumn.substring(firstColumn.indexOf("=")+1));
	        	}
	        }
	        if (firstColumn.indexOf("Wavelengths") != -1) {
	        	if(firstColumn.lastIndexOf("\"") != -1) {
	        		wavelength = firstColumn.substring(firstColumn.indexOf("=")+1, firstColumn.lastIndexOf("\""));
	        	} else {
	        		wavelength = firstColumn.substring(firstColumn.indexOf("=")+1);
	        	}
	        }
	        if (firstColumn.indexOf("ScanPower") != -1) {
	        	String powerLevel = null;
	        	if(firstColumn.lastIndexOf("\"") != -1) {
		        	powerLevel = firstColumn.substring(firstColumn.indexOf("=")+1, firstColumn.lastIndexOf("\""));	        		
	        	} else {
		        	powerLevel = firstColumn.substring(firstColumn.indexOf("=")+1);	        		
	        	}
	        	power = new PowerLevel();
	        	power.setPowerLevel(Double.parseDouble(powerLevel));
	        	power.setFlourophore(wavelength); //fileWrapper.getFlourophore());
	        	power.setPmtGain(pmtGain);
	        	fileWrapper.addPowerLevel(power);
	        	fileWrapper.setPowerLevel(powerLevel);
	        }
	        if (firstColumn.indexOf("Begin Raw Data") != -1) {
	        	tableStarts = true;
	        }
	        String blockColumn = null;
	        if (config.getBlockColumn() == -1) {
	        	if (splitted.length > config.getMetaColumn()) { 
		        	String metaColumn = splitted[config.getMetaColumn()].trim();
		        	if (tableStarts && metaColumn.indexOf("Meta") != -1) {
		        		dataStarts = true;
		        		prevMetaColumn = 1;
		        		prevMetaRow = 1;
		        		first = true;
		        		continue;
		        	}
	        	}
	        } else {
		        blockColumn = splitted[config.getBlockColumn()].trim();
		        if (blockColumn.indexOf("Block") != -1) {
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
		        		int blockLocation = (int)Double.parseDouble(blockColumn.trim());
		        		if (prevBlockLocation != -1 && blockLocation != prevBlockLocation) { // go to next block
		        			index++;
		        			prevBlockLocation = blockLocation;
		        			set = new MeasurementSet();
		        			set.setPowerLevel(power);
			     		}
		        		else if (first) {
		        			// first one
		        			set = new MeasurementSet();
		        			set.setPowerLevel(power);
		        			first = false;
		        		}
		        		if (index >= blocks.size())   // should not happen if the number of blocks in the layout matches the number of blocks in the data file
		        			break;
		        		block = blocks.get(index);
		        		if (block.getMeasurementSetMap() == null) {
		        			measurementMap = new HashMap<>();
		        			block.setMeasurementSetMap(measurementMap);
		        			measurementMap.put(set.getPowerLevel(), set);
		        		} else {
		        			measurementMap = block.getMeasurementSetMap();
		        			if (measurementMap.get(power) == null)
		        				measurementMap.put(set.getPowerLevel(), set);
		        		}
		        		
		        		// block identifier 1-X : changed for a customised slide layout
		        		int colInSlide = ((blockLocation-1) % slideCol)+1;
		        		int rowInSlide = ((int) Math.floor((blockLocation-1)/slideCol))+1;
	
		        		block.setPosition(new Well(colInSlide, rowInSlide));
	        		} else {   // using meta column and meta row
	        			String metaColumn = splitted[config.getMetaColumn()].trim();
	        			String metaRow = splitted[config.getMetaRow()].trim();
	        			int metaRowIdx = (int)Double.parseDouble(metaRow);
	        			int metaColumnIdx =(int) Double.parseDouble(metaColumn);
	        			if (prevMetaColumn != -1 && prevMetaRow != -1 &&
	        					(metaColumnIdx != prevMetaColumn || prevMetaRow != metaRowIdx)) {
	        				// new block
	        				index ++;
	        				prevMetaColumn = metaColumnIdx;
	        				prevMetaRow = metaRowIdx;
	        				set = new MeasurementSet();
		        			set.setPowerLevel(power);
			     		}
		        		else if (first) {
		        			// first one
		        			set = new MeasurementSet();
		        			set.setPowerLevel(power);
		        			first = false;
		        		}
	        			
	        			if (index >= blocks.size())   // should not happen if the number of blocks in the layout matches the number of blocks in the data file
		        			break;
		        		block = blocks.get(index);
		        		if (block.getMeasurementSetMap() == null) {
		        			measurementMap = new HashMap<>();
		        			block.setMeasurementSetMap(measurementMap);
		        			measurementMap.put(set.getPowerLevel(), set);
		        		} else {
		        			measurementMap = block.getMeasurementSetMap();
		        			if (measurementMap.get(power) == null)
		        				measurementMap.put(set.getPowerLevel(), set);
		        		}
	        			// set position for one dimensional 32 blocks (1 x 32) instead of (4 x 8) as indicated by meta row/column
	        			block.setPosition(new Well(1, index+1));
	        		}
	        		
	        		
	        		
//	        		if (blockLocation % 2 == 0) {
	        			// second column in the slide
//	        			block.setPosition(new Well(2, (int)Math.ceil((double)blockLocation/2) ));
//	        		}
//	        		else {
	        			// first column in the slide
//	        			block.setPosition(new Well(1, (int)Math.ceil((double)blockLocation/2)));
//	        		}
	        		
	        		// second and third column are spot indices
	        		int x = (int)Double.parseDouble(splitted[config.positionColumnX].trim()); // column
	        		int y = (int)Double.parseDouble(splitted[config.positionColumnY].trim()); // row
	        		Well spotLocation = new Well(x, y);
	        		if (block.getLayoutData() == null) {
	        			scan.close();
	        			throw new IOException ("There must be a problem with slide layout: block does not have any assigned spots");
	        		}
	        		SpotData spotData = block.getLayoutData().get(spotLocation);
	        		
	        		//if (spotData == null) {
	        		//	scan.close();
	        		//	throw new IOException ("Slide layout does not match with the data file");
	        		//}
	        		double coordX = Double.parseDouble(splitted[config.coordinateColumnX].trim());
	        		double coordY = Double.parseDouble(splitted[config.coordinateColumnY].trim());
	        		Coordinate coordinates = new Coordinate(coordX, coordY);
	        		coordinates.setDiameter(Double.parseDouble(splitted[config.diameterColumn].trim()));
	        		SpotData data = new SpotData();
	        		data.setPosition(spotLocation);
	        		if(spotData != null && spotData.getGroup() != null)
	        			data.setGroup(spotData.getGroup());
	        		data.setCoordinates(coordinates);
	        		data.setMedian(Double.parseDouble(splitted[config.medianColumn].trim()));
	        		data.setMean(Double.parseDouble(splitted[config.meanColumn].trim()));
	        		data.setStdev(Double.parseDouble(splitted[config.stdevColumn].trim()));
	        		data.setbMedian(Double.parseDouble(splitted[config.bMedianColumn].trim()));
	        		data.setbMean(Double.parseDouble(splitted[config.bMeanColumn].trim()));
	        		data.setbStDev(Double.parseDouble(splitted[config.bStDevColumn].trim()));
	        		if (config.medianMinusBColumn != -1)
	        			data.setMedianMinusB(Double.parseDouble(splitted[config.medianMinusBColumn].trim()));
	        		if (config.meanMinusBColumn != -1)
	        			data.setMeanMinusB(Double.parseDouble(splitted[config.meanMinusBColumn].trim()));
	        		if (config.flagsColumn != -1)
	        			data.setFlags((int)Double.parseDouble(splitted[config.flagsColumn].trim()));
	        		if (config.fPixelsColumn != -1)
	        			data.setfPixels((int)Double.parseDouble(splitted[config.fPixelsColumn].trim()));
	        		if (config.bPixelsColumn != -1)
	        			data.setbPixels((int)Double.parseDouble(splitted[config.bPixelsColumn].trim()));
		        	if (config.percentageOneSDColumn != -1)
		        		data.setPercentageOneSD(Double.parseDouble(splitted[config.percentageOneSDColumn].trim()));
	        		if (config.percentageTwoSDColumn != -1)
	        			data.setPercentageTwoSD(Double.parseDouble(splitted[config.percentageTwoSDColumn].trim()));
	        		if (config.percentageSaturatedColumn != -1)
	        			data.setPercentageSaturated(Double.parseDouble(splitted[config.percentageSaturatedColumn].trim()));
	        		if (config.signalToNoiseColumn != -1)
	        			data.setSnRatio(Double.parseDouble(splitted[config.signalToNoiseColumn]));
	        		//comment-out by yukie for CarbArrayART 1st release
	        		//if (config.totalIntensityColumn != -1)
	        			//data.setTotalIntensity(Double.parseDouble(splitted[config.totalIntensityColumn]));
	        		
	        		if (set.getDataMap() == null) 
	        			set.setDataMap(new HashMap<Well, SpotData>());
	        		set.getDataMap().put(spotLocation, data);
	        		
	        		Feature feature = new Feature();
	        		if(spotData != null && spotData.getFeature() != null)
	        			feature = spotData.getFeature();
	        		
	        		if(feature != null && feature.getId() != null){
	        			feature.setGroupId(spotData.getGroup());
		        		if (set.getMeasurementMap() == null) {
		        			set.setMeasurementMap(new HashMap<Feature, Measurement>());
		        		}
		        		Measurement measurement = set.getMeasurementMap().get(feature);
		        		if (measurement == null) {
		        			measurement = new Measurement();
		        			set.getMeasurementMap().put(feature, measurement);
		        		}
		        		
		        		data.setFeature(feature);
			        	data.setConcentration(spotData.getConcentration());
			        	data.setProbeLevelUnit(spotData.getProbeLevelUnit());

		        		measurement.setStatisticalMethod(experiment.getMethod());
		        		measurement.setValueType(experiment.getSignalType());
		        		measurement.addData(data);
	        		}
	        		
	        	} catch (NumberFormatException e) {
	        		// should not occur
	        		logger.error("Value should have been a number", e);
	        		scan.close();
	        		throw new IOException("Value should have been a number: " + e.getMessage());
	        	}
	        }
	       
	    }
	   //Comment-out by Yukie for CarbArrayART 1st release 
	    /*if (set == null || set.getMeasurementMap() == null) {
	    	scan.close();
	    	throw new IOException ("This is not a valid file, could not locate the data table");
	    }
	    
	    GlycanArrayParserUtils.splitNonAdjacentFeatures(set);
	    scan.close();
	    */
	    if (index+1 == blocks.size()) {
    		GlycanArrayParserUtils.splitNonAdjacentFeatures(set);
    		scan.close();
    	}else {
    		logger.error("Block numbers in GPR file do not match with the selected layout");
    	    scan.close();
			throw new IOException("Block numbers in GPR file do not match with the selected layout.");
    	}
	}

	public void setConfig(ParserConfiguration config) {
		this.config = config;
	}
	
	public ParserConfiguration getConfig() {
		return config;
	}
}
