package instructions;

import java.util.Scanner;

import common.Constants;
import common.IllegalRegisterException;
import common.NumberTools;

public class ASInstructionG extends AssemblerInstruction {
	/**
	 * Memory address literal
	 */
	private int r1 = 0;
	
	public ASInstructionG(String token, Scanner scanner) throws IllegalRegisterException {
		this.token = token;
		
		this.r1 = (int) NumberTools.parseNumber(scanner.next());
	}
	
	@Override
	public String sourceStringRepresentation() {
		return "" + this.token + " " + this.r1;
	}

	@Override
	public String binaryStringRepresentation() {
		String r1String = NumberTools.numberToBinaryString(this.r1, Constants.MEMADDR_LENGTH);
		String instruction = this.opcodeBinaryString() + r1String;
		return NumberTools.rpad(instruction, '0', Constants.INSTRUCTION_LENGTH);
	}

}
