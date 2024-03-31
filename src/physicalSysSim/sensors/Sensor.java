package physicalSysSim.sensors;

import physicalSysSim.PlantSim;
import physicalSysSim.Silo;

public class Sensor {
	public Silo itsSilo;
	PlantSim pSim;
	
	public Sensor(Silo silo,PlantSim pSim){
		itsSilo=silo;
		this.pSim=pSim;
	}
}
