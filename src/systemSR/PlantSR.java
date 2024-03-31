package systemSR;

import java.sql.Timestamp;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import javax.naming.OperationNotSupportedException;

import physicalSysSim.PlantSim;
import process.LPProcess;
import process.ProcessCmd;
import process.ProcessCmdCodes;
import system.LPSystem;
import system.LpsLogger;

public class PlantSR implements Runnable {
	private static final Logger LOGGER = LpsLogger.getLogger(Class.class.getName());
	public PlantSim itsSim;
	public LPSystem itsSystem;
	public ArrayBlockingQueue<SRCmd> queue;
	SiloSR[] siloSR;
	boolean[][] siloSupOps= {{false,false,false,false},
			{true,true,false,false},
			{false,false,true,true},
			{true,true,true,true},
	};
	public PipeSR pipeSR;
	public PowerSR powerSR;

	SRCmd cmd;

	int [] transferCompleted= new int[2];

	public PlantSR(LPSystem itsSystem,PlantSim itsSim) {
		this.itsSystem=itsSystem;
		this.itsSim = itsSim;
		queue = new ArrayBlockingQueue<SRCmd>(50);
		siloSR = new SiloSR[4];
		for(int i=0;i<4;i++)
			siloSR[i]= new SiloSR(i,this,siloSupOps[i]);
		pipeSR=new PipeSR(itsSim);
		powerSR=new PowerSR(itsSim);
	}

