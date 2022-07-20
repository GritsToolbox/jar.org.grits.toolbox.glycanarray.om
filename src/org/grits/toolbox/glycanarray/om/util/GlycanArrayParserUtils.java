package org.grits.toolbox.glycanarray.om.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.grits.toolbox.glycanarray.om.model.Feature;
import org.grits.toolbox.glycanarray.om.model.FileWrapper;
import org.grits.toolbox.glycanarray.om.model.GlycanArrayExperiment;
import org.grits.toolbox.glycanarray.om.model.Measurement;
import org.grits.toolbox.glycanarray.om.model.MeasurementSet;
import org.grits.toolbox.glycanarray.om.model.Slide;
import org.grits.toolbox.glycanarray.om.model.SpotData;
import org.grits.toolbox.glycanarray.om.parser.GenePixParser;
import org.grits.toolbox.glycanarray.om.parser.ParserConfiguration;
import org.grits.toolbox.glycanarray.om.parser.ProscanParser;


/**
 * This utility class contains common utility methods used throughout the plugin to extract some useful information from a 
 * GlycanArrayExperiment object
 * 
 * @author sena
 *
 */
public class GlycanArrayParserUtils {
	/**
	 * Parse the given genepix file to update the given experiment and slide
	 * @param fileWrapper
	 * @param experiment
	 * @param slide to be updated from the contents in the file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void processGenePixFile(FileWrapper fileWrapper, GlycanArrayExperiment experiment, Slide slide) throws FileNotFoundException, IOException {
		// tab separated file
		File file = new File(fileWrapper.getName());
		if (!file.exists())
			throw new FileNotFoundException(fileWrapper.getName() + " does not exist!");
		
		String version="1.0";
		String type="GenePix Results 3";
		Scanner scan = new Scanner(file);
		
		boolean gprColNumCheck = false;
		String gprColNum = "";
		boolean arrayItVersionCheck = false;
		
	    while(scan.hasNext()){
	        String curLine = scan.nextLine();
	        String[] splitted = curLine.split("\t");
	        if (splitted.length == 0)
	        	continue;
	        String firstColumn = splitted[0].trim();
	        
	        if(gprColNumCheck){
	        	gprColNum = splitted[1].trim();
	        	gprColNumCheck = false;
	        }
	        
	        if (arrayItVersionCheck) {
	        	if (splitted.length > 2 && splitted[2] != null) {
	        		version = splitted[2].trim();
	        		// no need to continue
	        		break;
	        	}
	        }
	        // get the version
	        if (firstColumn.equals("ATF")) {
	        	version = splitted[1].trim();
	        	gprColNumCheck = true;
	        }
	        
	        if (firstColumn.equalsIgnoreCase("Begin Header")) {
	        	type = "ArrayIt";
	        	arrayItVersionCheck = true;
	        }

	        if (firstColumn.contains("Type=")) {
	        	type = firstColumn.substring(firstColumn.indexOf("Type=")+5);
	        	break; // no need to continue after getting the type
	        } 
	    }
	    scan.close();
	    
	    //TODO
	    GenePixParser parser = new GenePixParser();
	    parser.setConfig(getConfigFor(version, type, gprColNum));
	    parser.parse(fileWrapper, experiment, slide);
	}
	
	/**
	 * handle different versions of GenePix result files
	 * @param version
	 * @param type
	 * @return a configuration based on the given version and type of the GenePix file
	 */
	public static ParserConfiguration getConfigFor (String version, String type, String gprColNum) {
		ParserConfiguration config= new ParserConfiguration();
		if ((version.equals("1.0") || version.equals("1")) && type.toLowerCase().contains("genepix results 3")) {
			config.setBlockColumn(0);
			config.setPositionColumnX(1);
			config.setPositionColumnY(2);
			config.setCoordinateColumnX(5);
			config.setCoordinateColumnY(6);
			config.setDiameterColumn(7);
			config.setMedianColumn(8);
			config.setMeanColumn(9);
			config.setStdevColumn(10);
			config.setbMedianColumn(13);
			config.setbMeanColumn(14);
			config.setbStDevColumn(15);
			config.setPercentageOneSDColumn(17);
			config.setPercentageTwoSDColumn(18);
			config.setPercentageSaturatedColumn(19);
			
			if(gprColNum.equals("40")){
				config.setfPixelsColumn(27);
				config.setbPixelsColumn(28);
				config.setMedianMinusBColumn(33);
				config.setMeanMinusBColumn(34);
				config.setSignalToNoiseColumn(36);
				config.setFlagsColumn(37);
			}else{
				config.setfPixelsColumn(20);
				config.setbPixelsColumn(21);
				config.setMedianMinusBColumn(23);
				config.setMeanMinusBColumn(24);
				config.setSignalToNoiseColumn(26);
				config.setFlagsColumn(27);	
			}			
		}
		else if ((version.equals("1.0") || version.equals("1")) && type.toLowerCase().contains("genepix export 3")) {
			config.setBlockColumn(3);
			config.setPositionColumnX(4);
			config.setPositionColumnY(5);
			config.setCoordinateColumnX(8);
			config.setCoordinateColumnY(9);
			config.setDiameterColumn(10);
			config.setMedianColumn(11);
			config.setMeanColumn(12);
			config.setStdevColumn(13);
			config.setbMedianColumn(16);
			config.setbMeanColumn(17);
			config.setbStDevColumn(18);
			config.setPercentageOneSDColumn(20);
			config.setPercentageTwoSDColumn(21);
			config.setPercentageSaturatedColumn(22);
			config.setfPixelsColumn(37);
			config.setbPixelsColumn(38);
			config.setMedianMinusBColumn(33);
			config.setMeanMinusBColumn(34);
			config.setSignalToNoiseColumn(24);
			config.setFlagsColumn(0);
		}
		else if ((version.equals("1.0") || version.equals("1")) && type.toLowerCase().contains("genepix results 2")) {
			config.setBlockColumn(0);
			config.setPositionColumnX(1);
			config.setPositionColumnY(2);
			config.setCoordinateColumnX(5);
			config.setCoordinateColumnY(6);
			config.setDiameterColumn(7);
			config.setMedianColumn(17);
			config.setMeanColumn(18);
			config.setStdevColumn(19);
			config.setbMedianColumn(20);
			config.setbMeanColumn(21);
			config.setbStDevColumn(22);
			config.setPercentageOneSDColumn(23);
			config.setPercentageTwoSDColumn(24);
			config.setPercentageSaturatedColumn(25);
			config.setfPixelsColumn(65);
			config.setbPixelsColumn(66);
			config.setMedianMinusBColumn(73);
			config.setMeanMinusBColumn(77);
			config.setSignalToNoiseColumn(-1);  // does not exist in this version
			config.setFlagsColumn(80);
		} else if (type.equalsIgnoreCase("ArrayIt") && (version.equals("5.6.1") || version.equals("6.1.0"))) {
			config.setBlockColumn(-1);
			config.setMetaColumn(3);
			config.setMetaRow(2);
			config.setPositionColumnX(5);
			config.setPositionColumnY(4);
			config.setCoordinateColumnX(26);
			config.setCoordinateColumnY(27);
			config.setDiameterColumn(28);
			config.setMedianColumn(10);
			config.setbMedianColumn(11);
			config.setMeanColumn(8);
			config.setbMeanColumn(9);
			config.setStdevColumn(18);
			config.setbStDevColumn(19);
			// does not exist in this version
			config.setPercentageOneSDColumn(-1);
			config.setPercentageTwoSDColumn(-1);
			config.setPercentageSaturatedColumn(-1);
			config.setfPixelsColumn(-1);
			config.setbPixelsColumn(-1);
			config.setMedianMinusBColumn(-1);
			config.setMeanMinusBColumn(-1);
			config.setSignalToNoiseColumn(-1);  
			config.setFlagsColumn(-1);
		}
		/**TODO
		 * Check if RatioFormulations exists -> new version for reading the Genepix
		 */
		return config;
	}

