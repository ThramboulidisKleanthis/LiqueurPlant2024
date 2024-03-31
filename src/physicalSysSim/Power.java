package physicalSysSim;

import java.awt.Color;

import physicalSysSim.gui.LpButton;

public class Power {
	public LpButton itsVis;
	PlantSim pSim;
	
	public Power(PlantSim pSim ){
		this.pSim=pSim;
		itsVis = new LpButton("Power",740, 180, 50, 65,Color.WHITE,Color.black,pSim.itsVis); 
	}
}
