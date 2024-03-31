package systemSR;

import java.awt.Color;
import physicalSysSim.PlantSim;

public class PipeSR {
	PlantSim pSim;
	private int ownerProcess=0;
	public PipeSR(PlantSim pSim) {
		this.pSim=pSim;
	}
	synchronized void reserve(int op) {
		//System.out.println("[PipeSR] pipe reserved for op " + op);
		ownerProcess=op;
		pSim.pipe.itsVis.setBackground((ownerProcess==1)?Color.cyan:Color.gray);
	}
	synchronized void free() {
		//System.out.println("[PipeSR] pipe freed for op " + ownerProcess);

		ownerProcess=0;
		pSim.pipe.itsVis.setBackground(Color.white);
	}
	synchronized public boolean occupied() {
		return ownerProcess!=0;
	}
	synchronized public int getOwnerProcess() {
		return ownerProcess;
	}

}
