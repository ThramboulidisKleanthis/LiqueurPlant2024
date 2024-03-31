// 12Mar2024 Start a new Version to redesign the system without using the mtc concept. 
// Have the physicalSystemSimulator as an entity that is completely separated form the cyber part.
// Also processes are separate threads

package system;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import physicalSysSim.PlantSim;
import physicalSysSim.PlantSimParams;
import process.LPProcess;
import process.LTypeAGenProcess;
import process.LTypeBGenProcess;
import systemSR.PlantSR;

public class LPSystem {
	public static Logger LOGGER; 
	public static LPSystem lpSystem;
	public PlantSim pSim;
	public PlantSR pSR;
	public LPProcess [] process= new LPProcess[2];
	public static int [] LiqueurQuantity= new int[2];
	
	public static void main(String[] args) {
		FileHandler fh;

		LOGGER = Logger.getLogger(LPSystem.class.getName() + " LOGGER");
		try {
			fh = new FileHandler("LPSystemlog.xml");
			LOGGER.addHandler(fh);
		} catch (IOException e) {e.printStackTrace();}
		LOGGER.setLevel(Level.ALL); // Request that every detail gets logged.
		LOGGER.finest("Liqueur Plant starting"); 
		PlantSimParams.read();
		lpSystem = new LPSystem();
		lpSystem.pSim = new PlantSim();
		lpSystem.pSR = new PlantSR(lpSystem,lpSystem.pSim);
		lpSystem.pSim.setItsSR(lpSystem.pSR);
		lpSystem.pSim.start();
		(new Thread(lpSystem.pSR)).start();

		lpSystem.process[0] = new LTypeAGenProcess("TypeA Liqueur Generation Process",lpSystem.pSR,LiqueurQuantity[0]);
//		(new Thread(lpSystem.process[0])).start();
		lpSystem.process[1] = new LTypeBGenProcess("TypeB Liqueur Generation Process",lpSystem.pSR,LiqueurQuantity[1]);
//		(new Thread(lpSystem.process[1])).start();
	}
}

