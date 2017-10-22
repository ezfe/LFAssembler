package simulator;

import java.util.ArrayList;

import common.BitSet;
import common.Constants;

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
	public final int registerCount;
	public final int registerSize;
	
	public BitSet memory;
	public int programCounter;

	public String lastInstruction = "- - -";

	/**
	 * Has the simulator halted execution
	 */
	public boolean isHalted = false;
	
	/**
	 * Negative result flag
	 */
	public boolean negativeFlag;
	
	/**
	 * Zero result flag
	 */
	public boolean zeroFlag;
	
	/**
	 * Carry result flag
	 */
	public boolean carryFlag;
	
	/**
	 * Overflow result flag
	 */
	public boolean overflowFlag;
	
	/**
	 * Zero register
	 */
	public SimulatorRegister zeroRegister;
	
	/**
	 * Stack Register
	 */
	public SimulatorRegister stackRegister;
	
	/**
	 * Branch Link Register
	 */
	public SimulatorRegister branchLinkRegister;
	
	/**
	 * Create a SimulatorState with parameters
	 * @param registerCount The number of registers
	 * @param registerSize The size of the registers
	 */
	public SimulatorState(int registerCount, int registerSize, BitSet bits) {
		this.registerCount = registerCount;
		this.registerSize = registerSize;
		this.registers = new ArrayList<>();
		for(int i = 0; i < registerCount; i++) {
			this.registers.add(new SimulatorRegister(registerSize, i));
		}
		
		this.programCounter = 0;
		this.memory = bits;
		
		this.zeroRegister = new SimulatorRegister(registerSize, registerCount + 1);
		this.zeroRegister.setValue("0");
		this.zeroRegister.lockRegister();
		
		this.stackRegister = new SimulatorRegister(registerSize, registerCount + 2);
		this.stackRegister.setValue("0");
		
		this.branchLinkRegister = new SimulatorRegister(registerSize, registerCount + 3);
		this.branchLinkRegister.setValue("0");
	}
	
	/**
	 * Get a register
	 * @param i The register number
	 * @return THe register
	 */
	public SimulatorRegister getRegister(int i) {
		if (i == Constants.ZERO_REGISTER_NUMBER) {
			return this.zeroRegister;
		} else if (i == Constants.LINK_REGISTER_NUMBER) {
			return this.branchLinkRegister;
		} else if (i < this.registerCount) {
			return this.registers.get(i);
		} else {
			throw new IndexOutOfBoundsException("Register " + i + " is out of bounds (" + this.registerCount + " registers)");
		}
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("General Registers...\n");
		for(int i = 0; i < this.registers.size(); i++) {
			SimulatorRegister reg = this.getRegister(i);
			str.append("R" + i + "\t");
			str.append(reg.getValue() + "\t");
			str.append(reg.getNumericalValue());
			str.append("\n");
		}

		str.append("Link Register:\n---\t");
		str.append(branchLinkRegister.getValue() + "\t");
		str.append(branchLinkRegister.getNumericalValue());
		str.append("\n");

		str.append("Last Instruction Executed:\n");
		str.append(lastInstruction);

		return str.toString();
	}
}
