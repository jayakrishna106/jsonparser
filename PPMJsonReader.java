package com.stc.ppm.ws.rest.util;


import java.io.FileReader;

import java.io.Reader;

import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class PPMJsonReader {

	// private JSONObject mainObject = null;
	private JSONArray fieldArray = null;
	private static String TOKEN = "token";
	private static String VALUE = "stringValue";
	private static String DATE_VALUE = "dateValue";
	private static String TABLE_VALUE = "tableValue";
	private static final String REQUEST_KEY = "ns2:request";
	private static final String FIELDS = "fields";
	private static final String FIELD = "field";
	private static final String COLUMN_TOKENS = "columnTokens";
	private static final String COLUMN_TOKEN = "columnToken";
	private static final String COLUMNS = "columns";
	private static final String COLUMN = "column";
	private static final String ROW = "row";
	private static final String REQ_TYPE = "requestType";
	// private List<Map<String, String>> tableList = null;
	private String requestType;
	private int counter;
	private static StcParserUtil parseUtil;
	private Map<String, List> tableValueMap;
	//private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

	public Map<String, List> getTableValueMap() {
		return tableValueMap;
	}

	public void setTableValueMap(Map<String, List> tableValueMap) {
		this.tableValueMap = tableValueMap;
	}

	public JSONArray getFieldArray() {
		return fieldArray;
	}

	public void setFieldArray(JSONArray fieldArray) {
		this.fieldArray = fieldArray;
	}

	public PPMJsonReader(String fileName) {
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(new FileReader(fileName));

			JSONObject mainObject = StcParserUtil.getJSONObject(REQUEST_KEY,
					obj);
			fieldArray = StcParserUtil.getJSONArray(FIELD,
					StcParserUtil.getJSONObject(FIELDS, mainObject));
			this.setFieldArray(fieldArray);
			generateTableValue();
			counter = 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public PPMJsonReader(Reader reader) {
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(reader);

			JSONObject mainObject = StcParserUtil.getJSONObject(REQUEST_KEY,
					obj);
			JSONArray fieldArray = StcParserUtil.getJSONArray(FIELD,
					StcParserUtil.getJSONObject(FIELDS, mainObject));
			setRequestType(mainObject.get(REQ_TYPE).toString());
			this.setFieldArray(fieldArray);
			generateTableValue();
			counter = 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void generateTableValue() {
		JSONArray fieldArr = this.getFieldArray();
		//System.out.println(" fieldArr ::" + fieldArr);
		Map<String, List> tableValueMap = new LinkedHashMap<String, List>(10);
		String mapToken = null;
		List<Map<String, String>> tableList = null;
		// Iterating through fieldarray
		for (Object element : fieldArr) {

			// getting each element from array
			JSONObject fieldArrayObj = (JSONObject) element;
			//System.out.println("fieldArrayObj::" + fieldArrayObj);
			if (fieldArrayObj.containsKey(TABLE_VALUE) ) {
				tableList = new ArrayList<Map<String, String>>();
				mapToken = (String) fieldArrayObj.get(TOKEN);
				//if (!("REQD.WO_ITEMS".equals("mapToken"))) {
					
				//System.out.println("mapToken ::" + mapToken);
				JSONObject tableObj = StcParserUtil.getJSONObject(TABLE_VALUE,
						fieldArrayObj);
				JSONArray colArray = StcParserUtil.getJSONArray(COLUMN_TOKEN,
						StcParserUtil.getJSONObject(COLUMN_TOKENS, tableObj));
				//System.out.println("colArray::" + colArray);
				//System.out.println("tableObj::" + tableObj);
				JSONArray rowArray = null;
				if ( tableObj != null && tableObj.containsKey(ROW)) {

					rowArray = StcParserUtil.getJSONArray(ROW, tableObj);

					//System.out.println("rowArray::" + rowArray);

					for (Object rowelement : rowArray) {
						JSONArray rowArrays = StcParserUtil.getJSONArray(
								COLUMN, StcParserUtil.getJSONObject(COLUMNS,
										rowelement));
						Map<String, String> myMap = new LinkedHashMap<String, String>(
								20);
						int i = 0;
						for (Object str : rowArrays) {
							JSONObject rowItem = (JSONObject) str;
							myMap.put((String) colArray.get(i++),
									this.getValue(rowItem));
						}
						tableList.add(myMap);
						//System.out.println("tableList" + tableList);
					}
				}
			//}
			tableValueMap.put(mapToken, tableList);

			 }
		}
      this.setTableValueMap(tableValueMap);
	}

	private String getValue(JSONObject object) {
		String value = null;
		try{
		value = (String) object.get(VALUE);
		if(value == null) {
			value = (String) object.get(DATE_VALUE);
		}
		}catch(ClassCastException cce){
			//System.out.println("object.get(VALUE)"+ object.get(VALUE));
			value = "";
			for(Object val: StcParserUtil.getJSONArray(VALUE, object)){
				value = value + (String) val +";";
			}
			//System.out.println("object.get(VALUE) value : "+ value);
		}
		return value;
		
	}
	public String findKeyValue(String key) {
		String value = null;
		//System.out.println("key:"+key);
		for (Object element : this.getFieldArray()) {
			JSONObject myObj = (JSONObject) element;
			//System.out.println("myObj.get(TOKEN)):"+myObj.get(TOKEN));
			
			if (key.equals(myObj.get(TOKEN))) {
				
				value = this.getValue(myObj);
				
				//System.out.println("myObj.get(TOKEN)) value:"+myObj.get(VALUE));
				/*if (value == null) {
					System.out.println("myObj.get(TOKEN)) DATE_VALUE and key:"+myObj.get(DATE_VALUE) + "key :"+key);
					value = (String) myObj.get(DATE_VALUE);
				}*/
			}
			
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getTableValueList(String key) {
//System.out.println("Key"+key + "this.getTableValueMap().get(key)"+this.getTableValueMap().get(key));
//System.out.println("Key"+key);
		return this.getTableValueMap().get(key);
	}

	private void setRequestType(String reqType) {
		this.requestType = reqType;
	}

	public String getRequestType() {
		return this.requestType;
	}

}
