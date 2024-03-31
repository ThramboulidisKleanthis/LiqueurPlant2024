package physicalSysSim.sensors;

import physicalSysSim.PlantSim;
import physicalSysSim.Silo;
import systemSR.SRCmd;
import systemSR.SRCmdCodes;

public class FillingLevelSensor extends LevelSensor {

	public FillingLevelSensor(Silo silo,PlantSim pSim) {
		super(silo,pSim);
	}

	public void trigger(){
//		System.out.println("FillingLevelSensor-HIGH_LEVEL_REACHED itsSilo.id= "+ itsSilo.id);
		if((itsSilo.id==0)||(itsSilo.id==1))
			pSim.itsSR.queue.add(new SRCmd(itsSilo.id,SRCmdCodes.HIGH_LEVEL_REACHED,0));
		else
			pSim.itsSR.queue.add(new SRCmd(itsSilo.id,SRCmdCodes.TRANSFER_COMPLETED,0));
	}
}
