package instructions;
import java.util.Scanner;

import common.Constants;
import common.IllegalRegisterException;
import common.NumberTools;

/**
 * 
 * @author Ezekiel Elin
 *
 */
public class ASInstructionB extends AssemblerInstruction {

	private Integer r1 = 0;
	private Integer r2 = 0;
	private Integer r3 = 0;
	
	private Boolean r2Constant = false;
	private Boolean r3Constant = false;
	
	public ASInstructionB(String token, Scanner scanner) throws IllegalRegisterException {
		this.token = token;
		
		String r1String = scanner.next();
		String r2String = scanner.next();
		String r3String = scanner.next();
		
		r2Constant = (r2String.charAt(0) == '#');
		r3Constant = (r3String.charAt(0) == '#');
		
		if ((r2Constant && r3Constant) || !(r2Constant || r3Constant)) {
			System.err.println("Cannot have two or zero constants");
			assert false;
		}
		
		this.r1 = Integer.parseInt(r1String.substring(1));
		this.r2 = Integer.parseInt(r2String.substring(1));
		this.r3 = Integer.parseInt(r3String.substring(1));
		
		AssemblerInstruction.checkRegister(r1);
		if (!r2Constant) AssemblerInstruction.checkRegister(r2);
		if (!r3Constant) AssemblerInstruction.checkRegister(r3);
	}
	
	@Override
	public String sourceStringRepresentation() {
		return "" + this.token + " R" + this.r1 + " " + (this.r2Constant ? "#" : "R") + this.r2 + " "  + (this.r3Constant ? "#" : "R") + this.r3;
	}

	@Override
	public String binaryStringRepresentation() {
		String r1String = NumberTools.numberToBinaryString(this.r1, Constants.REGISTER_LENGTH);
		String r2String = NumberTools.numberToBinaryString(this.r2, this.r2Constant ? Constants.LITERAL_LENGTH : Constants.REGISTER_LENGTH);
		String r3String = NumberTools.numberToBinaryString(this.r3, this.r3Constant ? Constants.LITERAL_LENGTH : Constants.REGISTER_LENGTH);
		
		String instruction = null;
		if (this.r2Constant) {
			instruction = this.opcodeBinaryString() + r1String + "1" + r3String + r2String;
		} else if (this.r3Constant) {
			instruction = this.opcodeBinaryString() + r1String + "0" + r2String + r3String;
		} else {
			System.err.println("Cannot have two or zero constants");
			assert false;
		}
		
		return NumberTools.rpad(instruction, '0', Constants.INSTRUCTION_LENGTH);
	}

}
