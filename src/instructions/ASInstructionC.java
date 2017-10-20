package instructions;

import common.Constants;
import common.NumberTools;
import simulator.SimulatorState;

public class ASInstructionC extends PerformableInstruction {

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

	@Override
	public void perform(SimulatorState state) {
		super.perform(state);
		
		if (this.token.equals("NOP")) {
			// Do nothing
		} else if (this.token.equals("HALT")) {
			state.isHalted = true;
		}
	}
}
