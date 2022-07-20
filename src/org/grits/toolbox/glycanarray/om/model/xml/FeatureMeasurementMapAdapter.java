package org.grits.toolbox.glycanarray.om.model.xml;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.grits.toolbox.glycanarray.om.model.Feature;
import org.grits.toolbox.glycanarray.om.model.Measurement;


public class FeatureMeasurementMapAdapter extends XmlAdapter<MeasurementSetEntry[], Map<Feature, Measurement>> {

	@Override
	public Map<Feature, Measurement> unmarshal(MeasurementSetEntry[] v)
			throws Exception {
		Map<Feature, Measurement> r = new HashMap<Feature, Measurement>();
        for (MeasurementSetEntry mapelement : v) {
            r.put(mapelement.feature, mapelement.measurement);
        }
        return r;
	}

	@Override
	public MeasurementSetEntry[] marshal(Map<Feature, Measurement> v) throws Exception {
		if (v == null)
			return null;
		MeasurementSetEntry[] mapElements = new MeasurementSetEntry[v.size()];
        int i = 0;
        for (Map.Entry<Feature, Measurement> entry : v.entrySet())
            mapElements[i++] = new MeasurementSetEntry(entry.getKey(), entry.getValue());

        return mapElements;
	}

}
