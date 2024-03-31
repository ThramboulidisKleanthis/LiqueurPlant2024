package physicalSysSim.sensors;

import java.awt.Color;

import physicalSysSim.PlantSim;
import physicalSysSim.Silo;
import systemSR.SRCmd;
import systemSR.SRCmdCodes;

public class MixingCompletedSensor extends LevelSensor {

	public MixingCompletedSensor(Silo silo,PlantSim pSim) {
		super(silo,pSim);
	}

	public void trigger(){
		var cmd=new SRCmd(itsSilo.id,SRCmdCodes.MIXING_COMPLETED,itsSilo.id==3?1:2);
		pSim.itsSR.queue.add(cmd);
		itsSilo.vis.m.setForeground(Color.BLACK);
	}
}
