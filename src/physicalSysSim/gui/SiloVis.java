// based on VisSilo

package physicalSysSim.gui;

import java.awt.Color;
import physicalSysSim.PlantSim;

public class SiloVis extends LpButton{
	private static final long serialVersionUID = 1L;
	
	public LpButton f,e,in,out;
	public LpButton h,t;
	public LpButton m;
	
public SiloVis(int n, int x, int y, boolean h,boolean hs, boolean m, PlantSim pSim) {
		super("S"+(n+1),50+x+400, 50+y, 70, 120,Color.white,Color.black, pSim.itsVis);
		f=new LpButton("F",x+420, 60+y, 30, 20,Color.GREEN,Color.black,pSim.itsVis);
//		f.addActionListener(new FSensorButtonHandler(n)); 
				
		e=new LpButton("E",x+420, 140+y, 30, 20,Color.GREEN,Color.RED,pSim.itsVis);
//		e.addActionListener(new ESensorButtonHandler(n)); 
						
		in=new LpButton("", 70+x+400, 30+y, 20, 20,Color.red,Color.black,pSim.itsVis);
		out=new LpButton("", 70+x+400, 30+140+y, 20, 20,Color.red,Color.black,pSim.itsVis);
	if(h) 
		this.h=new LpButton("R4",120+x+400, 30+y+60, 40, 20,Color.green,Color.white,pSim.itsVis);
	if(hs)
		t=new LpButton("T",120+x+400, 30+y+40, 20, 20,Color.green,Color.black,pSim.itsVis );
	if(m)
		this.m=new LpButton("M",120+x+400, 30+y+90, 30, 40,Color.green,Color.black,pSim.itsVis);
	}
}