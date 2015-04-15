package org.geojson;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Crs {
	
	public enum CrsType{
		namedCrs("name"),
		linkedCrs("link");
		
		public final String type;
		
		CrsType(String value){
			this.type = value;
		}
		
		public static CrsType getType(String crsString){
	    		for(CrsType crsType : CrsType.values()){
	    		    if( crsType.type.equals(crsString)){
	    			return crsType;
	    		    }
	    		}
	    		throw new RuntimeException("The requested Crs type " + crsString +" do not match any CrsType");
    	    	}
	}

	private final String type;
	private Map<String, Object> properties = new HashMap<String, Object>();
	
	public Crs(){//backward compatibility, the default constructor returned a named Crs.
	    this(CrsType.namedCrs);
	}
	
	public Crs(CrsType crsType){
	    this.type = crsType.type;
	}
	
	@JsonCreator
	public Crs(@JsonProperty("type")String crsTypeString){
	    this(CrsType.getType(crsTypeString));
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(CrsType crsType){
		this.type = crsType.type
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Crs)) {
			return false;
		}
		Crs crs = (Crs)o;
		if (properties != null ? !properties.equals(crs.properties) : crs.properties != null) {
			return false;
		}
		return !(type != null ? !type.equals(crs.type) : crs.type != null);
	}

	@Override
	public int hashCode() {
		int result = type != null ? type.hashCode() : 0;
		result = 31 * result + (properties != null ? properties.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Crs{" + "type='" + type + '\'' + ", properties=" + properties + '}';
	}
}
