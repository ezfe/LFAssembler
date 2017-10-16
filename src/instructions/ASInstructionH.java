package instructions;

import java.util.Scanner;

import common.Constants;
import common.IllegalRegisterException;
import common.NumberTools;

public class ASInstructionH extends AssemblerInstruction {

	/**
	 * Register
	 */
	private int r1 = 0;
	
	public ASInstructionH(String token, Scanner scanner) throws IllegalRegisterException {
		this.token = token;
		
		this.r1 = (int) NumberTools.parseNumber(scanner.next().substring(1));
	}

	
	@Override
	public String sourceStringRepresentation() {
		return "" + this.token + " R" + this.r1;
	}

	@Override
	public String binaryStringRepresentation() {
		String r1String = NumberTools.numberToBinaryString(this.r1, Constants.REGISTER_LENGTH);
		
		String instruction = this.opcodeBinaryString() + r1String;
		return NumberTools.rpad(instruction, '0', Constants.INSTRUCTION_LENGTH);
	}

}
