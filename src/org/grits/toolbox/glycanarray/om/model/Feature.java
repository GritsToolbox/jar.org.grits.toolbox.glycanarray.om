package org.grits.toolbox.glycanarray.om.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.grits.toolbox.glycanarray.om.Config;

@XmlRootElement(name="feature")
public class Feature {

	Integer id;
	String name;
	Integer groupId;	
	Integer probeId;
	List<Glycan> sequences;
	
	/*Measurement measurement;
	
	public Measurement getMeasurement() {
		return measurement;
	}
	
	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}*/
	
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	
	@XmlAttribute
	public Integer getGroupId() {
		return groupId;
	}
	
	
	public void setProbeId(Integer probeId) {
		this.probeId = probeId;
	}
	
	@XmlAttribute
	public Integer getProbeId() {
		return probeId;
	}
	
	
/*	@XmlTransient
	public Integer getGroupId () {
		if (groupId == null) {
			if (measurement != null) {
				if (measurement.getData() != null && !measurement.getData().isEmpty()) {
					groupId = measurement.getData().get(0).getGroup();
				}
			}
		}
		return groupId;
	}*/
	
	/** We need to use this to prevent cycles in XML (due to SpotData in Measurement containing a reference to its Feature)
	 *  Since id is an Integer, it is not allowed to be used as XmlID
	 * @return id as a string to be used for XML marshaling/unmarshaling
	 */
/*	@XmlAttribute
	@XmlID
	public String getXMLId () {
		return getId() + "-" + getGroupId();
	}*/

	@XmlAttribute
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElementWrapper(name="sequence")
	public List<Glycan> getSequences() {
		return sequences;
	}

	public void setSequences(List<Glycan> sequences2) {
		this.sequences = sequences2;
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Feature)
			if (this.id.equals(((Feature) obj).getId())) {
				if (this.groupId == null) {
					return true;
				}else if (this.groupId.equals(((Feature) obj).getGroupId())) {
					return true;
				}else if (!this.groupId.equals(((Feature) obj).getGroupId())) {
					return false;
				}
				if (this.probeId == null)
					return true;
				else if (this.probeId.equals(((Feature) obj).getProbeId()))
					return true;
			}
			
		return false;
	}
	
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public String getSequenceString() {
		if (sequences == null)
			return null;
		
		String seqString = "";
		for (Glycan seq : sequences) {	
			if(seq.getGlycanMoieties() != null) {
				for (GlycanMoiety moiety : seq.getGlycanMoieties()) {
					if(moiety.sequence != null) {
						if (seqString.trim().isEmpty()) { // first one
							seqString += moiety.sequence;
						} else {
							seqString += Config.COMBO_SEQUENCE_SEPARATOR + moiety.sequence;
						}
					}
				}				
			}
		}
		
		return seqString;
	}	
	
	
	public String getOriginalSequenceString() {
		if (sequences == null)
			return null;
		
		String seqOriginalString = "";
		for (Glycan seq : sequences) {		
			for (GlycanMoiety moiety : seq.getGlycanMoieties()) {
				if(moiety.orinalSequence != null) {
					if (seqOriginalString.trim().isEmpty()) { // first one
						seqOriginalString += moiety.orinalSequence;
					} else {
						seqOriginalString += Config.COMBO_SEQUENCE_SEPARATOR + moiety.orinalSequence;
					}
				}
			}
		}
		return seqOriginalString;
	}	
	
	public String getGlyTouCanID() {
		if (sequences == null)
			return null;
		
		String glyTouCanId = "";
		for (Glycan seq : sequences) {		
			for (GlycanMoiety moiety : seq.getGlycanMoieties()) {
				if(moiety.glyTouCanId != null) {
					if (glyTouCanId.trim().isEmpty()) { // first one
						glyTouCanId += moiety.glyTouCanId;
					} else {
						glyTouCanId += Config.COMBO_SEQUENCE_SEPARATOR + moiety.glyTouCanId;
					}
				}
			}
		}
		return glyTouCanId;
	}	
	
}
