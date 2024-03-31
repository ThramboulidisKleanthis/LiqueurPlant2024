package physicalSysSim;

import physicalSysSim.actuators.Mixer;
import physicalSysSim.actuators.Valve;
import physicalSysSim.gui.SiloVis;
import physicalSysSim.sensors.EmptyingLevelSensor;
import physicalSysSim.sensors.FillingLevelSensor;
import physicalSysSim.sensors.MixingCompletedSensor;

public class MixSilo extends Silo{

	public MixSilo(int id, PlantSim pSim){
		super(id,pSim);
		vis = new SiloVis(id,0,200, false, false, true,pSim);
		in = new Valve("IN3",vis.in);
		out = new Valve("OUT3",vis.out);
		es = new EmptyingLevelSensor(this,pSim);
		fs = new FillingLevelSensor(this,pSim);
		ms= new MixingCompletedSensor(this,pSim);
		m= new Mixer("M3",this);
	}
}
