package ar.edu.itba.bigdata;

import java.io.FileNotFoundException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import ar.edu.itba.bigdata.avgTakeOffDelay.AVGTakeOffDelayMapper;
import ar.edu.itba.bigdata.avgTakeOffDelay.AVGTakeOffDelayReducer;
import ar.edu.itba.bigdata.cancelledFlights.CancelledFlightsMapper;
import ar.edu.itba.bigdata.cancelledFlights.CancelledFlightsReducer;
import ar.edu.itba.bigdata.flightHoursByManufacturer.FlightHoursByManufacturerMapper;
import ar.edu.itba.bigdata.flightHoursByManufacturer.FlightHoursByManufacturerReducer;
import ar.edu.itba.bigdata.milesFlown.MilesFlownMapper;
import ar.edu.itba.bigdata.milesFlown.MilesFlownReducer;

@SuppressWarnings("all")
public class CLIParser {

	private static final String OUT_PATH = "outPath";
	private static final String IN_PATH = "inPath";
	private static final String AVG_TAKE_OFF_DELAY = "avgTakeOffDelay";
	private static final String CANCELLED_FLIGHTS = "cancelledFlights";
	private static final String MILES_FLOWN = "milesFlown";
	private static final String FLIGHT_HOURS_MANUFACTURER = "flightHours";
	private static final String TARGET_MANUFACTURER = "manufacturer";
	private static final String CARRIERS_PATH = "/user/hadoop/ITBA/TP1/INPUT/SAMPLE/ref/carriers.csv";

	private static Options getInputOptions() {
        final Options opts = new Options();

        final Option inFile = OptionBuilder.withLongOpt(IN_PATH)
											.withDescription("The path to the input file/folder.")
											.hasArg()
											.create(IN_PATH);

		final Option outPath = OptionBuilder.withLongOpt(OUT_PATH)
											.withDescription("The path to the output file/folder.")
											.hasArg()
											.create(OUT_PATH);

        final Option avgTakeOffDelay = OptionBuilder.withLongOpt(AVG_TAKE_OFF_DELAY)
								                	.withDescription("Calculate the average take-off delay for each state and month.")
								                	.create(AVG_TAKE_OFF_DELAY);

        final Option cancelledFlights = OptionBuilder.withLongOpt(CANCELLED_FLIGHTS)
            										.withDescription("Calculate the number of cancelled flights for each airline.")
            										.create(CANCELLED_FLIGHTS);

        final Option milesFlown = OptionBuilder.withLongOpt(MILES_FLOWN)
								                .withDescription("Calculate the number of miles flown for each airline and year.")
								                .hasArg()
								                .create(MILES_FLOWN);

        final Option flightHours = OptionBuilder.withLongOpt(FLIGHT_HOURS_MANUFACTURER)
								                .withDescription("Calculate the amount of flight hours for each plane of the given manufacturer.")
								                .create(FLIGHT_HOURS_MANUFACTURER);
        
        final Option manufacturer = OptionBuilder.withLongOpt(TARGET_MANUFACTURER)
											.withDescription("The target manufacturer.")
											.hasArg()
											.create(TARGET_MANUFACTURER);
        
        
        opts.addOption(inFile);
        opts.addOption(outPath);
        opts.addOption(avgTakeOffDelay);
        opts.addOption(cancelledFlights);
        opts.addOption(milesFlown);
        opts.addOption(flightHours);
        opts.addOption(manufacturer);
        return opts;
    }

    private static AppConfig parseOptions(final Options opts, final CommandLine line) throws FileNotFoundException, ParseException {

        if (line.hasOption("help")) {
            return null;
        } else {

        	if(!line.hasOption(IN_PATH) || !line.hasOption(OUT_PATH)) {
        		return null;
        	}

            AppConfig config = new AppConfig();
            config.setInPath(line.getOptionValue(IN_PATH));
            config.setOutPath(line.getOptionValue(OUT_PATH));
            
            if(line.hasOption(AVG_TAKE_OFF_DELAY)) {
            	config.setMapper(AVGTakeOffDelayMapper.class);
            	config.setReducer(AVGTakeOffDelayReducer.class);
            	
            } else if(line.hasOption(CANCELLED_FLIGHTS)) {
            	config.setMapper(CancelledFlightsMapper.class);
            	config.setReducer(CancelledFlightsReducer.class);
            	config.setExtra("carriersPath", CARRIERS_PATH);
            	
            } else if(line.hasOption(MILES_FLOWN)) {
            	config.setMapper(MilesFlownMapper.class);
            	config.setReducer(MilesFlownReducer.class);
            	config.setExtra("carriersPath", CARRIERS_PATH);
            	
            } else if(line.hasOption(FLIGHT_HOURS_MANUFACTURER)) {
            	config.setMapper(FlightHoursByManufacturerMapper.class);
            	config.setReducer(FlightHoursByManufacturerReducer.class);
            	config.setExtra("manufacturer", line.getOptionValue(TARGET_MANUFACTURER));
            }
            
            return config;
        }
    }

    private static void printHelp(Options opts) {
        new HelpFormatter().printHelp("hadoop jar bigdata-tp1.jar", opts);
    }

    public static AppConfig getAppConfig(String[] args) throws ParseException, FileNotFoundException {

        final CommandLineParser parser = new GnuParser();
        final Options opts = getInputOptions();
        final CommandLine line = parser.parse(opts, args);
        AppConfig config = parseOptions(opts, line);
        
        if (config == null) {
            printHelp(opts);
        }
        return config;

    }
}
