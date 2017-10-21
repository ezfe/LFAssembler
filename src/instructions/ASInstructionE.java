package instructions;

import java.util.Optional;
import java.util.Scanner;

import assembler.LabelInstruction;
import common.IllegalRegisterException;
import common.NumberTools;
import simulator.SimulatorRegister;
import simulator.SimulatorState;
import common.BinaryOperations;
import common.Constants;

/**
 * Represents an E-format instruction
 * @author Ezekiel Elin
 *
 */
public class ASInstructionE extends BranchingInstruction implements LabelInstruction {
	/**
	 * Register
	 */
	private int registerNumber = 0;
	
	/**
	 * Memory address label
	 */
	private Optional<String> labelValue;
	
	/**
	 * Memory address
	 */
	private Optional<Integer> labelAddress;
	
	public ASInstructionE(String token, Scanner scanner) throws IllegalRegisterException {
		this.token = token;
		
		this.registerNumber = (int) NumberTools.parseNumber(scanner.next().substring(1));
		this.labelValue = Optional.ofNullable(scanner.next());
		this.labelAddress = Optional.empty();
		
		AssemblerInstruction.checkRegister(registerNumber);
	}

	public ASInstructionE(String token, String binaryRepresentation) {
		this.token = token;
		
		this.labelValue = Optional.empty();
		
		int start = Constants.OPCODE_LENGTH;
		int end = start + Constants.REGISTER_LENGTH;
		this.registerNumber = NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end));
		
		start = end;
		end += Constants.MEMADDR_LENGTH;
		this.labelAddress = Optional.of(NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end)));
	}
	
	@Override
	public String sourceStringRepresentation() {
		if (this.labelValue.isPresent()) {
			return "" + this.token + " R" + this.registerNumber + " " + this.labelValue.get();
		} else if (this.labelAddress.isPresent()) {
			return "" + this.token + " R" + this.registerNumber + " 0x" + Integer.toHexString(this.labelAddress.get());
		} else {
			throw new IllegalStateException("E-format instructions must have either a label address or location");
		}
	}

	@Override
	public String binaryStringRepresentation() {
		String registerBinaryString = NumberTools.numberToBinaryString(this.registerNumber, Constants.REGISTER_LENGTH);
		String addressBinarySrtring = NumberTools.numberToBinaryString(this.labelAddress.orElse(0), Constants.MEMADDR_LENGTH);
		
		String instruction = this.opcodeBinaryString() + registerBinaryString + addressBinarySrtring;
		return NumberTools.rpad(instruction, '0', Constants.INSTRUCTION_LENGTH);
	}

	@Override
	public int getLabelBitIndex() {
		/* Will return 7 + 5 (12) which is the first bit of the MEMADDR */
		return Constants.OPCODE_LENGTH + Constants.REGISTER_LENGTH;
	}

	@Override
	public String getLabel() {
		return labelValue.get();
	}
	
	@Override
	public void perform(SimulatorState state) {
		super.perform(state);
		SimulatorRegister register = state.getRegister(this.registerNumber);

		if (this.token.equals("CBZ")) {
			if (BinaryOperations.isZero(register.getValue())) {
				branchWith(state, this.labelAddress);
			}
		} else if (this.token.equals("CBNZ")) {
			if (!BinaryOperations.isZero(register.getValue())) {
				branchWith(state, this.labelAddress);
			}
		} else if (this.token.equals("MOVZ")) {
			register.setValue(NumberTools.numberToBinaryString(this.labelAddress.orElse(0), state.registerSize));
		}
	}
}
