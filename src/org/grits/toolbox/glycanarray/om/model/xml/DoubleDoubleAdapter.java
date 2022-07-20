package org.grits.toolbox.glycanarray.om.model.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DoubleDoubleAdapter extends XmlAdapter<DoubleDoubleMapEntry[], Map<Double, List<Double>>> {


	@Override
	public Map<Double, List<Double>> unmarshal(DoubleDoubleMapEntry[] v) throws Exception {
		Map<Double, List<Double>> r = new HashMap<>();
        for (DoubleDoubleMapEntry mapelement : v)
            r.put(mapelement.concentration, mapelement.values);
        return r;
	}

	@Override
	public DoubleDoubleMapEntry[] marshal(Map<Double, List<Double>> v) throws Exception {
		if (v == null)
			return null;
		DoubleDoubleMapEntry[] mapElements = new DoubleDoubleMapEntry[v.size()];
        int i = 0;
        for (Map.Entry<Double, List<Double>> entry : v.entrySet())
            mapElements[i++] = new DoubleDoubleMapEntry(entry.getKey(), entry.getValue());

        return mapElements;
	}

}
