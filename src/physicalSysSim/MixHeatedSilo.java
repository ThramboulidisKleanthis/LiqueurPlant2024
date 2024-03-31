package physicalSysSim;

import physicalSysSim.actuators.Heater;
import physicalSysSim.actuators.Mixer;
import physicalSysSim.actuators.Valve;
import physicalSysSim.gui.SiloVis;
import physicalSysSim.sensors.EmptyingLevelSensor;
import physicalSysSim.sensors.FillingLevelSensor;
import physicalSysSim.sensors.HeatingCompletedSensor;
import physicalSysSim.sensors.MixingCompletedSensor;

public class MixHeatedSilo extends Silo{
	public MixHeatedSilo(int id, PlantSim pSim){
		super(id,pSim);
		vis = new SiloVis(id,200,200, true, true, true,pSim);
		in = new Valve("IN4",vis.in);
		out = new Valve("OUT4",vis.out);
		es = new EmptyingLevelSensor(this,pSim);
		fs = new FillingLevelSensor(this,pSim);
		ms= new MixingCompletedSensor(this,pSim);
		hs= new HeatingCompletedSensor(this,pSim);
		m= new Mixer("M4",this);
		h= new Heater("M4",this);
	}
}
