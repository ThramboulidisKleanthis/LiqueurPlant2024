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


public class LTypeAGenProcess extends LPProcess  {
//	public static Logger LOGGER = LPSystem.LOGGER;
	private static final Logger LOGGER = LpsLogger.getLogger(Class.class.getName());
	
	public LTypeAGenProcess(String name,PlantSR pSR, int units2Produce) {
		super(name,pSR,units2Produce);
	}

	@Override
	public void run() {
		ProcessCmd cmd = null;
		boolean pipeReqPending=false;
		var unitsProduced=0;
		boolean moreQuantity=LPSystem.LiqueurQuantity[1]!=0;

		if(moreQuantity) {	
			LOGGER.info(new Timestamp(System.currentTimeMillis()) + "\ts1" +" Start Filling");
			pSR.queue.add(new SRCmd(0,SRCmdCodes.FILL,1));
		}
		while(moreQuantity) {
			try {
				cmd = queue.take();
	//			System.out.println("[LTypeAGenProcess] cmd.siloId= " + cmd.siloId + "\tcmd.code= " + cmd.code);
			} catch (InterruptedException e) {e.printStackTrace();}
			switch(cmd.code) {
			case FILLING_COMPLETED:
				LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(cmd.siloId+1) +" Filling Completed");
				if((cmd.siloId==0)) {
					if(pSR.itsSim.silo[3].getState()==SiloState.EMPTY) {
						//System.out.println("[LTypeAGenProcess] pipeReq was send");
						pSR.queue.add(new SRCmd(0,SRCmdCodes.PIPE_RESERVE,1));
					}
					else {
						//System.out.println("[LTypeAGenProcess] pipeReq was postponed");
						pipeReqPending=true;
					}
				} else {
					LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts4 Start Heating");
					pSR.queue.add(new SRCmd(cmd.siloId,SRCmdCodes.HEAT,1));
				}
				break;
			case POURING_COMPLETED:
//				//System.out.println("[LTypeAGenProcess] servicing POURING_COMPLETED. pipeReqPending: " + pipeReqPending); 
				LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(cmd.siloId+1) +" Pouring Completed");
				if(cmd.siloId==3) {
					if(pipeReqPending) {
						//System.out.println("[LTypeAGenProcess] serving pending pipeReq");
						pSR.queue.add(new SRCmd(0,SRCmdCodes.PIPE_RESERVE,1));
						pipeReqPending=false;
					}
					unitsProduced++;
					LOGGER.info("[LTypeAGenProcess] quantity produced so far: "+ unitsProduced);
					PlantSimFrame.report("[LTypeAGenProcess] quantity produced so far: "+ unitsProduced);
					if(unitsProduced==units2Produce)
						moreQuantity=false;
				}
				break;			
			case HEATING_COMPLETED:
				LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(cmd.siloId+1) +" Heating Completed");
				pSR.queue.add(new SRCmd(cmd.siloId,SRCmdCodes.POWER_RESERVE,1));
				break;			
			case MIXING_COMPLETED:
				LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(cmd.siloId+1) +" Mixing Completed");
				LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(cmd.siloId+1) +" Start Pouring");
				pSR.queue.add(new SRCmd(cmd.siloId,SRCmdCodes.EMPTY,1));
//				LOGGER.info("[LTypeAGenProcess] quantity produced so far: "+ unitsProduced);
//				PlantSimFrame.report("[LTypeAGenProcess] quantity produced so far: "+ unitsProduced);
				break;	
			case PIPE_AVAILABLE:
				//System.out.println("[LTypeAGenProcess] pipe availability response received");
				LOGGER.info(new Timestamp(System.currentTimeMillis()) +"\tlgpA Start Transfer");
				pSR.queue.add(new SRCmd(0,SRCmdCodes.TRANSFER,1));
				break;
			case TRANSFER_COMPLETED:
				LOGGER.info(new Timestamp(System.currentTimeMillis()) +"\tlgpA Transfer Completed");
				pSR.queue.add(new SRCmd(0,SRCmdCodes.PIPE_FREE,1));
				if(unitsProduced+1<units2Produce) {
					LOGGER.info(new Timestamp(System.currentTimeMillis()) + "\ts1"  +" Start Filling");
					pSR.queue.add(new SRCmd(0,SRCmdCodes.FILL,1));
				} 
				break;
			case POWER_AVAILABLE:
				LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(cmd.siloId+1) +" Start Mixing");
				pSR.queue.add(new SRCmd(cmd.siloId,SRCmdCodes.MIX,1));
				break;
			default:
				break;
			}
		}
		pSR.itsSim.itsVis.pAbutton.setBackground(Color.GREEN);
	}
	
}

