package com.github.philippecade.biketoworkstats;

/**
 * A data point. Has a name and a value.
 * @author xphc
 * @param T type of the value
 */
class DataPoint<T> {
	
	private final String name;
	private final T value; 
	
	DataPoint(String name, T value) {
		this.name = name;
		this.value = value;
	}
	
	String getName() {
		return this.name;
	}
	
	T getValue() {
		return this.value;
	}

}
