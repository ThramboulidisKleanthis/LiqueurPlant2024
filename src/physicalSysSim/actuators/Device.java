package physicalSysSim.actuators;

import physicalSysSim.Silo;

public class Device {
	String name;
	Silo itsSilo;
	protected boolean updateVisual;
		
	protected Device(String str, Silo itsSilo){
		name = str;
		this.itsSilo = itsSilo;
	}
}
