package physicalSysSim;

import system.Cmd;

public class SimCmd extends Cmd{
	public SimCmdCodes code;
	public SimCmd(int siloId,SimCmdCodes code) {
		super(siloId);
		this.code=code;
	}
}
