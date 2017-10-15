package instructions;
import java.util.Scanner;

import common.Constants;
import common.IllegalRegisterException;
import common.NumberTools;

public class ASInstructionD extends AssemblerInstruction {

	private Integer r1 = 0;
	private Integer r2 = 0;
	private Integer r3 = 0;
	
	public ASInstructionD(String token, Scanner scanner) throws IllegalRegisterException {
		this.token = token;
		
		String r1String = scanner.next();
		String r2String = scanner.next();
		String r3String = scanner.next();
		
		if (r3String.charAt(0) != '#') {
			System.err.println("r3 must be constant");
			assert false;
		}
		
		this.r1 = Integer.parseInt(r1String.substring(1));
		this.r2 = Integer.parseInt(r2String.substring(2));
		this.r3 = Integer.parseInt(r3String.substring(1, r3String.length() - 1));
		
		AssemblerInstruction.checkRegister(r1);
		AssemblerInstruction.checkRegister(r2);
	}
	
	@Override
	public String sourceStringRepresentation() {
		return "" + this.token + " R" + this.r1 + " [R" + this.r2 + " #"  + this.r3 + "]";
	}

	@Override
	public String binaryStringRepresentation() {
		String r1String = NumberTools.numberToBinaryString(this.r1, Constants.REGISTER_LENGTH);
		String r2String = NumberTools.numberToBinaryString(this.r2, Constants.REGISTER_LENGTH);
		String r3String = NumberTools.numberToBinaryString(this.r3, Constants.LITERAL_LENGTH);
		
		String instruction = this.opcodeBinaryString() + r1String + r2String + r3String;
		return NumberTools.rpad(instruction, '0', Constants.INSTRUCTION_LENGTH);
	}
}
