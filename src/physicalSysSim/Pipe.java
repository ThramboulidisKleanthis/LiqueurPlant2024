package physicalSysSim;

import java.awt.Color;
import physicalSysSim.gui.LpButton;

public class Pipe {
	public LpButton itsVis;
	PlantSim pSim;
	
	public Pipe(PlantSim pSim){
		this.pSim=pSim;
		itsVis = new LpButton("Pipe",50+400, 50+140, 270, 40,Color.WHITE,Color.BLACK,pSim.itsVis);
	}
}
