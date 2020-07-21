package main;

import java.io.Serializable;

public class TextPair implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2638971245424402178L;
	
	private String key;
	private String value;

	public TextPair(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return this.key;
	}

	public String getValue() {
		return this.value;
	}

}
