package org.grits.toolbox.glycanarray.library.om;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.grits.toolbox.glycanarray.library.om.annotation.ProbeMetadata;
import org.grits.toolbox.glycanarray.library.om.feature.Classification;
import org.grits.toolbox.glycanarray.library.om.feature.Feature;
import org.grits.toolbox.glycanarray.library.om.feature.FeatureLibrary;
import org.grits.toolbox.glycanarray.library.om.feature.Glycan;
import org.grits.toolbox.glycanarray.library.om.feature.GlycanProbe;
import org.grits.toolbox.glycanarray.library.om.feature.Linker;
import org.grits.toolbox.glycanarray.library.om.feature.Ratio;
import org.grits.toolbox.glycanarray.library.om.layout.BlockLayout;
import org.grits.toolbox.glycanarray.library.om.layout.LayoutLibrary;
import org.grits.toolbox.glycanarray.library.om.layout.LevelUnit;
import org.grits.toolbox.glycanarray.library.om.layout.SlideLayout;
import org.grits.toolbox.glycanarray.library.om.layout.Spot;
import org.grits.toolbox.glycanarray.om.model.Block;
import org.grits.toolbox.glycanarray.om.model.GlycanMoiety;
import org.grits.toolbox.glycanarray.om.model.Layout;
import org.grits.toolbox.glycanarray.om.model.SpotData;
import org.grits.toolbox.glycanarray.om.model.Well;


public class LibraryInterface {
	static final Logger logger = Logger.getLogger(LibraryInterface.class);
	
	/**
	 * Add a new data into SlideLayout
	 * @param library
	 */
	public static List<org.grits.toolbox.glycanarray.om.model.Layout> addSlideLayouts (ArrayDesignLibrary library, String slideName, String slideId){
		List<Layout> layoutList = new ArrayList<Layout>();
		
		Layout layout = new Layout();
		layout.setName(slideName);
		layout.setId(slideId);
		layoutList.add(layout);
		
		return layoutList;
	}
	
	public static List<Layout> getSlideLayouts (ArrayDesignLibrary library) {
		if (library == null)
			return null;
		List<Layout> layoutList = new ArrayList<Layout>();
		LayoutLibrary layoutLibrary = library.getLayoutLibrary();
		for (SlideLayout slideLayout : layoutLibrary.getSlideLayout()) {
			Layout layout = new Layout();
			layout.setId(slideLayout.getId() + "");
			layout.setName(slideLayout.getName());
			layoutList.add(layout);
		}
		Collections.sort(layoutList);
		return layoutList;
	}	
	
	public static List<SlideLayout> getSlideLayoutList(ArrayDesignLibrary library) {
		if (library == null) return null;
		
		List<SlideLayout> slidelayoutList = new ArrayList<SlideLayout>();
		slidelayoutList = library.getLayoutLibrary().getSlideLayout();
	
		return slidelayoutList;
	}
	
