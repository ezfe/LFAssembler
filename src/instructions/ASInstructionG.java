package instructions;

import java.util.Scanner;

import assembler.LabelInstruction;
import common.Constants;
import common.IllegalRegisterException;
import common.NumberTools;

public class ASInstructionG extends AssemblerInstruction implements LabelInstruction {
	/**
	 * Memory address label
	 */
	private String r1;
	
	public ASInstructionG(String token, Scanner scanner) throws IllegalRegisterException {
		this.token = token;
		
		this.r1 = scanner.next();
	}
	
	@Override
	public String sourceStringRepresentation() {
		return "" + this.token + " " + this.r1;
	}

	@Override
	public String binaryStringRepresentation() {
		String r1String = NumberTools.numberToBinaryString(/*this.r1*/0, Constants.MEMADDR_LENGTH);
		String instruction = this.opcodeBinaryString() + r1String;
		return NumberTools.rpad(instruction, '0', Constants.INSTRUCTION_LENGTH);
	}

	@Override
	public int getLabelBitIndex() {
		/* Will return 7, which is the bit index for the first bit of the MEMADDR */
		return Constants.OPCODE_LENGTH;
	}

	@Override
	public String getLabel() {
		return r1;
	}

}
