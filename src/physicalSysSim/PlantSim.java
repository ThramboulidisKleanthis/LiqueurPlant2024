// based on the PlantSimFrame of the previous version

package physicalSysSim;
import java.util.concurrent.ArrayBlockingQueue;
import physicalSysSim.gui.PlantSimFrame;
import systemSR.PlantSR;

public class PlantSim extends Thread {
	public PlantSR itsSR;
	public PlantSimFrame itsVis;
	public Silo []silo;
	public Pipe pipe;
	public Power power;
	public ArrayBlockingQueue<SimCmd> queue;
	SimCmd cmd;
	
	public PlantSim() {
		silo = new Silo[4];
		itsVis = new PlantSimFrame(this);
		silo[0]= new SimpleSilo(0,this);
		silo[1]= new HeatedSilo(1,this);
		silo[2] = new MixSilo(2,this);
		silo[3] = new MixHeatedSilo(3,this);
		pipe = new Pipe(this);
		power= new Power(this);
		queue = new ArrayBlockingQueue<SimCmd>(100);
	}
	
	public void run() {
		while(true){
			try {
				cmd=queue.take();
//				System.out.println("[PlantSim] cmd.siloId= " + cmd.siloId + "\tcmd.code= " + cmd.code);
			} catch (InterruptedException e) {e.printStackTrace();	}
			switch(cmd.code) {
				case OPEN_INVALVE:
					silo[cmd.siloId].openInValve();
					break;
				case CLOSE_INVALVE:
					silo[cmd.siloId].closeInValve();
					break;
				case OPEN_OUTVALVE:
					silo[cmd.siloId].openOutValve();
					break;
				case CLOSE_OUTVALVE:
					silo[cmd.siloId].closeOutValve();
					break;
				case MIXER_ON:
					silo[cmd.siloId].mixerOn();
					break;
				case MIXER_OFF:
					silo[cmd.siloId].mixerOff();
					break;
				case HEATER_ON:
					silo[cmd.siloId].heaterOn();
					break;
				case HEATER_OFF:
					silo[cmd.siloId].heaterOff();
					break;
			}
		}
	}
	
	public void setItsSR(PlantSR itsSR) {
		this.itsSR = itsSR;
	}
}