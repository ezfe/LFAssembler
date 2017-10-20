package instructions;

import simulator.SimulatorState;

public abstract class PerformableInstruction extends AssemblerInstruction {
	/**
	 * Perform the instruction
	 * @param state The simulator state
	 */
	public void perform(SimulatorState state) {
		System.out.println("Performing " + this.sourceStringRepresentation());
	}
}
