package system;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LpsLogger {
	public static Logger LOGGER=null; 

	public static Logger getLogger(String cn){
		if(LOGGER!=null) return LOGGER;
		LOGGER = Logger.getLogger(cn);
		FileHandler fh;
		try {
			fh = new FileHandler("LPSystemLog.txt");
			fh.setLevel(Level.ALL);
			var formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  
			LOGGER.addHandler(fh);
			var ch = new ConsoleHandler();
			ch.setLevel(Level.OFF);
			LOGGER.addHandler(ch);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.setLevel(Level.ALL); 
		return LOGGER;
	}

	public static void setLevel(java.util.logging.Level l) {
		LOGGER.setLevel(l); 
	}
}