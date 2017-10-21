package instructions;

import java.util.Scanner;

import common.BinaryOperations;
import common.BinaryOperationsResult;
import common.Constants;
import common.IllegalRegisterException;
import common.NumberTools;
import simulator.SimulatorRegister;
import simulator.SimulatorState;

public class ASInstructionH extends BranchingInstruction {

	/**
	 * Register
	 */
	private int registerNumber = 0;
	
	public ASInstructionH(String token, Scanner scanner) throws IllegalRegisterException {
		this.token = token;
		
		String r1String = AssemblerInstruction.transformRegister(scanner.next());
		this.registerNumber = (int) NumberTools.parseNumber(r1String.substring(1));
	}
	
	public ASInstructionH(String token, String binaryRepresentation) {
		this.token = token;
		
		int start = Constants.OPCODE_LENGTH;
		int end = start + Constants.REGISTER_LENGTH;
		this.registerNumber = (int) NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));
	}

	
	@Override
	public String sourceStringRepresentation() {
		return "" + this.token + " R" + this.registerNumber;
	}

	@Override
	public String binaryStringRepresentation() {
		String r1String = NumberTools.numberToBinaryString(this.registerNumber, Constants.REGISTER_LENGTH);
		
		String instruction = this.opcodeBinaryString() + r1String;
		return NumberTools.rpad(instruction, '0', Constants.INSTRUCTION_LENGTH);
	}

	@Override
	public void perform(SimulatorState state) {
		super.perform(state);
		SimulatorRegister register = state.getRegister(this.registerNumber);
		
		if (this.token.equals("BR")) {
			this.branchWith(state, (int) register.getNumericalValue());
		} else if (this.token.equals("PUSH")) {
			/* STUR */
			long memaddr = state.stackRegister.getNumericalValue();
			state.memory.writeBytes((int) memaddr, NumberTools.forcelpad(register.getValue(), '0', 8 * 8));

			/* ADDI */
			BinaryOperationsResult val = BinaryOperations.add(state.stackRegister.getValue(), NumberTools.forcelpad("1000", '0', state.registerSize));
			state.stackRegister.setValue(val.result);			
		} else if (this.token.equals("POP")) {
			/* SUBI */
			BinaryOperationsResult val = null;
			val = BinaryOperations.subtract(state.stackRegister.getValue(), NumberTools.forcelpad("1000", '0', state.registerSize));
			state.stackRegister.setValue(val.result);
			
			/* LDUR */
			long memaddr = state.stackRegister.getNumericalValue();
			String found = state.memory.readBytes((int) memaddr, 8);
			register.setValue(found);			
		}
	}
}
