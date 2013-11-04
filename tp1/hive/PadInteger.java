package com.example.hive.udf;
import org.apache.hadoop.hive.ql.exec.UDF;

public final class PadInteger extends UDF {
	
	public String evaluate(final String key){
		
		try {
			Integer number = Integer.parseInt(key);
		} catch(NumberFormatException e) {	
// 			DO NOTHING
			return key;
		}
		
		if (key.length() == 1) {
			return "0" + key;
		}
		return key;
	}
}
