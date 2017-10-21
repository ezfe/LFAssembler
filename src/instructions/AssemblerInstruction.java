package instructions;

import common.Constants;
import common.IllegalRegisterException;
import common.NumberTools;
import common.Token;

/**
 * 
 * @author Ezekiel Elin
 *
 */

public abstract class AssemblerInstruction implements Token {
	protected String token;
	
	public abstract String sourceStringRepresentation();
	public abstract String binaryStringRepresentation();
	
	@Override
	public String toString() {
		return "Instruction{" + this.sourceStringRepresentation() + "}";
	}
	
	public Integer opcode() {
		return ASInstructionClassifier.getOpcode(this.token).orElse(0);
	}
	
	public String opcodeBinaryString() {
		return NumberTools.numberToBinaryString(this.opcode(), Constants.OPCODE_LENGTH);
	}
	
	public static void checkRegister(Integer register) throws IllegalRegisterException {
		if (register < 0) {
			throw new IllegalRegisterException("Registers may not be below 0 (found " + register + ")");
		} else if (register >= 32) {
			throw new IllegalRegisterException("Registers may not be above 31 (found " + register + ")");
		}
	}
	
	public static String transformRegister(String register) {
		if (register.equals("ZERO")) {
			return "R" + Constants.ZERO_REGISTER_NUMBER; 
		} else if (register.equals("LINK")) {
			return "R" + Constants.LINK_REGISTER_NUMBER;
		} else {
			return register;
		}
	}
}