	/**
	 * Parse a proscan file to update the experiment and slide 
	 * @param fileWrapper
	 * @param experiment
	 * @param slide to be updated with the contents from the file
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static void processProscanFile(FileWrapper fileWrapper, GlycanArrayExperiment experiment, Slide slide) throws FileNotFoundException, IOException, InvalidFormatException {
		new ProscanParser().parse(fileWrapper, experiment, slide);
	}
	
	/**
	 * This utility method is used to make sure each feature has replicate "spot" values from adjacent cells only, by checking the groupIds of each spot, recursively
	 * @param set that contains the measurement data
	 */
	public static void splitNonAdjacentFeatures(MeasurementSet set) {
		Set<Feature> features = set.getMeasurementMap().keySet();
		Map<Feature, Measurement> toBeAdded = new HashMap<>();
		boolean split = false;
		for (Feature feature : features) {
			List<SpotData> toBeRemoved = new ArrayList<>();
			Measurement measurement = set.getMeasurementMap().get(feature);
			List<SpotData> dataList = measurement.getData();
			Integer groupId = null;
			Feature newFeature = null;
			Measurement newMeasurement = null;
			for (SpotData spotData : dataList) {
				if (groupId == null)
					groupId = spotData.getGroup();
				if (!spotData.getGroup().equals(groupId)) {
					split = true;
					if (newFeature == null) {
						newFeature = new Feature();
						newFeature.setName(feature.getName());
						newFeature.setId(feature.getId());
						newFeature.setProbeId(feature.getProbeId());
						newFeature.setSequences(feature.getSequences());
						newFeature.setGroupId(spotData.getGroup());
						newMeasurement = new Measurement();
						newMeasurement.setStatisticalMethod(measurement.getStatisticalMethod());
						newMeasurement.setValueType(measurement.getValueType());	
					}
					spotData.setFeature(newFeature);
					// add these to the new measurement
					newMeasurement.addData(spotData);
					toBeRemoved.add(spotData);
				} else {
					// set the group id for the feature in any case
					feature.setGroupId(groupId);
					spotData.getFeature().setGroupId(groupId);
				}
			}
			if (!toBeRemoved.isEmpty()) {
				for (SpotData spotData : toBeRemoved) {
					measurement.removeData(spotData);
				}
			}
			if (newFeature != null) {
				toBeAdded.put(newFeature, newMeasurement);
			}
		}
		
		if (!toBeAdded.isEmpty()) {
			for (Map.Entry<Feature, Measurement> entry : toBeAdded.entrySet()) {
				set.getMeasurementMap().put(entry.getKey(), entry.getValue());
			}
		}
		
		if (!split)   // nothing to split
			return;
		else // try to split again
			splitNonAdjacentFeatures(set);
	}
}
