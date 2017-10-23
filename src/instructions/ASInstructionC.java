package instructions;

import common.Constants;
import common.NumberTools;
import simulator.SimulatorState;
import simulator.SimulatorController;

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
			if (SimulatorController.verbose) System.out.println("NOP!");
		} else if (this.token.equals("HALT")) {
			if (SimulatorController.verbose) System.out.println("HALT!");
			state.isHalted = true;
		}
	}
}
