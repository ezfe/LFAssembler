package common;

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
}
