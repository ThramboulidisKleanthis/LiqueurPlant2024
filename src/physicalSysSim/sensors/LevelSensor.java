package physicalSysSim.sensors;

import physicalSysSim.PlantSim;
import physicalSysSim.Silo;

public abstract class LevelSensor extends Sensor{
				
	LevelSensor(Silo silo,PlantSim pSim){
		super(silo,pSim);
	}
	
	public abstract void trigger();
	
}
