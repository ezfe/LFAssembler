package instructions;

import java.util.Optional;
import java.util.Scanner;

import assembler.LabelInstruction;
import common.BinaryOperations;
import common.BinaryOperationsResult;
import common.Constants;
import common.NumberTools;
import simulator.SimulatorRegister;
import simulator.SimulatorState;

public class ASInstructionG extends BranchingInstruction implements LabelInstruction {
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
		this.labelAddress = Optional.of((int) NumberTools.binaryStringToNumber(binaryRepresentation.substring(start, end)));
	}
	
	@Override
	public String sourceStringRepresentation() {
		if (this.labelValue.isPresent()) {
			return "" + this.token + " " + this.labelValue.get();
		} else if (this.labelAddress.isPresent()) {
			return "" + this.token + " 0x" + Integer.toHexString(this.labelAddress.get());
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
			branchWith(state, labelAddress);
		} else if (this.token.equals("BL")) {
			state.branchLinkRegister.setValue(NumberTools.numberToBinaryString(state.programCounter, state.registerSize));
			branchWith(state, labelAddress);
			System.out.println("Set link to " + state.branchLinkRegister.getNumericalValue());
		} else if (this.token.equals("B.EQ")) {
			/* Z = 1 */
			if (state.zeroFlag) branchWith(state, this.labelAddress);
		} else if (this.token.equals("B.NE")) {
			/* Z = 0 */
			if (!state.zeroFlag) branchWith(state, this.labelAddress);
		} else if (this.token.equals("B.LT")) {
			/* N != V */
			if (state.negativeFlag != state.overflowFlag) branchWith(state, this.labelAddress);
		} else if (this.token.equals("B.LE")) {
			/* ~(Z = 0 & N = V) */
			if (!(!state.zeroFlag && state.negativeFlag == state.overflowFlag)) branchWith(state, this.labelAddress);
		} else if (this.token.equals("B.GT")) {
			/* Z = 0 & N = V */
			if (!state.zeroFlag && state.negativeFlag == state.overflowFlag) branchWith(state, this.labelAddress);
		} else if (this.token.equals("B.GE")) {
			/* N = V */
			if (state.negativeFlag == state.overflowFlag) branchWith(state, this.labelAddress);
		} else if (this.token.equals("B.MI")) {
			/* N = 1 */
			if (state.negativeFlag) branchWith(state, this.labelAddress);
		} else if (this.token.equals("B.PL")) {
			/* N = 0 */
			if (!state.negativeFlag) branchWith(state, this.labelAddress);
		} else if (this.token.equals("B.VS")) {
			/* V = 1 */
			if (state.overflowFlag) branchWith(state, this.labelAddress);
		} else if (this.token.equals("B.VC")) {
			/* V = 0 */
			if (!state.overflowFlag) branchWith(state, this.labelAddress);
		} else {
			System.out.println(this.token + " is unimplemented");
		}
	}
}
