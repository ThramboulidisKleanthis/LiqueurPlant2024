package physicalSysSim.sensors;

import physicalSysSim.PlantSim;
import physicalSysSim.Silo;
import systemSR.SRCmd;
import systemSR.SRCmdCodes;

public class EmptyingLevelSensor extends LevelSensor {
		
	public EmptyingLevelSensor(Silo silo, PlantSim pSim) {
		super(silo,pSim);
	}

	public void trigger(){
		if((itsSilo.id==2)||(itsSilo.id==3))
			pSim.itsSR.queue.add(new SRCmd(itsSilo.id,SRCmdCodes.LOW_LEVEL_REACHED,0));
		else
			pSim.itsSR.queue.add(new SRCmd(itsSilo.id,SRCmdCodes.TRANSFER_COMPLETED,0));
	}
}
