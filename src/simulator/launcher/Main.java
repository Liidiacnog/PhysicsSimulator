package simulator.launcher;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import javax.swing.SwingUtilities;

import org.json.JSONObject;

//import extra.dialog.ex1.MainWindow;
import simulator.control.Controller;
import simulator.control.StateComparator;
import simulator.factories.*;
import simulator.model.*;
import simulator.view.MainWindow;

public class Main {

	// default values for some parameters
	//
	private final static Double _dtimeDefaultValue = 2500.0;
	private final static String _forceLawsDefaultValue = "nlug";
	private final static String _stateComparatorDefaultValue = "epseq";
	private final static Integer _stepsDefaultValue = 150;

	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double _dtime = null;
	private static int _steps = 0;
	private static String _inFile = null;
	private static String _outFile = null;
	private static String _mode = null;
	private static JSONObject _forceLawsInfo = null;
	private static String _expectedOutFile = null;
	private static JSONObject _stateComparatorInfo = null;

	// factories
	private static Factory<Body> _bodyFactory;
	private static Factory<ForceLaw> _forceLawsFactory;
	private static Factory<StateComparator> _stateComparatorFactory;

	private static void init() {
		// initializing the bodies factory
		List<Builder<Body>> bodyBuilders = new ArrayList<>();
		bodyBuilders.add(new BasicBodyBuilder());
		bodyBuilders.add(new MassLosingBodyBuilder());
		_bodyFactory = new BuilderBasedFactory<>(bodyBuilders);

		// initializing the force laws factory
		List<Builder<ForceLaw>> lawBuilders = new ArrayList<>();
		lawBuilders.add(new NewtonUniversalGravitationBuilder());
		lawBuilders.add(new MovingTowardsFixedPointBuilder());
		lawBuilders.add(new NoForceBuilder());
		_forceLawsFactory = new BuilderBasedFactory<>(lawBuilders);

		// initializing the state comparator
		List<Builder<StateComparator>> comparatorBuider = new ArrayList<>();
		comparatorBuider.add(new EpsilonEqualStatesBuilder());
		comparatorBuider.add(new MassEqualStatesBuilder());
		_stateComparatorFactory = new BuilderBasedFactory<>(comparatorBuider);
	}

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);

			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseStepsOption(line);
			parseExpectedFileOption(line);
			parseDeltaTimeOption(line);
			parseForceLawsOption(line);
			parseStateComparatorOption(line);
			parseModeOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());

		// output
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg()
				.desc("Output file, where output is written. Default value: the standard output.").build());

		// steps
		cmdLineOptions.addOption(Option.builder("s").longOpt("steps").hasArg()
				.desc("An integer representing the number of simulation steps. Default value: 150.").build());

		// expected output
		cmdLineOptions.addOption(Option.builder("eo").longOpt("expected-output").hasArg()
				.desc("The expected output file. If not provided no comparison is applied").build());

		// delta-time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _dtimeDefaultValue + ".")
				.build());

		// force laws
		cmdLineOptions.addOption(Option.builder("fl").longOpt("force-laws").hasArg()
				.desc("Force laws to be used in the simulator. Possible values: "
						+ factoryPossibleValues(_forceLawsFactory) + ". Default value: '" + _forceLawsDefaultValue
						+ "'.")
				.build());

		// gravity laws
		cmdLineOptions.addOption(Option.builder("cmp").longOpt("comparator").hasArg()
				.desc("State comparator to be used when comparing states. Possible values: "
						+ factoryPossibleValues(_stateComparatorFactory) + ". Default value: '"
						+ _stateComparatorDefaultValue + "'.")
				.build());

		// program mode
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc(
				"Execution Mode. Possible values: 'batch' (Batch mode), 'gui' (Graphical User Interface mode). Default value: 'batch'")
				.build());

		return cmdLineOptions;
	}

	public static String factoryPossibleValues(Factory<?> factory) {
		if (factory == null)
			return "No values found (the factory is null)";

		String s = "";

		for (JSONObject fe : factory.getInfo()) {
			if (s.length() > 0) {
				s = s + ", ";
			}
			s = s + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
		}

		s = s + ". You can provide the 'data' json attaching :{...} to the tag, but without spaces.";
		return s;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null) {
			throw new ParseException("In batch mode an input file of bodies is required");
		}
	}

	private static void parseExpectedFileOption(CommandLine line) {
		_expectedOutFile = line.getOptionValue("eo");
	}

	private static void parseStepsOption(CommandLine line) throws ParseException {
		String steps = line.getOptionValue("s", _stepsDefaultValue.toString());
		try {
			_steps = Integer.parseInt(steps);
			assert (_steps > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid number of steps: " + steps);
		}
	}

	private static void parseOutFileOption(CommandLine line) {
		_outFile = line.getOptionValue("o");
	}

	private static void parseModeOption(CommandLine line) {
		_mode = line.getOptionValue("m");
	}

	private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
		try {
			_dtime = Double.parseDouble(dt);
			assert (_dtime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
	}

	private static JSONObject parseWRTFactory(String v, Factory<?> factory) {

		// the value of v is either a tag for the type, or a tag: data where data is a
		// JSON structure corresponding to the data of that type. We split this
		// information
		// into variables 'type' and 'data'
		//
		int i = v.indexOf(":");
		String type = null;
		String data = null;
		if (i != -1) {
			type = v.substring(0, i);
			data = v.substring(i + 1);
		} else {
			type = v;
			data = "{}";
		}

		// look if the type is supported by the factory
		boolean found = false;
		for (JSONObject fe : factory.getInfo()) {
			if (type.equals(fe.getString("type"))) {
				found = true;
				break;
			}
		}

		// build a corresponding JSON for that data, if found
		JSONObject jo = null;
		if (found) {
			jo = new JSONObject();
			jo.put("type", type);
			jo.put("data", new JSONObject(data));
		}
		return jo;

	}

	private static void parseForceLawsOption(CommandLine line) throws ParseException {
		String fl = line.getOptionValue("fl", _forceLawsDefaultValue);
		_forceLawsInfo = parseWRTFactory(fl, _forceLawsFactory);
		if (_forceLawsInfo == null) {
			throw new ParseException("Invalid force laws: " + fl);
		}
	}

	private static void parseStateComparatorOption(CommandLine line) throws ParseException {
		String scmp = line.getOptionValue("cmp", _stateComparatorDefaultValue);
		_stateComparatorInfo = parseWRTFactory(scmp, _stateComparatorFactory);
		if (_stateComparatorInfo == null) {
			throw new ParseException("Invalid state comparator: " + scmp);
		}
	}

	private static void startBatchMode() throws Exception {
		ForceLaw force = _forceLawsFactory.createInstance(_forceLawsInfo);
		StateComparator comp = _stateComparatorFactory.createInstance(_stateComparatorInfo);
		PhysicsSimulator sim = new PhysicsSimulator(_dtime, force);
		Controller c = new Controller(sim, _bodyFactory, _forceLawsFactory);

		OutputStream outChar = null;
		BufferedInputStream expectedOut = null;

		try (InputStream inChar = new FileInputStream(_inFile)) {
			c.loadBodies(inChar);
		} catch (IOException ioe) {
			throw new IllegalArgumentException("Input file " + _inFile + " could not be opened");
		}

		if (_outFile != null) {
			try {
				outChar = new BufferedOutputStream(new FileOutputStream(_outFile));
			} catch (IOException ioe) {
				throw new IllegalArgumentException("Output file " + _outFile + " could not be opened");
			}
		} else {
			outChar = System.out;
		}

		if (_expectedOutFile != null) {
			try {
				expectedOut = new BufferedInputStream(new FileInputStream(_expectedOutFile));
			} catch (IOException ioe) {
				throw new IllegalArgumentException("Expected output file " + _expectedOutFile + " could not be opened");
			}
		}

		c.run(_steps, outChar, expectedOut, comp);

		if (outChar != null)
			outChar.close();
		if (expectedOut != null)
			expectedOut.close();
	}

	private static void startGUIMode() throws Exception {
		ForceLaw force = _forceLawsFactory.createInstance(_forceLawsInfo);
		PhysicsSimulator sim = new PhysicsSimulator(_dtime, force);
		Controller c = new Controller(sim, _bodyFactory, _forceLawsFactory);

		/*
		 * if we load a file with the "-i" option, there is chance that the viewer::
		 * autoScale() method won't do the expected effect and the bodies will be drawn
		 * close to the center (this is because if you call it from method onRegister,
		 * the size of the component is still 0). To solve this issue:
		 * 		load the bodies after SwingUtilities.invokeAndWait in startGUIMode
		 */
		
		 SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				new MainWindow(c, sim, _forceLawsFactory, _bodyFactory);
			}
		});

		try (InputStream inChar = new FileInputStream(_inFile)) {
			c.loadBodies(inChar);
		} catch (IOException ioe) {
			throw new IllegalArgumentException("Input file " + _inFile + " could not be opened");
		}
		
	}

	private static void start(String[] args) throws Exception {
		parseArgs(args);
		if (_mode.equalsIgnoreCase("gui"))
			startGUIMode();
		else
			startBatchMode();
	}

	public static void main(String[] args) {
		try {
			init();
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}

}
