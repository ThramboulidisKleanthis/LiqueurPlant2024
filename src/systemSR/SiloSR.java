package systemSR;

import javax.naming.OperationNotSupportedException;

import physicalSysSim.SimCmd;
import physicalSysSim.SimCmdCodes;

public class SiloSR {
	int siloId;
	PlantSR plantSR;
	boolean []supportedOp;
	
	public SiloSR(int siloId,PlantSR plantSR,boolean []so) {
		this.siloId=siloId;
		this.plantSR=plantSR;
		supportedOp=so;
	}
	public void fill() {
		var cmd = new SimCmd(siloId,SimCmdCodes.OPEN_INVALVE);
		plantSR.itsSim.queue.add(cmd);
	}
	public void highLevelReached() {
		plantSR.itsSim.queue.add(new SimCmd(siloId,SimCmdCodes.CLOSE_INVALVE));
	}
	public void empty() {
		plantSR.itsSim.queue.add(new SimCmd(siloId,SimCmdCodes.OPEN_OUTVALVE));
	}
	public void lowLevelReached() {
		plantSR.itsSim.queue.add(new SimCmd(siloId,SimCmdCodes.CLOSE_OUTVALVE));
	}
	public void heat() throws OperationNotSupportedException {
		if(supportedOp[SiloSrSupportedOperations.HEAT.ordinal()])
			plantSR.itsSim.queue.add(new SimCmd(siloId,SimCmdCodes.HEATER_ON));
		else throw new OperationNotSupportedException("HEAT");
	}
	public void heatingCompleted() throws OperationNotSupportedException {
		if(supportedOp[SiloSrSupportedOperations.HEATING_COMPLETED.ordinal()])
			plantSR.itsSim.queue.add(new SimCmd(siloId,SimCmdCodes.HEATER_OFF));
		else throw new OperationNotSupportedException("HEATING_COMPLETED");

	}
	public void mix() throws OperationNotSupportedException {
		if(supportedOp[SiloSrSupportedOperations.MIX.ordinal()])
			plantSR.itsSim.queue.add(new SimCmd(siloId,SimCmdCodes.MIXER_ON));
		else throw new OperationNotSupportedException("MIX");
		
	}
	public void mixingCompleted() throws OperationNotSupportedException {
		if(supportedOp[SiloSrSupportedOperations.MIXING_COMPLETED.ordinal()])
			plantSR.itsSim.queue.add(new SimCmd(siloId,SimCmdCodes.MIXER_OFF));
		else throw new OperationNotSupportedException("MIXING_COMPLETED");
	}
}
