package instructions;

import simulator.SimulatorState;

public abstract class PerformableInstruction extends AssemblerInstruction {
	/**
	 * Perform the instruction
	 * @param state The simulator state
	 */
	public void perform(SimulatorState state) {
		state.lastInstruction = this.sourceStringRepresentation();
		System.out.println("Performing " + this.sourceStringRepresentation());
	}
}
