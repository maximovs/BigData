package ar.edu.itba.bigdata;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.cli.ParseException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, ParseException {
		final AppConfig appConfig;

		appConfig = CLIParser.getAppConfig(args);

        if (appConfig == null) {
            return;
        }
        
		Job job = new Job();

		Configuration jobConfig = job.getConfiguration();

		FileInputFormat.addInputPath(job, new Path(appConfig.getInPath()));
		FileOutputFormat.setOutputPath(job, new Path(appConfig.getOutPath()));

		job.setJarByClass(Main.class);

		job.setMapperClass(appConfig.getMapper());
		job.setReducerClass(appConfig.getReducer());

		job.setMapOutputKeyClass(appConfig.getMapOutputKeyClass());
		job.setMapOutputValueClass(appConfig.getMapOutputValueClass());

		job.setOutputKeyClass(appConfig.getOutputKeyClass());
		job.setOutputValueClass(appConfig.getOutputValueClass());

		Map<String, String> extras = appConfig.getExtras();

		if(extras.get("carriersPath") != null) {
			DistributedCache.addArchiveToClassPath(new Path(extras.get("carriersPath")), jobConfig, FileSystem.get(jobConfig));
		}
		
		for(String key: extras.keySet()) {
			jobConfig.set(key, extras.get(key));
		}
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
