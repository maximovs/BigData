package com.example.hive.udf;
import org.apache.hadoop.hive.ql.exec.UDF;

public final class Position extends UDF{
	private int  counter = 1;
	private String last_key;

	public int evaluate(final String key){
		return this.counter++;
	}
}
