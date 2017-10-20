package simulator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Maintains the state of the simulator, and can be
 * passed along to allow shared manipulation of the
 * simulator state.
 * 
 * - Registers
 * - Flags
 * - Etc.
 * 
 * @author Ezekiel Elin
 *
 */
public class SimulatorState {
	/**
	 * The list of registers
	 */
	private ArrayList<SimulatorRegister> registers;
	
	private boolean isHalted = false;
	
	/**
	 * Negative result flag
	 */
	public boolean negative;
	
	/**
	 * Zero result flag
	 */
	public boolean zero;
	
	/**
	 * Carry result flag
	 */
	public boolean carry;
	
	/**
	 * Overflow result flag
	 */
	public boolean overflow;
	
	/**
	 * Create a SimulatorState with parameters
	 * @param registerCount The number of registers
	 * @param registerSize The size of the registers
	 */
	public SimulatorState(int registerCount, int registerSize) {
		this.registers = new ArrayList<>();
		for(int i = 0; i < registerCount; i++) {
			this.registers.add(new SimulatorRegister(registerSize, i));
		}
	}
	
	/**
	 * Get a register
	 * @param i The register number
	 * @return THe register
	 */
	public SimulatorRegister getRegister(int i) {
		return this.registers.get(i);
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < this.registers.size(); i++) {
			SimulatorRegister reg = this.getRegister(i);
			str.append("R" + i + "\t");
			str.append(reg.getValue());
			if (i < this.registers.size() - 1) {
				str.append("\n");
			}
		}
		return str.toString();
	}
}
