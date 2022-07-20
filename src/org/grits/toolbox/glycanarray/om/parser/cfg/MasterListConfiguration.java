package org.grits.toolbox.glycanarray.om.parser.cfg;

public class MasterListConfiguration {
	
	Integer sheetNumber=0;
	Integer masterListColumn=-1;
	Integer structureColumn=-1;
	Integer carbIdColumn=-1;
	
	public Integer getSheetNumber() {
		return sheetNumber;
	}
	public void setSheetNumber(Integer sheetNumber) {
		this.sheetNumber = sheetNumber;
	}
	public Integer getMasterListColumn() {
		return masterListColumn;
	}
	public void setMasterListColumn(Integer masterListColumn) {
		this.masterListColumn = masterListColumn;
	}
	public Integer getStructureColumn() {
		return structureColumn;
	}
	public void setStructureColumn(Integer structureColumn) {
		this.structureColumn = structureColumn;
	}
	public Integer getCarbIdColumn() {
		return carbIdColumn;
	}
	public void setCarbIdColumn(Integer carbIdColumn) {
		this.carbIdColumn = carbIdColumn;
	}
}
