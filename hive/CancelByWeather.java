package com.example.hive.udf;
import org.apache.hadoop.hive.ql.exec.UDF;

public final class CancelByWeather extends UDF {
	
	public int evaluate(final String key){
		
		if (key.equals("B")) {
			return 1;
		} else {
			return 0;
		}
	}
}