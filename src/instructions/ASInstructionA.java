package instructions;
import java.util.Scanner;

import common.BitSet;
import common.Constants;
import common.IllegalRegisterException;
import common.NumberTools;

/**
 * 
 * @author Ezekiel Elin
 *
 */

public class ASInstructionA extends AssemblerInstruction {
	
	/**
	 * Register
	 */
	private int r1 = 0;
	
	/**
	 * Register
	 */
	private int r2 = 0;
	
	/**
	 * Register
	 */
	private int r3 = 0;
	
	public ASInstructionA(String token, Scanner scanner) throws IllegalRegisterException {
		this.token = token;
		
		String r1String = scanner.next();
		String r2String = scanner.next();
		String r3String = scanner.next();
		
		this.r1 = (int) NumberTools.parseNumber(r1String.substring(1));
		this.r2 = (int) NumberTools.parseNumber(r2String.substring(1));
		this.r3 = (int) NumberTools.parseNumber(r3String.substring(1));
		
		AssemblerInstruction.checkRegister(r1);
		AssemblerInstruction.checkRegister(r2);
		AssemblerInstruction.checkRegister(r3);
	}
	
	public String sourceStringRepresentation() {
		return "" + this.token + " R" + this.r1 + " R" + this.r2 + " R"  + this.r3;
	}

	@Override
	public String binaryStringRepresentation() {
		String r1String = NumberTools.numberToBinaryString(this.r1, Constants.REGISTER_LENGTH);
		String r2String = NumberTools.numberToBinaryString(this.r2, Constants.REGISTER_LENGTH);
		String r3String = NumberTools.numberToBinaryString(this.r3, Constants.REGISTER_LENGTH);
		
		String instruction = this.opcodeBinaryString() + r1String + r2String + r3String;
		return NumberTools.rpad(instruction, '0', Constants.INSTRUCTION_LENGTH);
	}
}
