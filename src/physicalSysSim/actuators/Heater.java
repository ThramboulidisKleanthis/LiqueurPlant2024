package physicalSysSim.actuators;

import java.awt.Color;

import physicalSysSim.Silo;
import system.LPSystem;


public class Heater extends Device{
	boolean on=false;
	
	public Heater(String str,Silo itsSilo){
		super(str,itsSilo);
	}
	
	public void turnOn(){
		on = true;
		itsSilo.vis.h.setForeground(Color.RED);
	}
	
	public void turnOff(){
		on = false;
		itsSilo.vis.h.setForeground(Color.WHITE);
		itsSilo.vis.t.setForeground(Color.WHITE);
	}
}
