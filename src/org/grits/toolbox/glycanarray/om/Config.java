package org.grits.toolbox.glycanarray.om;

public class Config {
	
	public static final String COMBO_SEQUENCE_SEPARATOR = "~|~";
	// Labels used in the tables/text boxex etc.
	public static final String POWERLEVEL = "Scan Power";
	public static final String GLYCANCONCENTRATION="Probe Concentration";
	public static final String GLYCANCONCENTRATIONLOW = "Lo";
	public static final String GLYCANCONCENTRATIONHIGH = "Hi";
	public static final String[] GLYCANCONCENTRATIONUNIT = new String[] {"fmol", "mM", "uM", "ug/ml"};
	
	// should go into the preferences
	public static final String[] FLOUROPHORES = new String[] {"Alexa 647", "Cyanine 3", "Cyanine 5"};
	public static final String PROSCANFILETYPE = "Proscan";
	public static final String GENEPIXFILETYPE = "GenePix";
	public static final String[] FILETYPES = new String[]{GENEPIXFILETYPE, PROSCANFILETYPE};
}
