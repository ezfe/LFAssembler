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
	
	/**
	 * Stores main memory of the simulator
	 */
	public BitSet memory;
	
	/**
	 * The current instruction index
	 */
	public int programCounter;

	/**
	 * The string representing the last instruction
	 *
	 * Used to print CPU state
	 */
	public String lastInstruction = "No instruction run yet";

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
	 * @param bits The memory object
	 */
	public SimulatorState(int registerCount, int registerSize, BitSet bits) {
		this.registerCount = registerCount;
		this.registerSize = registerSize;
		this.registers = new ArrayList<>();
		for(int i = 0; i < registerCount; i++) {
			this.registers.add(new SimulatorRegister(registerSize, i));
		}
		if (SimulatorController.verbose) System.out.println("Created " + registerCount + " general registers");
		
		if (SimulatorController.verbose) System.out.println("Initialized program counter to zero");
		this.programCounter = 0;
		
		if (SimulatorController.verbose) System.out.println("Loaded memory");
		this.memory = bits;
		
		if (SimulatorController.verbose) System.out.println("Initialized and locked zero register to zero");
		this.zeroRegister = new SimulatorRegister(registerSize, registerCount + 1);
		this.zeroRegister.setValue("0");
		this.zeroRegister.lockRegister();
		
		if (SimulatorController.verbose) System.out.println("Initialized stack register to zero");
		this.stackRegister = new SimulatorRegister(registerSize, registerCount + 2);
		this.stackRegister.setValue("0");
		
		if (SimulatorController.verbose) System.out.println("Initialized branch link register to zero");
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
			if (SimulatorController.verbose) System.out.println("Converted " + i + " to ZERO");
			return this.zeroRegister;

		} else if (i == Constants.LINK_REGISTER_NUMBER) {
			if (SimulatorController.verbose) System.out.println("Converted " + i + " to LINK");
			return this.branchLinkRegister;
		} else if (i < this.registerCount) {
			if (SimulatorController.verbose) System.out.println("Found register " + i);
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
		
		str.append("Stack Pointer:\n---\t");
		str.append(stackRegister.getValue() + "\t");
		str.append(stackRegister.getNumericalValue());
		str.append("\n");

		str.append("Last Instruction Executed:\n");
		str.append(lastInstruction);
		
		str.append("\nProgram Counter:\n");
		str.append(programCounter);

		str.append("\nCPU Flags:\n");
		str.append("Negative: " + this.negativeFlag);
		str.append("\nZero: " + this.zeroFlag);
		str.append("\nOverflow: " + this.overflowFlag);
		str.append("\nCarry: " + this.carryFlag);
		str.append("\nHalted: " + this.isHalted);

		return str.toString();
	}
}
