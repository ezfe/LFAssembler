package instructions;

import java.util.Scanner;

import common.IllegalRegisterException;
import common.NumberTools;
import common.Constants;

/**
 * Represents an E-format instruction
 * @author Ezekiel Elin
 *
 */
public class ASInstructionE extends AssemblerInstruction {	
	private int r1 = 0;
	private long r2 = 0;
	
	public ASInstructionE(String token, Scanner scanner) throws IllegalRegisterException {
		this.token = token;
		
		String r1String = scanner.next();
		String r2String = scanner.next();
		
		this.r1 = Integer.parseInt(r1String.substring(1));
		this.r2 = NumberTools.parseNumber(r2String);
		
		AssemblerInstruction.checkRegister(r1);
	}
	
	@Override
	public String sourceStringRepresentation() {
		return "" + this.token + " R" + this.r1 + " " + this.r2;
	}

	@Override
	public String binaryStringRepresentation() {
		String r1String = NumberTools.numberToBinaryString(this.r1, Constants.REGISTER_LENGTH);
		String r2String = NumberTools.numberToBinaryString(this.r2, Constants.MEMADDR_LENGTH);
		
		String instruction = this.opcodeBinaryString() + r1String + r2String;
		return NumberTools.rpad(instruction, '0', Constants.INSTRUCTION_LENGTH);
	}
}
