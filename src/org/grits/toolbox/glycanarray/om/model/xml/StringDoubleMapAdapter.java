package org.grits.toolbox.glycanarray.om.model.xml;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringDoubleMapAdapter extends XmlAdapter<StringDoubleMapEntry[], Map<String, Double>> {

	@Override
	public Map<String, Double> unmarshal(StringDoubleMapEntry[] v) throws Exception {
		Map<String, Double> r = new HashMap<>();
        for (StringDoubleMapEntry mapelement : v)
            r.put(mapelement.concentration, mapelement.value);
        return r;
	}

	@Override
	public StringDoubleMapEntry[] marshal(Map<String, Double> v) throws Exception {
		if (v == null)
			return null;
		StringDoubleMapEntry[] mapElements = new StringDoubleMapEntry[v.size()];
        int i = 0;
        for (Map.Entry<String, Double> entry : v.entrySet())
            mapElements[i++] = new StringDoubleMapEntry(entry.getKey(), entry.getValue());

        return mapElements;
	}

}
