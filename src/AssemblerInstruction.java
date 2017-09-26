import java.util.ArrayList;

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
}
