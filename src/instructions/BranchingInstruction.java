package instructions;

import java.util.Optional;

import simulator.SimulatorState;

public abstract class BranchingInstruction extends PerformableInstruction {
	/**
	 * Attempts to modify the passed simulator state with the passed address.
	 * 
	 * @param state The simulator state
	 * @param address The address
	 */
	protected void branchWith(SimulatorState state, Optional<Integer> address) {
		if (address.isPresent()) {
			this.branchWith(state, address.get());
		} else {
			throw new IllegalStateException("No labelAddress found");
		}
	}
	
	/**
	 * Modifies the passed simulator state with the passed address.
	 * @param state The simulator state
	 * @param address The address
	 */
	protected void branchWith(SimulatorState state, int address) {
		System.out.println("Branched...");
		state.programCounter = address;
	}
}
