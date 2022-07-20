package org.grits.toolbox.glycanarray.om.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.poi.ss.examples.AddDimensionedImage;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;

public class ExcelWriterHelper extends AddDimensionedImage {
	/**
	 * 
	 * Writes the given image object into a cell in the given workbook and sheet 
	 * at the given cell identified by row and column indexes
	 * 
	 * @see {@link org.apache.poi.ss.examples.AddDimensionedImage.addImageToSheet()}
	 * @param a_workbook Excel workbook
	 * @param a_sheet sheet to use
	 * @param a_iRowNum row number for the cell
	 * @param a_iColNum column number for the cell
	 * @param a_img image to add to the given cell
	 * @param a_imgs array of images to put the newly generated excel picture into (to be used for resizing the images later)
	 */
	public void writeCellImage(Workbook a_workbook, Sheet a_sheet, int a_iRowNum, int a_iColNum, 
			BufferedImage a_img, List<Picture> a_imgs) throws Exception {

		if ( a_iColNum < 0 || a_img == null )
			return;

		Drawing drawing = a_sheet.createDrawingPatriarch();
		double imageWidthMM = ConvertImageUnits.widthUnits2Millimetres(ConvertImageUnits.pixel2WidthUnits(a_img.getWidth()));
		double imageHeightMM = ConvertImageUnits.widthUnits2Millimetres(ConvertImageUnits.pixel2WidthUnits(a_img.getHeight()));

		ClientAnchorDetail colClientAnchorDetail = this.fitImageToColumns(a_sheet, a_iColNum,
				imageWidthMM, AddDimensionedImage.EXPAND_ROW_AND_COLUMN);
		ClientAnchorDetail rowClientAnchorDetail = this.fitImageToRows(a_sheet, a_iRowNum,
				imageHeightMM, AddDimensionedImage.EXPAND_ROW_AND_COLUMN);

		ClientAnchor anchor = a_workbook.getCreationHelper().createClientAnchor();

		anchor.setDx1(0);
		anchor.setDy1(0);
		anchor.setDx2(colClientAnchorDetail.getInset());
		anchor.setDy2(rowClientAnchorDetail.getInset());
		anchor.setCol1(colClientAnchorDetail.getFromIndex());
		anchor.setRow1(rowClientAnchorDetail.getFromIndex());
		anchor.setCol2(colClientAnchorDetail.getToIndex());
		anchor.setRow2(rowClientAnchorDetail.getToIndex());

		anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		javax.imageio.ImageIO.write(a_img,"png",bos);

		int index = a_sheet.getWorkbook().addPicture(bos.toByteArray(), Workbook.PICTURE_TYPE_PNG);
		a_imgs.add( drawing.createPicture(anchor, index) );
	}

}
