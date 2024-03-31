package physicalSysSim.actuators;

import java.awt.Button;
import java.awt.Color;
import system.LPSystem;

public class Valve implements ValveIf{
	String name;
	Button itsGui;
	
	public Valve(String str, Button b){
		name=str;
		itsGui=b;
	}

	public void open(){
//		LPSystem.LOGGER.info("Valve " + name + " opened");
		itsGui.setBackground(Color.green);
	}
	
	public void close(){
//		LPSystem.LOGGER.info("Valve " + name + " closed");
		itsGui.setBackground(Color.red);
	}
}
