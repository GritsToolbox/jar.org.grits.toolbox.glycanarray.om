package org.grits.toolbox.glycanarray.om.model.xml;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.grits.toolbox.glycanarray.om.model.MeasurementSet;
import org.grits.toolbox.glycanarray.om.model.PowerLevel;


public class PowerLevelMeasurementMapAdapter extends XmlAdapter<MeasurementMapEntry[], Map<PowerLevel, MeasurementSet>> {

	@Override
	public Map<PowerLevel, MeasurementSet> unmarshal(MeasurementMapEntry[] v)
			throws Exception {
		Map<PowerLevel, MeasurementSet> r = new HashMap<PowerLevel, MeasurementSet>();
        for (MeasurementMapEntry mapelement : v)
            r.put(mapelement.power, mapelement.set);
        return r;
	}

	@Override
	public MeasurementMapEntry[] marshal(Map<PowerLevel, MeasurementSet> v) throws Exception {
		if (v == null)
			return null;
		MeasurementMapEntry[] mapElements = new MeasurementMapEntry[v.size()];
        int i = 0;
        for (Map.Entry<PowerLevel, MeasurementSet> entry : v.entrySet())
            mapElements[i++] = new MeasurementMapEntry(entry.getKey(), entry.getValue());

        return mapElements;
	}

}
