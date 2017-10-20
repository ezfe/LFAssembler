package instructions;

import simulator.SimulatorState;

public interface Performable {
	/**
	 * Perform the instruction
	 * @param state The simulator state
	 */
	public void perform(SimulatorState state);
}
