package main;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static main.LogFieldFormatter.format;
import static main.LogFieldFormatter.pair;

public class Updater {

	private static Logger logger = LoggerFactory.getLogger(Updater.class);

	public static void main(String[] args) {
		logger.debug(format(pair("time", LocalDateTime.now())), "Yeni log geldi");
	}

}
