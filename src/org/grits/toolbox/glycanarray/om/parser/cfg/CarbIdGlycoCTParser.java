package org.grits.toolbox.glycanarray.om.parser.cfg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class CarbIdGlycoCTParser {

	public static Map<String, String> parse (String filePath) throws EncryptedDocumentException, InvalidFormatException, IOException {
		Map<String, String> mapping = new HashMap<>();
		
		File file = new File(filePath);
		if (!file.exists())
			throw new FileNotFoundException(filePath + " does not exist!");
		
		//Create Workbook instance holding reference to .xls file
		Workbook workbook = WorkbookFactory.create(file);
        
		// get the sheet with glycoCT sequences
    	Sheet sheet = workbook.getSheetAt(1);
    	//Iterate through each row one by one
        Iterator<Row> rowIterator = sheet.iterator();
        if (rowIterator.hasNext())
        	rowIterator.next();      // skip the header line
        if (rowIterator.hasNext())
        	rowIterator.next();      // skip the header line
        while (rowIterator.hasNext()) {
        	Row row = rowIterator.next();
        	Cell carbIdCell = row.getCell(1);
        	Cell glycoCtCell = row.getCell(3);
        	String carbId = carbIdCell.getStringCellValue();
        	String glycoCT = glycoCtCell.getStringCellValue();
        	mapping.put(carbId, glycoCT);
        }
        
        workbook.close();
		
		return mapping;
	}
}