	@Override
	public void run() {
		int siloId=0;
//		int processIndex=0;
		LPProcess targetProcess;
		SRCmd pendingPipeAvailReq=null;
		SRCmd pendingPowerAvailReq=null;
		
		while(true) {
			try {
				cmd = queue.take();
				//System.out.println("[PlantSR] cmd.siloId= " + cmd.siloId + "\tcmd.code= " + cmd.code);
			} catch (InterruptedException e) {e.printStackTrace();}
			siloId=cmd.siloId;
//			processIndex=((cmd.siloId==0)||(cmd.siloId==3))?0:1;
			targetProcess=itsSystem.process[cmd.processId-1];
			switch(cmd.code) {
			case FILL:
				siloSR[cmd.siloId].fill();
				break;
			case HIGH_LEVEL_REACHED:
				siloSR[cmd.siloId].highLevelReached();
				targetProcess.queue.add(new ProcessCmd(siloId,ProcessCmdCodes.FILLING_COMPLETED,cmd.processId));
				break;
			case EMPTY:
				siloSR[cmd.siloId].empty();
				break;
			case LOW_LEVEL_REACHED:
				siloSR[cmd.siloId].lowLevelReached();
				targetProcess.queue.add(new ProcessCmd(siloId,ProcessCmdCodes.POURING_COMPLETED,cmd.processId));
				break;
			case MIX:
				try {
					siloSR[cmd.siloId].mix();
				} catch (OperationNotSupportedException e) {e.printStackTrace();}
				break;
			case MIXING_COMPLETED:
				try {
					siloSR[cmd.siloId].mixingCompleted();
				} catch (OperationNotSupportedException e) {e.printStackTrace();}
				targetProcess.queue.add(new ProcessCmd(siloId,ProcessCmdCodes.MIXING_COMPLETED,cmd.processId));
				pendingPowerAvailReq=powerFree(pendingPowerAvailReq);
				break;
			case HEAT:
				try {
					siloSR[cmd.siloId].heat();
				} catch (OperationNotSupportedException e) {e.printStackTrace();}
				break;
			case HEATING_COMPLETED:
				try {
					siloSR[cmd.siloId].heatingCompleted();
				} catch (OperationNotSupportedException e) {e.printStackTrace();}
				targetProcess.queue.add(new ProcessCmd(siloId,ProcessCmdCodes.HEATING_COMPLETED,cmd.processId));
				break;
			case PIPE_RESERVE:
				if(pipeSR.getOwnerProcess()==0) {
					pipeSR.reserve(cmd.processId);
					targetProcess.queue.add(new ProcessCmd(cmd.siloId,ProcessCmdCodes.PIPE_AVAILABLE,cmd.processId));
					//System.out.println("[PlantSR] PIPE_RESERVE serviced for process= " + cmd.processId );
				} else {
					pendingPipeAvailReq=cmd;
					//System.out.println("[PlantSR] PIPE_RESERVE serviced as pending for process= " + cmd.processId);
					//					pipePendingPr=(cmd.siloId==0?1:2);
				}
				break;
			case PIPE_FREE:
				//System.out.println("[PlantSR] PIPE_FREE cmd serviced from process: " + cmd.processId);
				pipeSR.free();
				transferCompleted[cmd.siloId]=0;
				break;				
			case TRANSFER:
				//System.out.println("[PlantSR] TRANSFER cmd serviced for process: " + cmd.processId);
				transfer(cmd);
				break;
			case TRANSFER_COMPLETED:		//from Sim
				//System.out.println("[PlantSR] TRANSFER_COMPLETED cmd servicing. transferCompleted[0]= " + transferCompleted[0] +
				//		"\ttransferCompleted[1]= " + transferCompleted[1]);
//				if(processIndex==0) 
				if(cmd.processId==1) 
					if(++transferCompleted[0]==2) {
						siloSR[0].lowLevelReached();
//						itsSystem.queue.add(new ProcessCmd(0,ProcessCmdCodes.POURING_COMPLETED,1));
						targetProcess.queue.add(new ProcessCmd(0,ProcessCmdCodes.POURING_COMPLETED,1));

						siloSR[3].highLevelReached();
//						itsSystem.queue.add(new ProcessCmd(3,ProcessCmdCodes.FILLING_COMPLETED,1));
						targetProcess.queue.add(new ProcessCmd(3,ProcessCmdCodes.FILLING_COMPLETED,1));

//					itsSystem.queue.add(new ProcessCmd(0,ProcessCmdCodes.TRANSFER_COMPLETED,1));
						targetProcess.queue.add(new ProcessCmd(0,ProcessCmdCodes.TRANSFER_COMPLETED,1));
						pipeSR.free();
						transferCompleted[0]=0;
						if(pendingPipeAvailReq!=null) {
							//System.out.println("[PlantSR] servicing pending pipe req for process: " + pendingPipeAvailReq.processId);
							pipeSR.reserve(pendingPipeAvailReq.processId);
							itsSystem.process[((pendingPipeAvailReq.siloId==0)||(pendingPipeAvailReq.siloId==3))?0:1].queue.add(new ProcessCmd(pendingPipeAvailReq.siloId,ProcessCmdCodes.PIPE_AVAILABLE,pendingPipeAvailReq.processId));
							pendingPipeAvailReq=null;
						}

					}
				if(cmd.processId==2)
					if(++transferCompleted[1]==2) {
						siloSR[1].lowLevelReached();
//						itsSystem.queue.add(new ProcessCmd(1,ProcessCmdCodes.POURING_COMPLETED,2));
						targetProcess.queue.add(new ProcessCmd(1,ProcessCmdCodes.POURING_COMPLETED,2));

						siloSR[2].highLevelReached();
//						itsSystem.queue.add(new ProcessCmd(2,ProcessCmdCodes.FILLING_COMPLETED,2));
						targetProcess.queue.add(new ProcessCmd(2,ProcessCmdCodes.FILLING_COMPLETED,2));

//						itsSystem.queue.add(new ProcessCmd(1,ProcessCmdCodes.TRANSFER_COMPLETED,2));
						targetProcess.queue.add(new ProcessCmd(1,ProcessCmdCodes.TRANSFER_COMPLETED,2));
						pipeSR.free();
						transferCompleted[1]=0;
						if(pendingPipeAvailReq!=null) {
							//System.out.println("[PlantSR] servicing pending pipe req for process: " + pendingPipeAvailReq.processId);
							pipeSR.reserve(pendingPipeAvailReq.processId);
							itsSystem.process[((pendingPipeAvailReq.siloId==0)||(pendingPipeAvailReq.siloId==3))?0:1].queue.add(new ProcessCmd(pendingPipeAvailReq.siloId,ProcessCmdCodes.PIPE_AVAILABLE,pendingPipeAvailReq.processId));
							pendingPipeAvailReq=null;
						}

					}
				break;
			case POWER_RESERVE:
				//System.out.println("[PlantSR]  POWER_RESERVE servicing");
				if(powerSR.getOwnerProcess()==0) {
					powerSR.reserve(cmd.processId);
					targetProcess.queue.add(new ProcessCmd(cmd.siloId,ProcessCmdCodes.POWER_AVAILABLE,cmd.processId));
					//System.out.println("[PlantSR] POWER_RESERVE serviced for process= " + cmd.processId );
				} else {
					pendingPowerAvailReq=cmd;
					//System.out.println("[PlantSR] POWER_RESERVE serviced as pending for process= " + cmd.processId);
				}
				break;
			case POWER_FREE:
			
				break;				
			default:
				LPSystem.LOGGER.info("[PlantSR] Command not supported");
				break;
			}
		}
	}


	private SRCmd powerFree(SRCmd pendingPowerAvailReq) {
		powerSR.free();
		//System.out.println("[PlantSR] POWER_FREE cmd serviced from process: " + cmd.processId);
		powerSR.free();
		if(pendingPowerAvailReq!=null) {
			//System.out.println("[PlantSR] servicing pending power req for process: " + pendingPowerAvailReq.processId);
			powerSR.reserve(pendingPowerAvailReq.processId);
			itsSystem.process[((pendingPowerAvailReq.siloId==0)||(pendingPowerAvailReq.siloId==3))?0:1].queue.add(
					new ProcessCmd(pendingPowerAvailReq.siloId,ProcessCmdCodes.POWER_AVAILABLE,
							pendingPowerAvailReq.processId));
			return null;
		}
		return null;
	}

	private void transfer(SRCmd cmd) {
//		LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(cmd.processId==1?1:2) +" Start Pouring");
//		LOGGER.info(new Timestamp(System.currentTimeMillis())+ "\ts" +(cmd.processId==1?4:3) +" Start Filling");
		if(cmd.processId==1) {
			siloSR[0].empty();
			siloSR[3].fill();
		} else if(cmd.processId==2){
			siloSR[1].empty();
			siloSR[2].fill();
		}
	}
}