	public static List<Block> getBlocksForLayout (ArrayDesignLibrary library, String slideLayoutId) {
		if (library == null)
			return null;
		List<Block> blockList = new ArrayList<Block>();
		LayoutLibrary layoutLibrary = library.getLayoutLibrary();
		for (SlideLayout slideLayout : layoutLibrary.getSlideLayout()) {
			if (slideLayout.getId().equals(Integer.parseInt(slideLayoutId))) {
				for (org.grits.toolbox.glycanarray.library.om.layout.Block block : slideLayout.getBlock()) {
					Block myBlock = new Block();
					myBlock.setPosition(new Well(block.getColumn(), block.getRow()));
					myBlock.setName("Block " + myBlock.getPosition());
					Map<Well, SpotData> spotDataList = new HashMap<>();
					Integer blockLayoutId = block.getLayoutId();
					BlockLayout blockLayout = LibraryInterface.getBlockLayout(library, blockLayoutId);
					if (blockLayout == null) 
						return null;
					myBlock.setBlockLayoutName(blockLayout.getName());
					//List<LevelUnit> levelUnits = blockLayout.getLevelUnit();
					for (Spot spot: blockLayout.getSpot()) {
						SpotData spotData = new SpotData();
						Well spotLocation = new Well(spot.getX(), spot.getY());
						spotData.setPosition(spotLocation);
												
						Feature feature = LibraryInterface.getFeature(library, spot.getFeatureId());
						String comment = "";
						if (feature != null) {
							org.grits.toolbox.glycanarray.om.model.Feature myFeature = new org.grits.toolbox.glycanarray.om.model.Feature();
							myFeature.setId(spot.getFeatureId());
							myFeature.setName(feature.getName());
							
							List<GlycanProbe> probes = new ArrayList<>();
							for (Ratio ratio : feature.getRatio()) {
								List<GlycanProbe> glycanProbeList = LibraryInterface.getGlycanProbe(library);
								for(GlycanProbe t_probe : glycanProbeList) {
									if(t_probe.getId().equals(ratio.getItemId())) {
										probes.add(t_probe);
										myFeature.setProbeId(t_probe.getId());
										break;
									}
								}
							}
							
							List<Classification> classifications = new ArrayList<Classification>();
							for(GlycanProbe probe : probes) {
								classifications.addAll(probe.getClassification());
								
								if(!comment.isEmpty())
									comment += "~|~";

								if(probe.getComment() != null)
									comment = comment + probe.getComment();
							}							
							
							List<org.grits.toolbox.glycanarray.om.model.Glycan> sequences = new ArrayList<>();
							if (probes != null) {
								for (GlycanProbe glycanProbe : probes) {
									List <GlycanMoiety> glycanMoieties = new ArrayList<>();
									if(glycanProbe.getRatio() != null) {
										for (Ratio ratio : glycanProbe.getRatio()) {	
											if (ratio != null) {	
												GlycanMoiety moietyObject = new GlycanMoiety();
												moietyObject.setGlycanId(ratio.getItemId());
												Glycan glyS = getGlycan(library, ratio.getItemId());
												if (glyS != null) {
													
													if(glyS.getSequence() != null) {
														moietyObject.setSequence(glyS.getSequence());
													}
													
													if(glyS.getOrigSequence() != null) {
														moietyObject.setOrinalSequence(glyS.getOrigSequence());
													}
													
													if(glyS.getFilterSetting() != null){
														moietyObject.setFilterSetting(glyS.getFilterSetting());
													}
													
													if(glyS.getGlyTouCanId() != null){
														moietyObject.setGlyTouCanID(glyS.getGlyTouCanId());
													}
												}
												glycanMoieties.add(moietyObject);
											} 
										}
									} else {
										logger.warn ("There is no glycan moiety.");
									}

									org.grits.toolbox.glycanarray.om.model.Glycan glycanObject = new org.grits.toolbox.glycanarray.om.model.Glycan();
									glycanObject.setGlycanMoieties(glycanMoieties);
									
									Linker linker = LibraryInterface.getLinker(library, glycanProbe.getLinker());
				
									org.grits.toolbox.glycanarray.om.model.Linker linkerObject = new org.grits.toolbox.glycanarray.om.model.Linker();
									if (linker != null) {
										linkerObject.setLinkerName(linker.getName());
										linkerObject.setLinkerSequence(linker.getSequence());
										glycanObject.setLinker(linkerObject);
									}
									List<org.grits.toolbox.glycanarray.om.model.Classification> classificationList = new ArrayList<>();
									for (Classification classification : classifications) {
										org.grits.toolbox.glycanarray.om.model.Classification classifier = new org.grits.toolbox.glycanarray.om.model.Classification();
										classifier.setName(classification.getClassifierId());
										classifier.setValue(classification.getValue());
										classificationList.add(classifier);
									}
									glycanObject.setClassifications(classificationList);
									glycanObject.setComment(comment);
									glycanObject.setProbeId(glycanProbe.getId());

									sequences.add(glycanObject);
									
								}
							}
							myFeature.setSequences(sequences);
							spotData.setFeature(myFeature);

							spotData.setConcentration(spot.getConcentration().getConcentration());
							spotData.setProbeLevelUnit(spot.getConcentration().getLevelUnit());

						}
						
						spotData.setGroup(spot.getGroup());
						spotDataList.put(spotLocation,spotData);
					}
					
					myBlock.setLayoutData(spotDataList);
					blockList.add(myBlock);
				}
			}
		}
		return blockList;
	}
	
