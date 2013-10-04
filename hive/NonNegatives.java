package com.example.hive.udf;
import org.apache.hadoop.hive.ql.exec.UDF;

public final class NonNegatives extends UDF {
	
	public int evaluate(final String key){
		
		Integer number = 0;
		try {
			number = Integer.parseInt(key);
		} catch(NumberFormatException e) {	
// 			DO NOTHING
			return number;
		}
		if (number < 0) {
			return 0;
		} else {
			return number;
		}
	}
}
