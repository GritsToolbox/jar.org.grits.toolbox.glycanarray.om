package org.grits.toolbox.glycanarray.om.parser;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.grits.toolbox.glycanarray.om.model.FileWrapper;
import org.grits.toolbox.glycanarray.om.model.GlycanArrayExperiment;
import org.grits.toolbox.glycanarray.om.model.Slide;


public interface IGlycanArrayExperimentParser {

	void parse(FileWrapper file, GlycanArrayExperiment experiment, Slide slide) throws FileNotFoundException, IOException, InvalidFormatException;
}