	public static BlockLayout getBlockLayout (ArrayDesignLibrary library, Integer blockLayoutId) {
		if (library == null)
			return null;
		for (BlockLayout layout : library.getLayoutLibrary().getBlockLayout()) {
			if (layout.getId().equals(blockLayoutId)) {
				return layout;
			}
		}
		
		return null;
	}
	
	public static Feature getFeature (ArrayDesignLibrary library, Integer featureId) {
		if (library == null)
			return null;
		for (Feature feature: library.getFeatureLibrary().getFeature()) {
			if (feature.getId().equals(featureId)) 
				return feature;
		}
		
		return null;
	}
	
	public static List<Feature> getFeatures (ArrayDesignLibrary library) {
		if (library == null)
			return null;
		List<Feature> featureList = new ArrayList<Feature>();
		FeatureLibrary featureLib = library.getFeatureLibrary();
		for (Feature feature: featureLib.getFeature()) {
			Feature probe = new Feature();
			probe.setId(feature.getId());
			probe.setName(feature.getName());
//			probe.setProbe(feature.getProbe());
			probe.setRatio(feature.getRatio());

			featureList.add(probe);
		}
		return featureList;
	}
	
	
	
	
	public static Glycan getGlycan (ArrayDesignLibrary library, Integer glycanId) {
		if (library == null)
			return null;
		for (Glycan glycan: library.getFeatureLibrary().getGlycan()) {
				if (glycan.getId().equals(glycanId)) 
					return glycan;
		}
		
		return null;
	}
	
	public static Linker getLinker (ArrayDesignLibrary library, Integer linkerId) {
		if (library == null)
			return null;
		for (Linker linker: library.getFeatureLibrary().getLinker()) {
			if (linker.getId().equals(linkerId)) 
				return linker;
		}
		
		return null;
	}
	
	
	
	
	public static List<Linker> getLinker (ArrayDesignLibrary library) {
		if (library == null)
			return null;
		
		List<Linker> linkerList = new ArrayList<Linker>();
		linkerList = library.getFeatureLibrary().getLinker();
		
		return linkerList;
	}
	
	
	public static List<GlycanProbe> getGlycanProbe (ArrayDesignLibrary library) {
		if (library == null)
			return null;
		
		List<GlycanProbe> glycanProbeList = new ArrayList<GlycanProbe>();
		glycanProbeList = library.getFeatureLibrary().getGlycanProbe();
		
		return glycanProbeList;
	}

	
	
/*	public static List<ProbeMetadata> getProbeMeta (ArrayDesignLibrary library) {
		if (library == null) return null;

		List<ProbeMetadata> pmList = new ArrayList<ProbeMetadata>();
		pmList = library.getSupplierLibrary().getProbeMeta();
		
		return pmList;
	}
*/
	
	
	public static List<Glycan> getGlycan(ArrayDesignLibrary library) {
		if (library == null) return null;
		
		List<Glycan> glycanList = new ArrayList<Glycan>();
		glycanList = library.getFeatureLibrary().getGlycan();
		
		return glycanList;
	}
	

	public static List<Layout> getBlockLayout(ArrayDesignLibrary library) {
		if (library == null) return null;
		
		List<Layout> layoutList = new ArrayList<Layout>();
		LayoutLibrary layoutLibrary = library.getLayoutLibrary();
		for (BlockLayout blockLayout : layoutLibrary.getBlockLayout()) {
			Layout layout = new Layout();
			layout.setId(blockLayout.getId() + "");
			layout.setName(blockLayout.getName());
			layoutList.add(layout);
		}
		Collections.sort(layoutList);
	
		return layoutList;
	}

	
	public static List<BlockLayout> getBlockLayoutList(ArrayDesignLibrary library) {
		if (library == null) return null;
		
		List<BlockLayout> blocklayoutList = new ArrayList<BlockLayout>();
		blocklayoutList = library.getLayoutLibrary().getBlockLayout();
	
		return blocklayoutList;
	}

	

	
}
