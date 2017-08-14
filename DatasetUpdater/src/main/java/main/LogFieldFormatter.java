package main;

import java.util.HashMap;

import org.slf4j.Marker;

import net.logstash.logback.marker.Markers;

public class LogFieldFormatter {

	public static Marker format(TextPair... textPairs) {
		HashMap<String, String> map = new HashMap<String, String>();
		if (textPairs != null)
			for (TextPair textPair : textPairs) {
				map.put(textPair.getKey(), textPair.getValue());
			}
		return Markers.appendEntries(map);
	}

	public static TextPair pair(Object key, Object value) {

		String textualKey = null;
		String textualValue = null;
		if (key != null) {
			textualKey = key.toString();
		}
		if (value != null) {
			textualValue = value.toString();
		}
		return new TextPair(textualKey, textualValue);
	}

}
