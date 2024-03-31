package systemSR;

import java.awt.Color;
import physicalSysSim.PlantSim;

public class PowerSR {
	PlantSim pSim;
	private int ownerProcess=0;
	
	public PowerSR(PlantSim pSim) {
		this.pSim=pSim;
	}
	synchronized void reserve(int op) {
		//System.out.println("[PowerSR] power reserved for op " + op);
		ownerProcess=op;
		pSim.power.itsVis.setBackground(Color.RED);
	}
	
	synchronized void free() {
		//System.out.println("[PowerSR] power freed for op " + ownerProcess);

		ownerProcess=0;
		pSim.power.itsVis.setBackground(Color.white);
	}
	
	synchronized public boolean occupied() {
		return ownerProcess!=0;
	}
	
	synchronized public int getOwnerProcess() {
		return ownerProcess;
	}
}
