package process;

import java.util.concurrent.ArrayBlockingQueue;

import systemSR.PlantSR;

public class LPProcess implements Runnable{
	String name;
	public PlantSR pSR;
	int units2Produce=0;
	public ArrayBlockingQueue<ProcessCmd> queue;

	
	public LPProcess(String name, PlantSR pSR,int units2Produce) {
		this.name=name;
		this.units2Produce=units2Produce;
		this.pSR=pSR;
		queue = new ArrayBlockingQueue<ProcessCmd>(50);
	}
	
	public void setpSR(PlantSR pSR) {
		this.pSR = pSR;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}
}
