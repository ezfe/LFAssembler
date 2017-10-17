package instructions;

import java.util.Scanner;

import assembler.LabelInstruction;
import common.IllegalRegisterException;
import common.NumberTools;
import common.Constants;

/**
 * Represents an E-format instruction
 * @author Ezekiel Elin
 *
 */
public class ASInstructionE extends AssemblerInstruction implements LabelInstruction {
	/**
	 * Register
	 */
	private int r1 = 0;
	
	/**
	 * Memory address label
	 */
	private String r2;
	
	public ASInstructionE(String token, Scanner scanner) throws IllegalRegisterException {
		this.token = token;
		
		this.r1 = (int) NumberTools.parseNumber(scanner.next().substring(1));
		this.r2 = scanner.next();//(int) NumberTools.parseNumber(scanner.next());
		
		AssemblerInstruction.checkRegister(r1);
	}
	
	@Override
	public String sourceStringRepresentation() {
		return "" + this.token + " R" + this.r1 + " " + this.r2;
	}

	@Override
	public String binaryStringRepresentation() {
		String r1String = NumberTools.numberToBinaryString(this.r1, Constants.REGISTER_LENGTH);
		String r2String = NumberTools.numberToBinaryString(/*this.r2*/0, Constants.MEMADDR_LENGTH);
		
		String instruction = this.opcodeBinaryString() + r1String + r2String;
		return NumberTools.rpad(instruction, '0', Constants.INSTRUCTION_LENGTH);
	}

	@Override
	public int getLabelBitIndex() {
		/* Will return 7 + 5 (12) which is the first bit of the MEMADDR */
		return Constants.OPCODE_LENGTH + Constants.REGISTER_LENGTH;
	}

	@Override
	public String getLabel() {
		return r2;
	}
}
