package com.example.hive.udf;
import org.apache.hadoop.hive.ql.exec.UDF;

public final class CheckVariable extends UDF{

	public boolean evaluate(final String key){
        if (key.contains("hiveconf"))
        {
            return false;
        }
        return true;
    }
}
