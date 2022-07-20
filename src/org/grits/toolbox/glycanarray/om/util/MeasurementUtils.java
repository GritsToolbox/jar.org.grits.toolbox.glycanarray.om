package org.grits.toolbox.glycanarray.om.util;

import java.util.List;

import org.grits.toolbox.glycanarray.om.model.SpotData;
import org.grits.toolbox.glycanarray.om.model.StatisticalMethod;
import org.grits.toolbox.glycanarray.om.model.ValueType;


public class MeasurementUtils {

	
	/**
	 * This method calculates the resulting value for a feature by getting the value (based on the valueType) from all the replicated spots 
	 * based on the selected statistical method
	 * @return
	 */
	public static Double calculateValue (StatisticalMethod statisticalMethod, List<SpotData> data, ValueType valueType) {
		Double value = 0.0;
		if (statisticalMethod.equals(StatisticalMethod.AVERAGE)) {
			value = calculateAverage(data, valueType);
		} else if (statisticalMethod.equals(StatisticalMethod.ELIMINATE)) {
			value = calculateAverage(data, valueType);
			value = calculateEliminate(data, valueType, value);
		}
		return value;
	}

	private static Double calculateEliminate(List<SpotData> data, ValueType valueType, Double average) {
		Double value = 0.0;
		Double absMax = 0.0;
		Double absMaxSpotVal = 0.0;
		Double absMin = 0.0;
		Double absMinSpotVal = 0.0;
		Double valForCal = 0.0;
		Double result = 0.0;
		
		if (data != null) {
			int count = 0;
			valForCal = 0.0;
			for (SpotData spotData : data) {
				value = getValue(spotData, valueType);
				
				if(average > value) {
					if((average - value) > absMin ) {
						absMin = average - value;
						absMinSpotVal = value;
					}
				}else {
					if((value - average) > absMax ) {
						absMax = value - average;
						absMaxSpotVal = value;
					}
				}				

				count ++;
			}		
			
			//return null for less than 2 items (no mean to calculate elimination)
			if(count < 3)
				return null;
			
			for (SpotData spotData : data) {
				value = getValue(spotData, valueType);
				if(value != absMaxSpotVal || value != absMinSpotVal)
					valForCal += value;
			}
			result = valForCal / (count-2);
		}

		return result;
	}
	

	/** 
	 * find an average of the values for the given list of spot data using the values for the given ValueType
	 * @param data list of values to average
	 * @param valueType type of value to retrieve from the SpotData
	 * @return the average of the values
	 */
	private static Double calculateAverage(List<SpotData> data, ValueType valueType) {
		Double value = 0.0;
		Double average = 0.0;
		if (data != null) {
			int count = 0;
			for (SpotData spotData : data) {
				value += getValue(spotData, valueType);
				count++;
			}
			if(count != 0)
				average = value / count;
		}
		return average;
	}
	
	public static Double calculateStDev(StatisticalMethod statisticalMethod, List<SpotData> dataList, ValueType valueType) {
		Double value = 0.0;
		if (statisticalMethod.equals(StatisticalMethod.AVERAGE))
			value = calculateAverageStDev(dataList, valueType);
		else if (statisticalMethod.equals(StatisticalMethod.ELIMINATE))
			value = calculateEliminateStDev(dataList, valueType);
		return value;
	}
	
	
	private static Double calculateEliminateStDev(List<SpotData> dataList, ValueType valueType) {
		Double average = 0.0;
		Double absMax = 0.0;
		Double absMaxSpotVal = 0.0;
		Double absMin = 0.0;
		Double absMinSpotVal = 0.0;
		Double elimination = 0.0;
		Double value = 0.0;
		
		int total = 0;
		for (SpotData spotData : dataList) {
			value = getValue(spotData, valueType);
			average += value;
			total++;
		}
		average = average / total;
		
		for (SpotData spotData : dataList) {
			value = getValue(spotData, valueType);
			
			if(average > value) {
				if((average - value) > absMin ) {
					absMin = average - value;
					absMinSpotVal = value;
				}
			}else {
				if((value - average) > absMax ) {
					absMax = value - average;
					absMaxSpotVal = value;
				}
			}				

		}		
			
		//return null for less than 2 items (no mean to calculate elimination)
		if(total < 3)
			return null;
		
		elimination = 0.0;
		for (SpotData spotData : dataList) {
			value = getValue(spotData, valueType);
			if(value != absMaxSpotVal || value != absMinSpotVal)
				elimination += value;
		}
		elimination = elimination / (total-2);
		
		Double deviation = 0.0;
		// find the deviations
		int i=0;
		for (SpotData spotData : dataList) {
			value = getValue (spotData, valueType);
			if(value != absMaxSpotVal || value != absMinSpotVal)
				deviation += Math.pow(value - elimination, 2);
		}
		if (total > 2)
			deviation = deviation/(total-2);
		
		return Math.sqrt(deviation);	
	}

	/**
	 * calculate the standard deviation for the given list of spot data using the values for the provided valueType
	 * @param dataList
	 * @param valueType
	 * @return
	 */
	private static Double calculateAverageStDev(List<SpotData> dataList, ValueType valueType) {
		double average = 0;
		int total = 0;
		for (SpotData spotData : dataList) {
			Double val = getValue(spotData, valueType);
			average += val;
			total++;
		}
		average = average / total;
		double[] deviations = new double[total];
		// find the deviations
		int i=0;
		for (SpotData spotData : dataList) {
			Double val = getValue (spotData, valueType);
			deviations[i++] = Math.pow(val - average, 2);
		}
		double variance=0;
		for (int j = 0; j < deviations.length; j++) {
			variance += deviations[j];
		}
		if (total > 1)
			variance = variance/(total-1);
		return Math.sqrt(variance);	
	}

	/**
	 * returns the value for a spot for the given valueType
	 * @param spotData
	 * @param valueType
	 * @return
	 */
	private static Double getValue(SpotData spotData, ValueType valueType) {
		Double val = 0.0;
		switch(valueType) {
		case MEAN:
			return spotData.getMean();
		case MEDIAN:
			return spotData.getMedian();
		case MEANB:
			return spotData.getMeanMinusB();
		case MEDIANB:
			return spotData.getMedianMinusB();
		case MEANB_S:
			val = spotData.getMean() - spotData.getbMean();
			return val;
		case MEDIANB_S:
			val = spotData.getMedian() - spotData.getbMedian();
			return val;
		default: 
			return spotData.getMean();
		}
	};

}