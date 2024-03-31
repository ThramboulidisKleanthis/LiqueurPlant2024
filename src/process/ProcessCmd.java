package process;

import system.Cmd;

public class ProcessCmd extends Cmd {
	public ProcessCmdCodes code;
	public int processId;
	public ProcessCmd(int siloId,ProcessCmdCodes code,int processId) {
		super(siloId);
		this.code=code;
		this.processId=processId;
	}
}
