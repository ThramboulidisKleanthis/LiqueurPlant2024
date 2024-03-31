package process;

import java.awt.Color;
import java.sql.Timestamp;
import java.util.logging.Logger;

import physicalSysSim.SiloState;
import physicalSysSim.gui.PlantSimFrame;
import system.LPSystem;
import system.LpsLogger;
import systemSR.PlantSR;
import systemSR.SRCmd;
import systemSR.SRCmdCodes;


public class LTypeBGenProcess extends LPProcess implements Runnable {
//	public static Logger LOGGER = LPSystem.LOGGER;
	private static final Logger LOGGER = LpsLogger.getLogger(Class.class.getName());

	public LTypeBGenProcess(String name,PlantSR pSR, int units2Produce) {
		super(name,pSR,units2Produce);
	}

	@Override
	public void run() {
		ProcessCmd cmd = null;
		boolean pipeREqPending=false;
		var unitsProduced=0;
		boolean moreQuantity=LPSystem.LiqueurQuantity[1]!=0;

		if(moreQuantity) {
			LOGGER.info(new Timestamp(System.currentTimeMillis()) + "\ts2" +" Start Filling");
			pSR.queue.add(new SRCmd(1,SRCmdCodes.FILL,2));
		}
		while(moreQuantity) {
			try {
				cmd = queue.take();
//				System.out.println("[LTypeBGenProcess] cmd.siloId= " + cmd.siloId + "\tcmd.code= " + cmd.code);
			} catch (InterruptedException e) {e.printStackTrace();}
			switch(cmd.code) {
			case FILLING_COMPLETED:
				LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(cmd.siloId+1) +" Filling Completed");
				if(cmd.siloId==1) {
					LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts2 Start Heating");
					pSR.queue.add(new SRCmd(cmd.siloId,SRCmdCodes.HEAT,2));
				}
				else
					pSR.queue.add(new SRCmd(cmd.siloId,SRCmdCodes.POWER_RESERVE,2));
//					pSR.queue.add(new SRCmd(cmd.siloId,SRCmdCodes.MIX,2));
				break;
			case POURING_COMPLETED:
				LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(cmd.siloId+1) +" Pouring Completed");
				if(cmd.siloId==2) {
					if(pipeREqPending) {
						//System.out.println("[LTypeBGenProcess] serving pending pipeReq");
						pSR.queue.add(new SRCmd(1,SRCmdCodes.PIPE_RESERVE,2));
						pipeREqPending=false;
					}
					unitsProduced++;
					LOGGER.info("[LTypeBGenProcess] quantity produced so far: "+ unitsProduced);
					PlantSimFrame.report("[LTypeBGenProcess] quantity produced so far: "+ unitsProduced);
					if(unitsProduced==units2Produce)
						moreQuantity=false;
				} 
				break;			
			case HEATING_COMPLETED:
				LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(cmd.siloId+1) +" Heating Completed");
				if((cmd.siloId==1)) {
					if(pSR.itsSim.silo[2].getState()==SiloState.EMPTY) {
						pSR.queue.add(new SRCmd(1,SRCmdCodes.PIPE_RESERVE,2));
						//System.out.println("[LTypeBGenProcess] pipeReq was send");
					}
					else {
						pipeREqPending=true;
						//System.out.println("[LTypeBGenProcess] pipeReq was postponed");
					}
				}
				break;			
			case MIXING_COMPLETED:
				LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(cmd.siloId+1) +" Mixing Completed");
				LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(cmd.siloId+1) +" Start Pouring");
				pSR.queue.add(new SRCmd(cmd.siloId,SRCmdCodes.EMPTY,2));
//				LOGGER.info("[LTypeBGenProcess] quantity produced so far: "+ unitsProduced);
//				PlantSimFrame.report("[LTypeBGenProcess] quantity produced so far: "+ unitsProduced);
				break;
			case PIPE_AVAILABLE:
				//System.out.println("[LTypeBGenProcess] pipe availability response received");
				LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\tlgpB Start Transfer");
				pSR.queue.add(new SRCmd(1,SRCmdCodes.TRANSFER,2));
				break;
			case TRANSFER_COMPLETED:
				LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\tlgpB Transfer Completed");
				pSR.queue.add(new SRCmd(1,SRCmdCodes.PIPE_FREE,2));
				if(unitsProduced+1<units2Produce) {
					LOGGER.info(new Timestamp(System.currentTimeMillis()) + "\ts2"  +" Start Filling");
					pSR.queue.add(new SRCmd(1,SRCmdCodes.FILL,2));
				} 
				break;
			case POWER_AVAILABLE:
				LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(cmd.siloId+1) +" Start Mixing");
				pSR.queue.add(new SRCmd(cmd.siloId,SRCmdCodes.MIX,2));
				break;
			default:
				break;
			}
		}
		pSR.itsSim.itsVis.pBbutton.setBackground(Color.GREEN);

	}
	
	public void setpSR(PlantSR pSR) {
		this.pSR = pSR;
	}
}

