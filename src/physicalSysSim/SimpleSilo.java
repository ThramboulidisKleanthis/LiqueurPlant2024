package physicalSysSim;

import physicalSysSim.actuators.Valve;
import physicalSysSim.gui.SiloVis;
import physicalSysSim.sensors.EmptyingLevelSensor;
import physicalSysSim.sensors.FillingLevelSensor;

public class SimpleSilo extends Silo{

	public SimpleSilo(int id, PlantSim pSim){
		super(id,pSim);
		vis = new SiloVis(id,0,0, false, false, false,pSim);
		in = new Valve("IN1",vis.in);
		out = new Valve("OUT1",vis.out);
		es = new EmptyingLevelSensor(this,pSim);
		fs = new FillingLevelSensor(this,pSim);
	}
}
