package instructions;

import simulator.SimulatorState;
import simulator.SimulatorController;

public abstract class PerformableInstruction extends AssemblerInstruction {
	/**
	 * Perform the instruction
	 * @param state The simulator state
	 */
	public void perform(SimulatorState state) {
		state.lastInstruction = this.sourceStringRepresentation();
		if (SimulatorController.verbose) System.out.println("Performing " + this.sourceStringRepresentation());
	}
}
