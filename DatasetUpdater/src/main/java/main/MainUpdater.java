package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainUpdater {
	private static final String PARAM_STOCK = "stock";
	private static final String PARAM_NYTIMES = "nytimes";
	private static Logger logger = LoggerFactory.getLogger(MainUpdater.class);

	public static void main(String[] args) {
		if (PARAM_NYTIMES.equals(args[0])) {
			new NyTimesUpdater().run();
		}

		if (PARAM_STOCK.equals(args[0])) {
			new StockUpdater().run();
		}
		if (args.length == 0) {
		} else {
			logger.warn("You did not provide a main class");
		}
	}
}
