package physicalSysSim;

import java.awt.Color;
//import java.sql.Timestamp;
//import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import physicalSysSim.actuators.*;
import physicalSysSim.gui.PlantSimFrame;
import physicalSysSim.gui.SiloVis;
import physicalSysSim.sensors.*;
import system.LPSystem;
import systemSR.SRCmd;
import systemSR.SRCmdCodes;

public class Silo {
	public int id;
	PlantSim itsPlant;
	private volatile SiloState state;
	SiloState prevState;
	public SiloVis vis;
	Valve in, out;
	Mixer m;
	Heater h;
	FillingLevelSensor fs;
	EmptyingLevelSensor es;
	HeatingCompletedSensor hs;
	MixingCompletedSensor ms;
	public Timer []timer = new Timer[4];

	Color sColor,prevColor;
	Color []visColor= {Color.white,Color.cyan,Color.blue,Color.LIGHT_GRAY,Color.orange,Color.magenta};
					  // 0=empty  , 1=filling, 2=full,    3=pouring,       4=heating,    5=mixing
	private boolean transferCompletedSend=false;
	
	public Silo(int id, PlantSim pSim) {
		this.id=id;
		itsPlant=pSim;
		setState(SiloState.EMPTY);
		fs = new FillingLevelSensor(this, pSim);
		es = new EmptyingLevelSensor(this, pSim);
		sColor=Color.white;
	}
	
	synchronized public void openInValve() {
//		System.out.println("openInValve executed on silo " + id);
		in.open();
		state=SiloState.FILLING;
		setVis(state);
//		LPSystem.LOGGER.info(new Timestamp(System.currentTimeMillis()) + "\ts" +(this.id+1) +" Start Filling");
//		PlantSimFrame.report("s" + (this.id+1) +" Start Filling");
//		timer[SiloParam.FILLING.ordinal()] = new Timer();
////		timer[0].schedule(new LpTimerTask(0,fs,this), PlantSimParams.fillingTime[id]);
//		timer[SiloParam.FILLING.ordinal()].schedule(new LpTimerTask(0,fs,this), PlantSimParams.val[this.id][SiloParam.FILLING.ordinal()]);
		vis.e.setForeground(Color.BLACK);

		new Thread(()->{
			int lLevel=0;
			while(lLevel<1980){ 
				itsPlant.itsVis.ll[id].setValue(lLevel);    
				lLevel+=20;    

				try{Thread.sleep(PlantSimParams.val[id][SiloParam.FILLING.ordinal()]);}catch(Exception e){} 
				if(lLevel>1970) {
//					fs.trigger();
					if((id==0)||(id==1))
						itsPlant.itsSR.queue.add(new SRCmd(id,SRCmdCodes.HIGH_LEVEL_REACHED,(id==0)?1:2));
					else
						itsPlant.itsSR.queue.add(new SRCmd(id,SRCmdCodes.TRANSFER_COMPLETED,(id==3)?1:2));
					vis.f.setForeground(Color.RED);
				}
			}
//			fs.trigger();
		}).start();
	}
	
	synchronized public void closeInValve() {
		in.close();
		state=SiloState.FULL;
		setVis(state);
//		LPSystem.LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(this.id+1) +" Filling Completed");
//		PlantSimFrame.report("s" + (this.id+1) +" Filling Completed");

	}
	
