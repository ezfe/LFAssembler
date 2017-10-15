package instructions;

import common.Constants;
import common.NumberTools;

public class ASInstructionC extends AssemblerInstruction {

	public ASInstructionC(String token) {
		this.token = token;
	}
	
	@Override
	public String sourceStringRepresentation() {
		return this.token;
	}

	@Override
	public String binaryStringRepresentation() {
		return NumberTools.rpad(this.opcodeBinaryString(), '0', Constants.INSTRUCTION_LENGTH);
	}
}
