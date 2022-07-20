package org.grits.toolbox.glycanarray.om.model.xml;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.grits.toolbox.glycanarray.om.model.SpotData;
import org.grits.toolbox.glycanarray.om.model.Well;



public class BlockLayoutMapAdapter extends XmlAdapter<BlockLayoutMapEntry[], Map<Well, SpotData>> {

	@Override
	public Map<Well, SpotData> unmarshal(BlockLayoutMapEntry[] v) throws Exception {
		Map<Well, SpotData> r = new HashMap<Well, SpotData>();
        for (BlockLayoutMapEntry mapelement : v) {
            r.put( mapelement.well, mapelement.spot);
        }
        return r;
	}

	@Override
	public BlockLayoutMapEntry[] marshal(Map<Well, SpotData> v) throws Exception {
		if (v != null) {
			BlockLayoutMapEntry[] mapElements = new BlockLayoutMapEntry[v.size()];
			int i = 0;
	        for (Map.Entry<Well, SpotData> entry : v.entrySet()) {
	            mapElements[i++] = new BlockLayoutMapEntry(entry.getKey(), entry.getValue());
	        }
			
			return mapElements;
		}
		return null;
	}

}