	synchronized public void openOutValve() {
		out.open();
		state=SiloState.POURING;
		setVis(state);
//		LPSystem.LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(this.id+1) +" Start Pouring");
//		PlantSimFrame.report("s" + (this.id+1) +" Start Pouring");
//		timer[siloParam.POURING.ordinal()] = new Timer();
////		timer[1].schedule(new LpTimerTask(1,es,this), PlantSimParams.pouringTime[id]);
//		timer[siloParam.POURING.ordinal()].schedule(new LpTimerTask(1,es,this), PlantSimParams.val[this.id][siloParam.POURING.ordinal()]);
		vis.f.setForeground(Color.BLACK);
//		System.out.println("[Silo " +id +" ] openOutValve executed Silo State =" +state);
		
		new Thread(()->{
			int lLevel=2000;
			while(lLevel>20){    
				itsPlant.itsVis.ll[id].setValue(lLevel);    
				lLevel-=20;    
				try{Thread.sleep(PlantSimParams.val[id][SiloParam.POURING.ordinal()]);}catch(Exception e){} 
				if(lLevel<30) {
//					es.trigger();
					if((id==2)||(id==3))
						itsPlant.itsSR.queue.add(new SRCmd(id,SRCmdCodes.LOW_LEVEL_REACHED,(id==3)?1:2));
					else
						itsPlant.itsSR.queue.add(new SRCmd(id,SRCmdCodes.TRANSFER_COMPLETED,(id==0)?1:2));
					vis.e.setForeground(Color.RED);
				}
			} 
		}).start();

	}
	
	synchronized public void closeOutValve() {
		out.close();
		state=SiloState.EMPTY;
		setVis(state);
		vis.e.setForeground(Color.RED);
//		LPSystem.LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(this.id+1) +" Pouring Completed");
//		PlantSimFrame.report("s" + (this.id+1) +" Pouring Completed");

	}
	
	synchronized public void mixerOn() {
		m.turnOn();
		prevState = state;
		state=SiloState.MIXING;
		setVis(state);
//		LPSystem.LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(this.id+1) +" Start Mixing");
//		PlantSimFrame.report("s" + (this.id+1) +" Start Mixing");
		itsPlant.power.itsVis.setBackground(Color.RED);
		timer[SiloParam.MIXING.ordinal()] = new Timer();
//		timer[3].schedule(new LpTimerTask(2,ms,this), PlantSimParams.MIXING_TIME);
		timer[SiloParam.MIXING.ordinal()].schedule(new LpTimerTask(3,ms,this), PlantSimParams.val[this.id][SiloParam.MIXING.ordinal()]);
	}

	synchronized public void mixerOff() {
		m.turnOff();
		state=prevState;
		setVis(state);
		itsPlant.power.itsVis.setBackground(Color.WHITE);
//		PlantSimFrame.report("s" + (this.id+1) +" Mixing Completed");
//		LPSystem.LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(this.id+1) +" Mixing Completed");
	}
	
	synchronized public void heaterOn() {
		h.turnOn();
		prevState = state;
		state=SiloState.HEATING;
		setVis(state);
		//	PlantSimFrame.report("s" + (this.id+1) +" Start Heating");
//		LPSystem.LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(this.id+1) +" Start Heating");
		timer[SiloParam.HEATING.ordinal()] = new Timer();
//		timer[2].schedule(new LpTimerTask(3,hs,this), PlantSimParams.HEATING_TIME);
		timer[SiloParam.HEATING.ordinal()].schedule(new LpTimerTask(2,hs,this), PlantSimParams.val[this.id][SiloParam.HEATING.ordinal()]);
	}
	
	synchronized public void heaterOff() {
		h.turnOff();
		state=prevState;
		setVis(state);
//		PlantSimFrame.report("s" + (this.id+1) +" Heating Completed");
//		LPSystem.LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(this.id+1) +" Heating Completed");
	}

	private void setVis(SiloState st){
		vis.setBackground(visColor[st.ordinal()]);
	}

	synchronized public SiloState getState() {
		return state;
	}

	synchronized public void setState(SiloState state) {
		this.state = state;
	}

	synchronized public boolean isTransferCompletedSend() {
		return transferCompletedSend;
	}

	synchronized public void setTransferCompletedSend(boolean transferCompletedSend) {
		this.transferCompletedSend = transferCompletedSend;
	}
}

class LpTimerTask extends TimerTask {
	private LevelSensor ls;
	private int timerId;
	Silo s;
	
	public LpTimerTask(int timerId,LevelSensor ls,Silo s){
		this.ls = ls;
		this.timerId=timerId;
		this.s=s;
		s.setTransferCompletedSend(false);
	}
    public void run() {
    	ls.trigger();
    	s.timer[timerId].cancel(); //Terminate the timer thread
    }
} 
