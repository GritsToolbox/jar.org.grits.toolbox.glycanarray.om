package org.grits.toolbox.glycanarray.om.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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


public class ProscanParser implements IGlycanArrayExperimentParser {

	@Override
	public void parse(FileWrapper fileWrapper, GlycanArrayExperiment experiment,
			Slide slide) throws FileNotFoundException, IOException, InvalidFormatException {
		 //Create Workbook instance holding reference to .xls file
		Workbook workbook = WorkbookFactory.create(new File(fileWrapper.getName()));
        List<Block> blocks =  slide.getBlocks();
        
    	Sheet sheet = workbook.getSheetAt(0);
    	List<PowerLevel> powerLevels = new ArrayList<>();
    	//Iterate through each row one by one
        Iterator<Row> rowIterator = sheet.iterator();
        boolean dataStarts = false;
        boolean imagedataStarts = false;
        int prevBlockRowIndex = 1;
        int prevBlockColumnIndex = 1;
        int index = 0;
        int imageDataIndex = 0;
		Map<PowerLevel, MeasurementSet> measurementMap= null;
		Block block = null;
		List<MeasurementSet> sets = new ArrayList<>();
        while (rowIterator.hasNext())
        {
            Row row = rowIterator.next();
            if (row.cellIterator().hasNext()) {
            	Cell firstCell = row.getCell(0);
            	String firstColumn = "";
            	if (firstCell.getCellType() == CellType.STRING) {
            		firstColumn = firstCell.getStringCellValue();
            	}
            	if (firstColumn.equals("Scanner")) {
            		fileWrapper.setScanner(row.getCell(1).getStringCellValue());
            	}
            	if (firstColumn.equals("Laser Powers")) { // first one is for control, there might be 2 or 3 more power levels
            		if (row.getCell(1) != null) {
            			MeasurementSet set = new MeasurementSet();
            			PowerLevel powerLevel = new PowerLevel();
            			powerLevel.setPowerLevel(row.getCell(1).getNumericCellValue());
            			powerLevels.add(powerLevel);
            			set.setPowerLevel(powerLevel);
            			sets.add(set);
            			fileWrapper.addPowerLevel(powerLevel);
            		}
            		if (row.getCell(2) != null) {
            			MeasurementSet set = new MeasurementSet();
            			PowerLevel powerLevel = new PowerLevel();
            			powerLevel.setPowerLevel(row.getCell(2).getNumericCellValue());
            			powerLevels.add(powerLevel);
            			set.setPowerLevel(powerLevel);
            			sets.add(set);
            			fileWrapper.addPowerLevel(powerLevel);
            		}
            		if (row.getCell(3) != null) {
            			MeasurementSet set = new MeasurementSet();
            			PowerLevel powerLevel = new PowerLevel();
            			powerLevel.setPowerLevel(row.getCell(3).getNumericCellValue());
            			powerLevels.add(powerLevel);
            			set.setPowerLevel(powerLevel);
            			sets.add(set);
            			fileWrapper.addPowerLevel(powerLevel);
            		}
            		if (row.getCell(4) != null) {
            			MeasurementSet set = new MeasurementSet();
            			PowerLevel powerLevel = new PowerLevel();
            			powerLevel.setPowerLevel(row.getCell(4).getNumericCellValue());
            			powerLevels.add(powerLevel);
            			set.setPowerLevel(powerLevel);
            			sets.add(set);
            			fileWrapper.addPowerLevel(powerLevel);
            		}
            	}
            	if (firstColumn.equals("PMT Voltages")) { // associate each one with the correct power level
            		if (row.getCell(1) != null) {
            			if (sets != null && sets.size() > 0) {
            				MeasurementSet set = sets.get(0);
            				if (set != null) {
            					set.getPowerLevel().setPmtGain(row.getCell(1).getNumericCellValue());
            				}
            			}
            		}
            		if (row.getCell(2) != null) {
            			if (sets != null && sets.size() > 1) {
            				MeasurementSet set = sets.get(1);
            				if (set != null) {
            					set.getPowerLevel().setPmtGain(row.getCell(2).getNumericCellValue());
            				}
            			}
            		}
            		if (row.getCell(3) != null) {
            			if (sets != null && sets.size() > 2) {
            				MeasurementSet set = sets.get(2);
            				if (set != null) {
            					set.getPowerLevel().setPmtGain(row.getCell(3).getNumericCellValue());
            				}
            			}
            		}
            		if (row.getCell(4) != null) {
            			if (sets != null && sets.size() > 3) {
            				MeasurementSet set = sets.get(3);
            				if (set != null) {
            					set.getPowerLevel().setPmtGain(row.getCell(4).getNumericCellValue());
            				}
            			}
            		}
            	}
            	if (firstColumn.equals("BEGIN IMAGE INFO")) {
            		imagedataStarts = true;
            		row = rowIterator.next(); // skip this line
            		row = rowIterator.next(); // skip the next line since that contains headers only
            	}
        
            	if (firstColumn.equals("BEGIN DATA")) {
            		dataStarts = true;
            		imagedataStarts = false;
            		row = rowIterator.next(); // skip this line
            		row = rowIterator.next(); // skip the next line since that contains headers only
            	}
            }
            
            if (imagedataStarts) {
            	Iterator<Cell> cellIterator = row.cellIterator();
            	while (cellIterator.hasNext()) {
            		Cell cell = cellIterator.next();
            		if (cell.getColumnIndex() == 3) {
            			if (imageDataIndex == 0) { // first one is for control
            				if (powerLevels.size() > 0) {
	            				PowerLevel powerLevel = powerLevels.get(0);
	            				if (powerLevel != null) {
	            					powerLevel.setFlourophore(cell.getStringCellValue());
	            					fileWrapper.addFlourophore(cell.getStringCellValue());
	            				}
            				}
            			}
            			else {
            				if (powerLevels.size() > imageDataIndex) {
	            				PowerLevel powerLevel = powerLevels.get(imageDataIndex);
	            				if (powerLevel != null) {
	            					powerLevel.setFlourophore(cell.getStringCellValue());
	            					fileWrapper.addFlourophore(cell.getStringCellValue());
	            				}
            				}
            			}
            		}
            	}
            	imageDataIndex ++;
            }
            
            if (dataStarts) {
            	Iterator<Cell> cellIterator = row.cellIterator();
            	int x=-1;
            	int y=-1;
            	double coordX = -1.0;
            	Feature feature = null;
            	Well spotLocation=null;
            	SpotData spotLayoutData = null;
            	Coordinate coordinates = null;
            	SpotData data = null;
            	Integer fPixels=null;
            	Integer bPixels=null;
            	Integer flags = null;
            	while (cellIterator.hasNext()) {
            		Cell cell = cellIterator.next();
            		switch (cell.getColumnIndex()) {
            		case 1:   // array row - block row
            			double rowIndex = cell.getNumericCellValue();
            			if ((int)rowIndex != prevBlockRowIndex) { // new block
            				prevBlockRowIndex = (int)rowIndex;
            				prevBlockColumnIndex = 1;
            				index ++;
            			}
            			
            			if (index >= blocks.size())   // should not happen if the number of blocks in the layout matches the number of blocks in the data file
    	        			break;
    	        		block = blocks.get(index);
    	        		if (block.getMeasurementSetMap() == null) {
    	        			measurementMap = new HashMap<>();
    	        			block.setMeasurementSetMap(measurementMap);
    	        			initializeMeasurementSets (block, sets);
    	        		} else {
    	        			measurementMap = block.getMeasurementSetMap();
    	        			initializeMeasurementSets (block, sets);
    	        		}
    	        	/*	if (block.getPosition() == null)
    	        			block.setPosition(new Well(1, (int)rowIndex ));
    	        		else
    	        			block.getPosition().setY((int)rowIndex);*/
    	        		
            			break;
            		case 2:   // array column - block column
            			double columnIndex = cell.getNumericCellValue();
            			if ((int)columnIndex != prevBlockColumnIndex) { // new block
            				prevBlockColumnIndex = (int)columnIndex;
            				index++; 
            				if (index >= blocks.size())   // should not happen if the number of blocks in the layout matches the number of blocks in the data file
        	        			break;
        	        		block = blocks.get(index);
        	        		if (block.getMeasurementSetMap() == null) {
        	        			measurementMap = new HashMap<>();
        	        			block.setMeasurementSetMap(measurementMap);
        	        			initializeMeasurementSets (block, sets);
        	        		} else {
        	        			measurementMap = block.getMeasurementSetMap();
        	        			initializeMeasurementSets (block, sets);
        	        		}
            			}
            			
            			
            		/*	if (block != null) { // should always be true
            				block.getPosition().setX((int)columnIndex);
            			} */
            			break;
            		case 3:  // spot row 
            			y = (int) cell.getNumericCellValue();
            			break;
            		case 4:  // spot column
            			x = (int) cell.getNumericCellValue();
            			
            			if (x != -1) {
            				spotLocation = new Well(x, y);
            				if (block.getLayoutData() == null) {
        	        			workbook.close();
        	        			throw new IOException ("There must be a problem with slide layout: block does not have any assigned spots");
        	        		}
        	        		spotLayoutData = block.getLayoutData().get(spotLocation);
        	        		if (spotLayoutData == null) {
        	        			workbook.close();
        	        			throw new IOException ("Slide layout does not match with the data file");
        	        		}
        	        		feature = spotLayoutData.getFeature();
            			}
            			break;
            		case 7:  // x coordinate
            			coordX = cell.getNumericCellValue();
            			break;
            		case 8:  // y coordinate
            			double coordY = cell.getNumericCellValue();
            			coordinates = new Coordinate(coordX, coordY);
            			//data.setCoordinates(coordinates);
            			break;
            		case 9: // diameter
            			double diameter = cell.getNumericCellValue();
            			coordinates.setDiameter(diameter);
            			break;
            		case 10: // fPixels
            			fPixels = (int) cell.getNumericCellValue();
            			break;
            		case 11: // bPixels
            			bPixels = (int) cell.getNumericCellValue();
            			break;
            		case 13:
            			flags = (int) cell.getNumericCellValue();
            			break;
            		case 14: // Ch median
            		case 26:
            		case 38:
            		case 50:
            			MeasurementSet set=null;
            			data = new SpotData();
    	        		data.setPosition(spotLocation);
    	        		data.setConcentration(spotLayoutData.getConcentration());
    	        		data.setProbeLevelUnit(spotLayoutData.getProbeLevelUnit());
    	        		data.setMedian(cell.getNumericCellValue());
    	        		data.setCoordinates(coordinates);
    	        		data.setfPixels(fPixels);
    	        		data.setbPixels(bPixels);
    	        		data.setFlags(flags);
    	        		data.setFeature(feature);
    	        		feature.setGroupId(spotLayoutData.getGroup());
    	        		data.setGroup(spotLayoutData.getGroup());
            			if (cell.getColumnIndex() == 14 && powerLevels.size() > 0) {	
            				// control
            				set = block.getMeasurementSetMap().get(powerLevels.get(0));
            			} else if (cell.getColumnIndex() == 26 && powerLevels.size() > 1) {	
            				set = block.getMeasurementSetMap().get(powerLevels.get(1));
            			} else if (cell.getColumnIndex() == 38 && powerLevels.size() > 2) {	
            				set = block.getMeasurementSetMap().get(powerLevels.get(2));
            			} else if (cell.getColumnIndex() == 50 && powerLevels.size() > 3) {	
            				set = block.getMeasurementSetMap().get(powerLevels.get(3));
            			}
            			
            			if (set == null) {
            				// no more power levels, skip the rest of the cells
            				continue;
            			}
            			
            			if (set.getDataMap() == null) 
    	        			set.setDataMap(new HashMap<Well, SpotData>());
    	        		set.getDataMap().put(spotLocation, data);
        				
        			/*	Measurement measurement=null;
        				if (set.getFeatures() == null)
        					set.setFeatures(new ArrayList<>());
        				if (!set.getFeatures().contains(feature))
        					set.getFeatures().add(feature);
        				if (feature.getMeasurement() == null) {
        					measurement = new Measurement();
        					feature.setMeasurement(measurement);
        				} else {
        					measurement = feature.getMeasurement();
        				}*/
        				
        				Measurement measurement=null;
        				if (set.getMeasurementMap() == null) {
        					set.setMeasurementMap(new HashMap<Feature, Measurement>());
        					measurement = new Measurement();
        					set.getMeasurementMap().put(feature, measurement);
        				} else {
        					measurement = set.getMeasurementMap().get(feature);
        					if (measurement == null) {
        						measurement = new Measurement();
        						set.getMeasurementMap().put(feature, measurement);
        					}
        				}
        				measurement.setStatisticalMethod(experiment.getMethod());
    	        		measurement.setValueType(experiment.getSignalType());
        				measurement.addData(data);
        				
            			break;
            		case 15: // CH mean
            		case 27:
            		case 39:
            		case 51:
            			data.setMean(cell.getNumericCellValue());
            			break;
            			
            		case 16: // CH StDev
            		case 28:
            		case 40:
            		case 52:
            			data.setStdev(cell.getNumericCellValue());
            			break;
            			
            		case 17: // CH B Median
            		case 29:
            		case 41:
            		case 53:
            			data.setbMedian(cell.getNumericCellValue());
            			break;
            			
            		case 18: // CH B Mean
            		case 30:
            		case 42:
            		case 54:
            			data.setbMean(cell.getNumericCellValue());
            			break;
            			
            		case 19: // CH B StDev
            		case 31:
            		case 43:
            		case 55:
            			data.setbStDev(cell.getNumericCellValue());
            			break;
            			
            		case 20: //Ch1 % > B + 1 SD
            		case 32:
            		case 44:
            		case 56:
            			data.setPercentageOneSD(cell.getNumericCellValue());
            			break;
            			
            		case 21: // Ch1 % > B + 2 SD
            		case 33:
            		case 45:
            		case 57:
            			data.setPercentageTwoSD(cell.getNumericCellValue());
            			break;
            			
            		case 22: // Ch1 F % Sat.
            		case 34:
            		case 46:
            		case 58:
            			data.setPercentageSaturated(cell.getNumericCellValue());
            			break;
            			
            		case 23: // CH Median-B
            		case 35:
            		case 47:
            		case 59:
            			data.setMedianMinusB(cell.getNumericCellValue());
            			break;
            			
            		case 24: // CH Mean-B
            		case 36:
            		case 48:
            		case 60:
            			data.setMeanMinusB(cell.getNumericCellValue());
            			break;
            			
            		case 25: // SNR
            		case 37:
            		case 49:
            		case 61:
            			data.setSnRatio(cell.getNumericCellValue());
            			break;
            			
            		default:
            			break;
            		}
            	}
            	
            }      	
        }
        
        //split features into two if there are duplicates within the block
        if ( powerLevels.size() > 3) {	
			GlycanArrayParserUtils.splitNonAdjacentFeatures(block.getMeasurementSetMap().get(powerLevels.get(3)));
			GlycanArrayParserUtils.splitNonAdjacentFeatures(block.getMeasurementSetMap().get(powerLevels.get(2)));
			GlycanArrayParserUtils.splitNonAdjacentFeatures(block.getMeasurementSetMap().get(powerLevels.get(1)));
			GlycanArrayParserUtils.splitNonAdjacentFeatures(block.getMeasurementSetMap().get(powerLevels.get(0)));
		} else if ( powerLevels.size() > 2) {	
			GlycanArrayParserUtils.splitNonAdjacentFeatures(block.getMeasurementSetMap().get(powerLevels.get(2)));
			GlycanArrayParserUtils.splitNonAdjacentFeatures(block.getMeasurementSetMap().get(powerLevels.get(1)));
			GlycanArrayParserUtils.splitNonAdjacentFeatures(block.getMeasurementSetMap().get(powerLevels.get(0)));
		} else if ( powerLevels.size() > 1) {	
			GlycanArrayParserUtils.splitNonAdjacentFeatures(block.getMeasurementSetMap().get(powerLevels.get(1)));
			GlycanArrayParserUtils.splitNonAdjacentFeatures(block.getMeasurementSetMap().get(powerLevels.get(0)));
		} else if (powerLevels.size() > 0) {	
			GlycanArrayParserUtils.splitNonAdjacentFeatures(block.getMeasurementSetMap().get(powerLevels.get(0)));
		}  
        workbook.close();

	}

	private void initializeMeasurementSets(Block block,
			List<MeasurementSet> sets) {
		Map<PowerLevel, MeasurementSet> measurementMap = block.getMeasurementSetMap();
		for (MeasurementSet measurementSet : sets) {
			MeasurementSet copy = new MeasurementSet();
			copy.setPowerLevel(measurementSet.getPowerLevel());
			if (measurementMap.get(measurementSet.getPowerLevel()) == null)  // only add if previously not there
				measurementMap.put(measurementSet.getPowerLevel(), copy);
		}
		
	}
}
