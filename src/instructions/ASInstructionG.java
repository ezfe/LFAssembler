package instructions;

import java.util.Optional;
import java.util.Scanner;

import assembler.LabelInstruction;
import common.Constants;
import common.NumberTools;
import simulator.BinaryOperations;
import simulator.BinaryOperationsResult;
import simulator.SimulatorRegister;
import simulator.SimulatorState;

public class ASInstructionG extends PerformableInstruction implements LabelInstruction {
	/**
	 * Memory address label
	 */
	private Optional<String> labelValue;
	
	/**
	 * Memory address
	 */
	private Optional<Integer> labelAddress;
	
	public ASInstructionG(String token, Scanner scanner) {
		this.token = token;
		
		this.labelValue = Optional.ofNullable(scanner.next());
		this.labelAddress = Optional.empty();
	}
	
	public ASInstructionG(String token, String binaryRepresentation) {
		this.token = token;
		
		this.labelValue = Optional.empty();
		
		int start = Constants.OPCODE_LENGTH;
		int end = start + Constants.MEMADDR_LENGTH;
		this.labelAddress = Optional.of(NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end)));
	}
	
	@Override
	public String sourceStringRepresentation() {
		if (this.labelValue.isPresent()) {
			return "" + this.token + " " + this.labelValue.get();
		} else if (this.labelAddress.isPresent()) {
			return "" + this.token + " " + Integer.toHexString(this.labelAddress.get());
		} else {
			throw new IllegalStateException("G-format instructions must have either a label address or location");
		}
	}

	@Override
	public String binaryStringRepresentation() {
		//If we have an address, fill it in. Otherwise, fill in zeros.
		String addressBinaryString = NumberTools.numberToBinaryString(this.labelAddress.orElse(0), Constants.MEMADDR_LENGTH);
		String instruction = this.opcodeBinaryString() + addressBinaryString;
		return NumberTools.rpad(instruction, '0', Constants.INSTRUCTION_LENGTH);
	}

	@Override
	public int getLabelBitIndex() {
		/* Will return 7, which is the bit index for the first bit of the MEMADDR */
		return Constants.OPCODE_LENGTH;
	}

	@Override
	public String getLabel() {
		return labelValue.get();
	}
	
	@Override
	public void perform(SimulatorState state) {
		super.perform(state);

		if (this.token.equals("B")) {
			if (this.labelAddress.isPresent()) {
				state.programCounter = this.labelAddress.get();
			} else {
				throw new IllegalStateException("No labelAddress found");
			}
		} else if (this.token.equals("BL")) {
			//TODO
		} else if (this.token.equals("B.EQ")) {
			//TODO
		} else if (this.token.equals("B.NE")) {
			//TODO
		} else if (this.token.equals("B.LT")) {
			//TODO
		} else if (this.token.equals("B.LE")) {
			//TODO
		} else if (this.token.equals("B.GT")) {
			//TODO
		} else if (this.token.equals("B.GE")) {
			//TODO
		} else if (this.token.equals("B.MI")) {
			//TODO
		} else if (this.token.equals("B.PL")) {
			//TODO
		} else if (this.token.equals("B.VS")) {
			//TODO
		} else if (this.token.equals("B.VC")) {
			//TODO
		} else {
			System.out.println(this.token + " is unimplemented");
		}
	}
}
