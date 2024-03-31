package physicalSysSim;

import physicalSysSim.actuators.Heater;
import physicalSysSim.actuators.Valve;
import physicalSysSim.gui.SiloVis;
import physicalSysSim.sensors.EmptyingLevelSensor;
import physicalSysSim.sensors.FillingLevelSensor;
import physicalSysSim.sensors.HeatingCompletedSensor;

public class HeatedSilo extends Silo{
	
	public HeatedSilo(int id, PlantSim pSim){
		super(id,pSim);
		vis = new SiloVis(id,200,0, true, true, false,pSim);
		in = new Valve("IN2",vis.in);
		out = new Valve("OUT2",vis.out);
		es = new EmptyingLevelSensor(this,pSim);
		fs = new FillingLevelSensor(this,pSim);
		hs= new HeatingCompletedSensor(this,pSim);
		h= new Heater("M2",this);
		}
}
