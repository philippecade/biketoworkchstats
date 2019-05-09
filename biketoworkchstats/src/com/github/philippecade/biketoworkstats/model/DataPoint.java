package com.github.philippecade.biketoworkstats.model;

/**
 * A data point. Has a name and a value
 * @author xphc
 * @param T type of the value
 */
public class DataPoint<T> {
	
	private final String name;
	private final T value; 
	
	public DataPoint(String name, T value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return this.name;
	}
	
	public T getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DataPoint [name=");
		builder.append(this.name);
		builder.append(", value=");
		builder.append(this.value);
		builder.append("]");
		return builder.toString();
	}

}
