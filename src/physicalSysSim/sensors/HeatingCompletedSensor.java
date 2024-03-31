package physicalSysSim.sensors;

import java.awt.Color;
import physicalSysSim.PlantSim;
import physicalSysSim.Silo;
import systemSR.SRCmd;
import systemSR.SRCmdCodes;

public class HeatingCompletedSensor extends LevelSensor {

	public HeatingCompletedSensor(Silo silo,PlantSim pSim) {
		super(silo,pSim);
	}

	public void trigger(){
//		System.out.println("TEMPERATURE_REACHED");
		var cmd=new SRCmd(itsSilo.id,SRCmdCodes.HEATING_COMPLETED,itsSilo.id==3?1:2);
		pSim.itsSR.queue.add(cmd);
		itsSilo.vis.h.setForeground(Color.WHITE);
		itsSilo.vis.t.setForeground(Color.RED);
	}
}
