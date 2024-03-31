package physicalSysSim.actuators;

import java.awt.Color;

import physicalSysSim.Silo;
import system.LPSystem;

public class Mixer extends Device {
	boolean on=false;
	
	public Mixer(String str,Silo itsSilo){
		super(str,itsSilo);
	}
	
	public void turnOn(){
		on=true;
		itsSilo.vis.m.setForeground(Color.RED);
	}
	
	
	public void turnOff(){  //it is instead of a timer for flexibility, this is why it reports directly to controller
		on=false;
		itsSilo.vis.m.setForeground(Color.BLACK);
		}
	}