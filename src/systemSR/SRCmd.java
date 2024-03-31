package systemSR;

import system.Cmd;

public class SRCmd extends Cmd{
	public SRCmdCodes code;
	public int processId;
	public SRCmd(int siloId,SRCmdCodes code,int processId) {
		super(siloId);
		this.code=code;
		this.processId=processId;
	}
}
