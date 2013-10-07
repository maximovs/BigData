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
import ar.edu.itba.bigdata.optional.flightCount.FlightCountMapper;
import ar.edu.itba.bigdata.optional.flightCount.FlightCountReducer;
import ar.edu.itba.bigdata.optional.proportionalCancelledFlights.ProportionalCancelledFlightsMapper;
import ar.edu.itba.bigdata.optional.proportionalCancelledFlights.ProportionalCancelledFlightsReducer;


@SuppressWarnings("all")
public class CLIParser {

	private static final String OUT_PATH = "outPath";
	private static final String IN_PATH = "inPath";
	private static final String AVG_TAKE_OFF_DELAY = "avgTakeOffDelay";
	private static final String CANCELLED_FLIGHTS = "cancelledFlights";
	private static final String MILES_FLOWN = "milesFlown";
	private static final String FLIGHT_HOURS_MANUFACTURER = "flightHours";
	private static final String TARGET_MANUFACTURER = "manufacturer";
	private static final String CARRIERS_PATH = "carriersPath";
	private static final String FLIGHT_COUNT = "flightCount";
	private static final String PROPORCIONAL_CANCELLED_FLIGHTS = "propCancelledFlights";

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

								                .create(MILES_FLOWN);

        final Option flightHours = OptionBuilder.withLongOpt(FLIGHT_HOURS_MANUFACTURER)
								                .withDescription("Calculate the amount of flight hours for each plane of the given manufacturer.")
								                .create(FLIGHT_HOURS_MANUFACTURER);
        
        final Option manufacturer = OptionBuilder.withLongOpt(TARGET_MANUFACTURER)
											.withDescription("The target manufacturer.")
											.hasArg()
											.create(TARGET_MANUFACTURER);


		final Option carriersPath = OptionBuilder.withLongOpt(CARRIERS_PATH)
												.withDescription("The path to the carriers file/folder.")
												.hasArg()
												.create(CARRIERS_PATH);

		final Option flightCount = OptionBuilder.withLongOpt(FLIGHT_COUNT)
				.withDescription("Count the total amount of flights of each airline.")
				.create(FLIGHT_COUNT);
		
		final Option proportionalCancelledFlights = OptionBuilder.withLongOpt(PROPORCIONAL_CANCELLED_FLIGHTS)
				.withDescription("Calculate de proportional amount of cancelled flights over total amount of flights for each airline.")
				.create(PROPORCIONAL_CANCELLED_FLIGHTS);
		

        
        opts.addOption(inFile);
        opts.addOption(outPath);
        opts.addOption(avgTakeOffDelay);
        opts.addOption(cancelledFlights);
        opts.addOption(milesFlown);
        opts.addOption(flightHours);
        opts.addOption(manufacturer);

        opts.addOption(carriersPath);
        opts.addOption(flightCount);
        opts.addOption(proportionalCancelledFlights);

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
            	

            } else if(line.hasOption(CANCELLED_FLIGHTS) && line.hasOption(CARRIERS_PATH)) {
            	config.setMapper(CancelledFlightsMapper.class);
            	config.setReducer(CancelledFlightsReducer.class);
            	config.setExtra("carriersPath", line.getOptionValue(CARRIERS_PATH));
            	
            } else if(line.hasOption(MILES_FLOWN) && line.hasOption(CARRIERS_PATH)) {
            	config.setMapper(MilesFlownMapper.class);
            	config.setReducer(MilesFlownReducer.class);
            	config.setExtra("carriersPath", line.getOptionValue(CARRIERS_PATH));
            	
            } else if(line.hasOption(FLIGHT_HOURS_MANUFACTURER) && line.hasOption(TARGET_MANUFACTURER)) {
            	config.setMapper(FlightHoursByManufacturerMapper.class);
            	config.setReducer(FlightHoursByManufacturerReducer.class);
            	config.setExtra("manufacturer", line.getOptionValue(TARGET_MANUFACTURER));
            } else if(line.hasOption(FLIGHT_COUNT) && line.hasOption(CARRIERS_PATH)) {
            	config.setMapper(FlightCountMapper.class);
            	config.setReducer(FlightCountReducer.class);
            	config.setExtra("carriersPath", line.getOptionValue(CARRIERS_PATH));
            } else if(line.hasOption(PROPORCIONAL_CANCELLED_FLIGHTS) && line.hasOption(CARRIERS_PATH)) {
            	config.setMapper(ProportionalCancelledFlightsMapper.class);
            	config.setReducer(ProportionalCancelledFlightsReducer.class);
            	config.setExtra("carriersPath", line.getOptionValue(CARRIERS_PATH));
            } else {
            	return null;

            }
            
            return config;
        }
    }

    private static void printHelp(Options opts) {

        new HelpFormatter().printHelp("hadoop jar bigdata-tp1-jar-with-dependencies.jar", opts);

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
