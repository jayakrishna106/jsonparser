package com.stc.ppm.ws.rest.util;


import java.text.SimpleDateFormat;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class StcParserUtil {
	
	SimpleDateFormat dataformat = new SimpleDateFormat("DD-MMM-YYYY HH:MM:SS");
	
	public static JSONObject getJSONObject(String name, final JSONObject value){
		return castToJSONObject(value.get(name));
	}
	
	public static JSONObject getJSONObject(String name, final Object value){
		return castToJSONObject(((JSONObject) value).get(name));
	}
	
	public static JSONArray getJSONArray(String name, final JSONObject value){
		return evaluateJSONArray(value.get(name));
	}

	private static  JSONArray evaluateJSONArray(final Object value)  {

	        JSONArray jsonArray = null;

	        if (value instanceof JSONArray) {

	            jsonArray = castToJSONArray(value);

	        } else if (value instanceof JSONObject) {

	            jsonArray = convertingJsonObjecytoJsonArray(value);

	        } 
	        return jsonArray;
	    }


	private static JSONArray convertingJsonObjecytoJsonArray(final Object value) {

	        JSONArray jsonArray = new JSONArray();
	        jsonArray.add(value);
	        return jsonArray;
	    }


	    private static JSONArray castToJSONArray(final Object value) {
	        return (JSONArray) value;
	    }
	    
	    private static JSONObject castToJSONObject(final Object value) {
	        return (JSONObject) value;
	    }
	    
	    public static String toString(JSONObject object){
	    	StringBuffer sb  = new StringBuffer(object.toString());
	    	return sb.toString();
	    }
	    
	   
	    
		public static JSONObject getERPSCMJsonObject(String name, final JSONObject value){
			return castToJSONObject(value.get(name));
		}
		
		public static JSONObject getERPSCMJsonObject(String name, final Object value){
			return castToJSONObject(((JSONObject) value).get(name));
		}
}
