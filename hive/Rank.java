package com.example.hive.udf;
import org.apache.hadoop.hive.ql.exec.UDF;

public final class Rank extends UDF{
	private int  counter = 1;
	private String last_key;
	
	public int evaluate(final String key){
		if ( !key.equalsIgnoreCase(this.last_key) ) {
			this.last_key = key;
			this.counter = 1;
		}
		return this.counter++;
	}
}
